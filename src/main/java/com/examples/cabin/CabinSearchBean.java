package com.examples.cabin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.PostRemove;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import org.eclipse.persistence.queries.ReadAllQuery;
import org.primefaces.event.map.OverlaySelectEvent;  
import org.primefaces.model.map.DefaultMapModel;  
import org.primefaces.model.map.LatLng;  
import org.primefaces.model.map.MapModel;  
import org.primefaces.model.map.Marker;  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.examples.cabin.entity.Cabin;

//@SessionScoped
@ConversationScoped
@Named
public class CabinSearchBean implements Serializable {
	Logger log = LoggerFactory.getLogger(CabinSearchBean.class);

	@PersistenceContext
	EntityManager db;

	@Inject
	UserTransaction userTransaction;

	@Inject Conversation conversation;
	
	private Cabin cabin;
	private List<Cabin> cabins = new ArrayList<Cabin>();
    private MapModel mapModel; 
    private LatLng mapCenter;
    private State state;

	private Marker marker;
    private java.lang.Double defaultRating = 3.0;
    
	@PostConstruct
	public void init() {
		cabin = new Cabin();
		mapModel = new DefaultMapModel();
		mapCenter = new LatLng(40,-82);
		log.debug("Creating cabin: {}", cabin);
		conversation.begin();
		log.info("Conversation begin {}", conversation.getId());
	}

	@PostRemove
	public void endConversation() {
		log.info("Conversation end {}", conversation.getId());
		conversation.end();
	}
	
	public void search() {
		log.warn("Searching cabins for {}", cabin);
		@SuppressWarnings("unchecked")
		List<Cabin> results = db.createNamedQuery("findMatchingCabins").setParameter("state", getCabin().getAddress().getState()).getResultList();
		log.info("Results: {}",results.size());
		
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
		for (Cabin cabin: getCabins()) {
			log.debug("Processing cabin {}",cabin);
			if (cabin.getAddress()!=null && cabin.getAddress()!=null && cabin.getAddress().getLocation() != null && cabin.getAddress().getLocation().getLatLng()!=null) {
				mapModel.addOverlay(new Marker(cabin.getAddress().getLocation().getLatLng(), cabin.getName(), cabin.getAverageRating(), "http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
//				mapModel.addOverlay(new Marker(coord1));
				setMapCenter(cabin.getAddress().getLocation().getLatLng());
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
    	log.info("Marker selected for {}",marker.getLatlng());
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
}
