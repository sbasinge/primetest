package com.examples.cabin.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.examples.cabin.BedroomType;

@Entity
public class Bedroom extends AbstractEntity {
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	int id;

	BedroomType type;
	
}
