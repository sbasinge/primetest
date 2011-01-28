package com.examples.cabin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.xml.parsers.ParserConfigurationException;

import org.primefaces.model.map.LatLng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.examples.cabin.entity.Address;
import com.examples.cabin.entity.Cabin;
import com.examples.cabin.entity.RentalTerms;
import com.examples.cabin.entity.Review;

@Named
@ConversationScoped
public class TestDataGenerator extends AbstractPageBean {
	Logger log = LoggerFactory.getLogger(TestDataGenerator.class);

	@PersistenceContext
	EntityManager db;

	@Inject
	UserTransaction userTransaction;

	@Inject
	Conversation conversation;

	private List<String> rawTestData;
	private List<Cabin> testData = new ArrayList<Cabin>();
	
	@PostConstruct
	private void setupTestData() {
		Cabin cabin1 = new Cabin("Hemlock", new Address(null, "Hocking Hills", State.OH,"43152",39.464,-82.545) ,new RentalTerms(new Integer(12), new BigDecimal(279), new BigDecimal(15), new Integer(8), null),new Review("stayed in 2009",4));
		cabin1.setFirePit(true);
		cabin1.setHotTub(true);
		cabin1.setFirePlace(true);
		testData.add(cabin1);
		Cabin cabin2 = new Cabin("Ash Cave Cabins", new Address("25780 Liberty Hill Rd.", "So. Bloomingville", State.OH,"43152",39.398,-82.536),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2));
		cabin2.setHotTub(true);
		testData.add(cabin2);
		Cabin cabin3 = new Cabin("Heritage Lodge", new Address("Buena Vista", "Hocking Hills", State.OH,"43152",39.566,-82.593),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2));
		cabin3.setFirePit(true);
		testData.add(cabin3);
//		testData.add(new Cabin("Cedar Hill Lodge", new Address("123 Main St", "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
//		testData.add(new Cabin("Heritage Retreat", new Address("123 Main St", "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
//		testData.add(new Cabin("Timber Creek", new Address("123 Main St", "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
//		testData.add(new Cabin("Luc Lodge", new Address("123 Main St", "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
//		testData.add(new Cabin("Serenity", new Address("123 Main St", "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
//		testData.add(new Cabin("Hillside Lodge", new Address("123 Main St", "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
//		testData.add(new Cabin("Garleff Lodge", new Address("123 Main St", "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
//		testData.add(new Cabin("Black Ridge", new Address("123 Main St", "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
//		testData.add(new Cabin("Indian Mound Lodge", new Address("123 Main St", "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
//		testData.add(new Cabin("Autumn Ridge", new Address("123 Main St", "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
//		testData.add(new Cabin("Crockets run - William Travis", new Address("123 Main St", "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
//		testData.add(new Cabin("Westwind", new Address("123 Main St", "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
//		testData.add(new Cabin("Maple Forest Lodge", new Address("123 Main St", "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
//		testData.add(new Cabin("Hocking Hills Getaway", new Address("123 Main St", "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
//		testData.add(new Cabin("Dogwood", new Address("123 Main St", "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4), null),new Review("sample",2)));

		testData.add(new Cabin("Someplace in PA",new Address("123 Main St", "Reading", State.PA,"43152",40.0,-80.0),null,null));
	}
	
	public void loadTestData() {
		if (getNumberOfCabins()>0) {
			deleteExistingCabins();
		}
		
		for (Cabin cabin : testData) {
			loadCabin(cabin);
		}
		log.info("Success");
		addError("msgs", "Ok", "Test Data Added.");

	}

	private void deleteExistingCabins() {
		try {
			userTransaction.begin();
			db.joinTransaction();
			List<Cabin> results = db.createQuery("select c from Cabin c").getResultList();
			log.info("Results: {}",results.size());
			for (Cabin cabin : results ) {
				db.remove(cabin);
				db.flush();
			}
			userTransaction.commit();
		} catch (Exception e) {
			try {
				userTransaction.rollback();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error("Error loading cabin",e);
		} finally {
		}
		
	}

	private void loadCabin(Cabin cabin) {
		try {
			userTransaction.begin();
//			db.joinTransaction();
			db.persist(cabin);
//			db.flush();
			userTransaction.commit();
		} catch (Exception e) {
			log.error("Error loading cabin",e);
		}
		
	}

	
	private int getNumberOfCabins() {
		int retVal = 0;
		List<Cabin> results = db.createQuery("select c from Cabin c").getResultList();
		log.info("Results: {}",results.size());
		retVal = results.size();
		return retVal;
	}
	
}
