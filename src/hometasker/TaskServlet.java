package hometasker;

import hometasker.data.Task;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
public class TaskServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Task task = new Task();
		
		String number = req.getParameter("task_num");
		task.setTaskNum(new Integer(number).intValue());
		
		String ht_id = req.getParameter("ht_id");
		task.setHometaskId(KeyFactory.stringToKey(ht_id).getId());
		
		Text text = new Text(req.getParameter("task_text"));
		task.setText(text);
		
		String is_add = req.getParameter("is_add");
		task.setAdditional(is_add == null ? false : is_add.equals("on"));
		
		String is_fine = req.getParameter("is_fine");
		task.setFine(is_fine == null ? false : is_fine.equals("on"));
		
		task.save();
		
		resp.sendRedirect("/hometasker?ht_id=" + ht_id);
		
	}
}
