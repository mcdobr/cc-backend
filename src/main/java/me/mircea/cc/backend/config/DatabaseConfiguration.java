package me.mircea.cc.backend.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
public class DatabaseConfiguration extends AbstractR2dbcConfiguration {
    @Value("${database.uri}")
    private String databaseUri;

    @Value("${database.username}")
    private String databaseUser;

    @Value("${database.password}")
    private String databasePassword;

    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        // See https://github.com/r2dbc/r2dbc-spi/pull/48
        ConnectionFactoryOptions options = ConnectionFactoryOptions.parse(databaseUri)
                .mutate()
                .option(ConnectionFactoryOptions.USER, databaseUser)
                .option(ConnectionFactoryOptions.PASSWORD, databasePassword)
                .build();

        return ConnectionFactories.get(options);
    }

    // todo: this creates a race condition; doesn't always start (doesn't find a connection)
//    @Bean
//    public ConnectionFactoryInitializer initializer(
//            @Qualifier("connectionFactory") ConnectionFactory connectionFactory
//    ) {
//        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
//        initializer.setConnectionFactory(connectionFactory);
//        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(
//                new ClassPathResource("schema.sql"), new ClassPathResource("data.sql")
//        );
//        initializer.setDatabasePopulator(resourceDatabasePopulator);
//        return initializer;
//    }
}
