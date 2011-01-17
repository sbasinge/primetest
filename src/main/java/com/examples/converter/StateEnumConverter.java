package com.examples.converter;

import javax.faces.convert.FacesConverter;

import com.examples.cabin.State;

@FacesConverter(forClass=State.class, value="StateEnumConverter")
public class StateEnumConverter extends EnumConverter {
	public StateEnumConverter() {
		super();
	}
}
