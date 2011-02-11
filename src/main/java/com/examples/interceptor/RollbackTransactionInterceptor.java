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

@Transactional(rollback=true)
@Interceptor
public class RollbackTransactionInterceptor implements Serializable {
	private static Logger log = LoggerFactory
			.getLogger(RollbackTransactionInterceptor.class);

	@Inject private UserTransaction tx;

	@AroundInvoke
	public Object workInTransaction(InvocationContext invocation)
			throws Exception {
		log.debug("RollbackTransactionInterceptor fired");
		try {
			tx.begin(); 
			Object result = invocation.proceed();
			tx.rollback();
			return result;
		} catch (Exception e) {
			tx.rollback();
			throw e;
		}
	}
}
