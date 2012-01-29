package org.jahia.modules.personalization.tracking.trackers;

import org.jahia.modules.personalization.tracking.TrackingData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Last access (GET or POST) tracker
 */
public class LastAccessTracker implements TrackerInterface {

    public boolean track(HttpServletRequest request, HttpServletResponse response, TrackingData trackingData) {

        Boolean alreadyCalculated = (Boolean) request.getAttribute("alreadyCalculatedLastAccess");
        if (alreadyCalculated != null && alreadyCalculated.booleanValue()) {
            return true;
        }

        // track only live, preview and contribute accesses, and only on the /cms dispatcher, ignore the GWT accesses for
        // the moment.
        if (request.getRequestURI().startsWith("/cms") &&
            !request.getRequestURI().startsWith("/cms/edit")) {
            long now = System.currentTimeMillis();

            Long lastAccessTime = trackingData.getLong("lastAccessTime");
            Double lastAccessLoadAverage = trackingData.getDouble("lastAccessLoadAverage");
            if (lastAccessTime != null) {
                long elapsedTime = now - lastAccessTime;
                double elapsedTimeInSeconds = elapsedTime / 1000.0;
                double timeInMinutes = 1;
                double calcFreqDouble = 5.0;
                if (lastAccessLoadAverage == null) {
                    lastAccessLoadAverage = 0.0;
                }
                lastAccessLoadAverage = lastAccessLoadAverage * Math.exp(-calcFreqDouble / (60.0 * timeInMinutes)) + (5 / (elapsedTimeInSeconds)) * (1 - Math.exp(-calcFreqDouble / (60.0 * timeInMinutes)));
                trackingData.setDouble("lastAccessLoadAverage", lastAccessLoadAverage);
            }

            trackingData.setLong("lastAccessTime", now);
        }

        request.setAttribute("alreadyCalculatedLastAccess", new Boolean(true));
        return true;
    }
}
