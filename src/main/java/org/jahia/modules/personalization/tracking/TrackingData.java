package org.jahia.modules.personalization.tracking;

import org.jahia.services.content.JCRNodeWrapper;

import javax.jcr.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class for all tracking data
 */
public class TrackingData implements Serializable, Cloneable {

    private String storageID;
    private String clientID;
    private String associatedUserKey;
    private String associatedUserIdentifier;

    private Map<String, List<String>> trackingMap = new HashMap<String, List<String>>();

    // @todo not yet implemented.
    private Map<String, List<String>> removedEntries = new HashMap<String, List<String>>();

    public TrackingData() {}

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public TrackingData(JCRNodeWrapper node) throws RepositoryException {
        storageID = node.getIdentifier();
        PropertyIterator propertyIterator = node.getProperties();
        while (propertyIterator.hasNext()) {
            Property property = propertyIterator.nextProperty();
            if (property.getName().equals("j:clientId")) {
                clientID = property.getString();
            } else if (property.getName().equals("j:associatedUserKey")) {
                associatedUserKey = property.getString();
            } else if (property.getName().equals("j:associatedUserIdentifier")) {
                associatedUserIdentifier = property.getNode().getIdentifier();
            } else {
                List<String> trackingMapValue = new ArrayList<String>();
                if (property.isMultiple()) {
                    Value[] values = property.getValues();
                    for (Value value : values) {
                        trackingMapValue.add(value.getString());
                    }
                } else {
                    trackingMapValue.add(property.getString());
                }
                trackingMap.put(property.getName(), trackingMapValue);
            }
        }
    }

    public JCRNodeWrapper toJCRNode(JCRNodeWrapper node) throws RepositoryException {
        node.setProperty("j:clientId", clientID);
        if (associatedUserKey != null) {
            node.setProperty("j:associatedUserKey", associatedUserKey);
        }
        if (associatedUserIdentifier != null) {
            JCRNodeWrapper associatedUserNode = node.getSession().getNodeByIdentifier(associatedUserIdentifier);
            node.setProperty("j:associatedUserIdentifier", associatedUserNode);
        }
        for (Map.Entry<String,List<String>> trackingMapEntry : trackingMap.entrySet()) {
            ValueFactory valueFactory = node.getSession().getValueFactory();
            List<String> stringList = trackingMapEntry.getValue();
            List<Value> valueList = new ArrayList<Value>();
            for (String curString : stringList) {
                valueList.add(valueFactory.createValue(curString));
            }
            node.setProperty(trackingMapEntry.getKey(), valueList.toArray(new Value[valueList.size()]));
        }
        return node;
    }

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

    /**
     * Use this method to merge tracking data from another object into this one. This method will only merge
     * the tracking map, not other fields. For the moment these objects do not track removals so we simply add
     * all values.
     * @param otherTrackingData
     * @return
     */
    public TrackingData merge(TrackingData otherTrackingData) {
        Map<String,List<String>> otherTrackingMap = otherTrackingData.getTrackingMap();
        for (Map.Entry<String,List<String>> otherTrackingMapEntry : otherTrackingMap.entrySet()) {
            if (trackingMap.containsKey(otherTrackingMapEntry.getKey())) {
                // we already have this entry, we must merge the lists.
                List<String> ourList = trackingMap.get(otherTrackingMapEntry.getKey());
                List<String> otherList = otherTrackingMapEntry.getValue();
                for (String otherString : otherList) {
                    if (!ourList.contains(otherString)) {
                        ourList.add(otherString);
                    }
                }
                trackingMap.put(otherTrackingMapEntry.getKey(), ourList);
            } else {
                trackingMap.put(otherTrackingMapEntry.getKey(), otherTrackingMapEntry.getValue());
            }
        }
        return this;
    }

    // Private methods

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
