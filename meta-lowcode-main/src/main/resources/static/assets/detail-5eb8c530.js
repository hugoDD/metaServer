import e from"./DetailTabs-e3a61e27.js";import{_ as a,g as l,h as t,f as i,q as o}from"./index-ea085229.js";import d from"./More-47f7037c.js";import{u as n}from"./vue-router-414ebc36.js";import u from"./FormatRow-d4305ec1.js";import s from"./edit-dfd0882d.js";import r from"./NewRelated-d601bf33.js";import{A as m}from"./ApprovalRelated-e5fb019d.js";import{a as v}from"./element-plus-bab58d46.js";import{r as c,R as p,u as f,p as y,v as b,l as g,Y as N,o as h,a as _,X as S,E as C,i as w,g as k,c as D,w as F,G as T,f as L,z as A,t as O,e as I,F as E,A as j,bh as R,bf as z,n as x}from"./@vue-1ad4818c.js";import V from"./CardLayout-74c0bddb.js";import{d as J}from"./pinia-3f60a8e1.js";const P=J("routerParams",(()=>{let e=c({});return{routerParams:e,setRouterParams:a=>{e.value=a}}})),q={key:1,class:"main"},B={class:"main-header"},W={class:"fr fr-box"},M={class:"el-dropdown-link"},U={key:0,class:"min-table mt-20"},$={key:1,class:"min-table mt-20"},Q={class:"collapse-title"},G={class:"title-span"},H=(e=>(R("data-v-94466dc2"),e=e(),z(),e))((()=>k("i",{class:"header-icon el-icon-info"},null,-1))),X=a({__name:"DetailTabCom",props:{cutTab:{type:String},entityId:{type:String,default:""},tabs:{type:Object,default:()=>{}}},setup(e,{expose:a}){const i=e,{setRouterParams:o}=P(),d=n(),s=p("$API");f((()=>i.cutTab),(()=>{ve()}),{deep:!0});let r=c(""),m=c("");y((()=>{ve()}));let v=c(!1),R=b({}),z=c([]),x=c([]),J=c([]),X=c([]),Y=b({}),K=b({}),Z=c("table"),ee=c(""),ae=c(""),le=c([]),te=c([]),ie=c([{fieldName:"modifiedOn",type:"DESC"}]),oe=c("");b({});let de=c(""),ue=c(""),se=c(""),re=c("默认排序");const me=e=>{"modifiedOnDesc"==e&&(te.value=[{fieldName:"modifiedOn",type:"DESC"}],re.value="最近修改时间"),"createdOnDesc"==e&&(te.value=[{fieldName:"createdOn",type:"DESC"}],re.value="最近创建时间"),"createdOnAsc"==e&&(te.value=[{fieldName:"createdOn",type:"ASC"}],re.value="最早创建时间"),Se()},ve=async()=>{var e;z.value=(null==(e=i.tabs)?void 0:e.config)?JSON.parse(i.tabs.config):[],Z.value="table";let a=z.value.filter((e=>e.entityName==i.cutTab));a[0]&&(r.value=a[0].entityCode,m.value=a[0].entityName,se.value=a[0].fieldName),v.value=!0;let l=await s.layoutConfig.getLayoutList(i.cutTab);if(l){de.value=l.data.idFieldName,ue.value=l.data.nameFieldName,R=l.data?{...l.data}:{};let{chosenListType:e,LIST:a}=R,{ALL:t,SELF:i}=a;Y=l.data.titleWidthForAll?{...JSON.parse(l.data.titleWidthForAll)}:{},K=l.data.titleWidthForSelf?{...JSON.parse(l.data.titleWidthForSelf)}:{},!l.data.TAB||l.data.TAB,i.FILTER=i.config?JSON.parse(i.config):[],t.FILTER=t.config?JSON.parse(t.config):[],l.data.chosenListType?(x.value=a[l.data.chosenListType].FILTER,ae.value=l.data.chosenListType):(x.value=t.FILTER,ae.value="ALL"),x.value.length>0&&he()}v.value=!1},ce=()=>{d.push({path:"/web/"+i.cutTab+"/list"}),o({path:"/web/"+i.cutTab+"/list",filter:{equation:"AND",items:[{fieldName:se.value,op:"EQ",value:i.entityId}]},quickFilter:ee.value})};let pe=b({no:1,size:20,total:0});const fe=e=>{pe.no=e,Se()},ye=e=>{pe.size=e,Se()},be=e=>{de.value&&oe.value.openDialog(e[de.value])},ge=e=>{let a={};"ascending"==e.order?(a.type="ASC",a.fieldName=e.prop):"descending"==e.order?(a.type="DESC",a.fieldName=e.prop):a={...ie[0]},te.value=[a],Se()},Ne=e=>"SELF"==ae.value&&K[e.fieldName]?K[e.fieldName]:"ALL"==ae.value&&Y[e.fieldName]?Y[e.fieldName]:e.columnWidth&&e.columnWidth>0?e.columnWidth:"150",he=()=>{le.value=x.value.map((e=>e.fieldName));let e=x.value.filter((e=>e.columnSort));e.length>0?(te.value=[{fieldName:e[0].fieldName,type:e[0].columnSort}],ie.value=[...te.value]):(ie.value=[{fieldName:"modifiedOn",type:"DESC"}],te.value=[...ie.value]),Se()};let _e=c(null);const Se=async()=>{var e;v.value=!0;let a={mainEntity:m.value,fieldsList:le.value.join(),pageSize:pe.size,pageNo:pe.no,filter:{equation:"AND",items:[{fieldName:se.value,op:"EQ",value:i.entityId}]},sortFields:te.value,quickFilter:ee.value},o=await l(a.mainEntity,a.fieldsList,a.filter,a.pageSize,a.pageNo,a.sortFields,a.advFilter,a.quickFilter);if(o&&o.data){J.value=o.data.dataList,X.value=J.value.map((e=>e[de.value])),pe.total=o.data.pagination.total,v.value=!0;let a=await t(m.value);a&&(_e.value=(null==(e=a.data)?void 0:e.layoutJson)||null),v.value=!1}else v.value=!1};return a({initData:ve}),(e,a)=>{const l=g("el-empty"),t=g("ElIconSearch"),i=g("el-icon"),o=g("el-input"),d=g("SvgIcon"),n=g("el-button"),s=g("arrow-down"),r=g("el-dropdown-item"),m=g("el-dropdown-menu"),c=g("el-dropdown"),p=g("el-table-column"),f=g("el-table"),y=g("mlPagination"),b=g("el-collapse-item"),R=g("el-collapse"),z=N("loading");return h(),_(E,null,[S((h(),_("div",null,[0==C(x).length?(h(),w(l,{key:0,"image-size":100,description:"未查询到该实体相关配置列数据"})):(h(),_("div",q,[k("div",B,[D(o,{modelValue:C(ee),"onUpdate:modelValue":a[0]||(a[0]=e=>T(ee)?ee.value=e:ee=e),placeholder:"快速查询",style:{width:"300px"}},{append:F((()=>[k("span",{class:"main-search-icon",onClick:Se},[D(i,null,{default:F((()=>[D(t)])),_:1})])])),_:1},8,["modelValue"]),"table"==C(Z)?(h(),w(n,{key:0,text:"",class:"ml-3",title:"列表页查看",onClick:ce},{default:F((()=>[D(d,{"icon-name":"open"})])),_:1})):L("",!0),k("div",W,[D(c,{trigger:"click",onCommand:me,disabled:"table"==C(Z)},{dropdown:F((()=>[D(m,null,{default:F((()=>[D(r,{command:"modifiedOnDesc"},{default:F((()=>[A("最近修改时间")])),_:1}),D(r,{command:"createdOnDesc"},{default:F((()=>[A("最近创建时间")])),_:1}),D(r,{command:"createdOnAsc"},{default:F((()=>[A("最早创建时间")])),_:1})])),_:1})])),default:F((()=>[k("span",M,[A(O(C(re))+" ",1),D(i,{class:"el-icon--right"},{default:F((()=>[D(s)])),_:1})])])),_:1},8,["disabled"]),D(n,{text:"",title:"卡片视图",style:{"margin-left":"0",padding:"8px"},class:I({"is-active":"card"==C(Z)}),onClick:a[1]||(a[1]=e=>T(Z)?Z.value="card":Z="card")},{default:F((()=>[D(d,{"icon-name":"separator-horizontal"})])),_:1},8,["class"]),D(n,{text:"",title:"列表视图",style:{"margin-left":"0",padding:"8px"},class:I({"is-active":"table"==C(Z)}),onClick:a[2]||(a[2]=e=>T(Z)?Z.value="table":Z="table")},{default:F((()=>[D(d,{"icon-name":"grid_n"})])),_:1},8,["class"])])]),"table"==C(Z)?(h(),_("div",U,[D(f,{ref:"elTables",data:C(J),border:!0,stripe:"",style:{width:"100%"},"max-height":"400px",onSortChange:ge,onRowDblclick:be},{default:F((()=>[(h(!0),_(E,null,j(C(x),((e,a)=>(h(),w(p,{key:a,prop:e.fieldName,label:e.columnAliasName?e.columnAliasName:e.fieldLabel,width:Ne(e),sortable:"","show-overflow-tooltip":""},{default:F((a=>[D(u,{row:a.row,nameFieldName:C(ue),column:e,onOpenDetailDialog:be},null,8,["row","nameFieldName","column"])])),_:2},1032,["prop","label","width"])))),128))])),_:1},8,["data"]),D(y,{no:C(pe).no,size:C(pe).size,total:C(pe).total,onPageChange:fe,onHandleSizeChange:ye,style:{background:"#fff"},bottom:!1},null,8,["no","size","total"])])):(h(),_("div",$,[C(J).length>0?(h(),w(R,{key:0,modelValue:C(X),"onUpdate:modelValue":a[3]||(a[3]=e=>T(X)?X.value=e:X=e)},{default:F((()=>[(h(!0),_(E,null,j(C(J),((e,a)=>(h(),w(b,{key:a,name:e[C(de)]},{title:F((()=>[k("div",Q,[k("span",G,O(e[C(ue)]),1),H])])),default:F((()=>[D(V,{layoutJson:C(_e),data:e},null,8,["layoutJson","data"])])),_:2},1032,["name"])))),128))])),_:1},8,["modelValue"])):(h(),w(l,{key:1,"image-size":100,description:"暂无数据"}))]))]))])),[[z,C(v)]]),D(ne,{ref_key:"detailRefs",ref:oe,onOnConfirm:Se},null,512)],64)}}},[["__scopeId","data-v-94466dc2"]]),Y=Object.freeze(Object.defineProperty({__proto__:null,default:X},Symbol.toStringTag,{value:"Module"})),K={class:"detail-header"},Z={class:"detail-header-title"},ee={class:"fr fr-box"},ae={class:"detail-main"},le={key:0},te={key:1},ie={class:"detail-right",style:{"padding-top":"40px"}},oe=(e=>(R("data-v-ea81af3b"),e=e(),z(),e))((()=>k("div",{class:"group-button-label"},"基本操作",-1))),de={class:"mr-5"},ne=a({__name:"detail",emits:["onConfirm","onAdd"],setup(a,{expose:l,emit:n}){const{queryEntityNameById:u,queryEntityCodeById:f}=i(),y=p("$API");let I=c(),E=b({isShow:!1,tab:{}}),j=c(!1),R=c([]),z=c(null),V=c(""),J=c(""),P=c(""),q=c(""),B=c(""),W=c(""),M=c("detail");const U=e=>{M.value=e,"detail"==e&&$()},$=()=>{M.value="detail",ne()};let Q=c({}),G=c();const H=async()=>{j.value=!0;let e=await y.layoutConfig.getLayoutList(J.value);e&&(Q.value=e.data.ADD?{...e.data.ADD}:{},"detail"==M.value?me():G.value.initData()),j.value=!1},Y=()=>{ye()},ne=async()=>{j.value=!0;let e=await y.layoutConfig.getLayoutList(J.value);if(e){E.tab=e.data.TAB?{...e.data.TAB}:{},Q.value=e.data.ADD?{...e.data.ADD}:{},q.value=e.data.idFieldName,W.value=e.data.nameFieldName;let a={};a[q.value]=P.value,R.value=[a],me()}else j.value=!1};let ue=c(!1),se=c(!1),re=c({});const me=async()=>{var e;j.value=!0;let a=await t(J.value);ue.value=!1,se.value=!1,a&&(null==(e=a.data)?void 0:e.layoutJson)?(ue.value=!0,re.value=a.data.optionData||{},x((async()=>{let e=await o(P.value);if(e&&e.data){B.value=e.data[W.value],I.value.setFormJson(a.data.layoutJson);let l=e.data||{};I.value.resetForm(),I.value.setFormData(l),x((()=>{"{}"==JSON.stringify(re.value)&&I.value.reloadOptionData(),z.value=(null==e?void 0:e.data.recordApprovalState)||null,z.value&&(z.value.entityCode=V.value,z.value.entityName=J.value,z.value.recordId=P.value,z.value.approvalName=E.detailTitle),I.value.setReadMode()})),se.value=!1}else se.value=!0;j.value=!1}))):j.value=!1};let ve=c();const ce=()=>{ve.value.openDialog({detailId:P.value})},pe=e=>{let a={};a.entityName=e.entityName,a.fieldName=e.fieldName,a.fieldNameVale=P.value,a.fieldNameLabel=B.value,ve.value.openDialog(a)},fe=e=>{e&&e.isDel?(E.isShow=!1,n("onConfirm")):ye()},ye=()=>{"detail"==M.value?me():G.value.initData(),n("onConfirm")},be=()=>{let e="";if(!z.value||3!=z.value.approvalStatus)return z.value&&1==z.value.approvalStatus&&(e="记录正在审批中，禁止编辑"),e;e="记录已完成审批，禁止编辑"};return l({openDialog:e=>{P.value=e,V.value=f(e),J.value=u(e),J.value?(E.entityCode=V.value,E.entityName=J.value,E.isShow=!0,$()):v.warning("当前实体未找到")}}),(a,l)=>{const t=g("ElIconRefresh"),i=g("el-icon"),o=g("ElIconCloseBold"),n=g("v-form-render"),u=g("el-empty"),v=g("el-col"),c=g("ElIconEditPen"),p=g("el-button"),f=g("el-row"),y=g("el-drawer"),b=N("loading");return h(),w(y,{size:"62.4%",class:"ml-drawer",modelValue:C(E).isShow,"onUpdate:modelValue":l[2]||(l[2]=e=>C(E).isShow=e),direction:"rtl","show-close":!1},{header:F((()=>[k("div",K,[k("div",Z,[A(O(C(B))+" ",1),k("div",ee,[k("span",{class:"fr-icon mr-10",onClick:$},[D(i,null,{default:F((()=>[D(t)])),_:1})]),k("span",{class:"fr-icon",onClick:l[0]||(l[0]=e=>C(E).isShow=!1)},[D(i,null,{default:F((()=>[D(o)])),_:1})])])])])])),default:F((()=>[S((h(),_("div",ae,[C(se)?(h(),w(u,{key:1,description:"暂无数据"})):(h(),w(f,{key:0,gutter:20},{default:F((()=>[D(v,{span:21},{default:F((()=>[D(e,{modelValue:C(E),"onUpdate:modelValue":l[1]||(l[1]=e=>T(E)?E.value=e:E=e),onTabChange:U,cutTab:C(M),onConfirm:$},null,8,["modelValue","cutTab"]),"detail"==C(M)?(h(),_("div",le,[C(ue)?(h(),w(n,{key:0,"option-data":C(re),ref_key:"vFormRef",ref:I},null,8,["option-data"])):(h(),w(u,{key:1,"image-size":100,description:"未查询到相关配置数据"}))])):(h(),_("div",te,[D(X,{ref_key:"detailTabComRefs",ref:G,cutTab:C(M),tabs:C(E).tab,entityId:C(P)},null,8,["cutTab","tabs","entityId"])]))])),_:1}),D(v,{span:3},{default:F((()=>[k("div",ie,[oe,D(f,{class:"group-el-button",style:{"margin-bottom":"30px"}},{default:F((()=>[D(v,{span:24},{default:F((()=>[a.$TOOL.checkRole("r6008")?(h(),w(r,{key:0,entityName:C(J),entityCode:C(V),addConf:C(Q),onConfirm:H,onAdd:pe},null,8,["entityName","entityCode","addConf"])):L("",!0)])),_:1}),D(v,{span:24},{default:F((()=>[D(p,{type:"primary",plain:"",onClick:ce,disabled:C(z)&&(3==C(z).approvalStatus||1==C(z).approvalStatus),title:be()},{default:F((()=>[k("span",de,[D(i,null,{default:F((()=>[D(c)])),_:1})]),A(" 编辑 ")])),_:1},8,["disabled","title"])])),_:1}),D(v,{span:24},{default:F((()=>[D(d,{type:"detail",multipleSelection:C(R),entityCode:C(V),detailId:C(P),idFieldName:C(q),nameFieldName:C(W),onEditColumnConfirm:fe},null,8,["multipleSelection","entityCode","detailId","idFieldName","nameFieldName"])])),_:1})])),_:1}),D(f,{class:"group-el-button"},{default:F((()=>[D(v,{span:24},{default:F((()=>[C(z)?(h(),w(m,{key:0,approvalStatus:C(z),onOnSubmit:Y},null,8,["approvalStatus"])):L("",!0)])),_:1})])),_:1})])])),_:1})])),_:1}))])),[[b,C(j)]]),D(s,{ref_key:"editRefs",ref:ve,onOnConfirm:ye,nameFieldName:C(W)},null,8,["nameFieldName"])])),_:1},8,["modelValue"])}}},[["__scopeId","data-v-ea81af3b"]]),ue=Object.freeze(Object.defineProperty({__proto__:null,default:ne},Symbol.toStringTag,{value:"Module"}));export{ne as D,Y as a,ue as d,P as r};
