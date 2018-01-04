package xxx.com.tutorial.common;

import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


//@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID> {

    Optional<T> deleteById(ID id);

    List<T> renameAttributeContainsText(String attributeName, String text);
}
