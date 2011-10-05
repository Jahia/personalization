<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<template:addResources type="javascript" resources="jquery.min.js,jquery-ui.min.js,jquery.cookie.js,cookies.js"/>

<c:if test="${renderContext.editMode}"><h4><fmt:message key="label.incrementCookieArea"/></h4>
    <p><fmt:message key="label.incrementCookie.componentDescription"/></p>
</c:if>

<script type="text/javascript">
    incrementCookie('<jcr:nodeProperty node="${currentNode}" name="j:cookieName"/>');
</script>