package com.examples.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.parsers.ParserConfigurationException;

import org.cyberneko.html.parsers.DOMParser;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.examples.annotation.Transactional;
import com.examples.cabin.AbstractPageBean;
import com.examples.cabin.CabinSearchBean;
import com.examples.cabin.entity.Cabin;

@Named
@ConversationScoped
public class HockingHillsRentalsService extends AbstractPageBean  {
	Logger log = LoggerFactory.getLogger(HockingHillsRentalsService.class);
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
		log.info("Performing online update for {}",cabinSearchBean.getSelectedCabin().getName());
		try {
			findCabinAmmenities();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void findCabinAmmenities() throws ParserConfigurationException, SAXException {
		if (cabinSearchBean.getSelectedCabin() != null && cabinSearchBean.getSelectedCabin().getName().trim().length()!=0) {
			searchTerm = cabinSearchBean.getSelectedCabin().getName();
			execute();
		} else {
			addWarn(null,"Warning","Search term is empty");
		}
		
	}

	@Transactional
	private void updateCabin(Cabin cabin) {
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
	public void execute() throws ParserConfigurationException, SAXException {
		String request = url;
		log.info("Executing: {}",request);

		// Send GET request
		try {
	        DOMParser parser = new DOMParser();
			URL url = new URL(request);
			InputStream stream = url.openStream();
			InputSource source = new InputSource();
			source.setByteStream(stream);
			parser.parse(source);
			Document doc = parser.getDocument();
			NodeList tbodies = doc.getElementsByTagName("TBODY");
			results = new ArrayList<CabinAmmenities>();
			for (int j =0; j < tbodies.getLength(); j++) {
				Node tbody = tbodies.item(j);
				log.info("Found tbody {} {}",tbody.getChildNodes().getLength(),printAttrs(tbody.getAttributes(),"  "));
				for (int i =0; i<tbody.getChildNodes().getLength(); i++) {
					Node tablerow = tbody.getChildNodes().item(i);
					log.info("Found tablerow {} {}",tablerow.getChildNodes().getLength(),printAttrs(tablerow.getAttributes(),"  "));
					if (tablerow.getAttributes() !=null && tablerow.getAttributes().getNamedItem("valign")!=null) {
						log.info("target row: {}",tablerow.getTextContent());
						if (tablerow.getTextContent().contains(searchTerm)) {
							log.info("target row -- search match");
							results.add(parseTableRow(tablerow));
						}
					}
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
	private CabinAmmenities parseTableRow(Node tablerow) {
		CabinAmmenities retVal = new CabinAmmenities();
		//column 1 has a <U><FONT><a></a>
		Node column1 = tablerow.getChildNodes().item(1);
		Node temp1 = column1.getFirstChild();
		Node temp2 = temp1.getFirstChild();
		NodeList temp2kids = temp2.getChildNodes();
		NodeList temp1kids = temp1.getChildNodes();
		Node temp = temp1kids.item(0);
		
		retVal.setName(temp.getFirstChild().getTextContent());
		retVal.setWebsiteLink(temp.getAttributes().getNamedItem("href").getNodeValue());
		 
		//column 2 has <FONT>
		Node column2 = tablerow.getChildNodes().item(3);
		retVal.setName(column2.getChildNodes().item(1).getTextContent());
		
		Node column3 = tablerow.getChildNodes().item(5);
		retVal.setMaxOccupants(Integer.parseInt(column3.getChildNodes().item(1).getFirstChild().getTextContent()));
		
		Node column4 = tablerow.getChildNodes().item(7);
		String priceLow = column4.getChildNodes().item(1).getFirstChild().getTextContent().substring(1);
		retVal.setLowPrice(new BigDecimal(Double.parseDouble(priceLow)));

		Node column6 = tablerow.getChildNodes().item(11);
		String priceHigh = column6.getChildNodes().item(1).getFirstChild().getTextContent().substring(1);
		retVal.setHighPrice(new BigDecimal(Double.parseDouble(priceHigh)));

		Node column7 = tablerow.getChildNodes().item(13);
		temp = column7.getChildNodes().item(1).getFirstChild();
		String numBedStr = temp.getTextContent();
		int i = numBedStr.indexOf("\n");
		retVal.setNumberOfBeds(Integer.parseInt(numBedStr.substring(0, i-1)));

		Node column9 = tablerow.getChildNodes().item(17);
		temp = column9.getChildNodes().item(1);
		if (temp!=null)
			retVal.setHotTub(true);
		
		Node column10 = tablerow.getChildNodes().item(19);
		temp = column10.getChildNodes().item(1);
		if (temp!=null)
			retVal.setFireplace(true);
		
		Node column11 = tablerow.getChildNodes().item(21);
		Node column12 = tablerow.getChildNodes().item(23);
		Node column13 = tablerow.getChildNodes().item(25);
		Node column14 = tablerow.getChildNodes().item(27);
		Node column15 = tablerow.getChildNodes().item(29);
		Node column16 = tablerow.getChildNodes().item(31);
		return retVal;
	}
	
    public void print(Node node, String indent) {
        log.info(indent+node.getNodeName()+", "+node.getNodeType()+", "+node.getNodeValue()+", "+printAttrs(node.getAttributes(),indent));
        Node child = node.getFirstChild();
        while (child != null) {
            print(child, indent+" ");
            child = child.getNextSibling();
        }
    }
    private String printAttrs(NamedNodeMap map, String indent) {
    	String retVal = "";
    	if (map != null) {
    		for (int i=0; i < map.getLength(); i++) {
    			Node temp = map.item(i);
    			retVal+= temp.getNodeName()+", "+temp.getNodeValue()+"\n";
    		}
    	} else {
    		retVal += indent+"None";
    	}
    	return retVal;
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

	public List<CabinAmmenities> getResults() {
		return results;
	}

	public void setResults(List<CabinAmmenities> results) {
		this.results = results;
	}

	public static void main(String args[]) {
		HockingHillsRentalsService service = new HockingHillsRentalsService();
		service.setSearchTerm("Crockett's Lodge");
//		service.setSearchTerm("The Shawnee"); //no hottub example
//		service.setSearchTerm("Grand Butte Lodge");
		
		try {
			service.execute();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

}
