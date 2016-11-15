package com.example.sehci;

import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.servlet.http.HttpServletRequest;
 
@PersistenceCapable
public class User { 
	private static String clean(String username) {
		if (username == null) 
			username = "";
		return username.trim().toLowerCase();
	}

	private static final long SESSION_TIMEOUT = 20 * 60 * 1000; // 20 minutes

	// Creates a user account and logs in, returning the session id;
	// but if the username is taken, then throws IllegalStateException
	public static User create(String username, String password, PersistenceManager pm) {
		username = clean(username);
		if (username.length() == 0)
			throw new IllegalArgumentException("Empty or missing username");
		if (password == null || password.length() == 0)
			throw new IllegalArgumentException("Empty or missing password");

		User existing = null;
		try {
			existing = pm.getObjectById(User.class, username);
		} catch (JDOObjectNotFoundException ignoreThis) {
		}
		if (existing != null)
			throw new IllegalStateException("Username already exists");

		User user = new User();
		user.setUsername(username);
		user.setHashpass(Util.hash(username, password));
		user.setSession(Util.getRandom());
		user.setExpires(SESSION_TIMEOUT + System.currentTimeMillis());
		pm.makePersistent(user);
		return user;
	}

	// Attempts to log the specified user in, initializes a new session,
	// and returns the session ID. Throws IllegalArgumentException if invalid
	// username or password.
	@SuppressWarnings("unchecked")
	private static User authenticate(String username, String password, PersistenceManager pm) {
		username = clean(username);
		if (username.length() == 0)
			throw new IllegalArgumentException("Empty or missing username");
		if (password == null || password.length() == 0)
			throw new IllegalArgumentException("Empty or missing password");

		Query query = pm.newQuery(User.class, "username == :uu && hashpass == :pp");
		String hashpass = Util.hash(username, password);
		List<User> rv = (List<User>) query.execute(username, hashpass);
		User user = rv.size() > 0 ? rv.get(0) : null;
		query.closeAll();
		if (user != null) {
			user.setSession(Util.getRandom());
			user.setExpires(System.currentTimeMillis() + SESSION_TIMEOUT);
			pm.makePersistent(user);
			return user;
		} else
			throw new IllegalArgumentException("Incorrect username and/or password");
	}

	// Checks if the specified session id is correct for the specified username,
	// and that the session hasn't yet expires. If it's a valid session,
	// then the expiration is incremented (to mark it as still "active").
	// If the session is missing, expired, or otherwise unavailable,
	// then this method throws an IllegalStateException.
	@SuppressWarnings("unchecked")
	private static User checkSession(long sessionId, PersistenceManager pm) {
		Query query = pm.newQuery(User.class, "session == :ss && expires >= :ee");
		List<User> rv = (List<User>) query.execute(sessionId, System.currentTimeMillis());
		User user = rv.size() > 0 ? rv.get(0) : null;
		query.closeAll();
		if (user != null) {
			user.setExpires(System.currentTimeMillis() + SESSION_TIMEOUT);
			pm.makePersistent(user);
			return user;
		} else
			throw new IllegalArgumentException("Missing and/or expired session identifier");
	}

	// Tries to authenticate the user, with a username and password, or with a
	// sessionid. Throws IllegalArgumentException on invalid username and
	// password,
	// or on invalid sessionid. Throws IllegalStateException if no credentials
	// are present.
	public static User authenticate(HttpServletRequest req, PersistenceManager pm) {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		if (username != null && password != null)
			return authenticate(username, password, pm);

		// normally, we'd want to pass the session ID as an http header (so it
		// doesn't get logged), but we can do it this way for demonstration
		// purposes
		long sessionId = Util.getLong(req, "sessionId");
		if (sessionId > 0)
			return checkSession(sessionId, pm);

		throw new IllegalStateException("No username and password, or sessionId, present on request");
	}

	@PrimaryKey
	@Persistent
	private String username;

	public String getUsername() {
		return username != null ? username : "";
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHashpass() {
		return hashpass != null ? hashpass : "";
	}

	public void setHashpass(String hashpass) {
		this.hashpass = hashpass;
	}

	public long getSession() {
		return session != null ? session.longValue() : 0L;
	}

	public void setSession(Long session) {
		this.session = session;
	}

	public long getExpires() {
		return expires != null ? expires.longValue() : 0L;
	}

	public void setExpires(Long expires) {
		this.expires = expires;
	}

	@Persistent
	private String hashpass;

	@Persistent
	private Long session;

	@Persistent
	private Long expires;

}
