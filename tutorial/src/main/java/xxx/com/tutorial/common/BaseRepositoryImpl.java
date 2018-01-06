package xxx.com.tutorial.common;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * User: liubo
 * Datetime: 2018/1/410:42
 */
public class BaseRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    private final EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public Optional<T> deleteById(ID id) {
        T deleted = entityManager.find(this.getDomainClass(), id);
        Optional<T> returned = Optional.empty();

        if (deleted != null) {
            entityManager.remove(deleted);
            returned = Optional.of(deleted);
        }
        return returned;
    }

    @Transactional
    @Override
    public List<T> findByAttributeContainsText(String attributeName, String text) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cQuery = builder.createQuery(getDomainClass());
        Root<T> root = cQuery.from(getDomainClass());
        cQuery.select(root)
                .where(builder
                        .like(root.<String>get(attributeName), "%" + text + "%"));
        TypedQuery<T> query = entityManager.createQuery(cQuery);
        return query.getResultList();
    }
}
