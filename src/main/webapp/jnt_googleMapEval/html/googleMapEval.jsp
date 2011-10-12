<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<template:addResources type="css" resources="personalization.css"/>
<template:addResources type="javascript"
                       resources="jquery.min.js,jquery-ui.min.js,jquery.cuteTime.js,http://maps.google.com/maps/api/js?sensor=true"/>
<c:choose>
    <c:when test="${renderContext.editMode}">
        <div class="personalizationbox">
            <h4><fmt:message key="label.googleMapEval"/></h4>

            <p><fmt:message key="label.googleMapEval.componentDescription"/></p>
            <strong><fmt:message key="label.googleMapEval.displayMap"/></strong>

            <div style="padding-left: 20px">
                <template:module path="expression"/>
                ( = ${jahiaComponentExpression} )
            </div>
        </div>
    </c:when>

    <c:otherwise>
        <template:module path="expression"/>
        <c:set var="position" value="${fn:split(jahiaComponentExpression, ',')}"/>
        <div id="map_canvas">
        </div>
        <script type="text/javascript">
            $(document).ready(function() {
                var latlng = new google.maps.LatLng('${position[0]}', '${position[1]}');
                var myOptions = {
                    zoom: 15,
                    center: latlng,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

                var marker = new google.maps.Marker({
                    position: latlng,
                    map: map,
                    title:"You are here!"
                });
            });
        </script>
    </c:otherwise>
</c:choose>
