package com.example.sehci;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import org.mortbay.log.Log;

@SuppressWarnings("serial")
public class TutorialAuthServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// normally, all idempotent requests would be done on GET and all
		// non-idempotent on POST, but we can make it interchangeable for
		// demonstration purposes
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String op = req.getParameter("op");
		if (op == null)
			op = "list";

		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();

		try {
			switch (op) {
			case "register":
				handleUserRegister(req, out, pm);
				break;
			case "login":
				handleUserLogin(req, out, pm);
				break;
			case "touch":
				handleUserSessionTouch(req, out, pm);
				break;
			case "create":
				handleCreateEntry(req, out, pm);
				break;
			case "read":
				handleReadEntry(req, out, pm);
				break;
			case "list":
				handleListEntries(req, out, pm);
				break;
			case "search":
				handleSearchEntries(req, out, pm);
				break;
			case "update":
				handleUpdateEntry(req, out, pm);
				break;
			case "delete":
				handleDeleteEntry(req, out, pm);
				break;
			case "showAllBooks":
				handleShowAllBooks(req, out, pm);
				break;
			case "bookByID":
				handleBookByID(req,out,pm);
				break;
			case "bookByNameAuthor":
				handleBookByNameAuthor(req,out,pm);
				break;
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			out.write(Util.toJsonPair("errormsg", e.getMessage()));
		} finally {
			pm.close();
		}
	}

	private BookInfo getEntryAndVerifyOwnership(HttpServletRequest req, PersistenceManager pm) {
		User user = User.authenticate(req, pm);

		long id = Util.getLong(req, "id");
		if (id == 0)
			throw new IllegalArgumentException("Invalid or missing entry id.");

		// verify ownership
		BookInfo entry = BookInfo.loadById(id, pm);
		if (!user.getUsername().equals(entry.getOwner()))
			throw new IllegalStateException("Unauthorized access");
		return entry;
	}

	private void handleCreateEntry(HttpServletRequest req, PrintWriter out, PersistenceManager pm) {
		User user = User.authenticate(req, pm);

		BookInfo entry = new BookInfo();
		entry.setTitle(req.getParameter("title"));
		entry.setClassName(req.getParameter("className"));
		entry.setCollegeName(req.getParameter("collegeName"));
		entry.setPrice(req.getParameter("price"));
		entry.setContactInfo(req.getParameter("contactInfo"));
		entry.setAuthor(req.getParameter("author"));
		entry.setEdition(req.getParameter("edition"));
		
		entry.setWhen(Util.getLong(req, "when"));;
		entry.setOwner(user.getUsername());

		pm.makePersistent(entry);
		out.write(Util.toJson(entry));
	}

	private void handleDeleteEntry(HttpServletRequest req, PrintWriter out, PersistenceManager pm) {
		BookInfo entry = getEntryAndVerifyOwnership(req, pm);
		BookInfo.delete(entry, pm);
	}

	private void handleListEntries(HttpServletRequest req, PrintWriter out, PersistenceManager pm) {
		User user = User.authenticate(req, pm);
		List<BookInfo> entries = BookInfo.listByOwner(user.getUsername(), pm);
		out.write(Util.toJson(entries));
	}

	private void handleReadEntry(HttpServletRequest req, PrintWriter out, PersistenceManager pm) {
		BookInfo entry = getEntryAndVerifyOwnership(req, pm);
		out.write(Util.toJson(entry));
	}

	private void handleSearchEntries(HttpServletRequest req, PrintWriter out, PersistenceManager pm) {
		User user = User.authenticate(req, pm);
		List<BookInfo> entries = BookInfo.listByQuery(user.getUsername(), pm);
		out.write(Util.toJson(entries));
	}

	private void handleUpdateEntry(HttpServletRequest req, PrintWriter out, PersistenceManager pm) {
		BookInfo entry = getEntryAndVerifyOwnership(req, pm);
		entry.setTitle(req.getParameter("title"));
		entry.setClassName(req.getParameter("className"));
		entry.setCollegeName(req.getParameter("collegeName"));
		entry.setPrice(req.getParameter("price"));
		entry.setContactInfo(req.getParameter("contactInfo"));
		entry.setAuthor(req.getParameter("author"));
		entry.setEdition(req.getParameter("edition"));
		pm.makePersistent(entry);
		out.write(Util.toJson(entry));
	}
	
	private void handleShowAllBooks(HttpServletRequest req, PrintWriter out, PersistenceManager pm){
		String title = req.getParameter("title");
		String collegeName = req.getParameter("collegeName");
		
		if(title == null || collegeName == null){
			List<BookInfo> entry = BookInfo.listOfAll(pm);
			out.write(Util.toJson(entry));
		}
		else{
			List<BookInfo> entry = BookInfo.listOfSearch(title, collegeName, pm);
			out.write(Util.toJson(entry));
		}
		
	}
	
	private void handleBookByID(HttpServletRequest req, PrintWriter out, PersistenceManager pm){
		String bookid = req.getParameter("id");
		
		List<BookInfo> book = BookInfo.bookByID(bookid,pm);
		out.write(Util.toJson(book));
	}
	
	private void handleBookByNameAuthor(HttpServletRequest req, PrintWriter out, PersistenceManager pm){
		String title = req.getParameter("bookName");
		String author = req.getParameter("author");
		List<BookInfo> book = BookInfo.bookByNameAuthor(title,author,pm);
		out.write(Util.toJson(book));
	}
	
	private void handleUserLogin(HttpServletRequest req, PrintWriter out, PersistenceManager pm) {
		User user = User.authenticate(req, pm);
		out.write(Util.toJson(user));
	}
	
	private void handleUserRegister(HttpServletRequest req, PrintWriter out, PersistenceManager pm) {
		User user = User.create(req.getParameter("username"), req.getParameter("password"), pm);
		out.write(Util.toJson(user));
	}

	private void handleUserSessionTouch(HttpServletRequest req, PrintWriter out, PersistenceManager pm) {
		User user = User.authenticate(req, pm);
		out.write(Util.toJson(user));
	}
}
