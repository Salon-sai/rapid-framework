package cn.org.rapid_framework.web.session.store;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import cn.org.rapid_framework.web.mvc.Scope;
import cn.org.rapid_framework.web.session.SessionStore;
/**
 * <pre>
 *	CREATE TABLE http_session (
 *	  session_id char(32) NOT NULL PRIMARY KEY,
 *	  session_data text,
 *	  expire_date datetime default NULL
 *	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 * </pre>
 * 
 * @author badqiu
 *
 */
public class JdbcSessionStore implements SessionStore{
	DataSource ds;
	String DELETE = "delete from http_session where session_id = ?";
	String INSERT = "insert http_session(session_id,session_data,expire_date) values (?,?,?)";
	String GET = "select session_data from http_session where session_id = ? and expire_date > ?";
	public void deleteSession(HttpServletResponse response,String sessionId) {
		getSimpleJdbcTemplate().update(DELETE, sessionId);
	}

	public Map getSession(HttpServletRequest request, String sessionId) {
		List<Map> results = getSimpleJdbcTemplate().query(GET,new ParameterizedRowMapper<Map>(){
			public Map mapRow(ResultSet rs, int row) throws SQLException {
				String sessionData = rs.getString(1);
				return decode(sessionData);
			}
		},sessionId);
		
		return results.size() > 0 ? results.get(0) : new HashMap();
	}

	public void saveSession(HttpServletResponse response, String sessionId,Map sessionData) {
		Scope.Session.save(response,sessionData);
	}

	private SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return new SimpleJdbcTemplate(ds);
	}
	
	public static Map decode(String sessionData) {
		return null;
	}
	
	public static String encode(Map sessionData) {
		return null;
	}
}
