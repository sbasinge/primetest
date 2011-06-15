package com.examples.cabin;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class AbstractPageBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void addError(String clientId, String summary, String msg) {  
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR,summary, msg));  
    }  

    public void addWarn(String clientId, String summary, String msg) {  
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_WARN,summary, msg));  
    }  

    public void addInfo(String clientId, String summary, String msg) {  
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,summary, msg));  
    }  

}
