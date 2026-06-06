package com.wo.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class SnowflakeId {
    private static final Snowflake snowflake = IdUtil.getSnowflake(1, 1);

    public static long nextId() {
        return snowflake.nextId();
    }
}
