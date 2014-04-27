package hometasker;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import hometasker.data.Hometask;
import hometasker.data.Student;
import hometasker.data.StudentHometaskCard;
import hometasker.data.StudentTaskProgress;
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
		
		resp.getWriter().println("<h1>Student : " + student.getSurname() + " " + student.getFirstName() + " " + student.getLastName() + "</h1><br>");
		
		resp.getWriter().println("<h2>Hometask progress: </h2><br>");
		
		List<StudentHometaskCard> cards = StudentHometaskCard.getByStudentId(id);
		List<Hometask> hometasks = Hometask.getByGroupId(student.getGroupId());
		
		if (hometasks.isEmpty())
			resp.getWriter().println("There is no hometask added yet.");
		else {
			for (Hometask hometask : hometasks) {
				
				String htHeader = "<h3>Hometask" + hometask.getHometaskNum() +": </h3>";
				
				StudentHometaskCard checkCard = null;
				for (StudentHometaskCard card : cards) {   //Check that card exists
					if (card.getHtId() == hometask.getKey().getId())
						checkCard = card;
				}
				
				if (checkCard == null) {
					checkCard = new StudentHometaskCard();
					checkCard.setHtId(hometask.getKey().getId());
					checkCard.setStudentId(id);
					checkCard.save();
				}
				
				if (checkCard.getProgress() == null || checkCard.getProgress().isEmpty()) {
					checkCard.resetProgress();
				}
				
				List<StudentTaskProgress> progressList = checkCard.getProgress();
				
				String tableHeader = "<table border = 1 width = 200>"
						 + "<caption>" + htHeader + "</caption>"
						 + "<tr>";
				String tableRow = "<tr>";
				int i = 0;
				
				for (StudentTaskProgress progress : progressList) {
					tableHeader += "<th>" + (++i) + "</th>";
					tableRow += "<td>" + progress.getMark() + "</td>";
				}
				
				resp.getWriter().println(tableHeader + "</tr>" + tableRow + "</tr>");	
			} 
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Student student = new Student(req.getParameter("surname"), req.getParameter("first_name"), req.getParameter("last_name"));
		student.setBirthDate(Date.valueOf(req.getParameter("b_date")));
		student.setGroupId(new Long(req.getParameter("group")).longValue());
		student.save();
		resp.sendRedirect("/hometasker");
	}
}
