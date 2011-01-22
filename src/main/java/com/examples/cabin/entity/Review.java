package com.examples.cabin.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Review extends AbstractEntity {
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	int id;

	public Review() {
		this.reviewDate = Calendar.getInstance().getTime();
	}

	public Review(String comment, int rating) {
		this.comments = comment;
		this.rating = rating;
		this.reviewDate = Calendar.getInstance().getTime();
	}

	@Temporal(TemporalType.DATE)
	Date reviewDate;

	String comments;
	int rating;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
}
