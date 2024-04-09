import{_ as e,ad as t,ae as i,U as l,af as a,ag as o}from"./index-ea085229.js";import{F as s}from"./field-state-variables-3bb6d4cb.js";import{f as n}from"./field-editor-mixin-140fe671.js";import{l as r,o as p,i as d,w as u,z as m,f,c,a0 as h,ah as b,g,a as v,A as j,t as y,X as k,$ as _,F as x,bh as I,bf as $}from"./@vue-1ad4818c.js";import"./element-plus-bab58d46.js";import"./lodash-es-b8e8104a.js";import"./@vueuse-477b4dbb.js";import"./@element-plus-2a6d398c.js";import"./@babel-6bd44fd1.js";import"./@popperjs-b78c3215.js";import"./@ctrl-4982d949.js";import"./dayjs-7fb79ad3.js";import"./async-validator-cf877c1f.js";import"./memoize-one-63ab667a.js";import"./escape-html-3148dbc9.js";import"./normalize-wheel-es-3222b0a2.js";import"./@floating-ui-0ef99041.js";import"./vue3-manner-report-313033e6.js";import"./axios-85bcd05e.js";import"./crypto-js-43a3a23e.js";import"./cron-parser-1391a961.js";import"./luxon-b01799b6.js";import"./vue-router-414ebc36.js";import"./nprogress-c04e6182.js";import"./pinia-3f60a8e1.js";import"./sortablejs-2f5483e3.js";import"./qrcodejs2-fix-bab07f30.js";import"./moment-bafe441d.js";import"./pinyin-pro-c81f5d39.js";import"./vue-i18n-0843b5c5.js";import"./@intlify-2528d766.js";import"./source-map-b06bd4b9.js";import"./vue-c723f8b1.js";import"./pinia-plugin-persistedstate-d2bd58cf.js";import"./resize-observer-polyfill-ad543aa3.js";import"./vue-smart-widget-8b3f5b0a.js";import"./echarts-b5d24e9a.js";import"./zrender-a15b073b.js";import"./tslib-a4e99503.js";import"./echarts-liquidfill-f5c863f1.js";import"./@antv-43d380be.js";import"./ant-design-vue-2db0c9af.js";import"./@ant-design-60f2ef8e.js";import"./vue-types-6bcea8eb.js";import"./dom-align-78cb5391.js";import"./vue-draggable-next-a413f884.js";import"./vue-resize-observer-90aea344.js";const C={name:"OptionWidgetEditor",props:{entity:String,fieldName:String,showingInDialog:Boolean,fieldState:{type:Number,default:s.NEW}},mixins:[n],data:()=>({fieldProps:{name:"",label:"",type:"Option",defaultMemberOfListFlag:!0,nullable:!1,creatable:!0,updatable:!0,validators:[]},optionItems:[],test:"1222",hoverIdx:-1,validators:[]}),mounted(){this.fieldState===s.EDIT&&this.getFieldProps()},methods:{getFieldProps(){t(this.fieldName,this.entity).then((e=>{null==e.error?this.readFieldProps(e.data):this.$message({message:e.error,type:"error"})})).catch((e=>{this.$message({message:e.message,type:"error"})}))},async readFieldProps(e){i(this.fieldProps,e),e.entityCode&&(this.fieldProps.entityCode=e.entityCode);let t=await l(this.entity,this.fieldName);t&&200==t.code&&(this.optionItems=t.data?t.data:[])},saveField(){this.$refs.editorForm.validate((async e=>{if(!e)return this.$message.error("数据不合规范，请检查"),!1;this.fieldProps.type="Option";let t=[];this.optionItems.forEach((e=>{e.value&&t.push({key:e.label,value:e.value})}));let i=a;this.fieldState===s.EDIT&&(i=o);let l=await i(this.fieldProps,this.entity,t);l&&200==l.code&&(this.$message.success("保存成功"),this.$emit("fieldSaved"))}))},cancelSave(){this.$emit("cancelSave")},validateOption(e,t){let i=!0;return this.optionItems.forEach(((l,a)=>{l.label===e&&a!==t&&(i=!1)})),i},getOptionMaxValue(){let e=0;return this.optionItems.forEach(((t,i)=>{t.value>e&&(e=t.value)})),e},addOption(){this.$prompt("请输入选项名称","提示",{confirmButtonText:"确定",cancelButtonText:"取消",inputPattern:/^[A-Za-z\u4e00-\u9fa5\uff0c\u3001\uff1b\uff1a\uff08\uff09\u2014\u201c\u201d\d]+$/,inputErrorMessage:"输入不正确"}).then((({value:e})=>{if(this.validateOption(e,-1)){let t={label:e,value:this.getOptionMaxValue()+1,saved:!1};this.optionItems.push(t),this.$nextTick((()=>{}))}else this.$message.warning("选项已存在")})).catch((e=>{this.$message.info("已取消")}))},insertOption(e){this.$prompt("请输入选项名称","提示",{confirmButtonText:"确定",cancelButtonText:"取消",inputPattern:/^[A-Za-z\u4e00-\u9fa5\uff0c\u3001\uff1b\uff1a\uff08\uff09\u2014\u201c\u201d\d]+$/,inputErrorMessage:"输入不正确"}).then((({value:t})=>{if(this.validateOption(t,-1)){let i={label:t,value:this.getOptionMaxValue()+1,saved:!1};this.optionItems.splice(e+1,0,i),this.$nextTick((()=>{}))}else this.$message.warning("选项已存在")})).catch((()=>{this.$message.info("已取消")}))},upOption(e){if(0===e)return void this.$message.info("已到最上");let t=this.optionItems[e];this.optionItems[e]=this.optionItems[e-1],this.optionItems[e-1]=t},downOption(e){if(e===this.optionItems.length-1)return void this.$message.info("已到最下");let t=this.optionItems[e];this.optionItems[e]=this.optionItems[e+1],this.optionItems[e+1]=t},editOption(e){let t=this.optionItems[e].label;this.$prompt("请修改选项名称","提示",{confirmButtonText:"确定",cancelButtonText:"取消",inputValue:t,inputPattern:/^[A-Za-z\u4e00-\u9fa5\uff0c\u3001\uff1b\uff1a\uff08\uff09\u2014\u201c\u201d\d]+$/,inputErrorMessage:"输入不正确"}).then((({value:t})=>{this.validateOption(t,e)?(this.optionItems[e].label=t,this.$nextTick((()=>{}))):this.$message.warning("选项已存在")})).catch((()=>{this.$message.info("已取消")}))},deleteOption(e){this.$confirm("确定删除该选项?","提示").then((()=>{this.optionItems.splice(e,1),this.$message.info("选项已删除")})).catch((()=>{this.$message.info("取消删除")}))}}},O=e=>(I("data-v-e1e9b35c"),e=e(),$(),e),P={class:"clear-fix"},V=O((()=>g("span",null,"选项管理",-1))),w=["onMouseenter"],F={class:"option-item"},M=O((()=>g("hr",{style:{border:"0","margin-bottom":"15px"}},null,-1)));const S=e(C,[["render",function(e,t,i,l,a,o){const s=r("el-header"),n=r("el-input"),I=r("el-form-item"),$=r("el-button"),C=r("el-radio"),O=r("el-radio-group"),S=r("el-card"),T=r("el-form"),E=r("el-main"),z=r("el-container");return p(),d(z,{class:"field-props-container"},{default:u((()=>[i.showingInDialog?f("",!0):(p(),d(s,{key:0,class:"field-props-header"},{default:u((()=>[m("[单选项]字段属性设置")])),_:1})),c(E,{class:"field-props-pane"},{default:u((()=>[c(T,{ref:"editorForm",model:a.fieldProps,rules:e.rules,"label-position":"left","label-width":"220px",onSubmit:t[7]||(t[7]=h((()=>{}),["prevent"]))},{default:u((()=>[c(I,{label:"显示名称",prop:"label"},{default:u((()=>[c(n,{modelValue:a.fieldProps.label,"onUpdate:modelValue":t[0]||(t[0]=e=>a.fieldProps.label=e),onChange:e.handleFieldLabelChange},null,8,["modelValue","onChange"])])),_:1}),c(I,{label:"字段名称",prop:"name"},{default:u((()=>[c(n,{modelValue:a.fieldProps.name,"onUpdate:modelValue":t[1]||(t[1]=e=>a.fieldProps.name=e),disabled:1!==i.fieldState},b({_:2},[1===i.fieldState?{name:"append",fn:u((()=>[c($,{onClick:e.generateFieldName},{default:u((()=>[m("刷新生成")])),_:1},8,["onClick"])])),key:"0"}:void 0]),1032,["modelValue","disabled"])])),_:1}),c(I,{label:"是否在列表中默认显示"},{default:u((()=>[c(O,{modelValue:a.fieldProps.defaultMemberOfListFlag,"onUpdate:modelValue":t[2]||(t[2]=e=>a.fieldProps.defaultMemberOfListFlag=e),style:{float:"right"}},{default:u((()=>[c(C,{label:!0},{default:u((()=>[m("是")])),_:1}),c(C,{label:!1},{default:u((()=>[m("否")])),_:1})])),_:1},8,["modelValue"])])),_:1}),c(I,{label:"是否允许空值"},{default:u((()=>[c(O,{modelValue:a.fieldProps.nullable,"onUpdate:modelValue":t[3]||(t[3]=e=>a.fieldProps.nullable=e),style:{float:"right"}},{default:u((()=>[c(C,{label:!0},{default:u((()=>[m("是")])),_:1}),c(C,{label:!1},{default:u((()=>[m("否")])),_:1})])),_:1},8,["modelValue"])])),_:1}),c(I,{label:"新建记录时允许修改字段"},{default:u((()=>[c(O,{modelValue:a.fieldProps.creatable,"onUpdate:modelValue":t[4]||(t[4]=e=>a.fieldProps.creatable=e),style:{float:"right"}},{default:u((()=>[c(C,{label:!0},{default:u((()=>[m("是")])),_:1}),c(C,{label:!1},{default:u((()=>[m("否")])),_:1})])),_:1},8,["modelValue"])])),_:1}),c(I,{label:"更新记录时允许修改字段"},{default:u((()=>[c(O,{modelValue:a.fieldProps.updatable,"onUpdate:modelValue":t[5]||(t[5]=e=>a.fieldProps.updatable=e),style:{float:"right"}},{default:u((()=>[c(C,{label:!0},{default:u((()=>[m("是")])),_:1}),c(C,{label:!1},{default:u((()=>[m("否")])),_:1})])),_:1},8,["modelValue"])])),_:1}),c(S,{class:"box-card",shadow:"never"},{header:u((()=>[g("div",P,[V,c($,{style:{float:"right",padding:"3px 0"},link:"",type:"primary",onClick:o.addOption},{default:u((()=>[m("新增选项")])),_:1},8,["onClick"])])])),default:u((()=>[(p(!0),v(x,null,j(a.optionItems,((e,i)=>(p(),v("div",{key:i,class:"clear-fix",onMouseenter:e=>a.hoverIdx=i,onMouseleave:t[6]||(t[6]=e=>a.hoverIdx=-1)},[m(y(e.label)+" ",1),k(g("div",F,[c($,{link:"",type:"primary",class:"option-item-insert",title:"插入",icon:"el-icon-plus",onClick:e=>o.insertOption(i)},null,8,["onClick"]),c($,{link:"",type:"primary",class:"option-item-up",title:"上移",icon:"el-icon-top",onClick:e=>o.upOption(i)},null,8,["onClick"]),c($,{link:"",type:"primary",class:"option-item-down",title:"下移",icon:"el-icon-bottom",onClick:e=>o.downOption(i)},null,8,["onClick"]),c($,{link:"",type:"primary",class:"option-item-edit",title:"编辑",icon:"el-icon-edit",onClick:e=>o.editOption(i)},null,8,["onClick"]),c($,{link:"",type:"primary",class:"option-item-delete",title:"删除",icon:"el-icon-delete",onClick:e=>o.deleteOption(i)},null,8,["onClick"])],512),[[_,!!e.value&&a.hoverIdx===i]])],40,w)))),128))])),_:1}),M,c(I,null,{default:u((()=>[c($,{type:"primary",style:{width:"120px"},onClick:o.saveField},{default:u((()=>[m("保存字段")])),_:1},8,["onClick"]),i.showingInDialog?(p(),d($,{key:0,onClick:o.cancelSave},{default:u((()=>[m("取消")])),_:1},8,["onClick"])):f("",!0)])),_:1})])),_:1},8,["model","rules"])])),_:1})])),_:1})}],["__scopeId","data-v-e1e9b35c"]]);export{S as default};
