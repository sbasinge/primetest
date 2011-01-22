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
//	@Id
//	@GeneratedValue
//	int id;

	LatLng latLng;
	
//	@OneToOne(mappedBy="location", cascade=CascadeType.ALL)
//	Address address;
	
	@Temporal(TemporalType.DATE)
	Date lastGeoLookup;

	public LatLng getLatLng() {
		return latLng;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	public Date getLastGeoLookup() {
		return lastGeoLookup;
	}

	public void setLastGeoLookup(Date lastGeoLookup) {
		this.lastGeoLookup = lastGeoLookup;
	}
	
}
