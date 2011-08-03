package com.examples.interceptor;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.examples.annotation.Transactional;

@Transactional
@Interceptor
public class TransactionInterceptor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory
			.getLogger(TransactionInterceptor.class);

	@Inject private UserTransaction tx;

	@AroundInvoke
	public Object workInTransaction(InvocationContext invocation)
			throws Exception {
		log.debug("TransactionInterceptor fired");
		try {
//			boolean transactionStarted = false;
//			try {
//				db.joinTransaction(); 
//			} catch (javax.persistence.TransactionRequiredException e) {
//				userTransaction.begin();
//				db.joinTransaction();
//				transactionStarted = true;
//			}
			
			tx.begin();
			Object result = invocation.proceed();
			tx.commit();
			log.info("TransactionInterceptor - commit");
			return result;
		} catch (Exception e) {
			tx.rollback();
			throw e;
		}
	}
}
