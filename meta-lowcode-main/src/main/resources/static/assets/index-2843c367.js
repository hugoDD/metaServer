import{_ as e,V as t,q as s,Q as o,R as r,s as i}from"./index-ea085229.js";import{u as a}from"./vue-router-414ebc36.js";import{a as n}from"./element-plus-bab58d46.js";import{r as p,p as l,q as m,l as u,Y as j,X as d,E as c,o as v,i as f,w as g,c as y,z as h}from"./@vue-1ad4818c.js";import"./vue3-manner-report-313033e6.js";import"./axios-85bcd05e.js";import"./crypto-js-43a3a23e.js";import"./@babel-6bd44fd1.js";import"./cron-parser-1391a961.js";import"./luxon-b01799b6.js";import"./nprogress-c04e6182.js";import"./pinia-3f60a8e1.js";import"./sortablejs-2f5483e3.js";import"./qrcodejs2-fix-bab07f30.js";import"./@element-plus-2a6d398c.js";import"./moment-bafe441d.js";import"./pinyin-pro-c81f5d39.js";import"./vue-i18n-0843b5c5.js";import"./@intlify-2528d766.js";import"./source-map-b06bd4b9.js";import"./vue-c723f8b1.js";import"./@vueuse-477b4dbb.js";import"./pinia-plugin-persistedstate-d2bd58cf.js";import"./resize-observer-polyfill-ad543aa3.js";import"./vue-smart-widget-8b3f5b0a.js";import"./echarts-b5d24e9a.js";import"./zrender-a15b073b.js";import"./tslib-a4e99503.js";import"./echarts-liquidfill-f5c863f1.js";import"./@antv-43d380be.js";import"./ant-design-vue-2db0c9af.js";import"./lodash-es-b8e8104a.js";import"./@ant-design-60f2ef8e.js";import"./@ctrl-4982d949.js";import"./dayjs-7fb79ad3.js";import"./vue-types-6bcea8eb.js";import"./dom-align-78cb5391.js";import"./vue-draggable-next-a413f884.js";import"./vue-resize-observer-90aea344.js";import"./@popperjs-b78c3215.js";import"./async-validator-cf877c1f.js";import"./memoize-one-63ab667a.js";import"./escape-html-3148dbc9.js";import"./normalize-wheel-es-3222b0a2.js";import"./@floating-ui-0ef99041.js";const b=e({__name:"index",setup(e){const b=a(),{Utils:_}=t.VFormSDK;let k=p(!1),C=p(""),w=p({componentLib:!1,formTemplates:!1,logoHeader:!1,layoutTypeButton:!1,clearDesignerButton:!1,previewFormButton:!1,importJsonButton:!1,exportJsonButton:!1,exportCodeButton:!1,generateSFCButton:!1,toolbarMaxWidth:300,chartLib:!0}),x=p();l((()=>{C.value=b.currentRoute.value.query.chartId,C.value?D():b.push("/web/dashboard-list")}));const D=async()=>{k.value=!0;let e=await s(C.value,"chartData");if(e)if(e.data.chartData){let t=JSON.parse(e.data.chartData);x.value.setFormJson(t)}else J();else J();k.value=!1},J=()=>{const e=o(r);e.id="dbCon"+_.generateId(),e.options.name=e.id;const t={widgetList:[e],formConfig:_.getDefaultFormConfig()};x.value.setFormJson(t)},B=()=>{x.value.previewForm()},F=()=>{x.value.importJson()},z=()=>{x.value.exportJson()},q=async()=>{k.value=!0;let e={entity:"Chart",formModel:{chartData:JSON.stringify(x.value.getFormJson(!1))},id:C.value};await i(e.entity,e.id,e.formModel)&&n.success("保存成功"),k.value=!1};return m((()=>{J()})),(e,t)=>{const s=u("ElIconDelete"),o=u("el-icon"),r=u("el-button"),i=u("View"),a=u("Finished"),n=u("v-form-designer"),p=j("loading");return d((v(),f(n,{ref_key:"dbDesignerRef",ref:x,"designer-config":c(w),class:"visual-design"},{customToolButtons:g((()=>[y(r,{type:"primary",link:"",onClick:J},{default:g((()=>[y(o,null,{default:g((()=>[y(s)])),_:1}),h("清空画布 ")])),_:1}),y(r,{type:"primary",link:"",onClick:B},{default:g((()=>[y(o,null,{default:g((()=>[y(i)])),_:1}),h("预览 ")])),_:1}),y(r,{type:"primary",link:"",onClick:F},{default:g((()=>[h("导入")])),_:1}),y(r,{type:"primary",link:"",onClick:z},{default:g((()=>[h("导出")])),_:1}),y(r,{type:"primary",link:"",onClick:q},{default:g((()=>[y(o,null,{default:g((()=>[y(a)])),_:1}),h("保存设计 ")])),_:1})])),_:1},8,["designer-config"])),[[p,c(k)]])}}},[["__scopeId","data-v-21531a55"]]);export{b as default};
