-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.30-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  11.1.0.6116
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 variantorm 的数据库结构
CREATE DATABASE IF NOT EXISTS `variantorm` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `variantorm`;

-- 导出  表 variantorm.t_data_list_view 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 正在导出表  variantorm.t_data_list_view 的数据：~0 rows (大约)
DELETE FROM `t_data_list_view`;
/*!40000 ALTER TABLE `t_data_list_view` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_data_list_view` ENABLE KEYS */;

-- 导出  表 variantorm.t_democompany 结构
CREATE TABLE IF NOT EXISTS `t_democompany` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `demoCompanyId` char(40) NOT NULL,
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `ownerUser` char(40) NOT NULL,
  `ownerDepartment` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  `c_companyName` varchar(190) DEFAULT NULL,
  `c_shortName` varchar(190) DEFAULT NULL,
  `c_creditCode` varchar(190) DEFAULT NULL,
  `c_phoneNumber` varchar(190) DEFAULT NULL,
  `c_webSite` varchar(190) DEFAULT NULL,
  `c_emailAddress` varchar(190) DEFAULT NULL,
  `c_officePictures` text,
  `c_industryType` smallint(6) DEFAULT NULL,
  `c_province` varchar(190) DEFAULT NULL,
  `c_city` varchar(190) DEFAULT NULL,
  `c_district` varchar(190) DEFAULT NULL,
  `c_address` varchar(190) DEFAULT NULL,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `demoCompanyId` (`demoCompanyId`) USING BTREE,
  KEY `ownerUser` (`ownerUser`) USING BTREE,
  KEY `ownerDepartment` (`ownerDepartment`) USING BTREE,
  KEY `isDeleted` (`isDeleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  variantorm.t_democompany 的数据：~3 rows (大约)
DELETE FROM `t_democompany`;
/*!40000 ALTER TABLE `t_democompany` DISABLE KEYS */;
INSERT INTO `t_democompany` (`autoId`, `demoCompanyId`, `createdOn`, `createdBy`, `ownerUser`, `ownerDepartment`, `modifiedOn`, `modifiedBy`, `isDeleted`, `c_companyName`, `c_shortName`, `c_creditCode`, `c_phoneNumber`, `c_webSite`, `c_emailAddress`, `c_officePictures`, `c_industryType`, `c_province`, `c_city`, `c_district`, `c_address`) VALUES
	(1, '122-836b02cf-a233-4d4a-8b49-940210a26909', '2021-02-10 14:59:38', '021-000000000000000000000000000000000001', '021-000000000000000000000000000000000001', '022-000000000000000000000000000000000001', NULL, NULL, 0, '广州蒙奇数据科技有限公司', '蒙奇数据', NULL, '56789088', 'http://www.gzmq.com', 'webmaster@gzmq.com', NULL, NULL, NULL, NULL, NULL, NULL),
	(2, '122-c56ee3a8-4929-4673-90ee-4042eee108e1', '2021-02-10 15:00:52', '021-000000000000000000000000000000000001', '021-000000000000000000000000000000000001', '022-000000000000000000000000000000000001', NULL, NULL, 0, '北京彩猫文化传播有限公司', '彩猫文化', NULL, NULL, 'http://www.caimao.com', 'admin@caimao.com', NULL, NULL, NULL, NULL, NULL, NULL),
	(3, '122-2a310688-aed0-4bf0-8557-024d16f421a0', '2021-02-10 15:08:43', '021-000000000000000000000000000000000001', '021-000000000000000000000000000000000001', '022-000000000000000000000000000000000001', '2021-03-02 11:58:48', '021-000000000000000000000000000000000001', 0, '上海新蓝汽车贸易有限公司', '新蓝汽车', NULL, NULL, NULL, NULL, NULL, NULL, '上海市', '市辖区', '黄浦区', '外滩8号');
/*!40000 ALTER TABLE `t_democompany` ENABLE KEYS */;

-- 导出  表 variantorm.t_democontact 结构
CREATE TABLE IF NOT EXISTS `t_democontact` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `demoContactId` char(40) NOT NULL,
  `createdOn` datetime NOT NULL,
  `createdBy` char(40) NOT NULL,
  `ownerUser` char(40) NOT NULL,
  `ownerDepartment` char(40) NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `modifiedBy` char(40) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT '0',
  `c_companyId` char(40) DEFAULT NULL,
  `c_fullName` varchar(190) DEFAULT NULL,
  `c_mobilePhone` varchar(190) DEFAULT NULL,
  `c_qqNumber` varchar(190) DEFAULT NULL,
  `c_wechat` varchar(190) DEFAULT NULL,
  `c_province` varchar(190) DEFAULT NULL,
  `c_city` varchar(190) DEFAULT NULL,
  `c_district` varchar(190) DEFAULT NULL,
  `c_address` varchar(190) DEFAULT NULL,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `demoContactId` (`demoContactId`) USING BTREE,
  KEY `ownerUser` (`ownerUser`) USING BTREE,
  KEY `ownerDepartment` (`ownerDepartment`) USING BTREE,
  KEY `isDeleted` (`isDeleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  variantorm.t_democontact 的数据：~0 rows (大约)
DELETE FROM `t_democontact`;
/*!40000 ALTER TABLE `t_democontact` DISABLE KEYS */;
INSERT INTO `t_democontact` (`autoId`, `demoContactId`, `createdOn`, `createdBy`, `ownerUser`, `ownerDepartment`, `modifiedOn`, `modifiedBy`, `isDeleted`, `c_companyId`, `c_fullName`, `c_mobilePhone`, `c_qqNumber`, `c_wechat`, `c_province`, `c_city`, `c_district`, `c_address`) VALUES
	(1, '123-35f29455-1de9-4741-a66d-7881dafcbdda', '2021-02-10 15:35:16', '021-000000000000000000000000000000000001', '021-000000000000000000000000000000000001', '022-000000000000000000000000000000000001', NULL, NULL, 0, '122-836b02cf-a233-4d4a-8b49-940210a26909', '孙立人', '15980882356', NULL, NULL, '北京市', '市辖区', '西城区', '西直门大街888号');
/*!40000 ALTER TABLE `t_democontact` ENABLE KEYS */;

-- 导出  表 variantorm.t_department 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  variantorm.t_department 的数据：~4 rows (大约)
DELETE FROM `t_department`;
/*!40000 ALTER TABLE `t_department` DISABLE KEYS */;
INSERT INTO `t_department` (`autoId`, `departmentId`, `isDeleted`, `parentDepartmentId`, `departmentName`, `description`) VALUES
	(1, '022-000000000000000000000000000000000001', 0, NULL, '公司总部', NULL);
/*!40000 ALTER TABLE `t_department` ENABLE KEYS */;

-- 导出  表 variantorm.t_department_node 结构
CREATE TABLE IF NOT EXISTS `t_department_node` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `departmentNodeId` char(40) NOT NULL,
  `parentDepartmentId` char(40) DEFAULT NULL,
  `childDepartmentId` char(40) DEFAULT NULL,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `departmentNodeId` (`departmentNodeId`) USING BTREE,
  UNIQUE KEY `parentDepartmentId_childDepartmentId` (`parentDepartmentId`,`childDepartmentId`)
) ENGINE=InnoDB AUTO_INCREMENT=387 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  variantorm.t_department_node 的数据：~1 rows (大约)
DELETE FROM `t_department_node`;
/*!40000 ALTER TABLE `t_department_node` DISABLE KEYS */;
INSERT INTO `t_department_node` (`autoId`, `departmentNodeId`, `parentDepartmentId`, `childDepartmentId`) VALUES
	(1, '011-873dcc76-7b3d-11eb-b21e-1cbfc037aa76', '022-000000000000000000000000000000000001', '022-000000000000000000000000000000000001');
/*!40000 ALTER TABLE `t_department_node` ENABLE KEYS */;

-- 导出  表 variantorm.t_form_layout 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  variantorm.t_form_layout 的数据：~6 rows (大约)
DELETE FROM `t_form_layout`;
/*!40000 ALTER TABLE `t_form_layout` DISABLE KEYS */;
INSERT INTO `t_form_layout` (`autoId`, `formLayoutId`, `createdOn`, `createdBy`, `modifiedOn`, `modifiedBy`, `layoutName`, `entityCode`, `layoutJson`) VALUES
	(6, '008-d50a2eab-9d23-4cc8-931a-2c6a3d49dde3', '2020-11-06 14:35:24', '021-000000000000000000000000000000000001', '2021-02-14 15:38:19', '021-000000000000000000000000000000000001', '默认表单布局', 21, '{"tabNames":["tabName4132"],"formTabs":[{"title":"用户信息","id":"tab7923","sections":[{"title":"基本信息","name":"sec1","id":"section11195","rows":[{"title":"新增行","id":"row4176","gutter":12,"cells":[{"id":"cell10740","span":12,"fields":[{"reserved":false,"name":"userName","label":"姓名","type":"Text","labelWidth":null,"unDraggable":true}]},{"id":"cell7799","span":12,"fields":[{"reserved":false,"name":"departmentId","label":"用户归属部门","type":"Reference","unDraggable":true,"labelWidth":85}]}]},{"title":"新增行","id":"row6949","gutter":12,"cells":[{"id":"cell9413","span":12,"fields":[{"reserved":false,"name":"loginName","label":"登录账号名","type":"Text","unDraggable":true,"labelWidth":null}]},{"id":"cell2869","span":12,"fields":[{"reserved":false,"name":"loginPwd","label":"登录密码","type":"Password","labelWidth":85,"unDraggable":true}]}]},{"title":"新增行","id":"row7545","gutter":12,"cells":[{"id":"cell6412","span":12,"fields":[{"reserved":false,"name":"jobTitle","label":"职务","type":"Option","unDraggable":true}]},{"id":"cell10398","span":12,"fields":[{"reserved":false,"name":"disabled","label":"是否禁用","type":"Boolean","unDraggable":true}]}]},{"title":"新增行","id":"row4506","gutter":12,"cells":[{"id":"cell14633","span":24,"fields":[{"reserved":false,"name":"roles","label":"权限角色","type":"ReferenceList","unDraggable":true}]}]}],"showSplitter":false,"showSectionTitle":true,"showArrowIcon":true,"openSplitter":false},{"title":"管理信息","name":"sec1","id":"section3382","rows":[{"title":"新增行","id":"row4744","gutter":12,"cells":[{"id":"cell9586","span":12,"fields":[{"reserved":true,"name":"createdBy","label":"创建用户","type":"Reference","unDraggable":true,"labelWidth":null}]},{"id":"cell11368","span":12,"fields":[{"reserved":true,"name":"createdOn","label":"创建时间","type":"DateTime","unDraggable":true,"labelWidth":85}]}]},{"title":"新增行","id":"row10535","gutter":12,"cells":[{"id":"cell9847","span":12,"fields":[{"reserved":true,"name":"modifiedBy","label":"修改用户","type":"Reference","unDraggable":true,"labelWidth":null}]},{"id":"cell14703","span":12,"fields":[{"reserved":true,"name":"modifiedOn","label":"最近修改时间","type":"DateTime","unDraggable":true,"labelWidth":85}]}]},{"title":"新增行","id":"row13678","gutter":12,"cells":[{"id":"cell12759","span":12,"fields":[{"reserved":true,"name":"ownerUser","label":"所属用户","type":"Reference","labelWidth":null,"unDraggable":true}]},{"id":"cell2351","span":12,"fields":[{"reserved":true,"name":"ownerDepartment","label":"所属部门","type":"Reference","labelWidth":85,"unDraggable":true}]}]}],"lineSpacing":10}],"name":"tabName4132"}],"labelPosition":"left","labelAlign":"label-right-align","labelWidth":75,"lineSpacing":12,"hideOnlyTabTitle":true}'),
	(7, '008-8c871f6e-a2d5-4d0e-a351-cc87b5ca05ae', '2020-11-11 16:42:31', '021-000000000000000000000000000000000001', '2021-02-24 23:29:24', '021-000000000000000000000000000000000001', '默认表单布局', 22, '{"tabNames":["tabName4664"],"formTabs":[{"title":"页签1","id":"tab9548","sections":[{"title":"新增区块","name":"sec1","id":"section11203","rows":[{"title":"新增行","id":"row4808","gutter":12,"cells":[{"id":"cell7962","span":12,"fields":[{"reserved":false,"name":"parentDepartmentId","label":"上级部门","type":"Reference","unDraggable":true,"labelWidth":null}]},{"id":"cell3340","span":12,"fields":[{"reserved":false,"name":"departmentName","label":"部门名称","type":"Text","unDraggable":true,"labelWidth":null}]}]},{"title":"新增行","id":"row1834","gutter":12,"cells":[{"id":"cell7530","span":24,"fields":[{"reserved":false,"name":"description","label":"部门说明","type":"TextArea","unDraggable":true}]}]}],"showSectionTitle":false,"showArrowIcon":false,"showSplitter":false}],"name":"tabName4664"}],"labelPosition":"left","labelAlign":"label-left-align","labelWidth":75,"lineSpacing":12,"hideOnlyTabTitle":true}'),
	(8, '008-f42c4cec-c91b-4e33-8ae9-e06286a732a4', '2020-11-11 16:44:18', '021-000000000000000000000000000000000001', NULL, NULL, '默认表单布局0', 22, '{"tabNames":["tabName4664"],"formTabs":[{"title":"页签1","id":"tab9548","sections":[{"title":"新增区块","name":"sec1","id":"section11203","rows":[{"title":"新增行","id":"row9965","gutter":12,"cells":[{"id":"cell4508","span":6,"fields":[]},{"id":"cell1916","span":12,"fields":[]}]}]}],"name":"tabName4664"}],"activeTab":{"title":"页签1","id":"tab9548","sections":[{"title":"新增区块","name":"sec1","id":"section11203","rows":[{"title":"新增行","id":"row9965","gutter":12,"cells":[{"id":"cell4508","span":6,"fields":[]},{"id":"cell1916","span":12,"fields":[]}]}]}],"name":"tabName4664"},"activeTabName":"tabName4664","activeSection":null,"activeRow":{"title":"新增行","id":"row6026","gutter":12,"cells":[{"id":"cell10795","span":6,"fields":[{"reserved":false,"name":"departmentName","label":"部门名称","type":"Text","labelWidth":75,"unDraggable":true}]},{"id":"cell5003","span":12,"fields":[{"reserved":false,"name":"parentDepartmentId","label":"上级部门","type":"Reference","labelWidth":75,"unDraggable":true}]}]},"activeCell":null,"activeFieldWrapper":null,"activeFieldLabelWidth":0,"labelAlign":"label-left-align","labelPosition":"left","hideOnlyTabTitle":false}'),
	(9, '008-a2b505f3-2c7a-43ec-b7ae-66e13637e6f6', '2021-02-10 14:41:22', '021-000000000000000000000000000000000001', '2021-03-03 11:18:55', '021-000000000000000000000000000000000001', '默认表单布局', 122, '{"tabNames":["tabName8104","tabName3221"],"formTabs":[{"title":"页签1","id":"tab7873","sections":[{"title":"基本信息","name":"sec1","id":"section8312","rows":[{"title":"新增行","id":"row2144","gutter":12,"cells":[{"id":"cell9773","span":24,"fields":[{"reserved":false,"name":"companyName","label":"公司名称","type":"Text","unDraggable":true}]}]},{"title":"新增行","id":"row5793","gutter":12,"cells":[{"id":"cell10435","span":12,"fields":[{"reserved":false,"name":"shortName","label":"公司简称","type":"Text","unDraggable":true}]},{"id":"cell10867","span":12,"fields":[{"reserved":false,"name":"creditCode","label":"纳税人编码","type":"Text","unDraggable":true}]}]},{"title":"新增行","id":"row10442","gutter":12,"cells":[{"id":"cell3992","span":8,"fields":[{"reserved":false,"name":"phoneNumber","label":"联系电话","type":"Text","unDraggable":true}]},{"id":"cell5563","span":8,"fields":[{"reserved":false,"name":"webSite","label":"网站","type":"Text","unDraggable":true}]},{"id":"cell8889","span":8,"fields":[{"reserved":false,"name":"emailAddress","label":"公司邮箱","type":"Text","unDraggable":true}]}]},{"title":"新增行","id":"row6704","gutter":12,"cells":[{"id":"cell7702","span":12,"fields":[{"reserved":false,"name":"industryType","label":"所属行业","type":"Option","unDraggable":true}]},{"id":"cell8539","span":12,"fields":[]}]},{"title":"新增行","id":"row5858","gutter":12,"cells":[{"id":"cell8361","span":12,"fields":[{"label":"地区选择","name":"Custom3898","componentName":"AreaCascadeSelect","fields":["province","city","district"],"type":"Custom"}]},{"id":"cell11704","span":12,"fields":[{"reserved":false,"name":"address","label":"详细地址","type":"Text","unDraggable":true}]}]}],"showSectionTitle":true,"showArrowIcon":true,"showSplitter":true},{"title":"照片上传区","name":"sec1","id":"section11993","rows":[{"title":"新增行","id":"row3956","gutter":12,"cells":[{"id":"cell5012","span":24,"fields":[{"reserved":false,"name":"officePictures","label":"办公场景照片","type":"Picture","unDraggable":true}]}]}]},{"title":"管理信息","name":"sec1","id":"section5410","rows":[{"title":"新增行","id":"row8003","gutter":12,"cells":[{"id":"cell12882","span":12,"fields":[{"reserved":true,"name":"createdOn","label":"创建时间","type":"DateTime","unDraggable":true}]},{"id":"cell4269","span":12,"fields":[{"reserved":true,"name":"createdBy","label":"创建用户","type":"Reference","unDraggable":true}]}]},{"title":"新增行","id":"row6519","gutter":12,"cells":[{"id":"cell1796","span":12,"fields":[{"reserved":true,"name":"modifiedOn","label":"最近修改时间","type":"DateTime","unDraggable":true}]},{"id":"cell4347","span":12,"fields":[{"reserved":true,"name":"modifiedBy","label":"修改用户","type":"Reference","unDraggable":true}]}]},{"title":"新增行","id":"row5888","gutter":12,"cells":[{"id":"cell7028","span":12,"fields":[{"reserved":true,"name":"ownerUser","label":"所属用户","type":"Reference","unDraggable":true}]},{"id":"cell8900","span":12,"fields":[{"reserved":true,"name":"ownerDepartment","label":"所属部门","type":"Reference","unDraggable":true}]}]}],"showSectionTitle":true,"showArrowIcon":false,"showSplitter":true}],"name":"tabName8104"},{"title":"页签2","id":"tab9966","sections":[{"title":"基本信息","name":"sec1","id":"section4699","rows":[{"title":"新增行","id":"row1462","gutter":12,"cells":[{"id":"cell3973","span":24,"fields":[]}]}]}],"name":"tabName3221"}],"labelPosition":"left","labelAlign":"label-right-align","labelWidth":75,"lineSpacing":16,"hideOnlyTabTitle":false}'),
	(13, '008-6e997cbf-50db-4f85-80fe-7ea7c50c4c5c', '2021-02-10 15:27:45', '021-000000000000000000000000000000000001', '2021-02-10 15:29:48', '021-000000000000000000000000000000000001', '默认表单布局', 123, '{"tabNames":["tabName8197"],"formTabs":[{"title":"页签1","id":"tab12619","sections":[{"title":"基本信息","name":"sec1","id":"section5102","rows":[{"title":"新增行","id":"row4319","gutter":12,"cells":[{"id":"cell8296","span":12,"fields":[{"reserved":false,"name":"fullName","label":"姓名","type":"Text","unDraggable":true}]},{"id":"cell3540","span":12,"fields":[{"reserved":false,"name":"companyId","label":"所属公司","type":"Reference","unDraggable":true}]}]},{"title":"新增行","id":"row4239","gutter":12,"cells":[{"id":"cell5387","span":12,"fields":[{"reserved":false,"name":"mobilePhone","label":"手机号码","type":"Text","unDraggable":true}]},{"id":"cell8647","span":12,"fields":[]}]},{"title":"新增行","id":"row3216","gutter":12,"cells":[{"id":"cell7574","span":12,"fields":[{"reserved":false,"name":"qqNumber","label":"QQ号码","type":"Text","unDraggable":true}]},{"id":"cell477","span":12,"fields":[{"reserved":false,"name":"wechat","label":"微信号","type":"Text","unDraggable":true}]}]}]},{"title":"联系地址","name":"sec1","id":"section10944","rows":[{"title":"新增行","id":"row4211","gutter":12,"cells":[{"id":"cell5188","span":12,"fields":[{"label":"地区选择","name":"Custom9558","componentName":"AreaCascadeSelect","fields":["province","city","district"],"type":"Custom"}]},{"id":"cell7694","span":12,"fields":[]}]},{"title":"新增行","id":"row3020","gutter":12,"cells":[{"id":"cell10483","span":24,"fields":[{"reserved":false,"name":"address","label":"详细地址","type":"Text","unDraggable":true}]}]}]}],"name":"tabName8197"}],"labelPosition":"left","labelAlign":"label-left-align","labelWidth":75,"lineSpacing":16,"hideOnlyTabTitle":false}');
/*!40000 ALTER TABLE `t_form_layout` ENABLE KEYS */;

-- 导出  表 variantorm.t_meta_entity 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb4 COMMENT='元数据实体表';

-- 正在导出表  variantorm.t_meta_entity 的数据：~12 rows (大约)
DELETE FROM `t_meta_entity`;
/*!40000 ALTER TABLE `t_meta_entity` DISABLE KEYS */;
INSERT INTO `t_meta_entity` (`autoId`, `entityId`, `name`, `label`, `physicalName`, `entityCode`, `detailEntityFlag`, `mainEntity`, `layoutable`, `listable`, `authorizable`, `shareable`, `assignable`) VALUES
	(5, '001-5985cd91-e41a-11ea-bf5a-1cbfc037aa76', 'OptionItem', '单选项', 't_option_item', 3, 0, NULL, 0, 0, 0, 0, 0),
	(41, '001-cbffd380-0f0f-4a8d-8fd7-e38b214d2c38', 'Department', '部门', 't_department', 22, 0, NULL, 1, 1, 0, 0, 0),
	(42, '001-451945e7-3e4e-4c1c-87fa-d9440571d178', 'User', '用户', 't_user', 21, 0, NULL, 1, 1, 1, 0, 0),
	(63, '001-7c3a3a0a-0df3-11eb-9b4d-1cbfc037aa76', 'TagItem', '多选项', 't_tag_item', 4, 0, NULL, 0, 0, 0, 0, 0),
	(64, '001-28412a4a-14d5-11eb-9b4d-1cbfc037aa76', 'ReferenceListMap', '多对多中间表', 't_reference_list_map', 5, 0, NULL, 0, 0, 0, 0, 0),
	(66, '001-2adc2060-835f-4a00-a5dd-9a283b8c7d07', 'FormLayout', '表单布局', 't_form_layout', 8, 0, NULL, 0, 0, 1, 0, 0),
	(67, '001-8b37a034-183b-11eb-9b4d-1cbfc037aa76', 'ReferenceCache', '名称字段缓存', 't_reference_cache', 6, 0, NULL, 0, 0, 0, 0, 0),
	(70, '001-739b93d6-0178-435c-ad49-c25ede549f8a', 'DataListView', '数据列表视图', 't_data_list_view', 9, 0, NULL, 0, 0, 0, 0, 0),
	(71, '001-bd6998eb-c993-430e-93d9-8a3c14dfe981', 'DemoCompany', '演示公司', 't_democompany', 122, 0, NULL, 1, 1, 1, 0, 0),
	(72, '001-5d1acc0d-ecfe-4eea-bc25-71b79049dec7', 'DemoContact', '演示联系人', 't_democontact', 123, 0, NULL, 1, 1, 1, 0, 0),
	(74, '001-c556338c-f71c-4a2b-8bb8-bb31b544f113', 'Role', '权限角色', 't_role', 23, 0, NULL, 0, 1, 0, 0, 0),
	(79, '001-9b336efa-54a6-4b04-8123-3dc70fc3bba7', 'SystemSetting', '系统参数', 't_system_setting', 7, 0, NULL, 0, 0, 0, 0, 0),
	(82, '001-6e6c0dff-3edb-4113-9c41-dd861673869f', 'RouterMenu', '系统导航菜单', 't_router_menu', 10, 0, NULL, 0, 0, 0, 0, 0),
	(84, '001-5ca44af2-f3bb-4c5b-aceb-3661ed1a5a94', 'DepartmentNode', '部门节点关系', 't_department_node', 11, 0, NULL, 0, 0, 0, 0, 0);
/*!40000 ALTER TABLE `t_meta_entity` ENABLE KEYS */;

-- 导出  表 variantorm.t_meta_field 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=464 DEFAULT CHARSET=utf8mb4 COMMENT='元数据实体字段表';

-- 正在导出表  variantorm.t_meta_field 的数据：~118 rows (大约)
DELETE FROM `t_meta_field`;
/*!40000 ALTER TABLE `t_meta_field` DISABLE KEYS */;
INSERT INTO `t_meta_field` (`autoId`, `fieldId`, `entityCode`, `name`, `label`, `physicalName`, `type`, `description`, `displayOrder`, `nullable`, `creatable`, `updatable`, `idFieldFlag`, `nameFieldFlag`, `mainDetailFieldFlag`, `defaultMemberOfListFlag`, `referTo`, `referenceSetting`, `fieldViewModel`) VALUES
	(3, '002-59941601-e41a-11ea-bf5a-1cbfc037aa76', 3, 'optionItemId', 'id主键', 'optionItemId', 'PrimaryKey', NULL, 0, 0, 1, 0, 1, 0, 0, 0, '', '', ' '),
	(4, '002-f92ba010-e41a-11ea-bf5a-1cbfc037aa76', 3, 'entityName', '实体名称', 'entityName', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '', ' '),
	(5, '002-64bd82c5-e41b-11ea-bf5a-1cbfc037aa76', 3, 'fieldName', '字段名称', 'fieldName', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '', ' '),
	(6, '002-b61b5dbe-e41b-11ea-bf5a-1cbfc037aa76', 3, 'value', '选项值', 'value', 'Integer', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '', ' '),
	(7, '002-d86481d2-e41b-11ea-bf5a-1cbfc037aa76', 3, 'label', '显示值', 'label', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', '', ' '),
	(8, '002-fd89c66e-e41b-11ea-bf5a-1cbfc037aa76', 3, 'displayOrder', '显示顺序', 'displayOrder', 'Integer', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', '', ' '),
	(99, '002-d913a2e1-f0e9-4c47-a023-c157b2ef4018', 22, 'departmentId', '部门Id主键', 'departmentId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', '', 'null'),
	(100, '002-e59b2ce5-e787-4d1c-a5f4-bee1488f0aeb', 22, 'parentDepartmentId', '上级部门', 'parentDepartmentId', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 1, 'Department,', '[{"entityName":"Department","fieldList":["parentDepartmentId","departmentName"]}]', '{"searchDialogWidth":520}'),
	(101, '002-270c7289-7981-4cde-871f-77dfe96e0dac', 22, 'departmentName', '部门名称', 'departmentName', 'Text', NULL, 0, 0, 1, 1, 0, 1, 0, 1, '', '[]', '{}'),
	(102, '002-f79eafe9-bbfc-4f90-b841-d7e5cce5a788', 21, 'userId', '用户Id主键', 'userId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', '', 'null'),
	(103, '002-58c9cfa1-bbc6-4079-aaf2-ea885b2407af', 21, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', '', 'null'),
	(104, '002-59f459d4-e463-4716-bff8-703f9ba0fd66', 21, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', '', 'null'),
	(105, '002-c5cc5611-014c-4f72-ad54-674eaf180513', 21, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', '', 'null'),
	(106, '002-d11e45f8-7090-43e9-83d0-9baad325037f', 21, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', '', 'null'),
	(107, '002-e7a92f3f-47ae-4a40-90ea-c570187d9500', 21, 'ownerUser', '所属用户', 'ownerUser', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', '', 'null'),
	(108, '002-710406e2-dbb3-474b-8aa0-66b170f6c290', 21, 'ownerDepartment', '所属部门', 'ownerDepartment', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'Department,', '', 'null'),
	(109, '002-c4f03472-3471-4fe1-a124-72b94491328c', 21, 'departmentId', '用户归属部门', 'departmentId', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 1, 'Department,', '[{"entityName":"Department","fieldList":["departmentName","parentDepartmentId"]}]', '{"searchDialogWidth":510}'),
	(110, '002-2bfe8fa8-cf67-4e32-aacd-4553c1162aff', 21, 'userName', '用户名称', 'userName', 'Text', NULL, 0, 0, 1, 1, 0, 1, 0, 1, '', '[]', '{"minLength":2,"maxLength":15}'),
	(140, '002-aa3f6493-8dff-4e74-ada1-b7d82682ffd9', 21, 'loginPwd', '登录密码', 'loginPwd', 'Password', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, '{}'),
	(141, '002-1af09034-a6cc-4f11-a1c6-b77f1064051a', 21, 'loginName', '登录账号名', 'loginName', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', '[]', '{"validators":["letterStartNumberIncluded"]}'),
	(247, '002-1d829a90-0df4-11eb-9b4d-1cbfc037aa76', 4, 'tagItemId', 'id主键', 'tagItemId', 'PrimaryKey', NULL, 0, 0, 1, 0, 1, 0, 0, 0, '', '', ' '),
	(248, '002-932cecba-0df4-11eb-9b4d-1cbfc037aa76', 4, 'entityName', '实体名称', 'entityName', 'Text', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', '', ' '),
	(250, '002-e012ce28-0df4-11eb-9b4d-1cbfc037aa76', 4, 'fieldName', '字段名称', 'fieldName', 'Text', NULL, 0, 0, 1, 0, 0, 1, 0, 1, '', '', ' '),
	(251, '002-fa420e94-0df4-11eb-9b4d-1cbfc037aa76', 4, 'value', '选项值', 'value', 'Text', NULL, 0, 0, 1, 0, 0, 1, 0, 1, '', '', ' '),
	(252, '002-0ad73718-0df5-11eb-9b4d-1cbfc037aa76', 4, 'displayOrder', '显示顺序', 'displayOrder', 'Integer', NULL, 0, 0, 1, 0, 0, 1, 0, 1, '', '', ' '),
	(276, '002-9694fd64-14d9-11eb-9b4d-1cbfc037aa76', 5, 'entityName', '实体名称', 'entityName', 'Text', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', '', ' '),
	(277, '002-07ed0120-14da-11eb-9b4d-1cbfc037aa76', 5, 'fieldName', '字段名称', 'fieldName', 'Text', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', '', ' '),
	(278, '002-2a666593-14da-11eb-9b4d-1cbfc037aa76', 5, 'objectId', '记录Id', 'objectId', 'AnyReference', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', '', ' '),
	(287, '002-672f0645-174f-11eb-9b4d-1cbfc037aa76', 5, 'mapId', '主键Id', 'mapId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', '', ' '),
	(288, '002-a648776b-18c1-4ea1-b326-fa4764d8f4a0', 8, 'formLayoutId', '布局Id主键', 'formLayoutId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(289, '002-03f49510-4296-4f69-a4b9-56ec3cb116e4', 8, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(290, '002-83d3c9ed-5e2c-4866-85af-ae67150c6527', 8, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(291, '002-9271d23e-e10f-42ec-9cd3-aa063bb84759', 8, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(292, '002-48dd787c-4dec-42f4-9745-c684d1643e0d', 8, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(295, '002-57b0a7b6-67b4-4b5b-8128-499c201e91c9', 8, 'layoutName', '布局名称', 'layoutName', 'Text', NULL, 0, 0, 1, 1, 0, 1, 0, 0, '', NULL, NULL),
	(296, '002-afd8c41d-1b79-48dd-a888-4b364ca5b213', 8, 'entityCode', '实体Code', 'entityCode', 'Integer', NULL, 0, 0, 1, 0, 0, 0, 0, 0, '', NULL, NULL),
	(297, '002-4303f0eb-107f-429b-a61a-df84158511ed', 8, 'layoutJson', '布局Json', 'layoutJson', 'TextArea', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(298, '002-d226592b-183c-11eb-9b4d-1cbfc037aa76', 6, 'cacheId', '缓存Id主键', 'cacheId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(300, '002-2fa31007-183d-11eb-9b4d-1cbfc037aa76', 6, 'referenceId', '引用记录Id', 'referenceId', 'AnyReference', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(301, '002-54092316-183d-11eb-9b4d-1cbfc037aa76', 6, 'recordLabel', '实体名称值', 'recordLabel', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(325, '002-aef7ab20-6527-4369-8667-35dfdfd2c971', 21, 'jobTitle', '职务', 'c_jobTitle', 'Option', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, NULL),
	(340, '002-aaf67998-f29d-4106-883d-238fce1a2a29', 9, 'dataListViewId', 'id主键', 'dataListViewId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(341, '002-cc80c16b-ce36-434d-9a77-a06fa8c9f489', 9, 'entityCode', '实体编码', 'entityCode', 'Integer', NULL, 0, 0, 1, 0, 0, 0, 0, 0, '', NULL, NULL),
	(342, '002-fced0268-86ff-4bc8-b795-6985fc733176', 9, 'viewName', '列表视图名称', 'viewName', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(343, '002-e555fa78-b941-452f-bfb8-a9706ba264ed', 9, 'headerJson', '表头Json', 'headerJson', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(344, '002-bbdb7e50-f11a-44ff-8ccd-b4bd771fd31c', 9, 'filterJson', '筛选条件Json', 'filterJson', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(345, '002-2c4c773c-fdd3-45b9-a882-e11cf936d796', 9, 'paginationJson', '分页Json', 'paginationJson', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(346, '002-5723b1bd-6c47-42a1-bc02-5e124c285175', 9, 'sortJson', '排序Json', 'sortJson', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(352, '002-90fc328f-5630-4af1-89e5-7a0a38226e4d', 122, 'demoCompanyId', 'id主键', 'demoCompanyId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(353, '002-8c683bc3-e79b-4620-be0f-89fc73c72eb6', 122, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(354, '002-7f14cfb8-f849-42b6-8ce1-38ad2a8be1a0', 122, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(355, '002-43e4b218-dc8b-4ef1-becb-d5a46320b071', 122, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(356, '002-778cef6f-a8b5-4b27-bf3b-cae744947732', 122, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(357, '002-4ddeda8c-977d-4576-8816-34eea2bd8e94', 122, 'ownerUser', '所属用户', 'ownerUser', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(358, '002-5cdb9b94-e777-42fa-ba08-bc358b4363c0', 122, 'ownerDepartment', '所属部门', 'ownerDepartment', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'Department,', NULL, NULL),
	(359, '002-6c4cd993-21b0-4c11-af73-4f14a4a7f3b5', 123, 'demoContactId', 'id主键', 'demoContactId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(360, '002-27d4e150-339a-4a77-874b-51bbfbe8c5dc', 123, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(361, '002-6a4e6c40-8929-47bd-83e8-0d7bbb0f9d9a', 123, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(362, '002-2d99d682-8ea3-41b5-b9ea-189f182716c0', 123, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(363, '002-b4e20e20-cc59-460e-934b-bc92733b3495', 123, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(364, '002-c7de11da-0485-4ba4-9e43-13b681eaf19d', 123, 'ownerUser', '所属用户', 'ownerUser', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(365, '002-3b0f613b-4b54-406d-9799-dc1a22ad8b7e', 123, 'ownerDepartment', '所属部门', 'ownerDepartment', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'Department,', NULL, NULL),
	(366, '002-ffcec386-90ba-4725-8ef0-2fa9aae3063b', 122, 'companyName', '公司名称', 'c_companyName', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":[]}'),
	(367, '002-b062ea59-53fa-48dc-9aa4-cef482545575', 122, 'shortName', '公司简称', 'c_shortName', 'Text', NULL, 0, 0, 1, 1, 0, 1, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":[]}'),
	(368, '002-3d56a1b4-eacc-4e91-a1c3-060764387b24', 122, 'creditCode', '纳税人编码', 'c_creditCode', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":[]}'),
	(369, '002-f33a7699-a81b-44fb-b7a4-6fa5590c4c50', 122, 'phoneNumber', '联系电话', 'c_phoneNumber', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":[]}'),
	(370, '002-8f32fd55-4cb1-46e6-9284-2d4c6546388b', 122, 'webSite', '网站', 'c_webSite', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":["url"]}'),
	(371, '002-9378c8a7-5574-4890-95d7-488da14e23b4', 122, 'emailAddress', '公司邮箱', 'c_emailAddress', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":["email"]}'),
	(372, '002-05bee809-d65c-4d4a-8a3a-42a8f9315ea7', 123, 'companyId', '所属公司', 'c_companyId', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 1, 'DemoCompany,', '[{"entityName":"DemoCompany","fieldList":["companyName","shortName"]}]', '{"searchDialogWidth":520,"validators":[]}'),
	(373, '002-d95a1e83-b861-4a41-88b6-6de2a24a1dd5', 123, 'fullName', '姓名', 'c_fullName', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":[]}'),
	(374, '002-9ecad1b3-5701-470b-8e6d-0bc2ce53b5cc', 123, 'mobilePhone', '手机号码', 'c_mobilePhone', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":["mobile"]}'),
	(375, '002-044f621a-0136-48b3-aa0e-2545d41ca1d0', 123, 'qqNumber', 'QQ号码', 'c_qqNumber', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":[]}'),
	(376, '002-67bdf56a-8127-4e49-b878-d8a8e2cbb7d5', 123, 'wechat', '微信号', 'c_wechat', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":[]}'),
	(377, '002-be11a38b-bbec-4acd-a1b8-21eeb94bcf38', 123, 'province', '省份', 'c_province', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":[]}'),
	(378, '002-63c38ff4-d935-4a08-a703-707a9c4d3712', 123, 'city', '城市', 'c_city', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":[]}'),
	(379, '002-b2b7fc61-17b5-4f44-bd13-0cc7c700a76d', 123, 'district', '地区', 'c_district', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":[]}'),
	(380, '002-0bb9f014-a720-4137-8dc8-9013863649e5', 123, 'address', '详细地址', 'c_address', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":[]}'),
	(384, '002-d1649426-6d16-41b8-bc17-4464ced988a3', 23, 'roleId', 'id主键', 'roleId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(385, '002-2efcf072-3c60-43d5-9ba5-3e4640d0bc27', 23, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(386, '002-b7aadbc2-e683-4712-b24a-ec03edafa25c', 23, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(387, '002-0e238dcd-0371-4c1b-ac6f-aa34255595b5', 23, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(388, '002-75b08064-72d1-47e2-809c-1cda1e6b8d3c', 23, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(389, '002-edf2968f-bd8e-4063-8d2a-3543b6683102', 23, 'description', '角色说明', 'description', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{}'),
	(390, '002-ec274eb7-80d6-4c00-84d2-05dea18c1762', 21, 'roles', '权限角色', 'roles', 'ReferenceList', NULL, 0, 1, 1, 1, 0, 0, 0, 0, 'Role,', '[{"entityName":"Role","fieldList":["roleName","disabled"]}]', '{}'),
	(391, '002-d3928cc6-aac9-4c11-98aa-862282472ee3', 23, 'roleName', '角色名称', 'roleName', 'Text', NULL, 0, 0, 1, 1, 0, 1, 0, 1, '', NULL, '{}'),
	(392, '002-c4a97663-485b-4aa2-9d8e-c9028988c19c', 21, 'disabled', '是否禁用', 'disabled', 'Boolean', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(393, '002-bc1fa99c-64ec-4eee-8349-6cc19c927094', 23, 'disabled', '是否禁用', 'disabled', 'Boolean', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{}'),
	(394, '002-426372bb-b9e8-4dd4-b25c-cf2b44d2579f', 23, 'rightJson', '权限明细', 'rightJson', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(418, '002-ac36433e-a4f5-4716-82c1-2e0503763e9e', 7, 'systemSettingId', 'id主键', 'systemSettingId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(419, '002-31d48f7e-43fa-4c97-a5c3-b3970ae559ca', 7, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(420, '002-3ec33e00-1d9d-4b16-ab75-c3a6f11b69d2', 7, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(421, '002-cf4dd54e-2bdb-4a43-9d42-06a21d626db2', 7, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(422, '002-34c049db-c43e-4da8-b784-792a0f1d3a09', 7, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(423, '002-386b0596-3477-41ca-83cb-21389fa04c8f', 7, 'settingName', '参数名', 'settingName', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(424, '002-c1f42af8-f114-4d68-ae8a-961f9490e300', 7, 'settingValue', '参数值', 'settingValue', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(425, '002-00a1c8eb-430e-48dd-a93f-0e3565a41bd3', 7, 'defaultValue', '参数默认值', 'defaultValue', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(438, '002-0e9404e9-75c1-11eb-b21e-1cbfc037aa76', 5, 'toId', '被引用记录Id', 'toId', 'AnyReference', NULL, 0, 0, 1, 0, 0, 0, 0, 1, '', '', ' '),
	(439, '002-d0e31d2b-7689-11eb-b21e-1cbfc037aa76', 9, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(440, '002-e2735a5d-7689-11eb-b21e-1cbfc037aa76', 9, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(441, '002-f3451480-7689-11eb-b21e-1cbfc037aa76', 9, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(442, '002-00b62412-768a-11eb-b21e-1cbfc037aa76', 9, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(443, '002-77fe0108-21c8-4151-a009-fe7a0f5419af', 22, 'description', '部门说明', 'description', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(444, '002-fd443924-fd7b-454f-a50f-8cac7292933b', 122, 'officePictures', '办公场景照片', 'c_officePictures', 'Picture', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', '[]', '{"maxFileCount":5,"fileMaxSize":5,"uploadFileTypes":["jpeg","png"],"uploadHint":"支持上传JPG/PNG格式，单张图片小于5MB","validators":[]}'),
	(445, '002-b424bf43-2f11-4ab1-b265-6af59620ea84', 122, 'industryType', '所属行业', 'c_industryType', 'Option', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, NULL),
	(446, '002-4a5b300a-dc90-4f20-9be6-1c66ac1a9dbf', 122, 'province', '省份', 'c_province', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":[]}'),
	(447, '002-18710dc3-148d-4d02-aeac-979c55d7c356', 122, 'city', '城市', 'c_city', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":[]}'),
	(448, '002-1f394e5f-486c-4a87-8321-34f2a477e6f1', 122, 'district', '地区', 'c_district', 'Text', NULL, 0, 1, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":[]}'),
	(449, '002-e8b31b1a-8d12-47a9-ace0-52f492992c16', 122, 'address', '详细地址', 'c_address', 'Text', NULL, 0, 0, 1, 1, 0, 0, 0, 1, '', NULL, '{"minLength":0,"maxLength":190,"validators":[]}'),
	(450, '002-fd203a5d-0195-4b56-879f-5172ebe2c4b5', 9, 'presetFilter', '内置筛选条件', 'presetFilter', 'TextArea', NULL, 0, 1, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(451, '002-ef2db689-ba58-4d08-b5c2-67539a59e0b4', 10, 'routerMenuId', 'id主键', 'routerMenuId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(452, '002-e5b739ef-6e88-4096-9ca3-f705387f89d5', 10, 'createdOn', '创建时间', 'createdOn', 'DateTime', NULL, 0, 0, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(453, '002-d8d7b9df-101e-4617-ba40-3fc601717276', 10, 'createdBy', '创建用户', 'createdBy', 'Reference', NULL, 0, 0, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(454, '002-2538586f-16e2-48c4-9cde-6bc52983e5ea', 10, 'modifiedOn', '最近修改时间', 'modifiedOn', 'DateTime', NULL, 0, 1, 0, 0, 0, 0, 0, 0, '', NULL, NULL),
	(455, '002-a85b9dfb-b1ce-4d91-9406-3d242241a41b', 10, 'modifiedBy', '修改用户', 'modifiedBy', 'Reference', NULL, 0, 1, 0, 0, 0, 0, 0, 0, 'User,', NULL, NULL),
	(456, '002-c040b49f-0879-47f6-83a0-eb5d34e1a337', 10, 'menuJson', '菜单Json', 'menuJson', 'TextArea', NULL, 0, 0, 1, 1, 0, 0, 0, 0, '', NULL, NULL),
	(457, '002-ca410102-2e81-4dc3-9e9e-4f6bfbd8f500', 11, 'departmentNodeId', 'id主键', 'departmentNodeId', 'PrimaryKey', NULL, 0, 0, 0, 0, 1, 0, 0, 0, '', NULL, NULL),
	(462, '002-aa833b14-2992-446a-aafc-df62b0d66f33', 11, 'parentDepartmentId', '父级部门Id', 'parentDepartmentId', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 0, 'Department,', NULL, NULL),
	(463, '002-8d7a286c-d013-47c7-a7eb-119d8e8c5164', 11, 'childDepartmentId', '下级部门Id', 'childDepartmentId', 'Reference', NULL, 0, 0, 1, 1, 0, 0, 0, 0, 'Department,', NULL, NULL);
/*!40000 ALTER TABLE `t_meta_field` ENABLE KEYS */;

-- 导出  表 variantorm.t_option_item 结构
CREATE TABLE IF NOT EXISTS `t_option_item` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `optionItemId` varchar(40) NOT NULL,
  `entityName` varchar(150) NOT NULL,
  `fieldName` varchar(150) NOT NULL,
  `value` smallint(6) NOT NULL,
  `label` varchar(190) NOT NULL,
  `displayOrder` smallint(6) NOT NULL DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`autoId`),
  UNIQUE KEY `entityName_fieldName_value` (`entityName`,`fieldName`,`value`),
  UNIQUE KEY `optionItemId` (`optionItemId`),
  KEY `entityName_fieldName` (`entityName`,`fieldName`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  variantorm.t_option_item 的数据：~23 rows (大约)
DELETE FROM `t_option_item`;
/*!40000 ALTER TABLE `t_option_item` DISABLE KEYS */;
INSERT INTO `t_option_item` (`autoId`, `optionItemId`, `entityName`, `fieldName`, `value`, `label`, `displayOrder`, `timestamp`) VALUES
	(1, '003-4e5d5dd7-c514-42be-8e56-700a163d509b', 'Lead', 'leadType', 1, '直客线索', 0, '2020-08-22 10:24:21'),
	(6, '003-08368e75-85fe-41fa-b955-40a6c25375bf', 'Contact', 'workTitle', 1, '经理', 1, '2020-11-13 16:16:05'),
	(7, '003-5b37a2e3-42a8-495a-bfb0-573da689a424', 'Contact', 'workTitle', 2, '副总', 2, '2020-11-13 16:16:05'),
	(8, '003-4bcb3709-7dec-43fa-9e8a-eb83748dbc96', 'Contact', 'workTitle', 3, '总经理', 3, '2020-11-13 16:16:05'),
	(9, '003-007364c0-6ef3-45f3-af4e-3fa272e4ed33', 'Contact', 'workTitle', 4, '董事长', 4, '2020-11-13 16:16:05'),
	(10, '003-4779749d-65d1-401b-b5ac-8148a2ce401b', 'Contact', 'workTitle', 5, '主管', 5, '2020-11-13 16:16:05'),
	(11, '003-7fb0970e-2a47-499d-8f0f-ef8b18d8d8c3', 'Contact', 'workTitle', 6, '厂长', 6, '2020-11-13 16:16:05'),
	(12, '003-5d5ffeb4-9fa3-41d2-ac99-d4acc80e1532', 'Account', 'accountIndustry', 2, 'IT', 1, '2020-11-16 10:50:32'),
	(13, '003-81324d73-b22d-47f0-b285-5cb13dd14e68', 'Account', 'accountIndustry', 3, '快消品', 2, '2020-11-16 10:50:32'),
	(14, '003-499c8e19-d399-464f-abf8-d1b528e1d654', 'Account', 'accountIndustry', 4, '工业制造', 3, '2020-11-16 10:50:32'),
	(15, '003-0d547a8c-416c-4cb6-af8d-ec96848592dd', 'Account', 'accountIndustry', 5, '金融', 4, '2020-11-16 10:50:32'),
	(16, '003-629af24a-a69a-433c-b20b-4d4a3a56b031', 'Account', 'accountIndustry', 6, '企业服务', 5, '2020-11-16 10:50:32'),
	(21, '003-e917d2d5-8f1c-4cb0-a007-2fd5faa4d07c', 'Department', 'departmentType', 1, '业绩部门', 1, '2020-12-04 15:57:01'),
	(22, '003-13942852-43f2-4960-9843-61edf3ee1b32', 'Department', 'departmentType', 2, '后勤部门', 2, '2020-12-04 15:57:01'),
	(23, '003-3f0d3631-fc4c-4741-a6f5-e5f9f698f516', 'Department', 'departmentType', 3, '外包部门', 3, '2020-12-04 15:57:01'),
	(24, '003-21e53b8a-ae64-4efc-8b8b-c2e5e0e50174', 'Lead', 'jobTitle', 1, '总经理', 1, '2020-12-04 15:58:47'),
	(25, '003-c498d552-4dfe-47a2-806f-4ee6440289b7', 'Lead', 'jobTitle', 2, '副总', 2, '2020-12-04 15:58:47'),
	(26, '003-91fa2267-33b5-4375-b0fd-1662211fb0db', 'Lead', 'jobTitle', 3, '财务负责人', 3, '2020-12-04 15:58:47'),
	(72, '003-cef34676-b976-4c55-9322-75bea0d2cc64', 'User', 'jobTitle', 1, '员工', 1, '2021-02-14 15:37:57'),
	(73, '003-85b6e93f-a26f-4c89-bea7-20aed09ddf45', 'User', 'jobTitle', 2, '主管', 2, '2021-02-14 15:37:57'),
	(74, '003-f230f052-4079-4c6a-bf30-9872a4432fc6', 'User', 'jobTitle', 3, '经理', 3, '2021-02-14 15:37:57'),
	(75, '003-98019ec2-5b14-40bc-ae6d-54cc300286d0', 'User', 'jobTitle', 4, '总监', 4, '2021-02-14 15:37:57'),
	(76, '003-bc571b35-bb8d-433a-a021-3a82d86aa64d', 'User', 'jobTitle', 5, '部长', 5, '2021-02-14 15:37:57');
/*!40000 ALTER TABLE `t_option_item` ENABLE KEYS */;

-- 导出  表 variantorm.t_reference_cache 结构
CREATE TABLE IF NOT EXISTS `t_reference_cache` (
  `autoId` int(11) NOT NULL AUTO_INCREMENT,
  `cacheId` char(40) NOT NULL,
  `referenceId` char(40) NOT NULL,
  `recordLabel` varchar(190) NOT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`autoId`),
  UNIQUE KEY `referenceId` (`referenceId`),
  UNIQUE KEY `cacheId` (`cacheId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='所有实体主键和名称字段值缓存表。';

-- 正在导出表  variantorm.t_reference_cache 的数据：~0 rows (大约)
DELETE FROM `t_reference_cache`;
/*!40000 ALTER TABLE `t_reference_cache` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_reference_cache` ENABLE KEYS */;

-- 导出  表 variantorm.t_reference_list_map 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COMMENT='多对多引用字段中间表';

-- 正在导出表  variantorm.t_reference_list_map 的数据：~3 rows (大约)
DELETE FROM `t_reference_list_map`;
/*!40000 ALTER TABLE `t_reference_list_map` DISABLE KEYS */;
INSERT INTO `t_reference_list_map` (`autoId`, `mapId`, `entityName`, `fieldName`, `objectId`, `toId`, `timestamp`) VALUES
	(29, '005-b637c372-0d45-4ead-b322-3c0b5c31d444', 'User', 'roles', '021-000000000000000000000000000000000001', '023-000000000000000000000000000000000001', '2021-02-24 12:11:13'),
	(52, '005-32474c0c-2bb8-41fb-8c45-7a329911612c', 'User', 'roles', '021-1a7fc78f-071e-400f-b780-629fc137e223', '023-000000000000000000000000000000000001', '2021-03-03 18:21:18'),
	(53, '005-385c51b0-2ce2-4684-8fac-4c9162f2f5dc', 'User', 'roles', '021-1a7fc78f-071e-400f-b780-629fc137e223', '023-a2dc1582-62f4-452c-9a5d-a09c675ace41', '2021-03-03 18:21:18');
/*!40000 ALTER TABLE `t_reference_list_map` ENABLE KEYS */;

-- 导出  表 variantorm.t_role 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  variantorm.t_role 的数据：~2 rows (大约)
DELETE FROM `t_role`;
/*!40000 ALTER TABLE `t_role` DISABLE KEYS */;
INSERT INTO `t_role` (`autoId`, `roleId`, `roleName`, `createdOn`, `createdBy`, `modifiedOn`, `modifiedBy`, `isDeleted`, `description`, `disabled`, `rightJson`) VALUES
	(1, '023-000000000000000000000000000000000001', '管理员角色', '2021-02-14 18:09:52', '021-000000000000000000000000000000000001', '2021-02-14 18:10:05', '021-000000000000000000000000000000000001', 0, '管理员权限角色，拥有全部权限', 0, NULL),
	(2, '023-a2dc1582-62f4-452c-9a5d-a09c675ace41', 'testRole', '2021-02-20 18:19:35', '021-000000000000000000000000000000000001', '2021-02-22 17:52:32', '021-000000000000000000000000000000000001', 0, '', 0, '{"r21-3":30,"r22-2":10,"r6001":false,"r6002":false,"r6003":false,"r6004":false,"r6005":false,"r6006":false,"r23-3":50,"r122-1":40,"r123-4":10,"r123-2":10,"r21-1":30,"r21-4":30}');
/*!40000 ALTER TABLE `t_role` ENABLE KEYS */;

-- 导出  表 variantorm.t_router_menu 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 正在导出表  variantorm.t_router_menu 的数据：~0 rows (大约)
DELETE FROM `t_router_menu`;
/*!40000 ALTER TABLE `t_router_menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_router_menu` ENABLE KEYS */;

-- 导出  表 variantorm.t_system_setting 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  variantorm.t_system_setting 的数据：~3 rows (大约)
DELETE FROM `t_system_setting`;
/*!40000 ALTER TABLE `t_system_setting` DISABLE KEYS */;
INSERT INTO `t_system_setting` (`autoId`, `systemSettingId`, `createdOn`, `createdBy`, `modifiedOn`, `modifiedBy`, `settingName`, `settingValue`, `defaultValue`) VALUES
	(1, '007-ac681aea-7341-11eb-b21e-1cbfc037aa76', '2021-02-20 14:04:23', '021-000000000000000000000000000000000001', NULL, NULL, 'DBVersion', '1.0.20210302', NULL),
	(3, '007-457431d4-7588-11eb-b21e-1cbfc037aa76', '2021-02-20 14:04:23', '021-000000000000000000000000000000000001', NULL, NULL, 'dateFormat', 'yyyy-MM-dd', 'yyyy-MM-dd'),
	(6, '007-50ece946-7588-11eb-b21e-1cbfc037aa76', '2021-02-20 14:04:23', '021-000000000000000000000000000000000001', NULL, NULL, 'dateTimeFormat', 'yyyy-MM-dd HH:mm:ss', 'yyyy-MM-dd HH:mm:ss');
/*!40000 ALTER TABLE `t_system_setting` ENABLE KEYS */;

-- 导出  表 variantorm.t_tag_item 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  variantorm.t_tag_item 的数据：~5 rows (大约)
DELETE FROM `t_tag_item`;
/*!40000 ALTER TABLE `t_tag_item` DISABLE KEYS */;
INSERT INTO `t_tag_item` (`autoId`, `tagItemId`, `entityName`, `fieldName`, `value`, `displayOrder`, `timestamp`) VALUES
	(18, '004-04e9253a-c284-4f53-90a2-407e97991a55', 'User', 'hobbies', '爬山', 1, '2021-01-11 18:17:15'),
	(19, '004-73af6a82-a9ea-4bac-9166-aff57d70440c', 'User', 'hobbies', '跑步', 2, '2021-01-11 18:17:15'),
	(20, '004-37d9fb94-3b85-4372-a739-b4e9be659ed6', 'User', 'hobbies', '摄影', 3, '2021-01-11 18:17:15'),
	(21, '004-b1024cd7-dab4-4c8e-b4ec-d6513dfe6eb1', 'User', 'hobbies', '旅游', 4, '2021-01-11 18:17:15'),
	(22, '004-e04a319e-fbe5-4a91-842a-019c015b3433', 'User', 'hobbies', '划船', 5, '2021-01-11 18:17:15');
/*!40000 ALTER TABLE `t_tag_item` ENABLE KEYS */;

-- 导出  表 variantorm.t_user 结构
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
  `c_jobTitle` smallint(6) DEFAULT NULL,
  `disabled` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`autoId`) USING BTREE,
  UNIQUE KEY `userId` (`userId`) USING BTREE,
  KEY `ownerUser` (`ownerUser`) USING BTREE,
  KEY `ownerDepartment` (`ownerDepartment`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  variantorm.t_user 的数据：~2 rows (大约)
DELETE FROM `t_user`;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` (`autoId`, `userId`, `createdOn`, `createdBy`, `ownerUser`, `ownerDepartment`, `modifiedOn`, `modifiedBy`, `isDeleted`, `departmentId`, `userName`, `loginPwd`, `loginName`, `c_jobTitle`, `disabled`) VALUES
	(1, '021-000000000000000000000000000000000001', '2020-08-24 14:02:44', '021-000000000000000000000000000000000001', '021-000000000000000000000000000000000001', '022-000000000000000000000000000000000001', '2021-02-24 12:11:13', '021-000000000000000000000000000000000001', 0, '022-000000000000000000000000000000000001', '系统管理员', 'admin', 'admin', 1, 0),
	(3, '021-1a7fc78f-071e-400f-b780-629fc137e223', '2021-02-22 16:19:08', '021-000000000000000000000000000000000001', '021-000000000000000000000000000000000001', '022-000000000000000000000000000000000001', '2021-03-03 18:21:19', '021-000000000000000000000000000000000001', 0, '022-000000000000000000000000000000000001', 'test006', '123456', 'test006', 1, 0);
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
