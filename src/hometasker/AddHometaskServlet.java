package hometasker;

import hometasker.data.Hometask;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class AddHometaskServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/html");
		String hometaskHeader = "<h1>Insert new hometask</h1><br><br>";
		String hometaskFormHTML = "<form action = \"/addhometask\" method = \"post\">"
						+ "Number: <input type = \"text\" name = \"hometask_num\" value  = 1><br>"
						+ "Subject: <input type = \"text\" name = \"subj\"><br>"
						+ "Start:  <input type = \"text\" name = \"start_date\"><br>"
						+ "End:    <input type = \"text\" name = \"end_date\"><br>"
						+ "<input type = \"hidden\" name = \"grId\" value = " + req.getParameter("grId") +">"
						+ "<input type = \"submit\" value = \"Add\">"
						+ "<input type = \"reset\" value = \"Clear\">"
						+ "</form>";
		resp.getWriter().println(hometaskHeader + hometaskFormHTML);		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Hometask hometask = new Hometask();
		
		hometask.setHometaskNum(new Integer(req.getParameter("hometask_num")).intValue());
		
		hometask.setSubject(req.getParameter("subj"));
		Date start_date = Date.valueOf(req.getParameter("start_date"));
		hometask.setStartDate(start_date);
		
		Date end_date = Date.valueOf(req.getParameter("end_date"));
		hometask.setEndDate(end_date);
		
		long grId = new Long(req.getParameter("grId")).longValue();
		hometask.setGroupId(grId);
		
		hometask.save();
		
		resp.sendRedirect("/group?grId=" + grId);
		
	}
}
