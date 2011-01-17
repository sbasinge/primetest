package com.examples.cabin;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
@Named
public class Factories {

	public List<State> getStates() {
		List<State> retVal = Arrays.asList(State.values());
		return retVal;
	}
}
