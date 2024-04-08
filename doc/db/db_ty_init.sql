-- MySQL dump 10.13  Distrib 8.0.16, for Linux (x86_64)
--
-- Host: 172.17.0.6    Database: db_ty
-- ------------------------------------------------------
-- Server version	5.6.28-cdb2016-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ty_animation`
--

DROP TABLE IF EXISTS `ty_animation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_animation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `alias` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '别名',
  `icon` varchar(245) COLLATE utf8_bin DEFAULT NULL COMMENT '图片',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='动画';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_api`
--

DROP TABLE IF EXISTS `ty_api`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_api` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `alias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `enable` tinyint(2) DEFAULT NULL,
  `method` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '类型别名(TY接口、测试接口、还是物业接口…)',
  `url` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '接口地址，主要针对自定义接口',
  `category` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '分类（配置接口还是自定义接口）   config/custom',
  `authType` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '授权类型',
  `createrId` int(11) DEFAULT '0' COMMENT '创建人id',
  `createrName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人姓名',
  `owerId` int(11) DEFAULT '0' COMMENT '负责人ID',
  `owerName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '负责人名称',
  `content` longtext COLLATE utf8_bin COMMENT '乐高以及输入输出的相关配置数据',
  `version` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '1.0 为从apiLego解析\n2.0 为从content中解析',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3512 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_api_lego`
--

DROP TABLE IF EXISTS `ty_api_lego`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_api_lego` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `legoAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `oiAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `index` int(11) DEFAULT NULL,
  `attributes` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `uuid` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '唯一值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12084 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_api_log`
--

DROP TABLE IF EXISTS `ty_api_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_api_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `operatorId` int(11) DEFAULT NULL COMMENT '操作人ID',
  `operatorName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '操作人姓名',
  `operateDate` datetime DEFAULT NULL COMMENT '操作时间',
  `operateContent` longtext COLLATE utf8_bin COMMENT '详细的操作内容',
  `operateAlias` varchar(90) COLLATE utf8_bin DEFAULT NULL COMMENT '操作接口别名',
  `operateDes` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '操作描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9146 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_api_type`
--

DROP TABLE IF EXISTS `ty_api_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_api_type` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `alias` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `dss` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_app`
--

DROP TABLE IF EXISTS `ty_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_app` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `alias` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '别名',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `online` tinyint(2) DEFAULT '1' COMMENT '是否上线  1表示上线  0表示下线',
  `entrance` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '入口页',
  `buzzAlias` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT 'buzz别名',
  `icon` varchar(445) COLLATE utf8_bin DEFAULT NULL COMMENT 'ICON别名',
  `createrId` int(11) DEFAULT '0' COMMENT '创建人id',
  `createrName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人姓名',
  `owerId` int(11) DEFAULT '0' COMMENT '负责人ID',
  `owerName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '负责人名称',
  `createTenantSerialNumber` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `userType` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `platform` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `apiTypes` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_app_role`
--

DROP TABLE IF EXISTS `ty_app_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_app_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `appAlias` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '应用别名',
  `superFlag` tinyint(2) DEFAULT NULL COMMENT '是否超级角色',
  `resources` text COLLATE utf8_bin COMMENT '资源',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `alias` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '角色别名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_attribute_describe`
--

DROP TABLE IF EXISTS `ty_attribute_describe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_attribute_describe` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` text COLLATE utf8_bin,
  `buildingBlockAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `attributeName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dt` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `et` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL,
  `etData` longtext COLLATE utf8_bin,
  `etProps` longtext COLLATE utf8_bin,
  `type` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '积木属性类型   数据源设置 ds ，通用设置  common，外观设置 appearance，功能设置  function',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `rules` longtext COLLATE utf8_bin COMMENT '规则',
  `disabled` tinyint(4) DEFAULT NULL COMMENT '是否禁用',
  `supportXCX` tinyint(4) DEFAULT NULL COMMENT '是否支持小程序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1297 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_auth_type`
--

DROP TABLE IF EXISTS `ty_auth_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_auth_type` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL COMMENT '名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `alias` varchar(45) DEFAULT NULL COMMENT '别名',
  `judgeAPIAlias` varchar(45) DEFAULT NULL COMMENT '判断执行的API',
  `noAuthErrorCode` bigint(20) DEFAULT NULL COMMENT '没有权限的错误代码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_bb_type`
--

DROP TABLE IF EXISTS `ty_bb_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_bb_type` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `alias` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_building_block`
--

DROP TABLE IF EXISTS `ty_building_block`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_building_block` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `alias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `type` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '积木类型别名',
  `icon` varchar(125) COLLATE utf8_bin DEFAULT NULL COMMENT '图标',
  `platform` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '平台',
  `tags` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '标签',
  `createrId` int(11) DEFAULT '0' COMMENT '创建人id',
  `createrName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人姓名',
  `owerId` int(11) DEFAULT '0' COMMENT '负责人ID',
  `owerName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '负责人名称',
  `containerFlag` tinyint(2) DEFAULT '0' COMMENT '是否容器积木：1:是，0:否',
  `interactive` text COLLATE utf8_bin COMMENT '交互',
  `defaultProps` text COLLATE utf8_bin COMMENT '积木默认属性',
  `module` tinyint(4) DEFAULT NULL COMMENT '是否module',
  `content` text COLLATE utf8_bin COMMENT '模块内容(content格式)',
  `config` tinyint(4) DEFAULT NULL COMMENT '是否用于配置平台',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=250 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_code`
--

DROP TABLE IF EXISTS `ty_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `code` longtext COLLATE utf8_bin,
  `type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `alias` varchar(145) COLLATE utf8_bin DEFAULT NULL,
  `createrId` int(11) DEFAULT '0' COMMENT '创建人id',
  `createrName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人姓名',
  `owerId` int(11) DEFAULT '0' COMMENT '负责人ID',
  `owerName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '负责人名称',
  `appAlias` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=451 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_common_field`
--

DROP TABLE IF EXISTS `ty_common_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_common_field` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `fieldName` varchar(255) DEFAULT NULL,
  `dt` varchar(255) DEFAULT NULL,
  `pk` bit(1) DEFAULT NULL,
  `fieldValue` varchar(255) DEFAULT NULL,
  `fieldLength` int(11) DEFAULT NULL,
  `group` varchar(255) DEFAULT NULL COMMENT '分组',
  `uuid` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_connector`
--

DROP TABLE IF EXISTS `ty_connector`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_connector` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `alias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `fromOIAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `fromFieldName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `toOIAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `toFieldName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=586 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_data_describe`
--

DROP TABLE IF EXISTS `ty_data_describe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_data_describe` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `buildingBlockAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `variableName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dt` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_dict`
--

DROP TABLE IF EXISTS `ty_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_dict` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '字典类型  数据源类型（ds）,乐高分类(lego)，积木属性分类（bba）,积木类型（bb）',
  `alias` varchar(90) COLLATE utf8_bin DEFAULT NULL COMMENT '类型别名',
  `name` varchar(90) COLLATE utf8_bin DEFAULT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_ds`
--

DROP TABLE IF EXISTS `ty_ds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_ds` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `alias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dst` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `connectionUrl` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `connectionUsername` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `connectionPassword` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dsConfigFrom` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源链接信息从哪里获取',
  `createDate` datetime DEFAULT NULL,
  `createrId` int(11) DEFAULT '0' COMMENT '创建人id',
  `createrName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人姓名',
  `owerId` int(11) DEFAULT '0' COMMENT '负责人ID',
  `owerName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '负责人名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_event`
--

DROP TABLE IF EXISTS `ty_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pageBuildingBlockId` int(11) DEFAULT NULL,
  `eventName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `executeType` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `triggerBuildingBlockId` int(11) DEFAULT NULL,
  `triggerBuildingBlockMethodName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `triggerScript` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_event_describe`
--

DROP TABLE IF EXISTS `ty_event_describe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_event_describe` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `buildingBlockAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `eventName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=297 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_field`
--

DROP TABLE IF EXISTS `ty_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_field` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `fieldName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `oiAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dt` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `parentFieldId` int(11) DEFAULT NULL,
  `pk` bit(1) DEFAULT NULL,
  `fieldValue` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `fieldLength` int(11) DEFAULT NULL,
  `group` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '分组',
  `uuid` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20858 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_file_storage`
--

DROP TABLE IF EXISTS `ty_file_storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_file_storage` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `oiAlias` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT 'oi别名',
  `serializeName` varchar(254) COLLATE utf8_bin DEFAULT NULL COMMENT '序列化文件名',
  `originalName` varchar(254) COLLATE utf8_bin DEFAULT NULL COMMENT '原始文件名',
  `fileSize` int(11) DEFAULT NULL COMMENT '文件大小',
  `fileType` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '文件类型',
  `contentType` varchar(125) COLLATE utf8_bin DEFAULT NULL COMMENT '内容类型',
  `fileUrl` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '文件下载地址',
  `uploadType` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '上传类型：oss(oss上传),fastdfs(fastdfs上传),server(服务器上传)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3584 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_handle`
--

DROP TABLE IF EXISTS `ty_handle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_handle` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `alias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `createrId` int(11) DEFAULT '0' COMMENT '创建人id',
  `createrName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人姓名',
  `owerId` int(11) DEFAULT '0' COMMENT '负责人ID',
  `owerName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '负责人名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_icon`
--

DROP TABLE IF EXISTS `ty_icon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_icon` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `iconClass` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'icon 图标别名',
  `type` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '类型，ty、wy、',
  `createDate` datetime DEFAULT NULL,
  `createUser` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_input_field`
--

DROP TABLE IF EXISTS `ty_input_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_input_field` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `apiLegoId` int(11) DEFAULT NULL,
  `fieldName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `constant` text COLLATE utf8_bin,
  `ct` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ift` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `fvt` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `fromApiLegoId` int(11) DEFAULT NULL,
  `alias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `attributes` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `validate` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `connectorPath` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dt` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `inputFieldDescribeId` int(11) DEFAULT NULL,
  `requestParamName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sessionParamName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `fromApiLegoOutputFieldAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `fieldTpl` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '字段值模板',
  `uuid` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '唯一值',
  `apiLegoUuid` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'apilego唯一值',
  `fromApiLegoUuid` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'FVT为output时使用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=110637 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_input_field_describe`
--

DROP TABLE IF EXISTS `ty_input_field_describe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_input_field_describe` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `legoAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `fieldName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `multiple` bit(1) DEFAULT NULL,
  `ift` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `fromOIFields` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=290 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_interactive`
--

DROP TABLE IF EXISTS `ty_interactive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_interactive` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pbbId` int(11) DEFAULT NULL,
  `triggerEventName` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `executeType` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `executePbbId` int(11) DEFAULT NULL,
  `executeBBMethodName` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `executeScript` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `containerMethodName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '容器方法名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=251 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_lego`
--

DROP TABLE IF EXISTS `ty_lego`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_lego` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `alias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `supportDST` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `icon` varchar(125) COLLATE utf8_bin DEFAULT NULL COMMENT '图标',
  `generateSupport` tinyint(2) DEFAULT NULL COMMENT '是否支持一键生成',
  `useNum` int(11) DEFAULT NULL COMMENT '使用数量',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `createrId` int(11) DEFAULT '0' COMMENT '创建人id',
  `createrName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人姓名',
  `owerId` int(11) DEFAULT '0' COMMENT '负责人ID',
  `owerName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '负责人名称',
  `type` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '乐高类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_lego_attribute`
--

DROP TABLE IF EXISTS `ty_lego_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_lego_attribute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `legoAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `defaultValue` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_log`
--

DROP TABLE IF EXISTS `ty_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `operateUserAlias` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `operateUserName` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `operateApi` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `operateLego` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `input` mediumtext COLLATE utf8_bin,
  `output` mediumtext COLLATE utf8_bin,
  `content` mediumtext COLLATE utf8_bin,
  `operateOi` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=105538 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_method_describe`
--

DROP TABLE IF EXISTS `ty_method_describe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_method_describe` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `buildingBlockAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `methodName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=228 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_model_log`
--

DROP TABLE IF EXISTS `ty_model_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_model_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `operatorId` int(11) DEFAULT NULL COMMENT '操作人ID',
  `operatorName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '操作人姓名',
  `operateDate` datetime DEFAULT NULL COMMENT '操作时间',
  `operateContent` longtext COLLATE utf8_bin COMMENT '详细的操作内容',
  `operateAlias` varchar(90) COLLATE utf8_bin DEFAULT NULL COMMENT '操作模型别名',
  `operateDes` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '操作描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8164 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_module`
--

DROP TABLE IF EXISTS `ty_module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `description` text COLLATE utf8_bin,
  `alias` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `icon` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `createrId` int(11) DEFAULT NULL,
  `createrName` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `ownerId` int(11) DEFAULT NULL,
  `ownerName` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `content` text COLLATE utf8_bin,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_oi`
--

DROP TABLE IF EXISTS `ty_oi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_oi` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `alias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `resource` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dsAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1841 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_output_field`
--

DROP TABLE IF EXISTS `ty_output_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_output_field` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `apiLegoId` int(11) DEFAULT NULL,
  `fieldName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `fieldValue` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `oft` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `response` bit(1) DEFAULT NULL,
  `handle` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `alias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dt` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `outputFieldDescribeId` int(11) DEFAULT NULL,
  `uuid` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '唯一值',
  `apiLegoUuid` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'apilego唯一值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16250 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_output_field_describe`
--

DROP TABLE IF EXISTS `ty_output_field_describe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_output_field_describe` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `legoAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `fieldName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `oft` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `multiple` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=156 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_owner`
--

DROP TABLE IF EXISTS `ty_owner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_owner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `alias` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '对应的别名',
  `ownerType` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'owner的类型app,ds',
  `ownerCode` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'owner的用户ID',
  `ownerName` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'owner名称',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `creatorCode` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人ID',
  `creatorName` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人姓名',
  `deleteStatus` tinyint(4) DEFAULT NULL COMMENT '删除标记，0=存在，1=删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3335 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_page`
--

DROP TABLE IF EXISTS `ty_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_page` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `alias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `layout` longtext COLLATE utf8_bin,
  `layoutType` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `template` tinyint(4) DEFAULT '0',
  `templatePageAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `custom` tinyint(4) DEFAULT '0',
  `customFile` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `appAlias` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '应用别名',
  `parentId` int(11) DEFAULT NULL COMMENT '父页面id',
  `enable` tinyint(2) DEFAULT NULL COMMENT '是否有效',
  `type` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '类型：category,menu,sub',
  `icon` varchar(125) COLLATE utf8_bin DEFAULT NULL COMMENT '图标',
  `sort` int(8) DEFAULT NULL COMMENT '排序',
  `ds` text COLLATE utf8_bin COMMENT '数据源',
  `platform` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '所属平台',
  `createrId` int(11) DEFAULT '0' COMMENT '创建人id',
  `createrName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人姓名',
  `owerId` int(11) DEFAULT '0' COMMENT '负责人ID',
  `owerName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '负责人名称',
  `layoutObject` longtext COLLATE utf8_bin COMMENT '属性json\n',
  `content` longtext COLLATE utf8_bin COMMENT 'pub列表内容',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1551 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_page_building_block`
--

DROP TABLE IF EXISTS `ty_page_building_block`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_page_building_block` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pageAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `buildingBlockAlias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `attributes` longtext COLLATE utf8_bin,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2863 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_page_log`
--

DROP TABLE IF EXISTS `ty_page_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_page_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `operatorId` int(11) DEFAULT NULL COMMENT '操作人ID',
  `operatorName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '操作人姓名',
  `operateDate` datetime DEFAULT NULL COMMENT '操作时间',
  `operateContent` longtext COLLATE utf8_bin COMMENT '详细的操作内容',
  `operateAlias` varchar(90) COLLATE utf8_bin DEFAULT NULL COMMENT '操作页面别名',
  `operateDes` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '操作描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2463 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_server`
--

DROP TABLE IF EXISTS `ty_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_server` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `description` text COLLATE utf8_bin,
  `host` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `apiTypes` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `apps` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_supplier`
--

DROP TABLE IF EXISTS `ty_supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_supplier` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `supplierCommpanySerialNumber` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `xingMing` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `account` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `alias` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_supplier_app`
--

DROP TABLE IF EXISTS `ty_supplier_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_supplier_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `supplierSerialNumber` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `appAlias` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_supplier_company`
--

DROP TABLE IF EXISTS `ty_supplier_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_supplier_company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serialNumber` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `tenantSerialNumber` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `companyName` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `logo` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_task`
--

DROP TABLE IF EXISTS `ty_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_task` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL COMMENT '名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `alias` varchar(45) DEFAULT NULL COMMENT '别名',
  `group` varchar(45) DEFAULT NULL COMMENT '组别',
  `cron` varchar(45) DEFAULT NULL COMMENT '执行规则',
  `status` varchar(45) DEFAULT NULL COMMENT '状态',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `apiAlias` varchar(45) DEFAULT NULL COMMENT '接口别名',
  `createrId` int(11) DEFAULT '0' COMMENT '创建人id',
  `createrName` varchar(45) DEFAULT NULL COMMENT '创建人姓名',
  `owerId` int(11) DEFAULT '0' COMMENT '负责人ID',
  `owerName` varchar(45) DEFAULT NULL COMMENT '负责人名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_task_log`
--

DROP TABLE IF EXISTS `ty_task_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_task_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `taskId` int(11) DEFAULT NULL COMMENT '任务id',
  `result` varchar(255) DEFAULT NULL COMMENT '执行结果',
  `timeConsuming` bigint(11) DEFAULT NULL COMMENT '耗时',
  `begin` datetime DEFAULT NULL COMMENT '开始时间',
  `finish` datetime DEFAULT NULL COMMENT '结束时间',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `remark` mediumtext COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2309 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_tenant`
--

DROP TABLE IF EXISTS `ty_tenant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_tenant` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `serialNumber` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '序号	',
  `companyName` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '公司名称',
  `logo` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_tenant_app`
--

DROP TABLE IF EXISTS `ty_tenant_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_tenant_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tenantSerialNumber` varchar(256) COLLATE utf8_bin NOT NULL COMMENT '租户序列号',
  `appAlias` varchar(256) COLLATE utf8_bin NOT NULL COMMENT 'APP序列号',
  `createDate` datetime DEFAULT NULL COMMENT '安装日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_user`
--

DROP TABLE IF EXISTS `ty_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `xingMing` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `userName` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '密码',
  `alias` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '用户别名(可能是工号或者其他)',
  `tenantSerialNumber` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12325 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='ty用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_user_app_role`
--

DROP TABLE IF EXISTS `ty_user_app_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_user_app_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `userAlias` varchar(64) DEFAULT NULL,
  `appAlias` varchar(64) DEFAULT NULL,
  `roles` varchar(64) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=945 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ty_validation`
--

DROP TABLE IF EXISTS `ty_validation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ty_validation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `alias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `createrId` int(11) DEFAULT '0' COMMENT '创建人id',
  `createrName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人姓名',
  `owerId` int(11) DEFAULT '0' COMMENT '负责人ID',
  `owerName` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '负责人名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-06-05 11:29:39
