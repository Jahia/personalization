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
 * Basic action to remove a user property
 */
public class RemoveUserProperty extends Action {

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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(propertyName, propertyValue);

        boolean successful = user.removeProperty(propertyName);
        if (!successful) {
            return new ActionResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, null, jsonObject);
        }

        return new ActionResult(HttpServletResponse.SC_ACCEPTED,null, jsonObject);
    }
}