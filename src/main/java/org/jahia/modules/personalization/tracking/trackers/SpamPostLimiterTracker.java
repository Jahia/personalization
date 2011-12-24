package org.jahia.modules.personalization.tracking.trackers;

import org.jahia.modules.personalization.tracking.TrackingData;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.RenderChain;

/**
 * A tracker that tracks HTTP POST activity in live mode, to be able to limit spamming using automated software.
 */
public class SpamPostLimiterTracker implements TrackerInterface {
    public boolean track(RenderContext renderContext, Resource resource, RenderChain chain, TrackingData trackingData) {
        if (renderContext.isLiveMode()) {
            Long lastPostMethodTime = trackingData.getLong("lastPostMethodTime");
            long now = System.currentTimeMillis();
            if (lastPostMethodTime != null) {
                // let's check for spam activity
                long elapsedTime = now - lastPostMethodTime;
            }

            if (renderContext.getRequest().getMethod().toLowerCase().equals("post")) {
                trackingData.setLong("lastPostMethodTime", now);
            }
        }
        return true;
    }
}
