package com.example.facieaiprojecttest.config;

import org.junit.jupiter.api.Test;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.junit.jupiter.api.Assertions.*;

public class AsyncConfigTest {

    @Test
    void testTaskExecutorCreation() {
        AsyncConfig config = new AsyncConfig();
        TaskExecutor taskExecutor = (TaskExecutor) config.taskExecutor();

        assertNotNull(taskExecutor);
    }

    @Test
    void testTaskExecutorProperties() {
        AsyncConfig config = new AsyncConfig();
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) config.taskExecutor();

        assertEquals(5, executor.getCorePoolSize());
        assertEquals(10, executor.getMaxPoolSize());
        assertEquals(50, executor.getQueueCapacity());
        assertTrue(executor.getThreadNamePrefix().startsWith("TradeProcessor-"));
    }
}
