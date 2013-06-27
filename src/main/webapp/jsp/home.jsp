<html>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<head>
		<meta charset="utf-8">
		<title>First Drools Web Project</title>
		<link rel="stylesheet" type="text/css" href="css/style.css" />
	</head>
	<body>
		<div class="menu-bar">
			<span class="welcome">Welcome, ${user.name} ! (${user.location})</span>
			<a href="/DroolsWeb/logout"><input type="button" value="Logout"></a>
		</div>
		<div class="table">
			<table>
				<tr>
					<th>Name</th>
					<th>Base Price</th>
						<c:choose>
							<c:when test="${itemList[0].discount != 0.0 }">
					<th>Discounted Price(${itemList[0].discount}%)</th>
							</c:when>
							<c:otherwise>
					<th>Discounted Price(0%)</th>
							</c:otherwise>
						</c:choose>
				</tr>
				<c:forEach var="item" items="${itemList}">
				<tr>
					<td>${item.name}</td>
					<td>${item.basePrice}</td>
					<td>${item.discountedPrice}</td>
				</tr>
				</c:forEach>
			</table>
		</div>
	</body>
</html>