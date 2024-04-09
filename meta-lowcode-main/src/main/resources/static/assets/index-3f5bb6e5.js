import{f as e,$ as i,j as t}from"./index-ea085229.js";import{s as r}from"./pinia-3f60a8e1.js";import o from"./ListDetail-8a5e24a7.js";import{D as s}from"./detail-5eb8c530.js";import{r as a,l,o as n,a as m,c as p,w as d,z as u,E as j,g as v,G as y,i as f,f as b,F as g,n as c}from"./@vue-1ad4818c.js";import"./element-plus-bab58d46.js";import"./lodash-es-b8e8104a.js";import"./@vueuse-477b4dbb.js";import"./@element-plus-2a6d398c.js";import"./@babel-6bd44fd1.js";import"./@popperjs-b78c3215.js";import"./@ctrl-4982d949.js";import"./dayjs-7fb79ad3.js";import"./async-validator-cf877c1f.js";import"./memoize-one-63ab667a.js";import"./escape-html-3148dbc9.js";import"./normalize-wheel-es-3222b0a2.js";import"./@floating-ui-0ef99041.js";import"./vue3-manner-report-313033e6.js";import"./axios-85bcd05e.js";import"./crypto-js-43a3a23e.js";import"./cron-parser-1391a961.js";import"./luxon-b01799b6.js";import"./vue-router-414ebc36.js";import"./nprogress-c04e6182.js";import"./sortablejs-2f5483e3.js";import"./qrcodejs2-fix-bab07f30.js";import"./moment-bafe441d.js";import"./pinyin-pro-c81f5d39.js";import"./vue-i18n-0843b5c5.js";import"./@intlify-2528d766.js";import"./source-map-b06bd4b9.js";import"./vue-c723f8b1.js";import"./pinia-plugin-persistedstate-d2bd58cf.js";import"./resize-observer-polyfill-ad543aa3.js";import"./vue-smart-widget-8b3f5b0a.js";import"./echarts-b5d24e9a.js";import"./zrender-a15b073b.js";import"./tslib-a4e99503.js";import"./echarts-liquidfill-f5c863f1.js";import"./@antv-43d380be.js";import"./ant-design-vue-2db0c9af.js";import"./@ant-design-60f2ef8e.js";import"./vue-types-6bcea8eb.js";import"./dom-align-78cb5391.js";import"./vue-draggable-next-a413f884.js";import"./vue-resize-observer-90aea344.js";import"./AddMembers-0d41fc5e.js";import"./team-3e46d8bc.js";import"./TabMemberList-d965ffd7.js";import"./edit-dfd0882d.js";import"./DetailTabs-e3a61e27.js";import"./DetailTabsSet-9cda58dd.js";import"./More-47f7037c.js";import"./SetColumn-15f4404b.js";import"./DataExport-cabbbed3.js";import"./Allocation-359987ff.js";import"./ReportForms-7661dfe6.js";import"./DefaultFilterDialog-f7a53bd1.js";import"./FormatRow-d4305ec1.js";import"./NewRelated-d601bf33.js";import"./ApprovalRelated-e5fb019d.js";import"./CardLayout-74c0bddb.js";const h={__name:"index",setup(h){const{allEntityLabel:w,allEntityName:C}=r(e());let D=a(""),I=a([{fieldName:"revisionOn",type:"DESC"}]),N=a([{prop:"entityCode",label:"所属实体",align:"center",formatter:e=>w.value[e.entityCode]},{prop:"entityId.name",label:"关联记录",align:"center",highlight:!0,formatter:e=>{var i;return null==(i=e.entityId)?void 0:i.name}},{prop:"revisionType.label",label:"变更类型",align:"center",formatter:e=>{var i;return null==(i=e.revisionType)?void 0:i.label}},{prop:"revisionBy.name",label:"变更用户",align:"center",formatter:e=>{var i;return null==(i=e.revisionBy)?void 0:i.name}},{prop:"revisionOn",label:"变更时间",align:"center",formatter:e=>i(e.revisionOn)}]),T=a(!1),x=a([]);const F=()=>{D.value.getTableList()};let R=a(!1),_=a(""),k=a(""),z=a(""),L=a("");let H=a("");const E=e=>{k.value=C.value[e.entityCode];var i;["User","Team"].includes(k.value)?(z.value="User"==k.value?"userId":"teamId",L.value="User"==k.value?"userName":"teamName",i=e,R.value=!0,c((()=>{_.value.openDetailDialog(i.entityId.id,i.entityId.name)}))):(e=>{H.value.openDialog(e.entityId.id)})(e)};return(e,i)=>{const r=l("el-button"),a=l("el-table-column"),c=l("mlSingleList"),h=l("el-table"),w=l("mlDialog");return n(),m(g,null,[p(c,{title:"变更历史",mainEntity:"RevisionHistory",fieldsList:"entityId,entityCode,revisionContent,revisionBy,revisionOn,revisionType,revisionHistoryId",sortFields:j(I),fieldName:"entityCode",tableColumn:j(N),ref_key:"mlSingleListRef",ref:D,queryUrl:"/revisionHistory/listQuery",onHighlightClick:E},{activeRow:d((()=>[p(a,{label:"操作",fixed:"right",align:"center",width:"100"},{default:d((e=>[p(r,{disabled:e.row.revisionType&&1!=e.row.revisionType.value&&5!=e.row.revisionType.value,link:"",type:"primary",size:"small",onClick:i=>async function(e){D.value.loading=!0;let i=await t.get("/revisionHistory/detailsById?revisionHistoryId="+e.revisionHistoryId);i&&i.data&&i.data.length>0&&(x.value=i.data,T.value=!0),D.value.loading=!1}(e.row)},{default:d((()=>[u("查看")])),_:2},1032,["disabled","onClick"])])),_:1})])),_:1},8,["sortFields","tableColumn"]),p(w,{modelValue:j(T),"onUpdate:modelValue":i[0]||(i[0]=e=>y(T)?T.value=e:T=e),title:"变更详情",width:"35%"},{default:d((()=>[v("div",null,[p(h,{data:j(x),style:{width:"100%"},stripe:"",size:"large"},{default:d((()=>[p(a,{prop:"label",label:"字段"}),p(a,{prop:"before",label:"变更前"}),p(a,{prop:"after",label:"变更后"})])),_:1},8,["data"])])])),_:1},8,["modelValue"]),j(R)?(n(),f(o,{key:0,ref_key:"listDetailRefs",ref:_,idFieldName:j(z),nameFieldName:j(L),onOnRefresh:F},null,8,["idFieldName","nameFieldName"])):b("",!0),p(s,{ref_key:"detailRefs",ref:H,onOnConfirm:F},null,512)],64)}}};export{h as default};
