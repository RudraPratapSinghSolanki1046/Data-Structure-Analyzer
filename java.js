/* Java Code tabs: shows the real Java source for the open algorithm or data structure and highlights the lines
   that correspond to what the visualization just did. The mapping is explicit and verifiable:
   - sorting/searching: each step carries an event (step.ev, or its pseudocode line via JAVA[key].lineEv);
   - stack/queue/list and the Linked List Lab: each button press is classified as an operation (push, pop, underflow, ...);
   and every event name maps to `// @marker` lines in java/*.java (built into javacode.js by tools/build-java.py).
   The visualization runs in JavaScript; the Java is never executed in the browser. */
(function(){
const $=id=>document.getElementById(id);
const KW=new Set('public private protected static final void int boolean return if else for while break new class import null true false'.split(' '));
const esc=s=>String(s).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
function hl(line){let o='',m;const re=/(\/\/.*$)|("(?:[^"\\]|\\.)*")|(\b\d+\b)|(\b[A-Za-z_]\w*\b)|(\s+|.)/g;
 while((m=re.exec(line))){if(m[1])o+=`<span class="tk-c">${esc(m[1])}</span>`;else if(m[2])o+=`<span class="tk-s">${esc(m[2])}</span>`;else if(m[3])o+=`<span class="tk-n">${m[3]}</span>`;
  else if(m[4])o+=KW.has(m[4])?`<span class="tk-k">${m[4]}</span>`:/^[A-Z]/.test(m[4])?`<span class="tk-t">${m[4]}</span>`:m[4];else o+=esc(m[5]);}return o;}
const v=x=>x.v;
const VARS={
 bubble:s=>{const o=[['n',s.a.length]];if(s.cmp.length){const j=s.cmp[0];o.push(['i',s.sorted.length],['j',j],['arr[j]',v(s.a[j])],['arr[j + 1]',v(s.a[j+1])]);if(s.swap)o.push(['temp',v(s.a[j+1])]);}else if(s.line===1)o.push(['i',s.sorted.length-1]);return o;},
 selection:s=>{const o=[['n',s.a.length]];if(s.cmp.length){if(s.swap)o.push(['i',s.cmp[0]],['minIdx',s.cmp[1]],['temp',v(s.a[s.cmp[1]])]);else o.push(['i',s.sorted.length],['j',s.cmp[1]],['minIdx',s.cmp[0]],['arr[j]',v(s.a[s.cmp[1]])],['arr[minIdx]',v(s.a[s.cmp[0]])]);}else if(s.line===0)o.push(['i',s.sorted.length-1]);return o;},
 insertion:s=>{const o=[['n',s.a.length]];if(s.cmp.length){const j=s.cmp[1];o.push(['i',s.sorted.length],['j',j],['arr[j - 1]',v(s.a[j-1])],['arr[j]',v(s.a[j])]);if(s.swap)o.push(['temp',v(s.a[j-1])]);}else if(s.line===0&&s.sorted.length)o.push(['i',s.sorted.length-1]);return o;},
 merge:s=>{const o=[['n',s.a.length]];if(s.ev==='divide'||s.line===0&&s.cmp.length)o.push(['mid (index)',s.cmp[0]]);else if(s.line===2)o.push(['i',s.cmp[0]],['j',s.cmp[1]],['arr[i]',v(s.a[s.cmp[0]])],['arr[j]',v(s.a[s.cmp[1]])]);else if(s.ev)o.push(['k - 1 (written)',s.cmp[0]],['value',v(s.a[s.cmp[0]])]);else if(s.line===4&&s.sorted.length)o.push(['left',s.sorted[0]],['right',s.sorted[s.sorted.length-1]]);return o;},
 quick:s=>{const o=[['n',s.a.length]];if(s.line===1)o.push(['high',s.cmp[0]],['pivot',v(s.a[s.cmp[0]])]);else if(s.line===2)o.push(['j',s.cmp[0]],['high',s.cmp[1]],['arr[j]',v(s.a[s.cmp[0]])],['pivot',v(s.a[s.cmp[1]])]);else if(s.line===3)o.push(['i',s.cmp[0]],['j',s.cmp[1]]);else if(s.line===4)o.push(['i',s.cmp[0]],['pivot index',s.cmp[0]]);return o;},
 heap:s=>{const o=[['n',s.a.length]];if(s.ev==='compareLeft')o.push(['root',s.cmp[0]],['left',s.cmp[1]],['arr[root]',v(s.a[s.cmp[0]])],['arr[left]',v(s.a[s.cmp[1]])]);else if(s.ev==='compareRight')o.push(['largest',s.cmp[0]],['right',s.cmp[1]],['arr[largest]',v(s.a[s.cmp[0]])],['arr[right]',v(s.a[s.cmp[1]])]);else if(s.ev==='heapSwap')o.push(['root',s.cmp[0]],['largest',s.cmp[1]]);else if(s.ev==='extractSwap')o.push(['end',s.cmp[1]]);else if(s.line===3)o.push(['end',s.sorted[s.sorted.length-1]]);return o;},
 linear:s=>{const o=[['target',S.target],['arr.length',s.a.length]];if(s.idx>=0)o.push(['i',s.idx],['arr[i]',v(s.a[s.idx])]);return o;},
 binary:s=>{const o=[['target',S.target]];if(s.idx>=0)o.push(['low',s.lo],['high',s.hi],['mid',s.idx],['arr[mid]',v(s.a[s.idx])]);return o;}};
const GROUPS={stack:[['stack_array','Array-based'],['stack_linked','Linked list']],queue:[['queue_array','Array (shifting)'],['queue_circular','Circular array'],['queue_linked','Linked list']],list:[['list_singly','Singly linked'],['list_doubly','Doubly linked'],['list_circular','Circular']]};
const CHOICE={stack:'stack_array',queue:'queue_array',list:'list_singly'},CAP=8;
const PN={main:{p:'j',key:null,ev:null,pin:null},lab:{p:'l',key:null,ev:null,pin:null},tree:{p:'t',key:null,ev:null,pin:null},graph:{p:'g',key:null,ev:null,pin:null,vars:null},ref:{p:'r',key:null,ev:null,pin:null}};
let tab='visualize',flash=0,qFront=0;
const J=pn=>pn.key&&JAVA[pn.key];
const opsKind=j=>!!(j&&j.rows);
function dsVars(key){const m=S.mode,a=S[m]||[],n=a.length,f=n?a[0]:'null',l=n?a[n-1]:'null';
 return {stack_array:[['top',n-1],['size',n],['capacity',CAP],['data[top]',n?l:'-']],stack_linked:[['size',n],['head.value',n?l:'null']],
  queue_array:[['size',n],['data[0]',n?f:'-'],['capacity',CAP]],queue_circular:[['front',qFront],['size',n],['(front + size) % capacity',(qFront+n)%CAP],['capacity',CAP]],
  queue_linked:[['size',n],['head.value',f],['tail.value',l]],list_singly:[['size',n],['head.value',f]],list_doubly:[['size',n],['head.value',f],['tail.value',l]],list_circular:[['size',n],['tail.next (head).value',f],['tail.value',l]]}[key]||[];}
const labVars=()=>[['size',$('ll-count').textContent],['head',$('ll-head').textContent],['tail',$('ll-tail').textContent]];
function codeBlock(pn,j,variantHTML){const p=pn.p,lines=j.code.replace(/\n$/,'').split('\n');
 const chips=opsKind(j)?`<div class="jbox"><h3>Show operation</h3><div class="jops" id="${p}ops">${Object.keys(j.marks).map(m=>`<button type="button" class="chip" data-m="${m}">${m}</button>`).join('')}</div></div>`:'';
 return `<div class="jwrap"><div class="jcode"><div class="jhead"><span>${j.file}</span><span class="jbtns">${variantHTML}<button class="btn" id="${p}-copy" type="button">Copy code</button><button class="btn" id="${p}-dl" type="button">Download .java</button></span></div><div class="jbody" id="${p}body" tabindex="0" aria-label="Java source code">${lines.map((l,i)=>`<div class="jl" data-n="${i+1}"><span class="ln">${i+1}</span><span class="lc">${hl(l)||' '}</span></div>`).join('')}</div></div>
 <div class="jside">${chips}<div class="jbox"><h3>Variables</h3><div id="${p}vars" class="jvars"></div></div><div class="jbox"><h3>Current step</h3><p id="${p}step"></p><p class="sub" id="${p}line"></p></div><div class="jbox"><h3>About this implementation</h3><p>${j.about}</p></div><p class="sub jnote">Highlighted lines show which Java statements this visualization step corresponds to. The visualization is computed in JavaScript; the Java is not run in your browser.</p></div></div>`;}
const explainHTML=j=>`<h3 class="jh3">How the visualization maps to the code</h3><div class="tbl"><table><tr><th>Visualization event</th><th>Highlighted Java lines</th></tr>${Object.keys(j.marks).map(k=>`<tr><td>${k}</td><td>${j.marks[k].join(', ')}</td></tr>`).join('')}</table></div>`;
const complexHTML=j=>j.rows?`<div class="tbl"><table><tr><th>Operation</th><th>Best case</th><th>Average case</th><th>Worst case</th><th>Auxiliary space</th></tr>${j.rows.map(r=>`<tr><td>${r[0]}</td><td>${r[1]}</td><td>${r[2]}</td><td>${r[3]}</td><td>${r[4]}</td></tr>`).join('')}</table></div><p class="sub">Total storage: ${j.space}.</p><div class="callout"><b>Why this applies to this implementation</b><br>${j.why}</div>`
 :`<div class="tbl"><table><tr><th>Best case</th><th>Average case</th><th>Worst case</th><th>Auxiliary space</th></tr><tr><td>${j.best}</td><td>${j.avg}</td><td>${j.worst}</td><td>${j.space}</td></tr></table></div><div class="callout"><b>Why this applies to this implementation</b><br>${j.why}</div>`;
function bind(pn){const p=pn.p;$(p+'-copy').onclick=()=>copy(pn);$(p+'-dl').onclick=()=>download(pn);
 const ops=$(p+'ops');if(ops)ops.onclick=e=>{const b=e.target.closest('[data-m]');if(!b)return;pn.pin=pn.pin===b.dataset.m?null:b.dataset.m;sync(pn);};}
function variantSel(group,cur,id){return group?`<select id="${id}" aria-label="Implementation">${group.map(([k,t])=>`<option value="${k}"${k===cur?' selected':''}>${t}</option>`).join('')}</select>`:'';}
function buildMain(){const pn=PN.main;pn.key=JAVA[S.mode]?S.mode:(GROUPS[S.mode]?CHOICE[S.mode]:null);pn.pin=null;const j=J(pn);
 if(!j){$('viztabs').hidden=true;setTab('visualize');return;}
 $('viztabs').hidden=false;
 $('tab-java').innerHTML=codeBlock(pn,j,variantSel(GROUPS[S.mode],pn.key,'j-variant'));
 $('tab-explain').innerHTML=`<div class="panel-head"><div><h2>Explanation</h2><div class="sub">${j.file}</div></div></div><p class="lead">${j.about}</p>${explainHTML(j)}`;
 $('tab-complexity').innerHTML=`<div class="panel-head"><div><h2>Complexity analysis</h2><div class="sub">${j.file}</div></div></div>${complexHTML(j)}`;
 bind(pn);const sel=$('j-variant');if(sel)sel.onchange=()=>{CHOICE[S.mode]=sel.value;buildMain();};setTab(tab);}
const treeVars=()=>{const nodes=[...document.querySelectorAll('#tree-view .tree-node')],meta=($('tree-meta')||{}).textContent||'';
 const root=nodes.length?nodes.reduce((a,b)=>parseFloat(a.style.top)<=parseFloat(b.style.top)?a:b).textContent:'null';
 const o=[['size',nodes.length],['height',(meta.match(/height:\s*(\d+)/i)||[])[1]||0],['root',root]];
 if(PN.tree.key==='tree_avl'){o.push(['balance(root)',(meta.match(/balance factor:\s*(-?\d+)/i)||[])[1]||0]);const r=window.dsaTreeRot||[];if(r.length)o.push(['rotations',r.join(', ')]);}return o;};
const GCH={v:'graph_list'},RCH={v:'ref_array'};
const gCount=()=>[document.querySelectorAll('#graph-view .graph-node').length,document.querySelectorAll('#graph-view line').length];
const graphVars=()=>PN.graph.vars||[['vertices',gCount()[0]],['edges',gCount()[1]]];
const LABCFG=[
 {pn:PN.lab,hash:'#/advanced/linked',type:'ll-type',pre:'list_',host:'lab-java',btn:'[data-ll]',vars:labVars,msg:()=>$('ll-status').textContent,
  ev(b){let e=({head:'insertHead',tail:'insertTail',at:'insertAt',delhead:'deleteHead',deltail:'deleteTail',delat:'deleteAt',reverse:'reverse',traverse:'traverse',search:'search'})[b.dataset.ll]||null;if(/delete/.test(e||'')&&/empty/i.test($('ll-status').textContent))e='underflow';return e;}},
 {pn:PN.tree,hash:'#/advanced/trees',type:'tree-type',pre:'tree_',host:'tree-java',btn:'[data-tree],[data-traverse]',vars:treeVars,msg:()=>$('tree-status').textContent,
  ev(b){if(b.dataset.traverse)return {inorder:'inorder',preorder:'preorder',postorder:'postorder',level:'levelOrder'}[b.dataset.traverse]||null;
   const t=b.dataset.tree;if(!['insert','delete','search'].includes(t)||/Enter a valid/i.test($('tree-status').textContent))return null;
   const e=[t];if(PN.tree.key==='tree_avl'&&t!=='search'){e.push('rebalance');(window.dsaTreeRot||[]).forEach(r=>e.push('rot'+r));}return e;}},
 {pn:PN.ref,hash:'#/structures',keyFn:()=>RCH.v,choice:RCH,variants:[['ref_array','Array operations'],['ref_deque','Deque'],['ref_pq','Priority queue (min-heap)'],['ref_tree','Binary tree']],host:'ref-java',btn:'[data-no-live-operations]',vars:()=>[],msg:()=>'',static:true,title:'Java reference',sub:'These structures have no visualizer in the app. Choose an operation to highlight the Java lines that implement it.'},
 {pn:PN.graph,hash:'#/advanced/graphs',keyFn:()=>GCH.v,choice:GCH,variants:[['graph_list','Adjacency list + algorithms'],['graph_matrix','Adjacency matrix']],host:'graph-java',btn:'[data-graph],[data-graph-run]',vars:graphVars,msg:()=>$('graph-status').textContent,
  ev(b){if(b.dataset.graphRun)return undefined;PN.graph.vars=null;const n=gCount(),was=PN.graph.was||[0,0];return n[0]>was[0]?'addVertex':n[1]>was[1]?'addEdge':null;}}];
function buildLabs(){for(const c of LABCFG){const pn=c.pn;if(CUR!==c.hash){pn.key=null;continue;}const page=$('page');if(!page||page.hidden||(c.type&&!$(c.type)))continue;
 pn.key=c.keyFn?c.keyFn():c.pre+$(c.type).value;pn.pin=null;pn.keep=false;const j=J(pn);if(!j)continue;
 let host=$(c.host);if(!host){host=document.createElement('section');host.className='panel';host.id=c.host;host.style.marginTop='16px';page.append(host);}
 host.innerHTML=`<div class="panel-head"><div><h2>${c.title||'Java implementation'}</h2><div class="sub">${j.file}. ${c.sub||'Press any operation above to see the matching Java lines.'}</div></div></div>${codeBlock(pn,j,variantSel(c.variants,pn.key,pn.p+'-variant'))}<h3 class="jh3" style="margin-top:16px">Complexity analysis</h3>${complexHTML(j)}${explainHTML(j)}`;
 bind(pn);if(c.static){['vars','step'].forEach(n=>{const e=$(pn.p+n);if(e)e.closest('.jbox').hidden=true;});if(!pn.keep)pn.pin=Object.keys(j.marks)[0];const nt=host.querySelector('.jnote');if(nt)nt.textContent='This structure has no visualizer in the app. The highlighted lines show the Java that implements the operation you selected.';}const vs=$(pn.p+'-variant');if(vs)vs.onchange=()=>{if(c.choice)c.choice.v=vs.value;buildLabs();};sync(pn);}}
function setTab(t){tab=t;const has=!!J(PN.main),t2=has?t:'visualize';
 document.querySelector('#viz .bottom-grid').hidden=t2!=='visualize';$('viz').classList.toggle('jtab',t2==='java');
 [['java','tab-java'],['explain','tab-explain'],['complexity','tab-complexity']].forEach(([k,id])=>$(id).hidden=t2!==k);
 document.querySelectorAll('#viztabs button').forEach(b=>{const on=b.dataset.tab===t2;b.classList.toggle('on',on);b.setAttribute('aria-selected',on);});
 sync(PN.main);}
function copy(pn){const j=J(pn),b=$(pn.p+'-copy'),done=ok=>{b.textContent=ok?'Copied':'Copy failed';clearTimeout(flash);flash=setTimeout(()=>b.textContent='Copy code',1600);};
 const fb=()=>{const t=document.createElement('textarea');t.value=j.code;t.style.cssText='position:fixed;opacity:0';document.body.append(t);t.select();let ok=false;try{ok=document.execCommand('copy')}catch(e){}t.remove();done(ok);};
 if(navigator.clipboard&&navigator.clipboard.writeText)navigator.clipboard.writeText(j.code).then(()=>done(true),fb);else fb();}
function download(pn){const j=J(pn),a=document.createElement('a');a.href=URL.createObjectURL(new Blob([j.code],{type:'text/x-java-source'}));a.download=j.file;document.body.append(a);a.click();a.remove();setTimeout(()=>URL.revokeObjectURL(a.href),500);}
const rng=a=>{const o=[];let s=a[0],p=a[0];for(let i=1;i<=a.length;i++){if(a[i]===p+1){p=a[i];continue;}o.push(s===p?s:s+'–'+p);s=a[i];p=a[i];}return o.join(', ');};
function sync(pn){const j=J(pn),p=pn.p;if(!j||!$(p+'body'))return;const on=new Set();let vars,msg,done=false;
 if(opsKind(j)){const evs=pn.pin?[pn.pin]:[].concat(pn.ev||[]);evs.forEach(ev=>((j.evMarks||{})[ev]||[ev]).forEach(m=>(j.marks[m]||[]).forEach(n=>on.add(n))));const c=LABCFG.find(x=>x.pn===pn);vars=c?c.vars():dsVars(pn.key);msg=c?c.msg():($('explanation').textContent||'');}
 else{const st=S.steps[S.i];if(!st)return;const ev=st.ev||(st.line>=0?j.lineEv[st.line]:null);if(ev)(j.evMarks[ev]||[ev]).forEach(m=>(j.marks[m]||[]).forEach(n=>on.add(n)));vars=(VARS[pn.key]||(()=>[]))(st);msg=st.msg||'';done=!!st.done;}
 let first=null;document.querySelectorAll('#'+p+'body .jl').forEach(el=>{const n=+el.dataset.n,a=on.has(n);el.classList.toggle('on',a);if(a&&first===null)first=el;});
 const body=$(p+'body');if(first&&(pn!==PN.main||tab==='java')){const t=first.offsetTop-body.offsetTop,vh=body.clientHeight;if(t<body.scrollTop+20||t>body.scrollTop+vh-60)body.scrollTop=Math.max(0,t-vh/3);}
 document.querySelectorAll('#'+p+'ops .chip').forEach(c=>c.classList.toggle('on',c.dataset.m===(pn.pin||pn.ev)));
 $(p+'vars').innerHTML=vars.length?vars.map(([k,x])=>`<span class="jv"><b>${esc(k)}</b>${esc(x)}</span>`).join(''):'<span class="sub">No variables yet</span>';
 $(p+'step').textContent=msg;const ls=[...on].sort((a,b)=>a-b);$(p+'line').textContent=ls.length?'Java line'+(ls.length>1?'s ':' ')+rng(ls):(done?'Finished':(opsKind(j)?(pn.ev==='overflow'&&!pn.pin?'This implementation has no capacity limit; the demo stops at '+CAP+' items':pn.ev||pn.pin?(pn.key==='graph_matrix'?'Algorithms are in the adjacency list version':'No Java line for this operation'):'Press an operation to highlight its Java lines'):'No Java line for this step'));}
/* hooks into the existing app */
const bs=window.switchMode;window.switchMode=function(m){bs.apply(this,arguments);buildMain();};
const br=window.render;window.render=function(){br.apply(this,arguments);try{if(!opsKind(J(PN.main)))sync(PN.main)}catch(e){console.error(e)}};
const brt=window.route;window.route=function(){brt.apply(this,arguments);try{buildLabs()}catch(e){console.error(e)}};
let before=0;
const DSOP={'ds-add':{stack:['push','overflow'],queue:['enqueue','overflow'],list:['insertTail','overflow']},'ds-head':{list:['insertHead','overflow']},'ds-remove':{stack:['pop','underflow'],queue:['dequeue','underflow'],list:['deleteHead','underflow']}};
['ds-add','ds-head','ds-remove','ds-reset'].forEach(id=>{const b=$(id);if(!b)return;
 b.addEventListener('click',()=>{before=(S[S.mode]||[]).length;},true);
 b.addEventListener('click',()=>{const pn=PN.main,m=S.mode,n=(S[m]||[]).length;if(!GROUPS[m])return;
  if(id==='ds-reset'){pn.ev=null;qFront=0;}
  else if(id==='ds-remove'){const ok=n<before;pn.ev=DSOP[id][m][ok?0:1];if(ok&&m==='queue')qFront=(qFront+1)%CAP;}
  else if(n>before)pn.ev=DSOP[id][m][0];else if(before>=CAP)pn.ev=DSOP[id][m][1];else pn.ev=null;
  pn.pin=null;if(opsKind(J(pn)))sync(pn);});});
document.addEventListener('click',e=>{const t=e.target.closest&&e.target.closest('[data-tree]');if(t&&window.dsaTreeRot)window.dsaTreeRot.length=0;if(e.target.closest&&e.target.closest('[data-graph]'))PN.graph.was=gCount();},true);
window.dsaGraphHook=info=>{const pn=PN.graph;if(!pn.key)return;pn.ev=info.ev;pn.vars=info.vars;pn.pin=null;sync(pn);};
document.addEventListener('click',e=>{if(!e.target.closest)return;for(const c of LABCFG){const b=e.target.closest(c.btn);if(!b||!c.pn.key)continue;const ev=c.ev(b);if(ev!==undefined)c.pn.ev=ev;c.pn.pin=null;sync(c.pn);return;}});
document.addEventListener('change',e=>{if(LABCFG.some(c=>c.type===e.target.id))setTimeout(buildLabs,0);});
document.querySelectorAll('#viztabs button').forEach(b=>b.addEventListener('click',()=>setTab(b.dataset.tab)));
if(!$('viz').hidden)buildMain();
})();
