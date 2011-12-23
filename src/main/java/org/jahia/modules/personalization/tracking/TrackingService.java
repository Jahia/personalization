package org.jahia.modules.personalization.tracking;

/**
 * Tracking data persistence storage service.
 */
public class TrackingService {

    public TrackingData getById(String trackingId) {
        return new TrackingData();

    }

    public boolean store(TrackingData trackingData) {
        return true;
    }

    public boolean merge(TrackingData trackingData) {
        return false;
    }
}
