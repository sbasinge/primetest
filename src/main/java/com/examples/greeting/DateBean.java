package com.examples.greeting;

import java.util.Date;

import javax.enterprise.inject.Model;

@Model
public class DateBean {
		private Date date;
		//Getter and Setter

		public void setDate(Date date) {
			this.date = date;
		}

		public Date getDate() {
			return date;
		}
}
