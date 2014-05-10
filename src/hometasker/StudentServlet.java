package hometasker;

import java.io.IOException;
import java.util.List;

import hometasker.data.Hometask;
import hometasker.data.HometaskerUser;
import hometasker.data.HometaskerUserService;
import hometasker.data.Student;
import hometasker.data.StudentHometaskCard;
import hometasker.data.Task;
import hometasker.data.HometaskerUser.UserType;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
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
		HometaskerUser user = new HometaskerUserService().getCurrentUser();
		boolean authorized = user.getUserType() == UserType.TEACHER || user.getUserType() == UserType.ADMIN;
		
		if (hometasks.isEmpty())
			resp.getWriter().println("There is no hometask added yet.");
		else {
			for (Hometask hometask : hometasks) {
				
				String htHeader = "<h3>Hometask" + hometask.getHometaskNum() + (authorized ? ":	<a href = \"/updstudentprogress?sid=" + id
								+"&htid=" + hometask.getKey().getId() + "\">Edit</a>" : "") +"</h3>";
				
				List<Task> tasks = hometask.getAllTasks();	
				
				String tableHeader = "<table border = 1 width = 200>"
						 + "<caption>" + htHeader + "</caption>"
						 + "<tr>";
				String tableRow1 = "<tr>";
				String tableRow2 = "<tr>";
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
					tableRow1 += "<td bgcolor=\""+ cellColor + "\">" + mark + "</td>";
					
					boolean isAuthStudent = user.getUserType() == UserType.STUDENT && user.getPerson().getKey().getId() == id; 
					
					String uploadForm = "<form action=\"" + BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/file") 
							+ "\" method=\"post\" enctype=\"multipart/form-data\">"
							+ "<input type=\"file\" name=\"filename\"><br>"
							+ "<input type=\"hidden\" name=\"card_id\" value=" + card.getKey().getId() + ">"
			                + "<input type=\"submit\" value=\"Submit\">";
					
					if (card.getTaskFileUrl() == null) {
						tableRow2 += "<td>" + (isAuthStudent ? uploadForm : "No file") + "</td>";
					} else {
						tableRow2 += "<td><a href = \"" + card.getTaskFileUrl() + "\">View</a><br>" + (isAuthStudent ? uploadForm : "") + "</td>";
					}
						
				}
				
				resp.getWriter().println(tableHeader + "</tr>" + tableRow1 + "</tr>" + tableRow2 + "</tr>");	
			} 
		}
	}
	
}
