package com.plux.distribution.core.aggregator.application.port.in;

import com.plux.distribution.core.aggregator.application.dto.Group;
import java.util.List;

public interface FlushReadyGroupsUseCase<T> {

    List<Group<T>> flushReadyGroups();
}
