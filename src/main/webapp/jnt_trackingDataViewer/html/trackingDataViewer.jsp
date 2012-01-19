<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<template:addResources type="css" resources="personalization.css"/>

<c:set value="${sessionScope['org.jahia.modules.personalization.trackingData']}" var="trackingData" />
<c:set value="${trackingData.trackingMap}" var="trackingMap" />
<c:set value="${trackingMap.urls}" var="urls" />
<c:set value="${trackingMap.locales}" var="locales" />
<c:set value="${trackingMap.locales}" var="hosts" />

<c:choose>
    <c:when test="${renderContext.editMode}">
        <div class="personalizationbox">
            <h4><fmt:message key="label.trackingData"/></h4>
                <p><fmt:message key="label.trackingData.componentDescription"/></p>
            <table>
                <tr>
                    <th>Tracker</th>
                    <th>Value</th>
                </tr>
                <tr>
                    <td>URLs</td><td><c:out value="${urls}"/></td>
                    <td>Locale</td><td><c:out value="${locales}"/></td>
                    <td>Hosts</td><td><c:out value="${hosts}"/></td>
                </tr>
            </table>
        </div>
    </c:when>

    <c:otherwise>
        <!-- display nothing -->
    </c:otherwise>
</c:choose>