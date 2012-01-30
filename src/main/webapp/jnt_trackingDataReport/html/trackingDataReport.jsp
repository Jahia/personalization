<%@ page import="org.jahia.modules.personalization.tracking.reports.AccumulatedTrackingData" %>
<%@ page import="org.jahia.services.content.JCRNodeWrapper" %>
<%@ page import="org.jahia.modules.personalization.tracking.TrackingData" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.jahia.modules.personalization.tracking.TrackingDataFactory" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="query" uri="http://www.jahia.org/tags/queryLib" %>
<%@ taglib prefix="facet" uri="http://www.jahia.org/tags/facetLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>

<jcr:sql var="lastTrackingData"
         sql="select * from [jnt:trackingData] as trackingData where ISDESCENDANTNODE(trackingData,'/tracking') order by trackingData.[date] desc"
         limit="100"/>

<c:set var="totalNodes" value="${functions:length(lastTrackingData.nodes)}" />
<c:set var="anonymousCount" value="0" />
<c:set var="authentifiedCount" value="0" />
<jsp:useBean id="accumulatedTrackingData" class="org.jahia.modules.personalization.tracking.reports.AccumulatedTrackingData" />
<c:forEach items="${lastTrackingData.nodes}" var="curNode" varStatus="listStatus">
    <jcr:nodeProperty node="${curNode}" name="j:associatedUserKey" var="associatedUserKey"/>
    <c:choose>
        <c:when test="${not empty associatedUserKey}">
            <c:set var="authentifiedCount" value="${authentifiedCount + 1}" />
        </c:when>
        <c:otherwise>
            <c:set var="anonymousCount" value="${anonymousCount + 1}" />
        </c:otherwise>
    </c:choose>
    <%
        JCRNodeWrapper curNode = (JCRNodeWrapper) pageContext.findAttribute("curNode");
        TrackingData trackingData = TrackingDataFactory.getInstance().getTrackingData(curNode);

        accumulatedTrackingData.accumulateForKey("browsers", trackingData.getTrackingMap().get("browser"));
        accumulatedTrackingData.accumulateForKey("operatingSystems", trackingData.getTrackingMap().get("operating-system"));
        accumulatedTrackingData.accumulateForKey("screenResolutions", trackingData.getTrackingMap().get("screenResolution"));
        accumulatedTrackingData.accumulateForKey("windowSizes", trackingData.getTrackingMap().get("windowSize"));
        accumulatedTrackingData.accumulateForKey("locations", trackingData.getTrackingMap().get("locations"));

    %>
</c:forEach>
<h2>Tracking statistics</h2>
<h3>Summary</h3>
<table width="100%">
    <tbody>
    <tr>
        <td>Number of visitors tracked</td><td style="text-align:right">${totalNodes} </td>
    </tr>
    <tr>
        <td>Number of anonymous visitors tracked</td><td style="text-align:right">${anonymousCount}</td>
    </tr>
    <tr>
        <td>Number of authentified visitors tracked</td><td style="text-align:right">${authentifiedCount}</td>
    </tr>
</table>
<h3>Browsers</h3>
<table width="100%">
    <tbody>
        <c:forEach var="browserEntry" items="${accumulatedTrackingData.accumulatedData['browsers']}">
            <tr>
                <td><c:out value="${browserEntry.key}" /></td>
                <td style="text-align:right"><c:out value="${browserEntry.value}" /></td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<h3>Operating systems</h3>
<table width="100%">
    <tbody>
        <c:forEach var="osEntry" items="${accumulatedTrackingData.accumulatedData['operatingSystems']}">
            <tr>
                <td><c:out value="${osEntry.key}" /></td>
                <td style="text-align:right"><c:out value="${osEntry.value}" /></td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<h3>Screen resolutions</h3>
<table width="100%">
    <tbody>
        <c:forEach var="screenResolutionEntry" items="${accumulatedTrackingData.accumulatedData['screenResolutions']}">
            <tr>
                <td><c:out value="${screenResolutionEntry.key}" /></td>
                <td style="text-align:right"><c:out value="${screenResolutionEntry.value}" /></td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<h3>Window sizes</h3>
<table width="100%">
    <tbody>
        <c:forEach var="windowSizeEntry" items="${accumulatedTrackingData.accumulatedData['windowSizes']}">
            <tr>
                <td><c:out value="${windowSizeEntry.key}" /></td>
                <td style="text-align:right"><c:out value="${windowSizeEntry.value}" /></td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<h3>Locations</h3>
<div id="map_canvas${currentNode.identifier}" style="width:100%; height: 200px">
</div>
<script type="text/javascript">

    function initReportMap() {
<c:forEach var="locationEntry" items="${accumulatedTrackingData.accumulatedData['locations']}" varStatus="listStatus">
        <c:set var="position" value="${fn:split(locationEntry.key, ',')}" />
        var latlng${listStatus.count} = new google.maps.LatLng('${position[0]}', '${position[1]}');
</c:forEach>
        var myOptions = {
            zoom: 15,
            center: latlng1,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        var map = new google.maps.Map(document.getElementById("map_canvas${currentNode.identifier}"), myOptions);

<c:forEach var="locationEntry" items="${accumulatedTrackingData.accumulatedData['locations']}" varStatus="listStatus">
        var marker${listStatus.count} = new google.maps.Marker({
            position: latlng${listStatus.count},
            map: map,
            title:"Location ${listStatus.count}"
        });
</c:forEach>
    }
    $(document).ready(function() {
        initReportMap();
    });
</script>

<!--
<c:forEach items="${lastTrackingData.nodes}" var="curNode" varStatus="listStatus">
    <template:module node="${curNode}" />
</c:forEach>
-->