<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.Movie" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<% SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); %>
 	<p>Booking Date: <%= dateFormat.format(new Date()) %></p>
<%
    Movie movie = (Movie) request.getAttribute("movie");
    Integer numberOfSeats = (Integer) request.getAttribute("numberOfSeats");
    Integer totalPrice = (Integer) request.getAttribute("totalPrice");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Movie Ticket</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: black;
            text-align: center;
            margin-top:100;
            margin-left:50px
            padding: 0;
        }

        h2 {
            color: white;
        }

        p {
            color: Blue;
            margin: 5px;
            font-size:40px
        }

        
    </style>
</head>
<body>
    <h2>Movie Ticket</h2>
    
    <p>Movie: <%= movie.getName() %></p>
    <p>Number of Seats: <%= numberOfSeats %></p>
    <p>Total Price: <%= totalPrice %></p>
    
    <p>Booking Date: <%= new Date() %></p>
    
    <p>Your ticket is booked successfully!!!!</p>
</body>
</html>