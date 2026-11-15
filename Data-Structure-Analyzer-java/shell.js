/* Shell: icons, global search, recent activity and the Home dashboard. Uses the existing router and data. */
(function(){
const $=id=>document.getElementById(id),svg=p=>`<svg viewBox="0 0 24 24" aria-hidden="true">${p}</svg>`;
const IC={home:svg('<path d="M3 11l9-8 9 8"/><path d="M5 10v10h14V10"/>'),sorting:svg('<path d="M7 4v16M3 16l4 4 4-4"/><path d="M17 20V4m-4 4l4-4 4 4"/>'),searching:svg('<circle cx="11" cy="11" r="7"/><path d="M21 21l-4.3-4.3"/>'),structures:svg('<rect x="3" y="4" width="18" height="5" rx="1"/><rect x="3" y="11" width="18" height="5" rx="1"/><path d="M3 19h18"/>'),quiz:svg('<circle cx="12" cy="12" r="9"/><path d="M9.5 9.5a2.5 2.5 0 015 .5c0 1.7-2.5 2-2.5 3.5M12 17h.01"/>'),advanced:svg('<path d="M9 3h6M10 3v6L4.5 19a1.5 1.5 0 001.3 2h12.4a1.5 1.5 0 001.3-2L14 9V3"/>')};
const LABS=[['linked','Linked List Lab','Singly, doubly and circular lists with pointer changes explained.'],['trees','Tree Lab','BST and AVL trees with traversals, height and balance factors.'],['graphs','Graph Lab','Build a graph, then run BFS, DFS, Dijkstra, Prim or Kruskal.'],['analyzer','Performance Analyzer','Benchmark the sorting algorithms on your own browser and hardware.'],['complexity','Complexity Explorer','Compare Big-O growth rates as the input size n changes.'],['race','Algorithm Race','Run all sorts on one input and rank the measured times.']];
const get=(k,d)=>{try{return JSON.parse(localStorage.getItem(k))??d}catch(e){return d}};
function decorate(){
 const nav=$('nav');if(!nav)return;
 nav.querySelectorAll('.grp').forEach(g=>{const i=g.querySelector('.navicon');if(i&&IC[g.dataset.c])i.innerHTML=IC[g.dataset.c];});
 let h=$('homelink');if(!h){h=document.createElement('a');h.id='homelink';h.className='homelink';h.href='#/';h.innerHTML='<span class="navicon">'+IC.home+'</span>Home';nav.before(h);}
 h.classList.toggle('cur',!CUR.replace(/^#\/?/,''));}
function remember(){const m=CUR.replace(/^#\/?/,'').split('/');if(m.length<2||m[0]==='quiz'||!m[1])return;const a=document.querySelector(`#nav a[href="${CUR}"]`);if(!a)return;
 const r=get('dsa-recent',[]).filter(x=>x.h!==CUR);r.unshift({h:CUR,t:a.textContent.trim()});try{localStorage.setItem('dsa-recent',JSON.stringify(r.slice(0,4)))}catch(e){}}
function home(){
 const rec=get('dsa-recent',[]),best=k=>{try{return localStorage.getItem('dsa-best-'+k)}catch(e){return null}};
 const mod=k=>`<div class="card"><b>${CAT[k].t}</b><p>${CAT[k].d}</p><div class="chips">${CAT[k].items.map(m=>`<a href="#/${k}/${m}">${INFO[m][0]}</a>`).join('')}</div></div>`;
 return `<div class="home-head"><h1>Workspace</h1><p>Step through algorithms, explore data structures and measure how they perform.</p></div>
 <div class="home-sec">Core topics</div><div class="home-grid">${Object.keys(CAT).map(mod).join('')}</div>
 <div class="home-grid"><div class="panel"><div class="panel-head"><div><h2>Continue learning</h2><div class="sub">Your most recently opened modules</div></div></div>${rec.length?rec.map(r=>`<a class="row" href="${r.h}" style="color:inherit;text-decoration:none"><span>${r.t}</span><span>Open →</span></a>`).join(''):'<div class="sub">Nothing yet. Open a visualizer and it will appear here.</div>'}</div>
 <div class="panel"><div class="panel-head"><div><h2>Quiz</h2><div class="sub">Best score per topic on this device</div></div><a class="btn primary" href="#/quiz" style="text-decoration:none">Start quiz</a></div>${[['all','All topics'],['sorting','Sorting'],['searching','Searching'],['structures','Data structures']].map(([k,t])=>`<div class="row"><span>${t}</span><span>${best(k)!==null?best(k)+'%':'Not attempted'}</span></div>`).join('')}</div></div>
 <div class="home-sec">Advanced labs</div><div class="home-grid">${LABS.map(l=>`<a class="card" href="#/advanced/${l[0]}"><b>${l[1]}</b><p>${l[2]}</p></a>`).join('')}</div>`;}
const wrapped=window.route;
window.route=function(){wrapped.apply(this,arguments);try{const c=CUR.replace(/^#\/?/,'').split('/')[0];OPEN.clear();if(CAT[c]||c==='advanced')OPEN.add(c);syncNav(c);decorate();remember();if(!CUR.replace(/^#\/?/,'')){$('page').innerHTML=home();$('page').hidden=false;}}catch(e){console.error(e)}};
/* global search */
function items(){return [...document.querySelectorAll('#nav .navbtn,#nav .grp-t')].map(a=>({h:a.getAttribute('href'),t:a.textContent.trim(),g:(a.closest('.grp')?.querySelector('.grp-t')?.textContent||'').trim()}));}
const box=$('gsearch'),out=$('gresults');let sel=0;
function show(){const q=box.value.trim().toLowerCase();if(!q){out.hidden=true;return;}
 const r=items().filter(i=>(i.t+' '+i.g).toLowerCase().includes(q)).slice(0,7);sel=0;
 out.innerHTML=r.length?r.map((i,n)=>`<a href="${i.h}" class="${n?'':'on'}">${i.t}<small>${i.g===i.t?'':i.g}</small></a>`).join(''):'<p>No matching topics.</p>';out.hidden=false;}
box.addEventListener('input',show);
box.addEventListener('keydown',e=>{const a=[...out.querySelectorAll('a')];if(e.key==='ArrowDown'||e.key==='ArrowUp'){e.preventDefault();sel=(sel+(e.key==='ArrowDown'?1:-1)+a.length)%(a.length||1);a.forEach((x,i)=>x.classList.toggle('on',i===sel));}
 else if(e.key==='Enter'&&a[sel]){e.preventDefault();nav(a[sel].getAttribute('href'));box.value='';out.hidden=true;box.blur();}else if(e.key==='Escape'){box.value='';out.hidden=true;box.blur();}});
out.addEventListener('click',()=>{box.value='';out.hidden=true;});document.addEventListener('click',e=>{if(!e.target.closest('.gsearch'))out.hidden=true;});
document.addEventListener('keydown',e=>{if((e.key==='/'||(e.key==='k'&&(e.ctrlKey||e.metaKey)))&&!/INPUT|SELECT|TEXTAREA/.test(e.target.tagName)){e.preventDefault();box.focus();}});
window.route();
})();
