<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

</div> <!-- END #content -->

<footer>
  <div id="copyright" class="container">
  </div>
</footer>

</div> <!-- END #container -->
<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/headjs/1.0.3/head.min.js"></script> -->
<script type="text/javascript" src="/cas/ajax/libs/headjs/1.0.3/head.min.js"></script>
<spring:theme code="cas.javascript.file" var="casJavascriptFile" text="" />
<script type="text/javascript" src="<c:url value="${casJavascriptFile}" />"></script>
<script type="text/javascript" src="/cas/js/jsencrypt.min.js"></script>
<script>
function jqueryReady() {
	/**
	 * 加密密码
	 * @param pwd
	 */
	function getEntryptPwd(pwd){
	    var pubKey = $('#pubKey').val();
	    var encrypt = new JSEncrypt();
	    encrypt.setPublicKey(pubKey);
	    var pwd = encrypt.encrypt(pwd);
	    return pwd;
	}
	
	$('#loginBtn').click(function(){
		var password = $("#password1").val();
		//清空原密码
		//$("#password1").val('');
		//sha256加密
		$("#password").val(getEntryptPwd(password));
		$("#fm1").submit();
	});
}
function refreshcaptchacode(obj) {
    obj.setAttribute("src", "kaptcha.jpg?date=" + new Date());
}
</script>

</body>
</html>

