package com.examples.cabin.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-02-10T06:26:14.610-0500")
@StaticMetamodel(Review.class)
public class Review_ extends AbstractEntity_ {
	public static volatile SingularAttribute<Review, Date> reviewDate;
	public static volatile SingularAttribute<Review, String> comments;
	public static volatile SingularAttribute<Review, Integer> rating;
}
