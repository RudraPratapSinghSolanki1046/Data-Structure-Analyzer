#!/usr/bin/env python3
"""Generates javacode.js from the tested files in java/.
Lines ending in `// @marker` tag the Java statements that a visualization event corresponds to.
The marker comment is removed from the displayed code, so line numbers stay identical to the .java file."""
import re, json, os, sys
ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
CLS = {'bubble': 'BubbleSort', 'selection': 'SelectionSort', 'insertion': 'InsertionSort', 'merge': 'MergeSort',
       'quick': 'QuickSort', 'heap': 'HeapSort', 'linear': 'LinearSearch', 'binary': 'BinarySearch'}
# lineEv: pseudocode line index used by the JavaScript step generators -> event name (events may also be set directly as step.ev)
# evMarks: event name -> Java markers to highlight (defaults to the marker with the same name)
META = {
 'bubble': dict(lineEv={2: 'compare', 3: 'swap', 1: 'passEnd'}, evMarks={},
  about='Compares neighbouring values and swaps them when the left one is larger, so the largest remaining value moves to the right end after every pass. The swapped flag lets the method stop as soon as a full pass makes no swaps.',
  best='O(n)', avg='O(n²)', worst='O(n²)', space='O(1)',
  why='Two nested loops compare up to n(n − 1)/2 pairs, which is quadratic. On already sorted input the first pass makes no swap and the swapped flag ends the method after n − 1 comparisons. Only a few local variables are used, so extra space is constant.'),
 'selection': dict(lineEv={3: 'compare', 4: 'swap', 0: 'passEnd'}, evMarks={},
  about='Each pass scans the unsorted part for the smallest value (minIdx) and swaps it into position i. It always scans the whole unsorted part, but makes at most n − 1 swaps.',
  best='O(n²)', avg='O(n²)', worst='O(n²)', space='O(1)',
  why='The inner loop always runs to the end of the array, so about n(n − 1)/2 comparisons happen no matter how the data is ordered. Sorting is done in place with a few variables.'),
 'insertion': dict(lineEv={2: 'compare', 3: 'swap', 0: 'passEnd'}, evMarks={},
  about='Keeps arr[0..i-1] sorted and moves arr[i] left, swapping with larger neighbours, until it reaches its place. This version swaps adjacent values so each swap can be shown in the visualization.',
  best='O(n)', avg='O(n²)', worst='O(n²)', space='O(1)',
  why='On sorted input each value is compared once with its left neighbour and the inner loop stops at once. On reversed input every value moves all the way left, giving about n²/2 swaps. It works in place.'),
 'merge': dict(lineEv={0: 'divide', 2: 'compare', 4: 'merged'}, evMarks={},
  about='Splits the array at the middle, sorts both halves recursively, then merges the two sorted halves into the temp array and copies the result back. Taking from the left half on ties (<=) keeps the sort stable.',
  best='O(n log n)', avg='O(n log n)', worst='O(n log n)', space='O(n)',
  why='The array is halved about log n times and each level merges n values in total. The temp array of size n is needed for merging, and the recursion adds O(log n) stack frames.'),
 'quick': dict(lineEv={1: 'pivot', 2: 'compare', 3: 'swapLeft', 4: 'placePivot'}, evMarks={},
  about='Uses the last value as the pivot and moves every smaller value to its left (Lomuto partition), then places the pivot at its final index and recursively sorts the two sides.',
  best='O(n log n)', avg='O(n log n)', worst='O(n²)', space='O(log n)',
  why='Balanced partitions give log n levels of n comparisons. If the pivot is always the smallest or largest value (for example an already sorted array with a last-element pivot) the partitions are lopsided and the work becomes n². Extra space is the recursion stack, O(log n) on average.'),
 'heap': dict(lineEv={0: 'build', 3: 'extractDone'}, evMarks={},
  about='Builds a max heap, then repeatedly swaps the root (largest value) to the end of the heap and sifts the new root down with heapify. The sorted values accumulate at the right end.',
  best='O(n log n)', avg='O(n log n)', worst='O(n log n)', space='O(1)',
  why='Building the heap costs O(n); each of the n − 1 extractions does one heapify that walks at most log n levels. Heapify is recursive here, so the call stack uses O(log n); an iterative version needs O(1).'),
 'linear': dict(lineEv={1: 'check', 2: 'found', 3: 'notFound'}, evMarks={'found': ['check', 'found']},
  about='Checks each element from left to right and returns the index of the first match, or -1 when the loop ends without a match. It works on unsorted data.',
  best='O(1)', avg='O(n)', worst='O(n)', space='O(1)',
  why='The best case finds the target at index 0. On average about half the array is checked, and in the worst case the target is last or missing so all n elements are checked.'),
 'binary': dict(lineEv={3: 'found', 4: 'goRight', 5: 'goLeft', 6: 'notFound'},
  evMarks={'found': ['probe', 'found'], 'goRight': ['probe', 'goRight'], 'goLeft': ['probe', 'goLeft']},
  about='Requires sorted input. Compares the middle value with the target and discards the half that cannot contain it, so the range [low, high] halves on every probe. mid is computed as low + (high - low) / 2 to avoid integer overflow.',
  best='O(1)', avg='O(log n)', worst='O(log n)', space='O(1)',
  why='The best case hits the middle value on the first probe. Each probe halves the range, so at most about log₂(n) probes are needed. The loop is iterative and uses constant extra space.'),
}

CLS.update({'ref_array': 'ArrayOperations', 'ref_deque': 'CircularDeque', 'ref_pq': 'MinPriorityQueue', 'ref_tree': 'BinaryTree'})
CLS.update({'graph_list': 'AdjacencyListGraph', 'graph_matrix': 'AdjacencyMatrixGraph'})
CLS.update({'tree_bst': 'BinarySearchTree', 'tree_avl': 'AVLTree'})
CLS.update({'stack_array': 'ArrayStack', 'stack_linked': 'LinkedStack', 'queue_array': 'ArrayQueue', 'queue_circular': 'CircularQueue',
            'queue_linked': 'LinkedQueue', 'list_singly': 'SinglyLinkedList', 'list_doubly': 'DoublyLinkedList', 'list_circular': 'CircularLinkedList'})
O1 = 'O(1)'
def r(op, b, a, w, sp=O1): return [op, b, a, w, sp]
LIST_COMMON = lambda tail_o1, circ: [
    r('insertHead', O1, O1, O1), r('insertTail', O1, O1 if tail_o1 else 'O(n)', O1 if tail_o1 else 'O(n)'),
    r('insertAt', O1, 'O(n)', 'O(n)'), r('deleteHead', O1, O1, O1),
    r('deleteTail', O1, O1 if tail_o1 == 'both' else 'O(n)', O1 if tail_o1 == 'both' else 'O(n)'),
    r('deleteAt', O1, 'O(n)', 'O(n)'), r('search', O1, 'O(n)', 'O(n)'), r('reverse', 'O(n)', 'O(n)', 'O(n)'), r('traverse', 'O(n)', 'O(n)', 'O(n)')]
META.update({
 'stack_array': dict(kind='ops', lineEv={}, evMarks={}, rows=[r('push', O1, O1, O1), r('pop', O1, O1, O1), r('peek', O1, O1, O1)], space='O(n) for the array',
  about='A fixed-capacity stack stored in an array. top is the index of the newest element (-1 when empty). push increments top and pop decrements it, so the last value pushed is the first one popped. Overflow and underflow throw IllegalStateException.',
  why='Every operation reads or writes only data[top] and updates one counter, so the cost does not depend on how many values are stored. The only storage is the array of capacity n.'),
 'stack_linked': dict(kind='ops', lineEv={}, evMarks={}, rows=[r('push', O1, O1, O1), r('pop', O1, O1, O1), r('peek', O1, O1, O1)], space='O(n) for the nodes',
  about='A stack built from immutable nodes. head is the top: push creates a node that points at the old head, and pop moves head to head.next. There is no capacity limit.',
  why='Each operation changes only the head reference and the size counter. Each stored value costs one node, so total space grows with the number of values, but each operation needs O(1) extra space.'),
 'queue_array': dict(kind='ops', lineEv={}, evMarks={}, rows=[r('enqueue', O1, O1, O1), r('dequeue', O1, 'O(n)', 'O(n)'), r('peek', O1, O1, O1)], space='O(n) for the array',
  about='A queue in a fixed array whose front is always index 0. enqueue writes at index size; dequeue returns data[0] and shifts every remaining value one place left. Simple, but dequeue is slow for large queues; see CircularQueue.',
  why='enqueue writes one slot. dequeue copies the remaining size − 1 values, so it costs O(n) in general and O(1) only when the queue holds a single value.'),
 'queue_circular': dict(kind='ops', lineEv={}, evMarks={}, rows=[r('enqueue', O1, O1, O1), r('dequeue', O1, O1, O1), r('peek', O1, O1, O1)], space='O(n) for the array',
  about='A queue in a fixed array that wraps around. front is the index of the first value and the next free slot is (front + size) % capacity, so slots freed by dequeue are reused and nothing is shifted.',
  why='Both ends move by changing one index with the % operator and the size counter, so no values are copied. Storage is the fixed array of capacity n.'),
 'queue_linked': dict(kind='ops', lineEv={}, evMarks={}, rows=[r('enqueue', O1, O1, O1), r('dequeue', O1, O1, O1), r('peek', O1, O1, O1)], space='O(n) for the nodes',
  about='A queue made of linked nodes with references to both head (front) and tail (rear). enqueue appends after tail and dequeue removes the head; when the last node is removed, tail is reset to null.',
  why='Keeping a tail reference means enqueue never walks the list, and dequeue only moves head. Each stored value costs one node.'),
 'list_singly': dict(kind='ops', lineEv={}, evMarks={}, rows=LIST_COMMON(False, False), space='O(n) for the nodes',
  about='Each node stores a value and a next reference; only head is kept. Operations at the head are O(1), but reaching the tail or an index means walking from the head. reverse relinks every node in one pass.',
  why='insertHead and deleteHead touch only head. Anything at the tail or at an index must follow next references, which costs up to n steps. search, reverse and traverse visit every node once, and they need only a few extra references.'),
 'list_doubly': dict(kind='ops', lineEv={}, evMarks={}, rows=LIST_COMMON(True, False), space='O(n) for the nodes',
  about='Each node has prev and next references and the list keeps both head and tail, so both ends can be inserted into and deleted from in O(1). reverse swaps prev and next in every node, then swaps head and tail.',
  why='Head and tail are directly reachable and prev lets deleteTail step back without a walk, so end operations are O(1). Indexed operations still walk up to n nodes. Every node stores two references.'),
 'list_circular': dict(kind='ops', lineEv={}, evMarks={}, rows=LIST_COMMON(True, True), space='O(n) for the nodes',
  about='A singly linked circular list that stores only tail; tail.next is the head, so the last node points back to the first. Both insertHead and insertTail are O(1). Traversals must stop after size nodes because there is no null end.',
  why='tail gives O(1) access to both ends, so insertHead, insertTail and deleteHead are constant time. deleteTail must find the node before tail, which takes up to n steps, as do indexed operations.'),
})

META.update({
 'tree_bst': dict(kind='ops', lineEv={}, evMarks={}, rows=[r('insert', O1, 'O(log n)', 'O(n)', 'O(h)'), r('search', O1, 'O(log n)', 'O(n)', O1), r('delete', O1, 'O(log n)', 'O(n)', 'O(h)'), r('inorder / preorder / postorder', 'O(n)', 'O(n)', 'O(n)', 'O(h)'), r('levelOrder', 'O(n)', 'O(n)', 'O(n)', 'O(w)')], space='O(n) for the nodes',
  about='A binary search tree keeps smaller values in the left subtree and larger values in the right subtree of every node, so inorder traversal visits values in ascending order. Duplicates are ignored. Deleting a node with two children replaces it with its inorder successor (the smallest value in the right subtree).',
  why='Insert, search and delete follow one root-to-leaf path, so they cost the tree height h: about log n when the tree is balanced, but n when values arrive in sorted order and the tree becomes a chain. The recursive methods use O(h) stack space; contains is iterative. Traversals visit every node once, and levelOrder holds up to one level of nodes (width w) in its queue.'),
 'tree_avl': dict(kind='ops', lineEv={}, evMarks={'rotLL': ['rotLL', 'rotR'], 'rotRR': ['rotRR', 'rotL'], 'rotLR': ['rotLR', 'rotL', 'rotR'], 'rotRL': ['rotRL', 'rotR', 'rotL']}, rows=[r('insert', O1, 'O(log n)', 'O(log n)', 'O(log n)'), r('search', O1, 'O(log n)', 'O(log n)', O1), r('delete', O1, 'O(log n)', 'O(log n)', 'O(log n)'), r('inorder / preorder / postorder', 'O(n)', 'O(n)', 'O(n)', 'O(log n)'), r('levelOrder', 'O(n)', 'O(n)', 'O(n)', 'O(w)')], space='O(n) for the nodes',
  about='An AVL tree is a binary search tree that rebalances after every insert and delete so the heights of the two subtrees of any node differ by at most 1. rebalance computes the balance factor and applies one of four rotations: LL (right rotation), RR (left rotation), LR (left then right) or RL (right then left).',
  why='Rebalancing keeps the height at about 1.44 log2 n, so insert, search and delete are O(log n) even in the worst case. A rotation relinks a constant number of nodes in O(1). The recursive methods use O(log n) stack space; traversals visit all n nodes.'),
})

META.update({
 'graph_list': dict(kind='ops', lineEv={}, evMarks={'bfsVisit': ['bfsVisit', 'bfsExpand'], 'dfsVisit': ['dfsVisit', 'dfsExpand'], 'dijkSettle': ['dijkSettle', 'dijkRelax'], 'primAdd': ['primPick', 'primAdd']},
  rows=[r('addVertex', O1, O1, O1), r('addEdge', O1, O1, O1), r('bfs', 'O(V)', 'O(V + E)', 'O(V + E)', 'O(V)'), r('dfs', 'O(V)', 'O(V + E)', 'O(V + E)', 'O(V + E)'),
        r('dijkstra', 'O(V)', 'O(V²)', 'O(V²)', 'O(V)'), r('prim', 'O(V)', 'O(V·E)', 'O(V·E)', 'O(V)'), r('kruskal', 'O(V + E log E)', 'O(V + E log E)', 'O(V + E log E)', 'O(V + E)')], space='O(V + E) for the adjacency lists and the edge list',
  about='Each vertex keeps a list of its outgoing edges, so visiting the neighbours of a vertex costs only its degree. The class also keeps one list of all edges for Prim and Kruskal. bfs uses a queue, dfs an explicit stack, dijkstra the simple O(V²) scan for the closest unsettled vertex, prim adds the cheapest edge leaving the tree each round, and kruskal sorts the edges and joins components with union-find.',
  why='bfs and dfs touch each vertex once and each adjacency entry once, so O(V + E); the visited array is O(V) even when nothing is reachable. This dijkstra scans all V vertices in each of up to V rounds, giving O(V²) (a priority queue would give O((V + E) log V)). prim rescans all E edges for each of the V − 1 vertices it adds. kruskal is dominated by sorting the E edges, and union-find with path halving is nearly constant per edge.'),
 'graph_matrix': dict(kind='ops', lineEv={}, evMarks={},
  rows=[r('addVertex', 'O(V²)', 'O(V²)', 'O(V²)', 'O(V²)'), r('addEdge', O1, O1, O1), r('removeEdge', O1, O1, O1), r('hasEdge / weight', O1, O1, O1), r('neighbours', 'O(V)', 'O(V)', 'O(V)', 'O(V)'), r('print', 'O(V²)', 'O(V²)', 'O(V²)')], space='O(V²) for the matrix',
  about='The graph is a V x V matrix where matrix[u][v] holds the weight of the edge u -> v, or -1 when there is no edge. Checking or changing a single edge is one array access, but the matrix uses O(V²) space even for a sparse graph, and finding all neighbours of a vertex means scanning a full row. The graph algorithms in this app are shown on the adjacency list version.',
  why='Edge operations index the matrix directly, so they are O(1). neighbours scans one row of V cells. addVertex must allocate a new (V + 1) x (V + 1) matrix and copy every cell, which costs O(V²). The matrix itself is O(V²) regardless of how many edges exist.'),
})

META.update({
 'ref_array': dict(kind='ops', lineEv={}, evMarks={}, rows=[r('traverse', 'O(n)', 'O(n)', 'O(n)', 'O(n)'), r('insertAt', 'O(n)', 'O(n)', 'O(n)', 'O(n)'), r('deleteAt', 'O(n)', 'O(n)', 'O(n)', 'O(n)'), r('search', O1, 'O(n)', 'O(n)', O1), r('update', O1, O1, O1)], space='O(n) for the array',
  about='A Java array has a fixed length and stores its elements next to each other, so reading or writing arr[i] is a single step. Because the length cannot change, insertAt and deleteAt build a new array and copy the elements around the changed position. The main method shows the usual ways to declare and initialize an array.',
  why='Index access and update touch one cell, so they are O(1). Traversal and linear search may visit every element. insertAt and deleteAt allocate a new array and copy n elements, so they are O(n) in every case and need O(n) extra space (an in-place shift in a larger array would use O(1) extra space).'),
 'ref_deque': dict(kind='ops', lineEv={}, evMarks={}, rows=[r('insertFront', O1, O1, O1), r('insertBack', O1, O1, O1), r('removeFront', O1, O1, O1), r('removeBack', O1, O1, O1), r('peekFront / peekBack', O1, O1, O1)], space='O(n) for the array',
  about='A double-ended queue in a fixed circular array. front is the index of the first element and the back is (front + size - 1) % capacity. Adding at the front moves front one step back (wrapping with %); every other operation changes one index and the size.',
  why='Each operation changes front or size and touches one array cell, so none of them depends on how many elements are stored. Wrapping with the % operator reuses freed slots, so nothing is ever shifted.'),
 'ref_pq': dict(kind='ops', lineEv={}, evMarks={}, rows=[r('insert', O1, O1, 'O(log n)', 'O(1) (O(n) when it grows)'), r('peek', O1, O1, O1), r('extractMin', O1, 'O(log n)', 'O(log n)')], space='O(n) for the heap array',
  about='A binary min-heap in an array: the parent of index i is (i - 1) / 2 and its children are 2i + 1 and 2i + 2, and every parent is not larger than its children, so the smallest value is always at index 0. insert adds at the end and sifts up; extractMin moves the last value to the root and sifts down. The array doubles when it is full.',
  why='The heap is a complete binary tree, so its height is about log2 n and a sift moves along one root-to-leaf path: worst case O(log n). An insert often stops after one or two comparisons, so a typical insert is O(1). extractMin always sifts the replacement root down, so it is O(log n) except for a one-element heap. Doubling the array makes growth O(1) amortized.'),
 'ref_tree': dict(kind='ops', lineEv={}, evMarks={}, rows=[r('insert', O1, 'O(n)', 'O(n)', 'O(n)'), r('search', O1, 'O(n)', 'O(n)', 'O(n)'), r('delete', 'O(n)', 'O(n)', 'O(n)', 'O(n)'), r('inorder / preorder / postorder', 'O(n)', 'O(n)', 'O(n)', 'O(log n)'), r('levelOrder', 'O(n)', 'O(n)', 'O(n)', 'O(n)')], space='O(n) for the nodes',
  about='A general binary tree has no ordering rule. This implementation inserts each new value at the first free position in level order, so the tree stays complete, and deletes by copying the last node\'s value into the deleted node and removing the last node. Searching has to look at nodes level by level. The four traversals are the standard orders.',
  why='Without an ordering rule nothing can be skipped: finding the first free position, searching, and finding the last node for a delete all scan level by level, so they are O(n) (insert and search can finish early). Traversals visit each node once; the recursive ones use O(h) stack, and h is about log2 n because the tree stays complete. Level order keeps up to one level of nodes in a queue, O(n).'),
})
out, problems = {}, []
for mode, cls in CLS.items():
    raw = open(os.path.join(ROOT, 'java', cls + '.java')).read().split('\n')
    code, marks = [], {}
    for n, line in enumerate(raw, 1):
        m = re.search(r'\s*//\s*@(\w+)\s*$', line)
        if m:
            marks.setdefault(m.group(1), []).append(n); line = line[:m.start()]
        code.append(line.rstrip())
    meta = META[mode]
    for ev in meta['lineEv'].values():
        for mk in meta['evMarks'].get(ev, [ev]):
            if mk not in marks: problems.append(f'{mode}: event {ev} needs marker @{mk}')
    out[mode] = dict(file=cls + '.java', code='\n'.join(code).rstrip('\n') + '\n', marks=marks, **meta)
if problems: print('\n'.join(problems)); sys.exit(1)
open(os.path.join(ROOT, 'javacode.js'), 'w').write('/* Generated by tools/build-java.py from java/*.java. Do not edit by hand. */\nconst JAVA=' + json.dumps(out, ensure_ascii=False) + ';\n')
print('javacode.js written:', {k: sum(len(v) for v in o['marks'].values()) for k, o in out.items()})
