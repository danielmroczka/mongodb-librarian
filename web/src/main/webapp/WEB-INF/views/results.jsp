<%-- 
    Document   : results
    Created on : Nov 30, 2013, 9:18:58 PM
    Author     : daniel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Results Page</title>
        <link rel="stylesheet" type="text/css" href="css/style.css" />
    </head>
    <body>
        <h3>Results:</h3>
        <c:forEach items="${list}" var="item">     
            <c:out value="${item.title}"/></br>
        </c:forEach>
    </body>
</html>
