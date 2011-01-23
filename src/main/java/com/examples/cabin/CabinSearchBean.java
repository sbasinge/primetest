package com.examples.cabin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.eclipse.persistence.queries.ReadAllQuery;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.examples.cabin.entity.Address;
import com.examples.cabin.entity.Cabin;

//@SessionScoped
@ConversationScoped
@Named
public class CabinSearchBean extends AbstractPageBean {
	Logger log = LoggerFactory.getLogger(CabinSearchBean.class);

	@PersistenceContext
	EntityManager db;

	@Inject
	UserTransaction userTransaction;

	@Inject
	Conversation conversation;

	private Cabin cabin;
	private List<Cabin> cabins = new ArrayList<Cabin>();
	private MapModel mapModel;
	private LatLng mapCenter;
	private State state;
	private Cabin selectedCabin;

	private Marker marker;
	private java.lang.Double defaultRating = 2.5;
	private java.lang.Double maxStars = 5d;

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
		mapModel = new DefaultMapModel();
		mapCenter = new LatLng(40, -82);
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
		List<Cabin> results = db.createNamedQuery("findMatchingCabins")
				.setParameter("state", getCabin().getAddress().getState())
				.getResultList();
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
		for (Cabin cabin : getCabins()) {
			log.debug("Processing cabin {}", cabin);
			if (cabin.getAddress() != null && cabin.getAddress() != null
					&& cabin.getAddress().getGeoLocation() != null
					&& cabin.getAddress().getGeoLocation().getLatLng() != null) {
				mapModel.addOverlay(new Marker(cabin.getAddress().getGeoLocation()
						.getLatLng(), cabin.getName(),
						cabin.getAverageRating(),
						"http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
				// mapModel.addOverlay(new Marker(coord1));
				setMapCenter(cabin.getAddress().getGeoLocation().getLatLng());
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
		log.info("Marker selected for {}", marker.getLatlng());
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

	@SuppressWarnings("unchecked")
	public List<Cabin> getAllCabins() {
		return db.createNamedQuery("findAllCabins").getResultList();
	}

	public String onRowSelectNavigate(SelectEvent event) {
//		FacesContext.getCurrentInstance().getExternalContext().getFlash()
//				.put("selectedCabin", event.getObject());
		setSelectedCabin((Cabin) event.getObject());
		log.info("redirecting");
		return "edit.jsf?faces-redirect=true";
	}

	public String saveUpdates() {
		boolean success = true;
		log.info("Saving cabin {}",selectedCabin);
		String retVal = "list.jsf?faces-redirect=true";
		try {
			userTransaction.begin();
			db.merge(selectedCabin);
			db.flush();
			userTransaction.commit();
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

	public String deleteSelectedCabin() {
		boolean success = true;
		log.info("Deleting cabin {}",selectedCabin);
		String retVal = "list.jsf?faces-redirect=true";
		try {
			userTransaction.begin();
			Cabin temp = db.find(Cabin.class, selectedCabin.getId());
			db.remove(temp);
			userTransaction.commit();
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
}
