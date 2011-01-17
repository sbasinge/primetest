package com.examples.greeting;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Greeting
{
   @Id
   @GeneratedValue
   int id;

   String text;
   
   @Temporal(TIMESTAMP)
   Date created = new Date();

   public Greeting(String greet) {
	   text = greet;
   }

   public Greeting() {
   }

   public int getId()
   {
      return id;
   }

   public void setId(int id)
   {
      this.id = id;
   }

   public String getText()
   {
      return text;
   }

   public void setText(String text)
   {
      this.text = text;
   }
}
