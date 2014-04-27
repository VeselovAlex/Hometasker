package hometasker.data;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import com.google.appengine.api.datastore.Link;

@Entity(name = "Progress")
@Embeddable
public class StudentTaskProgress {
	private long taskId;
	private long cardId;
	private float mark;
	@Basic
	private Link taskFileUrl;
	
	public long getTaskId() {
		return taskId;
	}
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	
	public long getCardId() {
		return cardId;
	}
	public void setCardId(long cardId) {
		this.cardId = cardId;
	}
	public float getMark() {
		return mark;
	}
	public void setMark(float mark) {
		this.mark = mark;
	}
	
	public Link getTaskFileUrl() {
		return taskFileUrl;
	}
	
	public void setTaskFileUrl(Link taskFileUrl) {
		this.taskFileUrl = taskFileUrl;
	}
	
}
