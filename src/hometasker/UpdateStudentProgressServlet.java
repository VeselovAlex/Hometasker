package hometasker;

import hometasker.data.Hometask;
import hometasker.data.StudentHometaskCard;
import hometasker.data.Task;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class UpdateStudentProgressServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		long sid = new Long(req.getParameter("sid")).longValue(); 
		long htid = new Long(req.getParameter("htid")).longValue(); 
		
		Hometask ht = Hometask.get(KeyFactory.createKey("Hometask", new Long(htid).longValue()));
		List<Task> tasks = ht.getAllTasks();
		
		String tableHeader = "<form action = \"/updstudentprogress\" method = \"post\">"
				 + "<input type = \"hidden\" name = \"size\" value = " + tasks.size() + ">"
				 + "<input type = \"hidden\" name = \"sid\" value = " + sid + ">"
				 + "<input type = \"hidden\" name = \"htid\" value = " + htid + ">"
				 + "<table border = 1 width = 200>"
				 + "<caption>Hometask</caption>"
				 + "<tr>";
		String tableRow = "<tr>";
		int i = 0;
		
		for (Task task : tasks) {
			StudentHometaskCard card = StudentHometaskCard.getByStudentAndTask(sid, task.getKey().getId());
			
			float mark = card.getMark();
			String cellColor;
			
			if (mark < 0)
				cellColor = "red";
			else if (mark < 0.25) 
				cellColor = "lightgrey";
			else if (mark < 0.75)
				cellColor = "yellow";
			else
				cellColor = "lightgreen";
			
			tableHeader += "<th>" + i + "</th>";
			tableRow += "<td bgcolor=\""+ cellColor + "\">"
					 + "<input type = \"hidden\" name  = \"taskid" + i + "\" value = " + task.getKey().getId() + ">" 
					 + " <input type = \"text\" name  = \"task" + (i++) + "\" value = " + mark + "></td>";
		}
		resp.setContentType("text/html");
		resp.getWriter().println(tableHeader + "</tr>" + tableRow + "</tr><br><input type = \"submit\"></form>");	
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		int size = new Integer(req.getParameter("size")).intValue();
		long sid = new Long(req.getParameter("sid")).longValue(); 
		
		for (int i = 0; i < size; i++) {
			float new_mark = new Float(req.getParameter("task" + i)).floatValue();
			long id = new Long(req.getParameter("taskid" + i)).longValue();
			StudentHometaskCard card = StudentHometaskCard.getByStudentAndTask(sid, id);
			card.setMark(new_mark);
			card.save();
		}
		
		
		resp.sendRedirect("/groups");
	}
}
