import{d as e}from"./pinia-3f60a8e1.js";import{r as a}from"./@vue-1ad4818c.js";const t=e("keepAliveStore",(()=>{let e=a([]),t=a(!0);return{keepLiveRoute:e,routeShow:t,pushKeepLive:function(a){e.value.includes(a)||e.value.push(a)},removeKeepLive:function(a){var t=e.value.indexOf(a);-1!==t&&e.value.splice(t,1)},clearKeepLive:function(a){e.value=[]},setRouteShow:function(e){t.value=e}}})),u=e("iframeStore",(()=>{let e=a([]);return{iframeList:e,setIframeList:function(a){e.value=[],e.value.push(a)},pushIframeList:function(a){e.value.find((e=>e.path===a.path))||e.value.push(a)},removeIframeList:function(a){e.value.forEach(((t,u)=>{t.path===a.path&&e.value.splice(u,1)}))},refreshIframe:function(a){e.value.forEach((e=>{if(e.path==a.path){var t=a.meta.url;e.meta.url="",setTimeout((function(){e.meta.url=t}),200)}}))},clearIframeList:function(){e.value=[]}}}),{persist:!0});export{u as a,t as u};
