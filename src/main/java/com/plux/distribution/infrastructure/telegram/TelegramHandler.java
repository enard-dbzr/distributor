package com.plux.distribution.infrastructure.telegram;

import com.plux.distribution.application.port.in.MessageDeliveryUseCase;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.attachment.ButtonAttachment;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import com.plux.distribution.domain.message.participant.UserParticipant;
import com.plux.distribution.domain.user.UserId;
import java.util.List;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramHandler implements LongPollingSingleThreadUpdateConsumer {

    private final MessageDeliveryUseCase messageDeliveryUseCase;

    public TelegramHandler(MessageDeliveryUseCase messageDeliveryUseCase) {
        this.messageDeliveryUseCase = messageDeliveryUseCase;
    }


    @Override
    public void consume(Update update) {
        System.out.println(update.toString());

        var msg = new Message(new UserParticipant(new UserId(1)),
                new SimpleMessageContent(
                        "test",
                        List.of(
                                new ButtonAttachment("hello", "hello"),
                                new ButtonAttachment("world", "world")
                        )
                ));

        messageDeliveryUseCase.send(msg);
    }
}
