package hometasker;

import hometasker.data.Hometask;
import hometasker.data.HometaskerUserService;
import hometasker.data.Task;
import hometasker.data.HometaskerUser.UserType;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class HometaskServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		long htId = new Long(req.getParameter("htId")).longValue(); 
		Hometask hometask = Hometask.get(KeyFactory.createKey("Hometask", htId));
		resp.getWriter().println("<a href = \"group?grId=" + hometask.getGroupId() + "\"> Back to group </a><br>");
		resp.getWriter().println("<h1>Hometask #" + hometask.getHometaskNum() + "</h1><br>");
		resp.getWriter().println("<h2>Subject: " + hometask.getSubject() + "</h2><br>");
		resp.getWriter().println("<h3>Start date / End date : " + hometask.getStartDate() + " / " + hometask.getEndDate() + "</h3><br>");
		
		List<Task> tasks= hometask.getAllTasks();
		
		UserType user = new HometaskerUserService().getCurrentUser().getUserType();
		boolean authorized = user == UserType.TEACHER || user == UserType.ADMIN;
		
		if (tasks.isEmpty())
			resp.getWriter().println("There is no task in group added yet." + (authorized ? " Would you like to <a href = \"/addtask?htId=" + htId + "\">add</a> the one?"
																						  : "<br>"));
		else {
			String tableHeaderHTML = "<table border = 1 width = 200>"
							 + "<caption>All tasks</caption>"
							 + "<tr>"
							 + "<th></th>"
							 + "<th>Text</th>"
							 + "<tr>";
			resp.getWriter().println(tableHeaderHTML);
			String tableRowHTML = "";
			for (Task task : tasks) {
				tableRowHTML = "<tr><td>" + task.getTaskNum() + (task.isFine() ? '*' : "") + "</td><td>" 
							 + "<p>" + (task.isAdditional() ? "(additional) " : "");
				resp.getWriter().println(tableRowHTML);
				resp.getWriter().print(task.getText().getValue());
				resp.getWriter().println("</td></tr>");
			}
			resp.getWriter().println("</table>");
			if (authorized)
				resp.getWriter().println("<br><h2><a href = \"/addtask?htId=" + htId + "\">Add new task</a></h2><br>");
			
		}
	}
}
