import{r as e,v as l,u as a,p as t,ad as o,l as i,o as r,i as s,w as d,a as u,F as n,A as p,E as m,e as c,t as v,z as f,g as b,c as g,C as h,R as V,s as y,Y as _,G as j,X as C,ag as x,f as w,n as k,bh as M,bf as I}from"./@vue-1ad4818c.js";import{_ as z,w as E,x as U,y as N,z as L,A as R}from"./index-ea085229.js";import{a as F,b as A}from"./element-plus-bab58d46.js";import"./vue3-manner-report-313033e6.js";import"./axios-85bcd05e.js";import"./crypto-js-43a3a23e.js";import"./@babel-6bd44fd1.js";import"./cron-parser-1391a961.js";import"./luxon-b01799b6.js";import"./vue-router-414ebc36.js";import"./nprogress-c04e6182.js";import"./pinia-3f60a8e1.js";import"./sortablejs-2f5483e3.js";import"./qrcodejs2-fix-bab07f30.js";import"./@element-plus-2a6d398c.js";import"./moment-bafe441d.js";import"./pinyin-pro-c81f5d39.js";import"./vue-i18n-0843b5c5.js";import"./@intlify-2528d766.js";import"./source-map-b06bd4b9.js";import"./vue-c723f8b1.js";import"./@vueuse-477b4dbb.js";import"./pinia-plugin-persistedstate-d2bd58cf.js";import"./resize-observer-polyfill-ad543aa3.js";import"./vue-smart-widget-8b3f5b0a.js";import"./echarts-b5d24e9a.js";import"./zrender-a15b073b.js";import"./tslib-a4e99503.js";import"./echarts-liquidfill-f5c863f1.js";import"./@antv-43d380be.js";import"./ant-design-vue-2db0c9af.js";import"./lodash-es-b8e8104a.js";import"./@ant-design-60f2ef8e.js";import"./@ctrl-4982d949.js";import"./dayjs-7fb79ad3.js";import"./vue-types-6bcea8eb.js";import"./dom-align-78cb5391.js";import"./vue-draggable-next-a413f884.js";import"./vue-resize-observer-90aea344.js";import"./@popperjs-b78c3215.js";import"./async-validator-cf877c1f.js";import"./memoize-one-63ab667a.js";import"./escape-html-3148dbc9.js";import"./normalize-wheel-es-3222b0a2.js";import"./@floating-ui-0ef99041.js";const S=["onClick"],T={key:0,class:"ml-select"},O={class:"ml-select-icon"},q={key:1},D=z({__name:"index",props:{modelValue:null,options:{type:Array,default:()=>[]},noFilterText:{type:String,default:"无权限"}},emits:["update:modelValue","onChange"],setup(V,{emit:y}){const _=V;let j=e(""),C=e([]);const x=e(null);let w=l({}),k=e(!1);a((()=>_.modelValue),(()=>{j.value=_.modelValue}),{deep:!0}),t((()=>{j.value=_.modelValue,C.value=_.options,w=o(),w.default&&(k.value=!0)}));const M=()=>{let e=C.value.filter((e=>e.value==j.value));return e.length>0?e[0].label:_.noFilterText};return(e,l)=>{const a=i("ElIconArrowDown"),t=i("el-icon"),o=i("el-tooltip");return r(),s(o,{ref_key:"tooltipRef",ref:x,placement:"bottom",effect:"light",trigger:"click","popper-class":"ml-select-popper"},{content:d((()=>[(r(!0),u(n,null,p(m(C),((e,l)=>(r(),u("div",{class:c(["op-item",{active:!m(k)&&m(j)==e.value}]),key:l,onClick:l=>(e=>{j.value=e.value,w.default?y("onChange",j.value):y("update:modelValue",j.value),x.value.hide()})(e)},v(e.label),11,S)))),128))])),default:d((()=>[m(k)?(r(),u("div",q,[h(e.$slots,"default",{},void 0,!0)])):(r(),u("div",T,[f(v(M())+" ",1),b("span",O,[g(t,null,{default:d((()=>[g(a)])),_:1})])]))])),_:3},512)}}},[["__scopeId","data-v-1324c01b"]]),B=e=>(M("data-v-0ac96914"),e=e(),I(),e),K=B((()=>b("span",{class:"ml-5"},"新建角色",-1))),G={class:"mr-3"},$=B((()=>b("span",null,"编辑",-1))),H={class:"mr-3"},J=B((()=>b("span",null,"删除",-1))),P={class:"entity-right-setting title"},X={class:"fl label"},Y=B((()=>b("div",{class:"fl text-align-center bold"},"查看权限",-1))),Z=B((()=>b("div",{class:"fl text-align-center bold"},"新建权限",-1))),Q=B((()=>b("div",{class:"fl text-align-center bold"},"修改权限",-1))),W=B((()=>b("div",{class:"fl text-align-center bold"},"删除权限",-1))),ee=B((()=>b("div",{class:"fl text-align-center bold"},"分配权限",-1))),le=B((()=>b("div",{class:"fl text-align-center bold"},"共享权限",-1))),ae={class:"fl"},te={class:"text-icon-all"},oe={class:"entity-right-setting-body"},ie={class:"fl label"},re={class:"fl text-align-center"},se={class:"fl text-align-center"},de={class:"fl text-align-center"},ue={class:"fl text-align-center"},ne={class:"fl text-align-center"},pe={class:"fl text-align-center"},me={class:"fl text-icon"},ce={class:"text-icon-span"},ve=B((()=>b("hr",{style:{border:"0","border-top":"1px dotted #cccccc"}},null,-1))),fe={class:"dialog-footer"},be=z({__name:"role-list-view",setup(l){const a=V("$TOOL");let t=e(),o=e({entity:"Role",fieldsList:"roleName, disabled, description,createdBy",fieldName:"roleName",sortFields:[{fieldName:"createdBy",type:"DESC"}],filterItems:[],tableColumn:[{prop:"roleName",label:"用户名称",width:"180"},{prop:"disabled",label:"是否禁用",align:"center",width:"120",formatter:e=>e.disabled?"是":"否"},{prop:"description",label:"角色描述"}]});const c=()=>{I.value=[],h.value=!0,M.value=!0,E().then((e=>{if(e&&200==e.data.code){let l=e.data.data;q.value.roleId="",q.value.roleName=l.roleName,q.value.disabled=l.disabled,q.value.description=l.description,q.value.rightValueMap=l.rightValueMap,I.value=U(l.rightEntityList),T()}M.value=!1})).catch((e=>{F.error(e.message),M.value=!1}))};let h=e(!1),M=e(!1),I=y([]),z=e(""),S=y([]);const T=()=>{M.value=!0,z.value?S.value=I.value.filter((e=>-1!=e.label.indexOf(z.value))):S.value=[...I.value],setTimeout((()=>{M.value=!1}),300)};let O=e(),q=e({roleId:"",roleName:"",disabled:!1,description:"",rightValueMap:{}});let B=y({roleName:[{validator:(e,l,a)=>{if(!l)return void a(new Error("请输入角色名称"));if(l.length<2)return void a(new Error("请输入至少两个字符"));if(l.length>30)return void a(new Error("名字最多只能30个字符"));/^[A-Za-z\d\u4e00-\u9fa5]+[_-]*/.test(l)?a():a(new Error("请以中文、英文字母、数字开头，中间可输入下划线或横杠"))},trigger:"blur"}]}),be=y([{label:"开发应用",value:"r6017"},{label:"实体管理",value:"r6001"},{label:"删除实体",value:"r6002"},{label:"设计表单布局",value:"r6003"},{label:"单选项管理",value:"r6005"},{label:"多选项管理",value:"r6006"},{label:"配置导航",value:"r6007"},{label:"实体布局配置",value:"r6008"},{label:"回收站管理",value:"r6009"},{label:"修改历史查询",value:"r6010"},{label:"数据导入",value:"r6011"},{label:"审批撤销",value:"r6013"},{label:"登录日志查看",value:"r6014"},{label:"触发器执行日志",value:"r6015"},{label:"流程配置管理",value:"r6016"}]),ge=y([{label:"无权限",value:0},{label:"本人",value:10},{label:"本部门",value:20},{label:"本部门及以下",value:30},{label:"上级部门及以下",value:40},{label:"全部数据",value:50}]),he=y([{label:"无权限",value:0},{label:"全部数据",value:50}]),Ve=y([{label:"不允许",value:0},{label:"允许",value:50}]);const ye=e=>!0===e.authorizable?ge.value:he.value,_e=e=>{I.value.forEach((l=>{q.value.rightValueMap["r"+l.entityCode+"-1"]=e,q.value.rightValueMap["r"+l.entityCode+"-2"]=e,q.value.rightValueMap["r"+l.entityCode+"-3"]=e,q.value.rightValueMap["r"+l.entityCode+"-4"]=e,q.value.rightValueMap["r"+l.entityCode+"-5"]=e,q.value.rightValueMap["r"+l.entityCode+"-6"]=e})),T()},je=()=>{O.value.validate((e=>{if(!e)return k((()=>{Ce()})),!1;M.value=!0,R(q.value).then((e=>{var l;e&&200==(null==(l=e.data)?void 0:l.code)&&(F.success("保存成功"),h.value=!1,t.value.getTableList()),M.value=!1})).catch((e=>{F({message:e.message,type:"error"}),M.value=!1}))}))},Ce=()=>{document.querySelectorAll(".el-form-item__error")[0].scrollIntoView({behavior:"smooth",block:"center"})};return(e,l)=>{const V=i("ElIconPlus"),y=i("el-icon"),k=i("el-button"),E=i("ElIconEdit"),R=i("ElIconDelete"),ge=i("el-table-column"),Ce=i("mlSingleList"),xe=i("el-input"),we=i("el-form-item"),ke=i("el-col"),Me=i("el-radio"),Ie=i("el-radio-group"),ze=i("el-row"),Ee=i("ElIconSearch"),Ue=i("ElIconFinished"),Ne=i("el-dropdown-item"),Le=i("el-dropdown-menu"),Re=i("el-dropdown"),Fe=i("el-scrollbar"),Ae=i("el-tab-pane"),Se=i("el-tabs"),Te=i("el-form"),Oe=i("el-dialog"),qe=i("el-container"),De=_("loading");return r(),s(qe,{direction:"horizontal"},{default:d((()=>[g(Ce,{ref_key:"mlSingleListRef",ref:t,title:"权限角色",mainEntity:m(o).entity,fieldsList:m(o).fieldsList,sortFields:m(o).sortFields,fieldName:m(o).roleName,tableColumn:m(o).tableColumn,filterItems:m(o).filterItems},{addbutton:d((()=>[g(k,{type:"primary",onClick:c,disabled:!m(a).checkRole("r23-2")},{default:d((()=>[g(y,{size:"14"},{default:d((()=>[g(V)])),_:1}),K])),_:1},8,["disabled"])])),activeRow:d((()=>[g(ge,{label:"操作",align:"center",width:"140",fixed:"right"},{default:d((e=>[g(k,{size:"small",type:"primary",link:"",disabled:!m(a).checkRole("r23-3"),onClick:l=>{var a;"023-000000000000000000000000000000000001"!==(a=e.row).roleId?(I.value=[],h.value=!0,M.value=!0,N(a.roleId).then((e=>{if(e&&200==e.data.code){const l=e.data.data;q.value.roleId=l.roleId,q.value.roleName=l.roleName,q.value.disabled=l.disabled,q.value.description=l.description,q.value.rightValueMap=l.rightValueMap,I.value=U(l.rightEntityList),T()}M.value=!1})).catch((e=>{F.error(e.message),M.value=!1}))):F.info("管理员角色禁止修改！")}},{default:d((()=>[b("span",G,[g(y,null,{default:d((()=>[g(E)])),_:1})]),$])),_:2},1032,["disabled","onClick"]),g(k,{size:"small",link:"",type:"primary",disabled:!m(a).checkRole("r23-4"),onClick:l=>{var a;"023-000000000000000000000000000000000001"!==(a=e.row).roleId?A.confirm("是否删除该权限角色?","删除确认").then((()=>{L(a.roleId).then((e=>{200==e.data.code&&(F.success("删除成功"),t.value.getTableList())})).catch((e=>{F.error(e.message)}))})).catch((()=>{F.info("取消删除")})):F.info("管理员角色禁止删除！")}},{default:d((()=>[b("span",H,[g(y,null,{default:d((()=>[g(R)])),_:1})]),J])),_:2},1032,["disabled","onClick"])])),_:1})])),_:1},8,["mainEntity","fieldsList","sortFields","fieldName","tableColumn","filterItems"]),m(h)?(r(),s(Oe,{key:0,title:m(q).roleId?"编辑权限角色":"新建权限角色",modelValue:m(h),"onUpdate:modelValue":l[5]||(l[5]=e=>j(h)?h.value=e:h=e),"destroy-on-close":!0,"close-on-click-modal":!1,class:"small-padding","show-close":!0,"close-on-press-escape":!1,width:"1160px","z-index":2001},{footer:d((()=>[b("div",fe,[g(k,{type:"primary",onClick:je,style:{width:"90px"},loading:m(M)},{default:d((()=>[f("保 存")])),_:1},8,["loading"]),g(k,{onClick:l[4]||(l[4]=e=>j(h)?h.value=!1:h=!1),loading:m(M)},{default:d((()=>[f("取 消")])),_:1},8,["loading"])])])),default:d((()=>[C((r(),s(Te,{"label-position":"left","label-width":"150px",size:"",ref_key:"roleFormRes",ref:O,model:m(q),rules:m(B),"element-loading-text":"数据加载中..."},{default:d((()=>[g(ze,{gutter:12},{default:d((()=>[g(ke,{span:12},{default:d((()=>[g(we,{label:"角色名称",prop:"roleName"},{default:d((()=>[g(xe,{modelValue:m(q).roleName,"onUpdate:modelValue":l[0]||(l[0]=e=>m(q).roleName=e),maxlength:"30"},null,8,["modelValue"])])),_:1})])),_:1}),g(ke,{span:12},{default:d((()=>[g(we,{label:"是否禁用"},{default:d((()=>[g(Ie,{modelValue:m(q).disabled,"onUpdate:modelValue":l[1]||(l[1]=e=>m(q).disabled=e)},{default:d((()=>[g(Me,{label:!0},{default:d((()=>[f("是")])),_:1}),g(Me,{label:!1},{default:d((()=>[f("否")])),_:1})])),_:1},8,["modelValue"])])),_:1})])),_:1})])),_:1}),g(ze,{gutter:12},{default:d((()=>[g(ke,{span:24},{default:d((()=>[g(we,{label:"角色说明"},{default:d((()=>[g(xe,{type:"textarea",autosize:{minRows:2,maxRows:3},modelValue:m(q).description,"onUpdate:modelValue":l[2]||(l[2]=e=>m(q).description=e)},null,8,["modelValue"])])),_:1})])),_:1})])),_:1}),g(Se,{type:"border-card"},{default:d((()=>[g(Ae,{label:"数据权限"},{default:d((()=>[b("div",P,[b("div",X,[g(xe,{size:"small",modelValue:m(z),"onUpdate:modelValue":l[3]||(l[3]=e=>j(z)?z.value=e:z=e),placeholder:"过滤实体（回车搜索）",onKeyup:x(T,["enter"])},{prefix:d((()=>[g(y,{class:"el-input__icon"},{default:d((()=>[g(Ee)])),_:1})])),_:1},8,["modelValue","onKeyup"])]),Y,Z,Q,W,ee,le,b("div",ae,[g(Re,{trigger:"click",onCommand:_e},{dropdown:d((()=>[g(Le,null,{default:d((()=>[(r(!0),u(n,null,p(m(he),((e,l)=>(r(),s(Ne,{command:e.value,key:l},{default:d((()=>[f(v(e.label),1)])),_:2},1032,["command"])))),128))])),_:1})])),default:d((()=>[b("span",te,[g(y,{size:"16"},{default:d((()=>[g(Ue)])),_:1})])])),_:1})])]),b("div",oe,[g(Fe,null,{default:d((()=>[(r(!0),u(n,null,p(m(S),((e,l)=>(r(),u("div",{class:"entity-right-setting",key:l},[b("div",ie,v(e.label),1),b("div",re,[g(D,{modelValue:m(q).rightValueMap["r"+e.entityCode+"-1"],"onUpdate:modelValue":l=>m(q).rightValueMap["r"+e.entityCode+"-1"]=l,options:ye(e)},null,8,["modelValue","onUpdate:modelValue","options"])]),b("div",se,[g(D,{modelValue:m(q).rightValueMap["r"+e.entityCode+"-2"],"onUpdate:modelValue":l=>m(q).rightValueMap["r"+e.entityCode+"-2"]=l,options:m(Ve)},null,8,["modelValue","onUpdate:modelValue","options"])]),b("div",de,[g(D,{modelValue:m(q).rightValueMap["r"+e.entityCode+"-3"],"onUpdate:modelValue":l=>m(q).rightValueMap["r"+e.entityCode+"-3"]=l,options:ye(e)},null,8,["modelValue","onUpdate:modelValue","options"])]),b("div",ue,[g(D,{modelValue:m(q).rightValueMap["r"+e.entityCode+"-4"],"onUpdate:modelValue":l=>m(q).rightValueMap["r"+e.entityCode+"-4"]=l,options:ye(e)},null,8,["modelValue","onUpdate:modelValue","options"])]),b("div",ne,[g(D,{modelValue:m(q).rightValueMap["r"+e.entityCode+"-5"],"onUpdate:modelValue":l=>m(q).rightValueMap["r"+e.entityCode+"-5"]=l,options:ye(e)},null,8,["modelValue","onUpdate:modelValue","options"])]),b("div",pe,[g(D,{modelValue:m(q).rightValueMap["r"+e.entityCode+"-6"],"onUpdate:modelValue":l=>m(q).rightValueMap["r"+e.entityCode+"-6"]=l,options:ye(e)},null,8,["modelValue","onUpdate:modelValue","options"])]),b("div",me,[g(D,{options:ye(e),onOnChange:l=>((e,l)=>{let{entityCode:a,authorizable:t}=l;for(let o=1;o<7;o++)q.value.rightValueMap["r"+a+"-"+o]=2!=o&&t?e:e>0?50:0})(l,e)},{default:d((()=>[b("span",ce,[g(y,{size:"16"},{default:d((()=>[g(Ue)])),_:1})])])),_:2},1032,["options","onOnChange"])])])))),128))])),_:1})])])),_:1}),g(Ae,{label:"功能权限"},{default:d((()=>[g(ze,null,{default:d((()=>[ve])),_:1}),g(ze,{gutter:12,class:"function-right-row"},{default:d((()=>[(r(!0),u(n,null,p(m(be),((e,l)=>(r(),s(ke,{span:12,key:l},{default:d((()=>[g(we,{title:e.value},{label:d((()=>[f(v(e.label),1)])),default:d((()=>[g(Ie,{modelValue:m(q).rightValueMap[e.value],"onUpdate:modelValue":l=>m(q).rightValueMap[e.value]=l},{default:d((()=>[g(Me,{label:!0},{default:d((()=>[f("是")])),_:1}),g(Me,{label:!1},{default:d((()=>[f("否")])),_:1})])),_:2},1032,["modelValue","onUpdate:modelValue"])])),_:2},1032,["title"])])),_:2},1024)))),128))])),_:1})])),_:1})])),_:1})])),_:1},8,["model","rules"])),[[De,m(M)]])])),_:1},8,["title","modelValue"])):w("",!0)])),_:1})}}},[["__scopeId","data-v-0ac96914"]]);export{be as default};