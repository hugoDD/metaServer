import{_ as e,f as l,n as a}from"./index-ea085229.js";import{m as t}from"./index-b30b6746.js";import{s as u}from"./pinia-3f60a8e1.js";import{R as o,r as i,p as n,v as s,l as d,Y as r,X as c,E as m,o as p,a as v,c as f,w as b,F as g,A as y,i as C,g as F,t as j,z as _,f as h,G as V,e as N,bh as k,bf as w}from"./@vue-1ad4818c.js";import"./element-plus-bab58d46.js";import"./lodash-es-b8e8104a.js";import"./@vueuse-477b4dbb.js";import"./@element-plus-2a6d398c.js";import"./@babel-6bd44fd1.js";import"./@popperjs-b78c3215.js";import"./@ctrl-4982d949.js";import"./dayjs-7fb79ad3.js";import"./async-validator-cf877c1f.js";import"./memoize-one-63ab667a.js";import"./escape-html-3148dbc9.js";import"./normalize-wheel-es-3222b0a2.js";import"./@floating-ui-0ef99041.js";import"./vue3-manner-report-313033e6.js";import"./axios-85bcd05e.js";import"./crypto-js-43a3a23e.js";import"./cron-parser-1391a961.js";import"./luxon-b01799b6.js";import"./vue-router-414ebc36.js";import"./nprogress-c04e6182.js";import"./sortablejs-2f5483e3.js";import"./qrcodejs2-fix-bab07f30.js";import"./moment-bafe441d.js";import"./pinyin-pro-c81f5d39.js";import"./vue-i18n-0843b5c5.js";import"./@intlify-2528d766.js";import"./source-map-b06bd4b9.js";import"./vue-c723f8b1.js";import"./pinia-plugin-persistedstate-d2bd58cf.js";import"./resize-observer-polyfill-ad543aa3.js";import"./vue-smart-widget-8b3f5b0a.js";import"./echarts-b5d24e9a.js";import"./zrender-a15b073b.js";import"./tslib-a4e99503.js";import"./echarts-liquidfill-f5c863f1.js";import"./@antv-43d380be.js";import"./ant-design-vue-2db0c9af.js";import"./@ant-design-60f2ef8e.js";import"./vue-types-6bcea8eb.js";import"./dom-align-78cb5391.js";import"./vue-draggable-next-a413f884.js";import"./vue-resize-observer-90aea344.js";const x=e=>(k("data-v-f6bee06b"),e=e(),w(),e),M={class:"action-div"},T=x((()=>F("div",{class:"info-text"},"仅会聚合到符合条件的数据上",-1))),E={key:0,class:"w-100 mb-10"},L={class:"uptade-rule-span"},I={class:"uptade-rule-span"},U=["onClick"],S=x((()=>F("div",{class:"w-100 info-text mt-3"},"目标字段",-1))),A=x((()=>F("div",{class:"w-100 info-text mt-3"},"源字段",-1))),z={key:0,class:"w-100 mb-10"},D={class:"uptade-rule-span"},$={class:"uptade-rule-span"},q=["onClick"],O=x((()=>F("div",{class:"w-100 info-text mt-3"},"目标字段",-1))),P=x((()=>F("div",{class:"w-100 info-text mt-3"},"聚合方式",-1))),R={key:2,class:"w-100 info-text mt-3"},J=x((()=>F("div",{class:"info-text"},"仅会聚合符合过滤条件的数据",-1))),B={key:0},G={key:1},H=e({__name:"groupAggregation",props:{modelValue:null},setup(e){const k=e;o("$API");const w=o("$ElMessage"),{allEntityName:x,unSystemEntityList:H}=u(l());let K=i(!1),X=i({actionContent:{}});n((()=>{X.value=k.modelValue,X.value.entityName=x.value[X.value.entityCode],X.value.actionContent.filter||(X.value.actionContent.filter={items:[]}),X.value.actionContent.groupItem||(X.value.actionContent.groupItem=[]),X.value.actionContent.targetFilter||(X.value.actionContent.targetFilter={items:[]}),ae()}));let Y=i([]),Z=i([]),Q=i({}),W=i([]),ee=i({}),le=i(!1);const ae=async()=>{K.value=!0,Promise.all([te(),ue()]).then((()=>{if(K.value=!1,Y.value.length>0){let e=0;if(X.value.isOnSave){let{actionContent:l}=X.value;Y.value.forEach(((a,t)=>{a.name==l.entityName&&(e=t)}))}X.value.defaultTargetEntity=H.value[e],oe(H.value[e].entityCode)}}))},te=()=>new Promise((async(e,l)=>{Y.value=[],H.value&&H.value.length>0&&(Y.value=H.value.map(((e,l)=>(e.entityInx=l,e)))),e()})),ue=()=>new Promise((async(e,l)=>{let t=await a(X.value.entityCode,!0,!0);t&&(W.value=t.data,t.data.forEach((e=>{ee.value[e.fieldName]=e.fieldLabel}))),e()})),oe=async e=>{var l;le.value=!0;let t=await a(e,!1,!0,!0);t&&(Z.value=[],t.data.forEach((e=>{e.fieldType&&(Q.value[e.fieldName]=e.fieldLabel,Z.value.push(e))})),qe().length>0?(ie.value=qe()[0],ne.targetField=qe()[0].fieldName,de.value=Te(qe()[0].fieldName),ne.calcMode=ce()[0].value,"forCompile"!==ne.calcMode&&(ne.sourceField=null==(l=Ee()[0])?void 0:l.fieldName),Ce()):(ie.value={},ne.targetField="",ne.calcMode="",ne.sourceField=""),Pe().length>0&&(Re.value=Pe()[0],Ke())),le.value=!1};let ie=i({}),ne=s({targetField:"",calcMode:"",sourceField:"",simpleFormula:!1});const se=s({forCompile:"计算公式",concatSet:"去重拼接",concat:"拼接",min:"最小值",max:"最大值",average:"平均值",countSet:"去重计数",count:"计数",sum:"求和"});let de=i("");const re=e=>{var l;ne.targetField=e.fieldName,de.value=Te(e.fieldName),ne.calcMode=ce()[0].value,"forCompile"!==ne.calcMode?ne.sourceField=null==(l=Ee()[0])?void 0:l.fieldName:ne.sourceField=""},ce=()=>he.value.includes(de.value)?[{label:"求和",value:"sum"},{label:"计数",value:"count"},{label:"去重计数",value:"countSet"},{label:"平均值",value:"average"},{label:"最大值",value:"max"},{label:"最小值",value:"min"},{label:"计算公式",value:"forCompile"}]:[{label:"计数",value:"count"},{label:"去重计数",value:"countSet"},{label:"拼接",value:"concat"},{label:"去重拼接",value:"concatSet"},{label:"计算公式",value:"forCompile"}],me=e=>{var l;ne.sourceField="forCompile"!=e?null==(l=Ee()[0])?void 0:l.fieldName:""};let pe=i([]);const ve=()=>{let{targetField:e,calcMode:l,sourceField:a,simpleFormula:t,updateMode:u}=ne;if(!e)return;if("toNull"!=u&&!a)return;if(X.value.actionContent.fieldName+"."+e==a)return void w.warning("目标字段与源字段不能为同一字段!");if(X.value.actionContent.items.filter((l=>l.targetField==e)).length>0)return void w.warning("目标字段重复!");let o="";"forCompile"!=l||t||(o=ke.value),X.value.actionContent.items.push({targetField:e,calcMode:l,sourceField:o||a,simpleFormula:t}),pe.value.push({targetField:fe(e),calcMode:be(l),sourceField:ge(ne),simpleFormula:t}),"forCompile"==l&&(ne.sourceField="")},fe=e=>Q.value[e],be=e=>se[e],ge=e=>"forCompile"==e.calcMode?e.simpleFormula?e.sourceField:ye(e.sourceField):ee.value[e.sourceField],ye=e=>{let l=e,a=l.match(/{([a-zA-Z0-9$\.]+)}/g);return a?(a.forEach((e=>{let a=e.substring(1);a=a.substring(0,a.length-1);let t=a.split("$"),u=`{${ee.value[t[0]]}(${se[t[1]]})}`;l=l.replace(e,u)})),l):l},Ce=()=>{pe.value=[],X.value.actionContent.items.forEach((e=>{pe.value.push({targetField:fe(e.targetField),calcMode:be(e.calcMode),sourceField:ge(e),simpleFormula:e.simpleFormula})}))};let Fe=i(!1),je=i([]),_e=i(""),he=i(["Integer","Decimal","Percent","Money"]),Ve=i(["Text","TextArea"]),Ne=i(!1),ke=i({});const we=()=>{xe(W.value,!0,ne.sourceField)},xe=(e,l,a)=>{Fe.value=!0,je.value=e,Ne.value=l,_e.value=l?a:"",ne.simpleFormula=l},Me=e=>{ke.value=e,ne.sourceField=e.label},Te=e=>Z.value.filter((l=>l.fieldName==e))[0].fieldType,Ee=()=>{if(["sum","average","max","min"].includes(ne.calcMode)){let e=[];return W.value.forEach((l=>{he.value.includes(l.fieldType)&&e.push(l)})),e}return W.value};let Le=i(!1),Ie=i({}),Ue=i("");const Se=e=>{Ue.value=e;let l=X.value.actionContent[e];l=Ae(l),Ie.value=JSON.parse(JSON.stringify(l)),Le.value=!0},Ae=e=>{let{equation:l}=e;return l&&"OR"!==l?"AND"===l?(e.type=2,e.equation="AND"):e.type=3:(e.type=1,e.equation="OR"),e},ze=e=>{let l=X.value.actionContent[e],a=l&&l.items?l.items.length:0;return a>0?`已设置条件（${a}）`:"点击设置"},De=e=>{X.value.actionContent[Ue.value]=e,Le.value=!1},$e=()=>{let e=X.value.defaultTargetEntity;X.value.actionContent.entityName=e.entityName,X.value.actionContent.fieldName=e.fieldName,X.value.actionContent.items=[],e.entityCode?oe(e.entityCode):(Z.value=W.value,ie.value=Z.value[0])},qe=()=>Z.value.filter((e=>e.fieldType&&he.value.includes(e.fieldType)));let Oe=i([]);const Pe=()=>Z.value.filter((e=>e.fieldType&&(Ve.value.includes(e.fieldType)||he.value.includes(e.fieldType)||"Reference"==e.fieldType)||"Date"==e.fieldType||"DateTime"==e.fieldType));let Re=i({}),Je=i({});const Be=()=>{Ge().length>0?Je.value=Ge()[0]:Je.value={}},Ge=()=>{let{fieldType:e}=Re.value;return W.value.filter((l=>l.fieldType==e))},He=()=>{var e,l;let a=null==(e=Re.value)?void 0:e.fieldName,t=null==(l=Je.value)?void 0:l.fieldName;a&&t&&(X.value.actionContent.groupItem.filter((e=>e.targetField==a)).length>0?w.warning("目标字段重复!"):(Oe.value.push({targetField:Re.value,sourceField:Je.value}),X.value.actionContent.groupItem.push({targetField:a,targetFieldName:Re.value.fieldLabel,sourceField:t,sourceFieldName:Je.value.fieldLabel})))},Ke=()=>{Oe.value=[],X.value.actionContent.groupItem.forEach((e=>{Oe.value.push({targetField:{fieldLabel:e.targetFieldName},sourceField:{fieldLabel:e.sourceFieldName}})}))},Xe=()=>{var e;let l=null==(e=X.value.defaultTargetEntity)?void 0:e.name;return W.value.filter((e=>"Reference"==e.fieldType&&e.referenceName==l))};return(e,l)=>{const a=d("el-option"),u=d("el-select"),o=d("el-col"),i=d("el-row"),n=d("el-form-item"),s=d("ElIconCloseBold"),k=d("el-icon"),w=d("el-button"),x=d("el-input"),H=d("el-checkbox"),Z=d("mlSetConditions"),Q=d("mlDialog"),W=r("loading");return c((p(),v("div",M,[f(n,{label:"目标实体"},{default:b((()=>[f(i,{class:"w-100 mb-15",gutter:20},{default:b((()=>[f(o,{span:9},{default:b((()=>[f(u,{modelValue:m(X).defaultTargetEntity,"onUpdate:modelValue":l[0]||(l[0]=e=>m(X).defaultTargetEntity=e),filterable:"",class:"w-100",onChange:$e,disabled:m(X).isOnSave,"value-key":"entityInx"},{default:b((()=>[(p(!0),v(g,null,y(m(Y),((e,l)=>(p(),C(a,{key:l,label:e.label,value:e},null,8,["label","value"])))),128))])),_:1},8,["modelValue","disabled"])])),_:1})])),_:1})])),_:1}),f(n,{class:"mt-20",label:"聚合目标条件"},{default:b((()=>[f(i,null,{default:b((()=>[f(o,{span:24},{default:b((()=>[F("span",{class:"ml-a-span",onClick:l[1]||(l[1]=e=>Se("targetFilter"))},j(ze("targetFilter")),1)])),_:1}),f(o,{span:24},{default:b((()=>[T])),_:1})])),_:1})])),_:1}),f(n,{class:"mt-20"},{label:b((()=>[_("分组字段关联")])),default:b((()=>[m(Oe).length>0?(p(),v("div",E,[(p(!0),v(g,null,y(m(Oe),((e,l)=>(p(),C(i,{gutter:20,class:"uptade-rule-row w-100 mb-5",key:l},{default:b((()=>[f(o,{span:9},{default:b((()=>{var l;return[F("span",L,j(null==(l=e.targetField)?void 0:l.fieldLabel),1)]})),_:2},1024),f(o,{span:9,class:"uptade-rule-col-last"},{default:b((()=>{var a;return[F("span",I,[_(j(null==(a=e.sourceField)?void 0:a.fieldLabel)+" ",1),F("span",{class:"del-icon",onClick:e=>(e=>{X.value.actionContent.groupItem.splice(e,1),Oe.value.splice(e,1)})(l)},[f(k,null,{default:b((()=>[f(s)])),_:1})],8,U)])]})),_:2},1024)])),_:2},1024)))),128))])):h("",!0),c((p(),C(i,{class:"w-100 mb-10 uptade-rule",gutter:20},{default:b((()=>[f(o,{span:9},{default:b((()=>[f(u,{modelValue:m(Re),"onUpdate:modelValue":l[2]||(l[2]=e=>V(Re)?Re.value=e:Re=e),filterable:"",class:"w-100","value-key":"fieldLabel",onChange:Be},{default:b((()=>[(p(!0),v(g,null,y(Pe(),((e,l)=>(p(),C(a,{key:l,label:e.fieldLabel,value:e},null,8,["label","value"])))),128))])),_:1},8,["modelValue"]),S])),_:1}),f(o,{span:9},{default:b((()=>[f(u,{modelValue:m(Je),"onUpdate:modelValue":l[3]||(l[3]=e=>V(Je)?Je.value=e:Je=e),filterable:"",class:"w-100","value-key":"fieldLabel"},{default:b((()=>[(p(!0),v(g,null,y(Ge(),((e,l)=>(p(),C(a,{key:l,label:e.fieldLabel,value:e},null,8,["label","value"])))),128))])),_:1},8,["modelValue"]),A])),_:1})])),_:1})),[[W,m(le)]])])),_:1}),f(n,{label:" "},{default:b((()=>[f(w,{type:"primary",plain:"",onClick:He},{default:b((()=>[_("+ 添加")])),_:1})])),_:1}),f(n,{class:"mt-20"},{label:b((()=>[_("聚合规则")])),default:b((()=>[m(pe).length>0?(p(),v("div",z,[(p(!0),v(g,null,y(m(pe),((e,l)=>(p(),C(i,{gutter:20,class:"uptade-rule-row w-100 mb-5",key:l},{default:b((()=>[f(o,{span:9},{default:b((()=>[F("span",D,j(e.targetField),1)])),_:2},1024),f(o,{span:5},{default:b((()=>[F("span",$,j(e.calcMode),1)])),_:2},1024),f(o,{span:9,class:"uptade-rule-col-last"},{default:b((()=>[F("span",{class:N(["uptade-rule-span",{toFixed:"toFixed"==e.calcMode,forCompile:"forCompile"==e.calcMode}])},[_(j(e.sourceField)+" ",1),F("span",{class:"del-icon",onClick:e=>(e=>{X.value.actionContent.items.splice(e,1),pe.value.splice(e,1)})(l)},[f(k,null,{default:b((()=>[f(s)])),_:1})],8,q)],2)])),_:2},1024)])),_:2},1024)))),128))])):h("",!0),c((p(),C(i,{class:"w-100 mb-10 uptade-rule",gutter:20},{default:b((()=>[f(o,{span:9},{default:b((()=>[f(u,{modelValue:m(ie),"onUpdate:modelValue":l[4]||(l[4]=e=>V(ie)?ie.value=e:ie=e),filterable:"",class:"w-100",onChange:re,"value-key":"fieldLabel"},{default:b((()=>[(p(!0),v(g,null,y(qe(),((e,l)=>(p(),C(a,{key:l,label:e.fieldLabel,value:e},null,8,["label","value"])))),128))])),_:1},8,["modelValue"]),O])),_:1}),f(o,{span:5},{default:b((()=>[f(u,{modelValue:m(ne).calcMode,"onUpdate:modelValue":l[5]||(l[5]=e=>m(ne).calcMode=e),class:"w-100",onChange:me},{default:b((()=>[(p(!0),v(g,null,y(ce(),((e,l)=>(p(),C(a,{key:l,label:e.label,value:e.value},null,8,["label","value"])))),128))])),_:1},8,["modelValue"]),P])),_:1}),f(o,{span:9},{default:b((()=>["forCompile"!==m(ne).calcMode?(p(),C(u,{key:0,modelValue:m(ne).sourceField,"onUpdate:modelValue":l[6]||(l[6]=e=>m(ne).sourceField=e),filterable:"",class:"w-100"},{default:b((()=>[(p(!0),v(g,null,y(Ee(),((e,l)=>(p(),C(a,{key:l,label:e.fieldLabel,value:e.fieldName},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])):(p(),C(x,{key:1,modelValue:m(ne).sourceField,"onUpdate:modelValue":l[7]||(l[7]=e=>m(ne).sourceField=e),placeholder:"计算公式",autosize:"",type:"textarea",onClick:we},null,8,["modelValue"])),"toNull"!==m(ne).calcMode?(p(),v("div",R,j("forCompile"==ne.calcMode?"计算公式":"聚合字段"),1)):h("",!0)])),_:1})])),_:1})),[[W,m(le)]])])),_:1}),f(n,{label:" "},{default:b((()=>[f(w,{type:"primary",plain:"",onClick:ve},{default:b((()=>[_("+ 添加")])),_:1})])),_:1}),f(n,{class:"mt-20",label:"聚合数据条件"},{default:b((()=>[f(i,null,{default:b((()=>[f(o,{span:24},{default:b((()=>[F("span",{class:"ml-a-span",onClick:l[8]||(l[8]=e=>Se("filter"))},j(ze("filter")),1)])),_:1}),f(o,{span:24},{default:b((()=>[J])),_:1}),f(o,{class:"mt-10"},{default:b((()=>[f(H,{modelValue:m(X).actionContent.autoCreate,"onUpdate:modelValue":l[9]||(l[9]=e=>m(X).actionContent.autoCreate=e)},{default:b((()=>[_("目标记录不存在时自动新建")])),_:1},8,["modelValue"])])),_:1})])),_:1})])),_:1}),f(n,{class:"mt-20",label:"聚合后回填"},{default:b((()=>[f(i,null,{default:b((()=>[f(u,{modelValue:m(X).actionContent.callbackField,"onUpdate:modelValue":l[10]||(l[10]=e=>m(X).actionContent.callbackField=e),class:"m-2",placeholder:"(可选)",clearable:""},{default:b((()=>[(p(!0),v(g,null,y(Xe(),(e=>(p(),C(a,{key:e.fieldName,label:e.fieldLabel,value:e.fieldName},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1})])),_:1}),m(Fe)?(p(),v("div",B,[f(t,{modelValue:m(Fe),"onUpdate:modelValue":l[11]||(l[11]=e=>V(Fe)?Fe.value=e:Fe=e),fields:m(je),defaultFormulaVal:m(_e),isAdvanced:m(Ne),onConfirm:Me},null,8,["modelValue","fields","defaultFormulaVal","isAdvanced"])])):h("",!0),m(Le)?(p(),v("div",G,[f(Q,{title:"聚合数据条件","append-to-body":"",width:"37%",modelValue:m(Le),"onUpdate:modelValue":l[14]||(l[14]=e=>V(Le)?Le.value=e:Le=e)},{default:b((()=>[f(Z,{modelValue:m(Ie),"onUpdate:modelValue":l[12]||(l[12]=e=>V(Ie)?Ie.value=e:Ie=e),footer:"",onCancel:l[13]||(l[13]=e=>V(Le)?Le.value=!1:Le=!1),onConfirm:De,entityName:m(X).entityName},null,8,["modelValue","entityName"])])),_:1},8,["modelValue"])])):h("",!0)])),[[W,m(K)]])}}},[["__scopeId","data-v-f6bee06b"]]);export{H as default};
