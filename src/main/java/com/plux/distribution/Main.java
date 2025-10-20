package com.plux.distribution;

import com.plux.distribution.core.chat.application.service.ChatService;
import com.plux.distribution.core.message.application.service.ExecuteActionService;
import com.plux.distribution.core.session.application.service.RandomSessionCloser;
import com.plux.distribution.core.session.application.service.ScheduleSettingsService;
import com.plux.distribution.core.workflow.application.frame.message.HelpFrame;
import com.plux.distribution.core.workflow.application.frame.registration.ChangeSettingsMessage;
import com.plux.distribution.core.workflow.application.frame.registration.RegistrationSuccessMessage;
import com.plux.distribution.core.workflow.application.frame.registration.hello.HelloFrame;
import com.plux.distribution.core.workflow.application.frame.registration.hello.PostHelloFrame;
import com.plux.distribution.core.workflow.application.frame.registration.pin.CheckPasswordFrame;
import com.plux.distribution.core.workflow.application.frame.registration.pin.CorrectPasswordFrame;
import com.plux.distribution.core.workflow.application.frame.registration.pin.InorrectPasswordFrame;
import com.plux.distribution.core.workflow.application.frame.settings.user.AskAgeFrame;
import com.plux.distribution.core.workflow.application.frame.settings.user.AskCityFrame;
import com.plux.distribution.core.workflow.application.frame.settings.user.AskEmailFrame;
import com.plux.distribution.core.workflow.application.frame.settings.user.AskHobbyFrame;
import com.plux.distribution.core.workflow.application.frame.settings.user.AskNameFrame;
import com.plux.distribution.core.workflow.application.frame.settings.SettingsAppliedMessage;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.AskHoursFrame;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.AskSpdFrame;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.AskTimezoneFrame;
import com.plux.distribution.core.workflow.application.frame.settings.user.FinalizeFrame;
import com.plux.distribution.core.workflow.application.frame.settings.user.StartUserBuildingFrame;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.FinalizeScheduleSettingsFrame;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.ScheduleSettingsBuilder;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.StartScheduleSettingsFrame;
import com.plux.distribution.core.workflow.application.service.FlowFeedbackProcessor;
import com.plux.distribution.core.feedback.application.service.FeedbackResolver;
import com.plux.distribution.core.integration.application.service.IntegrationFeedbackProcessor;
import com.plux.distribution.core.integration.application.service.IntegrationService;
import com.plux.distribution.core.message.application.service.MessageService;
import com.plux.distribution.core.feedback.application.service.RegisterFeedbackService;
import com.plux.distribution.core.integration.application.service.SendServiceMessageService;
import com.plux.distribution.core.session.application.service.RandomSessionInitializer;
import com.plux.distribution.core.session.application.service.SessionFeedbackProcessor;
import com.plux.distribution.core.session.application.service.SessionSchedulerRunner;
import com.plux.distribution.core.session.application.service.SessionService;
import com.plux.distribution.core.user.application.service.UserService;
import com.plux.distribution.core.workflow.application.utils.DefaultContextManager;
import com.plux.distribution.core.workflow.application.port.out.DataRegistry;
import com.plux.distribution.core.workflow.application.port.out.FrameRegistry;
import com.plux.distribution.core.workflow.application.frame.DefaultDataRegistry;
import com.plux.distribution.core.workflow.application.frame.DefaultFrameRegistry;
import com.plux.distribution.core.workflow.application.frame.settings.user.UserBuilder;
import com.plux.distribution.core.workflow.application.frame.utils.LastMessageData;
import com.plux.distribution.core.workflow.application.frame.utils.SequenceFrame;
import com.plux.distribution.core.workflow.application.service.WorkflowService;
import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.infrastructure.BundleTextProvider;
import com.plux.distribution.infrastructure.notifier.WebhookNotifier;
import com.plux.distribution.infrastructure.persistence.DbChatRepository;
import com.plux.distribution.infrastructure.persistence.DbFeedbackRepository;
import com.plux.distribution.infrastructure.persistence.DbFrameRepository;
import com.plux.distribution.infrastructure.persistence.DbIntegrationRepository;
import com.plux.distribution.infrastructure.persistence.DbMessageRepository;
import com.plux.distribution.infrastructure.persistence.DbScheduleSettingsRepository;
import com.plux.distribution.infrastructure.persistence.DbSessionInteractionsRepository;
import com.plux.distribution.infrastructure.persistence.DbSessionRepository;
import com.plux.distribution.infrastructure.persistence.DbTgChatLinker;
import com.plux.distribution.infrastructure.persistence.DbTgMessageLinker;
import com.plux.distribution.infrastructure.persistence.DbUserRepository;
import com.plux.distribution.infrastructure.persistence.config.HibernateConfig;
import com.plux.distribution.infrastructure.telegram.TelegramActionExecutor;
import com.plux.distribution.infrastructure.telegram.TelegramHandler;
import com.plux.distribution.infrastructure.telegram.sender.TelegramMessageSender;
import jakarta.servlet.DispatcherType;
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

        var botToken = System.getenv("TG_TOKEN");
        var scheduleServiceId = Optional.ofNullable(System.getenv("SERVICE_ID"))
                .map(value -> new ServiceId(Long.parseLong(value)))
                .orElse(new ServiceId(1L));

        var hibernateConfig = new HibernateConfig(
                System.getenv("DB_URL"),
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD")
        );

        var messageRepo = new DbMessageRepository(hibernateConfig.getSessionFactory());
        var feedbackRepo = new DbFeedbackRepository(hibernateConfig.getSessionFactory());
        var chatRepo = new DbChatRepository(hibernateConfig.getSessionFactory());
        var frameRepo = new DbFrameRepository(hibernateConfig.getSessionFactory());
        var userRepo = new DbUserRepository(hibernateConfig.getSessionFactory());
        var sessionRepo = new DbSessionRepository(hibernateConfig.getSessionFactory());
        var sessionInteractionsRepo = new DbSessionInteractionsRepository(hibernateConfig.getSessionFactory());
        var integrationRepo = new DbIntegrationRepository(hibernateConfig.getSessionFactory());
        var scheduleSettingsRepo = new DbScheduleSettingsRepository(hibernateConfig.getSessionFactory());

        var tgChatLinker = new DbTgChatLinker(hibernateConfig.getSessionFactory());
        var tgMessageLinker = new DbTgMessageLinker(hibernateConfig.getSessionFactory());
        var tgClient = new OkHttpTelegramClient(botToken);

        var sender = new TelegramMessageSender(tgClient, tgChatLinker, tgMessageLinker, tgMessageLinker);
        var executor = new TelegramActionExecutor(tgClient, tgChatLinker, tgMessageLinker);

        var messageService = new MessageService(sender, messageRepo, messageRepo, messageRepo);
        var executeActionService = new ExecuteActionService(executor);

        var chatService = new ChatService(chatRepo, chatRepo, chatRepo);
        var userService = new UserService(userRepo);

        var integrationService = new IntegrationService(integrationRepo);
        var notifier = new WebhookNotifier(integrationService);
        var sendIntegrationMessageService = new SendServiceMessageService(messageService, integrationRepo);
        var integrationFeedbackProcessor = new IntegrationFeedbackProcessor(notifier);

        var scheduleSettingsService = new ScheduleSettingsService(scheduleSettingsRepo);
        var sessionService = new SessionService(sessionRepo, notifier);

        var sessionCloser = new RandomSessionCloser(sessionInteractionsRepo);

        var sessionFeedbackProcessor = new SessionFeedbackProcessor(sessionService, sessionService, sessionCloser,
                sessionRepo);

        var feedbackResolverProcessor = new FeedbackResolver(List.of(
                sessionFeedbackProcessor,
                integrationFeedbackProcessor
        ), messageService);

        var textProvider = new BundleTextProvider(Locale.of("ru"));
        var frameRegistry = makeFrameRegistry(userService, chatService, scheduleSettingsService);
        var dataRegistry = makeDataRegistry();
        var frameContextManager = new DefaultContextManager(messageService, executeActionService);
        var workflowService = new WorkflowService(frameRegistry, dataRegistry, frameRepo, frameContextManager,
                textProvider);
        var flowFeedbackProcessor = new FlowFeedbackProcessor(
                feedbackResolverProcessor,
                workflowService,
                frameRegistry.get("flow.registration"),
                frameRegistry.get("flow.schedule_settings"),
                frameRegistry.get("flow.update_user_info"),
                frameRegistry.get("flow.help")
        );

        var sessionInitializer = new RandomSessionInitializer(
                sessionService, chatService, flowFeedbackProcessor, scheduleSettingsService,
                scheduleServiceId
        );
        scheduleSettingsService.setHandler(sessionInitializer);

        var registerFeedbackService = new RegisterFeedbackService(messageService, feedbackRepo,
                chatService, flowFeedbackProcessor);

        var schedulerRunner = new SessionSchedulerRunner(sessionInitializer, 60);
        schedulerRunner.start();
        log.info("Session scheduler successfully started");

        var tgHandler = new TelegramHandler(registerFeedbackService, tgMessageLinker,
                tgMessageLinker, tgChatLinker, tgChatLinker);

        AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();

        springContext.register(AppConfig.class, OpenApiConfig.class);
        springContext.addBeanFactoryPostProcessor(beanFactory -> {
            beanFactory.registerSingleton("sendServiceMessageUseCase", sendIntegrationMessageService);
            beanFactory.registerSingleton("createIntegrationUseCase", integrationService);
            beanFactory.registerSingleton("executeActionUseCase", executeActionService);
        });

        UrlHandlerFilter filter = UrlHandlerFilter.trailingSlashHandler("/**").wrapRequest().build();

        FilterHolder fh = new FilterHolder(filter);

        Server jettyServer = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addFilter(fh, "/*", EnumSet.of(DispatcherType.REQUEST));

        context.addServlet(new ServletHolder(new DispatcherServlet(springContext)), "/");

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

            log.info("Bot successfully started");
            Thread.currentThread().join();
        } catch (Exception e) {
            log.error("e: ", e);
        }
    }

    private static FrameRegistry makeFrameRegistry(
            UserService userService, ChatService chatService, ScheduleSettingsService scheduleSettingsService
    ) {
        var pin = System.getenv("BOT_PIN");

        var factory = new DefaultFrameRegistry();

        factory.register("registration.hello_frame", new HelloFrame());
        factory.register("registration.post_hello", new PostHelloFrame());

        factory.register("registration.check_pin", new CheckPasswordFrame(pin));
        factory.register("registration.check_pin.correct", new CorrectPasswordFrame());
        factory.register("registration.check_pin.incorrect", new InorrectPasswordFrame());

        factory.register("registration.user.ask_name", new AskNameFrame());
        factory.register("registration.user.ask_email", new AskEmailFrame());
        factory.register("registration.user.ask_age", new AskAgeFrame());
        factory.register("registration.user.ask_city", new AskCityFrame());
        factory.register("registration.user.ask_hobby", new AskHobbyFrame());
        factory.register("registration.user.finalize", new FinalizeFrame(
                userService, userService, chatService, chatService
        ));
        factory.register("registration.user.start_building", new StartUserBuildingFrame(
                factory.get("registration.user.finalize")
        ));

        factory.register("registration.change_settings", new ChangeSettingsMessage());
        factory.register("registration.finish.success", new RegistrationSuccessMessage());

        factory.register("settings.schedule.ask_timezone", new AskTimezoneFrame());
        factory.register("settings.schedule.ask_hours", new AskHoursFrame());
        factory.register("settings.schedule.ask_spd", new AskSpdFrame());
        factory.register("settings.schedule.finalize", new FinalizeScheduleSettingsFrame(scheduleSettingsService));
        factory.register("settings.schedule.start_building", new StartScheduleSettingsFrame(
                factory.get("settings.schedule.finalize")
        ));

        factory.register("settings.success", new SettingsAppliedMessage());

        factory.register("flow.registration", new SequenceFrame(List.of(
                factory.get("registration.hello_frame"),
                factory.get("registration.user.start_building"),
                factory.get("registration.change_settings"),
                factory.get("settings.schedule.start_building"),
                factory.get("registration.finish.success")
        )));

        factory.register("flow.schedule_settings", new SequenceFrame(List.of(
                factory.get("settings.schedule.start_building"),
                factory.get("settings.success")
        )));

        factory.register("flow.update_user_info", new SequenceFrame(List.of(
                factory.get("registration.user.start_building"),
                factory.get("settings.success")
        )));

        factory.register("flow.help", new HelpFrame());

        return factory;
    }

    private static DataRegistry makeDataRegistry() {
        var registry = new DefaultDataRegistry();

        registry.register("utils.last_message", LastMessageData.class, new LastMessageData.Serializer());
        registry.register("registration.user_builder", UserBuilder.class, new UserBuilder.Serializer());
        registry.register("settings.schedule_settings_builder", ScheduleSettingsBuilder.class,
                new ScheduleSettingsBuilder.Serializer());

        return registry;
    }
}