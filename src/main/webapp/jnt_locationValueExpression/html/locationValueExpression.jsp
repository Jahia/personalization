<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jcr:nodeProperty node="${currentNode}" name="j:latitude" var="latitude"/>
<jcr:nodeProperty node="${currentNode}" name="j:longitude" var="longitude"/>
<c:set var="jahiaComponentExpression" value="${latitude.double},${longitude.double}" scope="request" />
<c:if test="${renderContext.editMode}">Location (${jahiaComponentExpression})</c:if>