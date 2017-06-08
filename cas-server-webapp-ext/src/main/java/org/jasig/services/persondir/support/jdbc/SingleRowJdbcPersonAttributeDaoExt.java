package org.jasig.services.persondir.support.jdbc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

public class SingleRowJdbcPersonAttributeDaoExt extends SingleRowJdbcPersonAttributeDao {
	private String mobileAttribute = "mobile";
	private String emailAttribute = "email";

	public SingleRowJdbcPersonAttributeDaoExt() {
		super();
	}

	public SingleRowJdbcPersonAttributeDaoExt(final DataSource ds, final String sql) {
		super(ds, sql);
	}

	@Override
	protected Map<String, List<Object>> toSeedMap(final String uid) {
		final List<Object> values = Collections.singletonList((Object) uid);
		final String usernameAttribute = super.getUsernameAttributeProvider().getUsernameAttribute();

		final Map<String, List<Object>> seed = new HashMap<String,List<Object>>();
		seed.put(usernameAttribute, values);
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Created seed map='" + seed + "' for uid='" + uid + "'");
		}
		
		//允许手机、邮箱登录，然后查询出更多属性如uuid，返回给客户端
		seed.put(mobileAttribute, values);
		seed.put(emailAttribute, values);
		return seed;
	}
}
