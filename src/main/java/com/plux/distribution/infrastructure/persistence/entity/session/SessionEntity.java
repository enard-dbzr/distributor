package com.plux.distribution.infrastructure.persistence.entity.session;

import com.plux.distribution.application.dto.session.CreateSessionCommand;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.service.ServiceId;
import com.plux.distribution.domain.session.Session;
import com.plux.distribution.domain.session.SessionId;
import com.plux.distribution.domain.session.SessionState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "sessions")
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long chatId;

    @Column(nullable = false)
    private Long serviceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionState state;

    @Column(nullable = false)
    private Date openTime;

    private Date closeTime;

    protected SessionEntity() {

    }

    public SessionEntity(@NotNull CreateSessionCommand command) {
        this.chatId = command.chatId().value();
        this.serviceId = command.serviceId().value();
        this.state = command.state();
        this.openTime = command.openTime();
        this.closeTime = command.closeTime();
    }

    public SessionEntity(@NotNull Session model) {
        this.id = model.getId().value();
        this.chatId = model.getChatId().value();
        this.serviceId = model.getServiceId().value();
        this.state = model.getState();
        this.openTime = model.getOpenTime();
        this.closeTime = model.getCloseTime();
    }

    public Session toModel() {
        return new Session(
                new SessionId(id),
                new ChatId(chatId),
                new ServiceId(serviceId),
                state,
                openTime,
                closeTime
        );
    }
}
