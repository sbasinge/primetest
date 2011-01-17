package com.examples.cabin.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.examples.cabin.BedroomType;

@Entity
public class Bedroom extends AbstractEntity {
	@Id
	@GeneratedValue
	int id;

	BedroomType type;
	
}
