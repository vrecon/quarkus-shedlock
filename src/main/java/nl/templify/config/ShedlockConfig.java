package nl.templify.config;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbc.JdbcLockProvider;

@ApplicationScoped
public class ShedlockConfig {

    @Inject
    AgroalDataSource dataSource;

    @Produces
    @ApplicationScoped
    public LockProvider lockProvider() {
        return new JdbcLockProvider(dataSource);
    }
}
