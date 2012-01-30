<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jcr:nodeProperty node="${currentNode}" name="j:clientId" var="clientId"/>
<jcr:nodeProperty node="${currentNode}" name="j:associatedUserKey" var="associatedUserKey"/>

Client ID : <c:out value="${clientId.string}" />
Associated User Key : <c:out value="${associatedUserKey.string}" />

