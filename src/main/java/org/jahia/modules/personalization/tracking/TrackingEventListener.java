package org.jahia.modules.personalization.tracking;

import org.jahia.bin.Logout;
import org.jahia.bin.listeners.JahiaContextLoaderListener;
import org.jahia.params.ProcessingContext;
import org.jahia.params.valves.LoginEngineAuthValveImpl;
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

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * This class listeners to servlet events, such as session creation/deletion, etc...
 */
public class TrackingEventListener implements ApplicationListener {

    private static Logger logger = LoggerFactory.getLogger(TrackingEventListener.class);

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
            HttpSession session = sessionDestroyedEvent.getSession();
            long lastAccessedTime = session.getLastAccessedTime();
            long maxInactiveInterval = session.getMaxInactiveInterval() * 1000;
            long nowTime = System.currentTimeMillis();

            // we check the times because we need to differentiate between a session invalidation on login/logout and a session expiration.
            if ((nowTime - lastAccessedTime) >= maxInactiveInterval) {
                // session has indeed expired.
                TrackingData trackingData = (TrackingData) sessionDestroyedEvent.getSession().getAttribute(trackingFilter.getTrackingSessionName());
                if (trackingData == null) {
                    return;
                }
                // as sessions gets destroyed upon login (because we do an invalidate), we should use a ThreadLocal variable or a request attribute to keep the trackingData instance
                scheduleJob(trackingData);
            }

        } else if (event instanceof JahiaContextLoaderListener.HttpSessionAttributeAddedEvent) {
            HttpSessionBindingEvent sessionBindingEvent = ((JahiaContextLoaderListener.HttpSessionAttributeAddedEvent)event).getHttpSessionBindingEvent();
        } else if (event instanceof JahiaContextLoaderListener.HttpSessionAttributeRemovedEvent) {
            HttpSessionBindingEvent sessionBindingEvent = ((JahiaContextLoaderListener.HttpSessionAttributeRemovedEvent)event).getHttpSessionBindingEvent();
        } else if (event instanceof JahiaContextLoaderListener.HttpSessionAttributeReplacedEvent) {
            HttpSessionBindingEvent sessionBindingEvent = ((JahiaContextLoaderListener.HttpSessionAttributeReplacedEvent)event).getHttpSessionBindingEvent();
        } else if (event instanceof LoginEngineAuthValveImpl.LoginEvent) {
            LoginEngineAuthValveImpl.LoginEvent loginEvent = (LoginEngineAuthValveImpl.LoginEvent) event;
            TrackingData trackingData = (TrackingData) loginEvent.getAuthValveContext().getRequest().getSession().getAttribute(trackingFilter.getTrackingSessionName());
            if (trackingData == null) {
                trackingData = trackingFilter.getThreadLocalTrackingData();
                if (trackingData == null) {
                    return;
                }
            }
            trackingData.setAssociatedUserKey(loginEvent.getJahiaUser().getUserKey());
        } else if (event instanceof Logout.LogoutEvent) {
            Logout.LogoutEvent logoutEvent = (Logout.LogoutEvent) event;
            TrackingData trackingData = (TrackingData) logoutEvent.getRequest().getSession().getAttribute(trackingFilter.getTrackingSessionName());
            if (trackingData == null) {
                return;
            }
            scheduleJob(trackingData);

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
