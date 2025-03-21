package com.zhitan.common.utils;
import lombok.Data;

import java.util.Date;

/**
 * [
 * {"dataTime":"2025-01-01 00:00:00","value":"value0"}
 * {"dataTime":"2025-02-01 00:00:00","value":"value1"}
 * {"dataTime":"2025-03-01 00:00:00","value":"value2"}
 * {"dataTime":"2025-04-01 00:00:00","value":"value3"}
 * {"dataTime":"2025-05-01 00:00:00","value":"value4"}
 * {"dataTime":"2025-06-01 00:00:00","value":"value5"}
 * {"dataTime":"2025-07-01 00:00:00","value":"value6"}
 * {"dataTime":"2025-08-01 00:00:00","value":"value7"}
 * {"dataTime":"2025-09-01 00:00:00","value":"value8"}
 * {"dataTime":"2025-10-01 00:00:00","value":"value9"}
 * {"dataTime":"2025-11-01 00:00:00","value":"value10"}
 * {"dataTime":"2025-12-01 00:00:00","value":"value11"}
 * }
 */
@Data
public class TypeTime {
   private String dataTime;
   private String value;
   private String timeCode;

   private Date dateTime;
}
