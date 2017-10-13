<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Категории блюд</title>
</head>
<body>
<table>
    <tr>
        <th>Название</th>
    </tr>

    <c:forEach items="${categoryDishes}" var="categoryDishes">
        <tr>
            <td>${categoryDishes.name}</td>
        </tr>
    </c:forEach>
</table>
<form method="post" action="/categoryDishes">
    <input type="text" name="name">
    <input type="submit">
</form>
</body>
</html>
