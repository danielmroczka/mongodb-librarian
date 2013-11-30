<%-- 
    Document   : search
    Created on : Nov 30, 2013, 8:59:49 PM
    Author     : daniel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Page</title>
        <link rel="stylesheet" type="text/css" href="css/style.css" />

    </head>
    <body>
        <h1>Search Page</h1>

        <form action="search.do" method="POST">
            Title <input type="text" name="title"><br>
            <input type="submit" value="Submit">
        </form>
    </body>
</html>
