package org.jahia.modules.personalization.tracking;

import org.apache.log4j.Logger;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.nodetypes.ExtendedPropertyDefinition;
import org.jahia.services.content.nodetypes.initializers.ChoiceListValue;
import org.jahia.services.content.nodetypes.initializers.ModuleChoiceListInitializer;

import javax.jcr.RepositoryException;
import java.util.*;

/**
 * A choice list initializer to select tracking data values
 */
public class TrackingDataChoiceListInitializer implements ModuleChoiceListInitializer {
    private transient static Logger logger = Logger.getLogger(TrackingDataChoiceListInitializer.class);

    private String key;

    public List<ChoiceListValue> getChoiceListValues(ExtendedPropertyDefinition epd, String param,
                                                     List<ChoiceListValue> values, Locale locale,
                                                     Map<String, Object> context) {
        JCRNodeWrapper node = (JCRNodeWrapper) context.get("contextNode");
        if (node == null) {
            node = (JCRNodeWrapper) context.get("contextParent");
        }
        if (node != null) {
            try {
                TrackingData trackingData = TrackingHandlerInterceptor.getThreadLocalTrackingData();
                if (trackingData != null) {
                    Map<String, TrackingDataPropertyDefinition> propertyDefinitions = TrackingDataFactory.getInstance().getPropertyDefinitions();
                    Set<String> trackingDataPropertyNames = new TreeSet<String>();
                    if (propertyDefinitions != null && propertyDefinitions.size() > 0) {
                        trackingDataPropertyNames.addAll(propertyDefinitions.keySet());
                    }

                    trackingDataPropertyNames.addAll(trackingData.getTrackingMap().keySet());
                    List<ChoiceListValue> listValues = new ArrayList<ChoiceListValue>();
                    for (String trackingDataPropertyName : trackingDataPropertyNames) {
                        listValues.add(new ChoiceListValue(trackingDataPropertyName, null,
                                node.getSession().getValueFactory().createValue(trackingDataPropertyName)));
                    }
                    return listValues;
                }
            } catch (RepositoryException e) {
                logger.error("Error while building list of tracking data property names", e);
            }
        }
        return new ArrayList<ChoiceListValue>();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
