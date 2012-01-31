package org.jahia.modules.personalization.tracking.trackers;

import org.jahia.modules.personalization.tracking.TrackingData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple IP address and host tracker
 */
public class HostAndIpAddressTracker implements TrackerInterface {
    public boolean track(HttpServletRequest request, HttpServletResponse response, TrackingData trackingData) {
        trackingData.addValue("ips", request.getRemoteAddr());
        trackingData.addValue("hosts", request.getRemoteHost());
        return true;
    }
}
