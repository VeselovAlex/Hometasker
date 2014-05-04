package hometasker;

import hometasker.data.Group;
import hometasker.data.Teacher;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class TeacherServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		
		long id = new Long(req.getParameter("id")).longValue(); 
		Teacher teacher = Teacher.get(KeyFactory.createKey("Teacher", id));
		
		resp.getWriter().println("<h1>Teacher : " + teacher.getSurname() + " " + teacher.getFirstName() + " " + teacher.getLastName() + "</h1><br>");
		
		resp.getWriter().println("<h2>Groups </h2><br>");
		
		List<Group> groups = Group.getByTeacher(id);
		
		if (groups.isEmpty())
			resp.getWriter().println("There is no group added yet. Would you like to <a href = \"/addgroup?teacher_id=" + id + "\">add</a> the one?");
		else {
			String tableHeaderHTML = "<table border = 1>"
							 + "<caption>All groups</caption>"
							 + "<tr>"
							 + "<th></th>"
							 + "<th>Group #</th>"
							 + "<th>Subgroup #</th>"
							 + "<tr>";
			resp.getWriter().println(tableHeaderHTML);
			int i = 0;
			String tableRowHTML = "";
			for (Group group : groups) {
				tableRowHTML = "<tr><td>" + (++i) + "</td><td>" + group.getGroupNum() + "</td><td>" + group.getSubgroupNum() 
							 +  "</td><td>" + HTMLTemplates.createGroupViewURL(group.getKey().getId()) + "</td></tr>";
				resp.getWriter().println(tableRowHTML);
			}
			resp.getWriter().println("</table>");
			resp.getWriter().println("<br><h2><a href = \"/addgroup?teacher_id=" + id + "\">Add new group</a></h2>");
			
		}
	}
}
