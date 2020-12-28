package com.pps.back.frame.pupansheng.core.plug.filterplug;

import org.apache.ibatis.mapping.BoundSql;

public interface SqlResove {

    String filterSql(Condition condition, BoundSql boundSql, Object parameter);
}
