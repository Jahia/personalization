package org.jahia.modules.personalization.tracking;

import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.AbstractFilter;
import org.jahia.services.render.filter.RenderChain;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 * Tracking filter class
 */
public class TrackingFilter extends AbstractFilter {

    private String trackingCookieName = "jahiaTrackingID";

    private String trackingSessionName = "org.jahia.modules.personalization.trackingData";

    public String getTrackingCookieName() {
        return trackingCookieName;
    }

    public String getTrackingSessionName() {
        return trackingSessionName;
    }

    public void setTrackingCookieName(String trackingCookieName) {
        this.trackingCookieName = trackingCookieName;
    }

    public void setTrackingSessionName(String trackingSessionName) {
        this.trackingSessionName = trackingSessionName;
    }

    @Override
    public String prepare(RenderContext renderContext, Resource resource, RenderChain chain) throws Exception {

        HttpSession session = renderContext.getRequest().getSession(false);
        if (session == null) {
            // if no session is present, we don't do anything...
            return super.prepare(renderContext, resource, chain);
        }

        TrackingData trackingData = (TrackingData) session.getAttribute(trackingSessionName);
        if (trackingData == null) {
            Cookie[] cookies = renderContext.getRequest().getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(trackingCookieName)) {
                    // we have found the cookie, we must lookup the tracking data in the persistent storage.
                    String trackingID = cookie.getValue();
                    // trackingData = TrackingService.getById(trackingID);
                    break;
                }
            }
            if (trackingData == null) {
                // tracking data was not found using a cookie, this probably means that we need to create a new one
                trackingData = new TrackingData();
            }
        }

        return super.prepare(renderContext, resource, chain);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String execute(String previousOut, RenderContext renderContext, Resource resource, RenderChain chain) throws Exception {
        return super.execute(previousOut, renderContext, resource, chain);
    }
}
