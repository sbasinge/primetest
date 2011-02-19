package com.examples.cabin;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdi.scope.ScopeHandlingRule;
import com.examples.cabin.entity.Cabin;

public class AbstractTest {
	static Logger log = LoggerFactory.getLogger(AbstractTest.class);

	@Rule
	public ScopeHandlingRule scopeHandlingRule = new ScopeHandlingRule();

	@PersistenceContext
	EntityManager em;

	@Inject
	CabinSearchBean bean;

	@Deployment
	public static Archive<?> createDeployment() {
		WebArchive retVal = ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackage(CabinSearchBean.class.getPackage())
				.addPackage(Cabin.class.getPackage())
				.addManifestResource("test-persistence.xml", "persistence.xml")
				.addWebResource("logging.properties", "logging.properties")
				.addWebResource("log4j.xml", "log4j.xml")
				.addWebResource(EmptyAsset.INSTANCE, "beans.xml");
		return retVal;
	}

}
