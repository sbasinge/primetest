package com.examples.greeting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;



@ApplicationScoped
@Named
public class GreetingBean implements Serializable
{
   Greeting greeting = new Greeting();
   List<Greeting> greetings = new ArrayList<Greeting>();

   @Inject
   @Added
   Event<Greeting> greetingAddedEvent;

   @Inject
   GreetingArchiver greetingArchiver;
   
   @PostConstruct
   public void init()
   {
      greetings = greetingArchiver.loadGreetings();
   }

   public void addGreeting()
   {
      greetings.add(greeting);
      greetingAddedEvent.fire(greeting);
      greeting = new Greeting();
//      PushRenderer.render("greetings");
   }

   public Greeting getGreeting()
   {
      return greeting;
   }

   public void setGreeting(Greeting greeting)
   {
      this.greeting = greeting;
   }

   public List<Greeting> getGreetings()
   {
//      PushRenderer.addCurrentSession("greetings");
      return greetings;
   }

   public void setGreetings(List<Greeting> greetings)
   {
      this.greetings = greetings;
   }
}