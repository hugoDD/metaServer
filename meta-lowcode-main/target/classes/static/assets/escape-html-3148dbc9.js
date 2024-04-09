import{n as e}from"./@babel-6bd44fd1.js";
/*!
 * escape-html
 * Copyright(c) 2012-2013 TJ Holowaychuk
 * Copyright(c) 2015 Andreas Lubbe
 * Copyright(c) 2015 Tiancheng "Timothy" Gu
 * MIT Licensed
 */var a=/["'&<>]/;const r=e((function(e){var r,t=""+e,s=a.exec(t);if(!s)return t;var n="",c=0,b=0;for(c=s.index;c<t.length;c++){switch(t.charCodeAt(c)){case 34:r="&quot;";break;case 38:r="&amp;";break;case 39:r="&#39;";break;case 60:r="&lt;";break;case 62:r="&gt;";break;default:continue}b!==c&&(n+=t.substring(b,c)),b=c+1,n+=r}return b!==c?n+t.substring(b,c):n}));export{r as e};
