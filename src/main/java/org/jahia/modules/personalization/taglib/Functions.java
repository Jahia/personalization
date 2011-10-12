package org.jahia.modules.personalization.taglib;

import org.jahia.services.usermanager.JahiaUser;
import org.slf4j.Logger;

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

    public static double geoDistanceInMiles(double latitude1, double longitude1, double latitude2, double longitude2) {
        double earthRadius = 3958.75;
        return geoDistanceWithRadius(latitude1, longitude1, latitude2, longitude2, earthRadius);
    }

    public static double geoDistanceInKilometers(double latitude1, double longitude1, double latitude2, double longitude2) {
        double earthRadius = 6371;
        return geoDistanceWithRadius(latitude1, longitude1, latitude2, longitude2, earthRadius);
    }

    public static double geoDistanceWithRadius(double latitude1, double longitude1, double latitude2, double longitude2, double earthRadius) {
        double dLat = Math.toRadians(latitude2-latitude1);
        double dLng = Math.toRadians(longitude2-longitude1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
                   Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }
}
