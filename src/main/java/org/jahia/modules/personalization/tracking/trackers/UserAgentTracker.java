package org.jahia.modules.personalization.tracking.trackers;

import org.jahia.modules.personalization.tracking.TrackingData;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.RenderChain;

/**
 * A user-agent tracker
 */
public class UserAgentTracker implements TrackerInterface {
    public boolean track(RenderContext renderContext, Resource resource, RenderChain chain, TrackingData trackingData) {
        if (renderContext.getRequest().getHeader("User-Agent") != null) {
            trackingData.addStringToSet("user-agents", renderContext.getRequest().getHeader("User-Agent"));
        }
        return true;
    }
}
