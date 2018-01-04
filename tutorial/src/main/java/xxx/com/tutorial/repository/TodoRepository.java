package xxx.com.tutorial.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xxx.com.tutorial.common.BaseRepository;
import xxx.com.tutorial.domain.Todo;

import java.util.List;
import java.util.Optional;

/**
 * User: liubo
 * Datetime: 2018/1/410:45
 */
public interface TodoRepository extends BaseRepository<Todo, Integer> {

    List<Todo> findAll();

    Optional<Todo> findOne(Integer id);

    Todo save(Todo persisted);

    @Query("SELECT t FROM Todo t WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%'))   " +
            "ORDER BY t.title ASC")
    List<Todo> findBySearchTerm(@Param("searchTerm") String searchTerm);

}
