package org.jahia.modules.personalization.tracking;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Main class for all tracking data
 */
public class TrackingData implements Serializable {

    private String storageID;
    private String clientID;
    private String associatedUserKey;
    private String associatedUserIdentifier;

    private Map<String, Serializable> trackingMap = new HashMap<String, Serializable>();

}
