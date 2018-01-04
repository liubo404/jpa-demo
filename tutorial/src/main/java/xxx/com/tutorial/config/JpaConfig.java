package xxx.com.tutorial.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import xxx.com.tutorial.common.BaseRepositoryImpl;

@Configuration
@EnableJpaRepositories(
        basePackages = "xxx.com.tutorial.repository",
        repositoryBaseClass = BaseRepositoryImpl.class
)
@EnableTransactionManagement
public class JpaConfig {
}
