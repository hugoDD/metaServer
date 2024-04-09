import{r as e,u as l,p as a,l as t,Y as o,X as i,E as s,o as n,i as u,w as d,z as r,f as p,c as m,G as c,ah as f,g as b,a as v,A as y,t as g,a0 as h,F as j,ag as _,n as k,bh as V,bf as x}from"./@vue-1ad4818c.js";import{_ as C,a0 as E,a1 as w}from"./index-ea085229.js";import{a as T}from"./element-plus-bab58d46.js";import"./vue3-manner-report-313033e6.js";import"./axios-85bcd05e.js";import"./crypto-js-43a3a23e.js";import"./@babel-6bd44fd1.js";import"./cron-parser-1391a961.js";import"./luxon-b01799b6.js";import"./vue-router-414ebc36.js";import"./nprogress-c04e6182.js";import"./pinia-3f60a8e1.js";import"./sortablejs-2f5483e3.js";import"./qrcodejs2-fix-bab07f30.js";import"./@element-plus-2a6d398c.js";import"./moment-bafe441d.js";import"./pinyin-pro-c81f5d39.js";import"./vue-i18n-0843b5c5.js";import"./@intlify-2528d766.js";import"./source-map-b06bd4b9.js";import"./vue-c723f8b1.js";import"./@vueuse-477b4dbb.js";import"./pinia-plugin-persistedstate-d2bd58cf.js";import"./resize-observer-polyfill-ad543aa3.js";import"./vue-smart-widget-8b3f5b0a.js";import"./echarts-b5d24e9a.js";import"./zrender-a15b073b.js";import"./tslib-a4e99503.js";import"./echarts-liquidfill-f5c863f1.js";import"./@antv-43d380be.js";import"./ant-design-vue-2db0c9af.js";import"./lodash-es-b8e8104a.js";import"./@ant-design-60f2ef8e.js";import"./@ctrl-4982d949.js";import"./dayjs-7fb79ad3.js";import"./vue-types-6bcea8eb.js";import"./dom-align-78cb5391.js";import"./vue-draggable-next-a413f884.js";import"./vue-resize-observer-90aea344.js";import"./@popperjs-b78c3215.js";import"./async-validator-cf877c1f.js";import"./memoize-one-63ab667a.js";import"./escape-html-3148dbc9.js";import"./normalize-wheel-es-3222b0a2.js";import"./@floating-ui-0ef99041.js";const z={class:"w-100 clearfix"},F=["onClick"],U=["onClick"],A=(e=>(V("data-v-2c9c545b"),e=e(),x(),e))((()=>b("div",{class:"w-100 info-text"},"注：点击标签后颜色加深为选中状态，再次点击取消选中，可点击叉号删除标签。",-1))),I=C({__name:"entity-property-editor",props:{entityProps:{type:Object,default:()=>{}},showTitle:{type:Boolean,default:!1},filterEntityMethod:{type:Function}},setup(V,{expose:x}){const C=V;let I=e({}),P=e(!1);l((()=>C.entityProps),(()=>{I.value=C.entityProps}),{deep:!0});let q=e([{label:"表单设计",value:1},{label:"列表设计",value:2},{label:"审批流程",value:4}]),M=e([0]);a((()=>{I.value=C.entityProps}));let Z=e({name:[{required:!0,message:"请输入实体名称",trigger:"blur"},{pattern:/^[A-Z]+[A-Za-z\d_]*$/,message:"英文大写字母开头，不可包含中文、空格，中间可输入字母、下划线",trigger:"blur"},{min:2,max:30,message:"文字长度应在2-30之间",trigger:"blur"}],label:[{required:!0,message:"请输入显示名称",trigger:"blur"},{pattern:/^[A-Za-z\d\u4e00-\u9fa5\uff0c\u3001\uff1b\uff1a\uff08\uff09\u2014\u201c\u201d]+[_-]*[A-Za-z\d\u4e00-\u9fa5\uff0c\u3001\uff1b\uff1a\uff08\uff09\u2014\u201c\u201d]/,message:"以中文、英文字母、数字开头，中间可输入下划线或横杠",trigger:"blur"},{min:2,max:30,message:"文字长度应在2-30之间",trigger:"blur"}]}),B=e(!1),D=e(""),K=e([{prop:"name",label:"实体名称",width:"150",align:"center"},{prop:"label",label:"显示名称",width:"200",align:"center"}]),G=e([]);const S=e=>{e&&(I.value.authorizable=!1)},H=()=>{I.value.detailEntityFlag&&C.filterEntityMethod&&C.filterEntityMethod(G.value,(()=>{B.value=!0}))},J=()=>{D.value="",I.value.mainEntity=""},L=e=>{e?I.value.detailEntityFlag=!1:(I.value.assignable=!1,I.value.shareable=!1)};let N=e();const O=e=>{if(e&&!I.value.name){const l=E(e);I.value.name=w(l)}},R=()=>{if(I.value.label){let e=E(I.value.label);e=e.replaceAll("-","_"),I.value.name=w(e)}};let X=e(""),Y=e(!1);const $=e(),Q=()=>{Y.value=!0,k((()=>{$.value.focus()}))},W=()=>{let e=I.value.useTag.filter((e=>e.name==X.value));X.value&&e.length<1&&I.value.useTag.push({name:X.value,checked:!0}),Y.value=!1,X.value=""},ee=e=>{I.value.useTag.splice(e,1)};return x({validateForm:e=>{N.value.validate((l=>{if(l){if(I.value.detailEntityFlag&&!D.value)return void T.info("请选择所属主实体");e()}else T.error("数据不合规范，请检查")}))},entityProps:I.value,loading:P,copyEntiytSelectedType:M}),(e,l)=>{const a=t("el-header"),k=t("el-input"),x=t("el-form-item"),C=t("el-button"),E=t("el-switch"),w=t("InfoFilled"),T=t("el-icon"),le=t("el-tooltip"),ae=t("ElIconCircleClose"),te=t("ElIconCircleCloseFilled"),oe=t("el-check-tag"),ie=t("el-checkbox"),se=t("el-checkbox-group"),ne=t("el-form"),ue=t("el-main"),de=t("SimpleTable"),re=t("el-dialog"),pe=t("el-container"),me=o("loading");return i((n(),u(pe,{class:"entity-props-container"},{default:d((()=>[V.showTitle?(n(),u(a,{key:0,class:"entity-props-header"},{default:d((()=>[r("<实体>属性设置")])),_:1})):p("",!0),m(ue,{class:"entity-props-pane"},{default:d((()=>[m(ne,{model:s(I),rules:s(Z),ref_key:"entityPropsForm",ref:N,"label-position":"left","label-width":"230px"},{default:d((()=>[m(x,{label:"显示名称",prop:"label"},{default:d((()=>[m(k,{modelValue:s(I).label,"onUpdate:modelValue":l[0]||(l[0]=e=>s(I).label=e),minlength:"2",placeholder:"以中文、英文字母、数字开头，中间可输入下划线或横杠",onChange:O},null,8,["modelValue"])])),_:1}),m(x,{label:"实体名称",prop:"name"},{default:d((()=>[m(k,{modelValue:s(I).name,"onUpdate:modelValue":l[1]||(l[1]=e=>s(I).name=e),minlength:"2",placeholder:"英文大写字母开头，不可包含中文、空格，中间可输入字母、下划线"},{append:d((()=>[m(C,{onClick:R},{default:d((()=>[r("刷新生成")])),_:1})])),_:1},8,["modelValue"])])),_:1}),2==!s(I).activeType?(n(),u(x,{key:0,label:"实体编码"},{default:d((()=>[m(k,{modelValue:s(I).entityCode,"onUpdate:modelValue":l[2]||(l[2]=e=>s(I).entityCode=e),readonly:"",disabled:"",placeholder:"系统自动生成"},null,8,["modelValue"])])),_:1})):p("",!0),m(x,{label:"是否允许设计表单"},{default:d((()=>[m(E,{style:{display:"block",float:"right"},modelValue:s(I).layoutable,"onUpdate:modelValue":l[3]||(l[3]=e=>s(I).layoutable=e),"active-text":"是","inactive-text":"否",disabled:2==s(I).activeType},null,8,["modelValue","disabled"])])),_:1}),m(x,{label:"是否允许设计列表"},{default:d((()=>[m(E,{style:{display:"block",float:"right"},modelValue:s(I).listable,"onUpdate:modelValue":l[4]||(l[4]=e=>s(I).listable=e),"active-text":"是","inactive-text":"否",disabled:2==s(I).activeType},null,8,["modelValue","disabled"])])),_:1}),m(x,{label:"是否开启记录级权限"},{default:d((()=>[m(E,{style:{display:"block",float:"right"},modelValue:s(I).authorizable,"onUpdate:modelValue":l[5]||(l[5]=e=>s(I).authorizable=e),"active-text":"是","inactive-text":"否",onChange:L,disabled:2==s(I).activeType},null,8,["modelValue","disabled"])])),_:1}),m(x,null,{label:d((()=>[r(" 是否明细实体 "),m(le,{content:"明细实体不能单独设置权限，受主实体控制",effect:"light"},{default:d((()=>[m(T,null,{default:d((()=>[m(w)])),_:1})])),_:1})])),default:d((()=>[m(E,{style:{display:"block",float:"right"},modelValue:s(I).detailEntityFlag,"onUpdate:modelValue":l[6]||(l[6]=e=>s(I).detailEntityFlag=e),"active-text":"是","inactive-text":"否",onChange:S,disabled:2==s(I).activeType},null,8,["modelValue","disabled"])])),_:1}),m(x,{label:"所属主实体"},{default:d((()=>[m(k,{readonly:"",disabled:!s(I).detailEntityFlag,modelValue:s(D),"onUpdate:modelValue":l[7]||(l[7]=e=>c(D)?D.value=e:D=e)},f({_:2},[s(I).detailEntityFlag&&s(D)?{name:"append",fn:d((()=>[m(C,{icon:"el-icon-close",title:"清除",onClick:J})])),key:"0"}:s(I).detailEntityFlag?{name:"append",fn:d((()=>[m(C,{icon:"el-icon-search",title:"选择主实体",onClick:H})])),key:"1"}:void 0]),1032,["disabled","modelValue"])])),_:1}),m(x,{label:"标签"},{default:d((()=>{var e;return[b("div",z,[(n(!0),v(j,null,y(s(I).useTag,((e,l)=>(n(),u(oe,{class:"mr-5 entity-check-tag",key:l,checked:e.checked,onChange:l=>(e=>{e.checked=!e.checked})(e)},{default:d((()=>[b("span",null,g(e.name),1),b("span",{class:"del-tag del-tag-span",onClick:h((e=>ee(l)),["stop"])},[m(T,null,{default:d((()=>[m(ae)])),_:1})],8,F),b("span",{class:"del-tag del-tag-span-hover",onClick:h((e=>ee(l)),["stop"])},[m(T,null,{default:d((()=>[m(te)])),_:1})],8,U)])),_:2},1032,["checked","onChange"])))),128)),s(Y)?(n(),u(k,{key:0,class:"mb-5",ref_key:"InputRef",ref:$,modelValue:s(X),"onUpdate:modelValue":l[8]||(l[8]=e=>c(X)?X.value=e:X=e),style:{width:"100px"},onKeyup:_(W,["enter"]),onBlur:W},null,8,["modelValue","onKeyup"])):(n(),u(C,{key:1,class:"mb-5 button-new-tag ml-1",onClick:Q,disabled:(null==(e=s(I).useTag)?void 0:e.length)>9},{default:d((()=>[r("+ 新增标签")])),_:1},8,["disabled"]))]),A]})),_:1}),2==s(I).activeType?(n(),u(x,{key:1,label:"复制模块"},{default:d((()=>[s(I).hasDetailEntity?(n(),u(se,{key:1,modelValue:s(M),"onUpdate:modelValue":l[10]||(l[10]=e=>c(M)?M.value=e:M=e)},{default:d((()=>[m(ie,{label:0,disabled:""},{default:d((()=>[r("实体字段")])),_:1}),(n(!0),v(j,null,y(s(q),((e,l)=>(n(),u(ie,{key:l,label:e.value,disabled:""},{default:d((()=>[m(le,{class:"box-item",effect:"dark",content:"当前实体包含子实体，无法复制"+e.label,placement:"bottom"},{default:d((()=>[r(g(e.label),1)])),_:2},1032,["content"])])),_:2},1032,["label"])))),128))])),_:1},8,["modelValue"])):(n(),u(se,{key:0,modelValue:s(M),"onUpdate:modelValue":l[9]||(l[9]=e=>c(M)?M.value=e:M=e)},{default:d((()=>[m(ie,{label:0,disabled:""},{default:d((()=>[r("实体字段")])),_:1}),(n(!0),v(j,null,y(s(q),((e,l)=>(n(),u(ie,{key:l,label:e.value},{default:d((()=>[r(g(e.label),1)])),_:2},1032,["label"])))),128))])),_:1},8,["modelValue"]))])),_:1})):p("",!0)])),_:1},8,["model","rules"])])),_:1}),m(re,{ref:"selectMainEntityDlg",title:"选择主实体",modelValue:s(B),"onUpdate:modelValue":l[11]||(l[11]=e=>c(B)?B.value=e:B=e),"append-to-body":!0,class:"no-padding",width:"560px"},{default:d((()=>[m(de,{"show-pagination":!1,"show-check-box":!1,"table-size":"small",columns:s(K),"show-operation-column":!0,data:s(G),"max-height":420},{table_operation:d((({scope:e})=>[m(C,{class:"",icon:"el-icon-check",onClick:l=>{var a;(a=e.row)&&(I.value.mainEntity=a.name,D.value=a.label+"("+a.name+")",B.value=!1)}},{default:d((()=>[r("选择")])),_:2},1032,["onClick"])])),_:1},8,["columns","data"])])),_:1},8,["modelValue"])])),_:1})),[[me,s(P)]])}}},[["__scopeId","data-v-2c9c545b"]]);export{I as default};
