package org.jahia.modules.personalization.tracking.trackers;

import org.jahia.modules.personalization.tracking.TrackingData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * A tracker for the browser locales.
 */
public class LocalesTracker implements TrackerInterface {
    public boolean track(HttpServletRequest request, HttpServletResponse response, TrackingData trackingData) {

        // first we copy the list of locales since we will want to revert it.
        Enumeration<Locale> localeEnumeration = request.getLocales();
        List<Locale> localeList = new ArrayList<Locale>();
        while (localeEnumeration.hasMoreElements()) {
            Locale locale = localeEnumeration.nextElement();
            localeList.add(locale);
        }

        // now we reserve the list so the preferred locale is the last one, which is how tracking data works.
        Collections.reverse(localeList);
        for (Locale locale : localeList) {
            trackingData.addStringToSet("locales", locale.toString());
        }
        return true;
    }
}
