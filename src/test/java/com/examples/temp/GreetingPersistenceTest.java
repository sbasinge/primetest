package com.examples.temp;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.examples.greeting.Greeting;

@RunWith(Arquillian.class)
public class GreetingPersistenceTest {
		private static Logger log = LoggerFactory.getLogger(GreetingPersistenceTest.class);
	
	   @PersistenceContext
	   EntityManager em;
	   
	   @Inject
	   UserTransaction utx;

	   @Deployment
	   public static Archive<?> createDeployment()
	   {
	      return ShrinkWrap.create(WebArchive.class, "test.war")
	            .addPackage(Greeting.class.getPackage())
	            .addManifestResource("test-persistence.xml", "persistence.xml")
	            .addWebResource(EmptyAsset.INSTANCE, "beans.xml");
	   }

	   private static final String[] GREETING_TEXT =
	   {
	      "Super Mario Brothers",
	      "Mario Kart",
	      "F-Zero"
	   };

	   public void insertSampleRecords() throws Exception
	   {
	      utx.begin();
	      em.joinTransaction();
	    
	      log.info("Clearing the database...");
	      em.createQuery("delete from Greeting").executeUpdate();
	    
	      log.info("Inserting records...");
	      for (String title : GREETING_TEXT)
	      {
	         Greeting greet = new Greeting(title);
	         em.persist(greet);
	      }
	    
	      utx.commit();
	   }

	   @Test
	   public void should_be_able_to_select_games_using_jpql() throws Exception
	   {
	      insertSampleRecords();
	      
	      utx.begin();
	      em.joinTransaction();
	    
	      log.info("Selecting (using JPQL)...");
	      List<Greeting> greetings =
	         em.createQuery("select g from Greeting g order by g.id", Greeting.class)
	         .getResultList();
	      log.info("Found {} greetings (using JPQL)",greetings.size());
	      assertEquals(GREETING_TEXT.length, greetings.size());
	    
	      for (int i = 0; i < GREETING_TEXT.length; i++) {
	         assertEquals(GREETING_TEXT[i], greetings.get(i).getText());
	         log.info("{}",greetings.get(i).getText());
	      }
	      
	      utx.commit();
	   }
	   
	   @Test
	   public void should_be_able_to_select_games_using_criteria_api() throws Exception
	   {
	      insertSampleRecords();
	      
	      utx.begin();
	      em.joinTransaction();
	    
	      CriteriaBuilder builder = em.getCriteriaBuilder();
	      CriteriaQuery<Greeting> criteria = builder.createQuery(Greeting.class);
	      // FROM clause
	      Root<Greeting> greeting = criteria.from(Greeting.class);
	      // SELECT clause
	      criteria.select(greeting);
	      // ORDER BY clause
	      criteria.orderBy(builder.asc(greeting.get("id")));
	      // No WHERE clause, select all
	    
	      log.info("Selecting (using Criteria)...");
	      List<Greeting> greetings = em.createQuery(criteria).getResultList();
	      log.info("Found " + greetings.size() + " greetings (using Criteria)");
	      assertEquals(GREETING_TEXT.length, greetings.size());
	    
	      for (int i = 0; i < GREETING_TEXT.length; i++) {
	         assertEquals(GREETING_TEXT[i], greetings.get(i).getText());
	         log.info("{}",greetings.get(i).getText());
	      }
	    
	      utx.commit();
	   }

}
