package com.examples.greeting;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

@ApplicationScoped
public class GreetingArchiver
{
   @PersistenceContext
   EntityManager db;

   @Inject
   UserTransaction userTransaction;

   public void saveGreeting(@Observes @Added Greeting greeting)
   {
      try
      {
         userTransaction.begin();
         db.joinTransaction();
         db.persist(greeting);
         db.flush();
         userTransaction.commit();
      }
      catch (Exception e)
      {
         e.printStackTrace();
         // The recommended way of dealing with exceptions, right?
      }
   }

   public List<Greeting> loadGreetings()
   {
      return db.createQuery("select g from Greeting g").getResultList();
   }

}