package org.jahia.modules.personalization.tracking;

import java.io.Serializable;

/**
 * Definition of a tracking data property
 */
public class TrackingDataPropertyDefinition implements Serializable, Cloneable {

    private String name;
    private boolean timestamped = false;
    private boolean multivalued = true;
    private boolean counter = false;
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

    public boolean isCounter() {
        return counter;
    }

    public void setCounter(boolean counter) {
        this.counter = counter;
    }
}
