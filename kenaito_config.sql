/*
 Navicat Premium Data Transfer

 Source Server         : kenaito-mysql.odboy.local
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : kenaito-mysql.odboy.local:13306
 Source Schema         : kenaito_config

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 09/12/2024 17:30:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_app
-- ----------------------------
DROP TABLE IF EXISTS `config_app`;
CREATE TABLE `config_app`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `app_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '配置应用' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_app
-- ----------------------------
INSERT INTO `config_app` VALUES (1, 'admin', '2024-12-05 21:44:19', 'admin', '2024-12-05 21:44:19', 'kenaito-config-demo', '示范应用');

-- ----------------------------
-- Table structure for config_app_env
-- ----------------------------
DROP TABLE IF EXISTS `config_app_env`;
CREATE TABLE `config_app_env`  (
  `app_id` bigint(0) NOT NULL COMMENT '应用id',
  `env_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '环境编码',
  PRIMARY KEY (`app_id`, `env_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '配置环境' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_app_env
-- ----------------------------
INSERT INTO `config_app_env` VALUES (1, 'daily');
INSERT INTO `config_app_env` VALUES (1, 'prod');
INSERT INTO `config_app_env` VALUES (1, 'stage');
INSERT INTO `config_app_env` VALUES (1, 'test');

-- ----------------------------
-- Table structure for config_file
-- ----------------------------
DROP TABLE IF EXISTS `config_file`;
CREATE TABLE `config_file`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(0) NOT NULL COMMENT 'configAppId',
  `env_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '环境编码',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '例如: application-daily.properties',
  `version` bigint(0) NOT NULL DEFAULT 1 COMMENT '当前配置内容版本',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '配置文件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_file
-- ----------------------------
INSERT INTO `config_file` VALUES (1, 1, 'daily', 'application-daily.properties', 3, 'admin', '2024-12-05 21:27:38', 'admin', '2024-12-09 16:26:05');
INSERT INTO `config_file` VALUES (2, 1, 'stage', 'application-stage.properties', 1, 'admin', '2024-12-05 21:27:38', 'admin', '2024-12-05 21:27:38');
INSERT INTO `config_file` VALUES (3, 1, 'prod', 'application-prod.properties', 1, 'admin', '2024-12-05 21:27:38', 'admin', '2024-12-05 21:27:38');

-- ----------------------------
-- Table structure for config_user
-- ----------------------------
DROP TABLE IF EXISTS `config_user`;
CREATE TABLE `config_user`  (
  `app_id` bigint(0) NOT NULL COMMENT 'configAppId',
  `env_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` bigint(0) NOT NULL COMMENT 'systemUserId',
  PRIMARY KEY (`app_id`, `user_id`, `env_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_user
-- ----------------------------
INSERT INTO `config_user` VALUES (1, 'daily', 1);
INSERT INTO `config_user` VALUES (1, 'prod', 1);
INSERT INTO `config_user` VALUES (1, 'stage', 1);

-- ----------------------------
-- Table structure for config_version
-- ----------------------------
DROP TABLE IF EXISTS `config_version`;
CREATE TABLE `config_version`  (
  `file_id` bigint(0) NOT NULL,
  `file_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `file_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'yaml或者properties',
  `version` bigint(0) NOT NULL,
  PRIMARY KEY (`file_id`, `version`, `file_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '配置内容版本' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_version
-- ----------------------------
INSERT INTO `config_version` VALUES (1, 'server.port=28018\r\nserver.http2.enabled=true\r\nserver.undertow.threads.worker=16\r\nserver.undertow.threads.io=2\r\nserver.undertow.direct-buffers=true\r\nserver.undertow.buffer-size=1024\r\nserver.undertow.accesslog.enabled=false\r\nserver.compression.enabled=true\r\nserver.compression.mime-types=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json\r\nserver.shutdown=graceful\r\nmybatis-plus.configuration.cache-enabled=false\r\nmybatis-plus.configuration.local-cache-scope=STATEMENT\r\nmybatis-plus.configuration.map-underscore-to-camel-case=true\r\nmybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl\r\nmybatis-plus.check-config-location=true\r\nmybatis-plus.mapper-locations=classpath*:/mapper/**/*.xml\r\nmybatis-plus.global-config.db-config.logic-delete-value=0\r\nmybatis-plus.global-config.db-config.logic-not-delete-value=1\r\nmybatis-plus.global-config.db-config.id-type=auto\r\nmybatis-plus.global-config.db-config.table-underline=true\r\nmybatis-plus.global-config.banner=true\r\nspring.freemarker.check-template-location=false\r\nspring.freemarker.charset=utf-8\r\nspring.data.redis.repositories.enabled=false\r\nspring.lifecycle.timeout-per-shutdown-phase=30s\r\nspring.mail.host=smtp.qq.com\r\nspring.mail.port=465\r\nspring.mail.username=tianjun@odboy.cn\r\nspring.mail.password=xxxxxxxxxxxx\r\nspring.mail.default-encoding=UTF-8\r\nspring.mail.properties.mail.smtp.ssl.trust=smtp.qq.com\r\nspring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory\r\nspring.mail.properties.mail.smtp.socketFactory.port=465\r\nspring.mail.properties.mail.smtp.auth=true\r\nspring.mail.properties.mail.smtp.starttls.enable=true\r\nspring.mail.properties.mail.smtp.starttls.required=true\r\nspring.datasource.druid.db-type=com.alibaba.druid.pool.DruidDataSource\r\nspring.datasource.druid.driverClassName=net.sf.log4jdbc.sql.jdbcapi.DriverSpy\r\nspring.datasource.druid.url=jdbc:log4jdbc:mysql://${DB_HOST:kenaito-mysql.odboy.local}:${DB_PORT:13306}/${DB_NAME:kenaito_config}?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true\r\nspring.datasource.druid.username=${DB_USER:root}\r\nspring.datasource.druid.password=${DB_PWD:root}\r\nspring.datasource.druid.initial-size=5\r\nspring.datasource.druid.min-idle=15\r\nspring.datasource.druid.max-active=30\r\nspring.datasource.druid.remove-abandoned-timeout=180\r\nspring.datasource.druid.max-wait=3000\r\nspring.datasource.druid.time-between-eviction-runs-millis=60000\r\nspring.datasource.druid.min-evictable-idle-time-millis=300000\r\nspring.datasource.druid.max-evictable-idle-time-millis=900000\r\nspring.datasource.druid.test-while-idle=true\r\nspring.datasource.druid.test-on-borrow=false\r\nspring.datasource.druid.test-on-return=false\r\nspring.datasource.druid.validation-query=select 1\r\nspring.datasource.druid.web-stat-filter.enabled=true\r\nspring.datasource.druid.web-stat-filter.url-pattern=/*\r\nspring.datasource.druid.web-stat-filter.exclusions=*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\r\nspring.datasource.druid.web-stat-filter.session-stat-enable=true\r\nspring.datasource.druid.web-stat-filter.session-stat-max-count=1000\r\nspring.datasource.druid.stat-view-servlet.enabled=true\r\nspring.datasource.druid.stat-view-servlet.url-pattern=/druid/*\r\nspring.datasource.druid.stat-view-servlet.reset-enable=false\r\nspring.datasource.druid.stat-view-servlet.allow=127.0.0.1\r\nspring.datasource.druid.stat-view-servlet.deny=\r\nspring.datasource.druid.filter.stat.enabled=true\r\nspring.datasource.druid.filter.stat.log-slow-sql=true\r\nspring.datasource.druid.filter.stat.slow-sql-millis=2000\r\nspring.datasource.druid.filter.stat.merge-sql=true\r\nspring.datasource.druid.filter.wall.config.multi-statement-allow=true\r\nspring.datasource.druid.filter.slf4j.enabled=true\r\nspring.datasource.druid.filter.slf4j.statement-log-error-enabled=true\r\nspring.datasource.druid.filter.slf4j.statement-create-after-log-enabled=false\r\nspring.datasource.druid.filter.slf4j.statement-close-after-log-enabled=false\r\nspring.datasource.druid.filter.slf4j.result-set-open-after-log-enabled=false\r\nspring.datasource.druid.filter.slf4j.result-set-close-after-log-enabled=false\r\nspring.datasource.druid.aop-patterns=cn.odboy.mapper.*,cn.odboy.modules.*.mapper.*\r\nspring.datasource.druid.keep-alive=true\r\nspring.datasource.druid.max-open-prepared-statements=20\r\nspring.datasource.druid.pool-prepared-statements=true\r\nspring.redis.database=${REDIS_DB:2}\r\nspring.redis.host=${REDIS_HOST:kenaito-redis.odboy.local}\r\nspring.redis.port=${REDIS_PORT:16379}\r\nspring.redis.password=${REDIS_PWD:123456}\r\nspring.redis.timeout=5000\r\nspring.redis.lettuce.pool.enabled=true\r\nspring.redis.lettuce.pool.max-active=20\r\nspring.redis.lettuce.pool.min-idle=0\r\nspring.redis.lettuce.pool.max-wait=5000ms\r\nspring.redis.lettuce.pool.max-idle=10\r\nlogin.single-login=false\r\nlogin.user-cache.idle-time=21600\r\nlogin.login-code.enabled=false\r\nlogin.login-code.code-type=arithmetic\r\nlogin.login-code.expiration=2\r\nlogin.login-code.width=111\r\nlogin.login-code.height=36\r\nlogin.login-code.length=2\r\nlogin.login-code.font-name=\r\nlogin.login-code.font-size=25\r\njwt.header=Authorization\r\njwt.token-start-with=Bearer\r\njwt.base64-secret=ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=\r\njwt.token-validity-in-seconds=14400000\r\njwt.online-key=online-token:\r\njwt.code-key=captcha-code:\r\njwt.detect=1800000\r\njwt.renew=3600000\r\nfile.mac.path=~/file/\r\nfile.mac.avatar=~/avatar/\r\nfile.linux.path=/home/kenaito/file/\r\nfile.linux.avatar=/home/kenaito/avatar/\r\nfile.windows.path=C:/kenaito/file/\r\nfile.windows.avatar=C:/kenaito/avatar/\r\nfile.maxSize=100\r\nfile.avatarMaxSize=5\r\napp.private-key=MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEA0vfvyTdGJkdbHkB8mp0f3FE0GYP3AYPaJF7jUd1M0XxFSE2ceK3k2kw20YvQ09NJKk+OMjWQl9WitG9pB6tSCQIDAQABAkA2SimBrWC2/wvauBuYqjCFwLvYiRYqZKThUS3MZlebXJiLB+Ue/gUifAAKIg1avttUZsHBHrop4qfJCwAI0+YRAiEA+W3NK/RaXtnRqmoUUkb59zsZUBLpvZgQPfj1MhyHDz0CIQDYhsAhPJ3mgS64NbUZmGWuuNKp5coY2GIj/zYDMJp6vQIgUueLFXv/eZ1ekgz2Oi67MNCk5jeTF2BurZqNLR3MSmUCIFT3Q6uHMtsB9Eha4u7hS31tj1UWE+D+ADzp59MGnoftAiBeHT7gDMuqeJHPL4b+kC+gzV4FGTfhR9q3tTbklZkD2A==\r\napp.captcha.email.expire-time=30\r\ntask.pool.core-pool-size=10\r\ntask.pool.max-pool-size=30\r\ntask.pool.keep-alive-seconds=60\r\ntask.pool.queue-capacity=50\r\nkenaito.config-center.port=28011\r\nkenaito.config-center.test=xxxxxxxxxxx', 'properties', 1);
INSERT INTO `config_version` VALUES (1, 'server.port=28019\nserver.http2.enabled=true\nserver.undertow.threads.worker=16\nserver.undertow.threads.io=2\nserver.undertow.direct-buffers=true\nserver.undertow.buffer-size=1024\nserver.undertow.accesslog.enabled=false\nserver.compression.enabled=true\nserver.compression.mime-types=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json\nserver.shutdown=graceful\nmybatis-plus.configuration.cache-enabled=false\nmybatis-plus.configuration.local-cache-scope=STATEMENT\nmybatis-plus.configuration.map-underscore-to-camel-case=true\nmybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl\nmybatis-plus.check-config-location=true\nmybatis-plus.mapper-locations=classpath*:/mapper/**/*.xml\nmybatis-plus.global-config.db-config.logic-delete-value=0\nmybatis-plus.global-config.db-config.logic-not-delete-value=1\nmybatis-plus.global-config.db-config.id-type=auto\nmybatis-plus.global-config.db-config.table-underline=true\nmybatis-plus.global-config.banner=true\nspring.freemarker.check-template-location=false\nspring.freemarker.charset=utf-8\nspring.data.redis.repositories.enabled=false\nspring.lifecycle.timeout-per-shutdown-phase=30s\nspring.mail.host=smtp.qq.com\nspring.mail.port=465\nspring.mail.username=tianjun@odboy.cn\nspring.mail.password=xxxxxxxxxxxx\nspring.mail.default-encoding=UTF-8\nspring.mail.properties.mail.smtp.ssl.trust=smtp.qq.com\nspring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory\nspring.mail.properties.mail.smtp.socketFactory.port=465\nspring.mail.properties.mail.smtp.auth=true\nspring.mail.properties.mail.smtp.starttls.enable=true\nspring.mail.properties.mail.smtp.starttls.required=true\nspring.datasource.druid.db-type=com.alibaba.druid.pool.DruidDataSource\nspring.datasource.druid.driverClassName=net.sf.log4jdbc.sql.jdbcapi.DriverSpy\nspring.datasource.druid.url=jdbc:log4jdbc:mysql://${DB_HOST:kenaito-mysql.odboy.local}:${DB_PORT:13306}/${DB_NAME:kenaito_config}?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true\nspring.datasource.druid.username=${DB_USER:root}\nspring.datasource.druid.password=${DB_PWD:root}\nspring.datasource.druid.initial-size=5\nspring.datasource.druid.min-idle=15\nspring.datasource.druid.max-active=30\nspring.datasource.druid.remove-abandoned-timeout=180\nspring.datasource.druid.max-wait=3000\nspring.datasource.druid.time-between-eviction-runs-millis=60000\nspring.datasource.druid.min-evictable-idle-time-millis=300000\nspring.datasource.druid.max-evictable-idle-time-millis=900000\nspring.datasource.druid.test-while-idle=true\nspring.datasource.druid.test-on-borrow=false\nspring.datasource.druid.test-on-return=false\nspring.datasource.druid.validation-query=select 1\nspring.datasource.druid.web-stat-filter.enabled=true\nspring.datasource.druid.web-stat-filter.url-pattern=/*\nspring.datasource.druid.web-stat-filter.exclusions=*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\nspring.datasource.druid.web-stat-filter.session-stat-enable=true\nspring.datasource.druid.web-stat-filter.session-stat-max-count=1000\nspring.datasource.druid.stat-view-servlet.enabled=true\nspring.datasource.druid.stat-view-servlet.url-pattern=/druid/*\nspring.datasource.druid.stat-view-servlet.reset-enable=false\nspring.datasource.druid.stat-view-servlet.allow=127.0.0.1\nspring.datasource.druid.stat-view-servlet.deny=\nspring.datasource.druid.filter.stat.enabled=true\nspring.datasource.druid.filter.stat.log-slow-sql=true\nspring.datasource.druid.filter.stat.slow-sql-millis=2000\nspring.datasource.druid.filter.stat.merge-sql=true\nspring.datasource.druid.filter.wall.config.multi-statement-allow=true\nspring.datasource.druid.filter.slf4j.enabled=true\nspring.datasource.druid.filter.slf4j.statement-log-error-enabled=true\nspring.datasource.druid.filter.slf4j.statement-create-after-log-enabled=false\nspring.datasource.druid.filter.slf4j.statement-close-after-log-enabled=false\nspring.datasource.druid.filter.slf4j.result-set-open-after-log-enabled=false\nspring.datasource.druid.filter.slf4j.result-set-close-after-log-enabled=false\nspring.datasource.druid.aop-patterns=cn.odboy.mapper.*,cn.odboy.modules.*.mapper.*\nspring.datasource.druid.keep-alive=true\nspring.datasource.druid.max-open-prepared-statements=20\nspring.datasource.druid.pool-prepared-statements=true\nspring.redis.database=${REDIS_DB:2}\nspring.redis.host=${REDIS_HOST:kenaito-redis.odboy.local}\nspring.redis.port=${REDIS_PORT:16379}\nspring.redis.password=${REDIS_PWD:123456}\nspring.redis.timeout=5000\nspring.redis.lettuce.pool.enabled=true\nspring.redis.lettuce.pool.max-active=20\nspring.redis.lettuce.pool.min-idle=0\nspring.redis.lettuce.pool.max-wait=5000ms\nspring.redis.lettuce.pool.max-idle=10\nlogin.single-login=false\nlogin.user-cache.idle-time=21600\nlogin.login-code.enabled=false\nlogin.login-code.code-type=arithmetic\nlogin.login-code.expiration=2\nlogin.login-code.width=111\nlogin.login-code.height=36\nlogin.login-code.length=2\nlogin.login-code.font-name=\nlogin.login-code.font-size=25\njwt.header=Authorization\njwt.token-start-with=Bearer\njwt.base64-secret=ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=\njwt.token-validity-in-seconds=14400000\njwt.online-key=online-token:\njwt.code-key=captcha-code:\njwt.detect=1800000\njwt.renew=3600000\nfile.mac.path=~/file/\nfile.mac.avatar=~/avatar/\nfile.linux.path=/home/kenaito/file/\nfile.linux.avatar=/home/kenaito/avatar/\nfile.windows.path=C:/kenaito/file/\nfile.windows.avatar=C:/kenaito/avatar/\nfile.maxSize=100\nfile.avatarMaxSize=5\napp.private-key=MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEA0vfvyTdGJkdbHkB8mp0f3FE0GYP3AYPaJF7jUd1M0XxFSE2ceK3k2kw20YvQ09NJKk+OMjWQl9WitG9pB6tSCQIDAQABAkA2SimBrWC2/wvauBuYqjCFwLvYiRYqZKThUS3MZlebXJiLB+Ue/gUifAAKIg1avttUZsHBHrop4qfJCwAI0+YRAiEA+W3NK/RaXtnRqmoUUkb59zsZUBLpvZgQPfj1MhyHDz0CIQDYhsAhPJ3mgS64NbUZmGWuuNKp5coY2GIj/zYDMJp6vQIgUueLFXv/eZ1ekgz2Oi67MNCk5jeTF2BurZqNLR3MSmUCIFT3Q6uHMtsB9Eha4u7hS31tj1UWE+D+ADzp59MGnoftAiBeHT7gDMuqeJHPL4b+kC+gzV4FGTfhR9q3tTbklZkD2A==\napp.captcha.email.expire-time=30\ntask.pool.core-pool-size=10\ntask.pool.max-pool-size=30\ntask.pool.keep-alive-seconds=60\ntask.pool.queue-capacity=50\nkenaito.config-center.port=28011\nkenaito.config-center.test=xxxxxxxxxxx', 'properties', 2);
INSERT INTO `config_version` VALUES (1, 'server.port=28018\nserver.http2.enabled=true\nserver.undertow.threads.worker=16\nserver.undertow.threads.io=2\nserver.undertow.direct-buffers=true\nserver.undertow.buffer-size=1024\nserver.undertow.accesslog.enabled=false\nserver.compression.enabled=true\nserver.compression.mime-types=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json\nserver.shutdown=graceful\nmybatis-plus.configuration.cache-enabled=false\nmybatis-plus.configuration.local-cache-scope=STATEMENT\nmybatis-plus.configuration.map-underscore-to-camel-case=true\nmybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl\nmybatis-plus.check-config-location=true\nmybatis-plus.mapper-locations=classpath*:/mapper/**/*.xml\nmybatis-plus.global-config.db-config.logic-delete-value=0\nmybatis-plus.global-config.db-config.logic-not-delete-value=1\nmybatis-plus.global-config.db-config.id-type=auto\nmybatis-plus.global-config.db-config.table-underline=true\nmybatis-plus.global-config.banner=true\nspring.freemarker.check-template-location=false\nspring.freemarker.charset=utf-8\nspring.data.redis.repositories.enabled=false\nspring.lifecycle.timeout-per-shutdown-phase=30s\nspring.mail.host=smtp.qq.com\nspring.mail.port=465\nspring.mail.username=tianjun@odboy.cn\nspring.mail.password=xxxxxxxxxxxx\nspring.mail.default-encoding=UTF-8\nspring.mail.properties.mail.smtp.ssl.trust=smtp.qq.com\nspring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory\nspring.mail.properties.mail.smtp.socketFactory.port=465\nspring.mail.properties.mail.smtp.auth=true\nspring.mail.properties.mail.smtp.starttls.enable=true\nspring.mail.properties.mail.smtp.starttls.required=true\nspring.datasource.druid.db-type=com.alibaba.druid.pool.DruidDataSource\nspring.datasource.druid.driverClassName=net.sf.log4jdbc.sql.jdbcapi.DriverSpy\nspring.datasource.druid.url=jdbc:log4jdbc:mysql://${DB_HOST:kenaito-mysql.odboy.local}:${DB_PORT:13306}/${DB_NAME:kenaito_config}?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true\nspring.datasource.druid.username=${DB_USER:root}\nspring.datasource.druid.password=${DB_PWD:root}\nspring.datasource.druid.initial-size=5\nspring.datasource.druid.min-idle=15\nspring.datasource.druid.max-active=30\nspring.datasource.druid.remove-abandoned-timeout=180\nspring.datasource.druid.max-wait=3000\nspring.datasource.druid.time-between-eviction-runs-millis=60000\nspring.datasource.druid.min-evictable-idle-time-millis=300000\nspring.datasource.druid.max-evictable-idle-time-millis=900000\nspring.datasource.druid.test-while-idle=true\nspring.datasource.druid.test-on-borrow=false\nspring.datasource.druid.test-on-return=false\nspring.datasource.druid.validation-query=select 1\nspring.datasource.druid.web-stat-filter.enabled=true\nspring.datasource.druid.web-stat-filter.url-pattern=/*\nspring.datasource.druid.web-stat-filter.exclusions=*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\nspring.datasource.druid.web-stat-filter.session-stat-enable=true\nspring.datasource.druid.web-stat-filter.session-stat-max-count=1000\nspring.datasource.druid.stat-view-servlet.enabled=true\nspring.datasource.druid.stat-view-servlet.url-pattern=/druid/*\nspring.datasource.druid.stat-view-servlet.reset-enable=false\nspring.datasource.druid.stat-view-servlet.allow=127.0.0.1\nspring.datasource.druid.stat-view-servlet.deny=\nspring.datasource.druid.filter.stat.enabled=true\nspring.datasource.druid.filter.stat.log-slow-sql=true\nspring.datasource.druid.filter.stat.slow-sql-millis=2000\nspring.datasource.druid.filter.stat.merge-sql=true\nspring.datasource.druid.filter.wall.config.multi-statement-allow=true\nspring.datasource.druid.filter.slf4j.enabled=true\nspring.datasource.druid.filter.slf4j.statement-log-error-enabled=true\nspring.datasource.druid.filter.slf4j.statement-create-after-log-enabled=false\nspring.datasource.druid.filter.slf4j.statement-close-after-log-enabled=false\nspring.datasource.druid.filter.slf4j.result-set-open-after-log-enabled=false\nspring.datasource.druid.filter.slf4j.result-set-close-after-log-enabled=false\nspring.datasource.druid.aop-patterns=cn.odboy.mapper.*,cn.odboy.modules.*.mapper.*\nspring.datasource.druid.keep-alive=true\nspring.datasource.druid.max-open-prepared-statements=20\nspring.datasource.druid.pool-prepared-statements=true\nspring.redis.database=${REDIS_DB:2}\nspring.redis.host=${REDIS_HOST:kenaito-redis.odboy.local}\nspring.redis.port=${REDIS_PORT:16379}\nspring.redis.password=${REDIS_PWD:123456}\nspring.redis.timeout=5000\nspring.redis.lettuce.pool.enabled=true\nspring.redis.lettuce.pool.max-active=20\nspring.redis.lettuce.pool.min-idle=0\nspring.redis.lettuce.pool.max-wait=5000ms\nspring.redis.lettuce.pool.max-idle=10\nlogin.single-login=false\nlogin.user-cache.idle-time=21600\nlogin.login-code.enabled=false\nlogin.login-code.code-type=arithmetic\nlogin.login-code.expiration=2\nlogin.login-code.width=111\nlogin.login-code.height=36\nlogin.login-code.length=2\nlogin.login-code.font-name=\nlogin.login-code.font-size=25\njwt.header=Authorization\njwt.token-start-with=Bearer\njwt.base64-secret=ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=\njwt.token-validity-in-seconds=14400000\njwt.online-key=online-token:\njwt.code-key=captcha-code:\njwt.detect=1800000\njwt.renew=3600000\nfile.mac.path=~/file/\nfile.mac.avatar=~/avatar/\nfile.linux.path=/home/kenaito/file/\nfile.linux.avatar=/home/kenaito/avatar/\nfile.windows.path=C:/kenaito/file/\nfile.windows.avatar=C:/kenaito/avatar/\nfile.maxSize=100\nfile.avatarMaxSize=5\napp.private-key=MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEA0vfvyTdGJkdbHkB8mp0f3FE0GYP3AYPaJF7jUd1M0XxFSE2ceK3k2kw20YvQ09NJKk+OMjWQl9WitG9pB6tSCQIDAQABAkA2SimBrWC2/wvauBuYqjCFwLvYiRYqZKThUS3MZlebXJiLB+Ue/gUifAAKIg1avttUZsHBHrop4qfJCwAI0+YRAiEA+W3NK/RaXtnRqmoUUkb59zsZUBLpvZgQPfj1MhyHDz0CIQDYhsAhPJ3mgS64NbUZmGWuuNKp5coY2GIj/zYDMJp6vQIgUueLFXv/eZ1ekgz2Oi67MNCk5jeTF2BurZqNLR3MSmUCIFT3Q6uHMtsB9Eha4u7hS31tj1UWE+D+ADzp59MGnoftAiBeHT7gDMuqeJHPL4b+kC+gzV4FGTfhR9q3tTbklZkD2A==\napp.captcha.email.expire-time=30\ntask.pool.core-pool-size=10\ntask.pool.max-pool-size=30\ntask.pool.keep-alive-seconds=60\ntask.pool.queue-capacity=50\nkenaito.config-center.port=28011\nkenaito.config-center.test=xxxxxxxxxxx', 'properties', 3);
INSERT INTO `config_version` VALUES (2, 'server.port=28018\r\nserver.http2.enabled=true\r\nserver.undertow.threads.worker=16\r\nserver.undertow.threads.io=2\r\nserver.undertow.direct-buffers=true\r\nserver.undertow.buffer-size=1024\r\nserver.undertow.accesslog.enabled=false\r\nserver.compression.enabled=true\r\nserver.compression.mime-types=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json\r\nserver.shutdown=graceful\r\nmybatis-plus.configuration.cache-enabled=false\r\nmybatis-plus.configuration.local-cache-scope=STATEMENT\r\nmybatis-plus.configuration.map-underscore-to-camel-case=true\r\nmybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl\r\nmybatis-plus.check-config-location=true\r\nmybatis-plus.mapper-locations=classpath*:/mapper/**/*.xml\r\nmybatis-plus.global-config.db-config.logic-delete-value=0\r\nmybatis-plus.global-config.db-config.logic-not-delete-value=1\r\nmybatis-plus.global-config.db-config.id-type=auto\r\nmybatis-plus.global-config.db-config.table-underline=true\r\nmybatis-plus.global-config.banner=true\r\nspring.freemarker.check-template-location=false\r\nspring.freemarker.charset=utf-8\r\nspring.data.redis.repositories.enabled=false\r\nspring.lifecycle.timeout-per-shutdown-phase=30s\r\nspring.mail.host=smtp.qq.com\r\nspring.mail.port=465\r\nspring.mail.username=tianjun@odboy.cn\r\nspring.mail.password=xxxxxxxxxxxx\r\nspring.mail.default-encoding=UTF-8\r\nspring.mail.properties.mail.smtp.ssl.trust=smtp.qq.com\r\nspring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory\r\nspring.mail.properties.mail.smtp.socketFactory.port=465\r\nspring.mail.properties.mail.smtp.auth=true\r\nspring.mail.properties.mail.smtp.starttls.enable=true\r\nspring.mail.properties.mail.smtp.starttls.required=true\r\nspring.datasource.druid.db-type=com.alibaba.druid.pool.DruidDataSource\r\nspring.datasource.druid.driverClassName=net.sf.log4jdbc.sql.jdbcapi.DriverSpy\r\nspring.datasource.druid.url=jdbc:log4jdbc:mysql://${DB_HOST:kenaito-mysql.odboy.local}:${DB_PORT:13306}/${DB_NAME:kenaito_config}?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true\r\nspring.datasource.druid.username=${DB_USER:root}\r\nspring.datasource.druid.password=${DB_PWD:root}\r\nspring.datasource.druid.initial-size=5\r\nspring.datasource.druid.min-idle=15\r\nspring.datasource.druid.max-active=30\r\nspring.datasource.druid.remove-abandoned-timeout=180\r\nspring.datasource.druid.max-wait=3000\r\nspring.datasource.druid.time-between-eviction-runs-millis=60000\r\nspring.datasource.druid.min-evictable-idle-time-millis=300000\r\nspring.datasource.druid.max-evictable-idle-time-millis=900000\r\nspring.datasource.druid.test-while-idle=true\r\nspring.datasource.druid.test-on-borrow=false\r\nspring.datasource.druid.test-on-return=false\r\nspring.datasource.druid.validation-query=select 1\r\nspring.datasource.druid.web-stat-filter.enabled=true\r\nspring.datasource.druid.web-stat-filter.url-pattern=/*\r\nspring.datasource.druid.web-stat-filter.exclusions=*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\r\nspring.datasource.druid.web-stat-filter.session-stat-enable=true\r\nspring.datasource.druid.web-stat-filter.session-stat-max-count=1000\r\nspring.datasource.druid.stat-view-servlet.enabled=true\r\nspring.datasource.druid.stat-view-servlet.url-pattern=/druid/*\r\nspring.datasource.druid.stat-view-servlet.reset-enable=false\r\nspring.datasource.druid.stat-view-servlet.allow=127.0.0.1\r\nspring.datasource.druid.stat-view-servlet.deny=\r\nspring.datasource.druid.filter.stat.enabled=true\r\nspring.datasource.druid.filter.stat.log-slow-sql=true\r\nspring.datasource.druid.filter.stat.slow-sql-millis=2000\r\nspring.datasource.druid.filter.stat.merge-sql=true\r\nspring.datasource.druid.filter.wall.config.multi-statement-allow=true\r\nspring.datasource.druid.filter.slf4j.enabled=true\r\nspring.datasource.druid.filter.slf4j.statement-log-error-enabled=true\r\nspring.datasource.druid.filter.slf4j.statement-create-after-log-enabled=false\r\nspring.datasource.druid.filter.slf4j.statement-close-after-log-enabled=false\r\nspring.datasource.druid.filter.slf4j.result-set-open-after-log-enabled=false\r\nspring.datasource.druid.filter.slf4j.result-set-close-after-log-enabled=false\r\nspring.datasource.druid.aop-patterns=cn.odboy.mapper.*,cn.odboy.modules.*.mapper.*\r\nspring.datasource.druid.keep-alive=true\r\nspring.datasource.druid.max-open-prepared-statements=20\r\nspring.datasource.druid.pool-prepared-statements=true\r\nspring.redis.database=${REDIS_DB:2}\r\nspring.redis.host=${REDIS_HOST:kenaito-redis.odboy.local}\r\nspring.redis.port=${REDIS_PORT:16379}\r\nspring.redis.password=${REDIS_PWD:123456}\r\nspring.redis.timeout=5000\r\nspring.redis.lettuce.pool.enabled=true\r\nspring.redis.lettuce.pool.max-active=20\r\nspring.redis.lettuce.pool.min-idle=0\r\nspring.redis.lettuce.pool.max-wait=5000ms\r\nspring.redis.lettuce.pool.max-idle=10\r\nlogin.single-login=false\r\nlogin.user-cache.idle-time=21600\r\nlogin.login-code.enabled=false\r\nlogin.login-code.code-type=arithmetic\r\nlogin.login-code.expiration=2\r\nlogin.login-code.width=111\r\nlogin.login-code.height=36\r\nlogin.login-code.length=2\r\nlogin.login-code.font-name=\r\nlogin.login-code.font-size=25\r\njwt.header=Authorization\r\njwt.token-start-with=Bearer\r\njwt.base64-secret=ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=\r\njwt.token-validity-in-seconds=14400000\r\njwt.online-key=online-token:\r\njwt.code-key=captcha-code:\r\njwt.detect=1800000\r\njwt.renew=3600000\r\nfile.mac.path=~/file/\r\nfile.mac.avatar=~/avatar/\r\nfile.linux.path=/home/kenaito/file/\r\nfile.linux.avatar=/home/kenaito/avatar/\r\nfile.windows.path=C:/kenaito/file/\r\nfile.windows.avatar=C:/kenaito/avatar/\r\nfile.maxSize=100\r\nfile.avatarMaxSize=5\r\napp.private-key=MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEA0vfvyTdGJkdbHkB8mp0f3FE0GYP3AYPaJF7jUd1M0XxFSE2ceK3k2kw20YvQ09NJKk+OMjWQl9WitG9pB6tSCQIDAQABAkA2SimBrWC2/wvauBuYqjCFwLvYiRYqZKThUS3MZlebXJiLB+Ue/gUifAAKIg1avttUZsHBHrop4qfJCwAI0+YRAiEA+W3NK/RaXtnRqmoUUkb59zsZUBLpvZgQPfj1MhyHDz0CIQDYhsAhPJ3mgS64NbUZmGWuuNKp5coY2GIj/zYDMJp6vQIgUueLFXv/eZ1ekgz2Oi67MNCk5jeTF2BurZqNLR3MSmUCIFT3Q6uHMtsB9Eha4u7hS31tj1UWE+D+ADzp59MGnoftAiBeHT7gDMuqeJHPL4b+kC+gzV4FGTfhR9q3tTbklZkD2A==\r\napp.captcha.email.expire-time=30\r\ntask.pool.core-pool-size=10\r\ntask.pool.max-pool-size=30\r\ntask.pool.keep-alive-seconds=60\r\ntask.pool.queue-capacity=50\r\nkenaito.config-center.port=28011\r\nserver.port=28018\r\nserver.http2.enabled=true\r\nserver.undertow.threads.worker=16\r\nserver.undertow.threads.io=2\r\nserver.undertow.direct-buffers=true\r\nserver.undertow.buffer-size=1024\r\nserver.undertow.accesslog.enabled=false\r\nserver.compression.enabled=true\r\nserver.compression.mime-types=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json\r\nserver.shutdown=graceful\r\nmybatis-plus.configuration.cache-enabled=false\r\nmybatis-plus.configuration.local-cache-scope=STATEMENT\r\nmybatis-plus.configuration.map-underscore-to-camel-case=true\r\nmybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl\r\nmybatis-plus.check-config-location=true\r\nmybatis-plus.mapper-locations=classpath*:/mapper/**/*.xml\r\nmybatis-plus.global-config.db-config.logic-delete-value=0\r\nmybatis-plus.global-config.db-config.logic-not-delete-value=1\r\nmybatis-plus.global-config.db-config.id-type=auto\r\nmybatis-plus.global-config.db-config.table-underline=true\r\nmybatis-plus.global-config.banner=true\r\nspring.freemarker.check-template-location=false\r\nspring.freemarker.charset=utf-8\r\nspring.data.redis.repositories.enabled=false\r\nspring.lifecycle.timeout-per-shutdown-phase=30s\r\nspring.mail.host=smtp.qq.com\r\nspring.mail.port=465\r\nspring.mail.username=tianjun@odboy.cn\r\nspring.mail.password=xxxxxxxxxxxx\r\nspring.mail.default-encoding=UTF-8\r\nspring.mail.properties.mail.smtp.ssl.trust=smtp.qq.com\r\nspring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory\r\nspring.mail.properties.mail.smtp.socketFactory.port=465\r\nspring.mail.properties.mail.smtp.auth=true\r\nspring.mail.properties.mail.smtp.starttls.enable=true\r\nspring.mail.properties.mail.smtp.starttls.required=true\r\nspring.datasource.druid.db-type=com.alibaba.druid.pool.DruidDataSource\r\nspring.datasource.druid.driverClassName=net.sf.log4jdbc.sql.jdbcapi.DriverSpy\r\nspring.datasource.druid.url=jdbc:log4jdbc:mysql://${DB_HOST:kenaito-mysql.odboy.local}:${DB_PORT:13306}/${DB_NAME:kenaito_config}?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true\r\nspring.datasource.druid.username=${DB_USER:root}\r\nspring.datasource.druid.password=${DB_PWD:root}\r\nspring.datasource.druid.initial-size=5\r\nspring.datasource.druid.min-idle=15\r\nspring.datasource.druid.max-active=30\r\nspring.datasource.druid.remove-abandoned-timeout=180\r\nspring.datasource.druid.max-wait=3000\r\nspring.datasource.druid.time-between-eviction-runs-millis=60000\r\nspring.datasource.druid.min-evictable-idle-time-millis=300000\r\nspring.datasource.druid.max-evictable-idle-time-millis=900000\r\nspring.datasource.druid.test-while-idle=true\r\nspring.datasource.druid.test-on-borrow=false\r\nspring.datasource.druid.test-on-return=false\r\nspring.datasource.druid.validation-query=select 1\r\nspring.datasource.druid.web-stat-filter.enabled=true\r\nspring.datasource.druid.web-stat-filter.url-pattern=/*\r\nspring.datasource.druid.web-stat-filter.exclusions=*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\r\nspring.datasource.druid.web-stat-filter.session-stat-enable=true\r\nspring.datasource.druid.web-stat-filter.session-stat-max-count=1000\r\nspring.datasource.druid.stat-view-servlet.enabled=true\r\nspring.datasource.druid.stat-view-servlet.url-pattern=/druid/*\r\nspring.datasource.druid.stat-view-servlet.reset-enable=false\r\nspring.datasource.druid.stat-view-servlet.allow=127.0.0.1\r\nspring.datasource.druid.stat-view-servlet.deny=\r\nspring.datasource.druid.filter.stat.enabled=true\r\nspring.datasource.druid.filter.stat.log-slow-sql=true\r\nspring.datasource.druid.filter.stat.slow-sql-millis=2000\r\nspring.datasource.druid.filter.stat.merge-sql=true\r\nspring.datasource.druid.filter.wall.config.multi-statement-allow=true\r\nspring.datasource.druid.filter.slf4j.enabled=true\r\nspring.datasource.druid.filter.slf4j.statement-log-error-enabled=true\r\nspring.datasource.druid.filter.slf4j.statement-create-after-log-enabled=false\r\nspring.datasource.druid.filter.slf4j.statement-close-after-log-enabled=false\r\nspring.datasource.druid.filter.slf4j.result-set-open-after-log-enabled=false\r\nspring.datasource.druid.filter.slf4j.result-set-close-after-log-enabled=false\r\nspring.datasource.druid.aop-patterns=cn.odboy.mapper.*,cn.odboy.modules.*.mapper.*\r\nspring.datasource.druid.keep-alive=true\r\nspring.datasource.druid.max-open-prepared-statements=20\r\nspring.datasource.druid.pool-prepared-statements=true\r\nspring.redis.database=${REDIS_DB:2}\r\nspring.redis.host=${REDIS_HOST:kenaito-redis.odboy.local}\r\nspring.redis.port=${REDIS_PORT:16379}\r\nspring.redis.password=${REDIS_PWD:123456}\r\nspring.redis.timeout=5000\r\nspring.redis.lettuce.pool.enabled=true\r\nspring.redis.lettuce.pool.max-active=20\r\nspring.redis.lettuce.pool.min-idle=0\r\nspring.redis.lettuce.pool.max-wait=5000ms\r\nspring.redis.lettuce.pool.max-idle=10\r\nlogin.single-login=false\r\nlogin.user-cache.idle-time=21600\r\nlogin.login-code.enabled=false\r\nlogin.login-code.code-type=arithmetic\r\nlogin.login-code.expiration=2\r\nlogin.login-code.width=111\r\nlogin.login-code.height=36\r\nlogin.login-code.length=2\r\nlogin.login-code.font-name=\r\nlogin.login-code.font-size=25\r\njwt.header=Authorization\r\njwt.token-start-with=Bearer\r\njwt.base64-secret=ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=\r\njwt.token-validity-in-seconds=14400000\r\njwt.online-key=online-token:\r\njwt.code-key=captcha-code:\r\njwt.detect=1800000\r\njwt.renew=3600000\r\nfile.mac.path=~/file/\r\nfile.mac.avatar=~/avatar/\r\nfile.linux.path=/home/kenaito/file/\r\nfile.linux.avatar=/home/kenaito/avatar/\r\nfile.windows.path=C:/kenaito/file/\r\nfile.windows.avatar=C:/kenaito/avatar/\r\nfile.maxSize=100\r\nfile.avatarMaxSize=5\r\napp.private-key=MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEA0vfvyTdGJkdbHkB8mp0f3FE0GYP3AYPaJF7jUd1M0XxFSE2ceK3k2kw20YvQ09NJKk+OMjWQl9WitG9pB6tSCQIDAQABAkA2SimBrWC2/wvauBuYqjCFwLvYiRYqZKThUS3MZlebXJiLB+Ue/gUifAAKIg1avttUZsHBHrop4qfJCwAI0+YRAiEA+W3NK/RaXtnRqmoUUkb59zsZUBLpvZgQPfj1MhyHDz0CIQDYhsAhPJ3mgS64NbUZmGWuuNKp5coY2GIj/zYDMJp6vQIgUueLFXv/eZ1ekgz2Oi67MNCk5jeTF2BurZqNLR3MSmUCIFT3Q6uHMtsB9Eha4u7hS31tj1UWE+D+ADzp59MGnoftAiBeHT7gDMuqeJHPL4b+kC+gzV4FGTfhR9q3tTbklZkD2A==\r\napp.captcha.email.expire-time=30\r\ntask.pool.core-pool-size=10\r\ntask.pool.max-pool-size=30\r\ntask.pool.keep-alive-seconds=60\r\ntask.pool.queue-capacity=50\r\nkenaito.config-center.port=28011\r\n', 'properties', 1);
INSERT INTO `config_version` VALUES (3, 'server.port=28018\r\nserver.http2.enabled=true\r\nserver.undertow.threads.worker=16\r\nserver.undertow.threads.io=2\r\nserver.undertow.direct-buffers=true\r\nserver.undertow.buffer-size=1024\r\nserver.undertow.accesslog.enabled=false\r\nserver.compression.enabled=true\r\nserver.compression.mime-types=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json\r\nserver.shutdown=graceful\r\nmybatis-plus.configuration.cache-enabled=false\r\nmybatis-plus.configuration.local-cache-scope=STATEMENT\r\nmybatis-plus.configuration.map-underscore-to-camel-case=true\r\nmybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl\r\nmybatis-plus.check-config-location=true\r\nmybatis-plus.mapper-locations=classpath*:/mapper/**/*.xml\r\nmybatis-plus.global-config.db-config.logic-delete-value=0\r\nmybatis-plus.global-config.db-config.logic-not-delete-value=1\r\nmybatis-plus.global-config.db-config.id-type=auto\r\nmybatis-plus.global-config.db-config.table-underline=true\r\nmybatis-plus.global-config.banner=true\r\nspring.freemarker.check-template-location=false\r\nspring.freemarker.charset=utf-8\r\nspring.data.redis.repositories.enabled=false\r\nspring.lifecycle.timeout-per-shutdown-phase=30s\r\nspring.mail.host=smtp.qq.com\r\nspring.mail.port=465\r\nspring.mail.username=tianjun@odboy.cn\r\nspring.mail.password=xxxxxxxxxxxx\r\nspring.mail.default-encoding=UTF-8\r\nspring.mail.properties.mail.smtp.ssl.trust=smtp.qq.com\r\nspring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory\r\nspring.mail.properties.mail.smtp.socketFactory.port=465\r\nspring.mail.properties.mail.smtp.auth=true\r\nspring.mail.properties.mail.smtp.starttls.enable=true\r\nspring.mail.properties.mail.smtp.starttls.required=true\r\nspring.datasource.druid.db-type=com.alibaba.druid.pool.DruidDataSource\r\nspring.datasource.druid.driverClassName=net.sf.log4jdbc.sql.jdbcapi.DriverSpy\r\nspring.datasource.druid.url=jdbc:log4jdbc:mysql://${DB_HOST:kenaito-mysql.odboy.local}:${DB_PORT:13306}/${DB_NAME:kenaito_config}?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true\r\nspring.datasource.druid.username=${DB_USER:root}\r\nspring.datasource.druid.password=${DB_PWD:root}\r\nspring.datasource.druid.initial-size=5\r\nspring.datasource.druid.min-idle=15\r\nspring.datasource.druid.max-active=30\r\nspring.datasource.druid.remove-abandoned-timeout=180\r\nspring.datasource.druid.max-wait=3000\r\nspring.datasource.druid.time-between-eviction-runs-millis=60000\r\nspring.datasource.druid.min-evictable-idle-time-millis=300000\r\nspring.datasource.druid.max-evictable-idle-time-millis=900000\r\nspring.datasource.druid.test-while-idle=true\r\nspring.datasource.druid.test-on-borrow=false\r\nspring.datasource.druid.test-on-return=false\r\nspring.datasource.druid.validation-query=select 1\r\nspring.datasource.druid.web-stat-filter.enabled=true\r\nspring.datasource.druid.web-stat-filter.url-pattern=/*\r\nspring.datasource.druid.web-stat-filter.exclusions=*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\r\nspring.datasource.druid.web-stat-filter.session-stat-enable=true\r\nspring.datasource.druid.web-stat-filter.session-stat-max-count=1000\r\nspring.datasource.druid.stat-view-servlet.enabled=true\r\nspring.datasource.druid.stat-view-servlet.url-pattern=/druid/*\r\nspring.datasource.druid.stat-view-servlet.reset-enable=false\r\nspring.datasource.druid.stat-view-servlet.allow=127.0.0.1\r\nspring.datasource.druid.stat-view-servlet.deny=\r\nspring.datasource.druid.filter.stat.enabled=true\r\nspring.datasource.druid.filter.stat.log-slow-sql=true\r\nspring.datasource.druid.filter.stat.slow-sql-millis=2000\r\nspring.datasource.druid.filter.stat.merge-sql=true\r\nspring.datasource.druid.filter.wall.config.multi-statement-allow=true\r\nspring.datasource.druid.filter.slf4j.enabled=true\r\nspring.datasource.druid.filter.slf4j.statement-log-error-enabled=true\r\nspring.datasource.druid.filter.slf4j.statement-create-after-log-enabled=false\r\nspring.datasource.druid.filter.slf4j.statement-close-after-log-enabled=false\r\nspring.datasource.druid.filter.slf4j.result-set-open-after-log-enabled=false\r\nspring.datasource.druid.filter.slf4j.result-set-close-after-log-enabled=false\r\nspring.datasource.druid.aop-patterns=cn.odboy.mapper.*,cn.odboy.modules.*.mapper.*\r\nspring.datasource.druid.keep-alive=true\r\nspring.datasource.druid.max-open-prepared-statements=20\r\nspring.datasource.druid.pool-prepared-statements=true\r\nspring.redis.database=${REDIS_DB:2}\r\nspring.redis.host=${REDIS_HOST:kenaito-redis.odboy.local}\r\nspring.redis.port=${REDIS_PORT:16379}\r\nspring.redis.password=${REDIS_PWD:123456}\r\nspring.redis.timeout=5000\r\nspring.redis.lettuce.pool.enabled=true\r\nspring.redis.lettuce.pool.max-active=20\r\nspring.redis.lettuce.pool.min-idle=0\r\nspring.redis.lettuce.pool.max-wait=5000ms\r\nspring.redis.lettuce.pool.max-idle=10\r\nlogin.single-login=false\r\nlogin.user-cache.idle-time=21600\r\nlogin.login-code.enabled=false\r\nlogin.login-code.code-type=arithmetic\r\nlogin.login-code.expiration=2\r\nlogin.login-code.width=111\r\nlogin.login-code.height=36\r\nlogin.login-code.length=2\r\nlogin.login-code.font-name=\r\nlogin.login-code.font-size=25\r\njwt.header=Authorization\r\njwt.token-start-with=Bearer\r\njwt.base64-secret=ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=\r\njwt.token-validity-in-seconds=14400000\r\njwt.online-key=online-token:\r\njwt.code-key=captcha-code:\r\njwt.detect=1800000\r\njwt.renew=3600000\r\nfile.mac.path=~/file/\r\nfile.mac.avatar=~/avatar/\r\nfile.linux.path=/home/kenaito/file/\r\nfile.linux.avatar=/home/kenaito/avatar/\r\nfile.windows.path=C:/kenaito/file/\r\nfile.windows.avatar=C:/kenaito/avatar/\r\nfile.maxSize=100\r\nfile.avatarMaxSize=5\r\napp.private-key=MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEA0vfvyTdGJkdbHkB8mp0f3FE0GYP3AYPaJF7jUd1M0XxFSE2ceK3k2kw20YvQ09NJKk+OMjWQl9WitG9pB6tSCQIDAQABAkA2SimBrWC2/wvauBuYqjCFwLvYiRYqZKThUS3MZlebXJiLB+Ue/gUifAAKIg1avttUZsHBHrop4qfJCwAI0+YRAiEA+W3NK/RaXtnRqmoUUkb59zsZUBLpvZgQPfj1MhyHDz0CIQDYhsAhPJ3mgS64NbUZmGWuuNKp5coY2GIj/zYDMJp6vQIgUueLFXv/eZ1ekgz2Oi67MNCk5jeTF2BurZqNLR3MSmUCIFT3Q6uHMtsB9Eha4u7hS31tj1UWE+D+ADzp59MGnoftAiBeHT7gDMuqeJHPL4b+kC+gzV4FGTfhR9q3tTbklZkD2A==\r\napp.captcha.email.expire-time=30\r\ntask.pool.core-pool-size=10\r\ntask.pool.max-pool-size=30\r\ntask.pool.keep-alive-seconds=60\r\ntask.pool.queue-capacity=50\r\nkenaito.config-center.port=28011\r\n', 'properties', 1);

-- ----------------------------
-- Table structure for system_dept
-- ----------------------------
DROP TABLE IF EXISTS `system_dept`;
CREATE TABLE `system_dept`  (
  `dept_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint(0) NULL DEFAULT NULL COMMENT '上级部门',
  `sub_count` int(0) NULL DEFAULT 0 COMMENT '子部门数目',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `dept_sort` int(0) NULL DEFAULT 999 COMMENT '排序',
  `enabled` bit(1) NOT NULL COMMENT '状态',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE,
  INDEX `inx_pid`(`pid`) USING BTREE,
  INDEX `inx_enabled`(`enabled`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_dept
-- ----------------------------
INSERT INTO `system_dept` VALUES (2, 7, 1, '研发部', 3, b'1', 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_dept` VALUES (5, 7, 0, '运维部', 4, b'1', 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_dept` VALUES (6, 8, 0, '测试部', 6, b'1', 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_dept` VALUES (7, NULL, 2, '华南分部', 0, b'1', 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_dept` VALUES (8, NULL, 2, '华北分部', 1, b'1', 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_dept` VALUES (15, 8, 0, 'UI部门', 7, b'1', 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_dept` VALUES (17, 2, 0, '研发一组', 999, b'1', 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');

-- ----------------------------
-- Table structure for system_dict
-- ----------------------------
DROP TABLE IF EXISTS `system_dict`;
CREATE TABLE `system_dict`  (
  `dict_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据字典' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_dict
-- ----------------------------
INSERT INTO `system_dict` VALUES (1, 'user_status', '用户状态', 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_dict` VALUES (4, 'dept_status', '部门状态', 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_dict` VALUES (5, 'job_status', '岗位状态', 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_dict` VALUES (8, 'config_center_env', '配置中心环境', 'admin', 'admin', '2024-12-05 18:03:51', '2024-12-05 18:03:51');

-- ----------------------------
-- Table structure for system_dict_detail
-- ----------------------------
DROP TABLE IF EXISTS `system_dict_detail`;
CREATE TABLE `system_dict_detail`  (
  `detail_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dict_id` bigint(0) NULL DEFAULT NULL COMMENT '字典id',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典标签',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典值',
  `dict_sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`detail_id`) USING BTREE,
  INDEX `FK5tpkputc6d9nboxojdbgnpmyb`(`dict_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据字典详情' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_dict_detail
-- ----------------------------
INSERT INTO `system_dict_detail` VALUES (1, 1, '激活', 'true', 1, 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_dict_detail` VALUES (2, 1, '禁用', 'false', 2, 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_dict_detail` VALUES (3, 4, '启用', 'true', 1, 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_dict_detail` VALUES (4, 4, '停用', 'false', 2, 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_dict_detail` VALUES (5, 5, '启用', 'true', 1, 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_dict_detail` VALUES (6, 5, '停用', 'false', 2, 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_dict_detail` VALUES (14, 8, 'DAILY', 'daily', 1, 'admin', 'admin', '2024-12-05 18:04:35', '2024-12-05 18:04:35');
INSERT INTO `system_dict_detail` VALUES (15, 8, 'STAGE', 'stage', 2, 'admin', 'admin', '2024-12-05 18:04:55', '2024-12-05 18:04:55');
INSERT INTO `system_dict_detail` VALUES (16, 8, 'PROD', 'prod', 3, 'admin', 'admin', '2024-12-05 18:05:45', '2024-12-05 18:05:45');

-- ----------------------------
-- Table structure for system_job
-- ----------------------------
DROP TABLE IF EXISTS `system_job`;
CREATE TABLE `system_job`  (
  `job_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
  `enabled` bit(1) NOT NULL COMMENT '岗位状态',
  `job_sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`job_id`) USING BTREE,
  UNIQUE INDEX `uniq_name`(`name`) USING BTREE,
  INDEX `inx_enabled`(`enabled`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_job
-- ----------------------------
INSERT INTO `system_job` VALUES (8, '人事专员', b'1', 3, 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_job` VALUES (10, '产品经理', b'1', 4, 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_job` VALUES (11, '全栈开发', b'1', 2, 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_job` VALUES (12, '软件测试', b'1', 5, 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu`  (
  `menu_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint(0) NULL DEFAULT NULL COMMENT '上级菜单ID',
  `sub_count` int(0) NULL DEFAULT 0 COMMENT '子菜单数目',
  `type` int(0) NULL DEFAULT NULL COMMENT '菜单类型',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单标题',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件名称',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件',
  `menu_sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '链接地址',
  `i_frame` bit(1) NULL DEFAULT NULL COMMENT '是否外链',
  `cache` bit(1) NULL DEFAULT b'0' COMMENT '缓存',
  `hidden` bit(1) NULL DEFAULT b'0' COMMENT '隐藏',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE INDEX `uniq_title`(`title`) USING BTREE,
  UNIQUE INDEX `uniq_name`(`name`) USING BTREE,
  INDEX `inx_pid`(`pid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 139 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_menu
-- ----------------------------
INSERT INTO `system_menu` VALUES (2, 130, 3, 1, '用户管理', 'User', 'system/user/index', 2, 'peoples', 'user', b'0', b'0', b'0', 'user:list', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (3, 130, 3, 1, '角色管理', 'Role', 'system/role/index', 3, 'role', 'role', b'0', b'0', b'0', 'roles:list', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (5, 130, 3, 1, '菜单管理', 'Menu', 'system/menu/index', 5, 'menu', 'menu', b'0', b'0', b'0', 'menu:list', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (6, 131, 3, 0, '系统监控', NULL, NULL, 10, 'monitor', 'monitor', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (9, 6, 0, 1, 'SQL监控', 'Sql', 'monitor/sql/index', 18, 'sqlMonitor', 'druid', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (10, 131, 5, 0, '组件管理', NULL, NULL, 50, 'zujian', 'components', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (11, 10, 0, 1, '图标库', 'Icons', 'components/icons/index', 51, 'icon', 'icon', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (14, 36, 0, 1, '邮件工具', 'Email', 'tools/email/index', 35, 'email', 'email', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (15, 10, 0, 1, '富文本', 'Editor', 'components/Editor', 52, 'fwb', 'tinymce', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (18, 36, 3, 1, '存储管理', 'Storage', 'tools/storage/index', 34, 'qiniu', 'storage', b'0', b'0', b'0', 'storage:list', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (21, 131, 2, 0, '多级菜单', NULL, '', 900, 'menu', 'nested', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (22, 21, 2, 0, '二级菜单1', NULL, '', 999, 'menu', 'menu1', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (23, 21, 0, 1, '二级菜单2', NULL, 'nested/menu2/index', 999, 'menu', 'menu2', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (24, 22, 0, 1, '三级菜单1', 'Test', 'nested/menu1/menu1-1', 999, 'menu', 'menu1-1', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (27, 22, 0, 1, '三级菜单2', NULL, 'nested/menu1/menu1-2', 999, 'menu', 'menu1-2', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (33, 10, 0, 1, 'Markdown', 'Markdown', 'components/MarkDown', 53, 'markdown', 'markdown', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (34, 10, 0, 1, 'Yaml编辑器', 'YamlEdit', 'components/YamlEdit', 54, 'dev', 'yaml', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (35, 130, 3, 1, '部门管理', 'Dept', 'system/dept/index', 6, 'dept', 'dept', b'0', b'0', b'0', 'dept:list', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (36, 131, 2, 0, '系统工具', NULL, '', 30, 'sys-tools', 'sys-tools', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (37, 130, 3, 1, '岗位管理', 'Job', 'system/job/index', 7, 'Steve-Jobs', 'job', b'0', b'0', b'0', 'job:list', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (39, 131, 3, 1, '字典管理', 'Dict', 'system/dict/index', 8, 'dictionary', 'dict', b'0', b'0', b'0', 'dict:list', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (41, 6, 0, 1, '在线用户', 'OnlineUser', 'monitor/online/index', 10, 'Steve-Jobs', 'online', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (44, 2, 0, 2, '用户新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'user:add', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (45, 2, 0, 2, '用户编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'user:edit', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (46, 2, 0, 2, '用户删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'user:del', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (48, 3, 0, 2, '角色创建', NULL, '', 2, '', '', b'0', b'0', b'0', 'roles:add', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (49, 3, 0, 2, '角色修改', NULL, '', 3, '', '', b'0', b'0', b'0', 'roles:edit', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (50, 3, 0, 2, '角色删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'roles:del', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (52, 5, 0, 2, '菜单新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'menu:add', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (53, 5, 0, 2, '菜单编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'menu:edit', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (54, 5, 0, 2, '菜单删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'menu:del', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (56, 35, 0, 2, '部门新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'dept:add', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (57, 35, 0, 2, '部门编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'dept:edit', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (58, 35, 0, 2, '部门删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'dept:del', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (60, 37, 0, 2, '岗位新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'job:add', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (61, 37, 0, 2, '岗位编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'job:edit', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (62, 37, 0, 2, '岗位删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'job:del', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (64, 39, 0, 2, '字典新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'dict:add', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (65, 39, 0, 2, '字典编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'dict:edit', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (66, 39, 0, 2, '字典删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'dict:del', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (77, 18, 0, 2, '上传文件', NULL, '', 2, '', '', b'0', b'0', b'0', 'storage:add', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (78, 18, 0, 2, '文件编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'storage:edit', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (79, 18, 0, 2, '文件删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'storage:del', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (80, 6, 0, 1, '服务监控', 'ServerMonitor', 'monitor/server/index', 14, 'codeConsole', 'server', b'0', b'0', b'0', 'monitor:list', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (83, 10, 0, 1, '图表库', 'Echarts', 'components/Echarts', 50, 'chart', 'echarts', b'0', b'1', b'0', '', 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (118, 10, 0, 1, '拖拽表格', 'DragTableDemo', 'components/DragTableDemo', 999, 'app', 'dragTableDemo', b'0', b'1', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (119, 10, 0, 1, '数字滚动', 'CountToDemo', 'components/CountToDemo', 999, 'app', 'countToDemo', b'0', b'1', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (120, 10, 0, 1, '代码编辑器', 'CodemirrorDemo', 'components/CodemirrorDemo', 999, 'app', 'codemirrorDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (121, 10, 0, 1, '窗口拆分', 'SplitPaneDemo', 'components/SplitPaneDemo', 999, 'app', 'splitPaneDemo', b'0', b'1', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (122, 10, 0, 1, '一键复制', 'ClipboardDemo', 'components/ClipboardDemo', 999, 'app', 'clipboardDemo', b'0', b'1', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (123, 10, 0, 1, 'WebSocket', 'WebSocketDemo', 'components/WebSocketDemo', 999, 'app', 'webSocketDemo', b'0', b'1', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (130, NULL, 5, 0, '用户中心', NULL, NULL, 4, 'peoples', 'sso', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (131, NULL, 5, 0, '系统管理', NULL, NULL, 999, 'system1', 'sysconfig', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-11-15 22:12:18', '2024-11-15 22:12:18');
INSERT INTO `system_menu` VALUES (136, NULL, 0, 1, '配置管理', 'ConfigCenter', 'config-center/index', 999, 'menu', 'config-center', b'0', b'0', b'0', NULL, 'admin', 'admin', '2024-12-05 19:24:14', '2024-12-05 19:24:14');

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role`  (
  `role_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `level` int(0) NULL DEFAULT NULL COMMENT '角色级别',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `data_scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据权限',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `uniq_name`(`name`) USING BTREE,
  INDEX `role_name_index`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_role
-- ----------------------------
INSERT INTO `system_role` VALUES (1, '超级管理员', 1, '-', '全部', 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');
INSERT INTO `system_role` VALUES (2, '普通用户', 2, '-', '本级', 'admin', 'admin', '2024-09-30 18:45:18', '2024-09-30 18:45:18');

-- ----------------------------
-- Table structure for system_roles_depts
-- ----------------------------
DROP TABLE IF EXISTS `system_roles_depts`;
CREATE TABLE `system_roles_depts`  (
  `role_id` bigint(0) NOT NULL,
  `dept_id` bigint(0) NOT NULL,
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE,
  INDEX `FK7qg6itn5ajdoa9h9o78v9ksur`(`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色部门关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_roles_depts
-- ----------------------------

-- ----------------------------
-- Table structure for system_roles_menus
-- ----------------------------
DROP TABLE IF EXISTS `system_roles_menus`;
CREATE TABLE `system_roles_menus`  (
  `menu_id` bigint(0) NOT NULL COMMENT '菜单ID',
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`menu_id`, `role_id`) USING BTREE,
  INDEX `FKcngg2qadojhi3a651a5adkvbq`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_roles_menus
-- ----------------------------
INSERT INTO `system_roles_menus` VALUES (2, 1);
INSERT INTO `system_roles_menus` VALUES (3, 1);
INSERT INTO `system_roles_menus` VALUES (5, 1);
INSERT INTO `system_roles_menus` VALUES (6, 1);
INSERT INTO `system_roles_menus` VALUES (9, 1);
INSERT INTO `system_roles_menus` VALUES (10, 1);
INSERT INTO `system_roles_menus` VALUES (11, 1);
INSERT INTO `system_roles_menus` VALUES (14, 1);
INSERT INTO `system_roles_menus` VALUES (15, 1);
INSERT INTO `system_roles_menus` VALUES (18, 1);
INSERT INTO `system_roles_menus` VALUES (21, 1);
INSERT INTO `system_roles_menus` VALUES (22, 1);
INSERT INTO `system_roles_menus` VALUES (23, 1);
INSERT INTO `system_roles_menus` VALUES (24, 1);
INSERT INTO `system_roles_menus` VALUES (27, 1);
INSERT INTO `system_roles_menus` VALUES (33, 1);
INSERT INTO `system_roles_menus` VALUES (34, 1);
INSERT INTO `system_roles_menus` VALUES (35, 1);
INSERT INTO `system_roles_menus` VALUES (36, 1);
INSERT INTO `system_roles_menus` VALUES (37, 1);
INSERT INTO `system_roles_menus` VALUES (39, 1);
INSERT INTO `system_roles_menus` VALUES (41, 1);
INSERT INTO `system_roles_menus` VALUES (44, 1);
INSERT INTO `system_roles_menus` VALUES (45, 1);
INSERT INTO `system_roles_menus` VALUES (46, 1);
INSERT INTO `system_roles_menus` VALUES (48, 1);
INSERT INTO `system_roles_menus` VALUES (49, 1);
INSERT INTO `system_roles_menus` VALUES (50, 1);
INSERT INTO `system_roles_menus` VALUES (52, 1);
INSERT INTO `system_roles_menus` VALUES (53, 1);
INSERT INTO `system_roles_menus` VALUES (54, 1);
INSERT INTO `system_roles_menus` VALUES (56, 1);
INSERT INTO `system_roles_menus` VALUES (57, 1);
INSERT INTO `system_roles_menus` VALUES (58, 1);
INSERT INTO `system_roles_menus` VALUES (60, 1);
INSERT INTO `system_roles_menus` VALUES (61, 1);
INSERT INTO `system_roles_menus` VALUES (62, 1);
INSERT INTO `system_roles_menus` VALUES (64, 1);
INSERT INTO `system_roles_menus` VALUES (65, 1);
INSERT INTO `system_roles_menus` VALUES (66, 1);
INSERT INTO `system_roles_menus` VALUES (77, 1);
INSERT INTO `system_roles_menus` VALUES (78, 1);
INSERT INTO `system_roles_menus` VALUES (79, 1);
INSERT INTO `system_roles_menus` VALUES (80, 1);
INSERT INTO `system_roles_menus` VALUES (83, 1);
INSERT INTO `system_roles_menus` VALUES (118, 1);
INSERT INTO `system_roles_menus` VALUES (119, 1);
INSERT INTO `system_roles_menus` VALUES (120, 1);
INSERT INTO `system_roles_menus` VALUES (121, 1);
INSERT INTO `system_roles_menus` VALUES (122, 1);
INSERT INTO `system_roles_menus` VALUES (123, 1);
INSERT INTO `system_roles_menus` VALUES (130, 1);
INSERT INTO `system_roles_menus` VALUES (131, 1);
INSERT INTO `system_roles_menus` VALUES (136, 1);
INSERT INTO `system_roles_menus` VALUES (10, 2);
INSERT INTO `system_roles_menus` VALUES (11, 2);
INSERT INTO `system_roles_menus` VALUES (15, 2);
INSERT INTO `system_roles_menus` VALUES (21, 2);
INSERT INTO `system_roles_menus` VALUES (22, 2);
INSERT INTO `system_roles_menus` VALUES (23, 2);
INSERT INTO `system_roles_menus` VALUES (24, 2);
INSERT INTO `system_roles_menus` VALUES (27, 2);
INSERT INTO `system_roles_menus` VALUES (33, 2);
INSERT INTO `system_roles_menus` VALUES (34, 2);
INSERT INTO `system_roles_menus` VALUES (83, 2);
INSERT INTO `system_roles_menus` VALUES (118, 2);
INSERT INTO `system_roles_menus` VALUES (119, 2);
INSERT INTO `system_roles_menus` VALUES (120, 2);
INSERT INTO `system_roles_menus` VALUES (121, 2);
INSERT INTO `system_roles_menus` VALUES (122, 2);
INSERT INTO `system_roles_menus` VALUES (123, 2);

-- ----------------------------
-- Table structure for system_tool_email_log
-- ----------------------------
DROP TABLE IF EXISTS `system_tool_email_log`;
CREATE TABLE `system_tool_email_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `email_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮件类型',
  `from_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '谁发的',
  `to_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发给谁',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `err_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '异常信息',
  `send_status` int(0) NOT NULL COMMENT '发送状态(1已发送2发送成功3发送失败)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'TOOL邮件发送记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_tool_email_log
-- ----------------------------

-- ----------------------------
-- Table structure for system_tool_local_storage
-- ----------------------------
DROP TABLE IF EXISTS `system_tool_local_storage`;
CREATE TABLE `system_tool_local_storage`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件真实的名称',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名',
  `suffix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '后缀',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路径',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
  `size` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '大小',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '本地存储' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of system_tool_local_storage
-- ----------------------------

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user`  (
  `user_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dept_id` bigint(0) NULL DEFAULT NULL COMMENT '部门名称',
  `username` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号码',
  `email` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像地址',
  `avatar_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像真实路径',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `is_admin` bit(1) NULL DEFAULT b'0' COMMENT '是否为admin账号',
  `enabled` bit(1) NULL DEFAULT NULL COMMENT '状态：1启用、0禁用',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `pwd_reset_time` datetime(0) NULL DEFAULT NULL COMMENT '修改密码的时间',
  `dingtalk_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'dingtalk_id',
  `dingtalk_union_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'dingtalk_union_id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uniq_phone`(`phone`) USING BTREE,
  UNIQUE INDEX `uniq_username`(`username`) USING BTREE,
  UNIQUE INDEX `uniq_email`(`email`) USING BTREE,
  INDEX `inx_enabled`(`enabled`) USING BTREE,
  INDEX `inx_deptid`(`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_user
-- ----------------------------
INSERT INTO `system_user` VALUES (1, 2, 'admin', '管理员', '男', '18797874076', '1943815081@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'1', b'1', 'admin', 'anonymous', '2020-05-03 16:38:31', '0707644155678044', 'zf1xhIVabNeJOENYVGl1pwiEiE', '2024-09-30 18:45:18', '2024-10-29 15:56:38');
INSERT INTO `system_user` VALUES (3, 2, 'odboy', 'Odboy', '男', '17353066905', 'tianjun@odboy.cn', NULL, NULL, '$2a$10$4XcyudOYTSz6fue6KFNMHeUQnCX5jbBQypLEnGk1PmekXt5c95JcK', b'0', b'1', 'admin', 'anonymous', NULL, '016339065647678044', 'QRw9oJooPuMnV414UiPWiPnwiEiE', '2024-09-30 18:45:18', '2024-10-29 15:56:37');

-- ----------------------------
-- Table structure for system_users_jobs
-- ----------------------------
DROP TABLE IF EXISTS `system_users_jobs`;
CREATE TABLE `system_users_jobs`  (
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `job_id` bigint(0) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `job_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_users_jobs
-- ----------------------------
INSERT INTO `system_users_jobs` VALUES (1, 11);
INSERT INTO `system_users_jobs` VALUES (3, 11);

-- ----------------------------
-- Table structure for system_users_roles
-- ----------------------------
DROP TABLE IF EXISTS `system_users_roles`;
CREATE TABLE `system_users_roles`  (
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `FKq4eq273l04bpu4efj0jd0jb98`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_users_roles
-- ----------------------------
INSERT INTO `system_users_roles` VALUES (1, 1);
INSERT INTO `system_users_roles` VALUES (3, 2);

-- ----------------------------
-- Table structure for table_logic_demo
-- ----------------------------
DROP TABLE IF EXISTS `table_logic_demo`;
CREATE TABLE `table_logic_demo`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `available` int(0) NOT NULL DEFAULT 1 COMMENT '数据有效性',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of table_logic_demo
-- ----------------------------

-- ----------------------------
-- Table structure for table_normal_demo
-- ----------------------------
DROP TABLE IF EXISTS `table_normal_demo`;
CREATE TABLE `table_normal_demo`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `available` int(0) NOT NULL DEFAULT 1 COMMENT '数据有效性',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of table_normal_demo
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
