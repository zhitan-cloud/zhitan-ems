--设备管理增加重点能耗设备标识
ALTER TABLE "public"."facility_archives"
  ADD COLUMN "point_flag" varchar(2);

COMMENT ON COLUMN "public"."facility_archives"."point_flag" IS '重点能耗设备标识Y是N否';

--要用系统字典表 pointFlage
