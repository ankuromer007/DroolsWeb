<html>
	<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<head>
		<meta charset="utf-8">
		<title>First Drools Web Project</title>
		<link rel="stylesheet" type="text/css" href="css/style.css" />
		<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script type="text/javascript">
			$(function() {
				$('.error').delay(4000).slideUp('slow');
				if($('input[name="uname"]').val() != ''){
					$('input[name="pass"]').focus();
				}
			});
		</script>
	</head>
	<body>
		<c:if test="${not empty loginError}">
			<span class="error">${loginError}</span>
		</c:if>
		<form  action="/DroolsWeb/login" method="post" name="login-form" id="login-form">
			<input type="text" name="uname" class="placeholder" placeholder="Username" 
				autofocus="autofocus" title="Enter Username" required="required" value="${usernameValue}">
			<input type="password" name="pass" class="placeholder" placeholder="Password" 
				title="Enter Password" required="required">
			<input type="hidden" name="sessId" value="${pageContext.session.id}">
			<input type="submit" value="Log In">
		</form>
	</body>
</html>