package org.sagacity.sqltoy.support.solon;

import org.noear.solon.core.AopContext;
import org.sagacity.sqltoy.SqlToyContext;
import org.sagacity.sqltoy.dao.SqlToyLazyDao;
import org.sagacity.sqltoy.dao.impl.SqlToyLazyDaoImpl;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 夜の孤城
 * @since 1.5
 */
public class DbManager {
    private static final Map<DataSource, SqlToyLazyDao> daoMap = new HashMap<>();
    private static final Map<DataSource, DbSql> serviceMap = new HashMap<>();
    private static SqlToyContext context;

    public static void setContext(SqlToyContext context) {
        DbManager.context = context;
    }

    public static synchronized SqlToyLazyDao getDao(DataSource dataSource) {
        SqlToyLazyDao dao = daoMap.get(dataSource);

        if (dao == null) {
            SqlToyLazyDaoImpl sqlToyLazyDao = new SqlToyLazyDaoImpl();
            sqlToyLazyDao.setDataSource(dataSource);
            sqlToyLazyDao.setSqlToyContext(context);
            daoMap.put(dataSource, sqlToyLazyDao);
            dao = sqlToyLazyDao;
        }

        return dao;
    }

    public static synchronized DbSql getService(AopContext context, DataSource dataSource) {
        DbSql service = serviceMap.get(dataSource);
        if (service == null) {
            DbSql crudService = context.beanMake(DbSql.class).get();
            crudService.setDao(getDao(dataSource));
            serviceMap.put(dataSource, crudService);
            service = crudService;
        }
        return service;
    }

    public static Map<DataSource, DbSql> getServiceMap() {
        return serviceMap;
    }
}
