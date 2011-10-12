<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jpfn" uri="http://www.jahia.org/tags/personalization-functions" %>
<c:choose>
    <c:when test="${renderContext.editMode}">
        <div style="border-width: thin; border-style: dashed">
        DISTANCE BETWEEN (km)
        <div style="padding-left: 20px">
            <template:module path="location1" /><c:set var="location1" value="${fn:split(jahiaComponentExpression, ',')}" />
        </div>
        AND
        <div style="padding-left: 20px">
            <template:module path="location2" /><c:set var="location2" value="${fn:split(jahiaComponentExpression, ',')}" />
        </div>
        </div>
        <c:set var="jahiaComponentExpression" value="${jpfn:geoDistanceInKilometers(location1[0], location1[1], location2[0], location2[1])}" scope="request"/>
    </c:when>
    <c:otherwise>
        <template:module path="location1" />
        <c:set var="location1" value="${fn:split(jahiaComponentExpression, ',')}" />
        <template:module path="location2" />
        <c:set var="location2" value="${fn:split(jahiaComponentExpression, ',')}" />
        <c:set var="jahiaComponentExpression" value="${jpfn:geoDistanceInKilometers(location1[0], location1[1], location2[0], location2[1])}" scope="request"/>
    </c:otherwise>
</c:choose>