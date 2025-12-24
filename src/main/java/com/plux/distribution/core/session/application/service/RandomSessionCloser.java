package com.plux.distribution.core.session.application.service;

import com.plux.distribution.core.feedback.application.dto.Feedback;
import com.plux.distribution.core.session.application.dto.SessionDto;
import com.plux.distribution.core.session.application.port.in.CloseSessionStrategy;
import com.plux.distribution.core.session.application.port.out.SessionInteractionsRepositoryPort;
import com.plux.distribution.core.session.domain.SessionState;
import java.util.Optional;
import java.util.Random;
import org.jetbrains.annotations.NotNull;

public class RandomSessionCloser implements CloseSessionStrategy {

    private final static Random RANDOM = new Random();

    private final @NotNull SessionInteractionsRepositoryPort interactionsRepository;

    public RandomSessionCloser(
            @NotNull SessionInteractionsRepositoryPort interactionsRepository) {
        this.interactionsRepository = interactionsRepository;
    }


    @Override
    public boolean isCloseNeeded(@NotNull SessionDto session, @NotNull Feedback feedback) {
        if (session.state().equals(SessionState.CLOSED)) {
            return false;
        }

        var interactionsLeft = Optional.ofNullable(interactionsRepository.getLeftInteractionsCount(session.id()))
                .orElseGet(() -> RANDOM.nextInt(1, 6));

        if (interactionsLeft > 0) {
            interactionsLeft--;
            interactionsRepository.setLeftInteractionsCount(session.id(), interactionsLeft);
            return false;
        }

        return true;
    }
}
