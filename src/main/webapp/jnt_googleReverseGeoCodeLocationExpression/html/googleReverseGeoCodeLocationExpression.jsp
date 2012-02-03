<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jpfn" uri="http://www.jahia.org/tags/personalization-functions" %>
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

<c:choose>
    <c:when test="${renderContext.editMode}">
        <div style="border-width: thin; border-style: dashed">
        Reverse geocode location using Google Maps API
        <div style="padding-left: 20px">
            <template:module path="location" />
            <c:import url="http://maps.googleapis.com/maps/api/geocode/json?latlng=${jahiaComponentExpression}&sensor=true_or_false" var="reverseGeoCodeResult"/>
            <c:out value="${reverseGeoCodeResult}" />
            <%
                String reverseGeoCodeResult = (String) pageContext.findAttribute("reverseGeoCodeResult");
            %>
        </div>
        <c:set var="jahiaComponentExpression" value="${reverseGeoCodeResult}" scope="request"/>
    </c:when>
    <c:otherwise>
        <template:module path="location" />
        <c:import url="http://maps.googleapis.com/maps/api/geocode/json?latlng=${jahiaComponentExpression}&sensor=true_or_false" var="reverseGeoCodeResult"/>
        <c:set var="jahiaComponentExpression" value="${reverseGeoCodeResult}" scope="request"/>
    </c:otherwise>
</c:choose>