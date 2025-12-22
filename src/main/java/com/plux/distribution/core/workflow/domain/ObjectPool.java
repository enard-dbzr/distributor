package com.plux.distribution.core.workflow.domain;

import com.plux.distribution.core.workflow.domain.SerializerRegistry.SerializerWithId;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObjectPool {

    private final SerializerRegistry serializerRegistry;

    private final IdentityHashMap<Object, UUID> objectToUuid = new IdentityHashMap<>();

    private final Map<UUID, Object> uuidToObject = new HashMap<>();
    private final Map<UUID, DataSnapshot> data = new HashMap<>();

    public ObjectPool(SerializerRegistry serializerRegistry) {
        this.serializerRegistry = serializerRegistry;
    }

    public Optional<PoolId> getId(Object reference) {
        return Optional.ofNullable(objectToUuid.get(reference)).map(PoolId::new);
    }

    public <T> @Nullable PoolId put(@NotNull FrameContext context, @Nullable T reference) {
        if (reference == null) {
            return null;
        }

        if (objectToUuid.containsKey(reference)) {
            return new PoolId(objectToUuid.get(reference));
        }

        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) reference.getClass();

        SerializerWithId<T> serializerWithId = serializerRegistry.get(type);

        UUID objectUuid;
        do {
            objectUuid = UUID.randomUUID();
        } while (data.containsKey(objectUuid));

        objectToUuid.put(reference, objectUuid);

        data.put(objectUuid, new DataSnapshot(
                serializerWithId.id(),
                serializerWithId.serializer().serialize(context, reference)
        ));

        return new PoolId(objectUuid);
    }

    public <T> T getData(@NotNull FrameContext context, @Nullable PoolId poolId, @NotNull Class<T> type) {
        if (poolId == null) {
            return null;
        }

        if (uuidToObject.containsKey(poolId.uuid())) {
            return type.cast(uuidToObject.get(poolId.uuid()));
        }

        DataSnapshot dataSnapshot = data.get(poolId.uuid());

        DataSerializer<T> serializer = serializerRegistry.findById(dataSnapshot.serializerId(), type);

        T instance = serializer.create(context, dataSnapshot.node());

        uuidToObject.put(poolId.uuid(), instance);

        return instance;
    }

    public Map<UUID, DataSnapshot> dump() {
        Map<UUID, DataSnapshot> dataDump = new HashMap<>(data);

        data.clear();
        objectToUuid.clear();
        uuidToObject.clear();

        return dataDump;
    }

    public void load(Map<UUID, DataSnapshot> dataDump) {
        data.clear();
        objectToUuid.clear();
        uuidToObject.clear();

        data.putAll(dataDump);
    }

}
