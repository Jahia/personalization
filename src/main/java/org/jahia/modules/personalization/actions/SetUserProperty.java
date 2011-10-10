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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Basic action to a user's property.
 */
public class SetUserProperty extends Action {

    private JahiaUserManagerService userManagerService;

    public void setUserManagerService(JahiaUserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

    @Override
    public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, Resource resource, JCRSessionWrapper session, Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {
        String propertyName = getParameter(parameters, "propertyName");
        String propertyValue = getParameter(parameters, "propertyValue");
        if (StringUtils.isEmpty(propertyName) || StringUtils.isEmpty(propertyValue)) {
            return ActionResult.BAD_REQUEST;
        }

        JahiaUser user = renderContext.getUser();
        if (getParameter(parameters, "user") != null) {
            user = userManagerService.lookupUser(getParameter(parameters, "user"));
        }

        if (user == null) {
            return new ActionResult(HttpServletResponse.SC_NOT_FOUND, null, new JSONObject());
        }

        user.setProperty(propertyName, propertyValue);

        JSONObject jsonObject = new JSONObject();
        for (String curPropertyName : user.getProperties().stringPropertyNames()) {
            jsonObject.put(curPropertyName, user.getProperty(curPropertyName));
        }

        return new ActionResult(HttpServletResponse.SC_ACCEPTED,null, jsonObject);
    }
}
