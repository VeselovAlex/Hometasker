package hometasker;

import hometasker.data.Group;
import hometasker.data.Hometask;
import hometasker.data.Student;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class CurrentGroupServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");	
		long id;
		try {
			id = new Long(req.getParameter("grId")).longValue();
		} catch(NullPointerException np) {
			resp.sendRedirect("/groups");
			return;
		} catch(NumberFormatException nf) {
			resp.sendRedirect("/groups");
			return;
		}
		
		Group group = Group.get(KeyFactory.createKey("Group", id));
		resp.getWriter().println("<h1>Group " + group.getGroupNum() + "/" + group.getSubgroupNum() + "</h1><br>");
		resp.getWriter().println("<h3>Students:</h3><br>");
		
		List<Student> students = Student.getByGroup(id);
		
		if (students.isEmpty())
			resp.getWriter().println("There is no student in group added yet. Would you like to <a href = \"/addstudent?grId=" + id +"\">add</a> the one?");
		else {
			String tableHeaderHTML = "<table border = 1>"
							 + "<caption>All students</caption>"
							 + "<tr>"
							 + "<th></th>"
							 + "<th>First name</th>"
							 + "<th>Last Name</th>"
							 + "<th>Surname</th>"
							 + "<th></th>"
							 + "<tr>";
			resp.getWriter().println(tableHeaderHTML);
			int i = 0;
			String tableRowHTML = "";
			for (Student student : students) {
				tableRowHTML = "<tr><td>" + (++i) + "</td><td>" + student.getFirstName() + "</td><td>" + student.getLastName() 
							 +  "</td><td>" + student.getSurname() + "</td><td>" + createStudentViewURL(student.getKey().getId()) + "</td></tr>";
				resp.getWriter().println(tableRowHTML);
			}
			resp.getWriter().println("</table>");
			resp.getWriter().println("<br><h2><a href = \"/addstudent?grId=" + id +"\">Add new student</a></h2><br>");
			
		}
		
		resp.getWriter().println("<h3>Hometasks:</h3><br>");
		List<Hometask> hometasks = Hometask.getByGroupId(group.getKey().getId());
		
		if (hometasks.isEmpty())
			resp.getWriter().println("There is no hometask added yet. Would you like to <a href = \"/addhometask?grId=" + id +"\">add</a> the one?");
		else {
			String tableHeaderHTML = "<table border = 1>"
							 + "<caption>All hometasks</caption>"
							 + "<tr>"
							 + "<th></th>"
							 + "<th>Subject</th>"
							 + "<th>Start date</th>"
							 + "<th>End date</th>"
							 + "<th></th>"
							 + "<tr>";
			resp.getWriter().println(tableHeaderHTML);
			String tableRowHTML = "";
			for (Hometask hometask : hometasks) {
				tableRowHTML = "<tr><td>" + hometask.getHometaskNum() + "</td><td>" + hometask.getSubject() + "</td><td>" + hometask.getStartDate() 
							 +  "</td><td>" + hometask.getEndDate() + "</td><td>" + createHometaskViewURL(hometask.getKey().getId()) +  "</td></tr>";
				resp.getWriter().println(tableRowHTML);
			}
			resp.getWriter().println("</table>");
			resp.getWriter().println("<br><h2><a href = \"/addhometask?grId=" + id +"\">Add new hometask</a></h2>");
			
		}
	}
	
	private String createHometaskViewURL(long id) {
		return "<a href = \"/hometask?htId=" + id + "\">View</a>";
	}
	
	private String createStudentViewURL(long id) {
		return "<a href = \"/student?id=" + id + "\">View</a>";
	}
}
