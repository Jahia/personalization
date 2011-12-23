package org.jahia.modules.personalization.tracking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class for all tracking data
 */
public class TrackingData implements Serializable {

    private String storageID;
    private String clientID;
    private String associatedUserKey;
    private String associatedUserIdentifier;

    private Map<String, List<String>> trackingMap = new HashMap<String, List<String>>();

    public String getStorageID() {
        return storageID;
    }

    public void setStorageID(String storageID) {
        this.storageID = storageID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getAssociatedUserKey() {
        return associatedUserKey;
    }

    public void setAssociatedUserKey(String associatedUserKey) {
        this.associatedUserKey = associatedUserKey;
    }

    public String getAssociatedUserIdentifier() {
        return associatedUserIdentifier;
    }

    public void setAssociatedUserIdentifier(String associatedUserIdentifier) {
        this.associatedUserIdentifier = associatedUserIdentifier;
    }

    public Map<String, List<String>> getTrackingMap() {
        return trackingMap;
    }

    public void setTrackingMap(Map<String, List<String>> trackingMap) {
        this.trackingMap = trackingMap;
    }

    public Long getLong(String mapKey) {
        String longStr = getSingleValue(mapKey);
        if (longStr != null) {
            return new Long(longStr);
        } else {
            return null;
        }
    }

    public Long setLong(String mapKey, Long newValue) {
        String previousValue = setSingleValue(mapKey, newValue.toString());
        if (previousValue != null) {
            return new Long(previousValue);
        } else {
            return null;
        }
    }

    public Double getDouble(String mapKey) {
        String doubleStr =  getSingleValue(mapKey);
        if (doubleStr != null) {
            return new Double(doubleStr);
        } else {
            return null;
        }
    }

    public Double setDouble(String mapKey, Double newValue) {
        String previousValue = setSingleValue(mapKey, newValue.toString());
        if (previousValue != null) {
            return new Double(previousValue);
        } else {
            return null;
        }
    }

    public void addStringToSet(String mapKey, String newValue) {
        List<String> stringList = trackingMap.get(mapKey);
        if (stringList == null) {
            stringList = new ArrayList<String>();
        }
        if (stringList.contains(newValue)) {
            return;
        }
        stringList.add(newValue);
        trackingMap.put(mapKey, stringList);
    }

    private String getSingleValue(String mapKey) {
        List<String> stringList = trackingMap.get(mapKey);
        return getListSingleValue(stringList);
    }

    private String getListSingleValue(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        if (stringList.size() == 0) {
            return null;
        }
        return stringList.get(0);
    }

    private String setSingleValue(String mapKey, String value) {
        List<String> stringList = new ArrayList<String>();
        stringList.add(value);
        List<String> oldList = trackingMap.put(mapKey, stringList);
        return getListSingleValue(oldList);
    }
}
