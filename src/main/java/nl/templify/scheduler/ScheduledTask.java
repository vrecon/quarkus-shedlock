package nl.templify.scheduler;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class ScheduledTask {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ScheduledTask.class);

    @Inject
    LockProvider lockProvider;

    @Scheduled(every="30s")
    void executeTask() {
        log.info("Attempting to execute scheduled task on instance: {}",
                System.getProperty("quarkus.http.port", "8080"));

        LockConfiguration lockConfig = new LockConfiguration(
                Instant.now(),
                "schedulerName",
                Duration.ofMinutes(10),
                Duration.ofMinutes(10)
        );

        Optional<SimpleLock> lock = lockProvider.lock(lockConfig);
        if (lock.isPresent()) {
            try {
                log.info("Lock acquired, executing task...");
                // Je taak hier
            } finally {
                lock.get().unlock();
                log.info("Task completed and lock released");
            }
        } else {
            log.info("Could not acquire lock, task is already running on another instance");
        }
    }

}
