<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<template:addResources type="css" resources="personalization.css"/>

<c:if test="${renderContext.editMode}">
<div class="personalizationbox">
<h4><fmt:message key="label.ifThenElse"/></h4>
    <p><fmt:message key="label.ifThenElse.componentDescription"/></p>
</div>
</c:if>

<c:choose>
    <c:when test="${renderContext.editMode}">
        If
        <div style="padding-left: 20px">
            <template:module path="condition" />
            ( = ${jahiaComponentExpression} )
        </div>
        then display
        <div style="padding-left: 20px">
            <template:module path="then" />
        </div>
        else display
        <div style="padding-left: 20px">
            <template:module path="else" />
        </div>
    </c:when>

    <c:otherwise>
        <template:module path="condition" />
        <c:choose>
            <c:when test="${jahiaComponentExpression}">
                <template:module path="then" />
            </c:when>
            <c:otherwise>
                <template:module path="else" />
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>