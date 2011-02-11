package com.cdi.scope.handler;

import org.jboss.weld.Container;
import org.jboss.weld.context.ContextLifecycle;
import org.jboss.weld.context.SessionContext;
import org.jboss.weld.context.beanstore.HashMapBeanStore;


public class SessionScopeHandler implements ScopeHandler {

    @Override
    public void initializeContext() {
        SessionContext context = Container.instance().services().get(ContextLifecycle.class).getSessionContext();
        context.setBeanStore(new HashMapBeanStore());
        context.setActive(true);
    }

    @Override
    public void cleanupContext() {
        SessionContext context = Container.instance().services().get(ContextLifecycle.class).getSessionContext();
        if(context.isActive()) {
            context.setActive(false);
            context.cleanup();
        }
    }

}