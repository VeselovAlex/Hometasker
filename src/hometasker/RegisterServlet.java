package hometasker;

import hometasker.data.Group;
import hometasker.data.Hometask;
import hometasker.data.HometaskerUserService;
import hometasker.data.Student;
import hometasker.data.StudentHometaskCard;
import hometasker.data.Task;
import hometasker.data.Teacher;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		resp.getWriter().println("<h1>Register:</h1><br>");
		String type = req.getParameter("type");
		String header;
		String personHTML = "<form action = \"/register\" method = \"post\">"
							+ "Surname:     <input type = \"text\" name = \"surname\" value  = \"Surname\"><br>"
							+ "First name:  <input type = \"text\" name = \"first_name\" value  = \"First name\"><br>"
							+ "Last name: <input type = \"text\" name = \"last_name\" value  = \"Last name\"><br>"
							+ "Date format yyyy-mm-dd<br>"
							+ "Birth date:  <input type = \"text\" name = \"b_date\"><br>";
		
		String formFooter = "<input type = \"submit\" value = \"Add\">"
							+ "<input type = \"reset\" value = \"Clear\">"
							+ "</form>";
		
		if (type == null) {
			throw new IOException();
		} else if (type.equals("student")) {
			header = "<h1>Insert new student data</h1><br><br>";
			personHTML = personHTML
						+ "Group number: <input type = \"text\" name = \"group_num\"><br>"
						+ "Subgroup:  <input type = \"text\" name = \"subgroup\"><br>"
						+ "<input type = \"hidden\" name = \"type\" value = \"student\">"
						+ formFooter;
		} else if (type.equals("teacher")) {
			header = "<h1>Insert new teacher data</h1><br><br>";
			personHTML = personHTML
						+ "<input type = \"hidden\" name = \"type\" value = \"teacher\">"
						+ formFooter;
		}
		else
			throw new IOException();
		
		resp.getWriter().println(header + personHTML);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String sname = req.getParameter("surname");
		String fname = req.getParameter("first_name");
		String lname = req.getParameter("last_name");
		Date bDate = Date.valueOf(req.getParameter("b_date"));
		
		String type = req.getParameter("type");
		if (type == null) {
			throw new IOException();
		} else if (type.equals("student")) {
			int groupNum = new Integer(req.getParameter("group_num")).intValue();
			int subgroupNum = new Integer(req.getParameter("subgroup")).intValue();
			Group curGroup = null;
			List<Group> groups = Group.getByNumber(groupNum);
			if (groups == null || groups.isEmpty()) {
				resp.sendRedirect("/register?type=student&errCode=1");//group not found
			} else {
				for (Group group : groups) {
					if (group.getSubgroupNum() == subgroupNum)
						curGroup = group;
				}
				
				if (curGroup == null) {
					resp.sendRedirect("/register?type=student&errCode=1");//group not found
				} else {
					Student student = new Student();
					student.setBirthDate(bDate);
					student.setFirstName(fname);
					student.setLastName(lname);
					student.setSurname(sname);
					student.setNickname(new HometaskerUserService().getUserService().getCurrentUser().getNickname());
					student.setEmail(new HometaskerUserService().getUserService().getCurrentUser().getEmail());
					student.setGroupId(curGroup.getKey().getId());
					student.save();
					//Insert pages for home tasks
					List<Hometask> hometasks = Hometask.getByGroupId(curGroup.getKey().getId());
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
					resp.sendRedirect("/student?id=" + student.getKey().getId());
				}
			}
			
		} else if (type.equals("teacher")) {
			Teacher teacher = new Teacher();
			teacher.setBirthDate(bDate);
			teacher.setFirstName(fname);
			teacher.setLastName(lname);
			teacher.setSurname(sname);
			teacher.setNickname(new HometaskerUserService().getUserService().getCurrentUser().getNickname());
			teacher.setEmail(new HometaskerUserService().getUserService().getCurrentUser().getEmail());
			teacher.save();
			resp.sendRedirect("/teacher?id=" + teacher.getKey().getId());
		}
		else
			throw new IOException();
	}
}
