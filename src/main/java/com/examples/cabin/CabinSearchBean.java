package com.examples.cabin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PostRemove;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.examples.cabin.entity.Address;
import com.examples.cabin.entity.Address_;
import com.examples.cabin.entity.Cabin;
import com.examples.cabin.entity.Cabin_;
import com.examples.cabin.entity.RentalTerms;
import com.examples.cabin.entity.RentalTerms_;

//@Stateful
//@SessionScoped
@ConversationScoped
@Named
public class CabinSearchBean extends AbstractPageBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(CabinSearchBean.class);

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
    EntityManager db;

	@Inject
	Conversation conversation;

	private Cabin cabin;
	private List<Cabin> cabins = new ArrayList<Cabin>();
	private MapModel mapModel;
	private LatLng mapCenter;
	private State state;
	private Cabin selectedCabin;
	private RentalTerms selectedRentalTerms;

	private Marker marker;
	private java.lang.Double defaultRating = 2.5;
	private java.lang.Double maxStars = 5d;
	private double startingRange;
	private double endingRange;
	private double minPrice;
	private double maxPrice;
	private int rating;
	
	
	public java.lang.Double getMaxStars() {
		return maxStars;
	}

	public void setMaxStars(java.lang.Double maxStars) {
		this.maxStars = maxStars;
	}

	@PostConstruct
	public void init() {
		cabin = new Cabin();
		cabin.setAddress(new Address());
		log.debug("Creating cabin: {}", cabin);
		conversation.begin();
		log.info("Conversation begin {}", conversation.getId());
		populateAllCabins();
		calculateMinAndMaxPrices();
	}

	@PostRemove
	public void endConversation() {
		log.info("Conversation end {}", conversation.getId());
		conversation.end();
	}

	public void search() {
		log.warn("Searching cabins for {}", cabin);
		List<Cabin> results = null;
		results = buildAndRunQuery();
		log.info("Results: {}", results.size());
		setCabins(results);
		updateMapModel();
	}

	public void setCabin(Cabin cabin) {
		this.cabin = cabin;
	}

	public Cabin getCabin() {
		return cabin;
	}

	public void setCabins(List<Cabin> cabins) {
		this.cabins = cabins;
	}

	public List<Cabin> getCabins() {
		log.info("GetCabins returning {} cabins.",cabins.size());
		return cabins;
	}

	public void addCabin(Cabin cabin) {
		getCabins().add(cabin);
	}

	public void setMapModel(MapModel simpleModel) {
		this.mapModel = simpleModel;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	private void updateMapModel() {
		mapModel = new DefaultMapModel();
		mapCenter = new LatLng(40, -82);

		for (Cabin cabin : getCabins()) {
			LatLng latLng = cabin.getAddress().getGeoLocation().createLatLng();
			log.debug("Processing cabin {} at {}", cabin, cabin.getAddress().getGeoLocation().getLat());
			if (cabin.getAddress() != null && cabin.getAddress().getGeoLocation() != null) {
				log.debug("Adding overlay at {} {}", cabin.getAddress().getGeoLocation().getLat(), cabin.getAddress().getGeoLocation().getLng());
				mapModel.addOverlay(new Marker(latLng, cabin.getEscapedName(),
						cabin.getId(),
						"http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
				// mapModel.addOverlay(new Marker(coord1));
				setMapCenter(latLng);
			}
		}

	}

	public void setMapCenter(LatLng mapCenter) {
		this.mapCenter = mapCenter;
	}

	public LatLng getMapCenter() {
		return mapCenter;
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}

	public void onMarkerSelect(OverlaySelectEvent event) {
		marker = (Marker) event.getOverlay();
		int id = (Integer) marker.getData();
		log.info("Marker selected for {}", marker.getLatlng());
		for (Cabin cabin: cabins) {
			if (cabin.getId()==id) {
				selectedCabin = cabin;
				break;
			}
		}
		addInfo("msgs","select","marker selected.");
	}

	public Marker getMarker() {
		return marker;
	}

	public void setDefaultRating(java.lang.Double defaultRating) {
		this.defaultRating = defaultRating;
	}

	public java.lang.Double getDefaultRating() {
		return defaultRating;
	}

	public void setSelectedCabin(Cabin selectedCabin) {
		this.selectedCabin = selectedCabin;
		log.info("Cabin selected {}", selectedCabin);
		addInfo("msgs","select","cabin selected.");
	}

	public Cabin getSelectedCabin() {
		return selectedCabin;
	}

//	@Transactional
	@SuppressWarnings("unchecked")
	public void populateAllCabins() {
		log.info("Populating all cabins");
		cabins = db.createNamedQuery("findAllCabins").getResultList();
		//calc high and low prices
	}

	public String onRowSelectNavigate(SelectEvent event) {
//		FacesContext.getCurrentInstance().getExternalContext().getFlash()
//				.put("selectedCabin", event.getObject());
		setSelectedCabin((Cabin) event.getObject());
		log.info("redirecting");
		return "edit.jsf?faces-redirect=true";
	}

	public String onRentalRowSelectNavigate(SelectEvent event) {
		setSelectedRentalTerms((RentalTerms) event.getObject());
		log.info("redirecting");
		return "/rentalTerms/edit.jsf?faces-redirect=true";
	}

//	@Transactional
	public String saveUpdates() {
		boolean success = true;
		log.info("Saving cabin {}",selectedCabin);
		String retVal = "list.jsf?faces-redirect=true";
		try {
			db.merge(selectedCabin);
			db.flush();
		} catch (Exception e) {
			retVal = null;
			success = false;
			addError("msgs","Error saving", e.getMessage());
		}
		if (success) {
			addInfo("msgs","Added","cabnin added..");
		}
		return retVal;
	}

//	@Transactional
	public String deleteSelectedCabin() {
		boolean success = true;
		log.info("Deleting cabin {}",selectedCabin);
		String retVal = "list.jsf?faces-redirect=true";
		try {
			Cabin temp = db.find(Cabin.class, selectedCabin.getId());
			db.remove(temp);
		} catch (Exception e) {
			retVal = null;
			success = false;
			addError("msgs","Error deleting", e.getMessage());
		}
		if (success) {
			addInfo("msgs","Removed","cabnin removed..");
		}
		return retVal;
	}

	/**
	 * Discard changes, end conversation and return to list page.
	 * @return
	 */
	public String cancelEdit() {
		String retVal = "list.jsf?faces-redirect=true";
		db.clear();
		conversation.end();
		return retVal;
	}
	
	private List<Cabin> buildAndRunQuery() {
		List<Cabin> retVal = null;
		CriteriaBuilder builder = db.getCriteriaBuilder();
		CriteriaQuery<Cabin> query = builder.createQuery(Cabin.class);
		Root<Cabin> root = query.from(Cabin.class);

		Predicate temp = builder.conjunction();;
		if(cabin.getAddress().getState()!=null) {
			Join<Cabin,Address> address = root.join( Cabin_.address );
			temp = builder.and(temp,builder.equal(address.get(Address_.state),cabin.getAddress().getState()));
		}
		if (cabin.isFirePit()) {
			temp = builder.and(temp,builder.isTrue(root.get(Cabin_.firePit)));
		}
		if (cabin.isFirePlace()) {
			temp = builder.and(temp,builder.isTrue(root.get(Cabin_.firePlace)));
		}
		if (cabin.isHotTub()) {
			temp = builder.and(temp,builder.isTrue(root.get(Cabin_.hotTub)));
		}
		if (startingRange > minPrice || endingRange < maxPrice) {
			Join<Cabin,RentalTerms> terms = root.join( Cabin_.rentalTerms );
			Expression<BigDecimal> rate = terms.get(RentalTerms_.nightlyRental);
			temp = builder.and(temp,builder.between(rate, new BigDecimal(startingRange), new BigDecimal(endingRange)));
		}
		query.where(temp);
		
		retVal = db.createQuery(query).getResultList();
		if (this.getRating() >= 1) {
			List<Cabin> tempResult = new ArrayList<Cabin>();
			for (Cabin cabin: retVal) {
				if (cabin.getAverageRating()>= getRating()) {
					tempResult.add(cabin);
				}
			}
			retVal = tempResult;
		}
		return retVal;
	}

	public void setStartingRange(double startingRange) {
		this.startingRange = startingRange;
	}

	public double getStartingRange() {
		return startingRange;
	}

	public void setEndingRange(double endingRange) {
		this.endingRange = endingRange;
	}

	public double getEndingRange() {
		return endingRange;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public double getMaxPrice() {
		return maxPrice;
	}

	private void calculateMinAndMaxPrices() {
		minPrice = Double.MAX_VALUE;
		maxPrice = 0;
		for (Cabin cabin : cabins) {
			if (cabin.getNightlyRate() < minPrice) {
				minPrice = cabin.getNightlyRate();
			}
			if (cabin.getNightlyRate() > maxPrice) {
				maxPrice = cabin.getNightlyRate();
			}
		}
		startingRange = minPrice;
		endingRange = maxPrice;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	/**
	 * Take the selected cabin and store/pass to the rental terms edit page so terms are added to the selected cabin.
	 * 
	 * @return
	 */
	public String addRentalTerms() {
		String retVal = "/rentalTerms/add.jsf?faces-redirect=true";
		return retVal;
	}

	public void setSelectedRentalTerms(RentalTerms selectedRentalTerms) {
		this.selectedRentalTerms = selectedRentalTerms;
	}

	public RentalTerms getSelectedRentalTerms() {
		return selectedRentalTerms;
	}
	
	public String updateAmmenities() {
		String retVal = "/ammenities/list.jsf?faces-redirect=true";
		return retVal;
	}
}
