package org.jahia.modules.personalization.tracking;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

/**
 * Spring bean that will register an interceptor into an existing URL mapping handler to be able to intercept
 * other modules' mappings.
 */
public class DynamicUrlMappingHandlerInterceptorRegistrator implements InitializingBean {

    private SimpleUrlHandlerMapping simpleUrlHandlerMapping;

    private HandlerInterceptor interceptorToRegister;

    public void setInterceptorToRegister(HandlerInterceptor interceptorToRegister) {
        this.interceptorToRegister = interceptorToRegister;
    }

    public void setSimpleUrlHandlerMapping(SimpleUrlHandlerMapping simpleUrlHandlerMapping) {
        this.simpleUrlHandlerMapping = simpleUrlHandlerMapping;
    }

    public void afterPropertiesSet() throws Exception {
        simpleUrlHandlerMapping.setInterceptors(new Object[] { interceptorToRegister });
        simpleUrlHandlerMapping.initApplicationContext();
    }

}
