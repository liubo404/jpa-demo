package xxx.com.tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xxx.com.tutorial.config.JpaConfig;


@SpringBootApplication(scanBasePackageClasses = {JpaConfig.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
