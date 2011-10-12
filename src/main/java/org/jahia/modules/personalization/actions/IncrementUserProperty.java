package org.jahia.modules.personalization.actions;

import org.apache.commons.lang.StringUtils;
import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.jahia.services.usermanager.JahiaUser;
import org.jahia.services.usermanager.JahiaUserManagerService;
import org.json.JSONObject;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Basic action to increment a user property
 */
public class IncrementUserProperty extends Action {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(IncrementUserProperty.class);
    private JahiaUserManagerService userManagerService;

    public void setUserManagerService(JahiaUserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

    @Override
    public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, Resource resource, JCRSessionWrapper session, Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {
        String propertyName = getParameter(parameters, "propertyName");

        if (StringUtils.isEmpty(propertyName)) {
            return ActionResult.BAD_REQUEST;
        }

        JahiaUser user = renderContext.getUser();

        if (user == null) {
            return new ActionResult(HttpServletResponse.SC_NOT_FOUND, null, new JSONObject());
        }

        String propertyValue = user.getProperty(propertyName);
        if (propertyValue == null) {
            propertyValue = "0";
        }
        Long longValue = null;
        try {
            longValue = new Long(propertyValue);
            longValue += 1L;
            propertyValue = longValue.toString();

            user.setProperty(propertyName, propertyValue);
        } catch (NumberFormatException nfe) {
            logger.error("Invalid property value " + propertyValue + " for property " + propertyName + ", expected number. Will not modify it.");
            return new ActionResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,null, new JSONObject());
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(propertyName, propertyValue);
        return new ActionResult(HttpServletResponse.SC_ACCEPTED,null, jsonObject);

    }
}