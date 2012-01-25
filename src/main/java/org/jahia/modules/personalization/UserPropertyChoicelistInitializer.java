package org.jahia.modules.personalization;

import org.apache.log4j.Logger;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.nodetypes.ExtendedPropertyDefinition;
import org.jahia.services.content.nodetypes.initializers.ChoiceListValue;
import org.jahia.services.content.nodetypes.initializers.ModuleChoiceListInitializer;
import org.jahia.services.usermanager.JahiaUser;
import org.jahia.services.usermanager.UserProperties;

import javax.jcr.RepositoryException;
import java.util.*;

/**
 * An initializer to select user property names
 */
public class UserPropertyChoicelistInitializer implements ModuleChoiceListInitializer {

    private transient static Logger logger = Logger.getLogger(UserPropertyChoicelistInitializer.class);
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
                JahiaUser jahiaUser = node.getSession().getUser();
                List<ChoiceListValue> listValues = new ArrayList<ChoiceListValue>();
                UserProperties userProperties = jahiaUser.getUserProperties();
                Iterator<String> userPropertyNameIterator = userProperties.propertyNameIterator();
                while (userPropertyNameIterator.hasNext()) {
                    String userPropertyName = userPropertyNameIterator.next();
                    listValues.add(new ChoiceListValue(userPropertyName, null,
                            node.getSession().getValueFactory().createValue(userPropertyName)));
                }
                return listValues;
            } catch (RepositoryException e) {
                logger.error("Error while building list of user property names", e);
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
