package org.jahia.modules.personalization.tracking;

import org.jahia.services.content.JCRNodeWrapper;

import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TrackingData factory class
 */
public class TrackingDataFactory {

    Map<String,TrackingDataPropertyDefinition> propertyDefinitions = new HashMap<String, TrackingDataPropertyDefinition>();

    private List<String> ignoredProperties = new ArrayList<String>();

    private static TrackingDataFactory instance = new TrackingDataFactory();

    public static TrackingDataFactory getInstance() {
        return instance;
    }

    public Map<String, TrackingDataPropertyDefinition> getPropertyDefinitions() {
        return propertyDefinitions;
    }

    public void setPropertyDefinitions(Map<String, TrackingDataPropertyDefinition> propertyDefinitions) {
        this.propertyDefinitions = propertyDefinitions;
    }

    public List<String> getIgnoredProperties() {
        return ignoredProperties;
    }

    public void setIgnoredProperties(List<String> ignoredProperties) {
        this.ignoredProperties = ignoredProperties;
    }

    public TrackingData getTrackingData(JCRNodeWrapper jcrNodeWrapper) throws RepositoryException {
        return new TrackingData(jcrNodeWrapper, ignoredProperties, propertyDefinitions);
    }

    public JCRNodeWrapper toJCRNode(TrackingData trackingData, JCRNodeWrapper jcrNodeWrapper) throws RepositoryException {
        return trackingData.toJCRNode(jcrNodeWrapper, ignoredProperties);
    }

}
