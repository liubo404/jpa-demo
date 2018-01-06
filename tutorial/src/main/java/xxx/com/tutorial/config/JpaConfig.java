package xxx.com.tutorial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import xxx.com.tutorial.common.BaseRepositoryImpl;
import xxx.com.tutorial.db.MultipleDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "xxx.com.tutorial.repository",
        repositoryBaseClass = BaseRepositoryImpl.class
)
@EnableTransactionManagement
public class JpaConfig {

    @Bean
    DataSource dynamicDatasource() {

        Map<Object, Object> dataSourceMap = new HashMap<>();

        DriverManagerDataSource ds0 = new DriverManagerDataSource();
        ds0.setDriverClassName("com.mysql.jdbc.Driver");
        ds0.setUrl("jdbc:mysql://10.4.89.221:3306/retail_common?useUnicode=true&characterEncoding=utf8");
        ds0.setUsername("fcyun");
        ds0.setPassword("Fcdev123!");


        DriverManagerDataSource ds1 = new DriverManagerDataSource();
        ds1.setDriverClassName("com.mysql.jdbc.Driver");
        ds1.setUrl("jdbc:mysql://10.4.89.221:3306/retail_01?useUnicode=true&characterEncoding=utf8");
        ds1.setUsername("fcyun");
        ds1.setPassword("Fcdev123!");

        DriverManagerDataSource ds2 = new DriverManagerDataSource();
        ds2.setDriverClassName("com.mysql.jdbc.Driver");
        ds2.setUrl("jdbc:mysql://10.4.89.221:3306/retail_02?useUnicode=true&characterEncoding=utf8");
        ds2.setUsername("fcyun");
        ds2.setPassword("Fcdev123!");


        dataSourceMap.put("ds0", ds0);
        dataSourceMap.put("ds1", ds1);
        dataSourceMap.put("ds2", ds2);

        MultipleDataSource ms = new MultipleDataSource();
        ms.setTargetDataSources(dataSourceMap);
        ms.setDefaultTargetDataSource(ds0);
        return ms;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
//        adapter.setDatabase("MYSQL");
        adapter.setShowSql(true);
        adapter.setGenerateDdl(false);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        return adapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dynamicDatasource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean emfb =
                new LocalContainerEntityManagerFactoryBean();
        emfb.setDataSource(dynamicDatasource);
        emfb.setJpaVendorAdapter(jpaVendorAdapter);
        emfb.setPackagesToScan("xxx.com.tutorial.domain");

        return emfb;
    }

}
