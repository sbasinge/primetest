package com.examples.cabin.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-02-10T06:26:14.610-0500")
@StaticMetamodel(RentalTerms.class)
public class RentalTerms_ extends AbstractEntity_ {
	public static volatile SingularAttribute<RentalTerms, Integer> maxOccupants;
	public static volatile SingularAttribute<RentalTerms, BigDecimal> nightlyRental;
	public static volatile SingularAttribute<RentalTerms, BigDecimal> additionalPerPerson;
	public static volatile SingularAttribute<RentalTerms, Integer> basedOn;
	public static volatile SingularAttribute<RentalTerms, BigDecimal> weeklyRental;
}
