package xxx.com.tutorial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import xxx.com.tutorial.repository.TodoRepository;

/**
 * User: liubo
 * Datetime: 2018/1/417:45
 */
public class StartupRunner implements CommandLineRunner {

    @Autowired
    TodoRepository todoRepository;

    @Override
    public void run(String... strings) throws Exception {
        todoRepository.findAll();
    }
}
