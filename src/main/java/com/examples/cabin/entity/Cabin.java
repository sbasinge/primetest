package com.examples.cabin.entity;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@NamedQueries(value={
	@NamedQuery(name = "findMatchingCabins", query = "select c from Cabin c where c.address.state = :state"),
	@NamedQuery(name = "findAllCabins", query = "select c from Cabin c")
})
public class Cabin extends AbstractEntity {
	@Transient
	Logger log = LoggerFactory.getLogger(Cabin.class);

//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	int id;

	String name;
	String url;
	String imageUrl;
	boolean hotTub;
	boolean firePit;
	boolean firePlace;
	String phoneNumber;
	
	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="ADDRESS_ID")
	Address address;

	@OneToOne(cascade = CascadeType.ALL)
	RentalTerms rentalTerms;

	@OneToMany(cascade = CascadeType.ALL)
	List<Review> reviews;

	@OneToMany(cascade = CascadeType.ALL)
	List<Bedroom> bedrooms;

	public Cabin() {
//		address = new Address();
//		rentalTerms = new RentalTerms();
//		reviews = new ArrayList<Review>();
	}

	public Cabin(String name, Address address,RentalTerms rentalTerms, Review review ) {
		this.name = name;
		this.address = address;
		this.rentalTerms = rentalTerms;
		if (review != null)
			this.addReview(review);
	}

	public RentalTerms getRentalTerms() {
		return rentalTerms;
	}

	public void setRentalTerms(RentalTerms rentalTerms) {
		this.rentalTerms = rentalTerms;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getEscapedName() {
		return name.replaceAll("'", "");
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("id", this.getId()).append("name", this.getName())
				.append("address", this.getAddress())
				.append("rentalTerms", this.getRentalTerms())
				.toString();
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Double getAverageRating() {
		Double retval = 0d;
		double totalRating = 0;
		double totalReviews = 0;
		for (Review review : getReviews()) {
			log.info("reviewing rating comments {} and rating {}",review.getComments(),review.getRating());
			totalRating += review.getRating();
			totalReviews ++;
		}
		retval = totalReviews > 0 ? (totalRating / totalReviews) : 0;
		log.info("getAverageRating for {} returning {}",name, retval);
		return retval;
	}
	
	public void addReview(Review review) {
		if (getReviews()==null) {
			setReviews(new ArrayList<Review>());
		}
		getReviews().add(review);
	}

	public void addBedroom(Bedroom bedroom) {
		if (getBedrooms()==null) {
			setBedrooms(new ArrayList<Bedroom>());
		}
		getBedrooms().add(bedroom);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isHotTub() {
		return hotTub;
	}

	public void setHotTub(boolean hotTub) {
		this.hotTub = hotTub;
	}

	public boolean isFirePit() {
		return firePit;
	}

	public void setFirePit(boolean firePit) {
		this.firePit = firePit;
	}

	public List<Bedroom> getBedrooms() {
		return bedrooms;
	}

	public void setBedrooms(List<Bedroom> bedrooms) {
		this.bedrooms = bedrooms;
	}

	public boolean isFirePlace() {
		return firePlace;
	}

	public void setFirePlace(boolean firePlace) {
		this.firePlace = firePlace;
	}
	
	/**
	 * Convenience method
	 * @return
	 */
	public double getNightlyRate() {
		return getRentalTerms() != null ? getRentalTerms().getNightlyRental().doubleValue() : 0;
	}
	
	public Review getLastReview() {
		Review retVal = new Review();
		if (reviews != null && reviews.size()>0) {
			retVal = reviews.get(reviews.size()-1);
		}
		return retVal;
	}
}
