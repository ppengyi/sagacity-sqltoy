package org.sagacity.sqltoy.support.solon;

import org.noear.solon.Solon;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.data.cache.CacheService;
import org.sagacity.sqltoy.SqlToyContext;
import org.sagacity.sqltoy.support.solon.annotation.Db;
import org.sagacity.sqltoy.support.solon.configure.SqlToyContextProperties;
import org.sagacity.sqltoy.support.solon.translate.SolonTranslateCacheManager;

/**
 * 去除spring依赖，适配到Solon的Tran、Aop。TranslateCache默认设置为Solon CacheService
 * 实现Mapper接口功能
 *
 * @author 夜の孤城
 * @since 1.5
 * @since 1.8
 */
public class XPluginImp implements Plugin {

    AppContext context;

    @Override
    public void start(AppContext context) {
        this.context = context;

        // 尝试初始化 rdb
        SqlToyContextProperties properties = context.cfg().getBean("sqltoy", SqlToyContextProperties.class);
        if (properties == null) {
            properties = new SqlToyContextProperties();
        }

        if (Solon.cfg().isDebugMode()) {
            if (properties.getPrintSql()) {
                properties.setDebug(true);
            } else {
                properties.setDebug(false);
            }
        } else {
            properties.setDebug(false);
        }

        try {
            final SqlToyContext sqlToyContext = new SqlToyContextBuilder(properties, context).build();
            if ("solon".equals(properties.getCacheType()) || properties.getCacheType() == null) {
                context.getWrapAsync(CacheService.class, bw -> {
                    sqlToyContext.setTranslateCacheManager(new SolonTranslateCacheManager(bw.get()));
                    try {
                        DbManager.setContext(sqlToyContext);
                        initSqlToy(sqlToyContext);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                DbManager.setContext(sqlToyContext);
                initSqlToy(sqlToyContext);
            }

            context.beanInjectorAdd(Db.class, new DbInjector());
        } catch (Exception e) {
            // e.printStackTrace();
            EventBus.publishTry(e); // 转到事件总线
        }
    }

    private void initSqlToy(SqlToyContext sqlToyContext) throws Exception {
        sqlToyContext.initialize();
        context.wrapAndPut(SqlToyContext.class, sqlToyContext);
    }

    @Override
    public void stop() throws Throwable {
        SqlToyContext sqlToyContext = context.getBean(SqlToyContext.class);
        sqlToyContext.destroy();
    }
}
