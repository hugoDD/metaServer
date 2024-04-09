import{_ as e,n as l,aC as a,Y as t,U as o}from"./index-ea085229.js";import{m as i}from"./index-b30b6746.js";import{R as u,r as d,p as r,v as s,l as n,Y as p,X as m,E as c,o as f,a as v,c as F,w as b,F as y,A as g,i as j,z as h,g as N,t as V,e as M,f as C,G as x,bh as w,bf as T}from"./@vue-1ad4818c.js";import"./element-plus-bab58d46.js";import"./lodash-es-b8e8104a.js";import"./@vueuse-477b4dbb.js";import"./@element-plus-2a6d398c.js";import"./@babel-6bd44fd1.js";import"./@popperjs-b78c3215.js";import"./@ctrl-4982d949.js";import"./dayjs-7fb79ad3.js";import"./async-validator-cf877c1f.js";import"./memoize-one-63ab667a.js";import"./escape-html-3148dbc9.js";import"./normalize-wheel-es-3222b0a2.js";import"./@floating-ui-0ef99041.js";import"./vue3-manner-report-313033e6.js";import"./axios-85bcd05e.js";import"./crypto-js-43a3a23e.js";import"./cron-parser-1391a961.js";import"./luxon-b01799b6.js";import"./vue-router-414ebc36.js";import"./nprogress-c04e6182.js";import"./pinia-3f60a8e1.js";import"./sortablejs-2f5483e3.js";import"./qrcodejs2-fix-bab07f30.js";import"./moment-bafe441d.js";import"./pinyin-pro-c81f5d39.js";import"./vue-i18n-0843b5c5.js";import"./@intlify-2528d766.js";import"./source-map-b06bd4b9.js";import"./vue-c723f8b1.js";import"./pinia-plugin-persistedstate-d2bd58cf.js";import"./resize-observer-polyfill-ad543aa3.js";import"./vue-smart-widget-8b3f5b0a.js";import"./echarts-b5d24e9a.js";import"./zrender-a15b073b.js";import"./tslib-a4e99503.js";import"./echarts-liquidfill-f5c863f1.js";import"./@antv-43d380be.js";import"./ant-design-vue-2db0c9af.js";import"./@ant-design-60f2ef8e.js";import"./vue-types-6bcea8eb.js";import"./dom-align-78cb5391.js";import"./vue-draggable-next-a413f884.js";import"./vue-resize-observer-90aea344.js";const _=e=>(w("data-v-0b5b421b"),e=e(),T(),e),k={class:"action-div"},E={key:0,class:"w-100 mb-10"},U={class:"uptade-rule-span"},A={class:"uptade-rule-span"},R=["onClick"],Y=_((()=>N("div",{class:"w-100 info-text mt-3"},"目标字段",-1))),z=_((()=>N("div",{class:"w-100 info-text mt-3"},"更新方式",-1))),L={key:8,class:"w-100 info-text mt-3"},D={key:0},I=e({__name:"autoCreation",props:{modelValue:null},setup(e,{expose:w}){const T=e,_=u("$API"),I=u("$ElMessage");let O=d(!1),B=d({});r((()=>{B.value=T.modelValue,X()}));let P=d([]),$=d([]),S=d([]),q=d({}),G=d([]),H=d({}),J=d(!1),K=d(!1);const X=async()=>{O.value=!0,Promise.all([Z(),Q()]).then((()=>{if(O.value=!1,P.value.length>0){let e=0;if(B.value.isOnSave){let{actionContent:l}=B.value;P.value.forEach(((a,t)=>{a.fieldName==l.fieldName&&a.entityName==l.entityName&&(e=t)}))}B.value.defaultTargetEntity=P.value[e],W(P.value[e].entityCode)}}))},Z=()=>new Promise((async(e,l)=>{let a=await _.trigger.detial.dataAutoCreate(B.value.entityCode);a&&(P.value=[],a.data.forEach(((e,l)=>{e.entityInx=l,q.value[e.fieldName]=e.fieldLabel,P.value.push(e)}))),e()})),Q=()=>new Promise((async(e,a)=>{let t=await l(B.value.entityCode,!0,!0);t&&(G.value=t.data||[],t.data.forEach((e=>{H.value[e.fieldName]=e.fieldLabel}))),e()})),W=async e=>{var a;J.value=!0;let t=await l(e,!1,!1,!0);t&&($.value=t.data,S.value=[],t.data.forEach((e=>{e.isNullable||S.value.push(e)})),$.value&&$.value.length>0&&t.data.length>0&&(ee.value=t.data[0],le.targetField=t.data[0].fieldName,oe.value=Ve(t.data[0].fieldName),"Reference"==oe.value&&(le.updateMode="forField",le.sourceField={}),"Option"==oe.value&&(le.updateMode="toFixed",le.sourceField=""),"Tag"==oe.value&&(le.updateMode="toFixed",le.sourceField=[]),"forField"==le.updateMode&&(le.sourceField=null==(a=Me()[0])?void 0:a.fieldName),ve())),J.value=!1};let ee=d({}),le=s({targetField:"",updateMode:"forField",sourceField:"",simpleFormula:!1}),ae=d([{label:"字段值",value:"forField"},{label:"固定值",value:"toFixed"},{label:"置空",value:"toNull"},{label:"计算公式",value:"forCompile"}]);const te=s({forField:"字段值",toFixed:"固定值",toNull:"置空",forCompile:"计算公式"});let oe=d("");let ie=d([]),ue=d(!1);const de=async e=>{var l;if(le.targetField=e.fieldName,oe.value=Ve(e.fieldName),"forField"==le.updateMode?le.sourceField=null==(l=Me()[0])?void 0:l.fieldName:le.sourceField="","Tag"==e.fieldType||"Option"==e.fieldType){ue.value=!0;let l,a=B.value.defaultTargetEntity.entityName,i=e.fieldName;"Tag"==e.fieldType?(l=await t(a,i),le.updateMode="toFixed",le.sourceField=[]):(l=await o(a,i),le.updateMode="toFixed",le.sourceField={}),l&&l.data&&(ie.value=l.data),ue.value=!1}},re=e=>{var l;"forField"==e.value?le.sourceField=null==(l=Me()[0])?void 0:l.fieldName:"Reference"==oe.value||"Option"==oe.value?le.sourceField={}:"Tag"==oe.value?le.sourceField=[]:le.sourceField=null};let se=d([]);const ne=()=>{let{targetField:e,updateMode:l,sourceField:a,simpleFormula:t}=le;if(e&&("toNull"==l||a))if(B.value.actionContent.fieldName+"."+e!=a)if("mobilePhone"!=e||"toFixed"!=l||11==a.length&&!isNaN(a))if(B.value.actionContent.items.filter((l=>l.targetField==e)).length>0)I.warning("目标字段重复!");else{if("toNull"==l){let l=$.value.filter((l=>l.fieldName==e));if(!l[0].isNullable)return void I.warning("目标字段 [ "+l[0].fieldLabel+" ] 不能为空!")}B.value.actionContent.items.push({targetField:e,updateMode:l,sourceField:a,simpleFormula:t}),se.value.push({targetField:pe(e),updateMode:me(l),sourceField:ce(le),simpleFormula:t}),"forField"!=l&&(le.sourceField="")}else I.warning("手机号格式不正确!");else I.warning("目标字段与源字段不能为同一字段!")},pe=e=>q.value[e],me=e=>te[e],ce=e=>"forCompile"==e.updateMode?e.simpleFormula?e.sourceField:fe(e.sourceField):H.value[e.sourceField],fe=e=>{let l=e,a=l.match(/{([a-zA-Z0-9$\.]+)}/g);return a?(a.forEach((e=>{let a=e.substring(1);a=a.substring(0,a.length-1);let t=a.split("$");l=l.replace(e,`{${H.value[t[0]]}(${te[t[1]]})`)})),l):l},ve=()=>{se.value=[],B.value.actionContent.items.forEach((e=>{se.value.push({targetField:pe(e.targetField),calcMode:me(e.calcMode),sourceField:ce(e),simpleFormula:e.simpleFormula})}))};let Fe=d(!1),be=d([]),ye=d("");d(["Integer","Decimal","Percent","Money"]);let ge=d(!1);const je=()=>{he(G.value,!0,le.sourceField)},he=(e,l,a)=>{Fe.value=!0,be.value=e,ge.value=l,ye.value=l?a:"",le.simpleFormula=l},Ne=e=>{le.sourceField=e.label},Ve=e=>$.value.filter((l=>l.fieldName==e))[0].fieldType,Me=()=>{let{fieldType:e,referenceName:l}=ee.value;if(["Email","Url","TextArea","Text"].includes(e))return G.value.filter((e=>"Reference"!=e.fieldType));if("Reference"==e){let a=[];return G.value.forEach((t=>{t.fieldType==e&&t.referenceName==l&&a.push(t)})),a}{let l=[];return G.value.forEach((a=>{a.fieldType!=e&&"Text"!=a.fieldType||l.push(a)})),l.length<1?G.value.filter((e=>"Reference"!=e.fieldType)):l}},Ce=e=>{var l;return(null==(l=$.value.filter((l=>l.fieldName==e))[0])?void 0:l.fieldLabel)||"（该字段已删除）"},xe=e=>{var l;return null==(l=ae.value.filter((l=>l.value==e))[0])?void 0:l.label},we=e=>{var l,a;if("forField"!==e.updateMode){if(1==e.sourceField&&"Boolean"==oe.value)return"正常";if(0==e.sourceField&&"Boolean"==oe.value)return"禁用";if(e.sourceField&&e.sourceField.label)return e.sourceField.label;if(e.sourceField&&Array.isArray(e.sourceField)){return(e.sourceField.map((e=>e.label))||[]).join()}return e.sourceField}return null==(a=(null==(l=G.value)?void 0:l.filter((l=>l.fieldName==e.sourceField)))[0])?void 0:a.fieldLabel},Te=()=>{let e=B.value.defaultTargetEntity;B.value.actionContent.entityName=e.entityName,B.value.actionContent.fieldName=e.fieldName,B.value.actionContent.N=e.N,B.value.actionContent.items=[],e.entityCode?W(e.entityCode):($.value=G.value,ee.value=$.value[0])},_e=e=>{le.sourceField=e,K.value=!1};return w({requiredFields:S}),(e,l)=>{const t=n("el-option"),o=n("el-select"),u=n("el-col"),d=n("el-row"),r=n("el-form-item"),s=n("ElIconCloseBold"),w=n("el-icon"),T=n("el-date-picker"),_=n("el-input"),I=n("ElIconSearch"),S=n("el-button"),q=n("el-dialog"),G=p("loading");return m((f(),v("div",k,[F(r,{label:"目标实体"},{default:b((()=>[F(d,{class:"w-100 mb-15",gutter:20},{default:b((()=>[F(u,{span:9},{default:b((()=>[F(o,{modelValue:c(B).defaultTargetEntity,"onUpdate:modelValue":l[0]||(l[0]=e=>c(B).defaultTargetEntity=e),filterable:"",class:"w-100",onChange:Te,disabled:c(B).isOnSave,"value-key":"entityInx"},{default:b((()=>[(f(!0),v(y,null,g(c(P),((e,l)=>(f(),j(t,{key:l,label:e.label,value:e},null,8,["label","value"])))),128))])),_:1},8,["modelValue","disabled"])])),_:1})])),_:1})])),_:1}),F(r,null,{label:b((()=>[h("创建规则")])),default:b((()=>{var e,a;return[(null==(a=null==(e=c(B).actionContent)?void 0:e.items)?void 0:a.length)>0?(f(),v("div",E,[(f(!0),v(y,null,g(c(B).actionContent.items,((e,l)=>(f(),j(d,{gutter:20,class:"uptade-rule-row w-100 mb-5",key:l},{default:b((()=>[F(u,{span:9},{default:b((()=>[N("span",U,V(Ce(e.targetField)),1)])),_:2},1024),F(u,{span:5},{default:b((()=>[N("span",A,V(xe(e.updateMode)),1)])),_:2},1024),F(u,{span:9,class:"uptade-rule-col-last"},{default:b((()=>[N("span",{class:M(["uptade-rule-span",{toFixed:"toFixed"==e.updateMode,forCompile:"forCompile"==e.updateMode,toNull:"toNull"==e.updateMode}])},[h(V(we(e))+" ",1),N("span",{class:"del-icon",onClick:e=>(e=>{B.value.actionContent.items.splice(e,1),se.value.splice(e,1)})(l)},[F(w,null,{default:b((()=>[F(s)])),_:1})],8,R)],2)])),_:2},1024)])),_:2},1024)))),128))])):C("",!0),m((f(),j(d,{class:"w-100 mb-10 uptade-rule",gutter:20},{default:b((()=>[F(u,{span:9},{default:b((()=>[F(o,{modelValue:c(ee),"onUpdate:modelValue":l[1]||(l[1]=e=>x(ee)?ee.value=e:ee=e),filterable:"",class:"w-100",onChange:de,"value-key":"fieldLabel"},{default:b((()=>[(f(!0),v(y,null,g(c($),((e,l)=>(f(),j(t,{key:l,label:e.fieldLabel+(e.isNullable?"":"[必填]"),value:e},null,8,["label","value"])))),128))])),_:1},8,["modelValue"]),Y])),_:1}),F(u,{span:5},{default:b((()=>[F(o,{modelValue:c(le).updateMode,"onUpdate:modelValue":l[2]||(l[2]=e=>c(le).updateMode=e),class:"w-100",onChange:re},{default:b((()=>[(f(!0),v(y,null,g("Reference"==oe.value||"Option"==oe.value||"Tag"==oe.value?[{label:"字段值",value:"forField"},{label:"固定值",value:"toFixed"},{label:"置空",value:"toNull"}]:ae.value,((e,l)=>(f(),j(t,{key:l,label:e.label,value:e.value},null,8,["label","value"])))),128))])),_:1},8,["modelValue"]),z])),_:1}),F(u,{span:9},{default:b((()=>["forField"==c(le).updateMode?(f(),j(o,{key:0,modelValue:c(le).sourceField,"onUpdate:modelValue":l[3]||(l[3]=e=>c(le).sourceField=e),filterable:"",class:"w-100"},{default:b((()=>[(f(!0),v(y,null,g(Me(),((e,l)=>(f(),j(t,{key:l,label:e.fieldLabel,value:e.fieldName},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])):C("",!0),"toFixed"==c(le).updateMode&&"Boolean"==c(oe)?(f(),j(o,{key:1,modelValue:c(le).sourceField,"onUpdate:modelValue":l[4]||(l[4]=e=>c(le).sourceField=e),filterable:"",class:"w-100"},{default:b((()=>[F(t,{label:"正常",value:"1"}),F(t,{label:"禁用",value:"0"})])),_:1},8,["modelValue"])):C("",!0),"toFixed"!=c(le).updateMode||"DateTime"!=c(oe)&&"Time"!=c(oe)?C("",!0):(f(),j(T,{key:2,modelValue:c(le).sourceField,"onUpdate:modelValue":l[5]||(l[5]=e=>c(le).sourceField=e),format:"YYYY/MM/DD hh:mm:ss","value-format":"YYYY-MM-DD hh:mm:ss a",type:"datetime"},null,8,["modelValue"])),"toFixed"==c(le).updateMode&&"Reference"!=c(oe)&&"Tag"!=c(oe)&&"Option"!=c(oe)&&"Boolean"!=c(oe)&&"DateTime"!=c(oe)&&"Time"!=c(oe)?(f(),j(_,{key:3,modelValue:c(le).sourceField,"onUpdate:modelValue":l[6]||(l[6]=e=>c(le).sourceField=e),placeholder:"固定值"},null,8,["modelValue"])):C("",!0),"toFixed"==c(le).updateMode&&"Reference"==c(oe)?(f(),j(_,{key:4,modelValue:c(le).sourceField.label,"onUpdate:modelValue":l[8]||(l[8]=e=>c(le).sourceField.label=e),placeholder:"固定值"},{append:b((()=>[F(S,{onClick:l[7]||(l[7]=e=>x(K)?K.value=!0:K=!0)},{default:b((()=>[F(w,null,{default:b((()=>[F(I)])),_:1})])),_:1})])),_:1},8,["modelValue"])):C("",!0),"toFixed"==c(le).updateMode&&"Option"==c(oe)?m((f(),j(o,{key:5,modelValue:c(le).sourceField,"onUpdate:modelValue":l[9]||(l[9]=e=>c(le).sourceField=e),filterable:"",class:"w-100"},{default:b((()=>[(f(!0),v(y,null,g(c(ie),((e,l)=>(f(),j(t,{key:l,label:e.label,value:e},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])),[[G,c(ue)]]):C("",!0),"toFixed"==c(le).updateMode&&"Tag"==c(oe)?m((f(),j(o,{key:6,modelValue:c(le).sourceField,"onUpdate:modelValue":l[10]||(l[10]=e=>c(le).sourceField=e),filterable:"",class:"w-100",multiple:""},{default:b((()=>[(f(!0),v(y,null,g(c(ie),((e,l)=>(f(),j(t,{key:l,label:e.label,value:e},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])),[[G,c(ue)]]):C("",!0),"forCompile"==c(le).updateMode?(f(),j(_,{key:7,modelValue:c(le).sourceField,"onUpdate:modelValue":l[11]||(l[11]=e=>c(le).sourceField=e),placeholder:"计算公式",autosize:"",type:"textarea",onClick:je},null,8,["modelValue"])):C("",!0),"toNull"!==c(le).updateMode?(f(),v("div",L,V("forField"==le.updateMode?"源字段":"toFixed"==le.updateMode?"固定值":"forCompile"==le.updateMode?"计算公式":void 0),1)):C("",!0)])),_:1})])),_:1})),[[G,c(J)]])]})),_:1}),F(r,{label:" "},{default:b((()=>[F(S,{type:"primary",plain:"",onClick:ne},{default:b((()=>[h("+ 添加")])),_:1})])),_:1}),c(Fe)?(f(),v("div",D,[F(i,{modelValue:c(Fe),"onUpdate:modelValue":l[12]||(l[12]=e=>x(Fe)?Fe.value=e:Fe=e),fields:c(be),defaultFormulaVal:c(ye),isAdvanced:c(ge),onConfirm:Ne},null,8,["modelValue","fields","defaultFormulaVal","isAdvanced"])])):C("",!0),c(K)?(f(),j(q,{key:1,title:"请选择",modelValue:c(K),"onUpdate:modelValue":l[13]||(l[13]=e=>x(K)?K.value=e:K=e),"show-close":!0,class:"small-padding-dialog",width:"520px",draggable:"","close-on-click-modal":!1,"close-on-press-escape":!1,"append-to-body":!0},{default:b((()=>[F(a,{ref:"referST",entity:c(B).defaultTargetEntity.entityName,refField:c(le).targetField,onRecordSelected:_e},null,8,["entity","refField"])])),_:1},8,["modelValue"])):C("",!0)])),[[G,c(O)]])}}},[["__scopeId","data-v-0b5b421b"]]);export{I as default};