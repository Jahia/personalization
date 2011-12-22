package org.jahia.modules.personalization.tracking;

import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.AbstractFilter;
import org.jahia.services.render.filter.RenderChain;

/**
 * Tracking filter class
 */
public class TrackingFilter extends AbstractFilter {

    private String trackingCookieName = "jahiaTrackingID";

    public void setTrackingCookieName(String trackingCookieName) {
        this.trackingCookieName = trackingCookieName;
    }

    @Override
    public String prepare(RenderContext renderContext, Resource resource, RenderChain chain) throws Exception {
        return super.prepare(renderContext, resource, chain);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String execute(String previousOut, RenderContext renderContext, Resource resource, RenderChain chain) throws Exception {
        return super.execute(previousOut, renderContext, resource, chain);
    }
}
