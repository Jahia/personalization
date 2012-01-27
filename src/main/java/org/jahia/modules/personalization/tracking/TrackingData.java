package org.jahia.modules.personalization.tracking;

import org.jahia.services.content.JCRNodeWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static Logger logger = LoggerFactory.getLogger(TrackingData.class);

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

    public TrackingData(JCRNodeWrapper node, List<String> ignoredProperties) throws RepositoryException {
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
                if (!isIgnoredProperty(ignoredProperties, property.getName())) {
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
    }

    public JCRNodeWrapper toJCRNode(JCRNodeWrapper node, List<String> ignoredProperties) throws RepositoryException {
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
            if (!isIgnoredProperty(ignoredProperties, trackingMapEntry.getKey())) {
                node.setProperty(trackingMapEntry.getKey(), valueList.toArray(new Value[valueList.size()]));
            }
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

    public String getActiveValue(String mapKey) {
        List<String> stringList = trackingMap.get(mapKey);
        return stringList.get(stringList.size()-1);
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
            stringList.remove(newValue);
            // we remove the value because we want the latest value to always be inserted at the end of the list.
        }
        stringList.add(newValue);
        trackingMap.put(mapKey, stringList);
    }

    /**
     * Use this method to merge tracking data from another object into a new one. This method will only merge
     * the tracking map, not other fields. For the moment these objects do not track removals so we simply add
     * all values.
     *
     * This method does not modify this object, but creates a clone and merges into the clone. The result is then
     * returned
     *
     * The pattern of usage for this method should always be newTrackingData = oldTrackingData.merge(newTrackingData)
     * because of the active element being the last one and it should remain so.
     * @param otherTrackingData
     * @return the merge result.
     */
    public TrackingData merge(TrackingData otherTrackingData) {
        Map<String,List<String>> otherTrackingMap = otherTrackingData.getTrackingMap();
        TrackingData resultTrackingData = null;
        try {
            resultTrackingData = (TrackingData) clone();
        } catch (CloneNotSupportedException cnse) {
            logger.error("Cannot clone tracking data", cnse);
            return null;
        }
        for (Map.Entry<String,List<String>> otherTrackingMapEntry : otherTrackingMap.entrySet()) {
            if (resultTrackingData.getTrackingMap().containsKey(otherTrackingMapEntry.getKey())) {
                // we already have this entry, we must merge the lists.
                List<String> ourList = resultTrackingData.getTrackingMap().get(otherTrackingMapEntry.getKey());
                List<String> otherList = otherTrackingMapEntry.getValue();
                for (String otherString : otherList) {
                    if (!ourList.contains(otherString)) {
                        ourList.add(otherString);
                    }
                }
                resultTrackingData.getTrackingMap().put(otherTrackingMapEntry.getKey(), ourList);
            } else {
                resultTrackingData.getTrackingMap().put(otherTrackingMapEntry.getKey(), otherTrackingMapEntry.getValue());
            }
        }
        return resultTrackingData;
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

    private boolean isIgnoredProperty(List<String> ignoredProperties, String propertyName) {
        for (String ignoredProperty : ignoredProperties) {
            if (propertyName.matches(ignoredProperty)) {
                return true;
            }
        }
        return false;
    }
}
