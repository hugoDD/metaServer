import{R as inject,r as ref,p as onMounted,l as resolveComponent,Y as resolveDirective,o as openBlock,i as createBlock,w as withCtx,c as createVNode,z as createTextVNode,g as createBaseVNode,E as unref,G as isRef,X as withDirectives,a as createElementBlock,A as renderList,a0 as withModifiers,t as toDisplayString,f as createCommentVNode,F as Fragment,$ as vShow,n as nextTick,h,bh as pushScopeId,bf as popScopeId}from"./@vue-1ad4818c.js";import{_ as _export_sfc,f as useCommonStore,a9 as getEntitySet,a4 as getAllTagsOfEntity,a3 as getEntityProps,as as hasDetailEntity,at as copyEntity,au as createEntity,av as entityCanBeDeleted,aw as deleteEntity}from"./index-ea085229.js";import EntityPropEditor from"./entity-property-editor-c9880942.js";import{s as storeToRefs}from"./pinia-3f60a8e1.js";import{u as useRouter}from"./vue-router-414ebc36.js";import{a as ElMessage,b as ElMessageBox}from"./element-plus-bab58d46.js";import"./vue3-manner-report-313033e6.js";import"./axios-85bcd05e.js";import"./crypto-js-43a3a23e.js";import"./@babel-6bd44fd1.js";import"./cron-parser-1391a961.js";import"./luxon-b01799b6.js";import"./nprogress-c04e6182.js";import"./sortablejs-2f5483e3.js";import"./qrcodejs2-fix-bab07f30.js";import"./@element-plus-2a6d398c.js";import"./moment-bafe441d.js";import"./pinyin-pro-c81f5d39.js";import"./vue-i18n-0843b5c5.js";import"./@intlify-2528d766.js";import"./source-map-b06bd4b9.js";import"./vue-c723f8b1.js";import"./@vueuse-477b4dbb.js";import"./pinia-plugin-persistedstate-d2bd58cf.js";import"./resize-observer-polyfill-ad543aa3.js";import"./vue-smart-widget-8b3f5b0a.js";import"./echarts-b5d24e9a.js";import"./zrender-a15b073b.js";import"./tslib-a4e99503.js";import"./echarts-liquidfill-f5c863f1.js";import"./@antv-43d380be.js";import"./ant-design-vue-2db0c9af.js";import"./lodash-es-b8e8104a.js";import"./@ant-design-60f2ef8e.js";import"./@ctrl-4982d949.js";import"./dayjs-7fb79ad3.js";import"./vue-types-6bcea8eb.js";import"./dom-align-78cb5391.js";import"./vue-draggable-next-a413f884.js";import"./vue-resize-observer-90aea344.js";import"./@popperjs-b78c3215.js";import"./async-validator-cf877c1f.js";import"./memoize-one-63ab667a.js";import"./escape-html-3148dbc9.js";import"./normalize-wheel-es-3222b0a2.js";import"./@floating-ui-0ef99041.js";const MYSQL_KEYWORDS=["ACCESSIBLE","ACCOUNT","ACTION","ACTIVE","ADD","ADMIN","AFTER","AGAINST","AGGREGATE","ALGORITHM","ALL","ALTER","ALWAYS","ANALYZE","AND","ANY","ARRAY","AS","ASC","ASCII","ASENSITIVE","ASSIGN_GTIDS_TO_ANONYMOUS_TRANSACTIONS","AT","ATTRIBUTE","AUTHENTICATION","AUTOEXTEND_SIZE","AUTO_INCREMENT","AVG","AVG_ROW_LENGTH","BACKUP","BEFORE","BEGIN","BETWEEN","BIGINT","BINARY","BINLOG","BIT","BLOB","BLOCK","BOOL","BOOLEAN","BOTH","BTREE","BUCKETS","BULK","BY","BYTE","CACHE","CALL","CASCADE","CASCADED","CASE","CATALOG_NAME","CHAIN","CHALLENGE_RESPONSE","CHANGE","CHANGED","CHANNEL","CHAR","CHARACTER","CHARSET","CHECK","CHECKSUM","CIPHER","CLASS_ORIGIN","CLIENT","CLONE","CLOSE","COALESCE","CODE","COLLATE","COLLATION","COLUMN","COLUMNS","COLUMN_FORMAT","COLUMN_NAME","COMMENT","COMMIT","COMMITTED","COMPACT","COMPLETION","COMPONENT","COMPRESSED","COMPRESSION","CONCURRENT","CONDITION","CONNECTION","CONSISTENT","CONSTRAINT","CONSTRAINT_CATALOG","CONSTRAINT_NAME","CONSTRAINT_SCHEMA","CONTAINS","CONTEXT","CONTINUE","CONVERT","CPU","CREATE","CROSS","CUBE","CUME_DIST","CURRENT","CURRENT_DATE","CURRENT_TIME","CURRENT_TIMESTAMP","CURRENT_USER","CURSOR","CURSOR_NAME","DATA","DATABASE","DATABASES","DATAFILE","DATE","DATETIME","DAY","DAY_HOUR","DAY_MICROSECOND","DAY_MINUTE","DAY_SECOND","DEALLOCATE","DEC","DECIMAL","DECLARE","DEFAULT","DEFAULT_AUTH","DEFINER","DEFINITION","DELAYED","DELAY_KEY_WRITE","DELETE","DENSE_RANK","DESC","DESCRIBE","DESCRIPTION","DETERMINISTIC","DIAGNOSTICS","DIRECTORY","DISABLE","DISCARD","DISK","DISTINCT","DISTINCTROW","DIV","DO","DOUBLE","DROP","DUAL","DUMPFILE","DUPLICATE","DYNAMIC","EACH","ELSE","ELSEIF","EMPTY","ENABLE","ENCLOSED","ENCRYPTION","END","ENDS","ENFORCED","ENGINE","ENGINES","ENGINE_ATTRIBUTE","ENUM","ERROR","ERRORS","ESCAPE","ESCAPED","EVENT","EVENTS","EVERY","EXCEPT","EXCHANGE","EXCLUDE","EXECUTE","EXISTS","EXIT","EXPANSION","EXPIRE","EXPLAIN","EXPORT","EXTENDED","EXTENT_SIZE","FACTOR","FAILED_LOGIN_ATTEMPTS","FALSE","FAST","FAULTS","FETCH","FIELDS","FILE","FILE_BLOCK_SIZE","FILTER","FINISH","FIRST","FIRST_VALUE","FIXED","FLOAT","FLOAT4","FLOAT8","FLUSH","FOLLOWING","FOLLOWS","FOR","FORCE","FOREIGN","FORMAT","FOUND","FROM","FULL","FULLTEXT","FUNCTION","GENERAL","GENERATE","GENERATED","GEOMCOLLECTION","GEOMETRY","GEOMETRYCOLLECTION","GET","GET_FORMAT","GET_MASTER_PUBLIC_KEY","GET_SOURCE_PUBLIC_KEY","GLOBAL","GRANT","GRANTS","GROUP","GROUPING","GROUPS","GROUP_REPLICATION","GTID_ONLY","HANDLER","HASH","HAVING","HELP","HIGH_PRIORITY","HISTOGRAM","HISTORY","HOST","HOSTS","HOUR","HOUR_MICROSECOND","HOUR_MINUTE","HOUR_SECOND","IDENTIFIED","IF","IGNORE","IGNORE_SERVER_IDS","IMPORT","IN","INACTIVE","INDEX","INDEXES","INFILE","INITIAL","INITIAL_SIZE","INITIATE","INNER","INOUT","INSENSITIVE","INSERT","INSERT_METHOD","INSTALL","INSTANCE","INT","INT1","INT2","INT3","INT4","INT8","INTEGER","INTERSECT","INTERVAL","INTO","INVISIBLE","INVOKER","IO","IO_AFTER_GTIDS","IO_BEFORE_GTIDS","IO_THREAD","IPC","IS","ISOLATION","ISSUER","ITERATE","JOIN","JSON","JSON_TABLE","JSON_VALUE","KEY","KEYRING","KEYS","KEY_BLOCK_SIZE","KILL","LAG","LANGUAGE","LAST","LAST_VALUE","LATERAL","LEAD","LEADING","LEAVE","LEAVES","LEFT","LESS","LEVEL","LIKE","LIMIT","LINEAR","LINES","LINESTRING","LIST","LOAD","LOCAL","LOCALTIME","LOCALTIMESTAMP","LOCK","LOCKED","LOCKS","LOGFILE","LOGS","LONG","LONGBLOB","LONGTEXT","LOOP","LOW_PRIORITY","MASTER","MASTER_AUTO_POSITION","MASTER_BIND","MASTER_COMPRESSION_ALGORITHMS","MASTER_CONNECT_RETRY","MASTER_DELAY","MASTER_HEARTBEAT_PERIOD","MASTER_HOST","MASTER_LOG_FILE","MASTER_LOG_POS","MASTER_PASSWORD","MASTER_PORT","MASTER_PUBLIC_KEY_PATH","MASTER_RETRY_COUNT","MASTER_SSL","MASTER_SSL_CA","MASTER_SSL_CAPATH","MASTER_SSL_CERT","MASTER_SSL_CIPHER","MASTER_SSL_CRL","MASTER_SSL_CRLPATH","MASTER_SSL_KEY","MASTER_SSL_VERIFY_SERVER_CERT","MASTER_TLS_CIPHERSUITES","MASTER_TLS_VERSION","MASTER_USER","MASTER_ZSTD_COMPRESSION_LEVEL","MATCH","MAXVALUE","MAX_CONNECTIONS_PER_HOUR","MAX_QUERIES_PER_HOUR","MAX_ROWS","MAX_SIZE","MAX_UPDATES_PER_HOUR","MAX_USER_CONNECTIONS","MEDIUM","MEDIUMBLOB","MEDIUMINT","MEDIUMTEXT","MEMBER","MEMORY","MERGE","MESSAGE_TEXT","MICROSECOND","MIDDLEINT","MIGRATE","MINUTE","MINUTE_MICROSECOND","MINUTE_SECOND","MIN_ROWS","MOD","MODE","MODIFIES","MODIFY","MONTH","MULTILINESTRING","MULTIPOINT","MULTIPOLYGON","MUTEX","MYSQL_ERRNO","NAME","NAMES","NATIONAL","NATURAL","NCHAR","NDB","NDBCLUSTER","NESTED","NETWORK_NAMESPACE","NEVER","NEW","NEXT","NO","NODEGROUP","NONE","NOT","NOWAIT","NO_WAIT","NO_WRITE_TO_BINLOG","NTH_VALUE","NTILE","NULL","NULLS","NUMBER","NUMERIC","NVARCHAR","OF","OFF","OFFSET","OJ","OLD","ON","ONE","ONLY","OPEN","OPTIMIZE","OPTIMIZER_COSTS","OPTION","OPTIONAL","OPTIONALLY","OPTIONS","OR","ORDER","ORDINALITY","ORGANIZATION","OTHERS","OUT","OUTER","OUTFILE","OVER","OWNER","PACK_KEYS","PAGE","PARSER","PARTIAL","PARTITION","PARTITIONING","PARTITIONS","PASSWORD","PASSWORD_LOCK_TIME","PATH","PERCENT_RANK","PERSIST","PERSIST_ONLY","PHASE","PLUGIN","PLUGINS","PLUGIN_DIR","POINT","POLYGON","PORT","PRECEDES","PRECEDING","PRECISION","PREPARE","PRESERVE","PREV","PRIMARY","PRIVILEGES","PRIVILEGE_CHECKS_USER","PROCEDURE","PROCESS","PROCESSLIST","PROFILE","PROFILES","PROXY","PURGE","QUARTER","QUERY","QUICK","RANDOM","RANGE","RANK","READ","READS","READ_ONLY","READ_WRITE","REAL","REBUILD","RECOVER","RECURSIVE","REDO_BUFFER_SIZE","REDUNDANT","REFERENCE","REFERENCES","REGEXP","REGISTRATION","RELAY","RELAYLOG","RELAY_LOG_FILE","RELAY_LOG_POS","RELAY_THREAD","RELEASE","RELOAD","REMOVE","RENAME","REORGANIZE","REPAIR","REPEAT","REPEATABLE","REPLACE","REPLICA","REPLICAS","REPLICATE_DO_DB","REPLICATE_DO_TABLE","REPLICATE_IGNORE_DB","REPLICATE_IGNORE_TABLE","REPLICATE_REWRITE_DB","REPLICATE_WILD_DO_TABLE","REPLICATE_WILD_IGNORE_TABLE","REPLICATION","REQUIRE","REQUIRE_ROW_FORMAT","REQUIRE_TABLE_PRIMARY_KEY_CHECK","RESET","RESIGNAL","RESOURCE","RESPECT","RESTART","RESTORE","RESTRICT","RESUME","RETAIN","RETURN","RETURNED_SQLSTATE","RETURNING","RETURNS","REUSE","REVERSE","REVOKE","RIGHT","RLIKE","ROLE","ROLLBACK","ROLLUP","ROTATE","ROUTINE","ROW","ROWS","ROW_COUNT","ROW_FORMAT","ROW_NUMBER","RTREE","SAVEPOINT","SCHEDULE","SCHEMA","SCHEMAS","SCHEMA_NAME","SECOND","SECONDARY","SECONDARY_ENGINE","SECONDARY_ENGINE_ATTRIBUTE","SECONDARY_LOAD","SECONDARY_UNLOAD","SECOND_MICROSECOND","SECURITY","SELECT","SENSITIVE","SEPARATOR","SERIAL","SERIALIZABLE","SERVER","SESSION","SET","SHARE","SHOW","SHUTDOWN","SIGNAL","SIGNED","SIMPLE","SKIP","SLAVE","SLOW","SMALLINT","SNAPSHOT","SOCKET","SOME","SONAME","SOUNDS","SOURCE","SOURCE_AUTO_POSITION","SOURCE_BIND","SOURCE_COMPRESSION_ALGORITHMS","SOURCE_CONNECTION_AUTO_FAILOVER","SOURCE_CONNECT_RETRY","SOURCE_DELAY","SOURCE_HEARTBEAT_PERIOD","SOURCE_HOST","SOURCE_LOG_FILE","SOURCE_LOG_POS","SOURCE_PASSWORD","SOURCE_PORT","SOURCE_PUBLIC_KEY_PATH","SOURCE_RETRY_COUNT","SOURCE_SSL","SOURCE_SSL_CA","SOURCE_SSL_CAPATH","SOURCE_SSL_CERT","SOURCE_SSL_CIPHER","SOURCE_SSL_CRL","SOURCE_SSL_CRLPATH","SOURCE_SSL_KEY","SOURCE_SSL_VERIFY_SERVER_CERT","SOURCE_TLS_CIPHERSUITES","SOURCE_TLS_VERSION","SOURCE_USER","SOURCE_ZSTD_COMPRESSION_LEVEL","SPATIAL","SPECIFIC","SQL","SQLEXCEPTION","SQLSTATE","SQLWARNING","SQL_AFTER_GTIDS","SQL_AFTER_MTS_GAPS","SQL_BEFORE_GTIDS","SQL_BIG_RESULT","SQL_BUFFER_RESULT","SQL_CALC_FOUND_ROWS","SQL_NO_CACHE","SQL_SMALL_RESULT","SQL_THREAD","SQL_TSI_DAY","SQL_TSI_HOUR","SQL_TSI_MINUTE","SQL_TSI_MONTH","SQL_TSI_QUARTER","SQL_TSI_SECOND","SQL_TSI_WEEK","SQL_TSI_YEAR","SRID","SSL","STACKED","START","STARTING","STARTS","STATS_AUTO_RECALC","STATS_PERSISTENT","STATS_SAMPLE_PAGES","STATUS","STOP","STORAGE","STORED","STRAIGHT_JOIN","STREAM","STRING","SUBCLASS_ORIGIN","SUBJECT","SUBPARTITION","SUBPARTITIONS","SUPER","SUSPEND","SWAPS","SWITCHES","SYSTEM","TABLE","TABLES","TABLESPACE","TABLE_CHECKSUM","TABLE_NAME","TEMPORARY","TEMPTABLE","TERMINATED","TEXT","THAN","THEN","THREAD_PRIORITY","TIES","TIME","TIMESTAMP","TIMESTAMPADD","TIMESTAMPDIFF","TINYBLOB","TINYINT","TINYTEXT","TLS","TO","TRAILING","TRANSACTION","TRIGGER","TRIGGERS","TRUE","TRUNCATE","TYPE","TYPES","UNBOUNDED","UNCOMMITTED","UNDEFINED","UNDO","UNDOFILE","UNDO_BUFFER_SIZE","UNICODE","UNINSTALL","UNION","UNIQUE","UNKNOWN","UNLOCK","UNREGISTER","UNSIGNED","UNTIL","UPDATE","UPGRADE","URL","USAGE","USE","USER","USER_RESOURCES","USE_FRM","USING","UTC_DATE","UTC_TIME","UTC_TIMESTAMP","VALIDATION","VALUE","VALUES","VARBINARY","VARCHAR","VARCHARACTER","VARIABLES","VARYING","VCPU","VIEW","VIRTUAL","VISIBLE","WAIT","WARNINGS","WEEK","WEIGHT_STRING","WHEN","WHERE","WHILE","WINDOW","WITH","WITHOUT","WORK","WRAPPER","WRITE","X509","XA","XID","XML","XOR","YEAR","YEAR_MONTH","ZEROFILL","ZONE"];function textIsSystemKeyword(e){return!!e&&MYSQL_KEYWORDS.indexOf(e.toUpperCase())>-1}const entityList_vue_vue_type_style_index_0_scoped_9af4a99f_lang="",entityList_vue_vue_type_style_index_1_lang="",_withScopeId=e=>(pushScopeId("data-v-9af4a99f"),e=e(),popScopeId(),e),_hoisted_1={style:{float:"right"}},_hoisted_2=_withScopeId((()=>createBaseVNode("i",{class:"el-icon-plus"},null,-1))),_hoisted_3={class:"collapse-title"},_hoisted_4={class:"collapse-icon"},_hoisted_5={key:0,class:"system-flag system-entity"},_hoisted_6=_withScopeId((()=>createBaseVNode("i",{title:"系统实体"},"系",-1))),_hoisted_7=[_hoisted_6],_hoisted_8={key:1,class:"entity-flag main-entity"},_hoisted_9=_withScopeId((()=>createBaseVNode("i",{title:"主实体"},"主",-1))),_hoisted_10=[_hoisted_9],_hoisted_11={key:2,class:"entity-flag detail-entity"},_hoisted_12=_withScopeId((()=>createBaseVNode("i",{title:"明细实体"},"从",-1))),_hoisted_13=[_hoisted_12],_hoisted_14={key:3,class:"entity-flag detail-entity"},_hoisted_15=_withScopeId((()=>createBaseVNode("i",{title:"内部实体"},"内",-1))),_hoisted_16=[_hoisted_15],_hoisted_17={class:"collapse-title"},_hoisted_18={class:"collapse-icon"},_hoisted_19={key:0,class:"system-flag system-entity"},_hoisted_20=_withScopeId((()=>createBaseVNode("i",{title:"系统实体"},"系",-1))),_hoisted_21=[_hoisted_20],_hoisted_22={key:1,class:"entity-flag main-entity"},_hoisted_23=_withScopeId((()=>createBaseVNode("i",{title:"主实体"},"主",-1))),_hoisted_24=[_hoisted_23],_hoisted_25={key:2,class:"entity-flag detail-entity"},_hoisted_26=_withScopeId((()=>createBaseVNode("i",{title:"明细实体"},"从",-1))),_hoisted_27=[_hoisted_26],_hoisted_28={key:3,class:"entity-flag detail-entity"},_hoisted_29=_withScopeId((()=>createBaseVNode("i",{title:"内部实体"},"内",-1))),_hoisted_30=[_hoisted_29],_hoisted_31={class:"dialog-footer"},_hoisted_32=_withScopeId((()=>createBaseVNode("div",{class:"context-menu-divider"},null,-1))),_hoisted_33=_withScopeId((()=>createBaseVNode("div",{class:"context-menu-divider"},null,-1))),_sfc_main={__name:"entity-list",setup(__props){const{refreshCache:refreshCache}=useCommonStore(),{publicSetting:publicSetting}=storeToRefs(useCommonStore()),router=useRouter(),$TOOL=inject("$TOOL");let keyword=ref(""),loading=ref(!1),entityItems=ref(""),showNewEntityDialogFlag=ref(!1),newEntityProps=ref({name:"",label:"",entityCode:null,layoutable:!0,listable:!0,authorizable:!0,assignable:!1,shareable:!1,mainEntity:"",detailEntityFlag:!1,useTag:[]}),hoverEntityIdx=ref(-1),selectedEntityObj=ref({}),contextMenuVisible=ref(!1),hideMenuTimerId=ref(null),availableEntityList=ref([]),allTags=ref([]),notGroup=ref([]),entityGroup=ref({}),activeNames=ref(["未分组"]);onMounted((()=>{initEntity()}));const checkRole=e=>$TOOL.checkRole(e),initEntity=()=>{loading.value=!0,Promise.all([getEntityList(),getAllTags()]).then((()=>{loading.value=!1,filterEntityList()}))},getEntityList=()=>new Promise((async(e,t)=>{let E=await getEntitySet();E&&E.data&&(entityItems.value=E.data,refreshCache(E.data||[])),e()})),getAllTags=()=>new Promise((async(e,t)=>{let E=await getAllTagsOfEntity();E&&E.data&&(allTags.value=E.data,E.data.forEach((e=>{entityGroup.value[e]=[],activeNames.value.push(e)}))),e()})),filterEntityList=()=>{let e=entityItems.value.filter((e=>"ApprovalConfig"!==e.name&&"ReportConfig"!==e.name&&"TriggerConfig"!==e.name&&"MetaApi"!==e.name&&"Chart"!==e.name));keyword.value?availableEntityList.value=e.filter((e=>-1!=e.name.toLocaleUpperCase().indexOf(keyword.value.toLocaleUpperCase())||-1!=e.label.toLocaleUpperCase().indexOf(keyword.value.toLocaleUpperCase()))):availableEntityList.value=[...e];for(const t in entityGroup.value)entityGroup.value.hasOwnProperty.call(entityGroup.value,t)&&(entityGroup.value[t]=[]);notGroup.value=[],availableEntityList.value.forEach((e=>{e.newTags=e.tags?e.tags.split(","):[],e.newTags.length>0?e.newTags.forEach((t=>{entityGroup.value[t].push(e)})):notGroup.value.push(e)}))};let EPEditor=ref("");const createNewEntity=e=>{newEntityProps.value.name="",newEntityProps.value.label="",newEntityProps.value.entityCode=null,newEntityProps.value.layoutable=!0,newEntityProps.value.listable=!0,newEntityProps.value.authorizable=!0,newEntityProps.value.assignable=!1,newEntityProps.value.shareable=!1,newEntityProps.value.mainEntity="",newEntityProps.value.detailEntityFlag=!1,newEntityProps.value.useTag=allTags.value.map((e=>({name:e,checked:!1}))),showNewEntityDialogFlag.value=!0,"copy"==e&&(newEntityProps.value.activeType=2,nextTick((async()=>{EPEditor.value.loading=!0;let e=await getEntityProps(selectedEntityObj.value.name);if(e&&e.data){if(newEntityProps.value=Object.assign(newEntityProps.value,e.data),newEntityProps.value.tags){let e=newEntityProps.value.tags;newEntityProps.value.useTag.forEach((t=>{e.includes(t.name)&&(t.checked=!0)}))}newEntityProps.value.name="",newEntityProps.value.label="";let t=await hasDetailEntity(selectedEntityObj.value.name);newEntityProps.value.hasDetailEntity=(null==t?void 0:t.data)||!1}EPEditor.value.loading=!1})))},saveNewEntity=()=>{EPEditor.value.validateForm((async()=>{if(textIsSystemKeyword(newEntityProps.value.name))return void ElMessage.error("实体不能使用系统保留关键字，请修改。");newEntityProps.value=Object.assign(newEntityProps.value,EPEditor.value.entityProps);const mainEntityName=newEntityProps.value.mainEntity?newEntityProps.value.mainEntity:"null";EPEditor.value.loading=!0;let tags=[],res;if(newEntityProps.value.useTag&&newEntityProps.value.useTag.forEach((e=>{e.checked&&tags.push(e.name)})),newEntityProps.value.tags=tags.join(","),delete newEntityProps.value.useTag,2==newEntityProps.value.activeType){let params={sourceEntity:newEntityProps.value,mainEntityName:mainEntityName,operations:eval(EPEditor.value.copyEntiytSelectedType.join("+"))};res=await copyEntity(params)}else res=await createEntity(newEntityProps.value,mainEntityName);res&&200==res.code&&(ElMessage.success("保存成功"),showNewEntityDialogFlag.value=!1,initEntity()),EPEditor.value.loading=!1}))},onEnterEntity=(e,t,E)=>{hoverEntityIdx.value=E},onLeaveEntity=()=>{hoverEntityIdx.value=-1},gotoEntityManager=()=>{router.push({name:"FieldManager",query:{entity:selectedEntityObj.value.name,entityLabel:selectedEntityObj.value.label}})},gotoFormLayout=()=>{!0===selectedEntityObj.value.layoutable?router.push({name:"FormDesign",query:{entity:selectedEntityObj.value.name,entityLabel:selectedEntityObj.value.label}}):ElMessage.info("当前实体不允许设计表单")},gotoListView=()=>{selectedEntityObj.value.systemEntityFlag||selectedEntityObj.value.internalEntityFlag?ElMessage.info("当前实体不允许设计列表"):selectedEntityObj.value.detailEntityFlag?ElMessage.info("明细实体不允许设计列表"):router.push("/web/"+selectedEntityObj.value.name+"/list")},gotoRoute=(e,t)=>{selectedEntityObj.value.systemEntityFlag||selectedEntityObj.value.internalEntityFlag?ElMessage.info("当前实体不允许此操作"):t&&selectedEntityObj.value.detailEntityFlag?ElMessage.info("明细实体不允许此操作"):router.push("/web/"+e+"?entityCode="+selectedEntityObj.value.entityCode)},deleteSelectedEntity=()=>{publicSetting.value.trialVersionFlag?ElMessage.error("试用版本已禁用删除实体功能，敬请谅解"):entityCanBeDeleted(selectedEntityObj.value.name).then((e=>{if(null!=e.error)return void ElMessage({message:e.error,type:"error"});if(!0!==e.data)return void ElMessage.info("提示：系统实体、内部实体不能被删除！");let t=[];["实体删除后不能恢复，是否确认删除?","1. 删除实体会清空该实体的所有数据，包括实体所有字段、表单布局和列表设置，且不能恢复；’","2. 如该实体包含明细实体，请先删除明细实体；","3. 如有其他实体引用字段指向该实体，请手工删除引用字段；"].forEach((e=>{t.push(h("p",null,e))})),ElMessageBox.confirm("删除提醒",{message:h("div",null,t),showCancelButton:!0,type:"warning"}).then((()=>{ElMessageBox.prompt('请输入大写"MLC"',"再次确认删除",{confirmButtonText:"确定",cancelButtonText:"取消",inputPattern:/^MLC$/,inputErrorMessage:"输入不正确"}).then((({value:e})=>{"MLC"===e&&executeDeleteEntity(selectedEntityObj.value.name)})).catch((e=>{ElMessage({type:"info",message:"已取消删除"})}))})).catch((()=>{ElMessage({type:"info",message:"已取消删除"})}))})).catch((e=>{ElMessage({message:e.message,type:"error"})}))},executeDeleteEntity=e=>{deleteEntity(e).then((e=>{e&&e.data&&(ElMessage.success("实体已删除"),initEntity())})).catch((e=>{ElMessage({message:e.message,type:"error"})}))},filterMainEntity=(e,t)=>{e.length=0,e=entityItems.value.filter((t=>{t.systemEntityFlag||t.internalEntityFlag||!1===t.detailEntityFlag&&e.push({name:t.name,label:t.label})})),t()},hideContextMenu=e=>{contextMenuVisible.value=!1,clearHideMenuTimer()},contextMenuSetting=(e,t)=>{t.clientX>1800?e.style.left=t.clientX-100+"px":e.style.left=t.clientX+1+"px",t.clientY>700?e.style.top=t.clientY-70+"px":e.style.top=t.clientY-50+"px"},showContextMenu=(e,t)=>{clearHideMenuTimer(),contextMenuVisible.value=!1,contextMenuVisible.value=!0,selectedEntityObj.value={name:e.name,label:e.label,entityCode:e.entityCode,layoutable:e.layoutable,listable:e.listable,systemEntityFlag:e.systemEntityFlag,detailEntityFlag:e.detailEntityFlag};let E=document.querySelector("#contextMenu");contextMenuSetting(E,t),setHideMenuTimer()},setHideMenuTimer=()=>{hideMenuTimerId.value=setTimeout((()=>{hideContextMenu()}),3e3)},clearHideMenuTimer=()=>{clearTimeout(hideMenuTimerId.value)};return(e,t)=>{const E=resolveComponent("mlSearchInput"),o=resolveComponent("el-button"),a=resolveComponent("el-header"),i=resolveComponent("ElIconGrid"),n=resolveComponent("el-icon"),l=resolveComponent("el-card"),s=resolveComponent("el-collapse-item"),T=resolveComponent("el-collapse"),r=resolveComponent("el-dialog"),S=resolveComponent("el-main"),R=resolveComponent("el-container"),_=resolveDirective("loading");return openBlock(),createBlock(R,null,{default:withCtx((()=>[createVNode(a,{class:"entity-action-section"},{default:withCtx((()=>[createTextVNode(" 实体列表 "),createBaseVNode("div",_hoisted_1,[createVNode(E,{modelValue:unref(keyword),"onUpdate:modelValue":t[0]||(t[0]=e=>isRef(keyword)?keyword.value=e:keyword=e),placeholder:"查询",onConfirm:filterEntityList,onOnInput:filterEntityList,style:{"margin-right":"10px"}},null,8,["modelValue"]),createVNode(o,{type:"primary",onClick:createNewEntity},{default:withCtx((()=>[_hoisted_2,createTextVNode(" 新建实体 ")])),_:1})])])),_:1}),withDirectives((openBlock(),createBlock(S,{class:"card-container"},{default:withCtx((()=>[createVNode(T,{modelValue:unref(activeNames),"onUpdate:modelValue":t[1]||(t[1]=e=>isRef(activeNames)?activeNames.value=e:activeNames=e),class:"collapse-entity"},{default:withCtx((()=>[createVNode(s,{name:"未分组"},{title:withCtx((()=>[createBaseVNode("div",_hoisted_3,[createBaseVNode("span",_hoisted_4,[createVNode(n,null,{default:withCtx((()=>[createVNode(i)])),_:1})]),createTextVNode(" 未分组 ")])])),default:withCtx((()=>[(openBlock(!0),createElementBlock(Fragment,null,renderList(unref(notGroup),((e,t)=>(openBlock(),createBlock(l,{class:"entity-card",shadow:"hover",key:t,onClick:t=>showContextMenu(e,t),onContextmenu:withModifiers((t=>showContextMenu(e,t)),["prevent"]),onMouseenter:E=>onEnterEntity(e.name,e.label,t),onMouseleave:onLeaveEntity},{header:withCtx((()=>[createBaseVNode("div",null,[createBaseVNode("span",null,toDisplayString(e.label),1)])])),default:withCtx((()=>[createBaseVNode("div",null,toDisplayString(e.name),1),e.systemEntityFlag?(openBlock(),createElementBlock("div",_hoisted_5,_hoisted_7)):createCommentVNode("",!0),e.detailEntityFlag?createCommentVNode("",!0):(openBlock(),createElementBlock("div",_hoisted_8,_hoisted_10)),e.detailEntityFlag?(openBlock(),createElementBlock("div",_hoisted_11,_hoisted_13)):createCommentVNode("",!0),e.internalEntityFlag?(openBlock(),createElementBlock("div",_hoisted_14,_hoisted_16)):createCommentVNode("",!0)])),_:2},1032,["onClick","onContextmenu","onMouseenter"])))),128))])),_:1}),(openBlock(!0),createElementBlock(Fragment,null,renderList(unref(entityGroup),((e,t,E)=>(openBlock(),createElementBlock(Fragment,{key:E},[e.length>0?(openBlock(),createBlock(s,{key:0,name:t},{title:withCtx((()=>[createBaseVNode("div",_hoisted_17,[createBaseVNode("span",_hoisted_18,[createVNode(n,null,{default:withCtx((()=>[createVNode(i)])),_:1})]),createTextVNode(" "+toDisplayString(t),1)])])),default:withCtx((()=>[(openBlock(!0),createElementBlock(Fragment,null,renderList(e,((e,t)=>(openBlock(),createBlock(l,{class:"entity-card",shadow:"hover",key:t,onClick:t=>showContextMenu(e,t),onContextmenu:withModifiers((t=>showContextMenu(e,t)),["prevent"]),onMouseenter:E=>onEnterEntity(e.name,e.label,t),onMouseleave:onLeaveEntity},{header:withCtx((()=>[createBaseVNode("div",null,[createBaseVNode("span",null,toDisplayString(e.label),1)])])),default:withCtx((()=>[createBaseVNode("div",null,toDisplayString(e.name),1),e.systemEntityFlag?(openBlock(),createElementBlock("div",_hoisted_19,_hoisted_21)):createCommentVNode("",!0),e.detailEntityFlag?createCommentVNode("",!0):(openBlock(),createElementBlock("div",_hoisted_22,_hoisted_24)),e.detailEntityFlag?(openBlock(),createElementBlock("div",_hoisted_25,_hoisted_27)):createCommentVNode("",!0),e.internalEntityFlag?(openBlock(),createElementBlock("div",_hoisted_28,_hoisted_30)):createCommentVNode("",!0)])),_:2},1032,["onClick","onContextmenu","onMouseenter"])))),128))])),_:2},1032,["name"])):createCommentVNode("",!0)],64)))),128))])),_:1},8,["modelValue"]),unref(showNewEntityDialogFlag)?(openBlock(),createBlock(r,{key:0,title:2==unref(newEntityProps).activeType?"复制实体":"新建实体",modelValue:unref(showNewEntityDialogFlag),"onUpdate:modelValue":t[3]||(t[3]=e=>isRef(showNewEntityDialogFlag)?showNewEntityDialogFlag.value=e:showNewEntityDialogFlag=e),"show-close":!1,class:"new-entity-dialog","close-on-click-modal":!1,"close-on-press-escape":!1},{footer:withCtx((()=>{var e,E;return[createBaseVNode("div",_hoisted_31,[withDirectives((openBlock(),createBlock(o,{type:"primary",onClick:saveNewEntity,style:{width:"90px"}},{default:withCtx((()=>[createTextVNode("保 存")])),_:1})),[[_,null==(e=unref(EPEditor))?void 0:e.loading]]),withDirectives((openBlock(),createBlock(o,{onClick:t[2]||(t[2]=e=>isRef(showNewEntityDialogFlag)?showNewEntityDialogFlag.value=!1:showNewEntityDialogFlag=!1)},{default:withCtx((()=>[createTextVNode("取 消")])),_:1})),[[_,null==(E=unref(EPEditor))?void 0:E.loading]])])]})),default:withCtx((()=>[createVNode(EntityPropEditor,{ref_key:"EPEditor",ref:EPEditor,entityProps:unref(newEntityProps),"show-title":!1,"filter-entity-method":filterMainEntity},null,8,["entityProps"])])),_:1},8,["title","modelValue"])):createCommentVNode("",!0),withDirectives(createBaseVNode("div",{id:"contextMenu",class:"context-menu",onMouseenter:clearHideMenuTimer,onMouseleave:setHideMenuTimer},[checkRole("r6001")?(openBlock(),createElementBlock("div",{key:0,class:"context-menu__item",onClick:t[4]||(t[4]=e=>gotoEntityManager())},"字段管理")):createCommentVNode("",!0),checkRole("r6003")?(openBlock(),createElementBlock("div",{key:1,class:"context-menu__item",onClick:t[5]||(t[5]=e=>gotoFormLayout())},"表单设计")):createCommentVNode("",!0),checkRole("r6008")?(openBlock(),createElementBlock("div",{key:2,class:"context-menu__item",onClick:t[6]||(t[6]=e=>gotoListView())},"列表设计")):createCommentVNode("",!0),_hoisted_32,checkRole("r6016")?(openBlock(),createElementBlock("div",{key:3,class:"context-menu__item",onClick:t[7]||(t[7]=e=>gotoRoute("process-list",!0))},"流程设计")):createCommentVNode("",!0),checkRole("r6015")?(openBlock(),createElementBlock("div",{key:4,class:"context-menu__item",onClick:t[8]||(t[8]=e=>gotoRoute("trigger-list",!1))},"触发器设计")):createCommentVNode("",!0),checkRole("r45-2")?(openBlock(),createElementBlock("div",{key:5,class:"context-menu__item",onClick:t[9]||(t[9]=e=>gotoRoute("templates-list",!0))},"报表设计")):createCommentVNode("",!0),_hoisted_33,unref(selectedEntityObj).systemEntityFlag?createCommentVNode("",!0):(openBlock(),createElementBlock("div",{key:6,class:"context-menu__item",onClick:t[10]||(t[10]=e=>createNewEntity("copy"))},"复制实体")),checkRole("r6002")?(openBlock(),createElementBlock("div",{key:7,class:"context-menu__item",onClick:t[11]||(t[11]=e=>deleteSelectedEntity())},"删除实体")):createCommentVNode("",!0)],544),[[vShow,unref(contextMenuVisible)]])])),_:1})),[[_,unref(loading)]])])),_:1})}}},entityList=_export_sfc(_sfc_main,[["__scopeId","data-v-9af4a99f"]]);export{entityList as default};
