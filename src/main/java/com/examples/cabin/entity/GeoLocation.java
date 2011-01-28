package com.examples.cabin.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.primefaces.model.map.LatLng;

//@Entity
@Embeddable
public class GeoLocation implements Serializable {
	double lat;
	double lng;
	
	@Temporal(TemporalType.DATE)
	Date lastGeoLookup;

	public Date getLastGeoLookup() {
		return lastGeoLookup;
	}

	public void setLastGeoLookup(Date lastGeoLookup) {
		this.lastGeoLookup = lastGeoLookup;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public LatLng createLatLng() {
		return new LatLng(lat,lng);
	}
}
