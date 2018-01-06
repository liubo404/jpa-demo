package xxx.com.tutorial.db;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Aspect
@Component
public class DataSourceAspect {

    private final static Logger logger = Logger.getLogger(DataSourceAspect.class);

    /**
     * 拦截目标方法，获取由@DataSource指定的数据源标识，设置到线程存储中以便切换数据源
     *
     * @param point
     * @throws Exception
     */
    public void before(JoinPoint point) throws Exception {
        Class<?> target = point.getTarget().getClass();
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 默认使用目标类型的注解，如果没有则使用其实现接口的注解
        for (Class<?> clazz : target.getInterfaces()) {
            resolveDataSource(clazz, signature.getMethod());
        }
        resolveDataSource(target, signature.getMethod());
    }

    /**
     * 提取目标对象方法注解和类型注解中的数据源标识
     *
     * @param clazz
     * @param method
     */
    private void resolveDataSource(Class<?> clazz, Method method) {
        try {
            Class<?>[] types = method.getParameterTypes();
            // 默认使用类型注解
            if (clazz.isAnnotationPresent(DataSource.class)) {
                DataSource source = clazz.getAnnotation(DataSource.class);
                MultipleDataSource.setDataSourceKey(source.value());
                logger.info("before clazz切换到dataSource:" + source.value());
            }
            // 方法注解可以覆盖类型注解
            Method m = clazz.getMethod(method.getName(), types);
            if (m != null && m.isAnnotationPresent(DataSource.class)) {
                DataSource source = m.getAnnotation(DataSource.class);
                MultipleDataSource.setDataSourceKey(source.value());
                logger.info("before Method切换到dataSource:" + source.value());
            }
        } catch (Exception e) {
            System.out.println(clazz + ":" + e.getMessage());
        }
    }

    /**
     * 后置通知
     *
     * @param point
     */
    public void after(JoinPoint point) {
        Class<?> target = point.getTarget().getClass();
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 默认使用目标类型的注解，如果没有则使用其实现接口的注解
        for (Class<?> clazz : target.getInterfaces()) {
            afterResolveDataSource(clazz, signature.getMethod());
        }
        afterResolveDataSource(target, signature.getMethod());


    }

    /**
     * 处理完后，将数据源设置回默认数据源
     *
     * @param clazz
     * @param method
     */
    private void afterResolveDataSource(Class<?> clazz, Method method) {

        try {
            Class<?>[] types = method.getParameterTypes();
            // 默认使用类型注解
            if (clazz.isAnnotationPresent(DataSource.class)) {
                MultipleDataSource.setDataSourceKey(MultipleDataSource.DATA_SOURCE_DEFAULT);
                logger.info("after clazz切换到dataSource:" + MultipleDataSource.DATA_SOURCE_DEFAULT);
            }
            // 方法注解可以覆盖类型注解
            Method m = clazz.getMethod(method.getName(), types);
            if (m != null && m.isAnnotationPresent(DataSource.class)) {
                MultipleDataSource.setDataSourceKey(MultipleDataSource.DATA_SOURCE_DEFAULT);
                logger.info("after method切换到dataSource:" + MultipleDataSource.DATA_SOURCE_DEFAULT);
            }
        } catch (Exception e) {
            System.out.println(clazz + ":" + e.getMessage());
        }
    }
}