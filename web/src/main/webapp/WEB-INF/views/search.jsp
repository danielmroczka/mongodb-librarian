<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Page</title>
        <link rel="stylesheet" type="text/css" href="css/style.css" />
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    </head>
    <body>
        <form>
            Title:
            <input type="text" name="title" id="title">
            <input type="submit" value="Submit">
        </form>
        <span></span>
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
                        alert(request.responseText);
                    }

                });
                event.preventDefault();
            });
        </script>

        <div id="results">

        </div>
    </body>
</html>
