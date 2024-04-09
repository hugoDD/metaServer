-- --------------------------------------------------------
-- 主机:                           192.168.18.236
-- 服务器版本:                        5.7.16-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      linux-glibc2.5
-- HeidiSQL 版本:                  12.5.0.6677
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

ALTER DATABASE DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 导出  表 variantorm_base.t_approval_config 结构
CREATE TABLE IF NOT EXISTS `t_approval_config` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `approvalConfigId` char(40) NOT NULL,
  `approvalFlowId` char(40) DEFAULT NULL COMMENT '最新的流程定义id',
  `entityCode` mediumint(9) NOT NULL DEFAULT '0' COMMENT '实体code',
  `flowName` varchar(100) NOT NULL COMMENT '流程名称',
  `runningTotal` int(11) NOT NULL DEFAULT '0' COMMENT '运行中的流程统计',
  `completeTotal` int(11) NOT NULL DEFAULT '0' COMMENT '结束的流程统计',
  `isDisabled` tinyint(4) DEFAULT '0' COMMENT '是否禁用',
  `isDeleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  `createdBy` char(40) NOT NULL COMMENT '创建人',
  `createdOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifiedBy` char(40) DEFAULT NULL COMMENT '修改人',
  `modifiedOn` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`autoId`),
  UNIQUE KEY `approvalConfigId` (`approvalConfigId`) USING BTREE,
  KEY `entityCode` (`entityCode`),
  KEY `flowId` (`approvalFlowId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批流程总表' COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_approval_config 的数据：~0 rows (大约)

-- 导出  表 variantorm_base.t_approval_flow 结构
CREATE TABLE IF NOT EXISTS `t_approval_flow` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `approvalFlowId` char(40) NOT NULL COMMENT '流程定义id',
  `approvalConfigId` char(40) NOT NULL COMMENT '审批流程id',
  `flowDefinition` mediumtext COMMENT '流程定义json',
  `createdOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `createdBy` char(40) NOT NULL COMMENT '创建人',
  PRIMARY KEY (`autoId`),
  UNIQUE KEY `approvalFlowId` (`approvalFlowId`),
  KEY `approvalConfigId` (`approvalConfigId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批-流程定义' COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_approval_flow 的数据：~0 rows (大约)

-- 导出  表 variantorm_base.t_approval_history 结构
CREATE TABLE IF NOT EXISTS `t_approval_history` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `approvalHistoryId` char(40) NOT NULL COMMENT '流程历史id',
  `approvalTaskId` char(40) NOT NULL COMMENT '流程任务id',
  `approver` char(40) NOT NULL COMMENT '审批人',
  `stepName` char(40) NOT NULL COMMENT '步骤名称',
  `currentNode` smallint(10) NOT NULL DEFAULT '0' COMMENT '当前节点',
  `nodeType` smallint(2) NOT NULL DEFAULT '1' COMMENT '审核类型',
  `remark` varchar(600) DEFAULT NULL COMMENT '批注',
  `isBacked` tinyint(4) DEFAULT '0' COMMENT '是否驳回',
  `approvalOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审批时间',
  `attrMore` varchar(700) DEFAULT NULL COMMENT '扩展属性 (JSON Map)',
  `createdBy` char(40) NOT NULL COMMENT '创建人',
  `createdOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifiedBy` char(40) DEFAULT NULL COMMENT '修改人',
  `modifiedOn` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`autoId`),
  KEY `approvalTaskId` (`approvalTaskId`),
  KEY `approvalFlowId` (`approvalHistoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批-历史表' COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_approval_history 的数据：~0 rows (大约)

-- 导出  表 variantorm_base.t_approval_task 结构
CREATE TABLE IF NOT EXISTS `t_approval_task` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `approvalTaskId` char(40) NOT NULL COMMENT '流程任务id',
  `approvalFlowId` char(40) DEFAULT NULL COMMENT '流程定义id',
  `approvalConfigId` char(40) DEFAULT NULL COMMENT '流程配置id',
  `approvalStatus` tinyint(4) NOT NULL DEFAULT '0' COMMENT '审核状态',
  `entityId` char(40) NOT NULL COMMENT '实体Id',
  `currentNode` smallint(10) NOT NULL DEFAULT '0' COMMENT '当前节点',
  `signType` smallint(2) NOT NULL DEFAULT '2' COMMENT '1：会签 2：或签',
  `stepName` char(40) NOT NULL COMMENT '步骤名称',
  `approvalUser` char(40) NOT NULL COMMENT '审批人',
  `approvalOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审批时间',
  `remark` varchar(600) DEFAULT NULL COMMENT '批注',
  `attrMore` json DEFAULT NULL COMMENT '扩展属性 (JSON Map)',
  `createdBy` char(40) NOT NULL COMMENT '创建人',
  `createdOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifiedBy` char(40) DEFAULT NULL COMMENT '修改人',
  `modifiedOn` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`autoId`),
  UNIQUE KEY `approvalTaskId` (`approvalTaskId`),
  KEY `approvalFlowId` (`approvalFlowId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批-任务表' COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_approval_task 的数据：~0 rows (大约)

-- 导出  表 variantorm_base.t_chart 结构
CREATE TABLE IF NOT EXISTS `t_chart` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `chartId` char(40) NOT NULL,
  `chartName` varchar(200) DEFAULT NULL,
  `chartData` json DEFAULT NULL,
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `ownerUser` char(40) NOT NULL,
  `ownerDepartment` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  `defaultChart` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `chartId` (`chartId`) USING BTREE,
  KEY `ownerUser` (`ownerUser`) USING BTREE,
  KEY `ownerDepartment` (`ownerDepartment`) USING BTREE,
  KEY `isDeleted` (`isDeleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_chart 的数据：~0 rows (大约)

-- 导出  表 variantorm_base.t_data_list_view 结构
CREATE TABLE IF NOT EXISTS `t_data_list_view` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `dataListViewId` char(40) NOT NULL,
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `entityCode` int(11) DEFAULT NULL,
  `viewName` varchar(190) DEFAULT NULL,
  `headerJson` text,
  `filterJson` text,
  `paginationJson` text,
  `sortJson` text,
  `presetFilter` text,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `dataListViewId` (`dataListViewId`) USING BTREE,
  UNIQUE KEY `entityCode_viewName` (`entityCode`,`viewName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_data_list_view 的数据：~0 rows (大约)

-- 导出  表 variantorm_base.t_department 结构
CREATE TABLE IF NOT EXISTS `t_department` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `departmentId` char(40) NOT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  `parentDepartmentId` char(40) DEFAULT NULL,
  `departmentName` varchar(190) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `departmentId` (`departmentId`) USING BTREE,
  KEY `parentDepartmentId` (`parentDepartmentId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_department 的数据：~1 rows (大约)
INSERT INTO `t_department` (`autoId`, `departmentId`, `isDeleted`, `parentDepartmentId`, `departmentName`, `description`) VALUES
	(1, '0000022-00000000000000000000000000000001', 0, NULL, '公司总部', NULL);

-- 导出  表 variantorm_base.t_department_node 结构
CREATE TABLE IF NOT EXISTS `t_department_node` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `departmentNodeId` char(40) NOT NULL,
  `parentDepartmentId` char(40) DEFAULT NULL,
  `childDepartmentId` char(40) DEFAULT NULL,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `departmentNodeId` (`departmentNodeId`) USING BTREE,
  UNIQUE KEY `parentDepartmentId_childDepartmentId` (`parentDepartmentId`,`childDepartmentId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_department_node 的数据：~1 rows (大约)
INSERT INTO `t_department_node` (`autoId`, `departmentNodeId`, `parentDepartmentId`, `childDepartmentId`) VALUES
	(1, '0000011-873dcc767b3d11ebb21e1cbfc037aa76', '0000022-00000000000000000000000000000001', '0000022-00000000000000000000000000000001');

-- 导出  表 variantorm_base.t_form_layout 结构
CREATE TABLE IF NOT EXISTS `t_form_layout` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `formLayoutId` char(40) NOT NULL,
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `layoutName` varchar(190) DEFAULT NULL,
  `entityCode` int(11) DEFAULT NULL,
  `layoutJson` text,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `formLayoutId` (`formLayoutId`) USING BTREE,
  UNIQUE KEY `layoutName_entityCode` (`layoutName`,`entityCode`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_form_layout 的数据：~8 rows (大约)
INSERT INTO `t_form_layout` (`autoId`, `formLayoutId`, `createdOn`, `createdBy`, `modifiedOn`, `modifiedBy`, `layoutName`, `entityCode`, `layoutJson`) VALUES
	(1, '0000008-d50a2eab9d2abcd34cc8931a2c6a3d49', '2020-11-06 14:35:24', '0000021-00000000000000000000000000000001', '2021-02-14 15:38:19', '0000021-00000000000000000000000000000001', '默认表单布局0', 21, '{"tabNames":["tabName4132"],"formTabs":[{"title":"用户信息","id":"tab7923","sections":[{"title":"基本信息","name":"sec1","id":"section11195","rows":[{"title":"新增行","id":"row4176","gutter":12,"cells":[{"id":"cell10740","span":12,"fields":[{"reserved":false,"name":"userName","label":"姓名","type":"Text","labelWidth":null,"unDraggable":true}]},{"id":"cell7799","span":12,"fields":[{"reserved":false,"name":"departmentId","label":"用户归属部门","type":"Reference","unDraggable":true,"labelWidth":85}]}]},{"title":"新增行","id":"row6949","gutter":12,"cells":[{"id":"cell9413","span":12,"fields":[{"reserved":false,"name":"loginName","label":"登录账号名","type":"Text","unDraggable":true,"labelWidth":null}]},{"id":"cell2869","span":12,"fields":[{"reserved":false,"name":"loginPwd","label":"登录密码","type":"Password","labelWidth":85,"unDraggable":true}]}]},{"title":"新增行","id":"row7545","gutter":12,"cells":[{"id":"cell6412","span":12,"fields":[{"reserved":false,"name":"jobTitle","label":"职务","type":"Option","unDraggable":true}]},{"id":"cell10398","span":12,"fields":[{"reserved":false,"name":"disabled","label":"是否禁用","type":"Boolean","unDraggable":true}]}]},{"title":"新增行","id":"row4506","gutter":12,"cells":[{"id":"cell14633","span":24,"fields":[{"reserved":false,"name":"roles","label":"权限角色","type":"ReferenceList","unDraggable":true}]}]}],"showSplitter":false,"showSectionTitle":true,"showArrowIcon":true,"openSplitter":false},{"title":"管理信息","name":"sec1","id":"section3382","rows":[{"title":"新增行","id":"row4744","gutter":12,"cells":[{"id":"cell9586","span":12,"fields":[{"reserved":true,"name":"createdBy","label":"创建用户","type":"Reference","unDraggable":true,"labelWidth":null}]},{"id":"cell11368","span":12,"fields":[{"reserved":true,"name":"createdOn","label":"创建时间","type":"DateTime","unDraggable":true,"labelWidth":85}]}]},{"title":"新增行","id":"row10535","gutter":12,"cells":[{"id":"cell9847","span":12,"fields":[{"reserved":true,"name":"modifiedBy","label":"修改用户","type":"Reference","unDraggable":true,"labelWidth":null}]},{"id":"cell14703","span":12,"fields":[{"reserved":true,"name":"modifiedOn","label":"最近修改时间","type":"DateTime","unDraggable":true,"labelWidth":85}]}]},{"title":"新增行","id":"row13678","gutter":12,"cells":[{"id":"cell12759","span":12,"fields":[{"reserved":true,"name":"ownerUser","label":"所属用户","type":"Reference","labelWidth":null,"unDraggable":true}]},{"id":"cell2351","span":12,"fields":[{"reserved":true,"name":"ownerDepartment","label":"所属部门","type":"Reference","labelWidth":85,"unDraggable":true}]}]}],"lineSpacing":10}],"name":"tabName4132"}],"labelPosition":"left","labelAlign":"label-right-align","labelWidth":75,"lineSpacing":12,"hideOnlyTabTitle":true}'),
	(2, '0000008-8c871f6ea2d5abcd4d0ea351cc87b5ca', '2020-11-11 16:42:31', '0000021-00000000000000000000000000000001', '2021-02-24 23:29:24', '0000021-00000000000000000000000000000001', '默认表单布局01', 22, '{"tabNames":["tabName4664"],"formTabs":[{"title":"页签1","id":"tab9548","sections":[{"title":"新增区块","name":"sec1","id":"section11203","rows":[{"title":"新增行","id":"row4808","gutter":12,"cells":[{"id":"cell7962","span":12,"fields":[{"reserved":false,"name":"parentDepartmentId","label":"上级部门","type":"Reference","unDraggable":true,"labelWidth":null}]},{"id":"cell3340","span":12,"fields":[{"reserved":false,"name":"departmentName","label":"部门名称","type":"Text","unDraggable":true,"labelWidth":null}]}]},{"title":"新增行","id":"row1834","gutter":12,"cells":[{"id":"cell7530","span":24,"fields":[{"reserved":false,"name":"description","label":"部门说明","type":"TextArea","unDraggable":true}]}]}],"showSectionTitle":false,"showArrowIcon":false,"showSplitter":false}],"name":"tabName4664"}],"labelPosition":"left","labelAlign":"label-left-align","labelWidth":75,"lineSpacing":12,"hideOnlyTabTitle":true}'),
	(3, '0000008-f42c4cecc9abcd1b4e338ae9e06286a7', '2020-11-11 16:44:18', '0000021-00000000000000000000000000000001', NULL, NULL, '默认表单布局0', 22, '{"tabNames":["tabName4664"],"formTabs":[{"title":"页签1","id":"tab9548","sections":[{"title":"新增区块","name":"sec1","id":"section11203","rows":[{"title":"新增行","id":"row9965","gutter":12,"cells":[{"id":"cell4508","span":6,"fields":[]},{"id":"cell1916","span":12,"fields":[]}]}]}],"name":"tabName4664"}],"activeTab":{"title":"页签1","id":"tab9548","sections":[{"title":"新增区块","name":"sec1","id":"section11203","rows":[{"title":"新增行","id":"row9965","gutter":12,"cells":[{"id":"cell4508","span":6,"fields":[]},{"id":"cell1916","span":12,"fields":[]}]}]}],"name":"tabName4664"},"activeTabName":"tabName4664","activeSection":null,"activeRow":{"title":"新增行","id":"row6026","gutter":12,"cells":[{"id":"cell10795","span":6,"fields":[{"reserved":false,"name":"departmentName","label":"部门名称","type":"Text","labelWidth":75,"unDraggable":true}]},{"id":"cell5003","span":12,"fields":[{"reserved":false,"name":"parentDepartmentId","label":"上级部门","type":"Reference","labelWidth":75,"unDraggable":true}]}]},"activeCell":null,"activeFieldWrapper":null,"activeFieldLabelWidth":0,"labelAlign":"label-left-align","labelPosition":"left","hideOnlyTabTitle":false}'),
	(10, '0000008-5e869540cd5542518bc5238d21936d9c', '2023-10-10 15:35:19', '0000021-00000000000000000000000000000001', '2023-10-10 15:37:03', '0000021-00000000000000000000000000000001', '默认表单布局', 24, '{"widgetList":[{"key":84597,"type":"grid","alias":"column-2-grid","category":"container","icon":"column-2-grid","cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"input","alias":"","icon":"text-field","formItemFlag":true,"options":{"name":"teamName","keyNameEnabled":false,"keyName":"","label":"用户名称","labelAlign":"","type":"text","defaultValue":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"readonly":false,"disabled":false,"hidden":false,"clearable":true,"showPassword":false,"required":true,"requiredHint":"","validation":"","validationHint":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"minLength":null,"maxLength":null,"showWordLimit":false,"prefixIcon":"","suffixIcon":"","appendButton":false,"appendButtonDisabled":false,"buttonIcon":"custom-search","onCreated":"","onMounted":"","onInput":"","onChange":"","onFocus":"","onBlur":"","onValidate":"","onAppendButtonClick":""},"nameReadonly":true,"id":"input92535"}],"options":{"name":"gridCol58149","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":[]},"id":"grid-col-58149"},{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"reference","alias":"","icon":"reference-field","formItemFlag":true,"options":{"name":"principalId","keyNameEnabled":false,"keyName":"","label":"负责人","labelAlign":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"disabled":false,"hidden":false,"required":true,"requiredHint":"","validation":"","validationHint":"","newTest":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"prefixIcon":"","suffixIcon":"","buttonIcon":"Search","onCreated":"","onMounted":"","onChange":"","onValidate":""},"nameReadonly":true,"id":"reference41242"}],"options":{"name":"gridCol99681","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":""},"id":"grid-col-99681"}],"options":{"name":"grid77267","hidden":false,"gutter":12,"colHeight":null,"customClass":[]},"id":"grid77267"},{"key":53944,"type":"grid","alias":"column-2-grid","category":"container","icon":"column-2-grid","cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"reference","alias":"","icon":"reference-field","formItemFlag":true,"options":{"name":"ownerDepartment","keyNameEnabled":false,"keyName":"","label":"所属部门","labelAlign":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"disabled":false,"hidden":false,"required":true,"requiredHint":"","validation":"","validationHint":"","newTest":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"prefixIcon":"","suffixIcon":"","buttonIcon":"Search","onCreated":"","onMounted":"","onChange":"","onValidate":""},"nameReadonly":true,"id":"reference37493"}],"options":{"name":"gridCol37832","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":""},"id":"grid-col-37832"},{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[],"options":{"name":"gridCol103174","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":""},"id":"grid-col-103174"}],"options":{"name":"grid111078","hidden":false,"gutter":12,"colHeight":null,"customClass":[]},"id":"grid111078"},{"type":"radio","icon":"radio-field","formItemFlag":true,"options":{"name":"disabled","keyNameEnabled":false,"keyName":"","label":"是否禁用","labelAlign":"","defaultValue":null,"columnWidth":"200px","size":"","displayStyle":"inline","buttonStyle":false,"border":false,"labelWidth":null,"labelHidden":false,"labelWrap":false,"disabled":false,"hidden":false,"dsEnabled":false,"dsName":"","dataSetName":"","labelKey":"label","valueKey":"value","optionValueType":"","optionItems":[{"value":1,"label":"是","displayOrder":1},{"value":0,"label":"否","displayOrder":2},{"value":-1,"label":"未指定","displayOrder":3}],"required":true,"requiredHint":"","validation":"","validationHint":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"onCreated":"","onMounted":"","onChange":"","onValidate":""},"nameReadonly":true,"optionItemsReadonly":true,"id":"radio61214"}],"formConfig":{"modelName":"formData","refName":"vForm","rulesName":"rules","labelWidth":80,"labelPosition":"left","size":"","labelAlign":"label-left-align","cssCode":"","customClass":[],"functions":"","layoutType":"PC","jsonVersion":3,"dataSources":[],"onFormCreated":"","onFormMounted":"","onFormDataChange":"","onFormValidate":""}}'),
	(11, '0000008-8c49a51e848f4421b24d86ed29f6db10', '2023-10-12 13:32:22', '0000021-00000000000000000000000000000001', '2023-10-17 11:35:36', '0000021-00000000000000000000000000000001', '默认表单布局', 21, '{"widgetList":[{"key":46819,"type":"card","category":"container","icon":"card","widgetList":[{"key":47123,"type":"grid","alias":"column-2-grid","category":"container","icon":"column-2-grid","cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"input","alias":"","icon":"text-field","formItemFlag":true,"options":{"name":"userName","keyNameEnabled":false,"keyName":"","label":"用户名称","labelAlign":"","type":"text","defaultValue":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"readonly":false,"disabled":false,"hidden":false,"clearable":true,"showPassword":false,"required":true,"requiredHint":"","validation":"","validationHint":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"minLength":null,"maxLength":null,"showWordLimit":false,"prefixIcon":"","suffixIcon":"","appendButton":false,"appendButtonDisabled":false,"buttonIcon":"custom-search","onCreated":"","onMounted":"","onInput":"","onChange":"","onFocus":"","onBlur":"","onValidate":"","onAppendButtonClick":""},"nameReadonly":true,"id":"input79235"}],"options":{"name":"gridCol106875","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":""},"id":"grid-col-106875"},{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"reference","alias":"","icon":"reference-field","formItemFlag":true,"options":{"name":"departmentId","keyNameEnabled":false,"keyName":"","label":"部门","labelAlign":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"disabled":false,"hidden":false,"required":true,"requiredHint":"","validation":"","validationHint":"","newTest":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"prefixIcon":"","suffixIcon":"","buttonIcon":"Search","onCreated":"","onMounted":"","onChange":"","onValidate":""},"nameReadonly":true,"id":"reference45747"}],"options":{"name":"gridCol75819","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":[]},"id":"grid-col-75819"}],"options":{"name":"grid29062","hidden":false,"gutter":12,"colHeight":null,"customClass":""},"id":"grid29062"},{"type":"grid","alias":"column-4-grid","category":"container","icon":"column-4-grid","commonFlag":true,"cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"select","icon":"select-field","formItemFlag":true,"options":{"name":"jobTitle","keyNameEnabled":false,"keyName":"","label":"职务","labelAlign":"","defaultValue":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"disabled":false,"hidden":false,"clearable":true,"filterable":false,"allowCreate":false,"remote":false,"automaticDropdown":false,"multiple":false,"multipleLimit":0,"collapseTags":false,"dsEnabled":false,"dsName":"","dataSetName":"","labelKey":"label","valueKey":"value","optionValueType":"","optionItems":[{"value":4,"label":"总监","displayOrder":1},{"value":2,"label":"主管","displayOrder":2},{"value":3,"label":"经理","displayOrder":3},{"value":5,"label":"部长","displayOrder":4},{"value":1,"label":"员工","displayOrder":5},{"value":6,"label":"劳动人民","displayOrder":6}],"required":true,"requiredHint":"","validation":"","validationHint":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"onCreated":"","onMounted":"","onRemoteQuery":"","onChange":"","onFocus":"","onBlur":"","onValidate":""},"nameReadonly":true,"optionItemsReadonly":true,"id":"select9932"}],"options":{"name":"gridCol83947","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":[]},"id":"grid-col-83947"},{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"radio","icon":"radio-field","formItemFlag":true,"options":{"name":"disabled","keyNameEnabled":false,"keyName":"","label":"是否禁用","labelAlign":"","defaultValue":null,"columnWidth":"200px","size":"","displayStyle":"inline","buttonStyle":false,"border":false,"labelWidth":null,"labelHidden":false,"labelWrap":false,"disabled":false,"hidden":false,"dsEnabled":false,"dsName":"","dataSetName":"","labelKey":"label","valueKey":"value","optionValueType":"","optionItems":[{"value":true,"label":"是"},{"value":false,"label":"否"},{"value":null,"label":"未指定"}],"required":true,"requiredHint":"","validation":"","validationHint":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"onCreated":"","onMounted":"","onChange":"","onValidate":""},"nameReadonly":true,"optionItemsReadonly":true,"id":"radio62949"}],"options":{"name":"gridCol90266","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":""},"id":"grid-col-90266"}],"options":{"name":"grid46372","hidden":false,"gutter":12,"colHeight":null,"customClass":""},"id":"grid46372"},{"type":"grid","alias":"column-4-grid","category":"container","icon":"column-4-grid","commonFlag":true,"cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"input","alias":"","icon":"text-field","formItemFlag":true,"options":{"name":"mobilePhone","keyNameEnabled":false,"keyName":"","label":"手机号","labelAlign":"","type":"text","defaultValue":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"readonly":false,"disabled":false,"hidden":false,"clearable":true,"showPassword":false,"required":false,"requiredHint":"","validation":"","validationHint":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"minLength":null,"maxLength":null,"showWordLimit":false,"prefixIcon":"","suffixIcon":"","appendButton":false,"appendButtonDisabled":false,"buttonIcon":"custom-search","onCreated":"","onMounted":"","onInput":"","onChange":"","onFocus":"","onBlur":"","onValidate":"","onAppendButtonClick":""},"nameReadonly":true,"id":"input83069"}],"options":{"name":"gridCol52778","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":[]},"id":"grid-col-52778"},{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"input","alias":"","icon":"text-field","formItemFlag":true,"options":{"name":"email","keyNameEnabled":false,"keyName":"","label":"邮箱","labelAlign":"","type":"text","defaultValue":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"readonly":false,"disabled":false,"hidden":false,"clearable":true,"showPassword":false,"required":false,"requiredHint":"","validation":"","validationHint":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"minLength":null,"maxLength":null,"showWordLimit":false,"prefixIcon":"","suffixIcon":"","appendButton":false,"appendButtonDisabled":false,"buttonIcon":"custom-search","onCreated":"","onMounted":"","onInput":"","onChange":"","onFocus":"","onBlur":"","onValidate":"","onAppendButtonClick":""},"nameReadonly":true,"id":"input59625"}],"options":{"name":"gridCol75629","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":[]},"id":"grid-col-75629"}],"options":{"name":"grid16200","hidden":false,"gutter":12,"colHeight":null,"customClass":""},"id":"grid16200"},{"type":"grid","alias":"column-4-grid","category":"container","icon":"column-4-grid","commonFlag":true,"cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"input","alias":"","icon":"text-field","formItemFlag":true,"options":{"name":"loginName","keyNameEnabled":false,"keyName":"","label":"登录账号名","labelAlign":"","type":"text","defaultValue":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"readonly":false,"disabled":false,"hidden":false,"clearable":true,"showPassword":false,"required":true,"requiredHint":"","validation":"","validationHint":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"minLength":null,"maxLength":null,"showWordLimit":false,"prefixIcon":"","suffixIcon":"","appendButton":false,"appendButtonDisabled":false,"buttonIcon":"custom-search","onCreated":"","onMounted":"","onInput":"","onChange":"","onFocus":"","onBlur":"","onValidate":"","onAppendButtonClick":""},"nameReadonly":true,"id":"input16244"}],"options":{"name":"gridCol46331","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":""},"id":"grid-col-46331"},{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"input","alias":"","icon":"text-field","formItemFlag":true,"options":{"name":"loginPwd","keyNameEnabled":false,"keyName":"","label":"登录密码","labelAlign":"","type":"password","defaultValue":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"readonly":false,"disabled":false,"hidden":false,"clearable":true,"showPassword":true,"required":true,"requiredHint":"","validation":"","validationHint":"","customClass":"","labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"minLength":null,"maxLength":null,"showWordLimit":false,"prefixIcon":"","suffixIcon":"","appendButton":false,"appendButtonDisabled":false,"buttonIcon":"custom-search","onCreated":"","onMounted":"","onInput":"","onChange":"","onFocus":"","onBlur":"","onValidate":"","onAppendButtonClick":""},"nameReadonly":true,"id":"input59749"}],"options":{"name":"gridCol46446","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":[]},"id":"grid-col-46446"}],"options":{"name":"grid20245","hidden":false,"gutter":12,"colHeight":null,"customClass":[]},"id":"grid20245"},{"key":64151,"type":"grid","alias":"column-1-grid","category":"container","icon":"column-1-grid","cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"picture-upload","icon":"picture-upload-field","formItemFlag":true,"options":{"name":"avatar","keyNameEnabled":false,"keyName":"","label":"头像","labelAlign":"","labelWidth":null,"labelHidden":false,"columnWidth":"200px","disabled":false,"hidden":false,"required":false,"requiredHint":"","customRule":"","customRuleHint":"","uploadURL":"DSV[\'uploadServer\'] + \'/picture/upload\'","uploadTip":"","withCredentials":true,"multipleSelect":false,"showFileList":true,"limit":3,"fileMaxSize":5,"fileTypes":["jpg","jpeg","png"],"customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"onCreated":"","onMounted":"","onBeforeUpload":"","onUploadSuccess":"","onUploadError":"","onFileRemove":"","onValidate":""},"nameReadonly":true,"id":"pictureupload68035"}],"options":{"name":"gridCol109461","hidden":false,"span":24,"offset":0,"push":0,"pull":0,"responsive":false,"md":24,"sm":24,"xs":24,"customClass":""},"id":"grid-col-109461"}],"options":{"name":"grid57423","hidden":false,"gutter":12,"colHeight":null,"customClass":""},"id":"grid57423"}],"options":{"name":"card23310","label":"基本信息","hidden":false,"folded":false,"showFold":true,"cardWidth":"100%","shadow":"never","customClass":[]},"id":"card23310"}],"formConfig":{"modelName":"formData","refName":"vForm","rulesName":"rules","labelWidth":80,"labelPosition":"left","size":"","labelAlign":"label-right-align","cssCode":"","customClass":[],"functions":"","layoutType":"PC","jsonVersion":3,"dataSources":[],"onFormCreated":"","onFormMounted":"","onFormDataChange":"","onFormValidate":""}}'),
	(13, '0000008-e427812a303a44e081cf7150148fc791', '2023-10-12 14:16:05', '0000021-00000000000000000000000000000001', '2023-10-12 14:16:05', '0000021-00000000000000000000000000000001', '默认表单布局', 22, '{"widgetList":[{"key":40463,"type":"grid","alias":"column-2-grid","category":"container","icon":"column-2-grid","cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"input","alias":"","icon":"text-field","formItemFlag":true,"options":{"name":"departmentName","keyNameEnabled":false,"keyName":"","label":"部门名称","labelAlign":"","type":"text","defaultValue":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"readonly":false,"disabled":false,"hidden":false,"clearable":true,"showPassword":false,"required":true,"requiredHint":"","validation":"","validationHint":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"minLength":null,"maxLength":null,"showWordLimit":false,"prefixIcon":"","suffixIcon":"","appendButton":false,"appendButtonDisabled":false,"buttonIcon":"custom-search","onCreated":"","onMounted":"","onInput":"","onChange":"","onFocus":"","onBlur":"","onValidate":"","onAppendButtonClick":""},"nameReadonly":true,"id":"input99603"}],"options":{"name":"gridCol94789","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":""},"id":"grid-col-94789"},{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"reference","alias":"","icon":"reference-field","formItemFlag":true,"options":{"name":"parentDepartmentId","keyNameEnabled":false,"keyName":"","label":"上级部门","labelAlign":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"disabled":false,"hidden":false,"required":true,"requiredHint":"","validation":"","validationHint":"","newTest":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"prefixIcon":"","suffixIcon":"","buttonIcon":"Search","onCreated":"","onMounted":"","onChange":"","onValidate":""},"nameReadonly":true,"id":"reference28286"}],"options":{"name":"gridCol20275","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":[]},"id":"grid-col-20275"}],"options":{"name":"grid91283","hidden":false,"gutter":12,"colHeight":null,"customClass":[]},"id":"grid91283"},{"type":"textarea","icon":"textarea-field","formItemFlag":true,"options":{"name":"description","keyNameEnabled":false,"keyName":"","label":"部门说明","labelAlign":"","rows":3,"autosize":false,"defaultValue":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"readonly":false,"disabled":false,"hidden":false,"required":false,"requiredHint":"","validation":"","validationHint":"","customClass":"","labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"minLength":null,"maxLength":null,"showWordLimit":false,"onCreated":"","onMounted":"","onInput":"","onChange":"","onFocus":"","onBlur":"","onValidate":""},"nameReadonly":true,"id":"textarea89661"}],"formConfig":{"modelName":"formData","refName":"vForm","rulesName":"rules","labelWidth":80,"labelPosition":"left","size":"","labelAlign":"label-left-align","cssCode":"","customClass":[],"functions":"","layoutType":"PC","jsonVersion":3,"dataSources":[],"onFormCreated":"","onFormMounted":"","onFormDataChange":"","onFormValidate":""}}'),
	(18, '0000008-209e66e953694bd498e977c2e0d9cdfc', '2023-11-01 11:22:38', '0000021-00000000000000000000000000000001', '2023-11-01 11:56:52', '0000021-00000000000000000000000000000001', '默认表单布局', 54, '{"widgetList":[{"key":70498,"type":"grid","alias":"column-1-grid","category":"container","icon":"column-1-grid","cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"select","icon":"select-field","formItemFlag":true,"options":{"name":"wayType","keyNameEnabled":false,"keyName":"","label":"跟进方式","labelAlign":"","defaultValue":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"disabled":false,"hidden":false,"clearable":true,"filterable":false,"allowCreate":false,"remote":false,"automaticDropdown":false,"multiple":false,"multipleLimit":0,"collapseTags":false,"dsEnabled":false,"dsName":"","dataSetName":"","labelKey":"label","valueKey":"value","optionValueType":"","optionItems":[{"value":1,"label":"微信","displayOrder":1},{"value":2,"label":"电话","displayOrder":2},{"value":3,"label":"面对面","displayOrder":3},{"value":4,"label":"邮件","displayOrder":4}],"required":true,"requiredHint":"","validation":"","validationHint":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"onCreated":"","onMounted":"","onRemoteQuery":"","onChange":"","onFocus":"","onBlur":"","onValidate":""},"nameReadonly":true,"optionItemsReadonly":true,"id":"select72488"}],"options":{"name":"gridCol50560","hidden":false,"span":24,"offset":0,"push":0,"pull":0,"responsive":false,"md":24,"sm":24,"xs":24,"customClass":[]},"id":"grid-col-50560"}],"options":{"name":"grid87557","hidden":false,"gutter":12,"colHeight":null,"customClass":[]},"id":"grid87557"},{"key":70498,"type":"grid","alias":"column-1-grid","category":"container","icon":"column-1-grid","cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"textarea","icon":"textarea-field","formItemFlag":true,"options":{"name":"content","keyNameEnabled":false,"keyName":"","label":"跟进内容","labelAlign":"","rows":3,"autosize":false,"defaultValue":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"readonly":false,"disabled":false,"hidden":false,"required":true,"requiredHint":"","validation":"","validationHint":"","customClass":"","labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"minLength":null,"maxLength":null,"showWordLimit":false,"onCreated":"","onMounted":"","onInput":"","onChange":"","onFocus":"","onBlur":"","onValidate":""},"nameReadonly":true,"id":"textarea69929"}],"options":{"name":"gridCol75210","hidden":false,"span":24,"offset":0,"push":0,"pull":0,"responsive":false,"md":24,"sm":24,"xs":24,"customClass":""},"id":"grid-col-75210"}],"options":{"name":"grid102744","hidden":false,"gutter":12,"colHeight":null,"customClass":[]},"id":"grid102744"},{"key":70498,"type":"grid","alias":"column-1-grid","category":"container","icon":"column-1-grid","cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"picture-upload","icon":"picture-upload-field","formItemFlag":true,"options":{"name":"picture","keyNameEnabled":false,"keyName":"","label":"图片","labelAlign":"","labelWidth":null,"labelHidden":false,"columnWidth":"200px","disabled":false,"hidden":false,"required":false,"requiredHint":"","customRule":"","customRuleHint":"","uploadURL":"DSV[\'uploadServer\'] + \'/picture/upload\'","uploadTip":"","withCredentials":true,"multipleSelect":false,"showFileList":true,"limit":3,"fileMaxSize":5,"fileTypes":["jpg","jpeg","png"],"customClass":"","labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"onCreated":"","onMounted":"","onBeforeUpload":"","onUploadSuccess":"","onUploadError":"","onFileRemove":"","onValidate":""},"nameReadonly":true,"id":"pictureupload101125"}],"options":{"name":"gridCol96252","hidden":false,"span":24,"offset":0,"push":0,"pull":0,"responsive":false,"md":24,"sm":24,"xs":24,"customClass":""},"id":"grid-col-96252"}],"options":{"name":"grid62378","hidden":false,"gutter":12,"colHeight":null,"customClass":""},"id":"grid62378"},{"key":70498,"type":"grid","alias":"column-1-grid","category":"container","icon":"column-1-grid","cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"file-upload","icon":"file-upload-field","formItemFlag":true,"options":{"name":"attachment","keyNameEnabled":false,"keyName":"","label":"附件上传","labelAlign":"","labelWidth":null,"labelHidden":false,"columnWidth":"200px","disabled":false,"hidden":false,"required":false,"requiredHint":"","customRule":"","customRuleHint":"","uploadURL":"DSV[\'uploadServer\'] + \'/file/upload\'","uploadTip":"","withCredentials":true,"multipleSelect":false,"showFileList":true,"limit":3,"fileMaxSize":5,"fileTypes":["doc","docx","xls","xlsx"],"customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"onCreated":"","onMounted":"","onBeforeUpload":"","onUploadSuccess":"","onUploadError":"","onFileRemove":"","onValidate":""},"nameReadonly":true,"id":"fileupload85634"}],"options":{"name":"gridCol83930","hidden":false,"span":24,"offset":0,"push":0,"pull":0,"responsive":false,"md":24,"sm":24,"xs":24,"customClass":""},"id":"grid-col-83930"}],"options":{"name":"grid86828","hidden":false,"gutter":12,"colHeight":null,"customClass":[]},"id":"grid86828"}],"formConfig":{"modelName":"formData","refName":"vForm","rulesName":"rules","labelWidth":80,"labelPosition":"left","size":"","labelAlign":"label-left-align","cssCode":"","customClass":[],"functions":"","layoutType":"PC","jsonVersion":3,"dataSources":[],"onFormCreated":"","onFormMounted":"","onFormDataChange":"","onFormValidate":""}}'),
	(19, '0000008-74c86b125cd146c3b6cfef80d3d2b193', '2023-11-01 11:23:12', '0000021-00000000000000000000000000000001', '2023-11-01 11:59:17', '0000021-00000000000000000000000000000001', '默认表单布局', 55, '{"widgetList":[{"key":66893,"type":"grid","alias":"column-2-grid","category":"container","icon":"column-2-grid","cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"date","icon":"date-field","formItemFlag":true,"options":{"name":"todoDate","keyNameEnabled":false,"keyName":"","label":"待办时间","labelAlign":"","type":"date","defaultValue":null,"placeholder":"","columnWidth":"200px","size":"","autoFullWidth":true,"labelWidth":null,"labelHidden":false,"labelWrap":false,"readonly":false,"disabled":false,"hidden":false,"clearable":true,"editable":false,"format":"YYYY-MM-DD","valueFormat":"YYYY-MM-DD","required":true,"requiredHint":"","validation":"","validationHint":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"onCreated":"","onMounted":"","onChange":"","onFocus":"","onBlur":"","onValidate":""},"nameReadonly":true,"id":"date50461"}],"options":{"name":"gridCol65324","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":[]},"id":"grid-col-65324"},{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"check-tag","icon":"check-tag-field","formItemFlag":true,"options":{"name":"remindType","keyNameEnabled":false,"keyName":"","label":"提醒方式","labelAlign":"","defaultValue":null,"columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"disabled":false,"hidden":false,"dsEnabled":false,"dsName":"","dataSetName":"","labelKey":"label","valueKey":"value","optionValueType":"","optionItems":[{"value":1,"label":"通知","displayOrder":1},{"value":2,"label":"短信","displayOrder":2},{"value":3,"label":"邮件","displayOrder":3}],"required":false,"requiredHint":"","validation":"","validationHint":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"onCreated":"","onMounted":"","onChange":"","onValidate":""},"nameReadonly":true,"optionItemsReadonly":true,"id":"checktag111215"}],"options":{"name":"gridCol42047","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":[]},"id":"grid-col-42047"}],"options":{"name":"grid46255","hidden":false,"gutter":12,"colHeight":null,"customClass":[]},"id":"grid46255"},{"key":16741,"type":"grid","alias":"column-1-grid","category":"container","icon":"column-1-grid","cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"textarea","icon":"textarea-field","formItemFlag":true,"options":{"name":"todoItem","keyNameEnabled":false,"keyName":"","label":"待办事项","labelAlign":"","rows":3,"autosize":false,"defaultValue":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"readonly":false,"disabled":false,"hidden":false,"required":true,"requiredHint":"","validation":"","validationHint":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"minLength":null,"maxLength":null,"showWordLimit":false,"onCreated":"","onMounted":"","onInput":"","onChange":"","onFocus":"","onBlur":"","onValidate":""},"nameReadonly":true,"id":"textarea83445"}],"options":{"name":"gridCol49822","hidden":false,"span":24,"offset":0,"push":0,"pull":0,"responsive":false,"md":24,"sm":24,"xs":24,"customClass":""},"id":"grid-col-49822"}],"options":{"name":"grid71358","hidden":false,"gutter":12,"colHeight":null,"customClass":""},"id":"grid71358"}],"formConfig":{"modelName":"formData","refName":"vForm","rulesName":"rules","labelWidth":80,"labelPosition":"left","size":"","labelAlign":"label-left-align","cssCode":"","customClass":[],"functions":"","layoutType":"PC","jsonVersion":3,"dataSources":[],"onFormCreated":"","onFormMounted":"","onFormDataChange":"","onFormValidate":""}}'),
	(20, '0000008-a21947f0125f4edcb75291719a312ac0', '2023-11-02 10:41:10', '0000021-00000000000000000000000000000001', '2023-11-02 10:41:10', '0000021-00000000000000000000000000000001', '默认表单布局', 1001, '{"widgetList":[{"key":56398,"type":"grid","alias":"column-2-grid","category":"container","icon":"column-2-grid","cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"input","alias":"","icon":"text-field","formItemFlag":true,"options":{"name":"mingcheng","keyNameEnabled":false,"keyName":"","label":"名称","labelAlign":"","type":"text","defaultValue":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"readonly":false,"disabled":false,"hidden":false,"clearable":true,"showPassword":false,"required":true,"requiredHint":"","validation":"","validationHint":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"minLength":null,"maxLength":null,"showWordLimit":false,"prefixIcon":"","suffixIcon":"","appendButton":false,"appendButtonDisabled":false,"buttonIcon":"custom-search","onCreated":"","onMounted":"","onInput":"","onChange":"","onFocus":"","onBlur":"","onValidate":"","onAppendButtonClick":""},"nameReadonly":true,"id":"input55236"}],"options":{"name":"gridCol75221","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":""},"id":"grid-col-75221"},{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"number","icon":"number-field","formItemFlag":true,"options":{"name":"shuliang","keyNameEnabled":false,"keyName":"","label":"数量","labelAlign":"","defaultValue":0,"placeholder":"","columnWidth":"200px","size":"","controls":true,"labelWidth":null,"labelHidden":false,"labelWrap":false,"disabled":false,"hidden":false,"required":false,"requiredHint":"","validation":"","validationHint":"","customClass":"","labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"min":-100000000000,"max":100000000000,"precision":0,"step":1,"controlsPosition":"right","onCreated":"","onMounted":"","onChange":"","onFocus":"","onBlur":"","onValidate":""},"nameReadonly":true,"id":"number37708"}],"options":{"name":"gridCol14243","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":""},"id":"grid-col-14243"}],"options":{"name":"grid50745","hidden":false,"gutter":12,"colHeight":null,"customClass":[]},"id":"grid50745"},{"key":56398,"type":"grid","alias":"column-2-grid","category":"container","icon":"column-2-grid","cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"number","icon":"number-field","formItemFlag":true,"options":{"name":"jine","keyNameEnabled":false,"keyName":"","label":"金额","labelAlign":"","defaultValue":0,"placeholder":"","columnWidth":"200px","size":"","controls":false,"labelWidth":null,"labelHidden":false,"labelWrap":false,"disabled":false,"hidden":false,"required":true,"requiredHint":"","validation":"","validationHint":"","customClass":"","labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"min":-100000000000,"max":100000000000,"precision":2,"step":1,"controlsPosition":"right","onCreated":"","onMounted":"","onChange":"","onFocus":"","onBlur":"","onValidate":""},"nameReadonly":true,"id":"number61401"}],"options":{"name":"gridCol64255","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":""},"id":"grid-col-64255"},{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"date","icon":"date-field","formItemFlag":true,"options":{"name":"shijian","keyNameEnabled":false,"keyName":"","label":"时间","labelAlign":"","type":"date","defaultValue":null,"placeholder":"","columnWidth":"200px","size":"","autoFullWidth":true,"labelWidth":null,"labelHidden":false,"labelWrap":false,"readonly":false,"disabled":false,"hidden":false,"clearable":true,"editable":false,"format":"YYYY-MM-DD","valueFormat":"YYYY-MM-DD","required":true,"requiredHint":"","validation":"","validationHint":"","customClass":"","labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"onCreated":"","onMounted":"","onChange":"","onFocus":"","onBlur":"","onValidate":""},"nameReadonly":true,"id":"date35938"}],"options":{"name":"gridCol73264","hidden":false,"span":12,"offset":0,"push":0,"pull":0,"responsive":false,"md":12,"sm":12,"xs":12,"customClass":""},"id":"grid-col-73264"}],"options":{"name":"grid61793","hidden":false,"gutter":12,"colHeight":null,"customClass":""},"id":"grid61793"},{"key":60127,"type":"grid","alias":"column-1-grid","category":"container","icon":"column-1-grid","cols":[{"type":"grid-col","category":"container","icon":"grid-col","internal":true,"widgetList":[{"type":"textarea","icon":"textarea-field","formItemFlag":true,"options":{"name":"beizhu","keyNameEnabled":false,"keyName":"","label":"备注","labelAlign":"","rows":3,"autosize":false,"defaultValue":"","placeholder":"","columnWidth":"200px","size":"","labelWidth":null,"labelHidden":false,"labelWrap":false,"readonly":false,"disabled":false,"hidden":false,"required":true,"requiredHint":"","validation":"","validationHint":"","customClass":[],"labelIconClass":null,"labelIconPosition":"rear","labelTooltip":null,"minLength":null,"maxLength":null,"showWordLimit":false,"onCreated":"","onMounted":"","onInput":"","onChange":"","onFocus":"","onBlur":"","onValidate":""},"nameReadonly":true,"id":"textarea91142"}],"options":{"name":"gridCol34292","hidden":false,"span":24,"offset":0,"push":0,"pull":0,"responsive":false,"md":24,"sm":24,"xs":24,"customClass":""},"id":"grid-col-34292"}],"options":{"name":"grid42748","hidden":false,"gutter":12,"colHeight":null,"customClass":[]},"id":"grid42748"}],"formConfig":{"modelName":"formData","refName":"vForm","rulesName":"rules","labelWidth":80,"labelPosition":"left","size":"","labelAlign":"label-left-align","cssCode":"","customClass":[],"functions":"","layoutType":"PC","jsonVersion":3,"dataSources":[],"onFormCreated":"","onFormMounted":"","onFormDataChange":"","onFormValidate":""}}');

-- 导出  表 variantorm_base.t_layout_config 结构
CREATE TABLE IF NOT EXISTS `t_layout_config` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `layoutConfigId` char(40) NOT NULL,
  `configName` varchar(200) DEFAULT NULL,
  `entityCode` int(11) DEFAULT NULL,
  `config` text,
  `applyType` varchar(200) DEFAULT NULL,
  `shareTo` text,
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `ownerUser` char(40) NOT NULL,
  `ownerDepartment` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `layoutConfigId` (`layoutConfigId`) USING BTREE,
  KEY `ownerUser` (`ownerUser`) USING BTREE,
  KEY `ownerDepartment` (`ownerDepartment`) USING BTREE,
  KEY `isDeleted` (`isDeleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_layout_config 的数据：~1 rows (大约)
INSERT INTO `t_layout_config` (`autoId`, `layoutConfigId`, `configName`, `entityCode`, `config`, `applyType`, `shareTo`, `createdOn`, `createdBy`, `ownerUser`, `ownerDepartment`, `modifiedOn`, `modifiedBy`, `isDeleted`) VALUES
	(77, '0000015-81bd74e840ec461993b8365e66a8d42e', '默认导航', NULL, '[{"name":"演示实体","type":1,"entityCode":1001,"outLink":"","guid":"8e66d8d6937040d89dc30b18942d7dfe","parentGuid":"","isOpeneds":false,"useIcon":"","entityName":"Yanshishiti"}]', 'NAV', 'ALL', '2023-11-02 10:05:32', '0000021-00000000000000000000000000000001', '0000021-00000000000000000000000000000001', '0000022-00000000000000000000000000000001', '2023-11-02 10:05:32', '0000021-00000000000000000000000000000001', 0);

-- 导出  表 variantorm_base.t_login_log 结构
CREATE TABLE IF NOT EXISTS `t_login_log` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `loginLogId` char(40) NOT NULL,
  `loginUser` char(40) DEFAULT NULL,
  `sessionId` varchar(200) DEFAULT NULL,
  `ip` varchar(200) DEFAULT NULL,
  `browserName` varchar(200) DEFAULT NULL,
  `logout` tinyint(4) DEFAULT NULL,
  `logoutType` varchar(200) DEFAULT NULL,
  `logoutTime` datetime DEFAULT NULL,
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `loginLogId` (`loginLogId`) USING BTREE,
  KEY `isDeleted` (`isDeleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_login_log 的数据：~2 rows (大约)
INSERT INTO `t_login_log` (`autoId`, `loginLogId`, `loginUser`, `sessionId`, `ip`, `browserName`, `logout`, `logoutType`, `logoutTime`, `createdOn`, `createdBy`, `modifiedOn`, `modifiedBy`, `isDeleted`) VALUES
	(1, '0000025-6ec207c45cd84367ad5578dafc45c70a', '0000021-00000000000000000000000000000001', '0C658B1F1207D01156851507EFC524FA', '127.0.0.1', 'Chrome', 0, NULL, NULL, '2023-11-02 10:34:47', '0000021-00000000000000000000000000000001', '2023-11-02 10:34:47', '0000021-00000000000000000000000000000001', 0),
	(2, '0000025-03daef9bb00741c48a5d48baa3bcc9f1', '0000021-00000000000000000000000000000001', '440C930E853F50743A8C81A38750423F', '127.0.0.1', 'Chrome', 0, NULL, NULL, '2023-11-02 10:40:17', '0000021-00000000000000000000000000000001', '2023-11-02 10:40:17', '0000021-00000000000000000000000000000001', 0);

-- 导出  表 variantorm_base.t_meta_api 结构
CREATE TABLE IF NOT EXISTS `t_meta_api` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `metaApiId` char(40) NOT NULL,
  `appId` char(10) NOT NULL,
  `appSecret` char(40) DEFAULT NULL,
  `bindUser` char(40) DEFAULT NULL,
  `bindIps` varchar(200) DEFAULT NULL,
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `metaApiId` (`metaApiId`) USING BTREE,
  KEY `isDeleted` (`isDeleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_meta_api 的数据：~0 rows (大约)

-- 导出  表 variantorm_base.t_meta_entity 结构
CREATE TABLE IF NOT EXISTS `t_meta_entity` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `entityId` char(40) NOT NULL,
  `name` varchar(190) NOT NULL,
  `label` varchar(190) NOT NULL,
  `physicalName` varchar(190) NOT NULL,
  `entityCode` smallint(6) NOT NULL,
  `detailEntityFlag` tinyint(4) NOT NULL,
  `mainEntity` varchar(190) DEFAULT NULL,
  `layoutable` tinyint(4) NOT NULL,
  `listable` tinyint(4) NOT NULL,
  `authorizable` tinyint(4) NOT NULL,
  `shareable` tinyint(4) NOT NULL,
  `assignable` tinyint(4) NOT NULL,
  PRIMARY KEY (`autoId`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `entityId` (`entityId`) USING BTREE,
  UNIQUE KEY `entityCodeUnique` (`entityCode`),
  KEY `entityCode` (`entityCode`),
  KEY `mainEntity` (`mainEntity`)
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=utf8mb4 COMMENT='元数据实体表' COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_meta_entity 的数据：~29 rows (大约)
INSERT INTO `t_meta_entity` (`autoId`, `entityId`, `name`, `label`, `physicalName`, `entityCode`, `detailEntityFlag`, `mainEntity`, `layoutable`, `listable`, `authorizable`, `shareable`, `assignable`) VALUES
	(5, '0000001-94b75b5055d84b3b875e7bdf17b3fc15', 'OptionItem', '单选项', 't_option_item', 3, 0, NULL, 0, 0, 0, 0, 0),
	(41, '0000001-969db3a619374241a6a5204567c5d225', 'Department', '部门', 't_department', 22, 0, NULL, 1, 1, 0, 0, 0),
	(42, '0000001-da4c40fd5bf2442e8876ca2bf9325bb5', 'User', '用户', 't_user', 21, 0, NULL, 1, 1, 1, 0, 0),
	(63, '0000001-b55cefdc36be478fb8c55bfc74497b18', 'TagItem', '多选项', 't_tag_item', 4, 0, NULL, 0, 0, 0, 0, 0),
	(64, '0000001-d7e075a6db5e49678e9d7e19586227be', 'ReferenceListMap', '多对多中间表', 't_reference_list_map', 5, 0, NULL, 0, 0, 0, 0, 0),
	(66, '0000001-dcd3e497a01a4c6cb6963846fa18915f', 'FormLayout', '表单布局', 't_form_layout', 8, 0, NULL, 0, 0, 0, 0, 0),
	(67, '0000001-dc36c2270f7d4ccf92977829d6bf8ccf', 'ReferenceCache', '名称字段缓存', 't_reference_cache', 6, 0, NULL, 0, 0, 0, 0, 0),
	(70, '0000001-aa7da05abfc74b83927dabddebd2d372', 'DataListView', '数据列表视图', 't_data_list_view', 9, 0, NULL, 0, 0, 0, 0, 0),
	(74, '0000001-14e80397cc484d688db8527e9818c8c7', 'Role', '权限角色', 't_role', 23, 0, NULL, 0, 1, 0, 0, 0),
	(79, '0000001-c738db91bca94ed9b5bc9584aa08d9dc', 'SystemSetting', '系统参数', 't_system_setting', 7, 0, NULL, 0, 0, 0, 0, 0),
	(82, '0000001-20038000d906442bab8aceed1de1e865', 'RouterMenu', '系统导航菜单', 't_router_menu', 10, 0, NULL, 0, 0, 0, 0, 0),
	(84, '0000001-67445fd419924713afbd9e751413244a', 'DepartmentNode', '部门节点关系', 't_department_node', 11, 0, NULL, 0, 0, 0, 0, 0),
	(85, '0000001-292e4fe491194142a51435adbff06efd', 'ApprovalConfig', '审批配置', 't_approval_config', 30, 0, NULL, 1, 1, 0, 0, 0),
	(86, '0000001-abe9b411152949e9b8fe21cb7f133977', 'ApprovalFlow', '审批流程', 't_approval_flow', 31, 0, NULL, 1, 1, 1, 0, 0),
	(87, '0000001-16f13af6de3343cba7d3c894ed70ab3b', 'ApprovalHistory', '审批历史', 't_approval_history', 32, 0, NULL, 1, 1, 0, 0, 0),
	(88, '0000001-dcae3ebbc5fb467a89ce915f26462325', 'ApprovalTask', '审批任务', 't_approval_task', 33, 0, NULL, 1, 1, 0, 0, 0),
	(91, '0000001-d653eaf82cff4b0eba2fb0d5d3a65a40', 'ReportConfig', '报表模板', 't_report_config', 45, 0, NULL, 0, 0, 0, 0, 0),
	(94, '0000001-62813135f59540b09ae24c695ce0c0d4', 'Notification', '消息通知', 't_notification', 47, 0, NULL, 0, 0, 0, 0, 0),
	(95, '0000001-6a28f709261211eebaa248f3170452ce', 'StatusItem', '状态项', 't_status_item', 12, 0, NULL, 0, 0, 0, 0, 0),
	(98, '0000001-984d265c2a0411eebaa248f3170452ce', 'Team', '团队', 't_team', 24, 0, NULL, 1, 1, 1, 0, 0),
	(99, '0000001-9c815f4069d44dedb4ecf079e758731e', 'RecycleBin', '回收站', 't_recycle_bin', 46, 0, NULL, 0, 0, 0, 0, 0),
	(100, '0000001-f7e7325be7aa45a2865d80c30137d2fc', 'TriggerConfig', '触发器', 't_trigger_config', 48, 0, NULL, 0, 0, 0, 0, 0),
	(101, '0000001-d8e6f1c297df4e15acf982ed2c5ed4d3', 'RevisionHistory', '修改历史', 't_revision_history', 49, 0, NULL, 0, 0, 0, 0, 0),
	(104, '0000001-b76dd0a79844407481bc98b2a017bcf3', 'LayoutConfig', '布局设置', 't_layout_config', 15, 0, NULL, 1, 1, 1, 0, 0),
	(105, '0000001-6be16e778d5e4e78864d161119c9fc60', 'ShareAccess', '共享访问', 't_share_access', 50, 0, NULL, 1, 1, 0, 0, 0),
	(110, '0000001-6d389bf3be5944278275a15a46e2bdf4', 'MetaApi', 'API管理', 't_meta_api', 51, 0, NULL, 0, 0, 0, 0, 0),
	(111, '0000001-62825829efe84d6b922a63202d3d6096', 'Chart', '仪表盘', 't_chart', 52, 0, NULL, 1, 1, 1, 0, 0),
	(141, '0000001-ac3426a76c84413aa663e7a53e6b8799', 'TriggerLog', '触发器日志', 't_trigger_log', 53, 0, NULL, 0, 0, 0, 0, 0),
	(142, '0000001-9f3ae82b23ec44d08d1a835361d3d59b', 'LoginLog', '登录日志', 't_login_log', 25, 0, NULL, 0, 0, 0, 0, 0),
	(164, '0000001-0c58d4c7dc65473d81ed5e7622223f18', 'Yanshishiti', '演示实体', 't_yanshishiti', 1001, 0, NULL, 1, 1, 1, 0, 0);

-- 导出  表 variantorm_base.t_meta_field 结构
CREATE TABLE IF NOT EXISTS `t_meta_field` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `fieldId` char(40) NOT NULL,
  `entityCode` smallint(6) NOT NULL,
  `name` varchar(190) NOT NULL,
  `label` varchar(190) NOT NULL,
  `physicalName` varchar(190) NOT NULL,
  `type` varchar(50) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `displayOrder` smallint(6) DEFAULT '0',
  `nullable` tinyint(4) NOT NULL,
  `creatable` tinyint(4) NOT NULL,
  `updatable` tinyint(4) NOT NULL,
  `idFieldFlag` tinyint(4) NOT NULL,
  `nameFieldFlag` tinyint(4) NOT NULL,
  `mainDetailFieldFlag` tinyint(4) NOT NULL,
  `defaultMemberOfListFlag` tinyint(4) NOT NULL,
  `referTo` varchar(500) NOT NULL DEFAULT '',
  `referenceSetting` text,
  `fieldViewModel` text,
  PRIMARY KEY (`autoId`),
  UNIQUE KEY `fieldId` (`fieldId`) USING BTREE,
  UNIQUE KEY `entityCode_name` (`entityCode`,`name`),
  KEY `displayOrder` (`displayOrder`),
  KEY `mainDetailFieldFlag` (`mainDetailFieldFlag`),
  KEY `defaultMemberOfListFlag` (`defaultMemberOfListFlag`),
  KEY `name` (`name`),
  KEY `idFieldFlag` (`idFieldFlag`),
  KEY `nameFieldFlag` (`nameFieldFlag`),
  KEY `entityId` (`entityCode`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1544 DEFAULT CHARSET=utf8mb4 COMMENT='元数据实体字段表' COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_meta_field 的数据：~265 rows (大约)
INSERT INTO `t_meta_field` (`autoId`, `fieldId`, `entityCode`, `name`, `label`, `physicalName`, `type`, `description`, `displayOrder`, `nullable`, `creatable`, `updatable`, `idFieldFlag`, `nameFieldFlag`, `mainDetailFieldFlag`, `defaultMemberOfListFlag`, `referTo`, `referenceSetting`, `fieldViewModel`) VALUES
	(3, '0000002-10e0b126bbe241dba6706d6c35a2a733', 3, 'optionItemId', 'id主键', 'optionItemId', 'PrimaryKey', NULL, 0, 0, 1, 0, 1, 0, 0, 0, '', '', ' '),
	(4, '0000002-b8926e33ee6449bfb23c0c49fdced16e', 3, 'entityName', '实体名称', 'entityName', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '', ' '),
	(5, '0000002-e7daf427a4764c178948a658c6c2ce89', 3, 'fieldName', '字段名称', 'fieldName', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '', ' '),
	(6, '0000002-4c95f26acfff490e9ca8ac81029560c6', 3, 'value', '选项值', 'value', 'Integer', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '', ' '),
	(7, '0000002-87648cb551264d7eb13de38758563129', 3, 'label', '显示值', 'label', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '', ' '),
	(8, '0000002-08a2dbe9605c4714bffea1eee92617b8', 3, 'displayOrder', '显示顺序', 'displayOrder', 'Integer', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', '', ' '),
	(99, '0000002-5dbd6f5fae8042e89fdee04e3d16ec74', 22, 'departmentId', '部门Id主键', 'departmentId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', '', 'null'),
	(100, '0000002-95b6449e2d6046f696ce820d57a4af15', 22, 'parentDepartmentId', '上级部门', 'parentDepartmentId', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 1, 'Department,', '[{"entityName":"Department","fieldList":["parentDepartmentId","departmentName"]}]', '{"searchDialogWidth":520}'),
	(101, '0000002-81fad5455d484c5c98195efddd9873f0', 22, 'departmentName', '部门名称', 'departmentName', 'Text', NULL, 0, 0, 1, 1, 0, 1, 0, 1, '', '[]', '{}'),
	(102, '0000002-1d7b3217965740dd8c1dc02c42ac5c77', 21, 'userId', '用户Id主键', 'userId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', '', 'null'),
	(103, '0000002-13250634eeb747d3bbf38521ef81a02f', 21, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', '', 'null'),
	(104, '0000002-21dd1ebe60ba44a6a5979ad8389d44db', 21, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', '', 'null'),
	(105, '0000002-fdb3e8f617c24d8ab777ec62160cb15a', 21, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', '', 'null'),
	(106, '0000002-f6c5afa38f3a45bf929d0ad61ebdb8ba', 21, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', '', 'null'),
	(107, '0000002-e478114b892f4ec98c837999d0bd34d0', 21, 'ownerUser', '所属用户', 'ownerUser', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', '', 'null'),
	(108, '0000002-752fd630ebec4bc3ae78b55870af63da', 21, 'ownerDepartment', '所属部门', 'ownerDepartment', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'Department,', '', 'null'),
	(109, '0000002-7513d702731b4931942ad493cc5b2a65', 21, 'departmentId', '部门', 'departmentId', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 1, 'Department,', '[{"entityName":"Department","fieldList":["departmentName","parentDepartmentId"]}]', '{"searchDialogWidth":510}'),
	(110, '0000002-f2cb605fe6d6436592b96a48ecff5cea', 21, 'userName', '用户名称', 'userName', 'Text', NULL, 0, 0, 1, 1, 0, 1, 0, 1, '', '[]', '{"minLength":2,"maxLength":15}'),
	(140, '0000002-a2784238ef4b485f83fc127f4ce6f632', 21, 'loginPwd', '登录密码', 'loginPwd', 'Password', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', '[]', '{"minLength":6,"maxLength":30}'),
	(141, '0000002-b6d5956369bd4ff49665fde70193721e', 21, 'loginName', '登录账号名', 'loginName', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '[]', '{"minLength":1,"maxLength":30,"validators":["letterStartNumberIncluded"]}'),
	(247, '0000002-d1426705eec949f8834dd529d50ee9f4', 4, 'tagItemId', 'id主键', 'tagItemId', 'PrimaryKey', NULL, 0, 0, 1, 0, 1, 0, 0, 0, '', '', ' '),
	(248, '0000002-d272ba54c3024273a3895b8e5e3e431e', 4, 'entityName', '实体名称', 'entityName', 'Text', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', '', ' '),
	(250, '0000002-b858519aac5046f3aa920c7affd9a5e2', 4, 'fieldName', '字段名称', 'fieldName', 'Text', NULL, 0, 0, 1, 0, 0, 1, 0, 1, '', '', ' '),
	(251, '0000002-a7520280b2ff48c18248f9cacf956e0c', 4, 'value', '选项值', 'value', 'Text', NULL, 0, 0, 1, 0, 0, 1, 0, 1, '', '', ' '),
	(252, '0000002-ffd3f0af6385412cbcc0e599ad86d08d', 4, 'displayOrder', '显示顺序', 'displayOrder', 'Integer', NULL, 0, 0, 1, 0, 0, 1, 0, 1, '', '', ' '),
	(276, '0000002-8506e8bd1f9f44de8cddfb66a937bfc7', 5, 'entityName', '实体名称', 'entityName', 'Text', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', '', ' '),
	(277, '0000002-f1de92f4a8bc4b3691ec57c3fd7b25b6', 5, 'fieldName', '字段名称', 'fieldName', 'Text', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', '', ' '),
	(278, '0000002-4ba0343384e14796bf1c36e1fb78d83d', 5, 'objectId', '记录Id', 'objectId', 'PrimaryKey', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', '', ' '),
	(287, '0000002-8307d012d26540fbb32de971186d752a', 5, 'mapId', '主键Id', 'mapId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', '', ' '),
	(288, '0000002-9c18b976a6cc49259abbeb8d82296ea2', 8, 'formLayoutId', '布局Id主键', 'formLayoutId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(289, '0000002-8ee2ae343e41467d8adff215a48fe759', 8, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(290, '0000002-f295392a78724cb491d86ae6f8163a3e', 8, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(291, '0000002-9bb29a7d00a844319d9461257d79144a', 8, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(292, '0000002-91bbe75b348d4da7a00094d5b2e30916', 8, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(295, '0000002-b330a354ff044014b2a17b99de8ce7da', 8, 'layoutName', '布局名称', 'layoutName', 'Text', NULL, 0, 0, 1, 1, 0, 1, 0, 0, '', NULL, NULL),
	(296, '0000002-aed853a096fc4f1b877e0ba7560a7d8e', 8, 'entityCode', '实体Code', 'entityCode', 'Integer', NULL, 0, 0, 1, 0, 0, 0, 0, 0, '', NULL, NULL),
	(297, '0000002-1c86ce63e89143a892928dfff763c95f', 8, 'layoutJson', '布局Json', 'layoutJson', 'TextArea', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(298, '0000002-d75e8fb1c87f430b8cd313a749681e1e', 6, 'cacheId', '缓存Id主键', 'cacheId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(300, '0000002-0dba32430b964cc68b7a0c69ff6e1414', 6, 'referenceId', '实体记录Id', 'referenceId', 'PrimaryKey', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(301, '0000002-3cab622d31be43d68c5001cce0409f77', 6, 'recordLabel', '实体名称值', 'recordLabel', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(325, '0000002-e33e1fc889b64ab598a94258cce5740b', 21, 'jobTitle', '职务', 'jobTitle', 'Option', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, NULL),
	(340, '0000002-0d5b8cbdf00e4142838f9c2afb012006', 9, 'dataListViewId', 'id主键', 'dataListViewId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(341, '0000002-410b4109ea5c4068bc5717a02a8189f5', 9, 'entityCode', '实体编码', 'entityCode', 'Integer', NULL, 0, 0, 1, 0, 0, 0, 0, 0, '', NULL, NULL),
	(342, '0000002-3e9a74f67262436fbfe23c7f708388e5', 9, 'viewName', '列表视图名称', 'viewName', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(343, '0000002-37b4912ab02544459beebca6be74d02d', 9, 'headerJson', '表头Json', 'headerJson', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(344, '0000002-fa583966feed4a6aade430bd695d66b4', 9, 'filterJson', '筛选条件Json', 'filterJson', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(345, '0000002-20c80f782cf743a5ae025cc41adce6ec', 9, 'paginationJson', '分页Json', 'paginationJson', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(346, '0000002-c2f9937f838e41eb967519d658f215f3', 9, 'sortJson', '排序Json', 'sortJson', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(384, '0000002-22eca3bc8fba43caa55af51f0bb5cfa9', 23, 'roleId', 'id主键', 'roleId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(385, '0000002-7b6c8dadf43e4bff985832449211e592', 23, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(386, '0000002-ab898840b9f24bc49b0b08b2a0ba8eb9', 23, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(387, '0000002-4dc6107bc6494c2785bccbba62538afe', 23, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(388, '0000002-9f6008ef913b499bbec8f72379168229', 23, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(389, '0000002-e5c745c2590745d18a3d157cf6ff1a19', 23, 'description', '角色说明', 'description', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{}'),
	(390, '0000002-41684e98ac7b4e32bf4ea5ffc536cd34', 21, 'roles', '权限角色', 'roles', 'ReferenceList', NULL, 0, 1, 1, 1, 0, 0, 0, 0, 'Role,', '[{"entityName":"Role","fieldList":["roleName","disabled"]}]', '{}'),
	(391, '0000002-514f01a18668485c87c1bb61d0f61c78', 23, 'roleName', '角色名称', 'roleName', 'Text', NULL, 0, 0, 1, 1, 0, 1, 0, 1, '', NULL, '{}'),
	(392, '0000002-84801abecdee4232a5f98b14f20d467b', 21, 'disabled', '是否禁用', 'disabled', 'Boolean', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(393, '0000002-7f1f2a1961fc4af19cbe492accd5a73b', 23, 'disabled', '是否禁用', 'disabled', 'Boolean', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{}'),
	(394, '0000002-87113e1cedfc42da89ae92652495dee6', 23, 'rightJson', '权限明细', 'rightJson', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(418, '0000002-612ac4ef5f7a4c67a37d3701dda09ada', 7, 'systemSettingId', 'id主键', 'systemSettingId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(419, '0000002-eae1e10bbcca4300ad1daa67e115163c', 7, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(420, '0000002-3ab22df2ae0f43eba9942cfaac51d60d', 7, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(421, '0000002-bb1ec0801e1f434295d13496f152a24f', 7, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(422, '0000002-e0b22ade875f4b989de9a4c010e7c652', 7, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(423, '0000002-027f9c3161484b4eb49dc0abe66563db', 7, 'settingName', '参数名', 'settingName', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(424, '0000002-8ffcba44c63047a78216c7f07bbb724b', 7, 'settingValue', '参数值', 'settingValue', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(425, '0000002-5c87644ea3804ed5a036209b4338b120', 7, 'defaultValue', '参数默认值', 'defaultValue', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(438, '0000002-3a03545e162f4c38a1940df3025ba87d', 5, 'toId', '被引用记录Id', 'toId', 'PrimaryKey', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', '', ' '),
	(439, '0000002-c30ed9da51914472875f58dd3c70ffa6', 9, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(440, '0000002-9d9e1772f2aa410881a76f28c4885224', 9, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(441, '0000002-e4caadcacbc9466b8b23cc9eac69e030', 9, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(442, '0000002-614dc77ccc8447a0a70db3b83a6d54f1', 9, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(443, '0000002-a8a08012a696474f9b0ef1f696659b1d', 22, 'description', '部门说明', 'description', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(450, '0000002-7869d40b9c834bfd8202a124bd6422c2', 9, 'presetFilter', '内置筛选条件', 'presetFilter', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(451, '0000002-a00ec4d314fe40dcb1855e0ed5d230f8', 10, 'routerMenuId', 'id主键', 'routerMenuId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(452, '0000002-9a54a698a20646948a5bb5dddc6d6434', 10, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(453, '0000002-3a302e9a10264be0b6aea8bfb225d8dd', 10, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(454, '0000002-82b08f7f52624724a8eb648d5ddc9311', 10, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(455, '0000002-bee6df672ab84000b5a4732ed30ea037', 10, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(456, '0000002-7b3d5bef72a04e1aa0843f0ea4dd00b5', 10, 'menuJson', '菜单Json', 'menuJson', 'TextArea', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(457, '0000002-6578cbccc12d4742a7ec9367804fc076', 11, 'departmentNodeId', 'id主键', 'departmentNodeId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(462, '0000002-96e5ac1713f9476caf5352eb695db7f7', 11, 'parentDepartmentId', '父级部门Id', 'parentDepartmentId', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 0, 'Department,', NULL, NULL),
	(463, '0000002-6dd7cf2a0d7f49e6a552850db11ae811', 11, 'childDepartmentId', '下级部门Id', 'childDepartmentId', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 0, 'Department,', NULL, NULL),
	(465, '0000002-f88728b7bfa546efb03d54bbf17e11d5', 30, 'approvalConfigId', '流程配置id', 'approvalConfigId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', '[]', '{}'),
	(466, '0000002-b6d1cddd628b4eb695fa18cf61bb2387', 30, 'approvalFlowId', '最新的流程定义id', 'approvalFlowId', 'Reference', NULL, 0, 1, 1, 1, 0, 0, 0, 0, 'ApprovalFlow,', '[]', '{}'),
	(467, '0000002-1ada0d1790bc4764af60493064f93b9d', 30, 'entityCode', '实体Code', 'entityCode', 'Integer', NULL, 0, 0, 1, 0, 0, 0, 0, 0, '', NULL, NULL),
	(468, '0000002-834cd858ab664beaa3e66e644e325e67', 30, 'flowName', '流程名称', 'flowName', 'Text', NULL, 0, 0, 1, 1, 0, 1, 0, 1, '', '[]', '{}'),
	(469, '0000002-bb254b9615ed4ad5ab07dff956af1c08', 30, 'isDisabled', '是否禁用', 'isDisabled', 'Boolean', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{}'),
	(471, '0000002-1987a3b3f6ca4f1f92109dc2f6c24769', 30, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(472, '0000002-589ca44c4147436eb83918eef3c8e0fc', 30, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(487, '0000002-e05ebde79a8345e09147265b248da2d9', 45, 'reportConfigId', 'id主键', 'reportConfigId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(488, '0000002-98ec134468d948ada7e5e89f72a3bf1d', 45, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(489, '0000002-361839e052fb46ccb1e3ceb4343d85f1', 45, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(490, '0000002-acfb53e4aac74ddebc6da719d3cf20a7', 45, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(491, '0000002-c654895a0f1a42edb458b99375e9ad57', 45, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(492, '0000002-74eb35f3edad4777ac9d7a677231a928', 45, 'reportName', '模板名称', 'reportName', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(493, '0000002-74968787c77043fa8def7e999d587160', 45, 'reportJson', '报表json', 'reportJson', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(494, '0000002-0bddb8116e34442c96c8c6864f917001', 45, 'entityCode', '实体Code', 'entityCode', 'Integer', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(495, '0000002-33c97316ca7748618353efaba7f9b3f2', 31, 'approvalFlowId', '流程定义id', 'approvalFlowId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', '[]', '{}'),
	(496, '0000002-e22622789ef4419081ed490cd2a18fcd', 31, 'approvalConfigId', '审批流程id', 'approvalConfigId', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 0, 'ApprovalConfig,', '[]', '{}'),
	(497, '0000002-b61127049d4243918c8080c8ebaa87ee', 31, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(498, '0000002-bdf5e13054fe40879aeb0ca1b5f82b77', 31, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(499, '0000002-edd7ce1e143049d19444d22d740be8d0', 31, 'flowDefinition', '流程定义json', 'flowDefinition', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '[]', '{}'),
	(500, '0000002-5e22681be564497ab56518a03f9321c0', 32, 'approvalHistoryId', '流程任务id', 'approvalHistoryId', 'PrimaryKey', NULL, 0, 0, 1, 0, 1, 0, 0, 0, '', '[]', '{}'),
	(501, '0000002-2f8440d56e7649638e88c388c45cba12', 32, 'approvalTaskId', '审批流程id', 'approvalTaskId', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 0, 'ApprovalTask,', '[]', '{}'),
	(502, '0000002-b67a35a30dc34c938be2bccb12e58301', 32, 'approver', '审批人', 'approver', 'Reference', NULL, 0, 1, 1, 1, 0, 0, 0, 0, 'User,', '[{"entityName":"User","fieldList":["userName","disabled"]}]', '{}'),
	(503, '0000002-d990c4f0efd049df8c44d32ba8700f5c', 32, 'approvalOn', '审批时间', 'approvalOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(504, '0000002-a91989c996b643d28f328aeb384f927a', 32, 'remark', '批注', 'remark', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', '[]', '{}'),
	(505, '0000002-105a7b3925f448e9b97cd507fe5aaa7f', 32, 'isBacked', '是否驳回', 'isBacked', 'Boolean', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', '[]', '{}'),
	(506, '0000002-e4e10ef5aca249f99a1dd1f6408353f0', 32, 'attrMore', '扩展属性 (JSON Map)', 'attrMore', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', '[]', '{}'),
	(507, '0000002-04f7e43f36df42999c02ff5c1e35967a', 33, 'approvalTaskId', '流程任务id', 'approvalTaskId', 'PrimaryKey', NULL, 0, 0, 1, 0, 1, 0, 0, 0, '', '[]', '{}'),
	(508, '0000002-26ccd26c837044209f59dcccd0ff85b3', 33, 'approvalFlowId', '审批流程id', 'approvalFlowId', 'Reference', NULL, 0, 1, 1, 1, 0, 0, 0, 0, 'ApprovalFlow,', '[]', '{}'),
	(509, '0000002-bef377af681c4618a257918873dc63a6', 33, 'approver', '当前审批人', 'approver', 'ReferenceList', NULL, 0, 1, 1, 1, 0, 0, 0, 0, 'User,', '[{"entityName":"User","fieldList":["userName","disabled"]}]', '{}'),
	(510, '0000002-6c487c0164ed4a2eb84cc75914fefb83', 33, 'approvalOn', '审批时间', 'approvalOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(511, '0000002-63c7a920cc2d439eb37bac41f8f70020', 33, 'stepName', '步骤名称', 'stepName', 'Text', NULL, 0, 0, 1, 1, 0, 1, 0, 1, '', '[]', '{}'),
	(512, '0000002-c430e50a7f7c451082425f8895a09ba1', 33, 'remark', '批注', 'remark', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', '[]', '{}'),
	(515, '0000002-44359e6718ad451face1f64c3d89121f', 33, 'attrMore', '扩展属性 (JSON Map)', 'attrMore', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', '[]', '{}'),
	(516, '0000002-5a98bbc7476f4adabb7f66027259def6', 33, 'entityId', '关联实体ID', 'entityId', 'AnyReference', NULL, 0, 0, 1, 0, 0, 0, 0, 0, '', NULL, NULL),
	(517, '0000002-63c7a920cc2d439eb37bac41f8f71879', 33, 'currentNode', '步骤节点', 'currentNode', 'Integer', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(525, '0000002-1116ee70c29b4a4882266058422bafd8', 30, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(526, '0000002-c2711e4482fc40059ee9a44bc94dd3a7', 30, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(528, '0000002-e22622789ef4419081ed490cd2a18fce', 33, 'approvalConfigId', '审批流程id', 'approvalConfigId', 'Reference', NULL, 0, 1, 1, 1, 0, 0, 0, 0, 'ApprovalConfig,', '[]', '{}'),
	(529, '0000002-1ada0d1790bc4764af60493064f83b9d', 30, 'runningTotal', '运行中的流程统计', 'runningTotal', 'Integer', NULL, 0, 1, 1, 0, 0, 0, 0, 0, '', NULL, NULL),
	(530, '0000002-1ada0d1790bc4adcaf60493064f83b9d', 30, 'completeTotal', '结束的流程统计', 'completeTotal', 'Integer', NULL, 0, 1, 1, 0, 0, 0, 0, 0, '', NULL, NULL),
	(540, '0000002-63c7a920cc2d439eb37bac41f8f700er', 32, 'stepName', '步骤名称', 'stepName', 'Text', NULL, 0, 0, 1, 1, 0, 1, 0, 1, '', '[]', '{}'),
	(541, '0000002-63c7a920cc2d439eb37bac41f8f71456', 33, 'signType', '审核类型', 'signType', 'Integer', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(544, '0000002-63c7aabc456d439eb37bac41f8f71456', 32, 'nodeType', '节点类型', 'nodeType', 'Integer', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(550, '0000002-64e83f71e4484933aa0e68e97a931c98', 45, 'isDisabled', '是否禁用', 'isDisabled', 'Boolean', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, '{"validators":[]}'),
	(551, '0000002-16236335251211eea2c048f3170452ce', 3, 'systemFlag', '系统选项', 'systemFlag', 'Integer', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', '[]', '{}'),
	(554, '0000002-e2ea9922a18f4ae5a020b2013e77f4ed', 47, 'notificationId', 'id主键', 'notificationId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(555, '0000002-77ad542b6e41483782a7095d39799a92', 47, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(556, '0000002-06c1878ca4274e199702029f88a9828f', 47, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(557, '0000002-66fc1621e938492ba25b665ad2a852e9', 47, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(558, '0000002-911f6907dc16477c82f4f78fe7be9d84', 47, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(559, '0000002-703aa13ea6c8447e8fdb594b85974d04', 47, 'toUser', '接收人', 'toUser', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 1, 'User,', '[{"entityName":"User","fieldList":["userName"]}]', '{"searchDialogWidth":520,"validators":[]}'),
	(560, '0000002-12873f367c2344dbb8ff202864eb4d6f', 47, 'fromUser', '发送人', 'fromUser', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 0, 'User,', '[{"entityName":"User","fieldList":["userName"]}]', '{"searchDialogWidth":520,"validators":[]}'),
	(561, '0000002-5b430267430b4970be44486ae33a4b5f', 47, 'unread', '是否未读', 'unread', 'Boolean', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', '[]', '{"validators":[]}'),
	(562, '0000002-705e8e1125e649af9fe485d05b48c4bc', 47, 'type', '消息分类', 'type', 'Integer', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, '{"maxValue":999999999,"minValue":0,"validators":[]}'),
	(563, '0000002-5284394274be4a7cb6c84c4eb1f4b640', 47, 'relatedRecord', '相关记录', 'relatedRecord', 'AnyReference', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":200,"validators":[]}'),
	(564, '0000002-03b58fe6261411eebaa248f3170452ce', 12, 'value', '选项值', 'value', 'Integer', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '', ' '),
	(565, '0000002-120fa79b261411eebaa248f3170452ce', 12, 'statusItemId', 'id主键', 'statusItemId', 'PrimaryKey', NULL, 0, 0, 1, 0, 1, 0, 0, 0, '', '', ' '),
	(566, '0000002-1ce901e3261411eebaa248f3170452ce', 12, 'label', '显示值', 'label', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '', ' '),
	(567, '0000002-28a6bb99261411eebaa248f3170452ce', 12, 'fieldName', '字段名称', 'fieldName', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '', ' '),
	(569, '0000002-31ad9b3b261411eebaa248f3170452ce', 12, 'displayOrder', '显示顺序', 'displayOrder', 'Integer', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', '', ' '),
	(570, '0000002-68085d983c0e4647afeda219851490ae', 47, 'message', '消息', 'message', 'TextArea', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":1000,"rows":3,"validators":[]}'),
	(575, '0000002-26ccd26c837044209f59dcccdttt85b3', 33, 'approvalStatus', '审核状态', 'approvalStatus', 'Status', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', '[]', '{}'),
	(596, '0000002-2adb702cb6c64724b5e5fd02d450fb16', 32, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(597, '0000002-1d27e791f59148d0a95a6f62358ca0a3', 32, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(598, '0000002-8f5893bcb09e4e76be5ab348090e37ef', 32, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(599, '0000002-76df2889cb8546b6ba41a15755c44bea', 32, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(600, '0000002-a8018bba5cea401fa6edf71e6c1116cf', 33, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(601, '0000002-638ef780e9b446958c55e1a3f763a53f', 33, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(602, '0000002-299b3d187a9049d0836d3a78f8df5a15', 33, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(603, '0000002-e36b5a2bb789411cb41f4b6eec7f73b3', 33, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(604, '0000002-e631c5a8d1be43f1893876bbe2afc447', 33, 'ccTo', '抄送人', 'ccTo', 'ReferenceList', NULL, 0, 1, 1, 1, 0, 0, 0, 0, 'User,', '[{"entityName":"User","fieldList":["userName","disabled"]}]', '{}'),
	(605, '0000002-299b3d187a9049d0836d3a45f8df5a15', 33, 'approvalUser', '最近审批人', 'approvalUser', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(606, '0000002-4609c0d32a0711eebaa248f3170452ce', 24, 'teamId', '团队Id主键', 'teamId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', '', 'null'),
	(608, '0000002-829844ed2a0711eebaa248f3170452ce', 24, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', '', 'null'),
	(609, '0000002-8298451b2a0711eebaa248f3170452ce', 24, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', '', 'null'),
	(610, '0000002-8298451f2a0711eebaa248f3170452ce', 24, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', '', 'null'),
	(611, '0000002-829845082a0711eebaa248f3170452ce', 24, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', '', 'null'),
	(612, '0000002-829845182a0711eebaa248f3170452ce', 24, 'ownerUser', '所属用户', 'ownerUser', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', '', 'null'),
	(613, '0000002-829845202a0711eebaa248f3170452ce', 24, 'ownerDepartment', '所属部门', 'ownerDepartment', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'Department,', '', 'null'),
	(614, '0000002-829845222a0711eebaa248f3170452ce', 24, 'disabled', '是否禁用', 'disabled', 'Boolean', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(615, '0000002-e9c079572a0711eebaa248f3170452ce', 24, 'teamName', '团队名称', 'teamName', 'Text', NULL, 0, 0, 1, 1, 0, 1, 0, 1, '', '[]', '{"minLength":2,"maxLength":15}'),
	(617, '0000002-e9c0795e2a0711eebaa248f3170452ce', 24, 'principalId', '负责人', 'principalId', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 1, 'User,', '', NULL),
	(629, '0000002-63c7a920cc2d439eb98bac41f8f71879', 32, 'currentNode', '步骤节点', 'currentNode', 'Integer', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(630, '0000002-e631c5a8d1be43f1893876bbe2afc123', 32, 'currentCCTo', '当前步骤抄送人', 'currentCCTo', 'ReferenceList', NULL, 0, 1, 1, 1, 0, 0, 0, 0, 'User,', '[{"entityName":"User","fieldList":["userName","disabled"]}]', '{}'),
	(631, '0000002-204b88256ddd461993a0bd88b0ee6e0c', 46, 'recycleBinId', 'id主键', 'recycleBinId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(636, '0000002-6168dea1048a46fdab080d3e99ad163b', 46, 'entityCode', '所属实体', 'entityCode', 'Integer', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":50,"validators":[]}'),
	(637, '0000002-f35d43d210bd43dfa8be0932311c9392', 46, 'entityId', '相关实体Id', 'entityId', 'Text', NULL, 0, 0, 1, 0, 0, 0, 0, 0, '', NULL, '{"minLength":0,"maxLength":50,"validators":[]}'),
	(638, '0000002-077f2fabea7842789524c44f8521b109', 46, 'entityName', '相关实体名称', 'entityName', 'Text', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":200,"validators":[]}'),
	(639, '0000002-e06dbf3e519148e6a7ad1d71e3f72e2c', 46, 'deletedBy', '删除人', 'deletedBy', 'Reference', NULL, 0, 0, 1, 0, 0, 0, 0, 1, 'User,', '[{"entityName":"User","fieldList":["userName"]}]', '{"searchDialogWidth":520,"validators":[]}'),
	(640, '0000002-31b47e956be049e7ba409db567cb4493', 46, 'deletedOn', '删除时间', 'deletedOn', 'DateTime', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', NULL, '{"validators":[]}'),
	(641, '0000002-a127654af4914824a1d7763a705546f1', 46, 'deletedWith', '删除渠道', 'deletedWith', 'Text', NULL, 0, 1, 1, 0, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":200,"validators":[]}'),
	(642, '0000002-340405f5a2e84829bfa4dc869cfd3073', 46, 'restoreBy', '恢复人', 'restoreBy', 'Reference', NULL, 0, 1, 1, 0, 0, 0, 0, 1, 'User,', '[{"entityName":"User","fieldList":["userName"]}]', '{"searchDialogWidth":520,"validators":[]}'),
	(643, '0000002-6dc79fc9b42642499ad551e73e04c164', 46, 'restoreOn', '恢复时间', 'restoreOn', 'DateTime', NULL, 0, 1, 1, 0, 0, 0, 0, 1, '', NULL, '{"validators":[]}'),
	(644, '0000002-64ca3b1cefc84b53bca84533a3117940', 46, 'detailEntityIds', '关联删除数据Id', 'detailEntityIds', 'TextArea', NULL, 0, 1, 1, 0, 0, 0, 0, 0, '', NULL, '{"minLength":0,"maxLength":1000,"rows":3,"validators":[]}'),
	(645, '0000002-1c7318220895417abe9d81dab3288f6b', 48, 'triggerConfigId', 'id主键', 'triggerConfigId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(646, '0000002-7a14c9b6e1fa444395e28511e157f28c', 48, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(647, '0000002-4b4fa75a307e490e8e17889f987a22bb', 48, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(648, '0000002-198c4df8cc4f447d9ab43704b848d63f', 48, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(649, '0000002-5f6ef182eb1147c4bd407e415c94d66c', 48, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(650, '0000002-9ec07ab805b44709aea4294ba51f2ee3', 48, 'entityCode', '实体Code', 'entityCode', 'Integer', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":40,"validators":[]}'),
	(652, '0000002-36e2fb3a0db0481286d6e034edc9b92d', 48, 'whenCron', '触发周期', 'whenCron', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', '[]', '{"minLength":0,"maxLength":20,"validators":[]}'),
	(653, '0000002-9fd64d06b40345e89c2087e2d74de22d', 48, 'actionFilter', '过滤条件', 'actionFilter', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', '[]', '{"minLength":0,"maxLength":1000,"rows":3,"validators":[]}'),
	(655, '0000002-cf18c98586964e069bacfdcfa786f194', 48, 'priority', '优先级', 'priority', 'Integer', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', '[]', '{"maxValue":999999999,"minValue":-999999999,"validators":[]}'),
	(656, '0000002-18f7af194b804e338dcced1a4fe6f338', 48, 'name', '触发器名称', 'name', 'Text', NULL, 0, 0, 1, 1, 0, 1, 0, 1, '', NULL, '{"minLength":0,"maxLength":200,"validators":[]}'),
	(657, '0000002-29c513f0a1b944aa8792ba40b93bcf0e', 48, 'isDisabled', '是否禁用', 'isDisabled', 'Boolean', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', '[]', '{"validators":[]}'),
	(661, '0000002-53667b9f20324b50b52edb64bddb84d0', 49, 'revisionHistoryId', 'id主键', 'revisionHistoryId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(668, '0000002-9d655373b3e54d8cac090276b6ca45db', 49, 'entityCode', '所属实体', 'entityCode', 'Integer', NULL, 0, 0, 0, 0, 0, 0, 0, 1, '', '[]', '{"maxValue":40,"minValue":0,"validators":[]}'),
	(669, '0000002-714bdd5bab1a4b76bdb3eb20f0b7b711', 49, 'entityId', '相关实体Id', 'entityId', 'AnyReference', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '[]', '{"minLength":0,"maxLength":40,"validators":[]}'),
	(671, '0000002-dd015a33ccba402488a1f0575a8e655b', 49, 'revisionContent', '变更数据', 'revisionContent', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', '[]', '{"minLength":0,"maxLength":1000,"rows":3,"validators":[]}'),
	(672, '0000002-57d32f172667419ebdf1baacba9cc0e4', 49, 'revisionBy', '操作人', 'revisionBy', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 1, 'User,', '[{"entityName":"User","fieldList":["userName"]}]', '{"searchDialogWidth":520,"validators":[]}'),
	(673, '0000002-4d332e18022547098de6b087fabcf15b', 49, 'revisionOn', '操作时间', 'revisionOn', 'DateTime', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{"validators":[]}'),
	(675, '0000002-4d332e18022547098de6b087fabcf77b', 49, 'revisionType', '变更类型', 'revisionType', 'Status', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, NULL),
	(677, '0000002-5100fdd965af4024a9cfd8c0bec80b88', 48, 'actionType', '触发器类型', 'actionType', 'Status', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, NULL),
	(678, '0000002-cce1ba80d9df4269a936fe6a48d7d40f', 48, 'whenNum', '触发条件', 'whenNum', 'Integer', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', '[]', '{"maxValue":999999999,"minValue":0,"validators":[]}'),
	(679, '0000002-39be7d534981431299cbf5c39f242268', 48, 'actionContent', '触发器执行内容', 'actionContent', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', '[]', '{"minLength":0,"maxLength":1000,"rows":3,"validators":[]}'),
	(697, '0000002-5669ebe6afa54821a8c36396650a0b87', 21, 'mobilePhone', '手机号', 'mobilePhone', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', '[]', '{"minLength":11,"maxLength":11,"validators":["mobile"]}'),
	(698, '0000002-f7ca7e44cdc14942ab9cb3999e3f4dc8', 21, 'email', '邮箱', 'email', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":200,"validators":["email"]}'),
	(708, '0000002-9f4eb50012194d0581f962ce7c9b3c91', 15, 'layoutConfigId', 'id主键', 'layoutConfigId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(709, '0000002-ae9c496f2b754beaa3c9b376912b2c56', 15, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(710, '0000002-2cf82635d7fa4d6ab690ca165a092c00', 15, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(711, '0000002-0b2fad3e58514a66978e44ce1b709446', 15, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(712, '0000002-749db00563c24b47b4b26a59fc8645a4', 15, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(713, '0000002-e49614c3b66f462eb284fcca17702523', 15, 'ownerUser', '所属用户', 'ownerUser', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(714, '0000002-cc46af3936a240aebfdbdbad14011b5a', 15, 'ownerDepartment', '所属部门', 'ownerDepartment', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'Department,', NULL, NULL),
	(715, '0000002-eb7ebaf3f3194d4ea8ed825454d55800', 15, 'configName', '配置名称', 'configName', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":200,"validators":[]}'),
	(716, '0000002-a20fe3f3555b4a3cbe51ac2e4cfbcbce', 15, 'config', '配置', 'config', 'TextArea', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, '{"minLength":0,"maxLength":1000,"rows":3,"validators":[]}'),
	(717, '0000002-c2f0bbec0c7f42b4b254b693c0a55c5f', 15, 'shareTo', '分享给谁', 'shareTo', 'TextArea', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', '[]', '{"minLength":0,"maxLength":1000,"rows":3,"validators":[]}'),
	(720, '0000002-fc25d5e05aef41159b30e5b769b49a2e', 15, 'applyType', '应用类型', 'applyType', 'Text', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', '[]', '{"minLength":0,"maxLength":200,"validators":[]}'),
	(721, '0000002-3db8c51c1e6b47da88c15b22628d1f51', 15, 'entityCode', '实体Code', 'entityCode', 'Integer', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', '[]', '{"maxValue":999999999,"minValue":-999999999,"validators":[]}'),
	(722, '0000002-fb0992e279f94d34b8132ca39791e64f', 50, 'shareAccessId', 'id主键', 'shareAccessId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(723, '0000002-1c604a73cacb41b3a1c184eb650c4bf8', 50, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(724, '0000002-d59d3827e0e841b78322ff14d171ccca', 50, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(725, '0000002-55e3a2f7b31f4eec966350276b277f62', 50, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(726, '0000002-128e82c66ec8408d96473cc5b1ca6e7b', 50, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(729, '0000002-8f2f6fd960e345ed8e1a3696879409de', 50, 'entityCode', '实体Code', 'entityCode', 'Integer', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '[]', '{"maxValue":999999999,"minValue":-999999999,"validators":[]}'),
	(730, '0000002-e45c2d7746e1455abf4e7ccccc48fb74', 50, 'entityId', '实体ID', 'entityId', 'Text', NULL, 0, 0, 1, 0, 0, 0, 0, 0, '', '[]', '{"minLength":0,"maxLength":40,"validators":[]}'),
	(731, '0000002-8cff27733e044a07b6ed78b5dce9f6e1', 50, 'shareTo', '分享人', 'shareTo', 'Reference', NULL, 0, 0, 1, 0, 0, 0, 0, 0, 'User,', NULL, '{"minLength":0,"maxLength":40,"validators":[]}'),
	(733, '0000002-528e7025716144458c63dc0df7283cd8', 50, 'withUpdate', '允许修改', 'withUpdate', 'Boolean', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, '{"validators":[]}'),
	(767, '0000002-ee46cbf415444423986d5a68eba92e57', 51, 'metaApiId', 'id主键', 'metaApiId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(768, '0000002-5d13b24a1fec47b0a4bbca741579b89b', 51, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(769, '0000002-c3c026fe7aac45d3a0a1675d3be3db9b', 51, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(770, '0000002-05c77d3eb009468ab3a9781e566d3643', 51, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(771, '0000002-fbbbe12d847c4e5cb18ed026e8261632', 51, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(772, '0000002-9a0515a4983d406ab57179a6edecdc93', 51, 'appId', 'APP_ID', 'appId', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{"maxValue":999999999,"minValue":0,"validators":[]}'),
	(773, '0000002-3682ee66e57247fa97321821b679d2cc', 51, 'appSecret', 'APP_SECRET', 'appSecret', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{"maxValue":40,"minValue":0,"validators":[]}'),
	(774, '0000002-d38ce48d0c784a00b082bbb1cc857679', 51, 'bindUser', '绑定用户', 'bindUser', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 1, 'User,', '[{"entityName":"User","fieldList":["userName"]}]', '{"searchDialogWidth":520,"validators":[]}'),
	(775, '0000002-19eab99a66a045cfbeecc7dab3eb7f97', 51, 'bindIps', 'IP 白名单', 'bindIps', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":300,"validators":[]}'),
	(776, '0000002-e478114b892f4ec98c837999d0b777d0', 21, 'ownerTeam', '所属团队', 'ownerTeam', 'ReferenceList', NULL, 0, 1, 1, 1, 0, 0, 0, 0, 'Team,', '', 'null'),
	(777, '0000002-6e2cbdeb9a1e445087eb0c9eb8cdf60e', 52, 'chartId', 'id主键', 'chartId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(778, '0000002-215aafd03a574ed1bc12a91f35ac2e6b', 52, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(779, '0000002-1103885a1ee74c89a75dc141d1a9eb2b', 52, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(780, '0000002-d6cd6c6e2fa549c883ea60557bc586a6', 52, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(781, '0000002-a16b1f3360b44c7a809d31bf0cb4d28f', 52, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(782, '0000002-d923e24623dc42a0b4ab77a8224b1ed2', 52, 'ownerUser', '所属用户', 'ownerUser', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(783, '0000002-75af0afffb264605a79800012a719d67', 52, 'ownerDepartment', '所属部门', 'ownerDepartment', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'Department,', NULL, NULL),
	(784, '0000002-771028e2bffc4b4fb147bb3b4e60bf8f', 52, 'chartData', '仪表盘数据', 'chartData', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, '{"minLength":0,"maxLength":100000,"rows":3,"validators":[]}'),
	(785, '0000002-2be3ab4d5557494ca2e2af9692bf636c', 52, 'chartName', '仪表盘名称', 'chartName', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":200,"validators":[]}'),
	(815, '0000002-9015f41a55eb436aa937c3f259f9c04b', 21, 'avatar', '头像', 'avatar', 'Picture', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', '[]', '{"maxFileCount":1,"fileMaxSize":5,"uploadFileTypes":[],"uploadHint":"","validators":[]}'),
	(830, '0000002-dc6653f21f4949ba8905dadd81033c6c', 52, 'defaultChart', '默认视图', 'defaultChart', 'Boolean', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{"validators":[]}'),
	(1196, '0000002-97830c60a8f14b28a0699705146434d2', 53, 'triggerConfigId', '触发器', 'triggerConfigId', 'Reference', NULL, 0, 0, 1, 0, 0, 0, 0, 1, 'TriggerConfig,', '[{"entityName":"TriggerConfig","fieldList":["name"]}]', '{"searchDialogWidth":520,"validators":[]}'),
	(1197, '0000002-e9c9e763cc2e49a681a88c11976752a5', 53, 'executeFlag', '执行结果', 'executeFlag', 'Boolean', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', NULL, '{"validators":[]}'),
	(1198, '0000002-d18419df22fe42ea87369d1c45761844', 53, 'recordId', '实体记录Id', 'recordId', 'AnyReference', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":40,"validators":[]}'),
	(1199, '0000002-7a2aa675f3fe455fbdd9c22e479f8a04', 53, 'triggerReason', '触发原因', 'triggerReason', 'Text', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":10,"validators":[]}'),
	(1200, '0000002-e9607f5ecacc47a492efd68831778f06', 53, 'actionType', '触发器类型', 'actionType', 'Status', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', NULL, NULL),
	(1201, '0000002-819d5956c8214e528cec3ac4aff43f49', 53, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(1202, '0000002-895508aa78464d9ca6cf577f79b743dd', 53, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(1203, '0000002-17d080a3fb6c44d2b8e476d6f00ea52b', 53, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(1204, '0000002-3b3d0a359a2f4301a9461fc01d792d09', 53, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(1205, '0000002-73dd871a6787495c85f7266cc2326a6d', 53, 'triggerLogId', 'id主键', 'triggerLogId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(1206, '0000002-598e9145aa024a85b2f1aeb3ddb8864f', 25, 'loginUser', '登录用户', 'loginUser', 'Reference', NULL, 0, 0, 1, 0, 0, 0, 0, 1, 'User,', '[{"entityName":"User","fieldList":["userName"]}]', '{"searchDialogWidth":520,"validators":[]}'),
	(1207, '0000002-435ebb559a2a46679f4d114738f088f1', 25, 'logoutTime', '登出时间', 'logoutTime', 'DateTime', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, '{"validators":[]}'),
	(1208, '0000002-6dee5a2c697b49c29fc53e3544d32909', 25, 'logoutType', '登出类型', 'logoutType', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":50,"validators":[]}'),
	(1209, '0000002-94537a09ccf441e9a827506ddb5e768a', 25, 'logout', '已登出', 'logout', 'Boolean', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '[]', '{"validators":[]}'),
	(1210, '0000002-f6697944e6774ff1a5c0891f3429fa06', 25, 'sessionId', 'sessionId', 'sessionId', 'Text', NULL, 0, 0, 1, 0, 0, 0, 0, 0, '', NULL, '{"minLength":0,"maxLength":32,"validators":[]}'),
	(1211, '0000002-3c0d96956a754ba792926ebd1a443e63', 25, 'browserName', '浏览器', 'browserName', 'Text', NULL, 0, 1, 1, 0, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":20,"validators":[]}'),
	(1212, '0000002-019e60f2cea84b37b576c4fa39a10dce', 25, 'ip', '登录ip', 'ip', 'Text', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":20,"validators":[]}'),
	(1213, '0000002-21efa4d3cc4c4a1999ad47a2ca64ba5e', 25, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(1214, '0000002-dea43360903b4a2e9a7b77f195b78a10', 25, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(1215, '0000002-f89571cb1f4a46e3888a0c711ca7160c', 25, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(1216, '0000002-b5b3238c96a04d5a8022cc42d35738ab', 25, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(1217, '0000002-d3e1f13a10e347a29551632c24efaa6e', 25, 'loginLogId', 'id主键', 'loginLogId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(1530, '0000002-d5b75f0a1da646d3a5523a839b6e6329', 1001, 'yanshishitiId', 'id主键', 'yanshishitiId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(1531, '0000002-c36c3a00985447b58400ac0783b71ad9', 1001, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(1532, '0000002-6e003e3cb5d84c8dae5769dc281c3d4b', 1001, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(1533, '0000002-0c91a3dbd9a64b5f8596ea44131b4105', 1001, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(1534, '0000002-a9d7d738a1ab4befaf67b1d16f8d181d', 1001, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(1535, '0000002-b1ffe557175e4e41b5edc601f86c9eeb', 1001, 'ownerUser', '所属用户', 'ownerUser', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(1536, '0000002-db173a1fc95a454a9eba3e87db655d3f', 1001, 'ownerDepartment', '所属部门', 'ownerDepartment', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'Department,', NULL, NULL),
	(1539, '0000002-edb64bcebe2b42a4ac6b85064f8dd5d5', 1001, 'mingcheng', '名称', 'c_mingcheng', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":200,"validators":[]}'),
	(1540, '0000002-79e06bc3a67f4f3cb07ffc2e13082c07', 1001, 'shuliang', '数量', 'c_shuliang', 'Integer', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"maxValue":999999999,"minValue":-999999999,"validators":[]}'),
	(1541, '0000002-5e4dabfe6e7b4c0887711f924c08f9dd', 1001, 'jine', '金额', 'c_jine', 'Money', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{"precision":2,"maxValue":999999999,"minValue":-999999999,"validators":[]}'),
	(1542, '0000002-5f05ebfa2ce14c86a286770159c5c076', 1001, 'shijian', '时间', 'c_shijian', 'Date', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{"validators":[]}'),
	(1543, '0000002-5a9fe6cda76647348841e8ff0477a47a', 1001, 'beizhu', '备注', 'c_beizhu', 'TextArea', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, '{"minLength":0,"maxLength":1000,"rows":3,"validators":[]}');

-- 导出  表 variantorm_base.t_notification 结构
CREATE TABLE IF NOT EXISTS `t_notification` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `notificationId` char(40) NOT NULL,
  `fromUser` char(40) NOT NULL,
  `toUser` char(40) NOT NULL,
  `relatedRecord` char(40) NOT NULL DEFAULT '' COMMENT '相关记录Id',
  `message` text NOT NULL,
  `type` tinyint(4) NOT NULL DEFAULT '0',
  `unread` tinyint(4) NOT NULL DEFAULT '1',
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `notificationId` (`notificationId`) USING BTREE,
  KEY `isDeleted` (`isDeleted`) USING BTREE,
  KEY `toUser` (`toUser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_notification 的数据：~0 rows (大约)

-- 导出  表 variantorm_base.t_option_item 结构
CREATE TABLE IF NOT EXISTS `t_option_item` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `optionItemId` varchar(40) NOT NULL,
  `entityName` varchar(150) NOT NULL,
  `fieldName` varchar(150) NOT NULL,
  `value` smallint(6) NOT NULL,
  `label` varchar(190) NOT NULL,
  `displayOrder` smallint(6) NOT NULL DEFAULT '0',
  `systemFlag` smallint(6) NOT NULL DEFAULT '0' COMMENT '1为系统选项，禁用修改',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`autoId`),
  UNIQUE KEY `entityName_fieldName_value` (`entityName`,`fieldName`,`value`),
  UNIQUE KEY `optionItemId` (`optionItemId`),
  KEY `entityName_fieldName` (`entityName`,`fieldName`)
) ENGINE=InnoDB AUTO_INCREMENT=455 DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_option_item 的数据：~5 rows (大约)
INSERT INTO `t_option_item` (`autoId`, `optionItemId`, `entityName`, `fieldName`, `value`, `label`, `displayOrder`, `systemFlag`, `timestamp`) VALUES
	(447, '0000003-0251be4628b64917a65312626098b47c', 'User', 'jobTitle', 4, '总监', 1, 0, '2023-10-27 02:34:15'),
	(448, '0000003-124bd539a6ec4efd8c4dc3276754e1c3', 'User', 'jobTitle', 2, '主管', 2, 0, '2023-10-27 02:34:15'),
	(449, '0000003-743b1b77b48349458190a9c4c6958c32', 'User', 'jobTitle', 3, '经理', 3, 0, '2023-10-27 02:34:15'),
	(450, '0000003-a7f320239c1145d0b125b719ade0ac7d', 'User', 'jobTitle', 5, '部长', 4, 0, '2023-10-27 02:34:15'),
	(451, '0000003-647f5ef21be6496fa6fd863d12d84955', 'User', 'jobTitle', 1, '员工', 5, 0, '2023-10-27 02:34:15');

-- 导出  表 variantorm_base.t_recycle_bin 结构
CREATE TABLE IF NOT EXISTS `t_recycle_bin` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `recycleBinId` char(40) NOT NULL,
  `entityCode` smallint(6) DEFAULT NULL,
  `entityId` varchar(200) DEFAULT NULL,
  `entityName` varchar(200) DEFAULT NULL,
  `deletedBy` char(40) DEFAULT NULL,
  `deletedOn` datetime DEFAULT NULL,
  `deletedWith` varchar(200) DEFAULT NULL,
  `restoreBy` char(40) DEFAULT NULL,
  `restoreOn` datetime DEFAULT NULL,
  `detailEntityIds` text,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `recycleBinId` (`recycleBinId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_recycle_bin 的数据：~0 rows (大约)
INSERT INTO `t_recycle_bin` (`autoId`, `recycleBinId`, `entityCode`, `entityId`, `entityName`, `deletedBy`, `deletedOn`, `deletedWith`, `restoreBy`, `restoreOn`, `detailEntityIds`) VALUES
	(1, '0000046-a8aafe7bc97e458c9b8f82b64db89344', 52, '0000052-6eb3e79d76e04d229618b0a15b599475', '0000052-6eb3e79d76e04d229618b0a15b599475', '0000021-00000000000000000000000000000001', '2023-11-02 10:28:47', '手动删除', NULL, NULL, '');

-- 导出  表 variantorm_base.t_reference_cache 结构
CREATE TABLE IF NOT EXISTS `t_reference_cache` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `cacheId` char(40) NOT NULL,
  `referenceId` char(40) NOT NULL,
  `recordLabel` varchar(190) NOT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`autoId`),
  UNIQUE KEY `referenceId` (`referenceId`),
  UNIQUE KEY `cacheId` (`cacheId`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COMMENT='所有实体主键和名称字段值缓存表。' COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_reference_cache 的数据：~2 rows (大约)
INSERT INTO `t_reference_cache` (`autoId`, `cacheId`, `referenceId`, `recordLabel`, `timestamp`) VALUES
	(8, '0000006-af46cd4ef98f49b0ad9e778e1e7224f1', '0000023-00000000000000000000000000000001', '管理员角色', '2023-07-27 06:52:23'),
	(13, '0000006-af46cd4ef98f49b0ad9e778e1e7224f2', '0000023-a2dc158262f4452c9a5da09c675ace42', 'testRole', '2023-07-31 06:48:54');

-- 导出  表 variantorm_base.t_reference_list_map 结构
CREATE TABLE IF NOT EXISTS `t_reference_list_map` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `mapId` char(40) NOT NULL,
  `entityName` varchar(150) NOT NULL,
  `fieldName` varchar(150) NOT NULL,
  `objectId` char(40) NOT NULL,
  `toId` char(40) NOT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`autoId`),
  UNIQUE KEY `mapId` (`mapId`),
  UNIQUE KEY `entityName_fieldName_objectId_toId` (`entityName`,`fieldName`,`objectId`,`toId`),
  KEY `objectId` (`objectId`),
  KEY `to` (`toId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=841 DEFAULT CHARSET=utf8mb4 COMMENT='多对多引用字段中间表' COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_reference_list_map 的数据：~62 rows (大约)
INSERT INTO `t_reference_list_map` (`autoId`, `mapId`, `entityName`, `fieldName`, `objectId`, `toId`, `timestamp`) VALUES
	(348, '0000005-bb6eda4fe1b446f9967a426faf096013', 'ApprovalTask', 'ccTo', '0000033-264052f790754ca78bd259542017d84d', '0000021-00000000000000000000000000000001', '2023-07-27 08:05:33'),
	(352, '0000005-93f52fca528343c594ea270cbbe006f8', 'ApprovalTask', 'ccTo', '0000033-c62096e8505c4fe29ca8f37c02a9cd9c', '0000021-00000000000000000000000000000001', '2023-07-27 08:07:24'),
	(356, '0000005-11995fb5607645e5aaa76f84335bc0dd', 'ApprovalHistory', 'currentCCTo', '0000032-4232bed5890e4feea9ffcd2674bee0bc', '0000021-00000000000000000000000000000001', '2023-07-27 08:12:51'),
	(357, '0000005-4ba01f0a57ff4964b812beec68dff293', 'ApprovalTask', 'ccTo', '0000033-60fab5f43e094b0cbfbd04eb1269dc00', '0000021-00000000000000000000000000000001', '2023-07-27 08:12:52'),
	(361, '0000005-e0160007322e4596b2a61c6cf6deb071', 'ApprovalHistory', 'currentCCTo', '0000032-8da36ce29e1c449491fbdaf584347dd8', '0000021-00000000000000000000000000000001', '2023-07-27 08:19:31'),
	(365, '0000005-a0ee0416669642d3b00049c223fcae38', 'ApprovalTask', 'ccTo', '0000033-ab8ee7c575924a91abf48b1b4b5a3373', '0000021-00000000000000000000000000000001', '2023-07-27 08:20:15'),
	(369, '0000005-99e5a0f413d449e7906aa7a083b40ce7', 'ApprovalHistory', 'currentCCTo', '0000032-cf21477cdce54be092e3094ac48c5370', '0000021-00000000000000000000000000000001', '2023-07-27 08:25:14'),
	(375, '0000005-0d5ae69fe7d54435b73a72e468fa5222', 'ApprovalTask', 'ccTo', '0000033-37e7883bbf0a40bab8462082e9780747', '0000021-00000000000000000000000000000001', '2023-07-27 08:33:05'),
	(376, '0000005-88e4ed69174249fdbd11bee2ac25ab21', 'ApprovalTask', 'ccTo', '0000033-37e7883bbf0a40bab8462082e9780747', '0000021-1a7fc78f071e400fb780629fc137e223', '2023-07-27 08:33:05'),
	(379, '0000005-c34ae2e05daa4571a7211a9f9d6cba4a', 'ApprovalTask', 'ccTo', '0000033-117610871911426f8c583156c79bb54e', '0000021-00000000000000000000000000000001', '2023-07-31 06:46:30'),
	(380, '0000005-9f43522520cc4208a0178903cebd22a6', 'ApprovalTask', 'ccTo', '0000033-117610871911426f8c583156c79bb54e', '0000021-1a7fc78f071e400fb780629fc137e223', '2023-07-27 08:37:44'),
	(397, '0000005-d714644380814fc7a2f0f84378d53e43', 'ApprovalTask', 'ccTo', '0000033-c7cc867032134208acaab3195b4006cf', '0000021-00000000000000000000000000000001', '2023-07-31 08:15:42'),
	(398, '0000005-872dfad839974c1c9afba82e6b2ee302', 'ApprovalTask', 'ccTo', '0000033-c7cc867032134208acaab3195b4006cf', '0000021-1a7fc78f071e400fb780629fc137e223', '2023-07-31 08:15:42'),
	(416, '0000005-1c700dc5736d49e9b7ba92acf37cea57', 'ApprovalHistory', 'currentCCTo', '0000032-dc0790e3bb2d4921947c99f381990132', '0000021-1a7fc78f071e400fb780629fc137e223', '2023-07-31 09:11:07'),
	(421, '0000005-59306acee14048688a47f18537cf10ad', 'ApprovalTask', 'ccTo', '0000033-4ad5fed799a1405982a4b289572f1916', '0000021-00000000000000000000000000000001', '2023-07-31 09:13:46'),
	(422, '0000005-9ec7a5abe45243648d2274677c694b69', 'ApprovalTask', 'ccTo', '0000033-4ad5fed799a1405982a4b289572f1916', '0000021-1a7fc78f071e400fb780629fc137e223', '2023-07-31 09:13:46'),
	(428, '0000005-5449953b00624d54b10832c8e7462164', 'ApprovalTask', 'ccTo', '0000033-5bd3d9fc087942f897eab4c81e6c724f', '0000021-00000000000000000000000000000001', '2023-07-31 09:46:54'),
	(429, '0000005-a56fe5c86790433abe2846f025a79140', 'ApprovalTask', 'ccTo', '0000033-5bd3d9fc087942f897eab4c81e6c724f', '0000021-1a7fc78f071e400fb780629fc137e223', '2023-07-31 09:46:54'),
	(441, '0000005-ee082043255b4f0e8c7612c53e2c4e00', 'ApprovalTask', 'ccTo', '0000033-bd17f254aad34c899e4b3b444ccd0ab2', '0000021-00000000000000000000000000000001', '2023-08-01 01:35:50'),
	(442, '0000005-165150eaada34b91a7df6302a8a06015', 'ApprovalTask', 'ccTo', '0000033-bd17f254aad34c899e4b3b444ccd0ab2', '0000021-1a7fc78f071e400fb780629fc137e223', '2023-08-01 01:35:50'),
	(502, '0000005-bf071d3f7eab4015a5f5150c9319d357', 'ApprovalTask', 'ccTo', '0000033-060c7c66f9084adbb97c8ae6cbd19a18', '0000021-00000000000000000000000000000001', '2023-08-07 06:07:26'),
	(503, '0000005-82db3d2614604024982107bb2e857a43', 'ApprovalTask', 'ccTo', '0000033-060c7c66f9084adbb97c8ae6cbd19a18', '0000021-1a7fc78f071e400fb780629fc137e223', '2023-08-07 06:07:26'),
	(604, '0000005-2dc8b8eb898142eebafece645eb82a4e', 'ApprovalTask', 'ccTo', '0000033-329a07dc6efa4bf5b5ce3603db0214ef', '0000021-00000000000000000000000000000001', '2023-10-09 07:01:02'),
	(605, '0000005-782d614824124ebd8f1fee1ec6544eec', 'ApprovalTask', 'ccTo', '0000033-329a07dc6efa4bf5b5ce3603db0214ef', '0000021-1a7fc78f071e400fb780629fc137e223', '2023-10-09 07:01:02'),
	(705, '0000005-f064d8313a3145a6937928ee325a6ba8', 'ApprovalTask', 'ccTo', '0000033-cd3de161067f43c7b7022ebfc1da2ae7', '0000021-00000000000000000000000000000001', '2023-10-12 08:26:58'),
	(706, '0000005-79972ad41143476d8004a795aaa4a7c7', 'ApprovalTask', 'ccTo', '0000033-cd3de161067f43c7b7022ebfc1da2ae7', '0000021-1a7fc78f071e400fb780629fc137e223', '2023-10-12 08:26:58'),
	(718, '0000005-a424e7b870eb4f119686ddc02243ecca', 'ApprovalTask', 'ccTo', '0000033-06d5de1a0758445e935366cedb5d1b08', '0000021-00000000000000000000000000000001', '2023-10-13 06:24:45'),
	(719, '0000005-b9c15f991527423b9603f5fd7802507d', 'ApprovalTask', 'ccTo', '0000033-06d5de1a0758445e935366cedb5d1b08', '0000021-1a7fc78f071e400fb780629fc137e223', '2023-10-13 06:24:45'),
	(737, '0000005-064534fbd9ae49799c0be83b1ec327cf', 'User', 'ownerTeam', '0000021-39cc4e44a3f14b3e93bb7a0cd0cf796f', '0000024-4282ab63811645c483d1bff3318a013a', '2023-10-13 08:55:08'),
	(738, '0000005-3b5b2936b46f451cb94d75293c267b68', 'User', 'ownerTeam', '0000021-39cc4e44a3f14b3e93bb7a0cd0cf796f', '0000024-9ba150800cfa4f7fa5d2c66b9177640e', '2023-10-13 08:55:08'),
	(739, '0000005-5458f796daa24abf9f38ed9559ec8c78', 'User', 'ownerTeam', '0000021-1a7fc78f071e400fb780629fc137e223', '0000024-4282ab63811645c483d1bff3318a013a', '2023-10-13 08:55:08'),
	(740, '0000005-91b0992c75c348d0908a4e0294b5750a', 'User', 'ownerTeam', '0000021-1a7fc78f071e400fb780629fc137e223', '0000024-9ba150800cfa4f7fa5d2c66b9177640e', '2023-10-13 08:55:08'),
	(741, '0000005-32782a01c5274053b0fcbebf6eff35e2', 'User', 'ownerTeam', '0000021-1a7fc78f071e400fb780629fc137e223', '0000024-4f7cba893f494d699c519b19177b726a', '2023-10-13 08:55:08'),
	(742, '0000005-15972d5c11d348f6ad381d55a130cebb', 'User', 'ownerTeam', '0000021-00000000000000000000000000000001', '0000024-4282ab63811645c483d1bff3318a013a', '2023-10-13 09:02:09'),
	(743, '0000005-b47df645df65419e8fac52f0298079e8', 'User', 'ownerTeam', '0000021-00000000000000000000000000000001', '0000024-4f7cba893f494d699c519b19177b726a', '2023-10-13 09:02:09'),
	(745, '0000005-6bbb59b65e7c4f2295d2f804af5bfdca', 'User', 'roles', '0000021-1a7fc78f071e400fb780629fc137e223', '0000023-00000000000000000000000000000001', '2023-10-13 10:03:19'),
	(746, '0000005-38e8e1aa601845dcb4aa66efd338e768', 'User', 'roles', '0000021-1a7fc78f071e400fb780629fc137e223', '0000023-a2dc158262f4452c9a5da09c675ace41', '2023-10-13 10:03:19'),
	(747, '0000005-b47df645df65419e8fac52f0298011e8', 'User', 'roles', '0000021-00000000000000000000000000000001', '0000023-00000000000000000000000000000001', '2023-10-13 10:30:30'),
	(758, '0000005-0ebdff753a784ef6adccc6ba98ba2d01', 'User', 'roles', '0000021-39cc4e44a3f14b3e93bb7a0cd0cf796f', '0000023-00000000000000000000000000000001', '2023-10-13 10:51:13'),
	(763, '0000005-d08b05af63f840fab891e95eacbaf9f7', 'ApprovalTask', 'ccTo', '0000033-76c02c2721d841daa137f9e387625098', '0000021-00000000000000000000000000000001', '2023-10-16 01:28:33'),
	(764, '0000005-00fdf9a865554bf69bacc8cd3f6afe3c', 'ApprovalTask', 'ccTo', '0000033-76c02c2721d841daa137f9e387625098', '0000021-1a7fc78f071e400fb780629fc137e223', '2023-10-16 01:28:33'),
	(774, '0000005-0b6628d0f4684ad3adf024f0be966a75', 'ApprovalTask', 'ccTo', '0000033-bd1355b1b1c54133a36041097fc53f30', '0000021-00000000000000000000000000000001', '2023-10-16 02:05:41'),
	(775, '0000005-a7ed72f972754482b6458089eaa608a9', 'ApprovalTask', 'ccTo', '0000033-bd1355b1b1c54133a36041097fc53f30', '0000021-1a7fc78f071e400fb780629fc137e223', '2023-10-16 02:05:41'),
	(784, '0000005-f8b21a1edbbb4b4fa283bee7f0cd8d8b', 'User', 'roles', '0000021-9ea089d2bc974803a0d359d434b524c3', '0000023-a2dc158262f4452c9a5da09c675ace41', '2023-10-16 02:39:43'),
	(785, '0000005-aac928a45f034356a5600fdffeb4d38e', 'User', 'ownerTeam', '0000021-086b39310631473289fb716cf2e7835e', '0000024-5c5667611a0649588a38343a9f4b3021', '2023-10-16 03:53:29'),
	(801, '0000005-a1747b138ed042698c76c8255254aafa', 'ApprovalTask', 'approver', '0000033-52b2d27de1c54c49bfb71e2b92ec9b39', '0000021-086b39310631473289fb716cf2e7835e', '2023-10-19 06:32:04'),
	(813, '0000005-b54ce4d2b2e043719aa2965b0885fbad', 'ApprovalTask', 'ccTo', '0000033-a1787e17fbf5498699515e0a7ff92afa', '0000021-086b39310631473289fb716cf2e7835e', '2023-10-19 08:23:59'),
	(814, '0000005-4870d153320d42e2b2c3e2f67c8fb91f', 'ApprovalTask', 'ccTo', '0000033-a1787e17fbf5498699515e0a7ff92afa', '0000021-39cc4e44a3f14b3e93bb7a0cd0cf796f', '2023-10-19 08:23:59'),
	(815, '0000005-5dec12c52ee04c34a0426ace6d45170c', 'ApprovalTask', 'ccTo', '0000033-a1787e17fbf5498699515e0a7ff92afa', '0000021-fb866afd5bf94dd086401ba480e7ebc5', '2023-10-19 08:23:59'),
	(824, '0000005-eeea8a919b75472ea66cd8fa52b490a3', 'ApprovalTask', 'ccTo', '0000033-0e8c7726ac0640aea2a4078070c051ac', '0000021-39cc4e44a3f14b3e93bb7a0cd0cf796f', '2023-10-19 08:29:44'),
	(825, '0000005-fdc226e8f17d4bb8a3970befa87a8984', 'ApprovalTask', 'ccTo', '0000033-0e8c7726ac0640aea2a4078070c051ac', '0000021-fb866afd5bf94dd086401ba480e7ebc5', '2023-10-19 08:29:44'),
	(826, '0000005-f21d9eb9a3754a4fa2d378cdca216933', 'ApprovalTask', 'ccTo', '0000033-0e8c7726ac0640aea2a4078070c051ac', '0000021-086b39310631473289fb716cf2e7835e', '2023-10-19 08:29:44'),
	(827, '0000005-0b6438ac948943f291b419d20a8bcfea', 'User', 'roles', '0000021-086b39310631473289fb716cf2e7835e', '0000023-00000000000000000000000000000001', '2023-10-23 07:13:10'),
	(832, '0000005-ae546206aba04e98b74331c97afb3877', 'ApprovalTask', 'approver', '0000033-03778907ee814cf59c79c1625e180029', '0000021-00000000000000000000000000000001', '2023-10-23 07:15:24'),
	(833, '0000005-cc516acd1aa7484482b1c1d8ea8e9b9e', 'ApprovalTask', 'approver', '0000033-03778907ee814cf59c79c1625e180029', '0000021-39cc4e44a3f14b3e93bb7a0cd0cf796f', '2023-10-23 07:15:24'),
	(834, '0000005-e533e664350e43a0a437477c3ede3474', 'ApprovalTask', 'approver', '0000033-03778907ee814cf59c79c1625e180029', '0000021-fb866afd5bf94dd086401ba480e7ebc5', '2023-10-23 07:15:24'),
	(835, '0000005-509c2230dfeb46d38b6d5614e28342b0', 'ApprovalTask', 'approver', '0000033-03778907ee814cf59c79c1625e180029', '0000021-086b39310631473289fb716cf2e7835e', '2023-10-23 07:15:24'),
	(836, '0000005-30cdffa88f854260b5a3031ee025b0dc', 'ApprovalTask', 'approver', '0000033-03778907ee814cf59c79c1625e180029', '0000021-1a7fc78f071e400fb780629fc137e223', '2023-10-23 07:15:24'),
	(837, '0000005-7abd54ab2609432d80ea0600bf4abc85', 'ApprovalTask', 'approver', '0000033-23e45d1209a740a9ada0b54f69efee69', '0000021-00000000000000000000000000000001', '2023-10-26 07:22:06'),
	(838, '0000005-5f8da83a87c14aaf80282dcb8d015d09', 'ApprovalTask', 'approver', '0000033-23e45d1209a740a9ada0b54f69efee69', '0000021-39cc4e44a3f14b3e93bb7a0cd0cf796f', '2023-10-26 07:22:06'),
	(839, '0000005-db4163e1155846448c0eacb0e31598f6', 'ApprovalTask', 'approver', '0000033-23e45d1209a740a9ada0b54f69efee69', '0000021-086b39310631473289fb716cf2e7835e', '2023-10-26 07:22:06'),
	(840, '0000005-a33e50d70fcc4fd0bdf7aea7e4a70d91', 'ApprovalTask', 'approver', '0000033-23e45d1209a740a9ada0b54f69efee69', '0000021-1a7fc78f071e400fb780629fc137e223', '2023-10-26 07:22:06');

-- 导出  表 variantorm_base.t_report_config 结构
CREATE TABLE IF NOT EXISTS `t_report_config` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `reportConfigId` char(40) NOT NULL,
  `entityCode` int(11) DEFAULT NULL,
  `reportName` varchar(200) DEFAULT NULL,
  `reportJson` text,
  `modifiedBy` char(40) DEFAULT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `createdBy` char(40) NOT NULL,
  `createdOn` datetime NOT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  `isDisabled` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `reportConfigId` (`reportConfigId`) USING BTREE,
  KEY `isDeleted` (`isDeleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_report_config 的数据：~0 rows (大约)

-- 导出  表 variantorm_base.t_revision_history 结构
CREATE TABLE IF NOT EXISTS `t_revision_history` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `revisionHistoryId` char(40) NOT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  `entityCode` int(11) DEFAULT NULL,
  `entityId` varchar(40) DEFAULT NULL,
  `revisionType` smallint(10) DEFAULT NULL,
  `revisionContent` json DEFAULT NULL,
  `revisionBy` char(40) DEFAULT NULL,
  `revisionOn` datetime DEFAULT NULL,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `revisionHistoryId` (`revisionHistoryId`) USING BTREE,
  KEY `isDeleted` (`isDeleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_revision_history 的数据：~0 rows (大约)

-- 导出  表 variantorm_base.t_role 结构
CREATE TABLE IF NOT EXISTS `t_role` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `roleId` char(40) NOT NULL,
  `roleName` varchar(190) DEFAULT NULL,
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  `description` text,
  `disabled` tinyint(4) DEFAULT NULL,
  `rightJson` text,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `roleId` (`roleId`) USING BTREE,
  KEY `isDeleted` (`isDeleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_role 的数据：~1 rows (大约)
INSERT INTO `t_role` (`autoId`, `roleId`, `roleName`, `createdOn`, `createdBy`, `modifiedOn`, `modifiedBy`, `isDeleted`, `description`, `disabled`, `rightJson`) VALUES
	(1, '0000023-00000000000000000000000000000001', '管理员角色', '2021-02-14 18:09:52', '0000021-00000000000000000000000000000001', '2021-02-14 18:10:05', '0000021-00000000000000000000000000000001', 0, '管理员权限角色，拥有全部权限', 0, NULL);

-- 导出  表 variantorm_base.t_router_menu 结构
CREATE TABLE IF NOT EXISTS `t_router_menu` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `routerMenuId` char(40) NOT NULL,
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  `menuJson` text,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `routerMenuId` (`routerMenuId`) USING BTREE,
  KEY `isDeleted` (`isDeleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_router_menu 的数据：~0 rows (大约)

-- 导出  表 variantorm_base.t_share_access 结构
CREATE TABLE IF NOT EXISTS `t_share_access` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `shareAccessId` char(40) NOT NULL,
  `entityCode` int(11) DEFAULT NULL,
  `entityId` char(40) DEFAULT NULL,
  `shareTo` char(40) DEFAULT NULL,
  `withUpdate` tinyint(4) DEFAULT '0',
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `shareAccessId` (`shareAccessId`) USING BTREE,
  KEY `shareTo` (`shareTo`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_share_access 的数据：~0 rows (大约)

-- 导出  表 variantorm_base.t_status_item 结构
CREATE TABLE IF NOT EXISTS `t_status_item` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `statusItemId` varchar(40) NOT NULL COMMENT 'id',
  `fieldName` varchar(150) NOT NULL COMMENT '状态字段名称',
  `value` smallint(6) NOT NULL COMMENT '状态值',
  `label` varchar(190) NOT NULL COMMENT '状态label',
  `displayOrder` smallint(6) NOT NULL DEFAULT '0' COMMENT '显示顺序',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`autoId`),
  UNIQUE KEY `statusItemId` (`statusItemId`),
  UNIQUE KEY `fieldName_value` (`fieldName`,`value`),
  KEY `fieldName` (`fieldName`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COMMENT='系统状态选项表' COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_status_item 的数据：~25 rows (大约)
INSERT INTO `t_status_item` (`autoId`, `statusItemId`, `fieldName`, `value`, `label`, `displayOrder`, `timestamp`) VALUES
	(1, '0000012-df90b6acc8ec4bf7b4c6243c26656050', 'approvalStatus', 0, '未提交', 1, '2023-07-20 03:53:08'),
	(2, '0000012-97a7b727617c4d8597802759e9d761c8', 'approvalStatus', 1, '审核中', 2, '2023-07-20 03:53:09'),
	(3, '0000012-c639cfd349a74eea93e7ba9b8294b027', 'approvalStatus', 2, '已驳回', 3, '2023-07-20 03:53:10'),
	(4, '0000012-bc1bd35feacf417eba125de940c1486a', 'approvalStatus', 3, '已完成', 4, '2023-07-20 03:53:11'),
	(5, '0000012-bc1bd35feacf417qwe125de940c1486a', 'approvalStatus', 4, '已撤销', 5, '2023-07-20 03:53:11'),
	(7, '0000012-5b69f90a57dc434092e318daeddca3de', 'actionType', 1, '字段更新', 1, '2023-08-08 03:58:57'),
	(8, '0000012-25bffc02448b44d1a33c0e0a1998b910', 'actionType', 2, '字段聚合', 2, '2023-08-08 03:58:58'),
	(9, '0000012-1d7ef447d1eb4dcbb54b08e1e6778d2b', 'actionType', 3, '分组聚合', 3, '2023-08-08 03:58:58'),
	(10, '0000012-739f036191844ae09555f738510fe2ad', 'actionType', 4, '数据校验', 4, '2023-08-08 03:58:58'),
	(11, '0000012-420739cc876e4845bddb0f3d9adc9c05', 'actionType', 5, '发送通知', 5, '2023-08-08 03:58:58'),
	(12, '0000012-7738667ca28b46479fc9bfadc318ffe1', 'actionType', 6, '自动审批', 6, '2023-08-08 03:58:58'),
	(13, '0000012-e056231fcaf64947aaa480bcb05bc3b7', 'actionType', 7, '自动撤销审批', 7, '2023-08-08 03:58:58'),
	(14, '0000012-5780d0ee0be740f983066f3e277451f8', 'actionType', 8, '自动分配', 8, '2023-08-08 03:58:58'),
	(15, '0000012-c967a9fe1fb74cffa2da1eba2e406e23', 'actionType', 9, '自动共享', 9, '2023-08-08 03:58:58'),
	(16, '0000012-2d34f2659e7649d1b0cc39154bbc9f20', 'actionType', 10, '自动取消共享', 10, '2023-08-08 03:58:58'),
	(17, '0000012-605cdf1e22bd4645a68262e677592b63', 'actionType', 11, '自动记录转换', 11, '2023-08-08 03:58:58'),
	(18, '0000012-2e128348f37a43baa0c72086c259940a', 'actionType', 12, '自动删除', 12, '2023-08-08 03:58:58'),
	(19, '0000012-b304bb2d985049538f37f94b30562f16', 'actionType', 13, '新建动态', 13, '2023-08-08 03:58:58'),
	(20, '0000012-5c91a5c46e9142908c11529e995fffc8', 'actionType', 14, '回调', 14, '2023-09-25 05:52:30'),
	(21, '0000012-36f6fe969d3e41f1a8e9bdc471240cce', 'revisionType', 1, '数据更新', 1, '2023-09-22 02:53:12'),
	(22, '0000012-6f43f08dae5c40dfa5f0cff110b30281', 'revisionType', 2, '审批通过', 2, '2023-09-22 02:53:12'),
	(23, '0000012-b6b0095fb48b4ef8b7a1964bd2f35252', 'revisionType', 3, '审批驳回', 3, '2023-09-22 02:53:12'),
	(24, '0000012-e5130570e78541dd93e8d20a37e1fba4', 'revisionType', 4, '审批撤销', 4, '2023-09-22 02:53:12'),
	(25, '0000012-cd8c020492554087957bba9dd9d4112f', 'revisionType', 5, '分配', 5, '2023-09-22 02:53:12'),
	(26, '0000012-5c91a5c46e9142908c11529e995fffc9', 'actionType', 15, '自动创建', 15, '2023-09-25 05:52:30');

-- 导出  表 variantorm_base.t_system_setting 结构
CREATE TABLE IF NOT EXISTS `t_system_setting` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `systemSettingId` char(40) NOT NULL,
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `settingName` varchar(190) DEFAULT NULL,
  `settingValue` text,
  `defaultValue` text,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `systemSettingId` (`systemSettingId`) USING BTREE,
  UNIQUE KEY `settingName` (`settingName`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_system_setting 的数据：~14 rows (大约)
INSERT INTO `t_system_setting` (`autoId`, `systemSettingId`, `createdOn`, `createdBy`, `modifiedOn`, `modifiedBy`, `settingName`, `settingValue`, `defaultValue`) VALUES
	(1, '0000007-ac681aea734111ebb21e1cbfc037aa76', '2021-02-20 14:04:23', '0000021-00000000000000000000000000000001', '2023-10-16 15:31:36', '0000021-00000000000000000000000000000001', 'dbVersion', '1.0', '1.0'),
	(3, '0000007-457431d4758811ebb21e1cbfc037aa76', '2021-02-20 14:04:23', '0000021-00000000000000000000000000000001', NULL, NULL, 'dateFormat', 'yyyy-MM-dd', 'yyyy-MM-dd'),
	(6, '0000007-50ece946758811ebb21e1cbfc037aa76', '2021-02-20 14:04:23', '0000021-00000000000000000000000000000001', NULL, NULL, 'dateTimeFormat', 'yyyy-MM-dd HH:mm:ss', 'yyyy-MM-dd HH:mm:ss'),
	(8, '0000007-50ece946758811ebb21e1cbfc037aa71', '2023-08-17 09:33:53', '0000021-00000000000000000000000000000001', '2023-10-26 13:37:29', '0000021-00000000000000000000000000000001', 'sms', '{"openStatus":false,"appId":null,"appKey":null,"signature":null}', '{"openStatus":false}'),
	(9, '0000007-50ece946758811ebb21e1cbfc037aa72', '2023-08-17 09:33:53', '0000021-00000000000000000000000000000001', '2023-10-26 13:37:29', '0000021-00000000000000000000000000000001', 'email', '{"openStatus":false,"appId":null,"appKey":null,"from":null,"fromName":null,"cc":null}', '{"openStatus":false}'),
	(10, '0000007-50ece946758811ebb21e1cbfc037aa73', '2023-08-17 09:33:53', '0000021-00000000000000000000000000000001', '2023-10-16 14:34:49', '0000021-00000000000000000000000000000001', 'appName', '美乐低代码-极速开发', '美乐低代码'),
	(11, '0000007-50ece946758811ebb21e1cbfc037aa74', '2023-08-17 09:33:53', '0000021-00000000000000000000000000000001', '2023-10-16 14:36:25', '0000021-00000000000000000000000000000001', 'pageFooter', '上海极昇数科数字科技有限公司提供技术支持', '上海极昇数科数字科技有限公司提供技术支持'),
	(13, '0000007-50ece946758811ebb21e1cbfc037aa75', '2023-08-17 09:33:53', '0000021-00000000000000000000000000000001', '2023-10-17 09:14:27', '0000021-00000000000000000000000000000001', 'logo', '', ''),
	(16, '0000007-50ece946758811ebb21e1cbfc037aa77', '2023-08-17 09:33:53', '0000021-00000000000000000000000000000001', NULL, NULL, 'logoWhite', '', ''),
	(17, '0000007-50ece946758811ebb21e1cbfc037aa78', '2023-08-17 09:33:53', '0000021-00000000000000000000000000000001', '2023-10-26 13:37:37', '0000021-00000000000000000000000000000001', 'themeColor', '#5D33F6', '#5D33F6'),
	(19, '0000007-50ece946758811ebb21e1cbfc037aa79', '2023-08-17 09:33:53', '0000021-00000000000000000000000000000001', '2023-10-26 15:05:19', '0000021-00000000000000000000000000000001', 'watermark', 'false', 'false'),
	(20, '0000007-50ece946758811ebb21e1cbfc037aa80', '2023-08-17 09:33:53', '0000021-00000000000000000000000000000001', '2023-10-16 14:35:02', '0000021-00000000000000000000000000000001', 'appTitle', '极昇美乐低代码', '磐石美乐低代码'),
	(22, '0000007-50ece946758811ebb21e1cbfc037aa81', '2023-08-17 09:33:53', '0000021-00000000000000000000000000000001', NULL, NULL, 'appSubtitle', '快速搭建/功能丰富', '快速搭建/功能丰富'),
	(23, '0000007-50ece946758811ebb21e1cbfc037aa82', '2023-08-17 09:33:53', '0000021-00000000000000000000000000000001', '2023-10-16 14:35:52', '0000021-00000000000000000000000000000001', 'appIntro', '美乐低代码系统是一种创新的开发平台，旨在帮助企业快速构建应用程序，完成信息化建设，降低开发成本。它提供了一套直观易用的可视化开发工具和拖拽式组件，使开发人员能够以简单的方式创建应用程序的用户界面和业务流程。', '美乐低代码简介-美乐低代码简介-美乐低代码简介-美乐低代码简介-美乐低代码简介'),
	(27, '0000007-50ece946758811ebb21e1cbfc037aa83', '2023-11-01 15:16:52', '0000021-00000000000000000000000000000001', '2023-11-01 15:16:55', '0000021-00000000000000000000000000000001', 'sqlVersion', '0.1.1', '0.1.1');

-- 导出  表 variantorm_base.t_tag_item 结构
CREATE TABLE IF NOT EXISTS `t_tag_item` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `tagItemId` char(40) NOT NULL,
  `entityName` varchar(150) NOT NULL,
  `fieldName` varchar(150) NOT NULL,
  `value` varchar(190) NOT NULL,
  `displayOrder` smallint(6) NOT NULL DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`autoId`),
  UNIQUE KEY `entityName_fieldName_value` (`entityName`,`fieldName`,`value`),
  UNIQUE KEY `tagItemId` (`tagItemId`),
  KEY `entityName_fieldName` (`entityName`,`fieldName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_tag_item 的数据：~0 rows (大约)

-- 导出  表 variantorm_base.t_team 结构
CREATE TABLE IF NOT EXISTS `t_team` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `teamId` char(40) NOT NULL COMMENT '主键ID',
  `teamName` varchar(190) DEFAULT NULL COMMENT '团队名称',
  `principalId` char(40) DEFAULT NULL COMMENT '负责人ID',
  `disabled` tinyint(4) DEFAULT '0' COMMENT '是否禁用',
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `ownerUser` char(40) NOT NULL,
  `ownerDepartment` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`autoId`),
  UNIQUE KEY `teamId` (`teamId`),
  KEY `ownerUser` (`ownerUser`),
  KEY `ownerDepartment` (`ownerDepartment`),
  KEY `isDeleted` (`isDeleted`),
  KEY `disabled` (`disabled`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COMMENT='团队表' COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_team 的数据：~1 rows (大约)
INSERT INTO `t_team` (`autoId`, `teamId`, `teamName`, `principalId`, `disabled`, `createdOn`, `createdBy`, `ownerUser`, `ownerDepartment`, `modifiedOn`, `modifiedBy`, `isDeleted`) VALUES
	(1, '0000024-4282ab63811645c483d1bff3318a013a', '总部团队', '0000021-00000000000000000000000000000001', 1, '2023-09-22 16:46:56', '0000021-00000000000000000000000000000001', '0000021-00000000000000000000000000000001', '0000022-00000000000000000000000000000001', '2023-10-11 15:02:23', '0000021-00000000000000000000000000000001', 0);

-- 导出  表 variantorm_base.t_trigger_config 结构
CREATE TABLE IF NOT EXISTS `t_trigger_config` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `triggerConfigId` char(40) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `entityCode` int(11) DEFAULT NULL,
  `whenNum` int(11) DEFAULT NULL,
  `whenCron` varchar(200) DEFAULT NULL,
  `actionType` smallint(6) DEFAULT NULL,
  `actionFilter` text,
  `actionContent` text,
  `priority` int(11) DEFAULT NULL,
  `isDisabled` tinyint(4) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  `modifiedBy` char(40) DEFAULT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `createdBy` char(40) NOT NULL,
  `createdOn` datetime NOT NULL,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `triggerConfigId` (`triggerConfigId`) USING BTREE,
  KEY `isDeleted` (`isDeleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_trigger_config 的数据：~0 rows (大约)

-- 导出  表 variantorm_base.t_trigger_log 结构
CREATE TABLE IF NOT EXISTS `t_trigger_log` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `triggerLogId` char(40) NOT NULL,
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  `actionType` smallint(6) DEFAULT NULL,
  `triggerReason` varchar(200) DEFAULT NULL,
  `recordId` varchar(200) DEFAULT NULL,
  `executeFlag` tinyint(4) DEFAULT NULL,
  `triggerConfigId` char(40) DEFAULT NULL,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `triggerExecuteLogId` (`triggerLogId`) USING BTREE,
  KEY `isDeleted` (`isDeleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_trigger_log 的数据：~0 rows (大约)

-- 导出  表 variantorm_base.t_user 结构
CREATE TABLE IF NOT EXISTS `t_user` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `userId` char(40) NOT NULL,
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `ownerUser` char(40) NOT NULL,
  `ownerDepartment` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  `departmentId` char(40) DEFAULT NULL,
  `userName` varchar(190) DEFAULT NULL,
  `loginPwd` varchar(300) DEFAULT NULL,
  `loginName` varchar(190) DEFAULT NULL,
  `jobTitle` smallint(6) DEFAULT NULL,
  `disabled` tinyint(4) DEFAULT NULL,
  `mobilePhone` varchar(200) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `avatar` text,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `userId` (`userId`) USING BTREE,
  KEY `ownerUser` (`ownerUser`) USING BTREE,
  KEY `ownerDepartment` (`ownerDepartment`) USING BTREE,
  KEY `isDeleted` (`isDeleted`),
  KEY `disabled` (`disabled`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_user 的数据：~1 rows (大约)
INSERT INTO `t_user` (`autoId`, `userId`, `createdOn`, `createdBy`, `ownerUser`, `ownerDepartment`, `modifiedOn`, `modifiedBy`, `isDeleted`, `departmentId`, `userName`, `loginPwd`, `loginName`, `jobTitle`, `disabled`, `mobilePhone`, `email`, `avatar`) VALUES
	(1, '0000021-00000000000000000000000000000001', '2020-08-24 14:02:44', '0000021-00000000000000000000000000000001', '0000021-00000000000000000000000000000001', '0000022-00000000000000000000000000000001', '2023-11-02 10:26:40', '0000021-00000000000000000000000000000001', 0, '0000022-00000000000000000000000000000001', '系统管理员', 'ddab1f734f6cad338ece9e07785d6a6b', 'admin', 1, 0, '15215478481', '', null);

-- 导出  表 variantorm_base.t_yanshishiti 结构
CREATE TABLE IF NOT EXISTS `t_yanshishiti` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `yanshishitiId` char(40) NOT NULL,
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `ownerUser` char(40) NOT NULL,
  `ownerDepartment` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  `c_mingcheng` varchar(200) DEFAULT NULL,
  `c_shuliang` int(11) DEFAULT NULL,
  `c_jine` decimal(18,6) DEFAULT NULL,
  `c_shijian` date DEFAULT NULL,
  `c_beizhu` text,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `yanshishitiId` (`yanshishitiId`) USING BTREE,
  KEY `ownerUser` (`ownerUser`) USING BTREE,
  KEY `ownerDepartment` (`ownerDepartment`) USING BTREE,
  KEY `isDeleted` (`isDeleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE='utf8mb4_general_ci';

-- 正在导出表  variantorm_base.t_yanshishiti 的数据：~0 rows (大约)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
