package org.jahia.modules.personalization.tracking;

import org.jahia.bin.listeners.JahiaContextLoaderListener;
import org.jahia.params.ProcessingContext;
import org.jahia.services.scheduler.BackgroundJob;
import org.jahia.services.scheduler.SchedulerService;
import org.jahia.services.usermanager.JahiaUser;
import org.jahia.services.usermanager.JahiaUserManagerService;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import javax.servlet.http.HttpSessionBindingEvent;
import java.util.Set;

/**
 * This class listeners to servlet events, such as session creation/deletion, etc...
 */
public class ServletEventListener implements ApplicationListener {

    private static Logger logger = LoggerFactory.getLogger(ServletEventListener.class);

    public TrackingFilter trackingFilter;
    public SchedulerService schedulerService;

    public void setTrackingFilter(TrackingFilter trackingFilter) {
        this.trackingFilter = trackingFilter;
    }

    public void setSchedulerService(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    public void onApplicationEvent(ApplicationEvent event) {

        if (event instanceof JahiaContextLoaderListener.HttpSessionCreatedEvent) {
            JahiaContextLoaderListener.HttpSessionCreatedEvent sessionCreatedEvent = (JahiaContextLoaderListener.HttpSessionCreatedEvent) event;

        } else if (event instanceof JahiaContextLoaderListener.HttpSessionDestroyedEvent) {
            // schedule background job to store or update the TrackingData
            JahiaContextLoaderListener.HttpSessionDestroyedEvent sessionDestroyedEvent = (JahiaContextLoaderListener.HttpSessionDestroyedEvent) event;
            TrackingData trackingData = (TrackingData) sessionDestroyedEvent.getSession().getAttribute(trackingFilter.getTrackingSessionName());
            if (trackingData == null) {
                return;
            }
            // as sessions gets destroyed upon login (because we do an invalidate), we should use a ThreadLocal variable or a request attribute to keep the trackingData instance
        } else if (event instanceof JahiaContextLoaderListener.HttpSessionAttributeAddedEvent) {
            HttpSessionBindingEvent sessionBindingEvent = ((JahiaContextLoaderListener.HttpSessionAttributeAddedEvent)event).getHttpSessionBindingEvent();
            if (sessionBindingEvent.getName().equals(ProcessingContext.SESSION_USER)) {
                // seems we have a new user that is logged in, but we must still check if it is guest.
                JahiaUser jahiaUser = (JahiaUser) sessionBindingEvent.getValue();
                if (!jahiaUser.getUsername().equals(JahiaUserManagerService.GUEST_USERNAME)) {
                    // we have detected a non-guest user, we must associate the tracking data with the user.
                    TrackingData trackingData = (TrackingData) sessionBindingEvent.getSession().getAttribute(trackingFilter.getTrackingSessionName());
                    if (trackingData == null) {
                        trackingData = trackingFilter.getThreadLocalTrackingData();
                        if (trackingData == null) {
                            return;
                        }
                    }
                    trackingData.setAssociatedUserKey(jahiaUser.getUserKey());
                    // ideally we should also load tracking data in the user profile and merge with the current tracking data.
                }

            }
        } else if (event instanceof JahiaContextLoaderListener.HttpSessionAttributeRemovedEvent) {
            HttpSessionBindingEvent sessionBindingEvent = ((JahiaContextLoaderListener.HttpSessionAttributeRemovedEvent)event).getHttpSessionBindingEvent();
            if (sessionBindingEvent.getName().equals(ProcessingContext.SESSION_USER)) {
                // seems we have a new user that is logged out, but we must still check if it is guest.
                JahiaUser jahiaUser = (JahiaUser) sessionBindingEvent.getValue();
                if (!jahiaUser.getUsername().equals(JahiaUserManagerService.GUEST_USERNAME)) {
                    // we have detected a non-guest user, we must schedule schedule background storage
                    TrackingData trackingData = (TrackingData) sessionBindingEvent.getSession().getAttribute(trackingFilter.getTrackingSessionName());
                    if (trackingData == null) {
                        return;
                    }
                    scheduleJob(trackingData);
                }

            }
        } else if (event instanceof JahiaContextLoaderListener.HttpSessionAttributeReplacedEvent) {
            HttpSessionBindingEvent sessionBindingEvent = ((JahiaContextLoaderListener.HttpSessionAttributeReplacedEvent)event).getHttpSessionBindingEvent();
            if (sessionBindingEvent.getName().equals(ProcessingContext.SESSION_USER)) {
                // seems we have a new user that is logged out, but we must still check if it is guest.
                JahiaUser jahiaUser = (JahiaUser) sessionBindingEvent.getValue();
                if (!jahiaUser.getUsername().equals(JahiaUserManagerService.GUEST_USERNAME)) {
                    // we have detected a non-guest user, we must schedule schedule background storage
                    TrackingData trackingData = (TrackingData) sessionBindingEvent.getSession().getAttribute(trackingFilter.getTrackingSessionName());
                    if (trackingData == null) {
                        return;
                    }
                    scheduleJob(trackingData);
                }
            }
        }

    }

    private void scheduleJob(TrackingData trackingData) {
        JobDetail jobDetail = BackgroundJob.createJahiaJob("Tracking data storage for client "
                + trackingData.getClientID(), TrackingStorageJob.class);
        jobDetail.setGroup("Personalization");
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put(TrackingStorageJob.JOB_TRACKINGDATA, trackingData);

        logger.info("Scheduling tracking data storage for client {} associatedUser={}",
                trackingData.getClientID(), trackingData.getAssociatedUserKey());

        try {
            schedulerService.scheduleJobNow(jobDetail);
        } catch (SchedulerException e) {
            logger.error("Error scheduling tracking data storage for clientID=" + trackingData.getClientID(), e);
        }
    }

}
