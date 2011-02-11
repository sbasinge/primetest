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
import javax.persistence.Transient;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.xml.parsers.ParserConfigurationException;

import org.primefaces.model.map.LatLng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.examples.annotation.Transactional;
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

	public TestDataGenerator() {}
	
	@PostConstruct
	private void setupTestData() {
		Cabin cabin1 = new Cabin("Hemlock", new Address(null, "Hocking Hills",
				State.OH, "43152", 39.464, -82.545), new RentalTerms(
				new Integer(12), new BigDecimal(279), new BigDecimal(15),
				new Integer(8), null), new Review("stayed in 2009", 4));
		cabin1.setFirePit(true);
		cabin1.setHotTub(true);
		cabin1.setFirePlace(true);
		testData.add(cabin1);
		Cabin cabin2 = new Cabin("Ash Cave Cabins", new Address(
				"25780 Liberty Hill Rd.", "So. Bloomingville", State.OH,
				"43152", 39.398, -82.536), new RentalTerms(new Integer(10),
				new BigDecimal(100), new BigDecimal(10), new Integer(4), null),
				new Review("sample", 2));
		cabin2.setHotTub(true);
		testData.add(cabin2);
		Cabin cabin3 = new Cabin("Heritage Lodge", new Address("Buena Vista",
				"Hocking Hills", State.OH, "43152", 39.566, -82.593),
				new RentalTerms(new Integer(10), new BigDecimal(100),
						new BigDecimal(10), new Integer(4), null), new Review(
						"sample", 2));
		cabin3.setFirePit(true);
		testData.add(cabin3);
		// testData.add(new Cabin("Cedar Hill Lodge", new Address("123 Main St",
		// "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new
		// Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4),
		// null),new Review("sample",2)));
		// testData.add(new Cabin("Heritage Retreat", new Address("123 Main St",
		// "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new
		// Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4),
		// null),new Review("sample",2)));
		// testData.add(new Cabin("Timber Creek", new Address("123 Main St",
		// "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new
		// Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4),
		// null),new Review("sample",2)));
		// testData.add(new Cabin("Luc Lodge", new Address("123 Main St",
		// "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new
		// Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4),
		// null),new Review("sample",2)));
		// testData.add(new Cabin("Serenity", new Address("123 Main St",
		// "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new
		// Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4),
		// null),new Review("sample",2)));
		// testData.add(new Cabin("Hillside Lodge", new Address("123 Main St",
		// "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new
		// Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4),
		// null),new Review("sample",2)));
		// testData.add(new Cabin("Garleff Lodge", new Address("123 Main St",
		// "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new
		// Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4),
		// null),new Review("sample",2)));
		// testData.add(new Cabin("Black Ridge", new Address("123 Main St",
		// "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new
		// Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4),
		// null),new Review("sample",2)));
		// testData.add(new Cabin("Indian Mound Lodge", new
		// Address("123 Main St", "New Lexington", State.OH,new
		// LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100),
		// new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
		// testData.add(new Cabin("Autumn Ridge", new Address("123 Main St",
		// "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new
		// Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4),
		// null),new Review("sample",2)));
		// testData.add(new Cabin("Crockets run - William Travis", new
		// Address("123 Main St", "New Lexington", State.OH,new
		// LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100),
		// new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
		// testData.add(new Cabin("Westwind", new Address("123 Main St",
		// "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new
		// Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4),
		// null),new Review("sample",2)));
		// testData.add(new Cabin("Maple Forest Lodge", new
		// Address("123 Main St", "New Lexington", State.OH,new
		// LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100),
		// new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
		// testData.add(new Cabin("Hocking Hills Getaway", new
		// Address("123 Main St", "New Lexington", State.OH,new
		// LatLng(40,-82)),new RentalTerms(new Integer(10), new BigDecimal(100),
		// new BigDecimal(10), new Integer(4), null),new Review("sample",2)));
		// testData.add(new Cabin("Dogwood", new Address("123 Main St",
		// "New Lexington", State.OH,new LatLng(40,-82)),new RentalTerms(new
		// Integer(10), new BigDecimal(100), new BigDecimal(10), new Integer(4),
		// null),new Review("sample",2)));

		testData.add(new Cabin("Someplace in PA", new Address("123 Main St",
				"Reading", State.PA, "43152", 40.0, -80.0), null, null));
	}

	@Transactional(rollback=false)
	public void loadTestData() {
		
//		boolean transactionStarted = false;
//		try {
//			db.joinTransaction(); 
//		} catch (javax.persistence.TransactionRequiredException e) {
//			userTransaction.begin();
//			db.joinTransaction();
//			transactionStarted = true;
//		}
		if (getNumberOfCabins() > 0) {
			deleteExistingCabins();
		}

		for (Cabin cabin : testData) {
			db.persist(cabin);
		}
//		if (transactionStarted) {
//			userTransaction.commit();
//		}
		log.info("Test Data Loaded.");
		//addError("msgs", "Ok", "Test Data Added.");

	}

	@Transactional
	public void deleteExistingCabins() {
		List<Cabin> results = db.createQuery("select c from Cabin c")
				.getResultList();
		log.info("{} Cabins about to be deleted ...", results.size());
		for (Cabin cabin : results) {
			db.remove(cabin);
		}
		log.info("All cabins deleted.");
	}

	private int getNumberOfCabins() {
		int retVal = 0;
		List<Cabin> results = db.createQuery("select c from Cabin c")
				.getResultList();
		log.info("Currently there are {} cabins.", results.size());
		retVal = results.size();
		return retVal;
	}

}
