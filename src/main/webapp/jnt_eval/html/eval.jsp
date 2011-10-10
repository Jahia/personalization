<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<template:addResources type="css" resources="personalization.css"/>
<c:choose>
    <c:when test="${renderContext.editMode}">
        <div class="personalizationbox">
            <h4><fmt:message key="label.eval"/></h4>
                <p><fmt:message key="label.eval.componentDescription"/></p>
            <strong><fmt:message key="label.eval.evaluate"/></strong>
            <div style="padding-left: 20px">
                <template:module path="expression" />
                ( = ${jahiaComponentExpression} )
            </div>
        </div>
    </c:when>

    <c:otherwise>
        <template:module path="expression" />
        <c:out value="${jahiaComponentExpression}" />
    </c:otherwise>
</c:choose>