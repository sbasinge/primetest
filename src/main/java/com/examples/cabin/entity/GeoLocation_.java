package com.examples.cabin.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-02-10T06:26:14.610-0500")
@StaticMetamodel(GeoLocation.class)
public class GeoLocation_ {
	public static volatile SingularAttribute<GeoLocation, Double> lat;
	public static volatile SingularAttribute<GeoLocation, Double> lng;
	public static volatile SingularAttribute<GeoLocation, Date> lastGeoLookup;
}
