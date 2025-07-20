package ru.t1.daev.bishopstarter.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.t1.daev.bishopstarter.exception.GlobalExceptionHandler;
import ru.t1.daev.bishopstarter.metrics.AndroidMetrics;
import ru.t1.daev.bishopstarter.service.CommandExecutorService;
import ru.t1.daev.bishopstarter.service.CommandQueueService;

@Configuration
@ConditionalOnClass(name = "org.springframework.web.servlet.DispatcherServlet")
public class StarterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AndroidMetrics androidMetrics(MeterRegistry meterRegistry) {
        return new AndroidMetrics(meterRegistry);
    }

    @Bean
    @ConditionalOnMissingBean
    public CommandQueueService commandQueueService(AndroidMetrics androidMetrics) {
        return new CommandQueueService(androidMetrics);
    }

    @Bean
    @ConditionalOnMissingBean
    public CommandExecutorService commandExecutorService(CommandQueueService commandQueueService) {
        return new CommandExecutorService(commandQueueService);
    }
}