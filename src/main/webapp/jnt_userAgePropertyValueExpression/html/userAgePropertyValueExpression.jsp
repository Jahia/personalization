<%@ page import="java.util.Calendar" %>
<%@ page import="org.apache.jackrabbit.util.ISO8601" %>
<%@ page import="org.jahia.services.usermanager.JahiaUser" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%

    JahiaUser jahiaUser = (JahiaUser) pageContext.findAttribute("currentUser");
    Integer userAge = 0;
    if (jahiaUser.getProperty("age") != null) {
        userAge = Integer.parseInt(jahiaUser.getProperty("age"));
    } else if (jahiaUser.getProperty("j:birthDate") != null) {
        Calendar calendar = ISO8601.parse(jahiaUser.getProperty("j:birthDate"));
        Calendar nowCalendar = Calendar.getInstance();
        int years = -1;
        while (!calendar.after(nowCalendar)) {
            calendar.add(Calendar.YEAR, 1);
            years++;
        }
        userAge = years;
    }
    pageContext.setAttribute("userAge", userAge);
%>
<c:set var="jahiaComponentExpression" value="${userAge}" scope="request"/>
<c:if test="${renderContext.editMode}">User age = ${jahiaComponentExpression}</c:if>