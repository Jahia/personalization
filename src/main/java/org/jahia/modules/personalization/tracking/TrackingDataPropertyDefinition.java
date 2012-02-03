package org.jahia.modules.personalization.tracking;

import java.io.Serializable;

/**
 * Definition of a tracking data property
 */
public class TrackingDataPropertyDefinition implements Serializable, Cloneable {

    public enum PropertyTypes {
        STRING, LONG, COUNTER, DATETIME, TIMESTAMPED_STRING, STRING_COUNTER
    }

    private String name;
    private boolean timestamped = false;
    private boolean multivalued = true;
    private PropertyTypes type = PropertyTypes.STRING; // allowed types are : string,long,counter,datetime
    private long maxValues = 100;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTimestamped() {
        return timestamped;
    }

    public void setTimestamped(boolean timestamped) {
        this.timestamped = timestamped;
    }

    public boolean isMultivalued() {
        return multivalued;
    }

    public void setMultivalued(boolean multivalued) {
        this.multivalued = multivalued;
    }

    public long getMaxValues() {
        return maxValues;
    }

    public void setMaxValues(long maxValues) {
        this.maxValues = maxValues;
    }

    public PropertyTypes getType() {
        return type;
    }

    public void setType(PropertyTypes type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = PropertyTypes.valueOf(type);
    }
}
