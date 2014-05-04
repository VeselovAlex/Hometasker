package hometasker;

import java.io.IOException;
import java.util.List;

import hometasker.data.Hometask;
import hometasker.data.Student;
import hometasker.data.StudentHometaskCard;
import hometasker.data.Task;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class StudentServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		
		long id = new Long(req.getParameter("id")).longValue(); 
		Student student = Student.get(KeyFactory.createKey("Student", id));
		
		resp.getWriter().println("<a href = \"group?grId=" + student.getGroupId() + "\"> Back to group </a><br>");
		resp.getWriter().println("<h1>Student : " + student.getSurname() + " " + student.getFirstName() + " " + student.getLastName() + "</h1><br>");
		
		resp.getWriter().println("<h2>Hometask progress: </h2><br>");
		
		List<Hometask> hometasks = Hometask.getByGroupId(student.getGroupId());
		
		if (hometasks.isEmpty())
			resp.getWriter().println("There is no hometask added yet.");
		else {
			for (Hometask hometask : hometasks) {
				
				String htHeader = "<h3>Hometask" + hometask.getHometaskNum() +":	<a href = \"/updstudentprogress?sid=" + id
								+"&htid=" + hometask.getKey().getId() + "\">Edit</a></h3>";
				
				List<Task> tasks = hometask.getAllTasks();	
				
				String tableHeader = "<table border = 1 width = 200>"
						 + "<caption>" + htHeader + "</caption>"
						 + "<tr>";
				String tableRow = "<tr>";
				int i = 0;
				
				for (Task task : tasks) {
					StudentHometaskCard card = StudentHometaskCard.getByStudentAndTask(id, task.getKey().getId());
					
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
					
					tableHeader += "<th>" + (++i) + "</th>";
					tableRow += "<td bgcolor=\""+ cellColor + "\">" + mark + "</td>";
				}
				
				resp.getWriter().println(tableHeader + "</tr>" + tableRow + "</tr>");	
			} 
		}
	}
	
}
