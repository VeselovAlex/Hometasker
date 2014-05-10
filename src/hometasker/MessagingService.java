package hometasker;

import hometasker.data.Group;
import hometasker.data.Student;
import hometasker.data.Teacher;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.appengine.api.datastore.KeyFactory;


public class MessagingService {

	public static final void sendNewHometaskMsg(long groupId) {
		
		Group group = Group.get(KeyFactory.createKey("Group", groupId));
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		List<Student> students = Student.getByGroup(groupId);
		if (students.isEmpty())
			return;
		
		try {
			Message msg = new MimeMessage(session);
			Teacher teacher = Teacher.get(KeyFactory.createKey("Teacher", group.getTeacherId()));
			msg.setFrom(new InternetAddress(teacher.getEmail(), "Teacher"));
			
			for (Student student : students) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(student.getEmail()));
			}
			msg.setSubject("New Hometask!");
			msg.setText("You have some new hometasks, go to your page to watch it");
			Transport.send(msg);
		} catch (MessagingException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
