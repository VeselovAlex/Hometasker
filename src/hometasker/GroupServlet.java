package hometasker;

import hometasker.data.Group;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class GroupServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		resp.getWriter().println("<h1>Groups:</h1>");
		
		List<Group> groups = Group.getAll();
		
		if (groups.isEmpty())
			resp.getWriter().println("There is no group added yet. Would you like to <a href = \"/addgroup\">add</a> the one?");
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
							 +  "</td><td>" + createViewURL(group.getKey().getId()) + "</td></tr>";
				resp.getWriter().println(tableRowHTML);
			}
			resp.getWriter().println("</table>");
			resp.getWriter().println("<br><h2><a href = \"/addgroup\">Add new group</a></h2>");
			
		}
	}
	
	private String createViewURL(long id) {
		return "<a href = \"/group?grId=" + id + "\">View</a>";
	}
}
