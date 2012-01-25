package org.jahia.modules.personalization.tracking;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import java.util.Map;

/**
 * Spring bean that will register an interceptor into an existing URL mapping handler to be able to intercept
 * other modules' mappings.
 */
public class DynamicUrlMappingHandlerInterceptorRegistrator implements InitializingBean, ApplicationContextAware {

    private SimpleUrlHandlerMapping simpleUrlHandlerMapping;

    private HandlerInterceptor interceptorToRegister;

    private boolean interceptAllMappings = false;

    private ApplicationContext applicationContext;

    public void setInterceptorToRegister(HandlerInterceptor interceptorToRegister) {
        this.interceptorToRegister = interceptorToRegister;
    }

    public void setSimpleUrlHandlerMapping(SimpleUrlHandlerMapping simpleUrlHandlerMapping) {
        this.simpleUrlHandlerMapping = simpleUrlHandlerMapping;
    }

    public void setInterceptAllMappings(boolean interceptAllMappings) {
        this.interceptAllMappings = interceptAllMappings;
    }

    public void afterPropertiesSet() throws Exception {
        if ((simpleUrlHandlerMapping != null) && (interceptorToRegister != null)) {
            simpleUrlHandlerMapping.setInterceptors(new Object[] { interceptorToRegister });
            simpleUrlHandlerMapping.initApplicationContext();
        } else if (interceptAllMappings) {
            Map<String, SimpleUrlHandlerMapping> simpleUrlHandlerMappingMap = applicationContext.getBeansOfType(SimpleUrlHandlerMapping.class);
            for (Map.Entry<String, SimpleUrlHandlerMapping> simpleUrlHandlerMappingEntry : simpleUrlHandlerMappingMap.entrySet()) {
                simpleUrlHandlerMappingEntry.getValue().setInterceptors(new Object[] { interceptorToRegister });
                simpleUrlHandlerMappingEntry.getValue().initApplicationContext();
            }
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
