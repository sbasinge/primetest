package com.examples.cabin;

import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cdi.scope.RequiredScope;
import com.cdi.scope.ScopeType;
import com.examples.cabin.entity.Cabin;

@RunWith(Arquillian.class)
public class CabinSearchBeanTest extends AbstractTest {

	@Inject
	TestDataGenerator generator;

	@Inject
	UserTransaction utx;

	@Test
	@RequiredScope(ScopeType.CONVERSATION)
	public void FindAllCabins() throws Exception {
		utx.begin();
		em.joinTransaction();
		generator.loadTestData();
		bean.populateAllCabins();
		List<Cabin> temp = bean.getCabins();
		log.info("{} cabins found.", temp.size());
		assertTrue("All cabins = 4",temp.size()==4);
		utx.rollback();
	}

	@Test
	@RequiredScope(ScopeType.CONVERSATION)
	public void FindMatchingCabins() throws Exception {
		utx.begin();
		em.joinTransaction();
		generator.loadTestData();
		bean.getCabin().setFirePit(true);
		bean.search();
		List<Cabin> temp = bean.getCabins();
		log.info("{} cabins found.", temp.size());
		assertTrue("All cabins with firepits = 2",temp.size()==2);
		utx.rollback();
	}

}
