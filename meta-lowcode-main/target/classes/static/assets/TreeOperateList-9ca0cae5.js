import{R as e,r as l,v as t,p as a,n as s,l as i,Y as n,o as r,i as o,w as u,c as p,g as c,t as m,X as d,E as v,a as f,a0 as j,z as h,f as b,A as g,F as y}from"./@vue-1ad4818c.js";import{_,j as k}from"./index-ea085229.js";import{b as x}from"./element-plus-bab58d46.js";import"./vue3-manner-report-313033e6.js";import"./axios-85bcd05e.js";import"./crypto-js-43a3a23e.js";import"./@babel-6bd44fd1.js";import"./cron-parser-1391a961.js";import"./luxon-b01799b6.js";import"./vue-router-414ebc36.js";import"./nprogress-c04e6182.js";import"./pinia-3f60a8e1.js";import"./sortablejs-2f5483e3.js";import"./qrcodejs2-fix-bab07f30.js";import"./@element-plus-2a6d398c.js";import"./moment-bafe441d.js";import"./pinyin-pro-c81f5d39.js";import"./vue-i18n-0843b5c5.js";import"./@intlify-2528d766.js";import"./source-map-b06bd4b9.js";import"./vue-c723f8b1.js";import"./@vueuse-477b4dbb.js";import"./pinia-plugin-persistedstate-d2bd58cf.js";import"./resize-observer-polyfill-ad543aa3.js";import"./vue-smart-widget-8b3f5b0a.js";import"./echarts-b5d24e9a.js";import"./zrender-a15b073b.js";import"./tslib-a4e99503.js";import"./echarts-liquidfill-f5c863f1.js";import"./@antv-43d380be.js";import"./ant-design-vue-2db0c9af.js";import"./lodash-es-b8e8104a.js";import"./@ant-design-60f2ef8e.js";import"./@ctrl-4982d949.js";import"./dayjs-7fb79ad3.js";import"./vue-types-6bcea8eb.js";import"./dom-align-78cb5391.js";import"./vue-draggable-next-a413f884.js";import"./vue-resize-observer-90aea344.js";import"./@popperjs-b78c3215.js";import"./async-validator-cf877c1f.js";import"./memoize-one-63ab667a.js";import"./escape-html-3148dbc9.js";import"./normalize-wheel-es-3222b0a2.js";import"./@floating-ui-0ef99041.js";const C={class:"fields-list",style:{height:"100%"}},w={class:"fields-list-header"},E={class:"fields-list-box"},F={class:"title fl"},N={class:"section-fr fr"},z={class:"btn-icon-t1"},$=["title"],I={class:"op-icon-box"},M=["onClick"],T=["onClick"],B=["onClick"],L=["onClick"],V=["onClick"],q=_({__name:"TreeOperateList",props:{title:{type:String,default:""},getTreeFn:{type:Function},getMainFn:{type:Function},saveFn:{type:Function}},setup(_){const q=_,A=e("$ElMessage");l("");let D=l(!1),K=l([]),P=t({children:"children",label:"label"}),R=l(""),G=l({}),H=l(!1),J=l([]);a((()=>{O()}));const O=async()=>{D.value=!0,H.value=!0;let e=await q.getTreeFn();e?(K.value=S(e.data||[]),K.value.length>0?(G.value=K.value[0].children[0],s((()=>{R.value.setCurrentKey("1-1"),Y()}))):H.value=!1):H.value=!1,D.value=!1},S=e=>{let l=[];return e.forEach(((e,t)=>{let a={label:e.entityLabel,name:e.entityName,$inx:`${t+1}`,children:[]};e.fieldList.forEach(((l,s)=>{let i={label:l.fieldLabel,name:l.fieldName,parentName:e.entityName,$inx:`${t+1}-${s+1}`};a.children.push(i)})),a.children.length>0&&l.push(a)})),l},X=e=>{e.children||(G.value=e,Y())},Y=async()=>{H.value=!0;let e=await q.getMainFn(G.value.parentName,G.value.name);e&&(J.value=e.data||[]),H.value=!1},Z=(e,l,t)=>{let a="",s=null;"edit"==l?(a="请修改选项名称",s=t.label):a="请输入选项名称",x.prompt(a,"提示",{confirmButtonText:"确定",cancelButtonText:"取消",inputValue:s,inputPattern:/^[A-Za-z\u4e00-\u9fa5\uff0c\u3001\uff1b\uff1a\uff08\uff09\u2014\u201c\u201d\d]+$/,inputErrorMessage:"输入不正确",beforeClose:(t,a,s)=>{if("confirm"===t&&a.inputValue){let t={label:a.inputValue,value:Q(),saved:!1};for(let e=0;e<J.value.length;e++){if(J.value[e].label==t.label)return void A.warning("已存在【"+a.inputValue+"】选项")}"ins"==l?J.value.splice(e+1,0,t):"add"==l?J.value.push(t):J.value[e].label=t.label}s()}}).then((()=>{W()})).catch((()=>{}))},Q=()=>{let e=0;return J.value.forEach(((l,t)=>{l.value>e&&(e=l.value)})),e+1},U=(e,l)=>{let t={...J.value[e-1]},a={...J.value[e+1]};"top"==l?0==e?A.warning("已经在最上面了"):(J.value[e-1]=J.value[e],J.value[e]=t):e==J.value.length-1?A.warning("已经在最下面面了"):(J.value[e+1]=J.value[e],J.value[e]=a),W()},W=async e=>{"多选项管理"==q.title&&J.value.map((e=>{e.value!=e.label&&(e.value=e.label)})),H.value=!0,await q.saveFn(G.value.parentName,G.value.name,J.value)&&(A.success(e||"保存成功"),Y()),H.value=!1};return(e,l)=>{const t=i("el-tree"),a=i("el-scrollbar"),s=i("el-aside"),O=i("ElIconPlus"),S=i("el-icon"),Y=i("el-button"),Q=i("el-header"),ee=i("el-empty"),le=i("ElIconTop"),te=i("ElIconBottom"),ae=i("ElIconEdit"),se=i("ElIconDelete"),ie=i("el-main"),ne=i("el-container"),re=n("loading");return r(),o(ne,{class:"main-container"},{default:u((()=>[p(s,{width:"300px"},{default:u((()=>[c("div",C,[c("div",w,m(_.title),1),d((r(),f("div",E,[p(a,null,{default:u((()=>[p(t,{ref_key:"treeRefs",ref:R,data:v(K),props:v(P),"highlight-current":"","empty-text":"没有字段数据","check-strictly":"","node-key":"$inx",onNodeClick:X},null,8,["data","props"])])),_:1})])),[[re,v(D)]])])])),_:1}),p(ne,{class:"main-container"},{default:u((()=>[p(Q,{class:"main-header w-100"},{default:u((()=>[c("div",F,m(v(G).label),1),c("div",N,[p(Y,{onClick:l[0]||(l[0]=j((e=>Z(!1,"add")),["stop"])),disabled:v(K).length<1},{default:u((()=>[c("span",z,[p(S,null,{default:u((()=>[p(O)])),_:1})]),h(" 新增选项 ")])),_:1},8,["disabled"])])])),_:1}),d((r(),o(ie,{class:"mian-box"},{default:u((()=>[p(a,null,{default:u((()=>[0==v(J).length?(r(),o(ee,{key:0,description:"没有数据"})):b("",!0),(r(!0),f(y,null,g(v(J),((e,l)=>(r(),f("div",{class:"op-item",key:l},[c("div",{class:"op-item-text yichu",title:e.label},m(e.label),9,$),c("div",I,[c("span",{title:"插入",onClick:j((e=>Z(l,"ins")),["stop"])},[p(S,null,{default:u((()=>[p(O)])),_:1})],8,M),c("span",{title:"上移",onClick:j((e=>U(l,"top")),["stop"])},[p(S,null,{default:u((()=>[p(le)])),_:1})],8,T),c("span",{title:"下移",onClick:j((e=>U(l,"down")),["stop"])},[p(S,null,{default:u((()=>[p(te)])),_:1})],8,B),c("span",{title:"编辑",onClick:j((t=>Z(l,"edit",e)),["stop"])},[p(S,null,{default:u((()=>[p(ae)])),_:1})],8,L),c("span",{title:"删除",onClick:j((t=>((e,l)=>{x.confirm("确定删除该选项?","提示").then((async()=>{if(!l.saved)return J.value.splice(e,1),void A.success("删除成功");if("单选项管理"==q.title){let t=await k.get("/systemManager/optionCanBeDeleted",{entity:G.value.parentName,field:G.value.name,value:l.value});t&&(t.data?(W("删除成功"),J.value.splice(e,1)):A.warning("该选项有数据正在使用，无法删除。"))}else J.value.splice(e,1),W("删除成功")})).catch((()=>{}))})(l,e)),["stop"])},[p(S,null,{default:u((()=>[p(se)])),_:1})],8,V)])])))),128))])),_:1})])),_:1})),[[re,v(H)]])])),_:1})])),_:1})}}},[["__scopeId","data-v-770ceef0"]]);export{q as default};
