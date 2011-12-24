package org.jahia.modules.personalization.tracking.trackers;

import org.jahia.modules.personalization.tracking.TrackingData;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.RenderChain;

/**
 * Basic interface for a tracker
 */
public interface TrackerInterface {

    /**
     * Basic method for a track to perform it's logic.
     * @param renderContext
     * @param resource
     * @param chain
     * @param trackingData
     * @return if true, we will call the next tracker, if not we will stop here.
     */
    public boolean track(RenderContext renderContext, Resource resource, RenderChain chain, TrackingData trackingData);

}
