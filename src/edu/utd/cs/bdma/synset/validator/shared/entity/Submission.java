package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Submission implements Serializable{

	@Id Long id;
	
	@Index Long cameoId;
	
	@Index Long wordId;
	
	@Index Long userId;
	
	@Index Long adminId;
	
	@Index Date time;
	
	private String comment;

	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Long getId() {
		return id;
	}
	
	


	public Date getTime() {
		return time;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
	
	public Long getCameoId() {
		return cameoId;
	}

	public void setCameoId(Long cameoId) {
		this.cameoId = cameoId;
	}

	public Long getWordId() {
		return wordId;
	}

	public void setWordId(Long wordId) {
		this.wordId = wordId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
	
	public Submission() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null) return false;
		if (obj instanceof Submission){
			Submission other = (Submission) obj;
			return cameoId.equals(other.cameoId)
					&& wordId.equals(other.wordId)
					&& userId.equals(other.userId);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
          Long temp = wordId+cameoId+userId;
          return temp.hashCode();
	}
	
	
	
}
