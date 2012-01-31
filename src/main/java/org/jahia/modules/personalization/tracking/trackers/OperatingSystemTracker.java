package org.jahia.modules.personalization.tracking.trackers;

import nl.bitwalker.useragentutils.UserAgent;
import org.jahia.modules.personalization.tracking.TrackingData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Operating system tracker based on user agent
 */
public class OperatingSystemTracker implements TrackerInterface {
    public boolean track(HttpServletRequest request, HttpServletResponse response, TrackingData trackingData) {
        if (request.getHeader("User-Agent") != null) {
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            trackingData.addValue("operating-system", userAgent.getOperatingSystem().getName());
        }
        return true;
    }
}
