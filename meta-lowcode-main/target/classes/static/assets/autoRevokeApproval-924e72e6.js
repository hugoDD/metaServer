import{R as e,r as a,p as l,l as t,Y as s,X as o,E as n,o as u,a as i,c as d,w as c,F as r,A as v,i as m,g as p}from"./@vue-1ad4818c.js";const b={class:"action-div"},f=p("div",{class:"w-100"},[p("span",{class:"info-text"},"可撤销源实体记录或其关联记录")],-1),g={__name:"autoRevokeApproval",props:{modelValue:null},setup(p){const g=p,y=e("$API");e("$ElMessage");let V=a(!1),w=a({actionContent:{}}),C=a([]);l((()=>{w.value=g.modelValue,_()}));const _=async()=>{V.value=!0;let e=await y.trigger.detial.getDataDeleteEntityList(w.value.entityCode);e&&(C.value=e.data),V.value=!1};return(e,a)=>{const l=t("el-option"),p=t("el-select"),g=t("el-form-item"),y=s("loading");return o((u(),i("div",b,[d(g,{label:"撤销记录"},{default:c((()=>[d(p,{modelValue:n(w).actionContent.items,"onUpdate:modelValue":a[0]||(a[0]=e=>n(w).actionContent.items=e),multiple:"",placeholder:"选择撤销记录",style:{width:"100%"},clearable:"",filterable:"","value-key":"label"},{default:c((()=>[(u(!0),i(r,null,v(n(C),((e,a)=>(u(),m(l,{key:a,value:e,label:e.label},null,8,["value","label"])))),128))])),_:1},8,["modelValue"]),f])),_:1})])),[[y,n(V)]])}}};export{g as default};
