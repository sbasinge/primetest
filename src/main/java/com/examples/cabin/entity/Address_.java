package com.examples.cabin.entity;

import com.examples.cabin.State;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-02-10T06:26:14.610-0500")
@StaticMetamodel(Address.class)
public class Address_ extends AbstractEntity_ {
	public static volatile SingularAttribute<Address, String> addressLine1;
	public static volatile SingularAttribute<Address, String> addressLine2;
	public static volatile SingularAttribute<Address, String> city;
	public static volatile SingularAttribute<Address, String> zipCode;
	public static volatile SingularAttribute<Address, State> state;
	public static volatile SingularAttribute<Address, GeoLocation> geoLocation;
}
