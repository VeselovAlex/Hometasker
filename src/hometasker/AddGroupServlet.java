package hometasker;

import hometasker.data.Group;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class AddGroupServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		resp.getWriter().println("<a href = \"groups\"> Back to groups </a><br>");
		String groupHeader = "<h1>Add group</h1><br><h2>Insert new group data:</h2><br>";
		String groupFormHTML = "<form action = \"/addgroup\" method = \"post\">"
						+ "Number: <input type = \"text\" name = \"group_num\"><br>"
						+ "Subgroup:  <input type = \"text\" name = \"subgroup\"><br>"
						+ "<input type = \"submit\" value = \"Add\">"
						+ "<input type = \"reset\" value = \"Clear\">"
						+ "</form>";
		resp.getWriter().println(groupHeader + groupFormHTML);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Group group = new Group();
		
		String num = req.getParameter("group_num");
		if (num == null)
			resp.sendRedirect("/addgroup");
		else
			try {
				group.setGroupNum(new Integer(num).intValue());
			} catch (NumberFormatException exc) {
				System.out.println("Exception in num");
				resp.sendRedirect("/addgroup");
			}
		
		String snum = req.getParameter("subgroup");
		if (snum == null)
			resp.sendRedirect("/addgroup");
		else
			try {
				group.setSubgroupNum(new Integer(snum).intValue());
			} catch (NumberFormatException exc) {
				System.out.println("Exception in snum");
				resp.sendRedirect("/addgroup");
			}
		
		group.setTeacherId(1L);
		group.save();
		resp.sendRedirect("/groups");
	}
}
