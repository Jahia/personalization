package org.jahia.modules.personalization.tracking;

import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.AbstractFilter;
import org.jahia.services.render.filter.RenderChain;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Tracking filter class
 */
public class TrackingFilter extends AbstractFilter {

    private String trackingCookieName = "jahiaTrackingID";

    private String trackingSessionName = "org.jahia.modules.personalization.trackingData";

    private TrackingService trackingService;

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

    public void setTrackingService(TrackingService trackingService) {
        this.trackingService = trackingService;
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
                    trackingData = trackingService.getById(trackingID);
                    break;
                }
            }
            if (trackingData == null) {
                // tracking data was not found using a cookie, this probably means that we need to create a new one
                trackingData = new TrackingData();
                Calendar calendar = Calendar.getInstance();
                trackingData.setClientID(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + UUID.randomUUID().toString());
                Cookie trackingCookie = new Cookie(trackingCookieName, trackingData.getClientID());
                trackingCookie.setMaxAge(Integer.MAX_VALUE);
                trackingCookie.setPath("/");
                renderContext.getResponse().addCookie(trackingCookie);
            }
        }

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

        StringBuilder fullUrl = new StringBuilder(renderContext.getRequest().getRequestURI());
        if (renderContext.getRequest().getQueryString() != null) {
            fullUrl.append("?");
            fullUrl.append(renderContext.getRequest().getQueryString());
        }
        if (renderContext.getRequest().getHeader("Referer") != null) {
            fullUrl.append(":" + renderContext.getRequest().getHeader("Referer"));
        }
        trackingData.addStringToSet("urls", fullUrl.toString());

        trackingData.addStringToSet("ips", renderContext.getRequest().getRemoteAddr());
        trackingData.addStringToSet("hosts", renderContext.getRequest().getRemoteHost());

        if (renderContext.getRequest().getHeader("User-Agent") != null) {
            trackingData.addStringToSet("user-agents", renderContext.getRequest().getHeader("User-Agent"));
        }

        Enumeration<Locale> localeEnumeration = renderContext.getRequest().getLocales();
        while (localeEnumeration.hasMoreElements()) {
            Locale locale = localeEnumeration.nextElement();
            trackingData.addStringToSet("locales", locale.toString());
        }

        trackingData.addStringToSet("resources", renderContext.getMainResource().getNode().getPath());

        session.setAttribute(trackingSessionName, trackingData);
        renderContext.getRequest().setAttribute(trackingSessionName, trackingData);

        return super.prepare(renderContext, resource, chain);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String execute(String previousOut, RenderContext renderContext, Resource resource, RenderChain chain) throws Exception {
        return super.execute(previousOut, renderContext, resource, chain);
    }
}
