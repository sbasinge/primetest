package com.cdi.scope.handler;

import org.jboss.weld.Container;
import org.jboss.weld.context.ContextLifecycle;
import org.jboss.weld.context.RequestContext;
import org.jboss.weld.context.beanstore.HashMapBeanStore;


public class RequestScopeHandler implements ScopeHandler {

    @Override
    public void initializeContext() {
        RequestContext context = Container.instance().services().get(ContextLifecycle.class).getRequestContext();
        context.setBeanStore(new HashMapBeanStore());
        context.setActive(true);
    }

    @Override
    public void cleanupContext() {
        RequestContext context = Container.instance().services().get(ContextLifecycle.class).getRequestContext();
        if(context.isActive()) {
            context.setActive(false);
            context.cleanup();
        }
    }

}