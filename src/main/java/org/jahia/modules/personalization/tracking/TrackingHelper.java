package org.jahia.modules.personalization.tracking;

import java.util.Calendar;
import java.util.UUID;

/**
 * A singleton to help the storage job implementation.
 */
public class TrackingHelper {

    public static final String CLIENTID_SEPARATOR = "---";
    public TrackingService trackingService;
    private static TrackingHelper instance = new TrackingHelper();

    public TrackingService getTrackingService() {
        return trackingService;
    }

    public void setTrackingService(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    public static TrackingHelper getInstance() {
        return instance;
    }

    public String generateNewClientID() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + CLIENTID_SEPARATOR + calendar.get(Calendar.MONTH) + CLIENTID_SEPARATOR + calendar.get(Calendar.DAY_OF_MONTH) + CLIENTID_SEPARATOR + UUID.randomUUID().toString();
    }

    public String[] getClientIDParts(String clientID) {
        String[] clientIDParts = clientID.split(CLIENTID_SEPARATOR);
        return clientIDParts;
    }
}
