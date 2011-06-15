package com.examples.cabin;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostRemove;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.examples.annotation.Transactional;
import com.examples.cabin.entity.RentalTerms;

@Named
@ConversationScoped
public class RentalTermsBean extends AbstractPageBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger log = LoggerFactory.getLogger(RentalTermsBean.class);

	@PersistenceContext
	EntityManager em;

	@Inject
	Conversation conversation;

	@Inject
	CabinSearchBean cabinSearchBean;

	private RentalTerms rentalTerms;
	
	@PostConstruct
	public void init() {
		if (conversation.isTransient())
			conversation.begin();
		log.info("Conversation begin {}", conversation.getId());

		if (cabinSearchBean.getSelectedRentalTerms()!=null) {
			setRentalTerms(cabinSearchBean.getSelectedRentalTerms());
		} else {
			setRentalTerms(new RentalTerms());
		}
	}

	@PostRemove
	public void endConversation() {
		log.info("Conversation end {}", conversation.getId());
		conversation.end();
	}

	public void setRentalTerms(RentalTerms rentalTerms) {
		this.rentalTerms = rentalTerms;
	}

	public RentalTerms getRentalTerms() {
		return rentalTerms;
	}

	@Transactional
	public String saveUpdates() {
		boolean success = true;
		log.info("Saving rentalTerms {}",rentalTerms);
		String retVal = "/cabins/list.jsf?faces-redirect=true";
		cabinSearchBean.getSelectedCabin().setRentalTerms(rentalTerms);
		try {
			em.merge(cabinSearchBean.getSelectedCabin());
//			em.persist(rentalTerms);
			em.flush();
			conversation.end();
		} catch (Exception e) {
			retVal = null;
			success = false;
			addError("msgs","Error saving", e.getMessage());
		}
		if (success) {
			addInfo("msgs","Added","rentalTerms added..");
		}
		return retVal;
	}

	@Transactional
	public String deleteSelected() {
		boolean success = true;
		log.info("Deleting cabin {}",rentalTerms);
		String retVal = "list.jsf?faces-redirect=true";
		try {
			RentalTerms temp = em.find(RentalTerms.class, rentalTerms.getId());
			em.remove(temp);
			conversation.end();
		} catch (Exception e) {
			retVal = null;
			success = false;
			addError("msgs","Error deleting", e.getMessage());
		}
		if (success) {
			addInfo("msgs","Removed","rentalTerms removed..");
		}
		return retVal;
	}

	/**
	 * Discard changes, end conversation and return to list page.
	 * @return
	 */
	public String cancelEdit() {
		String retVal = "list.jsf?faces-redirect=true";
		em.clear();
		conversation.end();
		return retVal;
	}

}
