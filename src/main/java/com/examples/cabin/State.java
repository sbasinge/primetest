package com.examples.cabin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;


public enum State {
	OH("Ohio"),
	PA("Pennsylvania");
	
	private String name;
	
	private State(String name) {
		this.setName(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
//	@Override
//	public String toString() {
//		return new ToStringBuilder(this).appendSuper(super.toString())
//				.append("name", this.getName())
//				.toString();
//	}

}
