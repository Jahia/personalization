<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set value="${sessionScope['org.jahia.modules.personalization.trackingData']}" var="trackingData"/>
<jcr:nodeProperty node="${currentNode}" name="j:propertyName" var="propertyName"/>
<c:set var="trackingList" value="${trackingData.trackingMap[propertyName.string]}" />
<c:if test="${not empty trackingList}">
    <c:set var="jahiaComponentExpression" value="${trackingList[fn:length(trackingList)-1]}" scope="request" />
</c:if>
<c:if test="${renderContext.editMode}">Tracking data ${propertyName.string} = ${jahiaComponentExpression}</c:if>