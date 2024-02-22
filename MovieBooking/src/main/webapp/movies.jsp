<%@ page import="java.util.List" %>
<%@ page import="com.example.model.Movie" %>
<%@ page import="com.example.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>

<%
    User loggedInUser = (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
        response.sendRedirect("index.jsp");
    }
%>

<!DOCTYPE html>
<html>
<head>
     <title>Movies</title>
     <link rel="stylesheet" type="text/css" href="styles.css">
     <style>
        body {
            font-family: 'Arial', sans-serif;
           
            margin: 0;
            padding: 0;
            text-align: center;
            
            background-image:url("https://tse4.mm.bing.net/th?id=OIP.rXCz8RXHdZ1IF3ibBoA9kAHaEK&pid=Api&P=0&h=220")
            
        }

        h2 {
            color: white;
        }

        form {
            max-width: 400px;
            margin: 50px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            text-align: left;
        }

        label {
            display: block;
            margin-bottom: 8px;
            color: #555;
        }

        select, input {
            width: 100%;
            padding: 8px;
            margin-bottom: 15px;
            box-sizing: border-box;
        }

        button {
            background-color: #3498db;
            color: #fff;
            padding: 12px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s;
        }

        img {
            max-width: 100%;
            height: auto;
        }
     </style>
</head>
<body style="font-family: 'Arial', sans-serif; margin: 0; padding: 0; text-align: center; background-image: url('https://tse4.mm.bing.net/th?id=OIP.rXCz8RXHdZ1IF3ibBoA9kAHaEK&pid=Api&P=0&h=220'); background-repeat: no-repeat; background-size: cover;">
    <!-- Your content goes here -->

    <h2 style="color:white">Select a Movie</h2>
    
    <form action="bookTicket" method="post">
        <label for="selectedMovie">Select a Movie:</label>
        <select id="selectedMovie" name="selectedMovie">
           <option value="12th Fail">12th Fail</option>
           <option value="Salaar">Salaar</option>
           <option value="Hanuman">Hanuman</option>
        </select><br>

        <!-- Display movie images -->
        <img src="https://tse1.mm.bing.net/th?id=OIP.YYqQOAIYCbL2gZMy27QxuQHaEH&pid=Api&P=0&h=220" alt="12th Fail Image">
        <img src="https://tse2.mm.bing.net/th?id=OIP.5U6qaaDLsFkf3WvHS65M-AHaEK&pid=Api&P=0&h=220" alt="Salaar Image">
        <img src="https://tse3.mm.bing.net/th?id=OIP.SHh1Soy0LEcGolNXRXHrjQHaKX&pid=Api&P=0&h=220" alt="Hanuman Image" style="width: 500px; height:180px">
        <label for="numberOfSeats">Number of Seats:</label>
        <input type="number" id="numberOfSeats" name="numberOfSeats" required><br>
        
        <button type="submit">Book Ticket</button>
    </form>
</body>
</html>