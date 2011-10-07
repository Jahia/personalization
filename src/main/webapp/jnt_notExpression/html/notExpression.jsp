<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:choose>
    <c:when test="${renderContext.editMode}">
        <div style="border-width: thin; border-style: dashed">
            not
            <div style="padding-left: 20px">
                <template:module path="expression" />
            </div>
        </div>
        <c:set var="jahiaComponentExpression" value="${!jahiaComponentExpression}" scope="request"/>
    </c:when>
    <c:otherwise>
        <template:module path="expression" />
        <c:set var="jahiaComponentExpression" value="${!jahiaComponentExpression}" scope="request"/>
    </c:otherwise>
</c:choose>