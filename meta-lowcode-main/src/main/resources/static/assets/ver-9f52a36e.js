import{_ as e}from"./index-ea085229.js";import{l as o,o as t,i,w as a,g as n,t as r,c as l,z as d}from"./@vue-1ad4818c.js";const s={style:{height:"210px","text-align":"center"}},p={style:{"margin-top":"15px"}},g={style:{"margin-top":"5px"}},c={style:{"margin-top":"20px"}};const m=e({title:"版本信息",icon:"el-icon-monitor",description:"当前项目版本信息",data:()=>({ver:"loading..."}),mounted(){this.getVer()},methods:{async getVer(){},golog(){window.open("https://gitee.com/MetaLowCode/MetaLowCode")},gogit(){window.open("https://gitee.com/MetaLowCode/MetaLowCode")}}},[["render",function(e,m,u,w,y,C){const h=o("el-button"),_=o("el-card");return t(),i(_,{shadow:"hover",header:"版本信息"},{default:a((()=>[n("div",s,[n("h2",p,"MetaLowCode "+r(e.$CONFIG.CORE_VER),1),n("p",g,"最新版本 "+r(y.ver),1)]),n("div",c,[l(h,{type:"primary",plain:"",round:"",onClick:C.golog},{default:a((()=>[d("更新日志")])),_:1},8,["onClick"]),l(h,{type:"primary",plain:"",round:"",onClick:C.gogit},{default:a((()=>[d("gitee")])),_:1},8,["onClick"])])])),_:1})}]]),u=Object.freeze(Object.defineProperty({__proto__:null,default:m},Symbol.toStringTag,{value:"Module"}));export{u as _};
