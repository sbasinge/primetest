package com.cdi.scope.handler;

import org.jboss.weld.Container;
import org.jboss.weld.context.ContextLifecycle;
import org.jboss.weld.context.ConversationContext;
import org.jboss.weld.context.beanstore.HashMapBeanStore;

public class ConversationScopeHandler implements ScopeHandler {

    @Override
    public void initializeContext() {
        ConversationContext conversationContext = Container.instance().services().get(ContextLifecycle.class).getConversationContext();
        conversationContext.setBeanStore(new HashMapBeanStore());
        conversationContext.setActive(true);
    }

    @Override
    public void cleanupContext() {
        ConversationContext conversationContext = Container.instance().services().get(ContextLifecycle.class).getConversationContext();
        if(conversationContext.isActive()) {
            conversationContext.setActive(false);
            conversationContext.cleanup();
        }
    }

}