package hometasker;

public class HTMLTemplates {
	public static final String pageHeader() {
		return "<header>"
				+ "<ul>"
				+ "<li><a href = \"groups\"> Groups </a></li>"
				+ "</ul>"
				+ "</header>";
	}
	
	public static String createGroupViewURL(long id) {
		return "<a href = \"/group?grId=" + id + "\">View</a>";
	}
}
