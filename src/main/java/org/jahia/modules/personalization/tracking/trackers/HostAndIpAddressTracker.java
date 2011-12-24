package org.jahia.modules.personalization.tracking.trackers;

import org.jahia.modules.personalization.tracking.TrackingData;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.RenderChain;

/**
 * Simple IP address and host tracker
 */
public class HostAndIpAddressTracker implements TrackerInterface {
    public boolean track(RenderContext renderContext, Resource resource, RenderChain chain, TrackingData trackingData) {
        trackingData.addStringToSet("ips", renderContext.getRequest().getRemoteAddr());
        trackingData.addStringToSet("hosts", renderContext.getRequest().getRemoteHost());
        return true;
    }
}
