package xxx.com.tutorial.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import xxx.com.tutorial.config.JpaConfig;
import xxx.com.tutorial.domain.Todo;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@ComponentScan(basePackageClasses = JpaConfig.class)
@RunWith(SpringRunner.class)
@DataJpaTest
public class TodoRepositoryTest {


    Todo todo;

    @Autowired
    TodoRepository todoRepository;


    @Before
    public void setUp() throws Exception {
        todo = new Todo();
        todo.setName("test");
        todo = todoRepository.save(todo);

    }

    @After
    public void tearDown() throws Exception {

        todoRepository.deleteById(todo.getId());
        todo = null;
    }

    @Test
    public void findAll() throws Exception {
        List<Todo> todos = todoRepository.findAll();
        todos.forEach(System.out::println);
    }

    @Test
    public void findBySearchTerm() throws Exception {
    }

    @Test
    public void findOne() throws Exception {

        Optional<Todo> t = todoRepository.findOne(todo.getId());
        t.ifPresent(System.out::println);
    }

    @Test
    public void findByAttributeContainsText() throws Exception {
        List<Todo> students
                = todoRepository.findByAttributeContainsText("name", "test");

        assertEquals("size incorrect", 1, students.size());
    }

    @Test
    public void save() throws Exception {
    }

}