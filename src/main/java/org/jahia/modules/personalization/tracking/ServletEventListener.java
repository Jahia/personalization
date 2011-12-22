package org.jahia.modules.personalization.tracking;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * This class listeners to servlet events, such as session creation/deletion, etc...
 */
public class ServletEventListener implements ApplicationListener {
    public void onApplicationEvent(ApplicationEvent event) {
    }
}
