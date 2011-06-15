package com.examples.cabin.entity;

import javax.persistence.Entity;
import com.examples.cabin.BedroomType;

@Entity
public class Bedroom extends AbstractEntity {
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	int id;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BedroomType type;
	
}
