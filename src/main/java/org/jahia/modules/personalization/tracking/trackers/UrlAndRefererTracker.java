package org.jahia.modules.personalization.tracking.trackers;

import org.jahia.modules.personalization.tracking.TrackingData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An url and referer tracker
 */
public class UrlAndRefererTracker implements TrackerInterface {
    public boolean track(HttpServletRequest request, HttpServletResponse response, TrackingData trackingData) {
        StringBuilder fullUrl = new StringBuilder(request.getRequestURI());
        if (request.getQueryString() != null) {
            fullUrl.append("?");
            fullUrl.append(request.getQueryString());
        }
        if (request.getHeader("Referer") != null) {
            fullUrl.append(":" + request.getHeader("Referer"));
        }
        trackingData.addStringToSet("urls", fullUrl.toString());
        return true;
    }
}
