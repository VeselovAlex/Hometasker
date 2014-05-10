package hometasker;

import hometasker.data.Hometask;
import hometasker.data.Student;
import hometasker.data.StudentHometaskCard;
import hometasker.data.Task;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class AddStudentServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.getWriter().println("<a href = \"group?grId=" + req.getParameter("grId") + "\"> Back to group </a><br>");
		String studHeader = "<h1>Insert new student data</h1><br><br>";
		String studFormHTML = "<form action = \"/addstudent\" method = \"post\">"
							+ "Surname:     <input type = \"text\" name = \"surname\" value  = \"Surname\"><br>"
							+ "First name:  <input type = \"text\" name = \"first_name\" value  = \"First name\"><br>"
							+ "Last name: <input type = \"text\" name = \"last_name\" value  = \"Last name\"><br>"
							+ "Date format yyyy-mm-dd<br>"
							+ "Birth date:  <input type = \"text\" name = \"b_date\"><br>"
							+ "<input type = \"hidden\" name = \"group\" value = " + req.getParameter("grId") + ">"
							+ "<input type = \"submit\" value = \"Add\">"
							+ "<input type = \"reset\" value = \"Clear\">"
							+ "</form>";
		resp.getWriter().println(studHeader + studFormHTML);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Student student = new Student();
		
		long grId = new Long(req.getParameter("group")).longValue();
		String fname = req.getParameter("first_name");
		if (fname == null)
			resp.sendRedirect(req.getRequestURI());
		else
			student.setFirstName(fname);
		
		String lname = req.getParameter("last_name");
		if (lname == null)
			resp.sendRedirect(req.getRequestURI());
		else
			student.setLastName(lname);
		
		String sname = req.getParameter("surname");
		if (sname == null)
			resp.sendRedirect(req.getRequestURI());
		else
			student.setSurname(sname);
		
		try {
			student.setBirthDate(Date.valueOf(req.getParameter("b_date")));
		} catch(IllegalArgumentException e) {
			student.setBirthDate(Date.valueOf("1991-01-01"));
		}
		
		student.setGroupId(grId);
		student.save();
		//Insert pages for home tasks
		List<Hometask> hometasks = Hometask.getByGroupId(grId);
		for (Hometask hometask : hometasks) {
			List<Task> tasks = hometask.getAllTasks();
			for (Task task : tasks) {
				StudentHometaskCard card = new StudentHometaskCard();
				card.setStudentId(student.getKey().getId());
				card.setTaskId(task.getKey().getId());
				card.setMark(0.0F);
				card.save();
			}
		}
		
		resp.sendRedirect("/group?grId=" + grId);
	}
}
