package org.sagacity.sqltoy.support.solon;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.CheckedUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Singleton;
import org.sagacity.sqltoy.config.annotation.Entity;
import org.sagacity.sqltoy.dao.SqlToyLazyDao;
import org.sagacity.sqltoy.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author jerrt
 * @since 1.10
 */
@Singleton(false) // 因为会被多数据源使用，所以不能是单例
@Component
public class DbSql {
    protected final Logger log = LoggerFactory.getLogger(DbSql.class);
    protected SqlToyLazyDao dao;


    private static final ThreadLocal<ConcurrentHashMap<String,Object>> local = ThreadLocal.withInitial(() -> {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("requestId", IdUtil.fastSimpleUUID());
        return map;
    });

    /**
     * 线程缓存
     * @param key
     * @param handler
     * @param <T>
     * @return
     */
    public static <T> T cache(String key, CheckedUtil.Func0Rt<T> handler) {
        if (StrUtil.isBlank(key)) {
            return handler.call();
        }
        Object obj = local.get().get(key);
        if (obj != null) {
            return (T) obj;
        }
        T apply = handler.call();
        if (apply != null) {
            local.get().put(key, apply);
        }
        return apply;
    }

    public void setDao(SqlToyLazyDao dao) {
        this.dao = dao;
    }

    public <T extends Serializable> T loadById(Class<T> tClass, Long id) {
        if (id == null) return null;
        return this.dao.loadEntity(tClass, EntityQuery.create().where("is_delete = false and id = ?").values(id));
    }

    /**
     * 保证同一个对象在 同一个线程查询的时候 直接使用线程缓存 手动使用
     * 单体服务有用 多服务没啥用
     */
    public <T extends Serializable> T loadByIdTCache(Class<T> tClass, Long id) {
        return cache(buildTCacheKey(tClass, id), () -> loadById(tClass, id));
    }

    public <T extends Serializable> List<T> loadByIds(Class<T> voClass, Long... ids) {
        return this.loadByIds(voClass, CollUtil.newArrayList(ids));
    }

    public <T extends Serializable> List<T> loadByIdsTCache(Class<T> voClass, Long... ids) {
        return cache(buildTCacheKey(voClass, ids), () -> loadByIds(voClass, ids));

    }


    public <T extends Serializable> List<T> loadByIds(Class<T> voClass, Collection<Long> ids) {
        if (ArrayUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        String sql = "select * from " + getEntityTableName(voClass) + " where is_delete = false and id in (:ids)";
        return this.dao.findBySql(sql, MapParam.of("ids", ids), voClass);
    }


    /**
     * 分页查询
     */
    public <T> Page<T> findPageBySql(final int page, final int size, final String sqlOrSqlId, final Map<String, Object> paramsMap, final Class<T> resultType) {
        return this.dao.findPageBySql(new Page<>(size, page), sqlOrSqlId, paramsMap, resultType);
    }

    /**
     * 分页查询
     */
    public <T> Page<T> findPageBySql(final int page, final int size, final String sqlOrSqlId, final Map<String, Object> paramsMap, final Class<T> resultType, Boolean skipQueryCount) {
        return this.dao.findPageBySql(new Page<>(size, page)
                .setSkipQueryCount(skipQueryCount), sqlOrSqlId, paramsMap, resultType);
    }


    /**
     * 分页查询
     */
    public <T> Page<T> findPageBySqlSkipCount(final int page, final int size, final String sqlOrSqlId, final Map<String, Object> paramsMap, final Class<T> resultType) {
        return this.dao.findPageBySql(new Page<>(size, page).setSkipQueryCount(true), sqlOrSqlId, paramsMap, resultType);
    }


    /**
     * 分页查询
     */
    public <T, R> Page<R> findPageBySqlSkipCount(final int page, final int size, final String sqlOrSqlId, final Map<String, Object> paramsMap, final Class<T> resultType, Function<List<T>, List<R>> handler) {
        Page<T> pageBySql = this.dao.findPageBySql(new Page<T>(size, page).setSkipQueryCount(true), sqlOrSqlId, paramsMap, resultType);
        return page2Page(page, size, handler, pageBySql);
    }


    /**
     * 提供基于Map传参的top查询
     */
    public <T> List<T> findTopBySql(final String sqlOrSqlId, final Map<String, Object> paramsMap, final Class<T> resultType, final double topSize) {
        return this.dao.findTopBySql(sqlOrSqlId, paramsMap, resultType, topSize);
    }


    /**
     * 搜索查询
     * sql 搜索语句中含有 @include(:_s)
     *
     * @param sql          sql
     * @param ids          已筛选Id 自动注入为_ids 参数
     * @param searchParams 搜索参数
     * @param resultType   烦你类型
     * @param <T>          List<resultType>
     * @return List<resultType>
     */
    public <T> List<T> search(final String sql,
                              final Long[] ids,
                              final String keyFiled,
                              final MapParam searchParams,
                              final Class<T> resultType) {
        if (StrUtil.isEmpty(sql)) {
            return Collections.emptyList();
        }

        String sqlFragment = StrUtil.format("""
                #[ and {keyFiled} in (:y_ids) ]
                #[ and not {keyFiled} in (:y_notIds) ]
                """, MapParam.of("keyFiled",keyFiled));

        ArrayList<T> ts = new ArrayList<>();
        if (ArrayUtil.isNotEmpty(ids)) {
            ts.addAll(this.dao.findTopBySql(sql,
                    MapParam.of("y_ids", ids)
                            .include("_s", sqlFragment)
                    , resultType, ids.length));
            searchParams._num("y_notIds", ids);
        }
        ts.addAll(this.dao.findTopBySql(sql,
                searchParams.include("_s", sqlFragment), resultType, 5));
        return ts;
    }

    /**
     * 提供基于Map传参的top查询
     */
    public <T> List<T> findBySql(final String sqlOrSqlId, final Map<String, Object> paramsMap, final Class<T> resultType) {
        return this.dao.findBySql(sqlOrSqlId, paramsMap, resultType);
    }

    /**
     * 提供基于Map传参的top查询
     */
    public <T> List<T> findBySqlNext(final String sqlOrSqlId, Map<String, Object> paramsMap, final Class<T> resultType, Long nextId, Long size) {
        if (paramsMap == null) {
            paramsMap = new HashMap<>();
        }
        paramsMap.put("nextId", Optional.ofNullable(nextId).orElse(0L));
        return findTopBySql(sqlOrSqlId, paramsMap, resultType, size);
    }


    /**
     * 获取一个实体 默认 查询1个
     */
    public <T extends Serializable> T loadEntity(Class<T> entityClass, EntityQuery entityQuery) {
        return this.dao.loadEntity(entityClass, entityQuery.top(1));
    }

    public <T> List<T> findEntity(Class<T> entityClass, EntityQuery entityQuery) {
        return this.dao.findEntity(entityClass, entityQuery);
    }

    public <T> Long getCount(Class<T> entityClass, EntityQuery entityQuery) {
        return this.dao.getCount(entityClass, entityQuery);
    }

    public <T extends Serializable> Long deleteById(Class<T> entityClass, Long id) {
        if (id == null) {
            return 0L;
        }
        return this.dao.updateByQuery(entityClass, EntityUpdate.create().where("id = ?").values(id).set("is_delete", true));
    }

    public <T extends Serializable> Long deleteByQuery(Class<T> entityClass, String where, final Map<String, Object> paramsMap) {
        return this.dao.updateByQuery(entityClass, EntityUpdate.create().where(where).values(paramsMap).set("is_delete", true));
    }

    /**
     * 简单修改实体
     */
    public <T extends Serializable> Long updateByQuery(Class<T> entityClass, EntityUpdate update) {
        return this.dao.updateByQuery(entityClass, update);
    }

    /**
     * 保存实体 返回新增后的主键 实际上 实体内的id也会设置 分布式环境下有点用 平时返回值没啥用
     */
    public <T extends Serializable> Object save(T entity) {
        return this.dao.save(entity);
    }

    /**
     * 批量保存实体
     */
    public <T extends Serializable> Long saveAll(List<T> entity) {
        return this.dao.saveAll(entity);
    }

    /**
     * @param <T>
     * @return 返回的修改行数
     */
    public <T extends Serializable> Long update(T entity, String... force) {
        return this.dao.update(entity, force);
    }


    public <T extends Serializable> Long execute(String sql, Map<String, Object> params) {
        return this.dao.executeSql(sql, params);
    }

    public <T extends Serializable> Long execute(String sql) {
        return this.dao.executeSql(sql, Collections.emptyMap());
    }

    public QueryResult findByQuery(QueryExecutor executor) {
        return this.dao.findByQuery(executor);
    }

    public <T extends Serializable> Object saveOrUpdate(T entity) {
        if (getEntityId(entity) != null) {
            return this.dao.update(entity);
        } else {
            return this.dao.save(entity);
        }
    }

    public <T extends Serializable> Object saveOrUpdate(T entity, String... force) {
        if (getEntityId(entity) != null) {
            return this.dao.update(entity, force);
        } else {
            return this.dao.save(entity);
        }
    }


    public <T extends Serializable> Long saveOrUpdateAll(List<T> entities, String... forceUpdateProps) {
        return this.dao.saveOrUpdateAll(entities, forceUpdateProps);
    }


    public Long getCount(String sqlOrSqlId, Map<String, Object> paramsMap) {
        return this.dao.getCount(sqlOrSqlId, paramsMap);
    }


    public <T extends Serializable> String getEntityTableName(Class<T> entityClass) {
        Entity annotation = entityClass.getAnnotation(Entity.class);
        if (annotation == null) {
            throw new RuntimeException("只支持 VO");
        }
        return annotation.tableName();
    }

    public <T extends Serializable> void addNum(T e, String filed, int num) {
        String tableName = getEntityTableName(e.getClass());
        if (StrUtil.isEmpty(tableName)) {
            return;
        }
        Long id = getEntityId(e);
        if (id == null) {
            return;
        }
        String sql = "update " + tableName + " set " + filed + " = " + filed + "+" + num + " where id = :id";

        this.dao.executeSql(sql, (Map<String, Object>) MapParam.of("id", id));

    }


    private <T extends Serializable> Long getEntityId(T entity) {

        if (entity == null || !isEntity(entity.getClass())) {
            return null;
        }
        try {
            return (Long) entity.getClass().getMethod("getId").invoke(entity);
        } catch (NoSuchMethodException ignored) {
        } catch (Exception e) {
            log.warn("Exception while getting entity id", e);
        }
        return null;
    }

    private <T extends Serializable> boolean isEntity(Class<T> entityClass) {
        Entity annotation = entityClass.getAnnotation(Entity.class);
        return annotation != null;
    }

    private String buildTCacheKey(Class t, Object param) {
        return StrFormatter.format("db-{}-{}-{}-{}",
                local.get().get("requestId"), t.getPackageName(), t.getName(), param);
    }


    private static <T, R> Page<R> page2Page(int page, int size, Function<List<T>, List<R>> handler, Page<T> tPage) {
        Page<R> result = new Page<>(size, page);
        result.setRows(handler.apply(tPage.getRows()));
        result.setPageSize(tPage.getPageSize());
        result.setRecordCount(tPage.getRecordCount());
        result.setOverPageToFirst(tPage.getOverPageToFirst());
        result.setPageNo(tPage.getPageNo());
        result.setSkipQueryCount(tPage.getSkipQueryCount());
        return result;
    }




    /**
     * 对集合进行相关排序
     *
     * @param rows        集合
     * @param sort        排序
     * @param sortHandler 集合内容排序字段获取
     * @param <T> 集合对象类型
     * @param <R> 排序值比较对象类型
     * @return 排序后的值 始终按照sort进行排序 如果sort中没有rows对应的值 则忽略掉rows中对应的值
     */
    public static <T, R> List<T> sort(Collection<T> rows,
                                      Collection<R> sort,
                                      Function<T, R> sortHandler) {
        if (rows == null || sort == null || sortHandler == null) {
            return CollUtil.newArrayList(rows);
        }
        return sort.stream().map(r -> CollUtil.findOne(rows, s -> r.equals(sortHandler.apply(s))))
                .collect(Collectors.toList());
    }

}
































