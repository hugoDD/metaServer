import{u as e}from"./vue-router-414ebc36.js";import{r as t,l as o,o as a,a as l,c as r,w as i,E as n,F as s,g as d}from"./@vue-1ad4818c.js";const p=d("span",{class:"ml-5"},"添加",-1),u={__name:"templates-list",setup(d){const u=e();let m=t(""),c=t([{fieldName:"reportName",op:"LK",value:""}]),f=t([{prop:"reportName",label:"名称",sortable:!0,highlight:!0},{prop:"entityCode",label:"应用实体",entityCode:!0,sortable:!0},{prop:"isDisabled",label:"启用",align:"center",customSolt:"switch",isNegation:!0,width:80},{prop:"modifiedOn",label:"修改时间",align:"center",width:100,fromNow:!0}]);const v=e=>{let t={...e};t.actionType=t.actionType.value,y.value.form={...t},g.value.dialogForm={...y.value},g.value.saveProcess()};let g=t(),y=t({saveEntity:"ReportConfig",saveIdCode:"reportConfigId",checkCodes:["reportName"],codeErrMsg:["请输入模板名称"],fromEntityLabel:"应用实体",showFormItem:[{label:"名称",code:"reportName",type:"1"}]});const C=()=>{b({target:"add"})},b=e=>{let{target:t,row:o}=e,{defaultCode:a}=m.value;"add"===t?(y.value.title="添加模板",y.value.form={entityCode:"all"==a?"":parseInt(a)},y.value.type="add"):(y.value.title="编辑模板",y.value.type="edit",y.value.form={...o}),g.value.openDialog(y.value)},h=async()=>{m.value.getEntityList()},w=e=>{const t=u.resolve({path:"/web/luckysheet",query:{reportConfigId:e.reportConfigId}});window.open(t.href,"_blank")};return(e,t)=>{const d=o("ElIconPlus"),u=o("el-icon"),y=o("el-button"),I=o("mlEntityMenuAndList"),N=o("mlActiveDialog");return a(),l(s,null,[r(I,{ref_key:"mlEntityMenuAndListRef",ref:m,entityName:"ReportConfig",aciveId:"reportConfigId",fieldsList:"reportName,entityCode,isDisabled,modifiedOn,modifiedBy,createdOn",onGoDetial:w,checkCodes:["reportName"],codeErrMsg:["请输入模板名称"],tableColumn:n(f),showFormItem:[{label:"名称",code:"reportName",type:"1"}],defalutSortField:"createdOn",filterItems:n(c),onActionBtn:b,onChangeSwitch:v,onOpenAddDialog:C},{addbutton:i((()=>[r(y,{type:"primary",onClick:t[0]||(t[0]=e=>b({target:"add"}))},{default:i((()=>[r(u,{size:"14"},{default:i((()=>[r(d)])),_:1}),p])),_:1})])),_:1},8,["tableColumn","filterItems"]),r(N,{ref_key:"mlActiveDialogRefs",ref:g,onSaveProcess:h},null,512)],64)}}};export{u as default};