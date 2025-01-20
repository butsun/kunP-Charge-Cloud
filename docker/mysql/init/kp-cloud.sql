DROP DATABASE IF EXISTS `kp-cloud`;

CREATE DATABASE  `kp-cloud` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `kp-cloud`;

/*
 Navicat Premium Data Transfer

 Source Server         : CSMS
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : 116.204.75.140:3306
 Source Schema         : kp-cloud

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 20/01/2025 10:52:58
*/
-- ----------------------------
-- 充电站点表
-- ----------------------------
DROP TABLE IF EXISTS `kp_station`;
CREATE TABLE `kp_station`
(
    `id`              int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `station_id`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '充电站id(运营商自定义的唯一编码)',
    `operator_id`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '运营商id(组织机构代码)',
    `station_name`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '充电站名称',
    `area_code`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '充电站省市辖区编码',
    `address`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '详细地址',
    `station_tel`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '站点电话(能够联系场站工作人员进行协助的联系电话)',
    `service_tel`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '服务电话(平台服务电话，例如400的电话)',
    `station_type`    tinyint unsigned NOT NULL DEFAULT '0' COMMENT '站点类型:1、公共;50、个人;100、公交(专用);101、环卫(专用);102、物流(专用);103、出租车(专用);255、其他',
    `station_status`  tinyint unsigned NOT NULL DEFAULT '0' COMMENT '站点状态:0、未知;1、建设中;5、关闭下线;6、维护中;50、正常使用',
    `park_nums`       int unsigned NOT NULL DEFAULT '0' COMMENT '车位数量(可停放进行充电的车位总数，默认：0 未知)',
    `station_lng`     decimal(10, 6) unsigned NOT NULL DEFAULT '0.000000' COMMENT '站点经度(GCJ-02坐标系,保留小数点后6位)',
    `station_lat`     decimal(10, 6) unsigned NOT NULL DEFAULT '0.000000' COMMENT '站点纬度(GCJ-02坐标系,保留小数点后6位)',
    `construction`    tinyint unsigned NOT NULL DEFAULT '0' COMMENT '建设场所类型:1、居民区;2、公共机构;3、企事业单位;4、写字楼;5、工业园区;6、交通枢纽;7、大型文体设施;8、城市绿地;9、大型建筑配建停车场;10、路边停车位;11、城际高速服务区;255、其他',
    `pictures`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '站点照片(充电设备照片、充电车位照片、停车场入口照片)JSON串',
    `busine_hours`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '营业时间描述',
    `electricity_fee` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电电费描述',
    `service_fee`     varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '服务费描述',
    `park_fee`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '停车费描述',
    `payment`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '支付方式::刷卡、线上、现金。其中电子钱包类卡为刷卡，身份鉴权卡、微信/支付宝、APP为线上',
    `support_order`   tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否支持预约(充电设备是否需要提前预约后才能使用。0为不支持预约;1为支持预约。不填默认为0)',
    `remark`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注信息',
    `del_flag`        tinyint unsigned NOT NULL DEFAULT '0' COMMENT '数据状态:0、正常;1、删除',
    `create_dept`     bigint(20)      default null               comment '创建部门',
    `create_by`       bigint(20)      default null               comment '创建者',
    `create_time`     datetime comment '创建时间',
    `update_by`       bigint(20)      default null               comment '更新者',
    `update_time`     datetime comment '更新时间',
    `tenant_id`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY               `station_id` (`station_id`),
    KEY               `operator_id` (`operator_id`),
    KEY               `area_code` (`area_code`)
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充电站信息表';



-- ----------------------------
-- 充电设备表
-- ----------------------------
DROP TABLE IF EXISTS `kp_equipment`;
CREATE TABLE `kp_equipment`
(
    `id`                   bigint unsigned NOT NULL AUTO_INCREMENT,
    `equipment_id`         varchar(23) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备唯一编码，对同一运营商保证唯一；9组织机构+14桩编号',
    `pile_no`              varchar(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '桩编号',
    `station_id`           varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联充电站ID，运营商自定义的唯一编码 小于等于20字符',
    `price_code`           bigint unsigned DEFAULT '0' COMMENT '价格模版ID',
    `manufacturer_id`      varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT '' COMMENT '设备生产商组织机构代码',
    `manufacturer_name`    varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT '' COMMENT '设备生产商名称',
    `equipment_model`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT '' COMMENT '设备型号',
    `voltage_upper_limits` smallint unsigned NOT NULL DEFAULT '0' COMMENT '额定电压上限(单位:V)',
    `voltage_lower_limits` smallint unsigned NOT NULL DEFAULT '0' COMMENT '额定电压下限(单位:V)',
    `current_value`        smallint unsigned NOT NULL COMMENT '额定电流(单位:A)',
    `power`                decimal(6, 2) unsigned NOT NULL DEFAULT '0.00' COMMENT '额定功率(单位:kW)',
    `equipment_type`       tinyint                                                      NOT NULL COMMENT '1直流设备 2交流设备 3交直流一体设备 4无线设备 5其他',
    `park_no`              varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '车位号(停车场车位编号)',
    `max_power`            decimal(3, 2) unsigned DEFAULT '1.00' COMMENT '充电桩最大允许输出功率 30%-100% 1Bin表示1%',
    `is_working`           tinyint unsigned DEFAULT '0' COMMENT '是否启用 0 正常工作 1停止使用锁定',
    `sync_tm`              datetime                                                              DEFAULT NULL COMMENT '最近对时时间',
    `equipment_name`       varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '充电设备名称',
    `net_type`             tinyint unsigned DEFAULT '3' COMMENT '0、sim卡 1、LAN 2、WAN 3其他',
    `m_operator`           tinyint(1) DEFAULT '4' COMMENT '0移动 2电信 3联通 4其他',
    `online_tm`            datetime                                                              DEFAULT NULL COMMENT '最近上线时间（登录验证）',
    `serv_ip`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT '' COMMENT '连接主机IP',
    `create_dept`          bigint(20)      default null               comment '创建部门',
    `create_by`            bigint(20)      default null               comment '创建者',
    `create_time`          datetime comment '创建时间',
    `update_by`            bigint(20)      default null               comment '更新者',
    `update_time`          datetime comment '更新时间',
    `del_flag`             tinyint(1) DEFAULT '0' COMMENT '删除标记',
    `tenant_id`            varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                    `equipment_id` (`equipment_id`) USING BTREE COMMENT '充电设备编号索引',
    KEY                    `sys_equipment_station_id_IDX` (`station_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='充电设备信息表';



-- ----------------------------
-- 充电枪口表 充电最小单位
-- ----------------------------
DROP TABLE IF EXISTS `kp_connector`;
CREATE TABLE `kp_connector`
(
    `id`                bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `station_id`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电站id(运营商自定义的唯一编码)',
    `operator_id`       varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '基础运营商id(组织机构代码)',
    `equipment_id`      varchar(23) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '设备编码(设备唯一编码，对同一运营商，保证唯一)',
    `connector_id`      varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电设备接口编码(充电设备接口编码，同一运营商内唯一)',
    `connector_name`    varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '充电设备接口名称',
    `connector_type`    tinyint unsigned NOT NULL DEFAULT '0' COMMENT '充电设备接口类型:1、家用插座(模式2);2、交流接口插座(模式3，连接方式B);3、交流接口插头(带枪线，模式3，连接方式C);4、直流接口枪头(带枪线，模式4);5、无线充电座;6、其他',
    `national_standard` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '国家标准:1、2011;2、2015',
    `status`            tinyint unsigned NOT NULL DEFAULT '0' COMMENT '充电设备接口状态:0、离网;1、空闲;2、占用(未充电);3、占用(充电中);4、占用(预约锁定);255、故障',
    `park_status`       tinyint unsigned NOT NULL DEFAULT '0' COMMENT '车位状态:0:未知;10:空闲;50:已上锁',
    `lock_status`       tinyint unsigned NOT NULL DEFAULT '0' COMMENT '地锁状态:0:未知;10:已解锁;50:已上锁',
    `del_flag`          tinyint unsigned NOT NULL DEFAULT '0' COMMENT '数据状态:0、正常;1、删除',
    `create_dept`       bigint(20)      default null               comment '创建部门',
    `create_by`         bigint(20)      default null               comment '创建者',
    `create_time`       datetime comment '创建时间',
    `update_by`         bigint(20)      default null               comment '更新者',
    `update_time`       datetime comment '更新时间',
    `tenant_id`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                 `equipment_id` (`equipment_id`),
    KEY                 `connector_id` (`connector_id`),
    KEY                 `station_id` (`station_id`),
    KEY                 `operator_id` (`operator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4532 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充电设备接口信息表';



-- ----------------------------
-- 充电价格模版表
-- ----------------------------
DROP TABLE IF EXISTS `kp_price_template`;
CREATE TABLE `kp_price_template`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `station_id`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '充电站ID',
    `price_code`    bigint                                                        NOT NULL DEFAULT '0' COMMENT '价格模版ID，0为默认价格',
    `start_time`    datetime                                                      NOT NULL COMMENT '时段起始时间点 6位 HHmmss',
    `price_type`    tinyint unsigned NOT NULL DEFAULT '0' COMMENT '价格类型:0、尖;1、峰;2、平;3、谷;',
    `elec_price`    decimal(10, 4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '电价:XXXX.XXXX',
    `service_price` decimal(10, 4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '服务费单价:XXXX.XXXX',
    `remark`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
    `del_flag`      tinyint unsigned NOT NULL DEFAULT '0' COMMENT '数据状态:0、正常;1、删除',
    `create_dept`   bigint(20)      default null               comment '创建部门',
    `create_by`     bigint(20)      default null               comment '创建者',
    `create_time`   datetime comment '创建时间',
    `update_by`     bigint(20)      default null               comment '更新者',
    `update_time`   datetime comment '更新时间',
    `tenant_id`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充电价格模版表';



-- ----------------------------
-- 充电凭证表
-- ----------------------------
DROP TABLE IF EXISTS `kp_charge_voucher`;
CREATE TABLE `kp_charge_voucher`
(
    `id`             bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `voucher_number` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '凭证编号',
    `operator_id`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '归属公司ID',
    `voucher_type`   tinyint unsigned NOT NULL  DEFAULT '0' COMMENT '凭证类型:0 手机号; 1、卡; 2、 VIN;',
    `account_id`     bigint unsigned NOT NULL DEFAULT '0' COMMENT '归属账户ID',
    `disable_flag`   tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否禁用凭证:0、启用;1、禁用',
    `remark`         varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
    `del_flag`       tinyint unsigned NOT NULL DEFAULT '0' COMMENT '数据状态:0、正常;1、删除',
    `create_dept`    bigint(20)      default null               comment '创建部门',
    `create_by`      bigint(20)      default null               comment '创建者',
    `create_time`    datetime comment '创建时间',
    `update_by`      bigint(20)      default null               comment '更新者',
    `update_time`    datetime comment '更新时间',
    `tenant_id`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充电凭证表';


-- ----------------------------
-- 充电账户表
-- ----------------------------
DROP TABLE IF EXISTS `kp_charge_account`;
CREATE TABLE `kp_charge_account`
(
    `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `mobile`          varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '用户手机号',
    `nick_name`       varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '用户昵称',
    `sex`             tinyint(1) NOT NULL DEFAULT '0' COMMENT '0、未知;1、男;2、女',
    `accout_type`     tinyint(1) NOT NULL DEFAULT '0' COMMENT '0、个人;1、集团;',
    `disable_flag`    tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否禁用用户:0、启用;1、禁用',
    `register_time`   datetime                                                               DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `last_visit_time` datetime                                                               DEFAULT CURRENT_TIMESTAMP COMMENT '最近一次访问时间',
    `remark`          varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
    `del_flag`        tinyint unsigned NOT NULL DEFAULT '0' COMMENT '数据状态:0、正常;1、删除',
    `create_dept`     bigint(20)      default null               comment '创建部门',
    `create_by`       bigint(20)      default null               comment '创建者',
    `create_time`     datetime comment '创建时间',
    `update_by`       bigint(20)      default null               comment '更新者',
    `update_time`     datetime comment '更新时间',
    `tenant_id`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY               `mobile` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充电账户表';



-- ----------------------------
-- 充电用户车辆表
-- ----------------------------
DROP TABLE IF EXISTS `kp_user_car`;
CREATE TABLE `kp_user_car`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '车辆ID',
    `account_id`    bigint unsigned NOT NULL DEFAULT '0' COMMENT '归属账户ID',
    `plate_no`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '车牌号',
    `car_vin`       varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '车辆vin码',
    `car_model`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '品牌型号',
    `owner`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '车辆所有人',
    `use_character` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL DEFAULT '' COMMENT '使用性质:运营、非运营',
    `license_imgs`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '行驶证图片json串',
    `check_state`   tinyint unsigned NOT NULL DEFAULT '0' COMMENT '审核状态:0、待审核;1、审核通过;2、审核不通过;3、不需审核',
    `auth_state`    tinyint unsigned NOT NULL DEFAULT '0' COMMENT '认证状态:0、不认证;1、待认证;2、认证通过;3、认证不通过',
    `remark`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
    `del_flag`      tinyint unsigned NOT NULL DEFAULT '0' COMMENT '数据状态:0、正常;1、删除',
    `create_dept`   bigint(20)      default null               comment '创建部门',
    `create_by`     bigint(20)      default null               comment '创建者',
    `create_time`   datetime comment '创建时间',
    `update_by`     bigint(20)      default null               comment '更新者',
    `update_time`   datetime comment '更新时间',
    `tenant_id`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY             `plate_no` (`plate_no`),
    KEY             `car_vin` (`car_vin`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户的车辆';



-- ----------------------------
-- 充电订单表
-- ----------------------------
DROP TABLE IF EXISTS `kp_charge_order`;
CREATE TABLE `kp_charge_order`
(
    `id`                    bigint unsigned NOT NULL AUTO_INCREMENT,
    `start_charge_seq`      varchar(27) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '充电订单号：运营商ID+唯一编号 27个字符； 组织机构9位+id18位',
    `operator_id`           varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NOT NULL DEFAULT '' COMMENT '运营商ID',
    `trade_no`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci            DEFAULT '' COMMENT '交易流水号（基础平台和桩的规则 32位 16位BCD）',
    `start_charge_seq_stat` tinyint                                                        NOT NULL DEFAULT '5' COMMENT '充电订单状态；1启动中 2充电中 3停止中 4已结束 5未知',
    `station_id`            varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '关联充电站ID，运营商自定义的唯一编码 小于等于20字符',
    `connector_id`          varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '充电设备接口编码',
    `connector_status`      int                                                            NOT NULL DEFAULT '1' COMMENT '1空闲 2占用（未充电） 3占用（充电中） 4占用（预约锁定） 255故障',
    `current_a`             decimal(10, 1)                                                 NOT NULL DEFAULT '0.0' COMMENT 'A相电流 单位A 默认0，含直流（输出）',
    `current_b`             decimal(10, 1)                                                          DEFAULT '0.0' COMMENT 'B相电流',
    `current_c`             decimal(10, 1)                                                          DEFAULT '0.0' COMMENT 'C相电流',
    `voltage_a`             decimal(10, 1)                                                 NOT NULL DEFAULT '0.0' COMMENT 'A相电压 单位V 默认0，含直流（输出）',
    `voltage_b`             decimal(10, 1)                                                          DEFAULT '0.0' COMMENT 'B相电压',
    `voltage_c`             decimal(10, 1)                                                          DEFAULT '0.0' COMMENT 'C相电压',
    `soc`                   decimal(10, 4)                                                 NOT NULL DEFAULT '0.0000' COMMENT '电池剩余电量0-1.00',
    `start_time`            datetime                                                       NOT NULL COMMENT '开始充电时间',
    `end_time`              datetime                                                                DEFAULT NULL COMMENT '最新采样时间',
    `total_power`           decimal(10, 2)                                                 NOT NULL DEFAULT '0.00' COMMENT '累计充电量（度）',
    `elec_money`            decimal(10, 2)                                                          DEFAULT '0.00' COMMENT '累计电费（元）',
    `service_money`         decimal(10, 2)                                                          DEFAULT '0.00' COMMENT '累计服务费（元）',
    `total_money`           decimal(10, 2)                                                          DEFAULT '0.00' COMMENT '累计总金额（元）',
    `fail_reason`           int                                                                     DEFAULT '0' COMMENT '故障原因 0无 1此设备不存在 2此设备离线 3设备已停止充电 4-99自定义（参考12.1 充电停止原因代码表）',
    `car_vin`               varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci            DEFAULT '' COMMENT 'vin码',
    `create_time`           datetime                                                                DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`           datetime                                                                DEFAULT NULL COMMENT '更新时间',
    `del_flag`              tinyint(1) DEFAULT '0' COMMENT '删除标记',
    `plate_num`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '车牌号',
    `phone_num`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '手机号',
    `price_info`            varchar(3200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单时计价',
    `tenant_id`             varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci            DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                     `sys_charge_order_trade_no_IDX` (`trade_no`) USING BTREE,
    KEY                     `sys_charge_order_start_charge_seq_IDX` (`start_charge_seq`) USING BTREE,
    KEY                     `sys_charge_order_connector_id_IDX` (`connector_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='充电订单信息表';



-- ----------------------------
-- 充电运营商表
-- ----------------------------
DROP TABLE IF EXISTS `kp_operator`;
CREATE TABLE `kp_operator`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT,
    `operator_id` varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '运营商ID（组织机构代码）',
    `country`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '国家',
    `province`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '省',
    `city`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '城市',
    `create_dept` bigint(20)      default null               comment '创建部门',
    `create_by`   bigint(20)      default null               comment '创建者',
    `create_time` datetime comment '创建时间',
    `update_by`   bigint(20)      default null               comment '更新者',
    `update_time` datetime comment '更新时间',
    `del_flag`    tinyint(1) DEFAULT '0' COMMENT '删除标记',
    `tenant_id`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `operator_id` (`operator_id`) USING BTREE COMMENT '运营商组织机构索引'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='基础设施运营商信息表';


-- ----------------------------
-- 充电优惠表
-- ----------------------------
DROP TABLE IF EXISTS `kp_discount_activity`;
CREATE TABLE `kp_discount_activity`
(
    `id`              bigint unsigned NOT NULL AUTO_INCREMENT,
    `activity_name`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '活动简称',
    `operator_id`     varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '运营商ID（组织机构代码）',
    `station_id`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '充电站id(运营商自定义的唯一编码)',
    `dis_service`     decimal(3, 2) unsigned DEFAULT '1.00' COMMENT '服务费用折扣率 0%-100% 1Bin表示1%',
    `dis_electricity` decimal(3, 2) unsigned DEFAULT '1.00' COMMENT '充电费用折扣率 30%-100% 1Bin表示1%',
    `disable_flag`    tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否禁用:0、启用;1、禁用',
    `activity_type`   tinyint unsigned NOT NULL DEFAULT '0' COMMENT '活动类型:0、用户活动;1、集团活动',
    `remark`          varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
    `create_dept`     bigint(20)      default null               comment '创建部门',
    `create_by`       bigint(20)      default null               comment '创建者',
    `create_time`     datetime comment '创建时间',
    `update_by`       bigint(20)      default null               comment '更新者',
    `update_time`     datetime comment '更新时间',
    `del_flag`        tinyint(1) DEFAULT '0' COMMENT '删除标记',
    `tenant_id`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE,
    KEY               `operator_id` (`operator_id`) USING BTREE COMMENT '运营商组织机构索引'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='充电优惠表';

