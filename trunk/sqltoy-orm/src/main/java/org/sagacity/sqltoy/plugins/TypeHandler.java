/**
 * 
 */
package org.sagacity.sqltoy.plugins;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @project sagacity-sqltoy
 * @description 提供类型处理匹配，主要针对json、数组等类型
 * @author zhong
 * @version v1.0, Date:2020-11-25
 * @modify 2020-11-25,修改说明
 */
public abstract class TypeHandler {
	public boolean setNull(PreparedStatement pst, int paramIndex, int jdbcType) {
		return false;
	}

	public abstract boolean setValue(PreparedStatement pst, int paramIndex, int jdbcType, Object value)
			throws SQLException;

	public abstract Object toJavaType(String javaTypeName, Object jdbcValue) throws Exception;
}
