package com.examples.cabin;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.examples.cabin.entity.Review;

public class ReviewBean extends AbstractPageBean {
	Logger log = LoggerFactory.getLogger(RentalTermsBean.class);

	private Review review;
	
	@PostConstruct
	public void init() {
		review = new Review();
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

}
