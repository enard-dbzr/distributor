package com.plux.distribution.core.workflow.domain;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class ObjectPool {

    private final SerializerRegistry serializerRegistry;

    private final IdentityHashMap<Object, UUID> objectToUuid = new IdentityHashMap<>();

    private final Map<UUID, Object> uuidToObject = new HashMap<>();
    private final Map<UUID, JsonNode> data = new HashMap<>();

    public ObjectPool(SerializerRegistry serializerRegistry) {
        this.serializerRegistry = serializerRegistry;
    }

    public Optional<PoolId> getId(Object reference) {
        return Optional.ofNullable(objectToUuid.get(reference)).map(PoolId::new);
    }

    public <T> @NotNull PoolId put(@NotNull FrameContext context, T reference) {


        if (objectToUuid.containsKey(reference)) {
            return new PoolId(objectToUuid.get(reference));
        }

        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) reference.getClass();

        DataSerializer<T> serializer = serializerRegistry.get(type);

        UUID objectUuid;
        do {
            objectUuid = UUID.randomUUID();
        } while (data.containsKey(objectUuid));

        objectToUuid.put(reference, objectUuid);

        data.put(objectUuid, serializer.serialize(context, reference));

        return new PoolId(objectUuid);
    }

    public <T> T getData(FrameContext context, PoolId poolId, Class<T> type) {
        if (poolId == null) {
            return null;
        }

        if (uuidToObject.containsKey(poolId.uuid())) {
            return type.cast(uuidToObject.get(poolId.uuid()));
        }

        DataSerializer<T> serializer = serializerRegistry.get(type);
        JsonNode node = data.get(poolId.uuid());

        T instance = serializer.create(context, node);

        uuidToObject.put(poolId.uuid(), instance);

        serializer.restore(context, instance, node);

        return instance;
    }

    public Map<UUID, JsonNode> dump() {
        Map<UUID, JsonNode> dataDump = new HashMap<>(data);

        data.clear();
        objectToUuid.clear();
        uuidToObject.clear();

        return dataDump;
    }

    public void load(Map<UUID, JsonNode> dataDump) {
        data.clear();
        objectToUuid.clear();
        uuidToObject.clear();

        data.putAll(dataDump);
    }

}
