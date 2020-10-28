package com.thelocalclass.utm.utility;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Component;

import com.thelocalclass.utm.MonitorInitiator;

@Component
@Configuration
public class UpTimeMonitorScheduler {

    private final Map<Integer, ScheduledFuture<?>> monitors = new ConcurrentHashMap<>();

    public void cancelScheduledPolling(Integer webCheckId) {
        ScheduledFuture<?> scheduledFuture = monitors.remove(webCheckId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    public ScheduledFuture<?> getScheduledMonitor(Integer webCheckId) {
        return monitors.get(webCheckId);
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new MonitorTaskScheduler();
    }

    class MonitorTaskScheduler extends ThreadPoolTaskScheduler {

		private static final long serialVersionUID = 1L;

		@Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
            ScheduledFuture<?> future = super.scheduleAtFixedRate(task, period);

            ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
            monitors.put(((MonitorInitiator) runnable.getTarget()).getWebCheck().getId(), future);

            return future;
        }

        @Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
            ScheduledFuture<?> future = super.scheduleAtFixedRate(task, startTime, period);

            ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
            monitors.put(((MonitorInitiator) runnable.getTarget()).getWebCheck().getId(), future);

            return future;
        }
    }
}
