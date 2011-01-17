package com.examples.cabin.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.primefaces.model.map.LatLng;

@Entity
public class GeoLocation extends AbstractEntity {
	@Id
	@GeneratedValue
	int id;

	LatLng latLng;
	
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
