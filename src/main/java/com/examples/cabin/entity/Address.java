package com.examples.cabin.entity;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.primefaces.model.map.LatLng;

import com.examples.cabin.State;

@Entity
public class Address extends AbstractEntity {
	@Id
	@GeneratedValue
	int id;

	String addressLine1;
	String addressLine2;
	String city;
	String zipCode;

	@Enumerated(EnumType.STRING)
	State state;
	
	String zip;
	
	@OneToOne(cascade = CascadeType.ALL)
	GeoLocation location;

	public Address() {
	}

	public Address(String addressLine1, String city, State state) {
		this.addressLine1 = addressLine1;
		this.city = city;
		this.state = state;
	}

	public Address(String addressLine1, String city, State state, String zipCode, double lat, double lng) {
		this.addressLine1 = addressLine1;
		this.city = city;
		this.state = state;
		this.location = new GeoLocation();
		this.location.latLng = new LatLng(lat, lng);
		this.location.lastGeoLookup = Calendar.getInstance().getTime();
	}

	public Address(String addressLine1, String city, State state, LatLng latLng) {
		this.addressLine1 = addressLine1;
		this.city = city;
		this.state = state;
		this.location = new GeoLocation();
		this.location.latLng = latLng;
		this.location.lastGeoLookup = Calendar.getInstance().getTime();
	}

	public Address(String addressLine1, String city, State state, String zip, LatLng latLng) {
		this.addressLine1 = addressLine1;
		this.city = city;
		this.state = state;
		this.location = new GeoLocation();
		this.location.latLng = latLng;
		this.location.lastGeoLookup = Calendar.getInstance().getTime();
		this.zip = zip;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("id", this.getId())
				.append("addressLine1", this.getAddressLine1())
				.append("addressLine2", this.getAddressLine2())
				.append("city", this.getCity())
				.append("state", this.getState())
				.toString();
	}

	public GeoLocation getLocation() {
		return location;
	}

	public void setLocation(GeoLocation location) {
		this.location = location;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}
