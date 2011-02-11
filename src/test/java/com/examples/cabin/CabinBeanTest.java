package com.examples.cabin;

import java.util.List;

import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.weld.Container;
import org.jboss.weld.context.ContextLifecycle;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdi.scope.RequiredScope;
import com.cdi.scope.ScopeHandlingRule;
import com.cdi.scope.ScopeType;
import com.examples.cabin.entity.Cabin;

@RunWith(Arquillian.class)
public class CabinBeanTest {
	private static Logger log = LoggerFactory.getLogger(CabinBeanTest.class);

	@Rule
	public ScopeHandlingRule scopeHandlingRule = new ScopeHandlingRule();

	@PersistenceContext
	EntityManager em;

	@Inject
	UserTransaction utx;

	@Inject
	CabinSearchBean bean;

	@Inject
	TestDataGenerator generator;

	@Deployment
	public static Archive<?> createDeployment() {
		WebArchive retVal = ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackage(CabinSearchBean.class.getPackage())
				.addPackage(Cabin.class.getPackage())
				.addManifestResource("test-persistence.xml", "persistence.xml")
				.addWebResource("logging.properties", "logging.properties")
				.addWebResource(EmptyAsset.INSTANCE, "beans.xml");
		return retVal;
	}

	@Test
	@RequiredScope(ScopeType.CONVERSATION)
	public void ConverationContextOk() throws Exception {

		boolean ready = Container.instance().services()
				.get(ContextLifecycle.class).isConversationActive();
		log.info("Checking configuration ........ {}", ready);
		assert (ready);
	}

	@Test
	@RequiredScope(ScopeType.CONVERSATION)
	public void FindAllCabins() throws Exception {
		utx.begin();
		em.joinTransaction();
		generator.loadTestData();
		bean.populateAllCabins();
		List<Cabin> temp = bean.getCabins();
		log.info("{} cabins found.", temp.size());
		// utx.commit();
		utx.rollback();
	}
}
