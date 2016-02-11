<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Page</title>
        <link href="<c:url value="/resources/css/styles.css" />" rel="stylesheet">
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    </head>
    <body>
        <form>
            Book title or filename: <input type="text" name="title" id="title">
            <input type="submit" value="Search" class="myButton">
        </form>
        <script>
            $("form").submit(function(event) {
                $('#results').html('');
                var title = $('#title').val();
                $.ajax({
                    cache: false,
                    type: 'GET',
                    data: 'title=' + title,
                    url: 'ajax.search.do',
                    dataType: 'json',
                    success: function(responseData) {
                        if (responseData) {
                            for (var i in responseData) {
                                $('#results').append((i) + '. ' + responseData[i].title + '<br/>');
                            }
                        }
                    },
                    error: function(request, status, error) {
                        //alert(request.responseText);
                    }

                });
                event.preventDefault();
            });
        </script>

        <div id="results" >

        </div>

        <div id="error"></div>
    </body>
</html>
