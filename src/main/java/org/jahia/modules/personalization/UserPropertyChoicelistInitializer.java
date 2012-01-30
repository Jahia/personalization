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

    private Set<String> selectableProperties = new HashSet<String>();
    private Set<String> hiddenProperties = new HashSet<String>();

    public void setSelectableProperties(Set<String> selectableProperties) {
        this.selectableProperties = selectableProperties;
    }

    public void setHiddenProperties(Set<String> hiddenProperties) {
        this.hiddenProperties = hiddenProperties;
    }

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
                UserProperties userProperties = jahiaUser.getUserProperties();
                Iterator<String> userPropertyNameIterator = userProperties.propertyNameIterator();
                Set<String> userPropertyNames = new TreeSet<String>();
                userPropertyNames.addAll(selectableProperties);
                while (userPropertyNameIterator.hasNext()) {
                    String userPropertyName = userPropertyNameIterator.next();
                    if (!hiddenProperties.contains(userPropertyName) &&
                        !userPropertyNames.contains(userPropertyName)) {
                        userPropertyNames.add(userPropertyName);
                    }
                }
                List<ChoiceListValue> listValues = new ArrayList<ChoiceListValue>();
                for (String userPropertyName : userPropertyNames) {
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
