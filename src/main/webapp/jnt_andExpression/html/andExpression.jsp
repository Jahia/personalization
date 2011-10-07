<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:choose>
    <c:when test="${renderContext.editMode}">
        <div style="border-width: thin; border-style: dashed">
        <div style="padding-left: 20px">
            <template:module path="left" /><c:set var="leftJahiaComponentExpression" value="${jahiaComponentExpression}" />
        </div>
        and
        <div style="padding-left: 20px">
            <template:module path="right" /><c:set var="rightJahiaComponentExpression" value="${jahiaComponentExpression}" />
        </div>
        </div>
        <c:set var="jahiaComponentExpression" value="${leftJahiaComponentExpression && rightJahiaComponentExpression}" scope="request"/>
    </c:when>
    <c:otherwise>
        <template:module path="left" />
        <c:set var="leftJahiaComponentExpression" value="${jahiaComponentExpression}" />
        <template:module path="right" />
        <c:set var="rightJahiaComponentExpression" value="${jahiaComponentExpression}" />
        <c:set var="jahiaComponentExpression" value="${leftJahiaComponentExpression && rightJahiaComponentExpression}" scope="request"/>
    </c:otherwise>
</c:choose>