package com.examples.service;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.primefaces.model.map.LatLng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.examples.cabin.entity.Address;
import com.examples.cabin.entity.Cabin;
import com.examples.cabin.entity.GeoLocation;
import com.examples.cabin.AbstractPageBean;
import com.examples.cabin.State;

@Named
@ConversationScoped
public class YahooLocatorService extends AbstractPageBean  {
	Logger log = LoggerFactory.getLogger(YahooLocatorService.class);
	static final String url = "http://local.yahooapis.com/LocalSearchService/V3/localSearch?appid=YahooDemo";

	@PersistenceContext
	EntityManager db;

	@Inject
	UserTransaction userTransaction;

	String searchTerm;
	int numResults = 20;
	int startPosition = 0;
	String zipCode = "43152";
	List<Cabin> results;

	public void loadCabins() throws ParserConfigurationException, SAXException {
		if (searchTerm != null || searchTerm.trim().length()==0) {
			for (int i=0; i < 10; i++) {
				if (i>0)
					setStartPosition(1+(i*20));
				execute();
				for(Cabin cabin : getResults()) {
					loadCabin(cabin);
				}
			}
		} else {
			addWarn(null,"Warning","Search term is empty");
		}
		
	}

	private void loadCabin(Cabin cabin) {
		try {
			userTransaction.begin();
			db.persist(cabin);
			userTransaction.commit();
		} catch (Exception e) {
			log.error("Error loading cabin",e);
		}
		
	}

	public void execute() throws ParserConfigurationException, SAXException {
		results = new ArrayList<Cabin>();
		String request = url + "&query=" + searchTerm + "&zip=" + zipCode
				+ "&results=" + numResults;
		if (startPosition > 0)
			request += "&start="+startPosition;
		log.info("Executing: {}",request);
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(request);

		// Send GET request
		try {
			int statusCode = client.executeMethod(method);
			// readResults(method);
			Parser parser = new Parser();
			results = parser.parseResults(method);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		log.debug("Setting searchTerm to {}",searchTerm);
		this.searchTerm = searchTerm;
	}

	public int getNumResults() {
		return numResults;
	}

	public void setNumResults(int numResults) {
		this.numResults = numResults;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	class Parser extends DefaultHandler {
		Logger log = LoggerFactory.getLogger(Parser.class);

		NumberFormat formatter = new DecimalFormat("#.000");
		List<Cabin> cabins = new ArrayList<Cabin>();
		String tagInProcess;
		String latitude;
		
		public List<Cabin> parseResults(GetMethod method)
				throws ParserConfigurationException, SAXException, IOException {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser;
			saxParser = factory.newSAXParser();
			saxParser.parse(method.getResponseBodyAsStream(), this);
			return cabins;
		}

		/**
		 * Mapping
		 * 	Title = name
		 *  Address = addressLine1
		 *  City = city
		 *  State = state code
		 *  Phone = phone
		 *  Latitude = address.location.lat
		 *  Longitude = address.location.lng
		 *  Rating
		 *  	AverageRating
		 *  	TotalRatings
		 *  	TtotalReviews
		 *  BusinessUrl = url
		 */
		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {
			tagInProcess = localName != null && localName.length()>0 ? localName : name;
			
			log.trace("Processing startElement {}", tagInProcess);
			// If <Result>-tag was found, a new location must be created.
			if (tagInProcess.equals("Result")) {
				log.info("Starting new cabin");
				cabins.add(new Cabin());
			}
		}

		@Override
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			if (cabins.size()==0)
				return;
			log.trace("Processing endElement {}", tagInProcess);
			Cabin currentCabin = cabins.get(cabins.size()-1);
			// <Result>-tag is closed. Save the location, as there will be
			// a new location created when the next <Result> is found.
			if (tagInProcess.equals("Result")) {
				log.info("Ending cabin {}",currentCabin);
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (cabins.size()==0)
				return;
			
			Cabin currentCabin = cabins.get(cabins.size()-1);
			if (currentCabin.getAddress() == null) 
				currentCabin.setAddress(new Address());
			
			String s = new String(ch, start, length).trim();
			log.debug("Processing characters {} for tag {}", s, tagInProcess);

			if ("Title".equalsIgnoreCase(tagInProcess)) {
				currentCabin.setName(s);
			} else if ("Address".equalsIgnoreCase(tagInProcess)) {
				Address address = new Address();
				address.setAddressLine1(s);
				currentCabin.setAddress(address);
			} else if ("City".equalsIgnoreCase(tagInProcess)) {
				currentCabin.getAddress().setCity(s);
			} else if ("State".equalsIgnoreCase(tagInProcess)) {
				currentCabin.getAddress().setState(State.valueOf(s));
			} else if ("Phone".equalsIgnoreCase(tagInProcess)) {
				currentCabin.setPhoneNumber(s);
			} else if ("Latitude".equalsIgnoreCase(tagInProcess)) {
				currentCabin.getAddress().setGeoLocation(new GeoLocation());
				latitude = s;
			} else if ("Longitude".equalsIgnoreCase(tagInProcess)) {
				String tempLat = formatter.format(Double.parseDouble(latitude));
				String tempLng = formatter.format(Double.parseDouble(s));
				currentCabin.getAddress().getGeoLocation().setLat(Double.parseDouble(tempLat));
				currentCabin.getAddress().getGeoLocation().setLng(Double.parseDouble(tempLng));
			} else if ("BusinessUrl".equalsIgnoreCase(tagInProcess)) {
				currentCabin.setUrl(s);
			}
		}
	}

	public List<Cabin> getResults() {
		return results;
	}

	public void setResults(List<Cabin> results) {
		this.results = results;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}
}
