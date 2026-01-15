package com.plux.distribution.infrastructure.redis.aggegator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plux.distribution.core.aggregator.application.port.out.GroupBuffer;
import com.plux.distribution.core.aggregator.application.dto.Group;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

public class JedisGroupBuffer<T> implements GroupBuffer<T> {

    private static final String INDEX_KEY_TEMPLATE = "%s:index";  // <PREFIX>:index
    private static final String PARTS_KEY_TEMPLATE = "%s:parts:%s";  // <PREFIX>:parts:<GROUP_KEY>
    private static final String LOCK_KEY_TEMPLATE = "%s:lock:%s";  // <PREFIX>:lock:<GROUP_KEY>

    private static final String CREATED_FIELD = "_created";

    private final String prefix;
    private final JedisPool jedisPool;
    private final Class<T> type;

    private final Duration lockTtl;
    private final Duration groupTtl;

    private final ObjectMapper mapper = new ObjectMapper();
    private final String instanceId = UUID.randomUUID().toString();

    public JedisGroupBuffer(String prefix, JedisPool jedisPool, Class<T> type, Duration lockTtl, Duration groupTtl) {
        this.prefix = prefix;
        this.jedisPool = jedisPool;
        this.type = type;
        this.lockTtl = lockTtl;
        this.groupTtl = groupTtl;
    }


    @Override
    public void add(@NotNull String groupKey, @NotNull T part, @NotNull Instant groupTime) {
        String partsKey = PARTS_KEY_TEMPLATE.formatted(prefix, groupKey);

        String partId = UUID.randomUUID().toString();
        String json = serialize(part);

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(partsKey, partId, json);

            jedis.hsetnx(partsKey, CREATED_FIELD, String.valueOf(groupTime.toEpochMilli()));

            jedis.pexpire(partsKey, groupTtl.toMillis());

            long created = Long.parseLong(jedis.hget(partsKey, CREATED_FIELD));
            jedis.zadd(INDEX_KEY_TEMPLATE.formatted(prefix), created, groupKey);
        }
    }

    @Override
    public @NotNull List<Group<T>> popGroupsBefore(@NotNull Instant cutoff) {
        List<Group<T>> result = new ArrayList<>();

        List<String> groupKeys;
        try (Jedis jedis = jedisPool.getResource()) {
            groupKeys = jedis.zrangeByScore(
                    INDEX_KEY_TEMPLATE.formatted(prefix),
                    0,
                    cutoff.toEpochMilli()
            );
        }

        if (groupKeys == null || groupKeys.isEmpty()) {
            return List.of();
        }

        for (String groupKey : groupKeys) {
            if (!tryLock(groupKey)) {
                continue;
            }

            try (Jedis jedis = jedisPool.getResource()) {
                String partsKey = PARTS_KEY_TEMPLATE.formatted(prefix, groupKey);

                Map<String, String> entries = jedis.hgetAll(partsKey);
                if (entries == null || entries.isEmpty()) {
                    jedis.zrem(INDEX_KEY_TEMPLATE.formatted(prefix), groupKey);
                    jedis.del(partsKey);
                    continue;
                }

                Instant ts = Instant.ofEpochMilli(Long.parseLong(entries.remove(CREATED_FIELD)));

                List<T> parts = entries.values().stream()
                        .map(this::deserialize)
                        .toList();

                jedis.zrem(INDEX_KEY_TEMPLATE.formatted(prefix), groupKey);
                jedis.del(partsKey);

                result.add(new Group<>(groupKey, parts, ts));
            } finally {
                unlock(groupKey);
            }
        }

        return result;
    }

    private boolean tryLock(String groupKey) {
        String key = LOCK_KEY_TEMPLATE.formatted(prefix, groupKey);

        try (Jedis jedis = jedisPool.getResource()) {
            SetParams params = SetParams.setParams()
                    .nx()
                    .px(lockTtl.toMillis());
            String res = jedis.set(key, instanceId, params);
            return "OK".equals(res);
        }
    }

    private void unlock(String groupKey) {
        String key = LOCK_KEY_TEMPLATE.formatted(prefix, groupKey);

        try (Jedis jedis = jedisPool.getResource()) {
            String owner = jedis.get(key);
            if (instanceId.equals(owner)) {
                jedis.del(key);
            }
        }
    }

    private String serialize(T obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize group part", e);
        }
    }

    private T deserialize(String json) {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize group part", e);
        }
    }
}
