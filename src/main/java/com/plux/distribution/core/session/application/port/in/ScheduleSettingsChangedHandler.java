package com.plux.distribution.core.session.application.port.in;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.session.domain.ScheduleSettings;

public interface ScheduleSettingsChangedHandler {

    void onSettingsChanged(ChatId chatId, ScheduleSettings newSettings);
}
