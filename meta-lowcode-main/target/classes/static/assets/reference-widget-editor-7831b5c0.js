import{_ as e,ad as t,ae as l,aj as a,ak as i,a0 as s,al as o,am as d,an as r}from"./index-ea085229.js";import{F as n}from"./field-state-variables-3bb6d4cb.js";import{l as f,o as m,i as u,w as p,z as h,f as c,c as y,a0 as g,ah as b,g as E,a as F,A as _,t as j,F as w,ag as v,bh as V,bf as k}from"./@vue-1ad4818c.js";import"./element-plus-bab58d46.js";import"./lodash-es-b8e8104a.js";import"./@vueuse-477b4dbb.js";import"./@element-plus-2a6d398c.js";import"./@babel-6bd44fd1.js";import"./@popperjs-b78c3215.js";import"./@ctrl-4982d949.js";import"./dayjs-7fb79ad3.js";import"./async-validator-cf877c1f.js";import"./memoize-one-63ab667a.js";import"./escape-html-3148dbc9.js";import"./normalize-wheel-es-3222b0a2.js";import"./@floating-ui-0ef99041.js";import"./vue3-manner-report-313033e6.js";import"./axios-85bcd05e.js";import"./crypto-js-43a3a23e.js";import"./cron-parser-1391a961.js";import"./luxon-b01799b6.js";import"./vue-router-414ebc36.js";import"./nprogress-c04e6182.js";import"./pinia-3f60a8e1.js";import"./sortablejs-2f5483e3.js";import"./qrcodejs2-fix-bab07f30.js";import"./moment-bafe441d.js";import"./pinyin-pro-c81f5d39.js";import"./vue-i18n-0843b5c5.js";import"./@intlify-2528d766.js";import"./source-map-b06bd4b9.js";import"./vue-c723f8b1.js";import"./pinia-plugin-persistedstate-d2bd58cf.js";import"./resize-observer-polyfill-ad543aa3.js";import"./vue-smart-widget-8b3f5b0a.js";import"./echarts-b5d24e9a.js";import"./zrender-a15b073b.js";import"./tslib-a4e99503.js";import"./echarts-liquidfill-f5c863f1.js";import"./@antv-43d380be.js";import"./ant-design-vue-2db0c9af.js";import"./@ant-design-60f2ef8e.js";import"./vue-types-6bcea8eb.js";import"./dom-align-78cb5391.js";import"./vue-draggable-next-a413f884.js";import"./vue-resize-observer-90aea344.js";const P={name:"ReferenceWidgetEditor",props:{entity:String,fieldName:String,showingInDialog:Boolean,fieldState:{type:Number,default:n.NEW}},data(){return{fieldProps:{name:"",label:"",type:"Reference",defaultMemberOfListFlag:!0,nullable:!1,creatable:!0,updatable:!0,fieldViewModel:{searchDialogWidth:520,validators:[]},referenceSetting:null},rules:{name:[{required:!0,message:"请输入字段名称",trigger:"blur"},{pattern:/^[a-z]+[A-Za-z\d]*$/,message:"请以小写英文字母开头，中间可输入字母或数字，禁止中文",trigger:"blur"},{min:2,max:30,message:"文字长度应在2-30之间",trigger:"blur"}],label:[{required:!0,message:"请输入显示名称",trigger:"blur"},{pattern:/^[A-Za-z\d\u4e00-\u9fa5]+[_-]*/,message:"请以中文、英文字母、数字开头，中间可输入下划线或横杠",trigger:"blur"},{min:2,max:30,message:"文字长度应在2-30之间",trigger:"blur"}]},validators:[],currentRefEntity:"",refEntityAndFields:"",refEntityName:"",refEntityLabel:"",refEntityFullName:"",showRefEntityDialogFlag:!1,showEntityListDialogFlag:!1,fieldItems:[],selectedFieldItems:[],columns:[{prop:"name",label:"实体名称",width:"150",align:"center"},{prop:"label",label:"显示名称",width:"200",align:"center",formatter:this.formatter}],tableData:[],queryText:""}},mounted(){this.fieldState===n.EDIT&&this.getFieldProps()},methods:{async getFieldProps(){let e=await t(this.fieldName,this.entity);e&&200==e.code&&e.data&&this.readFieldProps(e.data)},async readFieldProps(e){l(this.fieldProps,e),this.fieldProps.fieldViewModel||(this.fieldProps.fieldViewModel={searchDialogWidth:520}),e.entityCode&&(this.fieldProps.entityCode=e.entityCode);let t=await a(e.name,this.entity);t&&200==t.code&&t.data&&(this.currentRefEntity=t.data.currentRefEntity,this.refEntityName=t.data.refEntityName,this.refEntityLabel=t.data.refEntityLabel,this.refEntityFullName=t.data.refEntityFullName,this.refEntityAndFields=t.data.refEntityAndFields,this.fieldItems=t.data.fieldItems,this.selectedFieldItems=t.data.selectedFieldItems)},saveField(){this.$refs.editorForm.validate((async e=>{if(!e)return this.$message.error("数据不合规范，请检查"),!1;if(!this.fieldProps.referenceSetting)return void this.$message.error("请设置引用实体！");this.fieldProps.type="Reference";let t=d;this.fieldState===n.EDIT&&(t=r);let l=await t(this.fieldProps,this.entity,this.currentRefEntity);l&&200==l.code&&(this.$message.success("保存成功"),this.$emit("fieldSaved"))}))},cancelSave(){this.$emit("cancelSave")},clearReferTo(){this.currentRefEntity="",this.refEntityAndFields="",this.fieldProps.referenceSetting=null},showRefEntitySettingDialog(){this.showRefEntityDialogFlag=!0},setRefEntity(){if(this.selectedFieldItems.length<=0)return void this.$message.info("请至少选择一个显示字段！");let e=this.refEntityLabel+"[";for(let l=0;l<this.selectedFieldItems.length;l++)e+=this.selectedFieldItems[l].label+",";e+="]",this.refEntityAndFields=e,this.currentRefEntity=this.refEntityName;let t=[];this.selectedFieldItems.forEach((e=>{t.push(e.name)})),this.fieldProps.referenceSetting=[{entityName:this.currentRefEntity,fieldList:t}],this.showRefEntityDialogFlag=!1},showEntityListDialog(){this.tableData.length=0,this.loadEntityList(),this.showEntityListDialogFlag=!0},async selectEntity(e){this.refEntityName=e.name,this.refEntityLabel=e.label,this.refEntityFullName=this.refEntityLabel+"("+this.refEntityName+")",this.showEntityListDialogFlag=!1,this.fieldItems.length=0;let t=await i(this.refEntityName);if(t&&200==t.code){let e=t.data;e&&e.filter((e=>{"PrimaryKey"!==e.type&&this.fieldItems.push(e)}))}},setRefEntityListField(e,t){if(t)this.selectedFieldItems.push(e);else for(let l=this.selectedFieldItems.length-1;l>=0;l--)this.selectedFieldItems[l].name===e.name&&this.selectedFieldItems.splice(l,1)},isSelectedField(e){let t=!1;return this.selectedFieldItems.forEach((l=>{e.name===l.name&&(t=!0)})),t},handleFieldLabelChange(e){e&&(this.fieldProps.name||(this.fieldProps.name=s(e)))},generateFieldName(){this.fieldProps.name=s(this.fieldProps.label)},async loadEntityList(){let e=await o(this.queryText);if(e&&200==e.code){this.tableData.length=0;let t=e.data;t&&t.filter((e=>{!1===e.detailEntityFlag&&this.tableData.push({name:e.name,label:e.label})}))}},doSearch(){this.loadEntityList()},cancelSearch(){this.loadEntityList()}}},D=e=>(V("data-v-1d8b90b8"),e=e(),k(),e),S=D((()=>E("hr",{style:{border:"0","margin-bottom":"15px"}},null,-1))),C=D((()=>E("div",{style:{"margin-bottom":"6px"}},"选择实体搜索列表字段：",-1))),I={key:0,style:{"font-style":"italic"}},L=D((()=>E("div",{style:{margin:"20px 0 6px"}},"已选择的显示字段：",-1))),x={key:0,style:{"font-style":"italic"}},R={class:"dialog-footer"};const N=e(P,[["render",function(e,t,l,a,i,s){const o=f("el-header"),d=f("el-input"),r=f("el-form-item"),n=f("el-button"),V=f("Close"),k=f("el-icon"),P=f("el-input-number"),D=f("el-radio"),N=f("el-radio-group"),A=f("el-form"),U=f("el-main"),z=f("el-checkbox"),M=f("el-card"),q=f("el-container"),T=f("el-dialog"),$=f("Search"),W=f("SimpleTable");return m(),u(q,{class:"field-props-container"},{default:p((()=>[l.showingInDialog?c("",!0):(m(),u(o,{key:0,class:"field-props-header"},{default:p((()=>[h("[一对一引用]字段属性设置")])),_:1})),y(U,{class:"field-props-pane"},{default:p((()=>[y(A,{ref:"editorForm",model:i.fieldProps,rules:i.rules,"label-position":"left","label-width":"220px",onSubmit:t[8]||(t[8]=g((()=>{}),["prevent"]))},{default:p((()=>[y(r,{label:"显示名称",prop:"label"},{default:p((()=>[y(d,{modelValue:i.fieldProps.label,"onUpdate:modelValue":t[0]||(t[0]=e=>i.fieldProps.label=e),onChange:s.handleFieldLabelChange},null,8,["modelValue","onChange"])])),_:1}),y(r,{label:"字段名称",prop:"name"},{default:p((()=>[y(d,{modelValue:i.fieldProps.name,"onUpdate:modelValue":t[1]||(t[1]=e=>i.fieldProps.name=e),disabled:1!==l.fieldState},b({_:2},[1===l.fieldState?{name:"append",fn:p((()=>[y(n,{onClick:s.generateFieldName},{default:p((()=>[h("刷新生成")])),_:1},8,["onClick"])])),key:"0"}:void 0]),1032,["modelValue","disabled"])])),_:1}),y(r,{label:"引用实体"},{default:p((()=>[y(d,{modelValue:i.refEntityAndFields,"onUpdate:modelValue":t[2]||(t[2]=e=>i.refEntityAndFields=e),readonly:""},b({suffix:p((()=>[i.refEntityAndFields&&1===l.fieldState||2===l.fieldState?(m(),u(k,{key:0,title:"清除",class:"el-input__icon",onClick:s.clearReferTo},{default:p((()=>[y(V)])),_:1},8,["onClick"])):c("",!0)])),_:2},[1===l.fieldState||2===l.fieldState?{name:"append",fn:p((()=>[y(n,{icon:"el-icon-search",title:"选择",onClick:s.showRefEntitySettingDialog},null,8,["onClick"])])),key:"0"}:void 0]),1032,["modelValue"])])),_:1}),y(r,{label:"搜索弹窗宽度（单位：像素）："},{default:p((()=>[y(P,{type:"number",modelValue:i.fieldProps.fieldViewModel.searchDialogWidth,"onUpdate:modelValue":t[3]||(t[3]=e=>i.fieldProps.fieldViewModel.searchDialogWidth=e),modelModifiers:{number:!0},style:{width:"100%"}},null,8,["modelValue"])])),_:1}),y(r,{label:"是否在列表中默认显示"},{default:p((()=>[y(N,{modelValue:i.fieldProps.defaultMemberOfListFlag,"onUpdate:modelValue":t[4]||(t[4]=e=>i.fieldProps.defaultMemberOfListFlag=e),style:{float:"right"}},{default:p((()=>[y(D,{label:!0},{default:p((()=>[h("是")])),_:1}),y(D,{label:!1},{default:p((()=>[h("否")])),_:1})])),_:1},8,["modelValue"])])),_:1}),y(r,{label:"是否允许空值"},{default:p((()=>[y(N,{modelValue:i.fieldProps.nullable,"onUpdate:modelValue":t[5]||(t[5]=e=>i.fieldProps.nullable=e),style:{float:"right"}},{default:p((()=>[y(D,{label:!0},{default:p((()=>[h("是")])),_:1}),y(D,{label:!1},{default:p((()=>[h("否")])),_:1})])),_:1},8,["modelValue"])])),_:1}),y(r,{label:"新建记录时允许修改字段"},{default:p((()=>[y(N,{modelValue:i.fieldProps.creatable,"onUpdate:modelValue":t[6]||(t[6]=e=>i.fieldProps.creatable=e),style:{float:"right"}},{default:p((()=>[y(D,{label:!0},{default:p((()=>[h("是")])),_:1}),y(D,{label:!1},{default:p((()=>[h("否")])),_:1})])),_:1},8,["modelValue"])])),_:1}),y(r,{label:"更新记录时允许修改字段"},{default:p((()=>[y(N,{modelValue:i.fieldProps.updatable,"onUpdate:modelValue":t[7]||(t[7]=e=>i.fieldProps.updatable=e),style:{float:"right"}},{default:p((()=>[y(D,{label:!0},{default:p((()=>[h("是")])),_:1}),y(D,{label:!1},{default:p((()=>[h("否")])),_:1})])),_:1},8,["modelValue"])])),_:1}),S,y(r,null,{default:p((()=>[y(n,{type:"primary",style:{width:"120px"},onClick:s.saveField},{default:p((()=>[h("保存字段")])),_:1},8,["onClick"]),l.showingInDialog?(m(),u(n,{key:0,onClick:s.cancelSave},{default:p((()=>[h("取消")])),_:1},8,["onClick"])):c("",!0)])),_:1})])),_:1},8,["model","rules"])])),_:1}),i.showRefEntityDialogFlag?(m(),u(T,{key:1,title:"引用实体设置",class:"refer-entity-dialog",modelValue:i.showRefEntityDialogFlag,"onUpdate:modelValue":t[11]||(t[11]=e=>i.showRefEntityDialogFlag=e),"append-to-body":!0,width:"560px"},{footer:p((()=>[E("div",R,[y(n,{onClick:t[10]||(t[10]=e=>i.showRefEntityDialogFlag=!1)},{default:p((()=>[h("取 消")])),_:1}),y(n,{type:"primary",onClick:s.setRefEntity},{default:p((()=>[h("确 定")])),_:1},8,["onClick"])])])),default:p((()=>[y(q,null,{default:p((()=>[y(o,null,{default:p((()=>[y(d,{placeholder:"请选择引用实体",modelValue:i.refEntityFullName,"onUpdate:modelValue":t[9]||(t[9]=e=>i.refEntityFullName=e),readonly:!0},{append:p((()=>[1===l.fieldState?(m(),u(n,{key:0,icon:"el-icon-search",title:"选择",onClick:s.showEntityListDialog},null,8,["onClick"])):c("",!0)])),_:1},8,["modelValue"])])),_:1}),y(U,null,{default:p((()=>[E("div",null,[C,y(M,{shadow:"never"},{default:p((()=>[i.fieldItems.length<=0?(m(),F("div",I,"暂无字段可选")):c("",!0),(m(!0),F(w,null,_(i.fieldItems,((e,t)=>(m(),u(z,{key:t,checked:s.isSelectedField(e),onChange:t=>s.setRefEntityListField(e,t)},{default:p((()=>[h(j(e.label)+"("+j(e.name)+")",1)])),_:2},1032,["checked","onChange"])))),128))])),_:1})]),E("div",null,[L,y(M,{shadow:"never"},{default:p((()=>[i.selectedFieldItems.length<=0?(m(),F("div",x,"未选择显示字段")):c("",!0),(m(!0),F(w,null,_(i.selectedFieldItems,((e,t)=>(m(),F("div",{key:t},j(e.label)+"("+j(e.name)+")",1)))),128))])),_:1})])])),_:1})])),_:1})])),_:1},8,["modelValue"])):c("",!0),i.showEntityListDialogFlag?(m(),u(T,{key:2,ref:"entityListDlg",title:"选择引用实体",modelValue:i.showEntityListDialogFlag,"onUpdate:modelValue":t[13]||(t[13]=e=>i.showEntityListDialogFlag=e),"append-to-body":!0,"destroy-on-close":!0,class:"entity-list-dialog",width:"560px"},{default:p((()=>[y(q,null,{default:p((()=>[y(o,null,{default:p((()=>[y(d,{modelValue:i.queryText,"onUpdate:modelValue":t[12]||(t[12]=e=>i.queryText=e),type:"text",placeholder:"请输入关键词搜索",onKeyup:v(s.doSearch,["enter","native"]),clearable:"",onClear:s.cancelSearch},{append:p((()=>[y(n,{onClick:s.doSearch},{default:p((()=>[y(k,null,{default:p((()=>[y($)])),_:1})])),_:1},8,["onClick"])])),_:1},8,["modelValue","onKeyup","onClear"])])),_:1}),y(U,{class:"table-main-wrapper"},{default:p((()=>[y(W,{"show-pagination":!1,"show-check-box":!1,"table-size":"small",columns:i.columns,data:i.tableData,"show-operation-column":!0,"max-height":420},{table_operation:p((({scope:e})=>[y(n,{class:"",icon:"el-icon-check",onClick:t=>s.selectEntity(e.row)},{default:p((()=>[h("选择")])),_:2},1032,["onClick"])])),_:1},8,["columns","data"])])),_:1})])),_:1})])),_:1},8,["modelValue"])):c("",!0)])),_:1})}],["__scopeId","data-v-1d8b90b8"]]);export{N as default};