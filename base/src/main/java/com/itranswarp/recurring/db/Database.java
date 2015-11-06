package com.itranswarp.recurring.db;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.itranswarp.recurring.base.context.TenantContext;
import com.itranswarp.recurring.base.model.Tenant;
import com.itranswarp.recurring.base.model.User;
import com.itranswarp.recurring.common.util.ClassScanner;
import com.itranswarp.recurring.common.util.IdGenerator;
import com.itranswarp.recurring.db.model.BaseEntity;

/**
 * A Spring-JdbcTemplate wrapper.
 */
@Named
public class Database {

	@Inject
	JdbcTemplate jdbcTemplate;

	final SqlObjectConverters converters;

	final Log log = LogFactory.getLog(getClass());
	final Map<String, Mapper<?>> mapping;

	@Inject
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Database(SqlObjectConverters converters) {
		List<Class<?>> classes = new ClassScanner().scan("com.itranswarp", c -> {
			return c.isAnnotationPresent(Entity.class);
		});
		Map<String, Mapper<?>> map = new HashMap<String, Mapper<?>>();
		for (Class<?> clazz : classes) {
			String name = clazz.getSimpleName().toLowerCase();
			if (map.containsKey(name)) {
				log.error("Duplicate table name: " + name + ", defined in class: " + map.get(name).clazz.getName()
						+ " and " + clazz.getName());
				throw new RuntimeException("Duplicate table name: " + name);
			}
			map.put(name, new Mapper(clazz, converters));
		}
		this.mapping = map;
		this.converters = converters;
	}

	/**
	 * Get a model instance by class type and id.
	 * 
	 * @param clazz
	 * @param id
	 * @return modelInstance
	 */
	public <T> T get(Class<T> clazz, String id) {
		String name = clazz.getSimpleName();
		@SuppressWarnings("unchecked")
		Mapper<T> mapper = (Mapper<T>) this.mapping.get(name.toLowerCase());
		return jdbcTemplate.queryForObject("select * from " + name + " where id=?", mapper.rowMapper, id);
	}

	public void remove(BaseEntity... beans) {
		for (BaseEntity bean : beans) {
			String name = bean.getClass().getSimpleName();
			Mapper<?> mapper = this.mapping.get(name.toLowerCase());
			Map<String, Field> fields = mapper.fields;
			Object idValue = null;
			try {
    			idValue = fields.get("id").get(bean);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			jdbcTemplate.update(mapper.deleteSQL, idValue);
		}
	}

	/**
	 * Start a select-query.
	 * 
	 * @param clazz
	 * @return Model
	 */
	public <T extends BaseEntity> From<T> from(Class<T> clazz) {
		return new From<T>(this, clazz);
	}

	public void update(BaseEntity... beans) {
		long current = System.currentTimeMillis();
		User user = TenantContext.getCurrentUser();
		for (BaseEntity bean : beans) {
			// set updatedBy and updatedAt:
			bean.setUpdatedAt(current);
			if (bean.getUpdatedBy() == null) {
				bean.setUpdatedBy(user.getId());
			}
			String name = bean.getClass().getSimpleName();
			Mapper<?> mapper = this.mapping.get(name.toLowerCase());
			Map<String, Field> fields = mapper.fields;
			Object[] args = new Object[mapper.updateFields.size() + 1];
			int n = 0;
			try {
				for (String field : mapper.updateFields) {
					Field f = fields.get(field);
					args[n] = this.converters.javaObjectToSqlObject(f.getType(), f.get(bean));
					n++;
				}
				args[n] = fields.get("id").get(bean);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			jdbcTemplate.update(mapper.updateSQL, args);
		}
	}

	public void save(BaseEntity... beans) {
		long current = System.currentTimeMillis();
		Tenant tenant = TenantContext.getCurrentTenant();
		User user = TenantContext.getCurrentUser();
		for (BaseEntity bean : beans) {
			// set tenant:
			bean.setTenantId(tenant.getId());
			// automatically set primary key:
			if (bean.getId() == null) {
				bean.setId(IdGenerator.next());
			}
			// set user:
			if (bean.getCreatedBy() == null) {
				bean.setCreatedBy(user.getId());
			}
			if (bean.getUpdatedBy() == null) {
				bean.setUpdatedBy(user.getId());
			}
			// set time:
			bean.setCreatedAt(current);
			bean.setUpdatedAt(current);
			String name = bean.getClass().getSimpleName();
			Mapper<?> mapper = this.mapping.get(name.toLowerCase());
			Map<String, Field> fields = mapper.fields;
			Object[] args = new Object[mapper.insertFields.size()];
			int n = 0;
			try {
				for (String field : mapper.insertFields) {
					Field f = fields.get(field);
					args[n] = this.converters.javaObjectToSqlObject(f.getType(), f.get(bean));
					n++;
				}
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			jdbcTemplate.update(mapper.insertSQL, args);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> T fetch(Class<T> clazz, String id) {
		String name = clazz.getSimpleName();
		Mapper<T> mapper = (Mapper<T>) this.mapping.get(name.toLowerCase());
		String sql = "select * from " + name + " where id=? limit 2";
		log.info("SQL: " + sql);
		List<T> list = (List<T>) jdbcTemplate.query(sql, new Object[] { id }, mapper.rowMapper);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	public <T extends BaseEntity> List<T> list(String sql, Object... args) {
		log.info("SQL: " + sql);
		Mapper<T> mapper = getEntity(sql);
		List<T> results = (List<T>) jdbcTemplate.query(sql, args, mapper.rowMapper);
		return Collections.unmodifiableList(results);
	}

	public int queryForInt(String sql, Object... args) {
		log.info("SQL: " + sql);
		Integer num = jdbcTemplate.queryForObject(sql, args, Integer.class);
		return num.intValue();
	}

	public <T extends BaseEntity> T unique(String sql, Object... args) {
		if (sql.indexOf(" limit ") == (-1)) {
			sql = sql + " limit 2";
		}
		List<T> list = list(sql, args);
		if (list.isEmpty()) {
			throw new RuntimeException();
		}
		if (list.size() > 1) {
			throw new RuntimeException();
		}
		return list.get(0);
	}

	public <T extends BaseEntity> T fetch(String sql, Object... args) {
		if (sql.indexOf(" limit ") == (-1)) {
			sql = sql + " limit 2";
		}
		List<T> list = list(sql, args);
		if (list.isEmpty()) {
			return null;
		}
		if (list.size() > 1) {
			throw new RuntimeException();
		}
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	<T extends BaseEntity> Mapper<T> getEntity(String sql) {
		String[] ss = sql.split("\\s+");
		for (int i = 0; i < ss.length; i++) {
			if (ss[i].toLowerCase().equals("from")) {
				if (i < ss.length - 1) {
					String table = ss[i + 1];
					if (table.length() > 2 && table.startsWith("`") && table.endsWith("`")) {
						table = table.substring(1, table.length() - 1);
					}
					return (Mapper<T>) this.mapping.get(table.toLowerCase());
				}
			}
		}
		throw new RuntimeException("Cannot find entity.");
	}

	<T extends BaseEntity> void checkAfterSelect(List<T> list) {
		String currentTenantId = TenantContext.getCurrentTenant().getId();
		for (T t : list) {
			if (!currentTenantId.equals(t.getTenantId())) {
				throw new RuntimeException("Out of current tenant scope!");
			}
		}
	}
}

class Mapper<T> {
	final Class<T> clazz;
	final Map<String, Field> fields;
	final BeanRowMapper<T> rowMapper;
	final String insertSQL;
	final String updateSQL;
	final String deleteSQL;
	final List<String> insertFields;
	final List<String> updateFields;

	public Mapper(Class<T> clazz, SqlObjectConverters converters) {
		super();
		this.clazz = clazz;
		this.fields = getAllFields(clazz);
		this.rowMapper = new BeanRowMapper<T>(clazz, fields, converters);
		this.insertFields = new ArrayList<String>(fields.keySet());
		this.insertSQL = buildInsertSQL(clazz, this.insertFields);
		this.updateFields = getUpdateFields(this.fields);
		this.updateSQL = buildUpdateSQL(clazz, this.updateFields);
		this.deleteSQL = buildDeleteSQL(clazz);
	}

	String buildDeleteSQL(Class<T> clazz) {
		return new StringBuilder().append("delete from ").append(clazz.getSimpleName()).append(" where id = ?").toString();
	}

	List<String> getUpdateFields(Map<String, Field> allFields) {
		List<String> updates = allFields.keySet().stream().filter((s) -> {
			if ("id".equals(s)) {
				return false;
			}
			Field f = allFields.get(s);
			if (f.isAnnotationPresent(Column.class)) {
				return f.getAnnotation(Column.class).updatable();
			}
			return true;
		}).collect(Collectors.toList());
		// add "id" at last for "where id = ?"
		List<String> all = new ArrayList<String>(updates);
		all.add("id");
		return all;
	}

	String buildUpdateSQL(Class<T> clazz, List<String> fields) {
		return new StringBuilder().append("update ").append(clazz.getSimpleName()).append(" set ")
				.append(String.join(", ", fields.stream().map((s) -> {
					return s + " = ?";
				}).collect(Collectors.toList())))
				.append(" where id = ?").toString();
	}

	String buildInsertSQL(Class<T> clazz, List<String> fields) {
		return new StringBuilder().append("insert into ").append(clazz.getSimpleName()).append(" (")
				.append(String.join(", ", fields)).append(") values (")
				.append(String.join(", ", fields.stream().map((s) -> {
					return "?";
				}).collect(Collectors.toList()))).append(")").toString();
	}

	Map<String, Field> getAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		addFields(clazz, fields);
		Map<String, Field> map = new HashMap<String, Field>();
		for (Field f : fields) {
			if (!f.isAnnotationPresent(Transient.class) && !Modifier.isStatic(f.getModifiers())
					&& !Modifier.isFinal(f.getModifiers())
					&& !Modifier.isTransient(f.getModifiers())) {
				f.setAccessible(true);
				map.put(f.getName(), f);
			}
		}
		return map;
	}

	void addFields(Class<?> clazz, List<Field> fields) {
		if (clazz.equals(Object.class)) {
			return;
		}
		addFields(clazz.getSuperclass(), fields);
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
	}
}

class BeanRowMapper<T> implements RowMapper<T> {

	static final Set<String> SUPPORTED_TYPES = new HashSet<String>(Arrays.asList(boolean.class.getName(),
			Boolean.class.getName(), short.class.getName(), Short.class.getName(), int.class.getName(),
			Integer.class.getName(), long.class.getName(), Long.class.getName(), float.class.getName(),
			Float.class.getName(), double.class.getName(), Double.class.getName(), String.class.getName(),
			LocalDate.class.getName(), LocalTime.class.getName(), LocalDateTime.class.getName()));

	final Map<String, Field> fields;
	final Function<T, T> constructor;
	final SqlObjectConverters converters;

	BeanRowMapper(Class<T> clazz, Map<String, Field> fields, SqlObjectConverters converters) {
		this.fields = fields;
		this.converters = converters;
		try {
			Constructor<T> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			this.constructor = (t) -> {
				try {
					return constructor.newInstance();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			};
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T bean = this.constructor.apply(null);
		ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();
		try {
			for (int i = 1; i <= count; i++) {
				Object obj = rs.getObject(i);
				String name = meta.getColumnName(i);
				Field f = this.fields.get(name);
				if (f != null) {
					f.set(bean, this.converters.sqlObjectToJavaObject(f.getType(), obj));
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return bean;
	}

}
