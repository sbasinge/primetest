package com.examples.cabin.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class RentalTerms extends AbstractEntity {
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	int id;
	
	Integer maxOccupants;
	BigDecimal nightlyRental;
	BigDecimal additionalPerPerson;
	Integer basedOn;
	BigDecimal weeklyRental;
	
	public RentalTerms() {
	}
	
	public RentalTerms(Integer maxOccupants, BigDecimal nightlyRental, BigDecimal additionalPerPerson, Integer basedOn, BigDecimal weeklyRental) {
		this.maxOccupants = maxOccupants;
		this.nightlyRental = nightlyRental;
		this.additionalPerPerson = additionalPerPerson;
		this.basedOn = basedOn;
		this.weeklyRental = weeklyRental;
	}
	
	public BigDecimal getWeeklyRental() {
		return weeklyRental;
	}
	public void setWeeklyRental(BigDecimal weeklyRental) {
		this.weeklyRental = weeklyRental;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public BigDecimal getNightlyRental() {
		return nightlyRental;
	}
	public void setNightlyRental(BigDecimal nightlyRental) {
		this.nightlyRental = nightlyRental;
	}
	public BigDecimal getAdditionalPerPerson() {
		return additionalPerPerson;
	}
	public void setAdditionalPerPerson(BigDecimal additionalPerPerson) {
		this.additionalPerPerson = additionalPerPerson;
	}
	public Integer getBasedOn() {
		return basedOn;
	}
	public void setBasedOn(Integer basedOn) {
		this.basedOn = basedOn;
	}
	public Integer getMaxOccupants() {
		return maxOccupants;
	}
	public void setMaxOccupants(Integer maxOccupants) {
		this.maxOccupants = maxOccupants;
	}
	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("id", this.getId()).append("basedOn", this.getBasedOn())
				.append("nightlyRental", this.getNightlyRental())
				.toString();
	}

}
