package org.jahia.modules.personalization.tracking.trackers;

import org.jahia.modules.personalization.tracking.TrackingData;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.RenderChain;

/**
 * An url and referer tracker
 */
public class UrlAndRefererTracker implements TrackerInterface {
    public boolean track(RenderContext renderContext, Resource resource, RenderChain chain, TrackingData trackingData) {
        StringBuilder fullUrl = new StringBuilder(renderContext.getRequest().getRequestURI());
        if (renderContext.getRequest().getQueryString() != null) {
            fullUrl.append("?");
            fullUrl.append(renderContext.getRequest().getQueryString());
        }
        if (renderContext.getRequest().getHeader("Referer") != null) {
            fullUrl.append(":" + renderContext.getRequest().getHeader("Referer"));
        }
        trackingData.addStringToSet("urls", fullUrl.toString());
        return true;
    }
}
