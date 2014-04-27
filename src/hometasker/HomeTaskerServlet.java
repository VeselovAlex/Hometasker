package hometasker;

import hometasker.data.Hometask;
import hometasker.data.Task;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class HomeTaskerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		String hometaskHeader = "<h1>Insert new hometask</h1><br><br>";
		String hometaskFormHTML = "<form action = \"/nhometask\" method = \"post\">"
					//	+ "<input type =\"hidden\" name = \"ht_id\" value = 1>"
						+ "Number: <input type = \"text\" name = \"hometask_num\" value  = 1><br>"
						+ "Start:  <input type = \"text\" name = \"start_date\"><br>"
						+ "End:    <input type = \"text\" name = \"end_date\"><br>"
						+ "<input type = \"submit\" value = \"Add\">"
						+ "<input type = \"reset\" value = \"Clear\">"
						+ "</form>";
		resp.getWriter().println(hometaskHeader + hometaskFormHTML);
		//Fix later
		String ht_id = req.getParameter("ht_id");
		resp.getWriter().println("Ht_Id : " + ht_id);
		
		String tasksStr = "<h2>Tasks in current hometask:</h2><br>";
		resp.getWriter().println(tasksStr);
		if ((ht_id != null) && !ht_id.equals("0"))
		{
			Hometask ht = Hometask.get(KeyFactory.stringToKey(ht_id));
			List <Task> tasks = ht.getAllTasks();
			for (Task task : tasks)
			{
				tasksStr = String.valueOf(task.getTaskNum()) + ". " + task.getText().toString() + "<br>";
				resp.getWriter().println(tasksStr);
			}
		}
		
		
		tasksStr = "<h2>All hometasks:</h2><br>";
		resp.getWriter().println(tasksStr);
		List<Hometask> tasks = Hometask.getAll();
		for (Hometask task : tasks)
		{
			tasksStr = String.valueOf(task.getHometaskNum()) + ". " + task.getStartDate() + "<br>";
			resp.getWriter().println(tasksStr);
		}
		
		
		String taskHeader = "<h1>Insert new task</h1><br><br>";
		String taskFormHTML = "<form action = \"/task\" method = \"post\">"
							+ "<input type =\"hidden\" name = \"ht_id\" value = "+ ht_id + " >"
							+ "Number: <input type = \"text\" name = \"task_num\" value  = 1><br>"
							+ "Text: <input type = \"text\" name = \"task_text\" value  = \"Text\"><br>"
							+ "Additional <input type = \"checkbox\" name = \"is_add\">"
							+ "Fine <input type = \"checkbox\" name = \"is_fine\"><br>"
							+ "<input type = \"submit\" value = \"Add\">"
							+ "<input type = \"reset\" value = \"Clear\">"
							+ "</form>";
		resp.getWriter().println(taskHeader + taskFormHTML);
		
		
		//---------Student-------------------
		String studHeader = "<h1>Insert new student data</h1><br><br>";
		String studFormHTML = "<form action = \"/student\" method = \"post\">"
							+ "Surname:     <input type = \"text\" name = \"surname\" value  = \"Surname\"><br>"
							+ "First name:  <input type = \"text\" name = \"first_name\" value  = \"First name\"><br>"
							+ "Last name: <input type = \"text\" name = \"last_name\" value  = \"Last name\"><br>"
							+ "Birth date:  <input type = \"text\" name = \"b_date\"><br>"
							+ "<input type = \"hidden\" name = \"group\" value = 2431>"
							+ "<input type = \"submit\" value = \"Add\">"
							+ "<input type = \"reset\" value = \"Clear\">"
							+ "</form>";
		resp.getWriter().println(studHeader + studFormHTML);

	}
}
