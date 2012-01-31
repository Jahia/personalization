package org.jahia.modules.personalization.tracking.trackers;

import org.jahia.modules.personalization.tracking.TrackingData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A user-agent tracker
 */
public class UserAgentTracker implements TrackerInterface {
    public boolean track(HttpServletRequest request, HttpServletResponse response, TrackingData trackingData) {
        if (request.getHeader("User-Agent") != null) {
            trackingData.addValue("user-agents", request.getHeader("User-Agent"));
        }
        return true;
    }
}
