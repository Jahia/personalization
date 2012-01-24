<%@ page import="net.sourceforge.wurfl.core.MarkUp" %>
<%@ page import="net.sourceforge.wurfl.core.Device" %>
<%@ page import="net.sourceforge.wurfl.core.WURFLManager" %>
<%@ page import="org.jahia.modules.personalization.tracking.TrackingData" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="org.jahia.utils.LanguageCodeConverters" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<template:addResources type="css" resources="personalization.css"/>
<template:addResources type="javascript"
                       resources="jquery.min.js,jquery-ui.min.js,jquery.cuteTime.js,slidingpanel.js,http://maps.google.com/maps/api/js?sensor=true"/>

<c:set value="${sessionScope['org.jahia.modules.personalization.trackingData']}" var="trackingData" />
<c:set value="${trackingData.trackingMap}" var="trackingMap" />
<c:set value="${trackingMap.urls}" var="urls" />
<c:set value="${trackingMap.locales}" var="locales" />
<c:set value="${trackingMap.hosts}" var="hosts" />

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
                              <c:out value="${listEntry}" /> <br/>
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

                    Map<String,String> capabilities = (Map<String,String>) device.getCapabilities();

                    out.println("<table>");
                    out.println("<tr><th>Name</th><th>Value</th></tr>");
                    for (Map.Entry<String,String> capabilityEntry : capabilities.entrySet()) {
                        out.println("<tr><td>" + capabilityEntry.getKey() + "</td><td>" + capabilityEntry.getValue() + "</td></tr>");
                    }
                    out.println("</table>");

            %>

        </div>
    </c:when>

    <c:otherwise>
        <!-- display nothing -->
        <div id="toppanel">
          <div id="panel">
            <div id="panel_contents">

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
            Map<String,List<String>> trackingMap = trackingData.getTrackingMap();
            List<String> userAgentList = trackingMap.get("user-agents");
            if (userAgentList != null) {
                out.println("<h2>Browsers</h2>");
                for (String userAgent : userAgentList) {
                    Device device = wurfl.getDeviceForRequest(userAgent);
                    String brandName = device.getCapability("brand_name");
                    out.println("<img src=\"/modules/personalization/img/devices/"+brandName+".png\"/>"+brandName);
                }
            }

            List<String> locations = trackingData.getTrackingMap().get("locations");
            if (locations != null) {
        %>
              <h2>Locations</h2>
              <div id="map_canvas">
              </div>
        <%
                int locationCounter=0;
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
                  $(document).ready(function() {
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
                  });
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
        %>
                </div>
          </div>
          <div class="panel_button" style="display: visible;"><img src="/modules/personalization/img/panel/expand.png"  alt="expand"/> <a href="#">Tracking data</a> </div>
          <div class="panel_button" id="hide_button" style="display: none;"><img src="/modules/personalization/img/panel/collapse.png" alt="collapse" /> <a href="#">Hide</a> </div>
        </div>
    </c:otherwise>
</c:choose>