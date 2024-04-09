import{_ as e,n as t}from"./index-ea085229.js";import{C as a}from"./element-plus-bab58d46.js";import{r as l,R as o,p as n,v as i,l as s,Y as u,o as r,a as d,X as p,E as c,c as m,w as v,z as f,g as C,i as j,f as y,F as b,A as g,t as V,G as h,bh as _,bf as k}from"./@vue-1ad4818c.js";import"./vue3-manner-report-313033e6.js";import"./axios-85bcd05e.js";import"./crypto-js-43a3a23e.js";import"./@babel-6bd44fd1.js";import"./cron-parser-1391a961.js";import"./luxon-b01799b6.js";import"./vue-router-414ebc36.js";import"./nprogress-c04e6182.js";import"./pinia-3f60a8e1.js";import"./sortablejs-2f5483e3.js";import"./qrcodejs2-fix-bab07f30.js";import"./@element-plus-2a6d398c.js";import"./moment-bafe441d.js";import"./pinyin-pro-c81f5d39.js";import"./vue-i18n-0843b5c5.js";import"./@intlify-2528d766.js";import"./source-map-b06bd4b9.js";import"./vue-c723f8b1.js";import"./@vueuse-477b4dbb.js";import"./pinia-plugin-persistedstate-d2bd58cf.js";import"./resize-observer-polyfill-ad543aa3.js";import"./vue-smart-widget-8b3f5b0a.js";import"./echarts-b5d24e9a.js";import"./zrender-a15b073b.js";import"./tslib-a4e99503.js";import"./echarts-liquidfill-f5c863f1.js";import"./@antv-43d380be.js";import"./ant-design-vue-2db0c9af.js";import"./lodash-es-b8e8104a.js";import"./@ant-design-60f2ef8e.js";import"./@ctrl-4982d949.js";import"./dayjs-7fb79ad3.js";import"./vue-types-6bcea8eb.js";import"./dom-align-78cb5391.js";import"./vue-draggable-next-a413f884.js";import"./vue-resize-observer-90aea344.js";import"./@popperjs-b78c3215.js";import"./async-validator-cf877c1f.js";import"./memoize-one-63ab667a.js";import"./escape-html-3148dbc9.js";import"./normalize-wheel-es-3222b0a2.js";import"./@floating-ui-0ef99041.js";const S={class:"action-div"},U={class:"w-100 mt-5"},T={key:0},x={class:"input-box"},L=(e=>(_("data-v-63c14c2e"),e=e(),k(),e))((()=>C("div",{class:"info-text mt-3"},[f(" 内容 (及标题) 支持字段变量，字段变量如 "),C("span",{style:{color:"#e83e8c"}},"{createdOn}"),f(" (其中 createdOn 为源实体的字段内部标识) ")],-1))),w=["onClick"],R=e({__name:"sendNotifications",props:{modelValue:null},setup(e){const _=e,k=l(),R=l(),z=o("$API");let N=l(!1),E=l({actionContent:{}}),I=l([{label:"通知",value:2},{label:"邮件",value:8,code:"emailState"},{label:"短信",value:4,code:"smsState"},{label:"钉钉",value:16,code:"dingState"}]),q=l([]);n((()=>{E.value=_.modelValue,E.value.actionContent.userType||(E.value.actionContent.userType=1),A(),P(),E.value.actionContent.content||(E.value.actionContent.content="")}));const A=()=>{E.value.actionContent.type||1!=E.value.actionContent.userType||(E.value.actionContent.type=2),B()},B=()=>{q.value=[];let{type:e}=E.value.actionContent;I.value.forEach((t=>{(e&t.value)>0&&q.value.push(t.value)}))},G=e=>{let t=0;q.value.forEach((e=>{t|=e})),E.value.actionContent.type=t},H=e=>{2==e&&(2&E.value.actionContent.type)>0&&(E.value.actionContent.type=E.value.actionContent.type-2,B()),2==e&&(16&E.value.actionContent.type)>0&&(E.value.actionContent.type=E.value.actionContent.type-16,B())},O=()=>{var e,t;null==(t=null==(e=c(R).popperRef)?void 0:e.delayHide)||t.call(e)};let W=l(""),$=l(0);const D=e=>{$.value=e.srcElement.selectionStart},F=(e,t,a)=>e.slice(0,t)+a+e.slice(t);let J=l([]),K=i({emailState:!1,smsState:!1,dingState:!1}),M=l([]);const P=async()=>{var e,a,l,o;N.value=!0;let n=await t(E.value.entityCode,!0,!0);if(n){J.value=n.data,M.value=n.data.filter((e=>"Text"==e.fieldType));let t=await z.trigger.detial.querySendState();if(K.emailState=null==(e=t.data)?void 0:e.emailState,K.smsState=null==(a=t.data)?void 0:a.smsState,K.dingState=null==(l=t.data)?void 0:l.dingState,1==E.value.actionContent.userType)if((null==(o=E.value.actionContent.sendTo)?void 0:o.length)>0){let e=await z.trigger.detial.idToIdName(E.value.actionContent.sendTo);e&&(E.value.actionContent.inUserList=e.data,E.value.actionContent.outUserList=[])}else E.value.actionContent.inUserList=[],E.value.actionContent.outUserList=[];else E.value.actionContent.inUserList=[]}N.value=!1};return(e,t)=>{const l=s("el-radio"),o=s("el-radio-group"),n=s("mlSelectUser"),i=s("el-option"),_=s("el-select"),z=s("el-form-item"),A=s("el-input"),B=s("el-checkbox"),P=s("el-checkbox-group"),X=s("el-col"),Y=s("el-popover"),Q=u("loading");return r(),d(b,null,[p((r(),d("div",S,[m(z,{class:"mt-20",label:"发送给谁"},{default:v((()=>[m(o,{modelValue:c(E).actionContent.userType,"onUpdate:modelValue":t[0]||(t[0]=e=>c(E).actionContent.userType=e),onChange:H},{default:v((()=>[m(l,{label:1},{default:v((()=>[f("内部用户")])),_:1}),m(l,{label:2},{default:v((()=>[f("外部人员")])),_:1}),m(l,{label:3,disabled:!c(K).dingState},{default:v((()=>[f("钉钉机器人")])),_:1},8,["disabled"])])),_:1},8,["modelValue"]),C("div",U,[1==c(E).actionContent.userType?(r(),j(n,{key:0,modelValue:c(E).actionContent.inUserList,"onUpdate:modelValue":t[1]||(t[1]=e=>c(E).actionContent.inUserList=e),multiple:"",clearable:""},null,8,["modelValue"])):y("",!0),2==c(E).actionContent.userType?(r(),j(_,{key:1,modelValue:c(E).actionContent.outUserList,"onUpdate:modelValue":t[2]||(t[2]=e=>c(E).actionContent.outUserList=e),multiple:"",placeholder:"请选择",style:{width:"100%"},clearable:"",filterable:""},{default:v((()=>[(r(!0),d(b,null,g(c(M),((e,t)=>(r(),j(i,{key:t,label:e.fieldLabel,value:e.fieldName},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])):y("",!0)])])),_:1}),3==c(E).actionContent.userType?(r(),j(z,{key:0,class:"mt-20",label:"Webhook地址"},{default:v((()=>[m(A,{modelValue:c(E).actionContent.dingdingRobotUrl,"onUpdate:modelValue":t[3]||(t[3]=e=>c(E).actionContent.dingdingRobotUrl=e),placeholder:"钉钉机器人Webhook地址",clearable:""},null,8,["modelValue"])])),_:1})):y("",!0),3==c(E).actionContent.userType?(r(),j(z,{key:1,class:"mt-20",label:"加签秘钥"},{default:v((()=>[m(A,{modelValue:c(E).actionContent.dingdingSign,"onUpdate:modelValue":t[4]||(t[4]=e=>c(E).actionContent.dingdingSign=e),placeholder:"钉钉机器人加签秘钥",clearable:""},null,8,["modelValue"])])),_:1})):y("",!0),3!=c(E).actionContent.userType?(r(),j(z,{key:2,class:"mt-20",label:"通知类型"},{default:v((()=>[m(P,{modelValue:c(q),"onUpdate:modelValue":t[5]||(t[5]=e=>h(q)?q.value=e:q=e),onChange:G},{default:v((()=>[(r(!0),d(b,null,g(c(I),((e,t)=>(r(),j(B,{key:t,label:e.value,disabled:2==c(E).actionContent.userType&&(2==e.value||16==e.value)},{default:v((()=>[f(V(e.label)+" ",1),e.code?(r(),d("span",T,"("+V(c(K)[e.code]?"可用":"不可用")+")",1)):y("",!0)])),_:2},1032,["label","disabled"])))),128))])),_:1},8,["modelValue"])])),_:1})):y("",!0),c(q).includes(8)?(r(),j(z,{key:3,class:"mt-20",label:"邮件标题"},{default:v((()=>[m(A,{modelValue:c(E).actionContent.title,"onUpdate:modelValue":t[6]||(t[6]=e=>c(E).actionContent.title=e),placeholder:"你有一条新通知"},null,8,["modelValue"])])),_:1})):y("",!0),m(z,{class:"mt-20",label:"内容"},{default:v((()=>[C("div",x,[m(A,{modelValue:c(E).actionContent.content,"onUpdate:modelValue":t[7]||(t[7]=e=>c(E).actionContent.content=e),placeholder:"数据校验未通过",autosize:{minRows:3},type:"textarea",onBlur:D,ref_key:"contentInputRef",ref:W},null,8,["modelValue"]),p((r(),d("span",{ref_key:"buttonRef",ref:k,class:"field-span"},[f("{ }")])),[[c(a),O]])]),m(X,{span:24},{default:v((()=>[L])),_:1})])),_:1})])),[[Q,c(N)]]),m(Y,{ref_key:"popoverRef",ref:R,"virtual-ref":k.value,trigger:"click",placement:"left","popper-class":"formula-popover","virtual-triggering":""},{default:v((()=>[(r(!0),d(b,null,g(c(J),((e,t)=>(r(),d("div",{class:"field-item text-ellipsis",key:t,onClick:t=>(e=>{E.value.actionContent.content=F(E.value.actionContent.content,$.value,`{${e.fieldName}}`);let t=$.value+e.fieldName.length+2;W.value.focus(),setTimeout((()=>{W.value.ref.setSelectionRange(t,t)}),0),R.value.hide()})(e)},V(e.fieldLabel),9,w)))),128))])),_:1},8,["virtual-ref"])],64)}}},[["__scopeId","data-v-63c14c2e"]]);export{R as default};
