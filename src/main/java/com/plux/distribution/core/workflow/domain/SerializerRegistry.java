package com.plux.distribution.core.workflow.domain;

public interface SerializerRegistry {

    <T> DataSerializer<T> get(Class<T> type);

}
