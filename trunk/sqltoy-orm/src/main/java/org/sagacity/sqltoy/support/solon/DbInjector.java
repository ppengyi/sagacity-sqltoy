package org.sagacity.sqltoy.support.solon;

import org.noear.solon.core.BeanInjector;
import org.noear.solon.core.VarHolder;
import org.sagacity.sqltoy.SqlToyContext;
import org.sagacity.sqltoy.dao.SqlToyLazyDao;
import org.sagacity.sqltoy.service.SqlToyCRUDService;
import org.sagacity.sqltoy.support.solon.annotation.Db;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 夜の孤城
 * @since 1.5
 */
class DbInjector implements BeanInjector<Db> {
    private static final Map<DataSource, SqlToyLazyDao> daoMap = new ConcurrentHashMap<>();
    private static final Map<DataSource, SqlToyCRUDService> serviceMap = new ConcurrentHashMap<>();

    @Override
    public void doInject(VarHolder varH, Db anno) {
        String v = anno.value();
        if (v.equals("")) {
            varH.context().getWrapAsync(DataSource.class, bw -> {
                inject(bw.get(), varH);
            });
        } else {
            varH.context().getWrapAsync(v, bw -> {
                inject(bw.get(), varH);
            });
        }
    }

    private void inject(DataSource dataSource, VarHolder varH) {
        Class type = varH.getType();
        if (type.equals(DataSource.class)) {
            varH.setValue(dataSource);
            return;
        }

        varH.context().getWrapAsync(SqlToyContext.class, bw -> {
            // dao 不在注入 全部融入一个地方
            // if (type.equals(SqlToyLazyDao.class)) {
            //     varH.setValue(DbManager.getDao(dataSource));
            //     return;
            // }
            // 只放扩展的了
            if (type.equals(DbSql.class)) {
                varH.setValue(DbManager.getService(bw.context(), dataSource));
                return;
            }
//            if (type.isInterface()) {
//                varH.setValue(DbManager.getMapper(dataSource, type));
//                return;
//            }
        });
    }
}
