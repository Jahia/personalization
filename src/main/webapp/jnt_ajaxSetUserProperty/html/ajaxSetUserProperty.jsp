<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="uiComponents" uri="http://www.jahia.org/tags/uiComponentsLib" %>
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
<template:addResources type="css" resources="personalization.css"/>
<template:addResources type="javascript" resources="jquery.min.js,jquery.cuteTime.js"/>
<jcr:nodeProperty node="${currentNode}" name="j:propertyName" var="propertyName"/>
<jcr:nodeProperty node="${currentNode}" name="j:propertyValue" var="propertyValue"/>

<c:choose>
    <c:when test="${renderContext.editMode}">
        <div class="personalizationbox">
            <h4><fmt:message key="label.ajaxSetUserProperty"/></h4>
            <p><fmt:message key="label.ajaxSetUserProperty.componentDescription"/></p>
            <fmt:message key="label.ajaxSetUserProperty.willSetPropertyToValue">
                <fmt:param value="${propertyName.string}" />
                <fmt:param value="${propertyValue.string}" />
            </fmt:message>
        </div>
    </c:when>

    <c:otherwise>
        <script type="text/javascript">
            $(document).ready(function() {
                $.post('<c:url value="${url.base}${currentNode.path}.setUserProperty.do"/>', { propertyName: '${propertyName.string}', propertyValue: '${propertyValue.string}'});
            });
        </script>
    </c:otherwise>
</c:choose>