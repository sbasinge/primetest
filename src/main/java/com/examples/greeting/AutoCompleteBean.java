package com.examples.greeting;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;

@Model
public class AutoCompleteBean {
	private String text;

	public List<String> complete(String query) {
		List<String> results = new ArrayList<String>();
		for (int i = 0; i < 10; i++)
			results.add(query + i);
		return results;
	}
	// getters and setters

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
