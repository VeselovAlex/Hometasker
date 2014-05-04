package hometasker;

import hometasker.data.HometaskerUser;
import hometasker.data.HometaskerUser.UserType;
import hometasker.data.HometaskerUserService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		resp.getWriter().println("<h1>Log in:</h1><br>");
		HometaskerUserService userService = new HometaskerUserService();
		if (!userService.getUserService().isUserLoggedIn()) {
			resp.getWriter().println("You need to <a href = \"" + userService.getUserService().createLoginURL("/login") + "\"> Sign in </a>");
		} else {
			HometaskerUser user = userService.getCurrentUser();
			resp.getWriter().println("Hello, " + user.getUser().getNickname() + " You've been logged in as " + user.getUserType().toString() + "<br>"); 
			
			if (user.getUserType() == UserType.GUEST) {
					resp.getWriter().println("You can register as a <a href = \"/register?type=student\">student</a>"
											+ " or a <a href = \"/register?type=teacher\">teacher</a> now or <a href = \"/groups\">continue as a GUEST</a><br>");
			}
			
			if (user.getUserType() == UserType.TEACHER) {
				resp.getWriter().println("You can watch your teacher's page <a href = \"/teacher?id=" + user.getPerson().getKey().getId() + "\">here</a>");
			}
			
			if (user.getUserType() == UserType.STUDENT) {
				resp.getWriter().println("You can watch your student's page <a href = \"/student?id=" + user.getPerson().getKey().getId() + "\">here</a>");
			}
			
			resp.getWriter().println("You can also <a href = \"" + userService.getUserService().createLogoutURL("/") + "\">Sign out</a> here");
		}
	}
}
