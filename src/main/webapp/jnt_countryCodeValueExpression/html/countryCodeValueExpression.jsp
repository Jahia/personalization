<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jcr:nodeProperty node="${currentNode}" name="j:value" var="countryCodeValue"/>
<c:set var="jahiaComponentExpression" value="${countryCodeValue.string}" scope="request" />
<c:if test="${renderContext.editMode}">${jahiaComponentExpression}</c:if>