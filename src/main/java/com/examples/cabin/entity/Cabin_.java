package com.examples.cabin.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-02-19T19:15:49.485-0500")
@StaticMetamodel(Cabin.class)
public class Cabin_ extends AbstractEntity_ {
	public static volatile SingularAttribute<Cabin, String> name;
	public static volatile SingularAttribute<Cabin, String> url;
	public static volatile SingularAttribute<Cabin, String> imageUrl;
	public static volatile SingularAttribute<Cabin, Boolean> hotTub;
	public static volatile SingularAttribute<Cabin, Boolean> firePit;
	public static volatile SingularAttribute<Cabin, Boolean> firePlace;
	public static volatile SingularAttribute<Cabin, String> phoneNumber;
	public static volatile SingularAttribute<Cabin, Address> address;
	public static volatile SingularAttribute<Cabin, RentalTerms> rentalTerms;
	public static volatile ListAttribute<Cabin, Review> reviews;
	public static volatile ListAttribute<Cabin, Bedroom> bedrooms;
}
