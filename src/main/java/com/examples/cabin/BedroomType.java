package com.examples.cabin;

public enum BedroomType {
	QN("Queen"),
	KN("King"),
	DB("Double"),
	BN("Bunkbed");
	
	private String name;

	private BedroomType(String name) {
		this.setName(name);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
