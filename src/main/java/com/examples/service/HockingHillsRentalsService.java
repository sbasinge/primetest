package com.examples.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.parsers.ParserConfigurationException;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.examples.annotation.Transactional;
import com.examples.cabin.AbstractPageBean;
import com.examples.cabin.CabinSearchBean;
import com.examples.cabin.entity.Cabin;

@Stateful
@Named
@ConversationScoped
public class HockingHillsRentalsService extends AbstractPageBean  {
	private static final long serialVersionUID = 1L;

	static final Logger log = LoggerFactory.getLogger(HockingHillsRentalsService.class);
	static final String url = "http://www.hockinghillsrentals.com/index.htm";

	@PersistenceContext
	EntityManager em;

	@Inject
	Conversation conversation;

	@Inject
	CabinSearchBean cabinSearchBean;

	String searchTerm;
	int numResults = 5;
	List<CabinAmmenities> results = new ArrayList<CabinAmmenities>();
	
	CabinAmmenities selectedAmmenities;

	
	@PostConstruct
	public void init() {
//		log.info("Performing online update for {}",cabinSearchBean.getSelectedCabin().getName());
		findCabinAmmenities();
	}

	public void findCabinAmmenities() {
		if (cabinSearchBean.getSelectedCabin() != null && cabinSearchBean.getSelectedCabin().getName().trim().length()!=0) {
			searchTerm = cabinSearchBean.getSelectedCabin().getName();
			execute();
		} else {
			addWarn(null,"Warning","Search term is empty");
		}
		
	}

	@Transactional
	public void updateCabin(Cabin cabin) {
		try {
			em.persist(cabin);
		} catch (Exception e) {
			log.error("Error loading cabin",e);
		}
		
	}

	/**
	 * Page has a 6 tables.  The 3rd table has a list of cabins.  Each cabin has 16 cells
	 * 3rd table has a <font><thead></thead><tbody></tbody>
	 * 1 - website link and name
	 * 2 - cabin name
	 * 3 - number of occupants and a static image of a bed
	 * 4 - low price with a $, e.g. $465
	 * 5 - the word to
	 * 6 - high price with a $
	 * 7 - number of bedrooms, e.g. 5 BR
	 * 8 - spacer
	 * 9 - empty cell or a img of a hot tub if the place has a hot tub.
	 * 10 - empty cell or has a img of a fireplace
	 * 11 - cell with img src=images/icon_handicapNO.gif if not handicap accessible, icon_handicapYes.gif for yes
	 * 12 - cell with img src=images/icon_dogNO.gif for no pets.
	 * 13 - cell with img src=images/icon_smokingNo.gif for no smoking
	 * 14 - square footage text, e.g. 3000 sqft
	 * 15 - acerage text
	 * 16 - additional text
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void execute() {
		String request = url;
		log.info("Executing: {} {}",request,searchTerm);

		// Send GET request
		try {
			URL url = new URL(request);
			InputStream stream = url.openStream();
			
			Source source = new Source(stream);
			List<Element> tablerows = source.getAllElements(HTMLElementName.TR);
			tablerows = tablerows.subList(3, tablerows.size()); //first three rows are not cabins
			results = new ArrayList<CabinAmmenities>();
			for (Element tablerow : tablerows) {
				log.trace("Found tablerow {}",tablerow.getTextExtractor().toString());
				if (tablerow.getTextExtractor().toString().contains(searchTerm)) {
						log.info("target row -- search match");
						results.add(parseTableRow(tablerow));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 1 - website link and name
	 * 2 - cabin name
	 * 3 - number of occupants and a static image of a bed
	 * 4 - low price with a $, e.g. $465
	 * 5 - the word to
	 * 6 - high price with a $
	 * 7 - number of bedrooms, e.g. 5 BR
	 * 8 - spacer
	 * 9 - empty cell or a img of a hot tub if the place has a hot tub.
	 * 10 - empty cell or has a img of a fireplace
	 * 11 - cell with img src=images/icon_handicapNO.gif if not handicap accessible, icon_handicapYes.gif for yes
	 * 12 - cell with img src=images/icon_dogNO.gif for no pets.
	 * 13 - cell with img src=images/icon_smokingNo.gif for no smoking
	 * 14 - square footage text, e.g. 3000 sqft
	 * 15 - acerage text
	 * 16 - additional text
	**/
	private CabinAmmenities parseTableRow(Element tablerow) {
		CabinAmmenities retVal = new CabinAmmenities();
		List<Element> columns = tablerow.getAllElements(HTMLElementName.TD);
		//column 1 has a <U><FONT><a></a>
		Element column1 = columns.get(0);
		Element a = column1.getAllElements(HTMLElementName.A).get(0);
		log.trace("temp is {}",a.getContent());
		
		retVal.setName(a.getTextExtractor().toString());
		retVal.setWebsiteLink(a.getAttributeValue("HREF"));
		 
		//column 2 has <FONT>
		Element column2 = columns.get(1);
		retVal.setName(column2.getTextExtractor().toString());
		
		Element column3 = columns.get(2);
		retVal.setMaxOccupants(Integer.parseInt(column3.getTextExtractor().toString()));
		
		Element column4 = columns.get(3);
		if (column4.getTextExtractor().toString().length()>0) {
			String priceLow = column4.getTextExtractor().toString().substring(1);
			retVal.setLowPrice(new BigDecimal(Double.parseDouble(priceLow)));
		}

		Element column6 = columns.get(5);
		if (column6.getTextExtractor().toString().length()>0) {
			String priceHigh = column6.getTextExtractor().toString().substring(1);
			retVal.setHighPrice(new BigDecimal(Double.parseDouble(priceHigh)));
		}

		Element column7 = columns.get(6);
		if (column7.getTextExtractor().toString().length()>0) {
			String numBedStr = column7.getTextExtractor().toString();
			int i = numBedStr.indexOf(" ");
			retVal.setNumberOfBeds(Integer.parseInt(numBedStr.substring(0, i)));
		}

		Element column9 = columns.get(8);
		if (column9.getAllElements(HTMLElementName.IMG).size()>0)
			retVal.setHotTub(true);
		
		Element column10 = columns.get(9);
		if (column10.getAllElements(HTMLElementName.IMG).size()>0)
			retVal.setFireplace(true);

		return retVal;
	}
	
	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		log.trace("Setting searchTerm to {}",searchTerm);
		this.searchTerm = searchTerm;
	}

	public int getNumResults() {
		return numResults;
	}

	public void setNumResults(int numResults) {
		this.numResults = numResults;
	}

	public List<CabinAmmenities> getResults() {
		return results;
	}

	public void setResults(List<CabinAmmenities> results) {
		this.results = results;
	}

	public static void main(String args[]) {
		HockingHillsRentalsService service = new HockingHillsRentalsService();
//		service.setSearchTerm("Crockett's Lodge");
//		service.setSearchTerm("The Shawnee"); //no hottub example
//		service.setSearchTerm("Grand Butte Lodge");
		service.setSearchTerm("Foxglove Lodge");
		
		service.execute();
	}
	

	public CabinAmmenities getSelectedAmmenities() {
		return selectedAmmenities;
	}

	public void setSelectedAmmenities(CabinAmmenities selectedAmmenities) {
		this.selectedAmmenities = selectedAmmenities;
	}
	public String onRowSelectNavigate(SelectEvent event) {
		setSelectedAmmenities((CabinAmmenities) event.getObject());
		log.info("Selected {}",getSelectedAmmenities().getCabinName());
		//outta update the cabin and save.....
		log.info("redirecting");
		return "/cabins/list.jsf?faces-redirect=true";
	}
	
//	@Transactional
	public void updateAllCabinAmmenities() {
		//select all cabins
		@SuppressWarnings("unchecked")
		List<Cabin> cabins = (List<Cabin>) em.createNamedQuery("findAllCabins").getResultList();
		//for each cabin call execute with the cabin name
		for (Cabin cabin : cabins) {
			searchTerm = cabin.getName();
			execute();
			//select the first of the results that come back
			if (results.size()>0) {
				CabinAmmenities ammenities = results.get(0);
				
				//update the ammenities
				cabin.updateAmmenities(ammenities);
				
				//save the cabin
				updateCabin(cabin);
				break;
			}
		}
	}

}
