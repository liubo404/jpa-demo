package xxx.com.tutorial.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import xxx.com.tutorial.common.BaseRepositoryImpl;
import xxx.com.tutorial.repository.TodoRepository;

@Configuration
@EnableJpaRepositories(
        basePackageClasses = TodoRepository.class,
        repositoryBaseClass = BaseRepositoryImpl.class
)
@EnableTransactionManagement
public class JpaConfig {
}
