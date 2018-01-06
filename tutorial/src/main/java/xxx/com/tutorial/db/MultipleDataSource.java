package xxx.com.tutorial.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by Thinkpad on 2017/3/29.
 */
public class MultipleDataSource extends AbstractRoutingDataSource {

    public static final String DATA_SOURCE_DEFAULT = "ds0"; //database 0
    public static final String DATA_SOURCE_ANALYSIS = "ds1";//database 1

    private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();

    public static void setDataSourceKey(String dataSource) {
        dataSourceKey.set(dataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKey.get();
    }
}