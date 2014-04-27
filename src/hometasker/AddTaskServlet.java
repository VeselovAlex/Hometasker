package hometasker;

import hometasker.data.Task;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
public class AddTaskServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/html");
		String taskHeader = "<h1>Insert new task</h1><br><br>";
		String taskFormHTML = "<form action = \"/addtask\" method = \"post\">"
						+ "Number: <input type = \"text\" name = \"task_num\" value  = 1><br>"
						+ "Text: <input type = \"text\" name = \"task_text\"><br>"
						+ "Additional <input type = \"checkbox\" name = \"is_add\">"
						+ "Fine <input type = \"checkbox\" name = \"is_fine\"><br>"
						+ "<input type = \"hidden\" name = \"htId\" value = " + req.getParameter("htId") +">"
						+ "<input type = \"submit\" value = \"Add\">"
						+ "<input type = \"reset\" value = \"Clear\">"
						+ "</form>";
		resp.getWriter().println(taskHeader + taskFormHTML);		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Task task = new Task();
		
		String number = req.getParameter("task_num");
		task.setTaskNum(new Integer(number).intValue());
		
		String ht_id = req.getParameter("htId");
		task.setHometaskId(new Long(ht_id).longValue());
		
		Text text = new Text(req.getParameter("task_text"));
		task.setText(text);
		
		String is_add = req.getParameter("is_add");
		task.setAdditional(is_add == null ? false : is_add.equals("on"));
		
		String is_fine = req.getParameter("is_fine");
		task.setFine(is_fine == null ? false : is_fine.equals("on"));
		
		task.save();
		
		resp.sendRedirect("/hometask?htId=" + ht_id);
		
	}
}
