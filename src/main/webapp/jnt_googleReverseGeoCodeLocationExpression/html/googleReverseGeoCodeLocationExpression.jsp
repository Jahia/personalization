<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.apache.commons.jxpath.servlet.PageContextHandler" %>
<%@ page import="javax.jcr.Value" %>
<%@ page import="javax.jcr.RepositoryException" %>
<%@ page import="org.json.JSONException" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="propertyDefinition" type="org.jahia.services.content.nodetypes.ExtendedPropertyDefinition"--%>
<%--@elvariable id="type" type="org.jahia.services.content.nodetypes.ExtendedNodeType"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<jcr:nodeProperty node="${currentNode}" name="j:addressComponent" var="addressComponentType"/>
<c:choose>
    <c:when test="${renderContext.editMode}">
        <div style="border-width: thin; border-style: dashed">
        Reverse geocode location using Google Maps API
        <div style="padding-left: 20px">
            <template:module path="location" />
            <c:import url="http://maps.googleapis.com/maps/api/geocode/json?latlng=${jahiaComponentExpression}&sensor=false" var="reverseGeoCodeResult"/>
            <% extractJSONResult(pageContext, out, "reverseGeoCodeResult", "addressComponentType", "jahiaComponentExpression"); %>
        </div>
    </c:when>
    <c:otherwise>
        <template:module path="location" />
        <c:import url="http://maps.googleapis.com/maps/api/geocode/json?latlng=${jahiaComponentExpression}&sensor=false" var="reverseGeoCodeResult"/>
        <% extractJSONResult(pageContext, out, "reverseGeoCodeResult", "addressComponentType", "jahiaComponentExpression"); %>
    </c:otherwise>
</c:choose><%!
    private void extractJSONResult(PageContext pageContext,
                                   JspWriter out,
                                   String reverseGeoCodeResultVarName,
                                   String addressComponentTypeVarName,
                                   String targetValueVarName) throws RepositoryException, JSONException {
        String reverseGeoCodeResult = (String) pageContext.findAttribute(reverseGeoCodeResultVarName);
        String addressComponentType = ((Value) pageContext.findAttribute(addressComponentTypeVarName)).getString();
        JSONObject jsonObject = new JSONObject(reverseGeoCodeResult);
        JSONArray jsonResults = jsonObject.getJSONArray("results");
        if (jsonResults.length() > 0) {
            JSONObject result = jsonResults.getJSONObject(0);
            JSONArray addressComponents = result.getJSONArray("address_components");
            String addressComponentValue = null;
            for (int i=0; i < addressComponents.length(); i++) {
                JSONObject addressComponent = addressComponents.getJSONObject(i);
                JSONArray types = addressComponent.getJSONArray("types");
                for (int j=0; j < types.length(); j++) {
                    String type = types.getString(j);
                    if (type.equals(addressComponentType)) {
                        addressComponentValue = addressComponent.getString("short_name");
                        pageContext.setAttribute(targetValueVarName, addressComponentValue, PageContext.REQUEST_SCOPE);
                        break;
                    }
                }
                if (addressComponentValue != null) {
                    break;
                }
            }
        }
    }
%>