package edu.utd.cs.bdma.synset.validator.shared.entity;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class SynsetStatistics implements Serializable{
	
	@Id Long id;
	@Index Long cameoId;
	//@Index Long wordId;
	@Index Long synsetId;
	
	int countCorrect;
	int countInCorrect;
	int countAmbiguous;
	
	
	
	public Long getCameoId() {
		return cameoId;
	}
	public void setCameoId(Long camoeId) {
		this.cameoId = camoeId;
	}
//	public Long getWordId() {
//		return wordId;
//	}
//	public void setWordId(Long wordId) {
//		this.wordId = wordId;
//	}
	public Long getSynsetId() {
		return synsetId;
	}
	public void setSynsetId(Long synsetId) {
		this.synsetId = synsetId;
	}
	public int getCountCorrect() {
		return countCorrect;
	}
	public void setCountCorrect(int countCorrect) {
		this.countCorrect = countCorrect;
	}
	public int getCountInCorrect() {
		return countInCorrect;
	}
	public void setCountInCorrect(int countInCorrect) {
		this.countInCorrect = countInCorrect;
	}
	public int getCountAmbiguous() {
		return countAmbiguous;
	}
	public void setCountAmbiguous(int countAmbiguous) {
		this.countAmbiguous = countAmbiguous;
	}
	public Long getId() {
		return id;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ""+synsetId+" "+cameoId+" "+countCorrect+" "+countInCorrect+" "+countAmbiguous;
	}
	

}
