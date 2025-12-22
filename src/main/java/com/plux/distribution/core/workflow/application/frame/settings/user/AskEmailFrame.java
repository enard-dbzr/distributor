package com.plux.distribution.core.workflow.application.frame.settings.user;

import com.plux.distribution.core.interaction.application.dto.action.ClearButtonsAction;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.ButtonAttachment;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.application.frame.settings.user.data.UserBuilder;
import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.application.serializer.PoolAwareSerializer;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot.PoolNodeSnapshotBuilder;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.frame.AbstractFrame;
import com.plux.distribution.core.workflow.domain.frame.FrameFeedback;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AskEmailFrame extends AbstractFrame {

    private final UserBuilder userBuilder;

    private InteractionId lastMessageId = null;

    public AskEmailFrame(UserBuilder userBuilder) {
        this.userBuilder = userBuilder;
    }

    @Override
    public void onEnter(@NotNull FrameContext context) {
        lastMessageId = context.getManager().send(
                context, new SimpleMessageContent(
                        context.getTextProvider().getString("registration.user.email.ask"),
                        List.of(new ButtonAttachment(context.getTextProvider().getString("utils.skip_button"), "skip"))
                )
        );
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        feedback.buttonTag().ifPresent(value -> {
            if (value.equals("skip")) {
                userBuilder.setEmail(null);
                goNext(context);
            }
        });

        feedback.text().ifPresent(text -> {
            try {
                userBuilder.setEmail(text);
                goNext(context);
            } catch (IllegalArgumentException e) {
                new InfoMessageFrame(
                        context.getTextProvider().getString("registration.user.email.wrong_type")
                ).onEnter(context);
            }
        });
    }

    private void goNext(@NotNull FrameContext context) {
        if (lastMessageId != null) {
            context.getManager().dispatch(
                    context,
                    new ClearButtonsAction(
                            context.getChatId(),
                            lastMessageId
                    )
            );
        }

        markFinished();
    }

    public static class AskEmailFrameFactory extends PoolAwareSerializer<AskEmailFrame> {

        @Override
        public PoolNodeSnapshotBuilder buildSnapshot(@NotNull FrameContext context, AskEmailFrame instance,
                PoolNodeSnapshotBuilder builder) {
            return super.buildSnapshot(context, instance, builder)
                    .value("userBuilder", context.getObjectPool().put(context, instance.userBuilder))
                    .value("lastMessageId", context.getObjectPool().put(context, instance.lastMessageId));
        }

        @Override
        public AskEmailFrame create(@NotNull FrameContext context, PoolNodeSnapshot snapshot) {
            var instance = new AskEmailFrame(
                    context.getObjectPool().getData(
                            context,
                            snapshot.values().get("userBuilder"),
                            UserBuilder.class
                    )
            );

            instance.lastMessageId = context.getObjectPool().getData(
                    context,
                    snapshot.values().get("lastMessageId"),
                    InteractionId.class
            );

            return instance;
        }
    }
}
