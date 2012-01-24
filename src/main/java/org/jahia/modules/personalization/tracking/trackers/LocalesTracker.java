package org.jahia.modules.personalization.tracking.trackers;

import org.jahia.modules.personalization.tracking.TrackingData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Locale;

/**
 * A tracker for the browser locales.
 */
public class LocalesTracker implements TrackerInterface {
    public boolean track(HttpServletRequest request, HttpServletResponse response, TrackingData trackingData) {
        Enumeration<Locale> localeEnumeration = request.getLocales();
        while (localeEnumeration.hasMoreElements()) {
            Locale locale = localeEnumeration.nextElement();
            trackingData.addStringToSet("locales", locale.toString());
        }
        return true;
    }
}
