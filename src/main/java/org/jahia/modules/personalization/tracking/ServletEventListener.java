package org.jahia.modules.personalization.tracking;

import org.jahia.bin.listeners.JahiaContextLoaderListener;
import org.jahia.params.ProcessingContext;
import org.jahia.services.scheduler.SchedulerService;
import org.jahia.services.usermanager.JahiaUser;
import org.jahia.services.usermanager.JahiaUserManagerService;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import javax.servlet.http.HttpSessionBindingEvent;

/**
 * This class listeners to servlet events, such as session creation/deletion, etc...
 */
public class ServletEventListener implements ApplicationListener {

    public TrackingFilter trackingFilter;
    public SchedulerService schedulerService;

    public void setTrackingFilter(TrackingFilter trackingFilter) {
        this.trackingFilter = trackingFilter;
    }

    public void setSchedulerService(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    public void onApplicationEvent(ApplicationEvent event) {

        if (event instanceof JahiaContextLoaderListener.HttpSessionDestroyedEvent) {
            // schedule background job to store or update the TrackingData
            JahiaContextLoaderListener.HttpSessionDestroyedEvent sessionDestroyedEvent = (JahiaContextLoaderListener.HttpSessionDestroyedEvent) event;
            TrackingData trackingData = (TrackingData) sessionDestroyedEvent.getSession().getAttribute(trackingFilter.getTrackingSessionName());
            if (trackingData == null) {
                return;
            }
        } else if (event instanceof JahiaContextLoaderListener.HttpSessionAttributeAddedEvent) {
            HttpSessionBindingEvent sessionBindingEvent = ((JahiaContextLoaderListener.HttpSessionAttributeRemovedEvent)event).getHttpSessionBindingEvent();
            if (sessionBindingEvent.getName().equals(ProcessingContext.SESSION_USER)) {
                // seems we have a new user that is logged in, but we must still check if it is guest.
                JahiaUser jahiaUser = (JahiaUser) sessionBindingEvent.getValue();
                if (!jahiaUser.getUsername().equals(JahiaUserManagerService.GUEST_USERNAME)) {
                    // we have detected a non-guest user, we must associate the tracking data with the user.
                }

            }
        } else if (event instanceof JahiaContextLoaderListener.HttpSessionAttributeRemovedEvent) {
            HttpSessionBindingEvent sessionBindingEvent = ((JahiaContextLoaderListener.HttpSessionAttributeRemovedEvent)event).getHttpSessionBindingEvent();
            if (sessionBindingEvent.getName().equals(ProcessingContext.SESSION_USER)) {
                // seems we have a new user that is logged out, but we must still check if it is guest.
                JahiaUser jahiaUser = (JahiaUser) sessionBindingEvent.getValue();
                if (!jahiaUser.getUsername().equals(JahiaUserManagerService.GUEST_USERNAME)) {
                    // we have detected a non-guest user, we must schedule schedule background storage
                }

            }
        }

    }
}
