package org.jahia.modules.personalization.tracking.trackers;

import org.jahia.modules.personalization.tracking.TrackingData;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.RenderChain;

import java.util.Enumeration;
import java.util.Locale;

/**
 * A tracker for the browser locales.
 */
public class LocalesTracker implements TrackerInterface {
    public boolean track(RenderContext renderContext, Resource resource, RenderChain chain, TrackingData trackingData) {
        Enumeration<Locale> localeEnumeration = renderContext.getRequest().getLocales();
        while (localeEnumeration.hasMoreElements()) {
            Locale locale = localeEnumeration.nextElement();
            trackingData.addStringToSet("locales", locale.toString());
        }
        return true;
    }
}
