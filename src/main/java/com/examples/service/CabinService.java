package com.examples.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.examples.cabin.entity.Cabin;

@Stateless
@Named
@Path(value = "/cabins")
public class CabinService implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(CabinService.class);

	@PersistenceContext
    EntityManager db; 

	private List<Cabin> cabins = new ArrayList<Cabin>();

	@PostConstruct
	public void init() {
		populateAllCabins();
		calculateMinAndMaxPrices();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "/list")	
	public List<Cabin> getAllCabins() {
		log.info("GetCabins returning {} cabins.",cabins.size());
		for (Cabin cabin : cabins) {
			log.trace("Cabin {} has {} reviews",cabin.getName(),cabin.getReviews().size());  //eager fetch?
			log.trace("Cabin {} has {} bedrooms",cabin.getName(),cabin.getBedrooms().size());  //eager fetch?
		}
		return cabins;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "/hello")	
	public String sayHello() {
		return "Hello";
	}

	@SuppressWarnings("unchecked")
	public void populateAllCabins() {
		log.info("Populating all cabins");
		cabins = db.createNamedQuery("findAllCabins").getResultList();
		//calc high and low prices
	}
	
	private void calculateMinAndMaxPrices() {
		double minPrice = Double.MAX_VALUE;
		double maxPrice = 0;
		for (Cabin cabin : cabins) {
			if (cabin.getNightlyRate() < minPrice) {
				minPrice = cabin.getNightlyRate();
			}
			if (cabin.getNightlyRate() > maxPrice) {
				maxPrice = cabin.getNightlyRate();
			}
		}
		double startingRange = minPrice;
		double endingRange = maxPrice;
	}

}
