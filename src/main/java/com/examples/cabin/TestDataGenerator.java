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
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	EntityManager em;

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
		testData.add(new Cabin("Someplace in PA", new Address("123 Main St",
				"Reading", State.PA, "43152", 40.0, -80.0), null, null));
	}

	@Transactional(rollback=false)
	public void loadTestData() {
		
		if (getNumberOfCabins() > 0) {
			deleteExistingCabins();
		}

		for (Cabin cabin : testData) {
			em.persist(cabin);
		}
		log.info("Test Data Loaded.");
	}

	@Transactional
	public void deleteExistingCabins() {
		List<Cabin> results = em.createQuery("select c from Cabin c")
				.getResultList();
		log.info("{} Cabins about to be deleted ...", results.size());
		for (Cabin cabin : results) {
			em.remove(cabin);
		}
		log.info("All cabins deleted.");
	}

	private int getNumberOfCabins() {
		int retVal = 0;
		List<Cabin> results = em.createQuery("select c from Cabin c")
				.getResultList();
		log.info("Currently there are {} cabins.", results.size());
		retVal = results.size();
		return retVal;
	}

}
