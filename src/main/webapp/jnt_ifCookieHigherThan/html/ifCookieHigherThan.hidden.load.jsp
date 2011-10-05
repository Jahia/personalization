<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jcr:nodeProperty node="${currentNode}" name="j:cookieName" var="cookieName"/>
<jcr:nodeProperty node="${currentNode}" name="j:cookieValue" var="cookieValue"/>

<template:addResources type="css" resources="personalization.css"/>

<c:if test="${renderContext.editMode}">
<div class="personalizationbox">
<h4><fmt:message key="label.ifCookieHigherThanArea"/></h4>
    <p><fmt:message key="label.ifCookieHigherThan.componentDescription"/></p>
</div>
</c:if>

<c:choose>
    <c:when test="${renderContext.editMode}">
        If cookie ${cookieName.string} is higher than ${cookieValue.long} then display
        <c:set var="facetParamVarName" value="N-${currentNode.name}"/>
        <%-- list mode --%>
        <c:choose>
            <c:when test="${not empty param[facetParamVarName]}">
                <query:definition var="listQuery" >
                    <query:selector nodeTypeName="nt:base"/>
                    <c:set var="descendantNode" value="${fn:substringAfter(currentNode.realNode.path,'/sites/')}"/>
                    <c:set var="descendantNode" value="${fn:substringAfter(descendantNode,'/')}"/>
                    <query:descendantNode path="/sites/${renderContext.site.name}/${descendantNode}"/>
                </query:definition>
                <c:set target="${moduleMap}" property="listQuery" value="${listQuery}"/>
            </c:when>
            <c:otherwise>
                <c:set target="${moduleMap}" property="editable" value="true" />
                <c:set target="${moduleMap}" property="currentList" value="${jcr:getChildrenOfType(currentNode, jcr:getConstraints(currentNode))}" />
                <c:set target="${moduleMap}" property="end" value="${fn:length(moduleMap.currentList)}" />
                <c:set target="${moduleMap}" property="listTotalSize" value="${moduleMap.end}" />
            </c:otherwise>
        </c:choose>
    </c:when>

    <c:otherwise>
        <c:if test="${cookie[cookieName.string].value > cookieValue.long}">
            <c:set var="facetParamVarName" value="N-${currentNode.name}"/>
            <%-- list mode --%>
            <c:choose>
                <c:when test="${not empty param[facetParamVarName]}">
                    <query:definition var="listQuery" >
                        <query:selector nodeTypeName="nt:base"/>
                        <c:set var="descendantNode" value="${fn:substringAfter(currentNode.realNode.path,'/sites/')}"/>
                        <c:set var="descendantNode" value="${fn:substringAfter(descendantNode,'/')}"/>
                        <query:descendantNode path="/sites/${renderContext.site.name}/${descendantNode}"/>
                    </query:definition>
                    <c:set target="${moduleMap}" property="listQuery" value="${listQuery}"/>
                </c:when>
                <c:otherwise>
                    <c:set target="${moduleMap}" property="editable" value="true" />
                    <c:set target="${moduleMap}" property="currentList" value="${jcr:getChildrenOfType(currentNode, jcr:getConstraints(currentNode))}" />
                    <c:set target="${moduleMap}" property="end" value="${fn:length(moduleMap.currentList)}" />
                    <c:set target="${moduleMap}" property="listTotalSize" value="${moduleMap.end}" />
                </c:otherwise>
            </c:choose>
        </c:if>
    </c:otherwise>
</c:choose>