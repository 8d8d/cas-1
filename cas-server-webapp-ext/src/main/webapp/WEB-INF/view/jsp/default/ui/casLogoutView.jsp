<%@ page import="com.gzzq.config.*" %>
<jsp:directive.include file="includes/top.jsp" />
  <div id="msg" class="success">
    <h2><spring:message code="screen.logout.header" /></h2>
    <p><spring:message code="screen.logout.success" /></p>
    <p><spring:message code="screen.logout.security" /></p>
  </div>
<% 
   String service=request.getParameter("service");
   if(service==null||"".equals(service)){
		response.sendRedirect(WebConfigHelper.getLoginDefaultRedirectUrl());
   }
%>
<jsp:directive.include file="includes/bottom.jsp" />