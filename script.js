const $=id=>document.getElementById(id);
const S={mode:'bubble',orig:[8,3,6,2,9,1,5],steps:[],i:0,playing:false,timer:null,stack:[],queue:[],list:[],quiz:false,done:new Set(),score:{r:0,n:0},lanes:[],open:false,target:0};
const CODE={
 bubble:['for i = 0 to n - 2','  for j = 0 to n - i - 2','    if array[j] > array[j + 1]','      swap(array[j], array[j + 1])','return sorted array'],
 selection:['for i = 0 to n - 2','  min = i','  for j = i + 1 to n - 1','    if array[j] < array[min]: min = j','  swap(array[i], array[min])','return sorted array'],
 insertion:['for i = 1 to n - 1','  j = i','  while j > 0 and array[j-1] > array[j]','    swap(array[j-1], array[j])','    j = j - 1','return sorted array'],
 binary:['lo = 0, hi = n - 1','while lo <= hi','  mid = floor((lo + hi) / 2)','  if array[mid] == target: return mid','  if array[mid] < target: lo = mid + 1','  else: hi = mid - 1','return not found'],
 compare:['Run all three sorts on the same values','Fewer comparisons means less checking','Fewer swaps means fewer writes'],
 list:['insertHead(v): new.next = head; head = new','insertTail(v): walk to the last node; last.next = new','deleteHead(): if head is null → underflow','  head = head.next'],
 linear:['for i = 0 to n - 1','  if array[i] == target','    return i','return not found'],
 stack:['push(value):','  add value to top','pop():','  if stack is empty → underflow','  remove and return top'],
 queue:['enqueue(value):','  add value at rear','dequeue():','  if queue is empty → underflow','  remove and return front']};
const INFO={
 bubble:['Bubble sort','Compare neighbours and swap them until the largest values settle on the right.','O(n²) time','<strong>Complexity</strong><br>Time: O(n²) average and worst case, O(n) if already sorted. Space: O(1).'],
 selection:['Selection sort','Find the smallest remaining value and place it at the front.','O(n²) time','<strong>Complexity</strong><br>Time: O(n²) in every case, but at most n−1 swaps. Space: O(1).'],
 insertion:['Insertion sort','Grow a sorted section on the left by inserting each value where it belongs.','O(n²) time','<strong>Complexity</strong><br>Time: O(n²) worst case, O(n) on nearly sorted data. Space: O(1).'],
 binary:['Binary search','Halve the sorted search range on every step.','O(log n) time','<strong>Complexity</strong><br>Time: O(log n). Space: O(1). The values must be sorted, so they are sorted for you when you run it.'],
 compare:['Compare sorts','Race three sorts on the same values and compare the work each does.','3 algorithms','<strong>Try this</strong><br>All three are O(n²), but insertion sort does far less work on nearly sorted data. Enter 1, 2, 3, 5, 4.'],
 list:['Linked list','Each node holds a value and points to the next node.','O(1) head, O(n) tail','<strong>Complexity</strong><br>Insert or delete at the head is O(1). Reaching the tail takes O(n) without a tail pointer. Space: O(n).'],
 linear:['Linear search','Check each value in order until the target turns up.','O(n) time','<strong>Complexity</strong><br>Time: O(n) in the worst case. Space: O(1). Works on unsorted data.'],
 stack:['Stack','Last in, first out (LIFO): the newest item leaves first.','O(1) per operation','<strong>Complexity</strong><br>Push and pop are O(1). Think of a stack of plates.'],
 queue:['Queue','First in, first out (FIFO): the oldest item leaves first.','O(1) per operation','<strong>Complexity</strong><br>Enqueue and dequeue are O(1) with a linked list or ring buffer. A plain array shift costs O(n).']};
const DSM=['stack','queue','list'],SEARCHES=['linear','binary'],VERB={stack:['Pushed','Popped'],queue:['Enqueued','Dequeued'],list:['Inserted at the tail','Deleted head']},NAME={stack:'stack',queue:'queue',list:'list'};
const CAT={
 sorting:{t:'Sorting',ic:'↕',d:'Understand how data gets ordered.',items:['bubble','selection','insertion','compare'],
  lead:'Sorting puts values in a chosen order, such as smallest to largest. Many tasks get faster and simpler once data is sorted, like searching, spotting duplicates or showing a ranked list.',
  analogy:'Arranging the playing cards in your hand from lowest to highest.',when:'Sort when you will search the same data many times, need ranked output, or want equal values grouped together.',
  th:['Algorithm','Best','Average','Worst','Space','Stable'],rows:[['bubble','O(n)','O(n²)','O(n²)','O(1)','Yes'],['selection','O(n²)','O(n²)','O(n²)','O(1)','No'],['insertion','O(n)','O(n²)','O(n²)','O(1)','Yes']]},
 searching:{t:'Searching',ic:'⌕',d:'Learn how to find an element efficiently.',items:['linear','binary'],
  lead:'Searching finds where a value sits in a collection, or tells you it is not there. How fast it is depends on how the data is arranged.',
  analogy:'Finding a name in a phone book: check every page, or open the middle and jump.',when:'Use linear search for small or unsorted data. Use binary search when the data is sorted and large.',
  th:['Algorithm','Best','Average','Worst','Space','Needs sorted data'],rows:[['linear','O(1)','O(n)','O(n)','O(1)','No'],['binary','O(1)','O(log n)','O(log n)','O(1)','Yes']]},
 structures:{t:'Data structures',ic:'▤',d:'See how data can be organised and accessed.',items:['stack','queue','list'],
  lead:'A data structure organises data so that certain operations are fast. Stacks, queues and linked lists differ in where items are added and removed.',
  analogy:'A stack of plates, a line at a ticket counter, and a treasure hunt where each clue points to the next.',when:'Use a stack for undo and backtracking, a queue for fair ordering of tasks, and a linked list when you insert and delete often.',
  th:['Structure','Add','Remove','Order','Space'],rows:[['stack','O(1)','O(1)','Last in, first out','O(n)'],['queue','O(1)','O(1)','First in, first out','O(n)'],['list','O(1) head, O(n) tail','O(1) head','Follows pointers','O(n)']]} };
const QB=[
['sorting','What is the worst-case time of bubble sort?','O(n²)',['O(n)','O(n log n)','O(log n)'],'It makes up to n passes with up to n comparisons each, so the work grows with n².'],
['sorting','After the first full pass of bubble sort, what is guaranteed?','The largest value is at the end',['The smallest value is at the start','The whole array is sorted','Nothing has moved'],'Each pass carries the largest remaining value to the right end.'],
['sorting','Which of these sorts makes the fewest swaps, at most n − 1?','Selection sort',['Bubble sort','Insertion sort','They all swap equally'],'Selection sort swaps once per pass, after finding the smallest value.'],
['sorting','What is the best case for insertion sort, and when does it happen?','O(n) on already sorted data',['O(n²) on sorted data','O(log n) on any data','O(n) on reversed data'],'On sorted data each value is compared once and stays put.'],
['sorting','Which pair of algorithms is stable?','Bubble sort and insertion sort',['Bubble sort and selection sort','Selection sort and insertion sort','None of them'],'Selection sort can jump equal values past each other, so it is not stable.'],
['searching','What is the worst-case time of linear search?','O(n)',['O(1)','O(log n)','O(n²)'],'In the worst case the target is last or missing, so every value is checked.'],
['searching','What must be true before you can use binary search?','The values are sorted',['The values are unique','The array has an even length','The values are positive'],'Binary search discards half the range by comparing with the middle, which only works on sorted data.'],
['searching','About how many probes does binary search need on 1,000,000 sorted values?','About 20',['About 1,000','About 500,000','About 100'],'Each probe halves the range, and log₂(1,000,000) is about 20.'],
['searching','Binary search for 7 in [1, 3, 5, 7, 9]. Which value is checked first?','5 (the middle)',['1','7','9'],'The middle index is (0 + 4) / 2 = 2, which holds 5. Then the right half is kept.'],
['structures','Which structure removes the most recently added item first?','Stack',['Queue','Linked list','Sorted array'],'A stack is last in, first out (LIFO).'],
['structures','Which ordering does a queue follow?','First in, first out',['Last in, first out','Smallest first','Random order'],'The oldest item leaves first, like a line at a ticket counter.'],
['structures','What is the time to insert at the head of a linked list?','O(1)',['O(n)','O(log n)','O(n²)'],'You only create a node and repoint head, no matter how long the list is.'],
['structures','What is it called when you pop from an empty stack?','Underflow',['Overflow','Deadlock','Null swap'],'Underflow means removing from an empty structure.'],
['structures','Which structure fits an undo feature best?','Stack',['Queue','Binary search','Bubble sort'],'The latest action should be undone first, which is last in, first out.']];
const QZ={cat:'all',stage:'start',list:[],i:0,r:0,pick:-1,opts:[],weak:{}};
const shuf=a=>{a=[...a];for(let i=a.length-1;i>0;i--){const j=Math.floor(Math.random()*(i+1));[a[i],a[j]]=[a[j],a[i]];}return a;};
const qzOpts=()=>{const q=QZ.list[QZ.i];QZ.opts=shuf([q[2],...q[3]]);QZ.pick=-1;};
function qz(a,v){
 if(a==='cat')QZ.cat=v;
 else if(a==='start'){QZ.list=shuf(QB.filter(q=>QZ.cat==='all'||q[0]===QZ.cat)).slice(0,10);QZ.i=0;QZ.r=0;QZ.weak={};QZ.stage='q';qzOpts();}
 else if(a==='ans'&&QZ.pick<0){QZ.pick=+v;const q=QZ.list[QZ.i];if(QZ.opts[QZ.pick]===q[2])QZ.r++;else QZ.weak[q[0]]=(QZ.weak[q[0]]||0)+1;}
 else if(a==='next'){if(QZ.i+1<QZ.list.length){QZ.i++;qzOpts();}else{QZ.stage='end';try{const k='dsa-best-'+QZ.cat,p=Math.round(QZ.r/QZ.list.length*100);if(p>(+localStorage.getItem(k)||0))localStorage.setItem(k,p);}catch(e){}}}
 else if(a==='menu')QZ.stage='start';
 $('page').innerHTML=quizHTML();}
function quizHTML(){
 const n=QZ.list.length;
 if(QZ.stage==='start'){let best='';try{const b=localStorage.getItem('dsa-best-'+QZ.cat);if(b)best=` · Best score: ${b}%`;}catch(e){}
  const cnt=QB.filter(q=>QZ.cat==='all'||q[0]===QZ.cat).length;
  return `<div class="panel"><h2 class="big">Quiz</h2><p class="lead">Short multiple-choice questions on sorting, searching and data structures. Choose a topic, then answer up to 10 questions with an explanation after each one.</p><div class="controls" style="margin-top:16px">${[['all','All topics'],['sorting','Sorting'],['searching','Searching'],['structures','Data structures']].map(([v,t])=>`<button class="btn chip${QZ.cat===v?' on':''}" data-qz="cat" data-v="${v}">${t}</button>`).join('')}</div><div style="margin-top:18px"><button class="btn primary" data-qz="start">Start quiz</button></div><div class="sub" style="margin-top:12px">${cnt} questions in this topic${best}</div></div>`;}
 if(QZ.stage==='q'){const q=QZ.list[QZ.i],done=QZ.pick>=0,last=QZ.i+1===n;
  return `<div class="panel"><div class="statusline" style="padding-top:0"><span>Question ${QZ.i+1} of ${n}</span><span>Score: ${QZ.r}</span></div><div class="qprog"><i style="width:${QZ.i/n*100}%"></i></div><h2 style="margin-top:18px">${q[1]}</h2>${QZ.opts.map((o,k)=>`<button class="btn opt ${done?(o===q[2]?'right':k===QZ.pick?'wrong':''):''}" data-qz="ans" data-v="${k}"${done?' disabled':''}>${esc(o)}</button>`).join('')}${done?`<div class="callout"><strong>${QZ.opts[QZ.pick]===q[2]?'Correct.':'Not quite.'}</strong> ${q[4]}</div><button class="btn primary" style="margin-top:14px" data-qz="next">${last?'See results':'Next question'}</button>`:''}</div>`;}
 const pct=Math.round(QZ.r/n*100),w=Object.entries(QZ.weak).sort((a,b)=>b[1]-a[1])[0];
 return `<div class="panel"><h2 class="big">Your result</h2><div class="score">${QZ.r} / ${n}</div><p class="lead">${pct>=80?'Great work. You understand this topic well.':pct>=50?'A good start. Review the topics below and try again.':'Keep practising. Read the concept pages, use the visualizers, then retry.'}</p><div class="controls" style="margin-top:18px"><button class="btn primary" data-qz="start">Try again</button><button class="btn" data-qz="menu">Choose a topic</button>${w?`<a class="btn" href="#/${w[0]}" style="text-decoration:none">Review ${CAT[w[0]].t}</a>`:''}</div></div>`;}
const OPEN=new Set();
let CUR='#/';try{CUR=location.hash||'#/'}catch(e){}
const esc=s=>String(s).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const fresh=a=>a.map((v,id)=>({id,v}));

function bubble(arr){let a=fresh(arr),n=a.length,st=[],c=0,s=0;
 const snap=o=>st.push({a:[...a],cmp:[],sorted:[],c,s,...o}),tail=k=>Array.from({length:k},(_,t)=>n-1-t);
 snap({line:-1,msg:'Values loaded. Press Play or Next to start comparing neighbours.'});
 for(let i=0;i<n-1;i++){let sw=false;
  for(let j=0;j<n-i-1;j++){c++;const big=a[j].v>a[j+1].v;
   snap({cmp:[j,j+1],sorted:tail(i),line:2,msg:`Compare ${a[j].v} and ${a[j+1].v}. ${big?'The left one is larger, so they swap.':'They are already in order, so nothing moves.'}`});
   if(big){[a[j],a[j+1]]=[a[j+1],a[j]];s++;sw=true;snap({cmp:[j,j+1],sorted:tail(i),swap:true,line:3,msg:`Swapped: ${a[j+1].v} moved right, ${a[j].v} moved left.`});}}
  snap({sorted:tail(i+1),line:1,msg:`Pass ${i+1} done. The largest remaining value is now at the right end.${sw?'':' No swaps happened, so the array is sorted.'}`});
  if(!sw)break;}
 snap({sorted:a.map((_,i)=>i),line:4,msg:'Sorted! The values are now in ascending order.',done:true});return st;}
function selection(arr){let a=fresh(arr),n=a.length,st=[],c=0,s=0;
 const snap=o=>st.push({a:[...a],cmp:[],sorted:[],c,s,...o}),pre=k=>Array.from({length:k},(_,t)=>t);
 snap({line:-1,msg:'Values loaded. Each pass finds the smallest remaining value.'});
 for(let i=0;i<n-1;i++){let mn=i;
  for(let j=i+1;j<n;j++){c++;const lt=a[j].v<a[mn].v;snap({cmp:[mn,j],sorted:pre(i),line:3,msg:`Compare ${a[j].v} with the current smallest, ${a[mn].v}. ${lt?`${a[j].v} is smaller, so it becomes the new smallest.`:'The smallest stays the same.'}`});if(lt)mn=j;}
  if(mn!==i){[a[i],a[mn]]=[a[mn],a[i]];s++;snap({cmp:[i,mn],sorted:pre(i),swap:true,line:4,msg:`Swap ${a[i].v} into position ${i}.`});}
  snap({sorted:pre(i+1),line:0,msg:`Pass ${i+1} done. Position ${i} holds the smallest remaining value.`});}
 snap({sorted:a.map((_,i)=>i),line:5,msg:'Sorted! The values are now in ascending order.',done:true});return st;}
function insertion(arr){let a=fresh(arr),n=a.length,st=[],c=0,s=0;
 const snap=o=>st.push({a:[...a],cmp:[],sorted:[],c,s,...o}),pre=k=>Array.from({length:k},(_,t)=>t);
 snap({sorted:n?[0]:[],line:-1,msg:'Values loaded. The first value is a sorted section of one.'});
 for(let i=1;i<n;i++){
  for(let j=i;j>0;j--){c++;const big=a[j-1].v>a[j].v;snap({cmp:[j-1,j],sorted:pre(i),line:2,msg:`Compare ${a[j-1].v} and ${a[j].v}. ${big?'The left one is larger, so the new value moves left.':'The new value is in the right place. Stop here.'}`});
   if(!big)break;[a[j-1],a[j]]=[a[j],a[j-1]];s++;snap({cmp:[j-1,j],sorted:pre(i),swap:true,line:3,msg:`Swapped: ${a[j-1].v} moved one step left.`});}
  snap({sorted:pre(i+1),line:0,msg:`Inserted. The first ${i+1} values are now sorted.`});}
 snap({sorted:a.map((_,i)=>i),line:5,msg:'Sorted! The values are now in ascending order.',done:true});return st;}
const GEN={bubble,selection,insertion},SORTS=Object.keys(GEN);
function binary(arr,t){const a=fresh(arr),n=a.length,st=[{a,idx:-1,seen:0,lo:0,hi:n-1,found:-1,line:-1,c:0,msg:`Looking for ${t} in the sorted values. Each step checks the middle of the range.`}];let lo=0,hi=n-1,c=0;
 while(lo<=hi){const mid=(lo+hi)>>1,v=a[mid].v;c++;
  if(v===t){st.push({a,idx:mid,seen:0,lo,hi,found:mid,line:3,c,done:true,msg:`Middle is index ${mid}: ${v} equals ${t}. Found!`});return st;}
  const right=v<t;st.push({a,idx:mid,seen:0,lo,hi,found:-1,line:right?4:5,c,msg:`Middle is index ${mid}: ${v} is ${right?'smaller':'larger'} than ${t}, so discard the ${right?'left':'right'} half.`});
  if(right)lo=mid+1;else hi=mid-1;}
 st.push({a,idx:-1,seen:0,lo:1,hi:0,found:-1,line:6,c,done:true,msg:`The range is empty, so ${t} is not in this array.`});return st;}
function search(arr,t){const a=fresh(arr),n=a.length,st=[{a,idx:-1,seen:0,found:-1,line:-1,c:0,msg:`Looking for ${t}. We'll check from left to right.`}];
 for(let i=0;i<n;i++){const m=a[i].v===t;st.push({a,idx:i,seen:i,found:m?i:-1,line:m?2:1,c:i+1,done:m,msg:m?`Match! ${t} is at index ${i}.`:`${a[i].v} is not ${t}. Move to the next value.`});if(m)return st;}
 st.push({a,idx:-1,seen:n,found:-1,line:3,c:n,done:true,msg:`${t} is not in this array.`});return st;}

function draw(st,o,box0){
 const box=box0||$('bars'),n=st.a.length,mx=Math.max(1,...st.a.map(x=>Math.abs(x.v)));
 const ids=new Set(st.a.map(x=>x.id));
 box.querySelectorAll('.bar-wrap').forEach(e=>{if(!ids.has(+e.dataset.id))e.remove()});
 st.a.forEach((x,i)=>{
  let w=box.querySelector(`.bar-wrap[data-id="${x.id}"]`);
  if(!w){w=document.createElement('div');w.className='bar-wrap';w.dataset.id=x.id;w.style.setProperty('--d',i*40+'ms');w.innerHTML='<div class="bar"></div><span class="index"></span>';box.append(w);}
  w.style.width=100/n+'%';w.style.left=i*100/n+'%';
  const b=w.firstChild;b.textContent=x.v;b.style.height=`calc((100% - 22px) * ${(Math.abs(x.v)/mx*.82+.12).toFixed(3)})`;
  w.lastChild.textContent=i;
  let cls='bar';
  if(o.sorted?.includes(i))cls+=' sorted';
  if(o.cmp?.includes(i))cls+=o.swap?' swap':' cmp';
  if(o.found===i)cls+=' hit'; else if(i<o.seen||(o.lo>=0&&(i<o.lo||i>o.hi)))cls+=' dim'; else if(o.ptr===i&&o.found<0)cls+=' cmp';
  b.className=cls;
  w.classList.toggle('wave',!!o.done&&o.mode==='bubble');w.style.setProperty('--w',i*60+'ms');
 });
 const p=box.querySelector('.ptr');if(p){p.classList.toggle('on',o.ptr>=0);if(o.ptr>=0)p.style.left=(o.ptr+.5)*100/n+'%';}
}
function renderCode(line){$('pseudocode').innerHTML=CODE[S.mode].map((l,i)=>`<span class="cl${i===line?' on':''}">${esc(l)||' '}</span>`).join('');}
function metrics(v,l){[0,1,2].forEach(k=>{const e=$('m'+(k+1)),t=String(v[k]);$('m'+(k+1)+'l').textContent=l[k];if(e.textContent!==t){e.textContent=t;e.classList.remove('bump');void e.offsetWidth;e.classList.add('bump');}});}
function say(m){const e=$('explanation');if(e.dataset.m!==m){e.dataset.m=m;e.innerHTML=m.startsWith('<')?m:esc(m);e.classList.remove('swapin');void e.offsetWidth;e.classList.add('swapin');}}
function render(){
 const st=S.steps[S.i];if(!st)return;const last=S.steps.length-1,sort=SORTS.includes(S.mode);
 if(S.mode==='compare'){renderCmp(last);return;}
 if(sort){draw(st,{cmp:st.cmp,sorted:st.sorted,swap:st.swap,done:st.done,mode:'bubble',seen:0,ptr:-1,found:-1});
  $('operation-status').textContent=st.done?'Finished':INFO[S.mode][0];metrics([st.c,st.s,st.a.length],['Comparisons','Swaps','Array size']);}
 else{draw(st,{found:st.found,seen:st.seen,ptr:st.idx,lo:st.lo,hi:st.hi,mode:'linear'});
  $('operation-status').textContent=st.found>=0?'Target found':st.done?'Not found':st.idx>=0?'Checking index '+st.idx:'Ready';
  metrics([st.c,st.found>=0?'Index '+st.found:st.done?'None':'…',st.a.length],['Values checked','Result','Array size']);}
 renderCode(st.line);say(st.msg);
 $('step-status').textContent=st.done?'Complete':`Step ${S.i} of ${last}`;
 $('prog').style.width=(last?S.i/last*100:100)+'%';
 $('next-btn').disabled=S.i>=last;$('prev-btn').disabled=S.i===0;quizUI();
}
function renderCmp(last){let fin=0;
 SORTS.forEach((k,j)=>{const L=S.lanes[j],st=L[Math.min(S.i,L.length-1)];
  draw(st,{cmp:st.cmp,sorted:st.sorted,swap:st.swap,done:st.done,mode:'lane',seen:0,ptr:-1,found:-1},$('lb-'+k));
  $('ls-'+k).textContent=`${st.c} comparisons · ${st.s} swaps${st.done?' · done':''}`;$('ln-'+k).classList.toggle('fin',!!st.done);if(st.done)fin++;});
 const F=S.lanes.map(l=>l[l.length-1]);
 $('step-status').textContent=S.i>=last?'Race complete':`Step ${S.i} of ${last}`;$('operation-status').textContent=`${fin} of 3 finished`;
 metrics([S.i,fin+' / 3',S.orig.length],['Step','Finished','Array size']);renderCode(-1);
 say(fin===3?`Total work: bubble ${F[0].c} comparisons and ${F[0].s} swaps, selection ${F[1].c} and ${F[1].s}, insertion ${F[2].c} and ${F[2].s}.`:'All three sorts run on the same values, one step at a time.');
 $('prog').style.width=(last?S.i/last*100:100)+'%';$('next-btn').disabled=S.i>=last;$('prev-btn').disabled=S.i===0;quizUI();}
function quizUI(){const box=$('quiz'),st=S.steps[S.i],ok=['bubble','insertion','binary'].includes(S.mode);let q=null;
 if(S.quiz&&ok&&st&&!st.done){
  if(S.mode!=='binary'&&st.cmp.length&&!st.swap&&st.line===2){const [x,y]=st.cmp.map(i=>st.a[i].v);q={t:`Will ${x} and ${y} swap?`,o:['Yes, swap','No, keep order'],ok:S.steps[S.i+1]?.swap?0:1};}
  else if(S.mode==='binary'&&st.idx>=0){const v=st.a[st.idx].v;q={t:`The middle value is ${v} and the target is ${S.target}. Which half do we keep?`,o:['Left half','Right half'],ok:v>S.target?0:1};}}
 S.open=!!q&&!S.done.has(S.i);box.hidden=!(S.quiz&&ok);$('start-btn').disabled=S.open;
 if(box.hidden)return;const sc=`Score: ${S.score.r} of ${S.score.n}`;
 if(!S.open){box.innerHTML=`<b>Quiz mode is on</b><div class="sub">Predict each step before it plays. ${sc}</div>`;return;}
 box.innerHTML=`<b>${q.t}</b><div class="controls">${q.o.map((o,k)=>`<button class="btn" data-k="${k}">${o}</button>`).join('')}</div><div class="sub" style="margin-top:8px">${sc}</div>`;
 box.querySelectorAll('button').forEach(b=>b.onclick=()=>{const hit=+b.dataset.k===q.ok;S.score.n++;if(hit)S.score.r++;S.done.add(S.i);render();toast(hit?'Correct! Read the explanation to see why.':'Not quite. Read the explanation, then try the next one.');});
 say('Make a prediction first, then the step will play out.');$('next-btn').disabled=true;}
function stop(){S.playing=false;clearTimeout(S.timer);S.timer=null;$('start-btn').textContent='▶ Play';}
function go(d){if(S.open&&d>0)return;stop();S.i=Math.max(0,Math.min(S.steps.length-1,S.i+d));render();}
function tick(){if(!S.playing)return;if(S.i<S.steps.length-1){S.i++;render();if(S.open)stop();else S.timer=setTimeout(tick,+$('speed').value);}else stop();}
function speed(){document.documentElement.style.setProperty('--t',Math.min(.55,+$('speed').value/1000*.7)+'s');}
function clearBars(){$('bars').querySelectorAll('.bar-wrap').forEach(e=>e.remove());}
function parse(s){const v=s.split(',').map(x=>x.trim()).filter(Boolean).map(Number);if(!v.length||v.some(x=>!Number.isFinite(x)||x<0)||v.length>18)throw new Error('Enter 1 to 18 numbers (0 or higher) separated by commas.');return v;}
function toast(s){$('toast').textContent=s;}
function loadSort(msg){try{S.orig=parse($('array-input').value);if(S.mode==='compare'){S.lanes=SORTS.map(k=>GEN[k](S.orig));S.steps=S.lanes.reduce((a,b)=>b.length>a.length?b:a);$('compare-view').innerHTML=SORTS.map(k=>`<div class="lane" id="ln-${k}"><div class="lane-h"><b>${INFO[k][0]}</b><span id="ls-${k}"></span></div><div class="bars mini" id="lb-${k}"></div></div>`).join('');}else S.steps=GEN[S.mode](S.orig);S.done=new Set();S.i=0;stop();clearBars();render();toast(msg||'Values loaded.');}catch(e){toast(e.message);}}
function loadSearch(){try{let arr=parse($('search-array').value),raw=$('target-input').value.trim(),t=Number(raw);if(raw===''||!Number.isFinite(t))throw new Error('Enter a target number.');S.target=t;if(S.mode==='binary'){arr=[...arr].sort((x,y)=>x-y);$('search-array').value=arr.join(', ');}S.steps=(S.mode==='binary'?binary:search)(arr,t);S.done=new Set();S.i=0;stop();clearBars();render();toast(S.mode==='binary'?'Values sorted for binary search. Press Play or Next.':'Search ready. Press Play or Next.');}catch(e){toast(e.message);}}

function dsMeta(line){const m=S.mode,a=S[m];
 $('step-status').textContent=`${a.length} item${a.length===1?'':'s'} stored`;$('operation-status').textContent={stack:'Push / Pop',queue:'Enqueue / Dequeue',list:'Insert / Delete'}[m];
 metrics([a.length,a.length?(m==='stack'?a[a.length-1]:a[0]):'—',a.length+' / 8'],['Items',m==='stack'?'Top value':m==='list'?'Head value':'Front value','Capacity']);
 $('prog').style.width=a.length/8*100+'%';renderCode(line??-1);}
function renderDS(enterLast,line){
 const m=S.mode,a=S[m],it=(v,i)=>`<div class="item${enterLast&&(enterLast==='head'?i===0:i===a.length-1)?' enter':''}">${esc(v)}</div>`;
 $('structure-view').innerHTML=m==='stack'
  ?`<div class="stage"><div><div class="tag">TOP</div><div class="stack-col" id="col">${a.length?a.map(it).join(''):'<div class="empty">Empty stack. Push a value.</div>'}</div></div><div class="explain" style="max-width:230px"><strong>LIFO</strong><br>The most recently pushed item is the first one removed.</div></div>`
  :m==='list'?`<div class="stage" style="display:block;padding-top:44px"><div class="q-track ll" id="col">${a.length?a.map(it).join(''):'<div class="empty">Empty list. head points to null.</div>'}</div><div class="tag" style="margin-top:28px"><strong>Linked list</strong>: each node stores a value and a pointer to the next node</div></div>`
  :`<div class="stage" style="display:block;padding-top:40px"><div class="q-track" id="col">${a.length?a.map(it).join(''):'<div class="empty">Empty queue. Enqueue a value.</div>'}</div><div class="marks" style="max-width:520px;margin:12px auto 0"><span>Front: items leave here</span><span>Rear: items join here</span></div><div class="tag" style="margin-top:28px"><strong>FIFO</strong>: first in, first out</div></div>`;
 dsMeta(line);}
function dsAdd(){const v=$('ds-value').value.trim(),a=S[S.mode];if(v===''){toast('Enter a value first.');return;}
 if(a.length>=8){toast('This demo holds up to 8 items. Pop or clear to make room.');return;}
 a.push(v);renderDS(true,1);say(`${VERB[S.mode][0]} ${v}.`);toast(`${VERB[S.mode][0]} ${v}.`);$('ds-value').select();}
function dsRemove(){const a=S[S.mode],s=S.mode==='stack';
 if(!a.length){renderCode(S.mode==='list'?2:3);say('Underflow: nothing to remove from an empty '+NAME[S.mode]+'.');toast('The '+NAME[S.mode]+' is empty.');return;}
 const v=s?a.pop():a.shift(),kids=[...$('col').children],el=s?kids[kids.length-1]:kids[0];
 if(el)el.classList.add('leave');renderCode(S.mode==='list'?3:4);say(`${VERB[S.mode][1]} ${v}.`);toast(`${VERB[S.mode][1]} ${v}.`);
 const m=S.mode;setTimeout(()=>{if(S.mode===m)renderDS(false,S.mode==='list'?3:4);},300);}

function switchMode(m){stop();S.mode=m;
 document.querySelectorAll('.navbtn').forEach(b=>b.classList.toggle('active',b.dataset.mode===m));
 const cm=m==='compare',sort=SORTS.includes(m)||cm,srch=SEARCHES.includes(m),ds=DSM.includes(m);
 $('sort-controls').hidden=!sort;$('search-controls').hidden=!srch;$('ds-controls').hidden=!ds;$('play').hidden=ds;$('bars').hidden=ds||cm;$('compare-view').hidden=!cm;$('structure-view').hidden=!ds;
 const [t,sub,pill,note]=INFO[m];$('module-title').textContent=t;$('module-sub').textContent=sub;$('complexity-pill').textContent=pill;$('complexity-note').innerHTML=note;
 const L={stack:['Push','Pop'],queue:['Enqueue','Dequeue'],list:['Insert tail','Delete head']}[m];if(L){$('ds-add').textContent=L[0];$('ds-remove').textContent=L[1];}$('ds-head').hidden=m!=='list';S.quiz=false;$('quiz-btn').hidden=!['bubble','insertion','binary'].includes(m);$('quiz-btn').textContent='Quiz: off';
 toast('');$('explanation').dataset.m='';
 if(sort)loadSort(''); else if(srch)loadSearch(); else{say(m==='list'?'Insert at the head or tail. Delete removes the head node.':m==='stack'?'Push adds to the top. Pop removes the top item.':'Enqueue adds at the rear. Dequeue removes from the front.');renderDS(false);}
 toast('');}

$('load-array').onclick=()=>loadSort();
$('randomize').onclick=()=>{$('array-input').value=Array.from({length:7},()=>Math.floor(Math.random()*29)+1).join(', ');loadSort('Shuffled with new values.');};
$('run-search').onclick=loadSearch;
$('next-btn').onclick=()=>go(1);$('prev-btn').onclick=()=>go(-1);
$('reset-btn').onclick=()=>{stop();S.i=0;render();toast('Back to the start.');};
$('start-btn').onclick=()=>{if(S.playing){stop();return;}if(S.i>=S.steps.length-1)S.i=0;S.playing=true;$('start-btn').textContent='Ⅱ Pause';render();S.timer=setTimeout(tick,+$('speed').value);};
$('speed').onchange=speed;
$('ds-add').onclick=dsAdd;$('ds-remove').onclick=dsRemove;
$('ds-reset').onclick=()=>{S.stack=[];S.queue=[];S.list=[];renderDS(false);say('Cleared.');toast('Cleared.');};
const enter=(id,fn)=>$(id).addEventListener('keydown',e=>{if(e.key==='Enter')fn();});
enter('array-input',()=>loadSort());enter('search-array',loadSearch);enter('target-input',loadSearch);enter('ds-value',dsAdd);
$('progbar').onclick=e=>{if(!S.steps.length||DSM.includes(S.mode))return;const r=e.currentTarget.getBoundingClientRect();stop();S.i=Math.max(0,Math.min(S.steps.length-1,Math.round((e.clientX-r.left)/r.width*(S.steps.length-1))));render();};
document.addEventListener('keydown',e=>{if(/INPUT|SELECT|TEXTAREA|BUTTON/.test(e.target.tagName)||$('viz').hidden||DSM.includes(S.mode))return;
 if(e.key===' '){e.preventDefault();$('start-btn').click();}else if(e.key==='ArrowRight')go(1);else if(e.key==='ArrowLeft')go(-1);});
$('quiz-btn').onclick=()=>{S.quiz=!S.quiz;stop();$('quiz-btn').textContent='Quiz: '+(S.quiz?'on':'off');S.done=new Set();S.score={r:0,n:0};render();};
$('ds-head').onclick=()=>{const v=$('ds-value').value.trim(),a=S.list;if(v===''){toast('Enter a value first.');return;}if(a.length>=8){toast('This demo holds up to 8 items. Delete or clear to make room.');return;}a.unshift(v);renderDS('head',0);say(`Inserted ${v} at the head.`);toast(`Inserted ${v} at the head.`);};
function buildNav(){
 $('nav').innerHTML=Object.entries(CAT).map(([k,c])=>`<div class="grp" data-c="${k}"><div class="grp-h"><a class="grp-t" href="#/${k}"><span class="navicon">${c.ic}</span>${c.t}</a><button class="chev" aria-label="Show or hide ${c.t}" aria-expanded="false">⌄</button></div><div class="sub-wrap"><div class="sub-list">${c.items.map(m=>`<a class="navbtn" data-mode="${m}" href="#/${k}/${m}">${INFO[m][0]}</a>`).join('')}</div></div></div>`).join('')+`<div class="grp" data-c="quiz"><div class="grp-h"><a class="grp-t" href="#/quiz"><span class="navicon">?</span>Quiz</a></div></div>`;
 document.querySelectorAll('.grp:not([data-c="quiz"])').forEach(g=>g.querySelector('.chev').onclick=()=>{const k=g.dataset.c;OPEN.has(k)?OPEN.delete(k):OPEN.add(k);syncNav(CUR.split('/')[1]);});}
function syncNav(c){document.querySelectorAll('.grp').forEach(g=>{const k=g.dataset.c;g.classList.toggle('open',OPEN.has(k));g.classList.toggle('cur',k===c);g.querySelector('.chev')?.setAttribute('aria-expanded',OPEN.has(k));});}
const card=(m,c)=>`<a class="card" href="#/${c}/${m}"><b>${INFO[m][0]}</b><p>${INFO[m][1]}</p><span class="pill">${INFO[m][2]}</span><span class="go">Open visualizer →</span></a>`;
function pageHTML(c){
 if(c==='quiz')return quizHTML();
 if(!CAT[c])return `<div class="panel"><h2 class="big">Choose a topic</h2><div class="sub">New here? Start with Sorting.</div><div class="cards">${Object.entries(CAT).map(([k,v])=>`<a class="card" href="#/${k}"><span class="navicon">${v.ic}</span><b>${v.t}</b><p>${v.d}</p><span class="pill">${v.items.length} visualizers</span><span class="go">Explore →</span></a>`).join('')}<a class="card" href="#/quiz"><span class="navicon">?</span><b>Quiz</b><p>Test what you learned with short questions.</p><span class="pill">${QB.length} questions</span><span class="go">Start →</span></a></div></div>
 <div class="panel"><h2>How it works</h2><div class="cards"><div class="card"><b>1. Learn</b><p>Read the idea and a real-life analogy first.</p></div><div class="card"><b>2. Visualize</b><p>Step through each algorithm and watch the matching code line.</p></div><div class="card"><b>3. Practice</b><p>Use quiz mode to predict the next step before it plays.</p></div></div></div>`;
 const v=CAT[c];
 return `<nav class="crumb"><a href="#/">Home</a> › <b>${v.t}</b></nav>
 <div class="panel"><h2 class="big">${v.t}</h2><p class="lead">${v.lead}</p><div class="two"><div class="callout"><strong>Real-life analogy</strong><br>${v.analogy}</div><div class="callout"><strong>When to use it</strong><br>${v.when}</div></div></div>
 <div class="panel"><h2>${v.t==='Data structures'?'Structures':'Algorithms'}</h2><div class="sub">Pick one to open its step-by-step visualizer.</div><div class="cards">${v.items.map(m=>card(m,c)).join('')}</div></div>
 <div class="panel"><h2>At a glance</h2><div class="sub">Time and space complexity</div><div class="tbl"><table><tr>${v.th.map(x=>`<th>${x}</th>`).join('')}</tr>${v.rows.map(r=>`<tr><td>${INFO[r[0]][0]}</td>${r.slice(1).map(x=>`<td>${x}</td>`).join('')}</tr>`).join('')}</table></div></div>`;}
function route(){
 const [,c,m]=CUR.split('/'),known=!!CAT[c],viz=known&&CAT[c].items.includes(m);stop();
 if(known)OPEN.add(c);
 const isQ=c==='quiz';$('viz').hidden=!viz;$('page').hidden=viz;document.querySelector('.hero').hidden=known||isQ;$('page').classList.toggle('qzp',isQ);if(isQ)QZ.stage='start';
 if(viz){switchMode(m);$('crumb').innerHTML=`<a href="#/">Home</a> › <a href="#/${c}">${CAT[c].t}</a> › <b>${INFO[m][0]}</b>`;document.title=INFO[m][0]+' · Data Structure Analyzer';}
 else{document.querySelectorAll('.navbtn').forEach(b=>b.classList.remove('active'));$('page').innerHTML=pageHTML(c);document.title=(known?CAT[c].t+' · ':isQ?'Quiz · ':'')+'Data Structure Analyzer';try{scrollTo(0,0)}catch(e){}}
 const el=viz?$('viz'):$('page');el.classList.remove('view-in');void el.offsetWidth;el.classList.add('view-in');
 syncNav(known||isQ?c:'');}
function nav(to){CUR=to;try{history.pushState(null,'',to)}catch(e){}route();}
document.addEventListener('click',e=>{const q=e.target.closest&&e.target.closest('[data-qz]');if(q){qz(q.dataset.qz,q.dataset.v);return;}const a=e.target.closest&&e.target.closest('a[href^="#/"]');if(!a)return;e.preventDefault();nav(a.getAttribute('href'));});
const back=()=>{try{CUR=location.hash||'#/'}catch(e){}route();};addEventListener('popstate',back);addEventListener('hashchange',back);
const setTheme=t=>{document.documentElement.dataset.theme=t;$('theme').textContent=t==='dark'?'☀ Light':'☾ Dark';try{localStorage.setItem('dsa-theme',t)}catch(e){}document.querySelector('meta[name=theme-color]').content=t==='dark'?'#080c18':'#f3f5fb';};
$('theme').onclick=()=>setTheme(document.documentElement.dataset.theme==='dark'?'light':'dark');setTheme(document.documentElement.dataset.theme==='light'?'light':'dark');
buildNav();
speed();route();