-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill;
-- 使用数据库
use seckill;
CREATE TABLE `seckill` (
  `seckill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品库存ID',
  `name` varchar(120) NOT NULL COMMENT '商品名称',
  `number` int(11) NOT NULL COMMENT '库存数量',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '秒杀开始时间',
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';
/*!40101 SET character_set_client = @saved_cs_client */;


-- 初始化数据
INSERT INTO `seckill` VALUES 
(1000,'1000元秒杀iphone6',93,'2018-08-15 07:17:55','2018-08-16 16:00:00','2018-08-07 02:16:27'),
(1001,'800元秒杀ipad',198,'2018-08-16 08:42:44','2018-08-16 16:00:00','2018-08-07 02:16:27'),
(1002,'6600元秒杀mac book pro',299,'2018-08-16 09:00:10','2018-08-16 16:00:00','2018-08-07 02:16:27'),
(1003,'7000元秒杀iMac',399,'2018-08-16 06:12:05','2018-08-16 16:00:00','2018-08-07 02:16:27');


-- 秒杀成功明细表
-- 用户登录认证相关信息(简化为手机号)
CREATE TABLE `success_killed` (
  `seckill_id` bigint(20) NOT NULL COMMENT '秒杀商品ID',
  `user_phone` bigint(20) NOT NULL COMMENT '用户手机号',
  `state` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '状态标识:-1:无效 0:成功 1:已付款 2:已发货',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`,`user_phone`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';