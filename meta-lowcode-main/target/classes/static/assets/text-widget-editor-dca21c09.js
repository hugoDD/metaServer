import{F as e}from"./field-state-variables-3bb6d4cb.js";import{f as l}from"./field-editor-mixin-140fe671.js";import{l as a,o as t,i as o,w as i,z as r,f as s,c as d,a0 as m,ah as p,bh as n,bf as u,g as f}from"./@vue-1ad4818c.js";import{_ as b}from"./index-ea085229.js";import"./element-plus-bab58d46.js";import"./lodash-es-b8e8104a.js";import"./@vueuse-477b4dbb.js";import"./@element-plus-2a6d398c.js";import"./@babel-6bd44fd1.js";import"./@popperjs-b78c3215.js";import"./@ctrl-4982d949.js";import"./dayjs-7fb79ad3.js";import"./async-validator-cf877c1f.js";import"./memoize-one-63ab667a.js";import"./escape-html-3148dbc9.js";import"./normalize-wheel-es-3222b0a2.js";import"./@floating-ui-0ef99041.js";import"./vue3-manner-report-313033e6.js";import"./axios-85bcd05e.js";import"./crypto-js-43a3a23e.js";import"./cron-parser-1391a961.js";import"./luxon-b01799b6.js";import"./vue-router-414ebc36.js";import"./nprogress-c04e6182.js";import"./pinia-3f60a8e1.js";import"./sortablejs-2f5483e3.js";import"./qrcodejs2-fix-bab07f30.js";import"./moment-bafe441d.js";import"./pinyin-pro-c81f5d39.js";import"./vue-i18n-0843b5c5.js";import"./@intlify-2528d766.js";import"./source-map-b06bd4b9.js";import"./vue-c723f8b1.js";import"./pinia-plugin-persistedstate-d2bd58cf.js";import"./resize-observer-polyfill-ad543aa3.js";import"./vue-smart-widget-8b3f5b0a.js";import"./echarts-b5d24e9a.js";import"./zrender-a15b073b.js";import"./tslib-a4e99503.js";import"./echarts-liquidfill-f5c863f1.js";import"./@antv-43d380be.js";import"./ant-design-vue-2db0c9af.js";import"./@ant-design-60f2ef8e.js";import"./vue-types-6bcea8eb.js";import"./dom-align-78cb5391.js";import"./vue-draggable-next-a413f884.js";import"./vue-resize-observer-90aea344.js";const j={name:"TextWidgetEditor",props:{entity:String,fieldName:String,showingInDialog:Boolean,fieldState:{type:Number,default:e.NEW}},mixins:[l],data:()=>({fieldProps:{name:"",label:"",type:"Text",defaultMemberOfListFlag:!0,nullable:!1,creatable:!0,updatable:!0,fieldViewModel:{minLength:0,maxLength:200,validators:[]}},validators:[{value:"number",label:"数字"},{value:"letterStartNumberIncluded",label:"字母开头可包含数字"},{value:"mobile",label:"手机号码"},{value:"noChinese",label:"禁止中文"},{value:"email",label:"电子邮箱"},{value:"url",label:"URL网址"}]}),mounted(){},methods:{saveField(){this.doSave("Text")},cancelSave(){this.$emit("cancelSave")}}},g=(e=>(n("data-v-6942e460"),e=e(),u(),e))((()=>f("hr",{style:{border:"0","margin-bottom":"15px"}},null,-1)));const c=b(j,[["render",function(e,l,n,u,f,b){const j=a("el-header"),c=a("el-input"),h=a("el-form-item"),v=a("el-button"),_=a("el-input-number"),V=a("el-radio"),y=a("el-radio-group"),x=a("el-form"),P=a("el-main"),w=a("el-container");return t(),o(w,{class:"field-props-container"},{default:i((()=>[n.showingInDialog?s("",!0):(t(),o(j,{key:0,class:"field-props-header"},{default:i((()=>[r("[文本]字段属性设置")])),_:1})),d(P,{class:"field-props-pane"},{default:i((()=>[d(x,{ref:"editorForm",model:f.fieldProps,rules:e.rules,"label-position":"left","label-width":"220px",onSubmit:l[8]||(l[8]=m((()=>{}),["prevent"]))},{default:i((()=>[d(h,{label:"显示名称",prop:"label"},{default:i((()=>[d(c,{modelValue:f.fieldProps.label,"onUpdate:modelValue":l[0]||(l[0]=e=>f.fieldProps.label=e),onChange:e.handleFieldLabelChange},null,8,["modelValue","onChange"])])),_:1}),d(h,{label:"字段名称",prop:"name"},{default:i((()=>[d(c,{modelValue:f.fieldProps.name,"onUpdate:modelValue":l[1]||(l[1]=e=>f.fieldProps.name=e),disabled:1!==n.fieldState},p({_:2},[1===n.fieldState?{name:"append",fn:i((()=>[d(v,{onClick:e.generateFieldName},{default:i((()=>[r("刷新生成")])),_:1},8,["onClick"])])),key:"0"}:void 0]),1032,["modelValue","disabled"])])),_:1}),d(h,{label:"最小长度"},{default:i((()=>[d(_,{modelValue:f.fieldProps.fieldViewModel.minLength,"onUpdate:modelValue":l[2]||(l[2]=e=>f.fieldProps.fieldViewModel.minLength=e),min:0,max:190,style:{width:"100%"}},null,8,["modelValue"])])),_:1}),d(h,{label:"最大长度（建议不超过200）"},{default:i((()=>[d(_,{modelValue:f.fieldProps.fieldViewModel.maxLength,"onUpdate:modelValue":l[3]||(l[3]=e=>f.fieldProps.fieldViewModel.maxLength=e),min:0,max:500,style:{width:"100%"}},null,8,["modelValue"])])),_:1}),d(h,{label:"是否在列表中默认显示"},{default:i((()=>[d(y,{modelValue:f.fieldProps.defaultMemberOfListFlag,"onUpdate:modelValue":l[4]||(l[4]=e=>f.fieldProps.defaultMemberOfListFlag=e),style:{float:"right"}},{default:i((()=>[d(V,{label:!0},{default:i((()=>[r("是")])),_:1}),d(V,{label:!1},{default:i((()=>[r("否")])),_:1})])),_:1},8,["modelValue"])])),_:1}),d(h,{label:"是否允许空值"},{default:i((()=>[d(y,{modelValue:f.fieldProps.nullable,"onUpdate:modelValue":l[5]||(l[5]=e=>f.fieldProps.nullable=e),style:{float:"right"}},{default:i((()=>[d(V,{label:!0},{default:i((()=>[r("是")])),_:1}),d(V,{label:!1},{default:i((()=>[r("否")])),_:1})])),_:1},8,["modelValue"])])),_:1}),d(h,{label:"新建记录时允许修改字段"},{default:i((()=>[d(y,{modelValue:f.fieldProps.creatable,"onUpdate:modelValue":l[6]||(l[6]=e=>f.fieldProps.creatable=e),style:{float:"right"}},{default:i((()=>[d(V,{label:!0},{default:i((()=>[r("是")])),_:1}),d(V,{label:!1},{default:i((()=>[r("否")])),_:1})])),_:1},8,["modelValue"])])),_:1}),d(h,{label:"更新记录时允许修改字段"},{default:i((()=>[d(y,{modelValue:f.fieldProps.updatable,"onUpdate:modelValue":l[7]||(l[7]=e=>f.fieldProps.updatable=e),style:{float:"right"}},{default:i((()=>[d(V,{label:!0},{default:i((()=>[r("是")])),_:1}),d(V,{label:!1},{default:i((()=>[r("否")])),_:1})])),_:1},8,["modelValue"])])),_:1}),g,d(h,null,{default:i((()=>[d(v,{type:"primary",style:{width:"120px"},onClick:b.saveField},{default:i((()=>[r("保存字段")])),_:1},8,["onClick"]),n.showingInDialog?(t(),o(v,{key:0,onClick:b.cancelSave},{default:i((()=>[r("取消")])),_:1},8,["onClick"])):s("",!0)])),_:1})])),_:1},8,["model","rules"])])),_:1})])),_:1})}],["__scopeId","data-v-6942e460"]]);export{c as default};
