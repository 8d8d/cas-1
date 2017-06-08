<%@ page import="org.jasig.cas.authentication.principal.Principal,
		java.io.PrintWriter,
		com.gzzq.config.*" %>
  
<%
String callbackName = request.getParameter("callback");
if(callbackName==null){ 
	//不带service参数时，会自动跳回主页
   String service=request.getParameter("service");
   if(service==null||"".equals(service)){
		response.sendRedirect(WebConfigHelper.getLoginDefaultRedirectUrl());
   }
%>	
<jsp:directive.include file="includes/top.jsp" />
  <div id="msg" class="success">
    <h2><spring:message code="screen.success.header" /></h2>
    <p><spring:message code="screen.success.success" arguments="${principal.id}"/></p>
    <p><spring:message code="screen.success.security" /></p>
  </div>
<jsp:directive.include file="includes/bottom.jsp" />  
 <%}else{
	//通过jsonp判断用户是否已经登录，登录成功的可以看到昵称
	Principal principal = (Principal)request.getAttribute("principal");
	response.setContentType("text/plain");
	PrintWriter out1 = response.getWriter();
	out1.println(callbackName+"({'login':'true','uuid':'"+principal.getAttributes().get("uuid")+"','nickname':'"+principal.getAttributes().get("nickname")+"'})");
	out1.flush();
	out1.close();
}%>

