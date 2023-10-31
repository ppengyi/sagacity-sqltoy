package org.sagacity.sqltoy.support.solon;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author jerry
 * @since 2023/6/8 0:06
 */
public class MapParam extends HashMap<String, Object> implements Serializable {

    protected final Logger logger = LoggerFactory.getLogger(MapParam.class);


    public static MapParam of(String key, Object value) {
        MapParam obj = new MapParam();
        obj.obj(key, value);
        return obj;
    }

    public static MapParam of(Object request) {
        MapParam obj = new MapParam();
        BeanUtil.beanToMap(request, obj, false, true);
        return obj;
    }

    public static MapParam of() {
        return new MapParam();
    }

    public static MapParam build() {
        return new MapParam();
    }

    public MapParam str(String key, String value) {
        super.put(key, value);
        return this;
    }


    public MapParam obj(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public MapParam date(String key, String value) {
        if (StrUtil.isBlankOrUndefined(value)) {
            return this;
        }
        try {
            DateTime date = DateUtil.parse(value);
            super.put(key, date.toJdkDate());
        } catch (Exception e) {
            logger.warn("Failed to parse date: " + value, e);
        }
        return this;
    }

    public MapParam num(String key, Object value) {
        if (StrUtil.isBlankOrUndefined(key) || value == null) {
            return this;
        }
        try {
            super.put(key, NumberUtil.parseNumber(String.valueOf(value)));
        } catch (NumberFormatException ignore) {
        } catch (Exception e) {
            logger.warn("Failed to parse number: " + value, e);
        }
        return this;
    }

    public MapParam _num(String key, Object[] value) {
        if (ArrayUtil.isEmpty(value) || StrUtil.isBlank(key)) {
            return this;
        }
        HashSet<Number> numbers = CollUtil.newHashSet();
        for (Object o : value) {
            try {
                numbers.add(NumberUtil.parseNumber(String.valueOf(o)));
            } catch (NumberFormatException ignore) {
            } catch (Exception e) {
                logger.warn("Failed to parse number: " + o, e);
            }
        }
        if (CollUtil.isNotEmpty(numbers)) {
            super.put(key, numbers);
        }
        return this;
    }

    public MapParam _num(String key, Collection<?> value) {
        if (CollUtil.isEmpty(value) || StrUtil.isBlank(key)) {
            return this;
        }
        return _num(key, value.toArray());
    }


    public MapParam _str(String key, String... value) {
        super.put(key, value);
        return this;
    }

    public MapParam str(String key, Object value) {
        if (StrUtil.isNullOrUndefined(StrUtil.toString(value))) {
            return this;
        }
        super.put(key, String.valueOf(value));
        return this;
    }

    public MapParam _enum(String key, Enum<?>... value) {
        if (ArrayUtil.isEmpty(value)) {
            return this;
        }
        super.put(key, Arrays.stream(value).map(Enum::name).toArray(String[]::new));
        return this;
    }




    /**
     * 包含的语句
     */
    public MapParam include(String key, String value, Object... params) {
        String format = StrUtil.format(value, params);
        if (StrUtil.isNullOrUndefined(format)) {
            return this;
        }
        super.put(key, format);
        return this;
    }


    /**
     * 包含的语句
     * params 不能为空 字符串校验 空字符串 数组校验空数组 集合校验空集合 任意一个元素不满足则不加入查询
     */
    public MapParam includeCheckParams(String key, String value, Object... params) {
        if (ObjectUtil.isAllEmpty(params)) {
            return this;
        }
        String format = StrUtil.format(value, params);
        if (StrUtil.isNullOrUndefined(format)) {
            return this;
        }
        super.put(key, format);
        return this;
    }
}
