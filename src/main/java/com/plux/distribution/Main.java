package com.plux.distribution;

import com.plux.distribution.core.chat.application.service.ChatService;
import com.plux.distribution.core.feedback.application.service.FeedbackBotHandler;
import com.plux.distribution.core.feedback.application.service.FeedbackResolver;
import com.plux.distribution.core.integration.application.service.FindInteractionSourceService;
import com.plux.distribution.core.integration.application.service.IntegrationFeedbackProcessor;
import com.plux.distribution.core.integration.application.service.IntegrationService;
import com.plux.distribution.core.integration.application.service.SendServiceInteractionService;
import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.core.interaction.application.service.ExecuteActionService;
import com.plux.distribution.core.interaction.application.service.InteractionDeliveryService;
import com.plux.distribution.core.interaction.application.service.InteractionService;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.mediastorage.application.service.MediaStorageService;
import com.plux.distribution.core.session.application.service.CheckChatIsBusyService;
import com.plux.distribution.core.session.application.service.RandomSessionCloser;
import com.plux.distribution.core.session.application.service.RandomSessionInitializer;
import com.plux.distribution.core.session.application.service.ScheduleSettingsService;
import com.plux.distribution.core.session.application.service.SessionFeedbackProcessor;
import com.plux.distribution.core.session.application.service.SessionSchedulerRunner;
import com.plux.distribution.core.session.application.service.SessionService;
import com.plux.distribution.core.storage.application.service.StorageService;
import com.plux.distribution.core.user.application.service.UserService;
import com.plux.distribution.core.workflow.application.frame.message.HelpFrame;
import com.plux.distribution.core.workflow.application.frame.message.HelpFrame.HelpFrameFactory;
import com.plux.distribution.core.workflow.application.frame.registration.ChangeSettingsMessage;
import com.plux.distribution.core.workflow.application.frame.registration.ChangeSettingsMessage.ChangeSettingsMessageFactory;
import com.plux.distribution.core.workflow.application.frame.registration.CheckPasswordFrame;
import com.plux.distribution.core.workflow.application.frame.registration.CheckPasswordFrame.CheckPasswordFrameFactory;
import com.plux.distribution.core.workflow.application.frame.registration.HelloFrame;
import com.plux.distribution.core.workflow.application.frame.registration.RegistrationSuccessMessage;
import com.plux.distribution.core.workflow.application.frame.registration.RegistrationSuccessMessage.RegistrationSuccessMessageFactory;
import com.plux.distribution.core.workflow.application.frame.settings.SettingsAppliedMessage;
import com.plux.distribution.core.workflow.application.frame.settings.SettingsAppliedMessage.SettingsAppliedMessageFactory;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.AskHoursFrame;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.AskHoursFrame.AskHoursFrameFactory;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.AskSpdFrame;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.AskSpdFrame.AskSpdFrameFactory;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.AskTimezoneFrame;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.AskTimezoneFrame.AskTimezoneFrameFactory;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.ScheduleSettingsWorkflow;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.ScheduleSettingsWorkflow.ScheduleSettingsWorkflowFactory;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.data.ScheduleSettingsBuilder;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.data.ScheduleSettingsBuilder.ScheduleSettingsBuilderSerializer;
import com.plux.distribution.core.workflow.application.frame.settings.user.AskAgeFrame;
import com.plux.distribution.core.workflow.application.frame.settings.user.AskAgeFrame.AskAgeFrameFactory;
import com.plux.distribution.core.workflow.application.frame.settings.user.AskCityFrame;
import com.plux.distribution.core.workflow.application.frame.settings.user.AskCityFrame.AskCityFrameFactory;
import com.plux.distribution.core.workflow.application.frame.settings.user.AskEmailFrame;
import com.plux.distribution.core.workflow.application.frame.settings.user.AskEmailFrame.AskEmailFrameFactory;
import com.plux.distribution.core.workflow.application.frame.settings.user.AskHobbyFrame;
import com.plux.distribution.core.workflow.application.frame.settings.user.AskHobbyFrame.AskHobbyFrameFactory;
import com.plux.distribution.core.workflow.application.frame.settings.user.AskNameFrame;
import com.plux.distribution.core.workflow.application.frame.settings.user.AskNameFrame.AskNameFrameFactory;
import com.plux.distribution.core.workflow.application.frame.settings.user.UpdateUserWorkflow;
import com.plux.distribution.core.workflow.application.frame.settings.user.UpdateUserWorkflow.UpdateUserWorkflowFactory;
import com.plux.distribution.core.workflow.application.frame.settings.user.data.UserBuilder;
import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame.InfoMessageFrameFactory;
import com.plux.distribution.core.workflow.application.frame.utils.RootFrame;
import com.plux.distribution.core.workflow.application.frame.utils.RootFrame.RootFrameFactory;
import com.plux.distribution.core.workflow.application.frame.utils.SequenceFrame;
import com.plux.distribution.core.workflow.application.frame.utils.SequenceFrame.SequenceFrameFactory;
import com.plux.distribution.core.workflow.application.frame.utils.data.InteractionIdDataSerializer;
import com.plux.distribution.core.workflow.application.frame.utils.data.SequenceCreator;
import com.plux.distribution.core.workflow.application.serializer.DefaultSerializerRegistry;
import com.plux.distribution.core.workflow.application.service.FlowFeedbackProcessor;
import com.plux.distribution.core.workflow.application.service.WorkflowService;
import com.plux.distribution.core.workflow.application.utils.DefaultContextManager;
import com.plux.distribution.core.workflow.domain.frame.Frame;
import com.plux.distribution.infrastructure.BundleTextProvider;
import com.plux.distribution.infrastructure.notifier.WebhookNotifier;
import com.plux.distribution.infrastructure.persistence.DbChatRepository;
import com.plux.distribution.infrastructure.persistence.DbFrameRepository;
import com.plux.distribution.infrastructure.persistence.DbIntegrationRepository;
import com.plux.distribution.infrastructure.persistence.DbInteractionRepository;
import com.plux.distribution.infrastructure.persistence.DbMediaRepository;
import com.plux.distribution.infrastructure.persistence.DbScheduleSettingsRepository;
import com.plux.distribution.infrastructure.persistence.DbServiceSendingRepository;
import com.plux.distribution.infrastructure.persistence.DbSessionInteractionsRepository;
import com.plux.distribution.infrastructure.persistence.DbSessionRepository;
import com.plux.distribution.infrastructure.persistence.DbTgChatLinker;
import com.plux.distribution.infrastructure.persistence.DbTgMessageLinker;
import com.plux.distribution.infrastructure.persistence.DbUserRepository;
import com.plux.distribution.infrastructure.persistence.MinioStorage;
import com.plux.distribution.infrastructure.persistence.config.HibernateConfig;
import com.plux.distribution.infrastructure.telegram.TelegramActionExecutor;
import com.plux.distribution.infrastructure.telegram.TelegramHandler;
import com.plux.distribution.infrastructure.telegram.sender.TelegramInteractionSender;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.MultipartConfigElement;
import java.time.Duration;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.UrlHandlerFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        var pin = System.getenv("BOT_PIN");

        var botToken = System.getenv("TG_TOKEN");
        var scheduleServiceId = Optional.ofNullable(System.getenv("SERVICE_ID"))
                .map(value -> new ServiceId(Long.parseLong(value)))
                .orElse(new ServiceId(1L));

        var hibernateConfig = new HibernateConfig(
                System.getenv("DB_URL"),
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD")
        );

        var chatRepo = new DbChatRepository(hibernateConfig.getSessionFactory());
        var userRepo = new DbUserRepository(hibernateConfig.getSessionFactory());
        var frameRepo = new DbFrameRepository(hibernateConfig.getSessionFactory());
        var mediaRepo = new DbMediaRepository(hibernateConfig.getSessionFactory());
        var sessionRepo = new DbSessionRepository(hibernateConfig.getSessionFactory());
        var integrationRepo = new DbIntegrationRepository(hibernateConfig.getSessionFactory());
        var interactionsRepo = new DbInteractionRepository(hibernateConfig.getSessionFactory());
        var serviceSendingRepo = new DbServiceSendingRepository(hibernateConfig.getSessionFactory());
        var scheduleSettingsRepo = new DbScheduleSettingsRepository(hibernateConfig.getSessionFactory());
        var sessionInteractionsRepo = new DbSessionInteractionsRepository(hibernateConfig.getSessionFactory());

        var tgChatLinker = new DbTgChatLinker(hibernateConfig.getSessionFactory());
        var tgMessageLinker = new DbTgMessageLinker(hibernateConfig.getSessionFactory());
        var tgClient = new OkHttpTelegramClient(botToken);

        var sender = new TelegramInteractionSender(tgClient, tgChatLinker, tgMessageLinker);
        var executor = new TelegramActionExecutor(tgClient, tgChatLinker, tgMessageLinker);

        var interactionService = new InteractionService(interactionsRepo);
        var interactionDeliveryService = new InteractionDeliveryService(interactionsRepo, sender);
        var executeActionService = new ExecuteActionService(executor);

        var chatService = new ChatService(chatRepo, chatRepo, chatRepo);
        var userService = new UserService(userRepo);

        var mediaFileStorage = new MinioStorage(
                System.getenv("S3_ENDPOINT"),
                System.getenv("S3_ACCESS_KEY"),
                System.getenv("S3_SECRET_KEY"),
                System.getenv("S3_BUCKET")
        );
        var mediaFileStorageService = new StorageService(mediaFileStorage, Duration.ofMinutes(1));
        var mediaService = new MediaStorageService(mediaFileStorageService, mediaFileStorageService,
                mediaFileStorageService, mediaFileStorageService, mediaRepo);

        var integrationService = new IntegrationService(integrationRepo);
        var findInteractionSourceService = new FindInteractionSourceService(serviceSendingRepo);
        var notifier = new WebhookNotifier(integrationService);
        var sendIntegrationMessageService = new SendServiceInteractionService(interactionDeliveryService,
                integrationRepo, serviceSendingRepo);

        var scheduleSettingsService = new ScheduleSettingsService(scheduleSettingsRepo);
        var sessionService = new SessionService(sessionRepo, notifier);

        var sessionCloser = new RandomSessionCloser(sessionInteractionsRepo);

        var sessionFeedbackProcessor = new SessionFeedbackProcessor(sessionService, sessionService, sessionCloser,
                sessionRepo, findInteractionSourceService);

        var integrationFeedbackProcessor = new IntegrationFeedbackProcessor(notifier, sessionService,
                serviceSendingRepo);

        var feedbackResolverProcessor = new FeedbackResolver(List.of(
                sessionFeedbackProcessor,
                integrationFeedbackProcessor
        ), interactionService);



        var textProvider = new BundleTextProvider(Locale.of("ru"));
        var frameContextManager = new DefaultContextManager(interactionDeliveryService, executeActionService);
        var serializerRegistry = makeSerializerRegistry(pin, userService, chatService, scheduleSettingsService);

        var workflowService = new WorkflowService(frameRepo, frameContextManager,
                textProvider, serializerRegistry);
        var flowFeedbackProcessor = new FlowFeedbackProcessor(
                feedbackResolverProcessor,
                workflowService,
                serializerRegistry.findById("workflow.registration", Frame.class)::create,
                serializerRegistry.findById("workflow.schedule_settings", Frame.class)::create,
                serializerRegistry.findById("workflow.user.update_info", Frame.class)::create,
                serializerRegistry.findById("workflow.help", Frame.class)::create
        );

        var botHandler = new FeedbackBotHandler(List.of(flowFeedbackProcessor));
        interactionDeliveryService.setBotInteractionHandler(botHandler);

        CheckChatIsBusyService checkChatIsBusyService = new CheckChatIsBusyService(chatService, workflowService);
        var sessionInitializer = new RandomSessionInitializer(
                sessionService, chatService, checkChatIsBusyService, scheduleSettingsService,
                scheduleServiceId
        );
        scheduleSettingsService.setHandler(sessionInitializer);

        var schedulerRunner = new SessionSchedulerRunner(sessionInitializer, 60);
        schedulerRunner.start();
        log.info("Session scheduler successfully started");

        var tgHandler = new TelegramHandler(interactionDeliveryService, chatService, tgMessageLinker, tgChatLinker);

        AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();

        springContext.register(AppConfig.class, OpenApiConfig.class);
        springContext.addBeanFactoryPostProcessor(beanFactory -> {
            beanFactory.registerSingleton("sendServiceMessageUseCase", sendIntegrationMessageService);
            beanFactory.registerSingleton("createIntegrationUseCase", integrationService);
            beanFactory.registerSingleton("executeActionUseCase", executeActionService);
            beanFactory.registerSingleton("crudMediaUseCase", mediaService);
        });

        UrlHandlerFilter filter = UrlHandlerFilter.trailingSlashHandler("/**").wrapRequest().build();

        FilterHolder fh = new FilterHolder(filter);

        Server jettyServer = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addFilter(fh, "/*", EnumSet.of(DispatcherType.REQUEST));

        var servletHolder = new ServletHolder(new DispatcherServlet(springContext));

        servletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement(
                System.getProperty("java.io.tmpdir"),
                50 * 1024 * 1024,
                50 * 1024 * 1024,
                2 * 1024
        ));

        context.addServlet(servletHolder, "/");

        jettyServer.setHandler(context);
        springContext.setServletContext(context.getServletContext());

        springContext.refresh();

        RequestMappingHandlerMapping handlerMapping = springContext.getBean(RequestMappingHandlerMapping.class);
        handlerMapping.getHandlerMethods().forEach((key, value) ->
                log.debug("Mapped: {} {}", key, value));

        try {
            jettyServer.start();
            log.info("Spring Web MVC server started on port 8080 with Jetty");
        } catch (Exception e) {
            log.error("Failed to start Jetty: ", e);
        }

        try (var botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, tgHandler);

            log.info("BotParticipant successfully started");
            Thread.currentThread().join();
        } catch (Exception e) {
            log.error("e: ", e);
        }
    }

    private static @NotNull DefaultSerializerRegistry makeSerializerRegistry(
            String pin,
            UserService userService,
            ChatService chatService,
            ScheduleSettingsService scheduleSettingsService
    ) {
        var registry = new DefaultSerializerRegistry();

        registry.register("type.interaction.id", InteractionId.class, new InteractionIdDataSerializer());

        registry.register("frame.utils.info_message", InfoMessageFrame.class, new InfoMessageFrameFactory());
        registry.register("frame.utils.sequence", SequenceFrame.class, new SequenceFrameFactory());
        registry.register("frame.utils.root", RootFrame.class, new RootFrameFactory());

        registry.register("frame.registration.hello_frame", HelloFrame.class, new HelloFrame.HelloFrameFactory());
        registry.register("frame.registration.check_pin", CheckPasswordFrame.class, new CheckPasswordFrameFactory(pin));
        registry.register("frame.registration.change_settings", ChangeSettingsMessage.class,
                new ChangeSettingsMessageFactory());
        registry.register("frame.registration.finish.success", RegistrationSuccessMessage.class,
                new RegistrationSuccessMessageFactory());

        registry.register("frame.user.update_info", UpdateUserWorkflow.class, new UpdateUserWorkflowFactory(
                chatService, userService, userService, chatService
        ));
        registry.register("frame.user.update_info.type.user_builder", UserBuilder.class, new UserBuilder.Serializer());
        registry.register("frame.user.update_info.ask_name", AskNameFrame.class, new AskNameFrameFactory());
        registry.register("frame.user.update_info.ask_email", AskEmailFrame.class, new AskEmailFrameFactory());
        registry.register("frame.user.update_info.ask_age", AskAgeFrame.class, new AskAgeFrameFactory());
        registry.register("frame.user.update_info.ask_city", AskCityFrame.class, new AskCityFrameFactory());
        registry.register("frame.user.update_info.ask_hobby", AskHobbyFrame.class, new AskHobbyFrameFactory());

        registry.register("frame.schedule_settings.type.settings_builder", ScheduleSettingsBuilder.class,
                new ScheduleSettingsBuilderSerializer());
        registry.register("frame.schedule_settings", ScheduleSettingsWorkflow.class,
                new ScheduleSettingsWorkflowFactory(scheduleSettingsService));
        registry.register("frame.schedule_settings.ask_timezone", AskTimezoneFrame.class,
                new AskTimezoneFrameFactory());
        registry.register("frame.schedule_settings.ask_hours", AskHoursFrame.class, new AskHoursFrameFactory());
        registry.register("frame.schedule_settings.ask_spd", AskSpdFrame.class, new AskSpdFrameFactory());

        registry.register("frame.settings.success", SettingsAppliedMessage.class, new SettingsAppliedMessageFactory());

        registry.register("workflow.registration", null, new SequenceCreator(List.of(
                registry.findById("frame.registration.hello_frame", Frame.class),
                registry.findById("frame.registration.check_pin", Frame.class),
                registry.findById("frame.user.update_info", Frame.class),
                registry.findById("frame.registration.change_settings", Frame.class),
                registry.findById("frame.schedule_settings", Frame.class),
                registry.findById("frame.registration.finish.success", Frame.class)
        )));

        registry.register("workflow.user.update_info", null, new SequenceCreator(List.of(
                registry.findById("frame.user.update_info", Frame.class),
                registry.findById("frame.settings.success", Frame.class)
        )));

        registry.register("workflow.schedule_settings", null, new SequenceCreator(List.of(
                registry.findById("frame.schedule_settings", Frame.class),
                registry.findById("frame.settings.success", Frame.class)
        )));

        registry.register("workflow.help", HelpFrame.class, new HelpFrameFactory());

        return registry;
    }

}