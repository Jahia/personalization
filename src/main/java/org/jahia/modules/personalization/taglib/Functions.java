package org.jahia.modules.personalization.taglib;

import org.jahia.bin.ActionResult;
import org.jahia.services.usermanager.JahiaUser;
import org.json.JSONObject;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletResponse;

/**
 * Personalization EL functions
 */
public class Functions {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(Functions.class);

    public static boolean setUserProperty(JahiaUser jahiaUser, String propertyName, String propertyValue) {
        return jahiaUser.setProperty(propertyName, propertyValue);
    }

    public static boolean removeUserProperty(JahiaUser jahiaUser, String propertyName) {
        return jahiaUser.removeProperty(propertyName);
    }

    public static Long incrementUserProperty(JahiaUser jahiaUser, String propertyName) {
        String propertyValue = jahiaUser.getProperty(propertyName);
        if (propertyValue == null) {
            propertyValue = "0";
        }
        Long longValue = null;
        try {
            longValue = new Long(propertyValue);
            longValue += 1L;
            propertyValue = longValue.toString();

            jahiaUser.setProperty(propertyName, propertyValue);
            return longValue;
        } catch (NumberFormatException nfe) {
            logger.error("Invalid property value " + propertyValue + " for property " + propertyName + ", expected number. Will not modify it.");
            throw nfe;
        }
    }
}
