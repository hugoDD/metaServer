function e(e,r){var t;return e="object"==typeof(t=e)&&null!==t?e:Object.create(null),new Proxy(e,{get:(e,t,o)=>"key"===t?Reflect.get(e,t,o):Reflect.get(e,t,o)||Reflect.get(r,t,o)})}function r(e,{storage:r,serializer:t,key:o,debug:s}){try{const s=null==r?void 0:r.getItem(o);s&&e.$patch(null==t?void 0:t.deserialize(s))}catch(n){}}function t(e,{storage:r,serializer:t,key:o,paths:s,debug:n}){try{const n=Array.isArray(s)?function(e,r){return r.reduce(((r,t)=>{const o=t.split(".");return function(e,r,t){return r.slice(0,-1).reduce(((e,r)=>/^(__proto__)$/.test(r)?{}:e[r]=e[r]||{}),e)[r[r.length-1]]=t,e}(r,o,function(e,r){return r.reduce(((e,r)=>null==e?void 0:e[r]),e)}(e,o))}),{})}(e,s):e;r.setItem(o,t.serialize(n))}catch(a){}}var o=function(o={}){return s=>{const{auto:n=!1}=o,{options:{persist:a=n},store:l}=s;if(!a)return;const i=(Array.isArray(a)?a.map((r=>e(r,o))):[e(a,o)]).map((({storage:e=localStorage,beforeRestore:r=null,afterRestore:t=null,serializer:s={serialize:JSON.stringify,deserialize:JSON.parse},key:n=l.$id,paths:a=null,debug:i=!1})=>{var u;return{storage:e,beforeRestore:r,afterRestore:t,serializer:s,key:(null!=(u=o.key)?u:e=>e)(n),paths:a,debug:i}}));l.$persist=()=>{i.forEach((e=>{t(l.$state,e)}))},l.$hydrate=({runHooks:e=!0}={})=>{i.forEach((t=>{const{beforeRestore:o,afterRestore:n}=t;e&&(null==o||o(s)),r(l,t),e&&(null==n||n(s))}))},i.forEach((e=>{const{beforeRestore:o,afterRestore:n}=e;null==o||o(s),r(l,e),null==n||n(s),l.$subscribe(((r,o)=>{t(o,e)}),{detached:!0})}))}}();export{o as s};