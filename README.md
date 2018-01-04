"# jpa-demo" 

r is there a more convenient method to get/check this?

liubo404 @liubo404 14:59

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID> {

    Optional<T> deleteById(ID id);

    public List<T> findByAttributeContainsText(String attributeName, String text);
}


public class BaseRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    private final EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation entityInformation,  EntityManager entityManager) {
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
        cQuery
                .select(root)
                .where(builder
                        .like(root.<String>get(attributeName), "%" + text + "%"));
        TypedQuery<T> query = entityManager.createQuery(cQuery);
        return query.getResultList();
    }
}

public interface TodoRepository  extends BaseRepository<Todo, Integer> {}

java.lang.IllegalStateException: Failed to load ApplicationContext
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'todoRepository': Invocation of init method failed; nested exception is java.lang.IllegalArgumentException: Failed to create query method public abstract java.util.List xxx.com.tutorial.common.BaseRepository.findByAttributeContainsText(java.lang.String,java.lang.String)! No property attributeContainsText found for type Todo!
 Caused by: org.springframework.data.mapping.PropertyReferenceException: No property attributeContainsText found for type Todo!
Caused by: org.springframework.data.mapping.PropertyReferenceException: No property attributeContainsText found for type Todo!
I have created a customized base jpa repository, But I get this error

Andreas Schilling @styx_hcr_twitter 15:35
I remember we had similar issues due to component scanning. As we do no full automatic scanning anyway (just the configuration package, from there stuff is explicitely wired or selectively scanned) we used a repository configuration like this:
 @Configuration
@EnableJpaRepositories( basePackageClasses = MyCustomRepositoryImpl.class,
                        repositoryBaseClass = MyCustomRepositoryImpl.class )
public class MyCustomRepositoryConfiguration {
}
Also, your base repository interface does not necessarily have to inherit from Repository, but I think this is unrelated to your problem
I'm not sure anymore, but I think it's mandatory to bootstrap it with the repositoryBaseClass thingie when customizing all repositories with a custom base.
HTH

liubo404 @liubo404 16:12
Thanks @styx_hcr_twitter , I'm still finding solutions ..

Andreas Schilling @styx_hcr_twitter 16:21
@liubo404 have you tried my suggestion?

liubo404 @liubo404 17:13
@styx_hcr_twitter
```
 @Configuration
@EnableJpaRepositories(
        basePackageClasses = BaseRepositoryImpl.class,
        repositoryBaseClass = BaseRepositoryImpl.class
)
@EnableTransactionManagement
public class JpaConfig {
}

 No property attributeContainsText found for type Todo!
@styx_hcr_twitter I have tried you directions...but still get the same error

Aleš Justin @alesj 17:14
i usually see this when multiple repo modules overlap
so you need to be more specific
which is what @styx_hcr_twitter is suggesting ... so, it's strange it doesn't work
or ... are you sure you have this property on Todo class?

liubo404 @liubo404 17:16
@alesj , @styx_hcr_twitter 's suggestion, in my opinion, I add
```
basePackageClasses = BaseRepositoryImpl.class,

Aleš Justin @alesj 17:17
how does your Todo class look like?

liubo404 @liubo404 17:18
http://www.baeldung.com/spring-data-jpa-method-in-all-repositories
https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-adding-custom-methods-into-all-repositories/
 @Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String title;

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
according to this post http://www.baeldung.com/spring-data-jpa-method-in-all-repositories, I want add a customized method on base repository

Aleš Justin @alesj 17:19
yeah, i see ... hmmm ...
what if you rename it?
so it doesn't start with "find"
(not that this is the right solution, as, imo, your stuff should work, as it's the Impl that impls the method)

liubo404 @liubo404 17:21
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID> {

    Optional<T> deleteById(ID id);

    List<T> renameAttributeContainsText(String attributeName, String text);
}
: No property renameAttributeContainsText found for type Todo!

Aleš Justin @alesj 17:21
uh uh

liubo404 @liubo404 17:23
first, I according to this post https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-adding-custom-methods-into-all-repositories/ , and it works
second, I want add the findByAttributeContainsText method from http://www.baeldung.com/spring-data-jpa-method-in-all-repositories, but failed...

Aleš Justin @alesj 17:23
https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.single-repository-behavior
what if you extract this method to some other interface, which has no @NoRepoBean on it
and then let Impl implment it

liubo404 @liubo404 17:24
In my recent projects, I need add some customized method to all repositories, not just single repository

Aleš Justin @alesj 17:24
and your BaseRepo extend it
or ... just remove @NoRepoBean from your BaseRepo interface?
@liubo404 ^^

liubo404 @liubo404 17:27
let me try it..
 
//@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID> {

    Optional<T> deleteById(ID id);

    List<T> renameAttributeContainsText(String attributeName, String text);
}
the same error No property renameAttributeContainsText found for type Todo!

Aleš Justin @alesj 17:29
are BaseRepo and BaseRepoImpl in the same package?

liubo404 @liubo404 17:30
yes
I post the code to github... wait a minutes

Aleš Justin @alesj 17:31
i'm out of ideas ... so no need :grinning:

liubo404 @liubo404 17:31
ok , thanks very much ~

Aleš Justin @alesj 17:32
np ..
perhaps just try that last one ... where you split the interfaces
MyCustomRepo -- with that findAttrContainsText
and then BaseRepo extends it
my last idea ... :-)

Andreas Schilling @styx_hcr_twitter 17:33
@liubo404 do you use standard component scanning of Spring Boot?

liubo404 @liubo404 17:34
yes

Andreas Schilling @styx_hcr_twitter 17:34
I'd give the following a try:

liubo404 @liubo404 17:34
i don't set any other config
git@github.com:liubo404/jpa-demo.git
I put the code to github

Andreas Schilling @styx_hcr_twitter 17:35
just checked
I don't think you need the factory bean btw
then try:
use `@SpringBootApplication(scanbasePackageClasses={JpaConfig.class})

liubo404 @liubo404 17:36
yes, I delete the factory bean

Andreas Schilling @styx_hcr_twitter 17:38
add basePackageClasses=TodoRepository.class to your @EnabledJpaRepositories annotation

liubo404 @liubo404 17:40
still failure : No property renameAttributeContainsText found for type Todo!
my spring-data-commons is 1.13.9.RELEASE,
spring-data-jpa is 1.11.9.RELEASE

Andreas Schilling @styx_hcr_twitter 17:47
OK, this works for me, at least with H2.
Are you talking about your unittests btw?

liubo404 @liubo404 17:48
yes,

Andreas Schilling @styx_hcr_twitter 17:48
OK
same issue there, the automatic boot component scanning
add the following to your test:
@ComponentScan(basePackageClasses = JpaConfig.class)

liubo404 @liubo404 17:49

@RunWith(SpringRunner.class)
@DataJpaTest
public class TodoRepositoryTest {


    Todo todo;

    @Autowired
    TodoRepository todoRepository;
maybe the @DataJpaTest have problems.

Andreas Schilling @styx_hcr_twitter 17:49
add the annotation I posted above

liubo404 @liubo404 17:49
let me try the command line ..
wow...
it's passed
thanks

Andreas Schilling @styx_hcr_twitter 17:50
DataJpaTest triggers a default full component scan AFAIK
so that's the problem here

liubo404 @liubo404 17:53
thanks very very much @styx_hcr_twitter , This problems stock me for recent two weeks....
I'm happy right now....smiling....

Aleš Justin @alesj 17:54
nice, good to know :wink:

Andreas Schilling @styx_hcr_twitter 17:55
you're welcome
