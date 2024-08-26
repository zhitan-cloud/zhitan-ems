--网关配置信息表
CREATE TABLE "gateway_setting" (
  "id" varchar(36) NOT NULL,
  "gateway_num" varchar(20),
  "plant_area" varchar(50),
  "address" varchar(255),
  "create_by" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6),
  PRIMARY KEY ("id")
)
;

COMMENT ON COLUMN "gateway_setting"."id" IS 'UUID主键';

COMMENT ON COLUMN "gateway_setting"."gateway_num" IS '网关编号';

COMMENT ON COLUMN "gateway_setting"."plant_area" IS '厂区';

COMMENT ON COLUMN "gateway_setting"."address" IS '地址';

COMMENT ON COLUMN "gateway_setting"."create_by" IS '创建者';

COMMENT ON COLUMN "gateway_setting"."create_time" IS '创建时间';

COMMENT ON COLUMN "gateway_setting"."update_by" IS '更新者';

COMMENT ON COLUMN "gateway_setting"."update_time" IS '更新时间';

COMMENT ON TABLE "gateway_setting" IS '网关配置信息表';

ALTER TABLE "gateway_setting"
  ADD COLUMN "hbt_time" date;

COMMENT ON COLUMN "gateway_setting"."hbt_time" IS '心跳时间';

--心跳包日志表 gateway_hbt_log
CREATE TABLE gateway_hbt_log (
  "id" varchar(36) NOT NULL,
  "gateway_no" varchar(20),
  "hbt_time" date,
  "content" varchar(500),
  PRIMARY KEY ("id")
);

COMMENT ON COLUMN gateway_hbt_log."id" IS '主键';

COMMENT ON COLUMN gateway_hbt_log."gateway_no" IS '网关编号';

COMMENT ON COLUMN gateway_hbt_log."hbt_time" IS '心跳时间';

COMMENT ON COLUMN gateway_hbt_log."content" IS '心跳包';

COMMENT ON TABLE gateway_hbt_log IS '网关心跳日志表';


