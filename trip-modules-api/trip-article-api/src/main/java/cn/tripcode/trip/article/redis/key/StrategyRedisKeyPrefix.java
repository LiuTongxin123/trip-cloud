package cn.tripcode.trip.article.redis.key;

import cn.tripcode.trip.redis.key.BaseKeyPrefix;

import java.util.concurrent.TimeUnit;

public class StrategyRedisKeyPrefix extends BaseKeyPrefix {

    public static final StrategyRedisKeyPrefix STRATEGIES_STAT_DATA_MAP =
            new StrategyRedisKeyPrefix("STRATEGIES:STAT:DATA");
    public static final StrategyRedisKeyPrefix STRATEGIES_TOP_MAP =
            new StrategyRedisKeyPrefix("STRATEGIES:TOP");
    public static final StrategyRedisKeyPrefix STRATEGIES_STAT_COUNT_RANK_ZSET =
            new StrategyRedisKeyPrefix("STRATEGIES:STAT:COUNT:RANK");

    public StrategyRedisKeyPrefix(String prefix) {
        super(prefix);
    }

    public StrategyRedisKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
        super(prefix, timeout, unit);
    }
}
