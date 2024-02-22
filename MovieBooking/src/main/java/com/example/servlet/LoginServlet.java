package com.example.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.model.User;
import com.example.util.DatabaseUtil; // Assume you have a DatabaseUtil class for database connections

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection connection = DatabaseUtil.getConnection()) {
            // Use PreparedStatement to safely execute SQL queries
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                	if (resultSet.next()) {
                	    User validUser = new User(resultSet.getString("username"), resultSet.getString("password"));
                	    validUser.setId(resultSet.getInt("id")); // Set the user ID
                	    request.getSession().setAttribute("loggedInUser", validUser);
                	    System.out.println("User ID set in session: " + validUser.getId());
                	    response.sendRedirect("movies.jsp");
                	    return;
                	}
                }
            }

            // Authentication failed
            response.sendRedirect("index.jsp?loginError=true");
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
            response.sendRedirect("index.jsp?loginError=true");
        }
    }
}