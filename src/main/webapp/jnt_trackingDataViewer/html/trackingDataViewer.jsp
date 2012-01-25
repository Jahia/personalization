<%@ page import="net.sourceforge.wurfl.core.MarkUp" %>
<%@ page import="net.sourceforge.wurfl.core.Device" %>
<%@ page import="net.sourceforge.wurfl.core.WURFLManager" %>
<%@ page import="org.jahia.modules.personalization.tracking.TrackingData" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="org.jahia.utils.LanguageCodeConverters" %>
<%@ page import="org.jahia.services.usermanager.JahiaUser" %>
<%@ page import="java.util.*" %>
<%@ page import="org.jahia.services.usermanager.UserProperties" %>
<%@ page import="org.jahia.services.usermanager.UserProperty" %>
<%@ page import="java.net.URI" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<template:addResources type="css" resources="personalization.css"/>
<template:addResources type="javascript"
                       resources="jquery.min.js,jquery-ui.min.js,jquery.cuteTime.js,slidingpanel.js,http://maps.google.com/maps/api/js?sensor=true"/>

<c:set value="${sessionScope['org.jahia.modules.personalization.trackingData']}" var="trackingData"/>
<c:set value="${trackingData.trackingMap}" var="trackingMap"/>
<c:set value="${trackingMap.urls}" var="urls"/>
<c:set value="${trackingMap.locales}" var="locales"/>
<c:set value="${trackingMap.hosts}" var="hosts"/>

<%
    WURFLManager wurfl = (WURFLManager) pageContext.getServletContext().getAttribute("net.sourceforge.wurfl.core.WURFLHolder");
    TrackingData trackingData = (TrackingData) session.getAttribute("org.jahia.modules.personalization.trackingData");

%>
<c:choose>
<c:when test="${renderContext.editMode}">
    <div class="personalizationbox">
        <h4><fmt:message key="label.trackingDataViewer"/></h4>

        <p><fmt:message key="label.trackingDataViewer.componentDescription"/></p>
        <table>
            <tr>
                <th>Tracker</th>
                <th>Value</th>
            </tr>
            <c:forEach var="entry" items="${trackingMap}">
                <tr>
                    <td><c:out value="${entry.key}"/></td>
                    <td>
                        <c:forEach var="listEntry" items="${entry.value}">
                            <c:out value="${listEntry}"/> <br/>
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <%

            Device device = wurfl.getDeviceForRequest(request);

            out.println("Device: " + device.getId());
            out.println("Capability: " + device.getCapability("preferred_markup"));

            MarkUp markUp = device.getMarkUp();

            Map<String, String> capabilities = (Map<String, String>) device.getCapabilities();

            out.println("<table>");
            out.println("<tr><th>Name</th><th>Value</th></tr>");
            for (Map.Entry<String, String> capabilityEntry : capabilities.entrySet()) {
                out.println("<tr><td>" + capabilityEntry.getKey() + "</td><td>" + capabilityEntry.getValue() + "</td></tr>");
            }
            out.println("</table>");

        %>

    </div>
</c:when>

<c:otherwise>
<!-- display nothing -->
<div id="toppanel">
<div class="panel_button" style="display: block;"><img src="/modules/personalization/img/panel/expand.png"
                                                       alt="expand"/> <a href="#"></a></div>
<div class="panel_button" id="hide_button" style="display: none;"><img
        src="/modules/personalization/img/panel/collapse.png" alt="collapse"/> <a href="#"></a></div>
<div id="panel">
<div class="contents">

<%
    List<String> locales = trackingData.getTrackingMap().get("locales");
    if (locales != null) {
%>
<h2>Browser Languages</h2>
<%
        for (String localeStr : locales) {
            Locale locale = LanguageCodeConverters.languageCodeToLocale(localeStr);
            String language = locale.getDisplayLanguage(request.getLocale());
            String country = locale.getDisplayCountry(request.getLocale());
            String variant = locale.getDisplayVariant(request.getLocale());
            out.println("<p>" + language + " (" + country + ") " + variant + "</p>");
        }
    }
%>
<%

    long lastAccessTime = trackingData.getLong("lastAccessTime");
    if (lastAccessTime != -1) {
%>
<h2>Last access time</h2>
<%
        String dateString = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, request.getLocale()).format(new Date(lastAccessTime));
        out.println("<p>" + dateString + "</p>");
    }
%>
<%
    long lastPostTime = trackingData.getLong("lastPostMethodTime");
    if (lastPostTime != -1) {
%>
<h2>Last contribution time</h2>
<%
        String dateString = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, request.getLocale()).format(new Date(lastPostTime));
        out.println("<p>" + dateString + "</p>");
    }

    Map<String, List<String>> trackingMap = trackingData.getTrackingMap();
    List<String> userAgentList = trackingMap.get("user-agents");
    if (userAgentList != null) {
        out.println("<h2>Browsers</h2>");
        out.println("<table><tbody><tr>");
        for (String userAgent : userAgentList) {
            Device device = wurfl.getDeviceForRequest(userAgent);
            String brandName = device.getCapability("brand_name");
            out.println("<td style=\"text-align:center\"><img src=\"/modules/personalization/img/devices/" + brandName + ".png\"/><br/>" + brandName + "<td>");
        }
        out.println("</tr></tbody></table>");
    }

    List<String> locations = trackingData.getTrackingMap().get("locations");
    if (locations != null) {
%>
<h2>Locations</h2>

<div id="map_canvas" style="width:100%; height: 200px">
</div>
<%
    int locationCounter = 0;
    List<String[]> positionList = new ArrayList<String[]>();
    for (String location : locations) {
        locationCounter++;
        String[] position = location.split(",");
        positionList.add(position);
        pageContext.setAttribute("position", position);
        pageContext.setAttribute("locationCounter", locationCounter);
%>
<p>Location ${locationCounter} : ${position[0]}, ${position[1]}</p>
<script type="text/javascript">
    var latlng${locationCounter} = new google.maps.LatLng('${position[0]}', '${position[1]}');
</script>
<%
    }
%>
<script type="text/javascript">
    function initLocationMap() {
        var myOptions = {
            zoom: 15,
            center: latlng1,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

    <%
        locationCounter = 0;
            for (String[] position : positionList) {
                locationCounter++;
                pageContext.setAttribute("position", position);
                pageContext.setAttribute("locationCounter", locationCounter);
    %>

        var marker${locationCounter} = new google.maps.Marker({
            position: latlng${locationCounter},
            map: map,
            title:"Location ${locationCounter}"
        });
    <%
            }
    %>
    }
</script>
<%
    }

    List<String> screenResolutions = trackingData.getTrackingMap().get("screenResolution");
    if (screenResolutions != null) {
%>
<h2>Screen resolutions</h2>
<%
        out.println("<ul>");
        for (String resolution : screenResolutions) {
            out.println("<li>" + resolution + "</li>");
        }
        out.println("</ul>");
    }

    List<String> windowsSizes = trackingData.getTrackingMap().get("windowSize");
    if (windowsSizes != null) {
%>
<h2>Window sizes</h2>
<%
        out.println("<ul>");
        for (String windowSize : windowsSizes) {
            out.println("<li>" + windowSize + "</li>");
        }
        out.println("</ul>");
    }

    List<String> hosts = trackingData.getTrackingMap().get("hosts");
    if (hosts != null) {
%>
<h2>Source hosts</h2>
<%
        out.println("<ul>");
        for (String host : hosts) {
            out.println("<li>" + host + "</li>");
        }
        out.println("</ul>");
    }

    JahiaUser jahiaUser = (JahiaUser) pageContext.findAttribute("currentUser");
    UserProperties userProperties = jahiaUser.getUserProperties();
%>
<h2>User properties</h2>
<table>
    <tbody>
    <tr>
        <td style="text-align:center">
            <jcr:node var="userNode" path="${currentUser.localPath}"/>
            <jcr:nodeProperty var="picture" node="${userNode}" name="j:picture"/>
            <c:if test="${not empty picture}">
                <img class='user-profile-img userProfileImage' src="${picture.node.thumbnailUrls['avatar_120']}"
                     alt="${fn:escapeXml(title)} ${fn:escapeXml(firstname)} ${fn:escapeXml(lastname)}" width="60"
                     height="60"/>
            </c:if>
            <c:if test="${empty picture}">
                <img class='user-profile-img' src="<c:url value='${url.currentModule}/images/usersmall.png'/>" alt=""
                     border="0" width="32"
                     height="32"/>
            </c:if>
            <br/>
            <%=jahiaUser.getUsername()%>
        </td>
        <td><%=jahiaUser.getProperty("j:firstName")%> <%=jahiaUser.getProperty("j:lastName")%>
        </td>
    </tr>
    </tbody>
</table>
<%
    out.println("<table>");
    out.println("<tbody>");
    out.println("<tr><td>Organization</td><td>" + jahiaUser.getProperty("j:organization") + "</td></tr>");
    out.println("<tr><td>Email</td><td>" + jahiaUser.getProperty("j:email") + "</td></tr>");
    if (jahiaUser.getProperty("emailNotificationsDisabled") != null) {
        Boolean emailNotificationsDisabled = new Boolean(jahiaUser.getProperty("emailNotificationsDisabled"));
        out.println("<tr><td>Email notifications</td><td>" + !emailNotificationsDisabled.booleanValue() + "</td></tr>");
    }
    if (jahiaUser.getProperty("preferredLanguage") != null) {
        Locale locale = LanguageCodeConverters.languageCodeToLocale(jahiaUser.getProperty("preferredLanguage"));
        out.println("<tr><td>Preferred language</td><td>" + locale.getDisplayLanguage(request.getLocale()) + "</td></tr>");
    }
    out.println("</tbody></table>");
    Iterator<String> propertyNameIterator = userProperties.propertyNameIterator();
    out.println("<!--");
    while (propertyNameIterator.hasNext()) {
        UserProperty userProperty = userProperties.getUserProperty(propertyNameIterator.next());
        if (userProperty.getName().startsWith("jcr:")) {
        } else if (userProperty.getName().equals("j:originWS") ||
                userProperty.getName().equals("j:external") ||
                userProperty.getName().equals("j:published") ||
                userProperty.getName().equals("j:lastPublishedBy") ||
                userProperty.getName().equals("cookieauth")
                ) {
        } else {
            out.println("" + userProperty.getName() + ":" + userProperty.getValue());
        }
    }
    out.println("-->");

    List<String> urls = trackingData.getTrackingMap().get("urls");
    if (urls != null) {
%>
<h2>Referers</h2>
<%
        out.println("<ul>");
        for (String url : urls) {
            int colonPos = url.indexOf(":");
            if (colonPos > -1) {
                String referer = url.substring(colonPos + 1);
                URI refererURI = new URI(referer);
                out.println("<li><a href=\"" + referer + "\" title=\"" + referer + "\">" + refererURI.getHost() + "</a></li>");
            }
        }
        out.println("</ul>");
    }
%>
</div>
</div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $('#toppanel').appendTo(document.body);
        initLocationMap();
    });
</script>
</c:otherwise>
</c:choose>