package com.cdi.scope;

import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;

import com.cdi.scope.handler.ScopeHandler;


public class ScopeHandlingRule extends TestWatchman {

    private ScopeHandler handler;
    
    @Override
    public void starting(FrameworkMethod method) {
        RequiredScope rc = method.getAnnotation(RequiredScope.class);
        if (null == rc) {
            return;
        }
        
        ScopeType scopeType = rc.value();
        handler = scopeType.getHandler();
        handler.initializeContext();
        
    }
    
    @Override
    public void finished(FrameworkMethod method) {
        if (null != handler) {
            handler.cleanupContext();
        }
    }
    
}