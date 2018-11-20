package ru.nord.siwatch.backend.services.networkmonitoring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJndi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import ru.nord.backend.infrastructure.utils.StringUtils;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class DataSourceConfig {

    @Bean(name = "dataSource")
    @Order(1)
    @ConditionalOnJndi
    public DataSource dataSourceFromJndi(@Value("${main.datasource.jndi-name}") String jndiName) {
        if (StringUtils.isNullOrEmpty(jndiName))
            return null;
        else {
            log.debug("Looking up JNDI for DataSource with name '" + jndiName + "'");
            return new JndiDataSourceLookup().getDataSource(jndiName);
        }
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "main.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "dataSource")
    @Order(2)
    @ConditionalOnMissingBean(name = "dataSource")
    @ConfigurationProperties(prefix = "main.datasource")
    public DataSource dataSourceFromEnv() {
        return dataSourceProperties().initializeDataSourceBuilder().build();
    }
}
