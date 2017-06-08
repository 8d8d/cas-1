<%@ page import="java.io.PrintWriter" %>
<%
String callbackName = request.getParameter("callback");
if(callbackName!=null){
	PrintWriter out1 = response.getWriter();
	out1.println(callbackName+"({'login':'false'})");
}else{

%>
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
    <meta name="renderer" content="webkit"/>
    <meta name="renderer" content="webkit"/>
    <title>优茶大数据平台 登录</title>
    <link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css"/>
    <link rel="stylesheet" href="font-awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="css/pluginsExt/webSite.css"/>
    <link rel="stylesheet" href="css/pluginsExt/login/login.css"/> 
</head>
<body>
<!-- 没有开启https的错误提醒
<c:if test="${not pageContext.request.secure}">
    <div id="msg" class="errors">
        <h2><spring:message code="screen.nonsecure.title" /></h2>
        <p><spring:message code="screen.nonsecure.message" /></p>
    </div>
</c:if>-->

<div id="cookiesDisabled" class="errors" style="display:none;">
    <h2><spring:message code="screen.cookies.disabled.title" /></h2>
    <p><spring:message code="screen.cookies.disabled.message" /></p>
</div>

<c:if test="${not empty registeredService}">
    <c:set var="registeredServiceLogo" value="images/webapp.png"/>
    <c:set var="registeredServiceName" value="${registeredService.name}"/>
    <c:set var="registeredServiceDescription" value="${registeredService.description}"/>

    <c:choose>
        <c:when test="${not empty mduiContext}">
            <c:if test="${not empty mduiContext.logoUrl}">
                <c:set var="registeredServiceLogo" value="${mduiContext.logoUrl}"/>
            </c:if>
            <c:set var="registeredServiceName" value="${mduiContext.displayName}"/>
            <c:set var="registeredServiceDescription" value="${mduiContext.description}"/>
        </c:when>
        <c:when test="${not empty registeredService.logo}">
            <c:set var="registeredServiceLogo" value="${registeredService.logo}"/>
        </c:when>
    </c:choose>
    <p/>
</c:if>
<form:form method="post" id="fm1" commandName="${commandName}" htmlEscape="true">
    <form:errors path="*" id="msg" cssClass="errors" element="div" htmlEscape="false" cssStyle="display:none" />
</form:form>
<div class="hidden-xs">
    <div class="Head">
        <div class="w">
            <h2 style="font-family: 微软雅黑;font-weight:bold;"><a href="http://www.utea20.com/">优 茶</a></h2>
            <h4>欢迎登录</h4>
            <h4 class="fr" style="line-height:30px"><a href="http://www.utea20.com/">返回网站</a></h4>
        </div>
    </div>
    <div class="content">
        <div class="loginBac">
            <div class="loginCont">
                <div class="loginTit">
                    <div class="static">
                        <a href="javascript:;" class="active"  style="border-right:0px">账户登录</a>
                    </div>
                    <div id="codeQuick" style="display:none">
                        <a href="javascript:;" class="grayfont">扫码登录</a>
                    </div>
                </div>
                <div class="loginBox">
                    <div>
                        <div class="msg-tips">
                            <b class="fa fa-bullhorn"></b>
                            &nbsp;&nbsp;公众场合不建议自动登录，以防账号丢失
                        </div>
                        <div class="msg-error">
						</div>
                    </div>
                    <!-- 账号登录-->
                    <div class="acc-login">
                        <form class="loginForm" action="login" method="post" onSubmit="return login()">
							<input type="hidden" name="pubKey" id="pubKey"
value="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDaAss40iZzJYP0IRoog1vhBnFmU2yq43EWjF3uXujTv3fHa1KMyENuNPNLDUNNllfqRgxIRe4dAl60b5v/nwWIk9euPdq8A6DHO98Au1O+5NK3DexLbQEVCxFqCYzM63DxARzTr3lKg05Q+s+OjRKVaF8ABWZQ3NUGoi387rrc7QIDAQAB" />                        
                            <input type="hidden" name="lt" value="${loginTicket}" />
                            <input type="hidden" name="execution" value="${flowExecutionKey}" />
                            <input type="hidden" name="_eventId" value="submit" />
                            <div class="form-group">
                                <div class="input-group">
                                    <span class="input-group-addon logIcon">
									<i class="fa fa-user"></i>
									</span>
									<input class="form-control userName"  maxlength="20" placeholder="邮箱/用户名/已验证手机" type="text" id="username" name="username">
                                    <i class="fa fa-close"></i>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="input-group">
                                    <span class="input-group-addon logIcon">
									<i class="fa fa-lock"></i>
									</span>
									<input class="form-control pwd" placeholder="密码" type="password"  id="password" name="password">
                                    <i class="fa fa-close"></i>
                                </div>
                            </div>
                            <a class="pull-right grayfont" href="https://ucenter.utea20.com/website/register/forgetPsw" target="_blank">忘记密码?</a>
                            <div class="text-left" style="display:none">
                                <label class="checkbox" for="checkbox">
								<input id="checkbox" type="checkbox" name="rememberMe"  id="rememberMe" value="true">
								<span class="grayfont">自动登录</span>
								</label>
                            </div>
                            <input class="btn btn-lg  btn-block" type="submit" value="登录">
                        </form>
                        <div class="otChoise">
                            <div class="choise">
							<c:if test="${!empty pac4jUrls}">
                                <ul>
									<c:forEach var="entry" items="${pac4jUrls}">
										<li><a href="${entry.value}" title="${entry.key}" class="iconWX"></a></li>
									</c:forEach>
                                </ul>
							</c:if>
<!-- 								<ul> -->
<!-- 	                                <li><a href="#" class="iconQQ" title="QQ"></a></li> -->
<!-- 	                                <li><a href="#" class="iconWX" title="微信"></a></li> -->
<!-- 	                                <li><a href="#" class="iconWB" title="微博"></a></li> -->
<!-- 								</ul> -->
                            </div>
                            <div class="loginRs pull-right">
                                <a href="https://ucenter.utea20.com/website/register/" target="_blank">立即注册</a>
                            </div>
                        </div>
                    </div>
                    <!-- 二维码-->
                    <div class="qrcode-login">
                        <div>
                            <img src="img/login/qrcode.png" alt=""/>
                        </div>
                        <div class="code-tips">
                            <a href="javascript:;">打开新版手机优茶，扫描二维码</a>
                            <a href="javascript:;">使用帮助</a>
                        </div>
                        <div class="code-footer">

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="hidden-sm hidden-md hidden-lg" style="margin-top: 30px">
    <div class="col-md-6">
        <h2 class="tea-name" style="text-align: center">优茶<span style="letter-spacing: 2px;margin-left: 10px;">Utea</span></h2>
        <br>
        <div class="msg-error"></div>
        <br>
        <div class="ibox-content">
            <form class="loginForm" action="" method="post" onsubmit="return login()">
                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon logIcon"><i class="fa fa-user"></i></span><input class="form-control userName"  maxlength="20" placeholder="邮箱/用户名/已验证手机" type="text">
                        <i class="fa fa-close"></i>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon logIcon"><i class="fa fa-lock"></i></span><input class="form-control pwd"  maxlength="15" placeholder="密码" type="password">
                        <i class="fa fa-close"></i>
                    </div>
                </div>
                <a class="grayfont" href="https://ucenter.utea20.com/website/register">&nbsp;立即注册</a>
                <a class="pull-right grayfont" href="forgetPsw.html">忘记密码?</a>
                <br>
                <input class="btn btn-lg  btn-block" type="submit" value="登录">
            </form>
        </div>
    </div>
</div>
</body>
<!--[if gte IE 7]>
<script src="../js/jquery-1.11.3.min.js" type="text/javascript"></script>
<![endif]-->
<!--[if !IE]><!-->
<script src="js/jquery-2.2.3.min.js" type="text/javascript"></script>
<!--<![endif]-->
<script src="js/jsencrypt.min.js" type="text/javascript"></script>
<script src="js/cas.js" type="text/javascript" ></script>
<script src="js/pluginsExt/login/login.js" type="text/javascript"></script>
<script src="js/pluginsExt/footer/footer.js" type="text/javascript"></script>
</html>
<%}%>