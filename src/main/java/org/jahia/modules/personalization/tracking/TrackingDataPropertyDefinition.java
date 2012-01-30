package org.jahia.modules.personalization.tracking;

/**
 * Definition of a tracking data property
 */
public class TrackingDataPropertyDefinition {

    private String name;
    private boolean timestamped = false;
    private boolean multivalued = true;

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
}
