package com.example.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.model.Movie;
import com.example.model.User;
import com.example.util.DatabaseUtil;

@WebServlet("/bookTicket")
public class MovieTicketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User loggedInUser = (User) request.getSession().getAttribute("loggedInUser");

        if (loggedInUser != null) {
            String selectedMovie = request.getParameter("selectedMovie");
            String seatsString = request.getParameter("numberOfSeats");

            if (selectedMovie != null && seatsString != null && seatsString.matches("\\d+")) {
                try {
                    int numberOfSeats = Integer.parseInt(seatsString);
                    Movie movie = getMovieByName(selectedMovie);

                    if (movie != null) {
                        int totalPrice = calculateTotalPrice(movie, numberOfSeats);
                        System.out.println("User ID before insertBooking: " + loggedInUser.getId());

                        int bookingId = insertBooking(loggedInUser.getId(), movie.getId(), numberOfSeats, totalPrice, response);

                        if (bookingId != -1) {
                            // Format the current date
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String bookingDate = dateFormat.format(new Date());

                            // Set attributes for JSP
                            request.setAttribute("bookingDate", bookingDate);
                            request.setAttribute("movie", movie);
                            request.setAttribute("numberOfSeats", numberOfSeats);
                            request.setAttribute("totalPrice", totalPrice);

                            // Forward to ticket.jsp
                            request.getRequestDispatcher("ticket.jsp").forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                    "Error while processing the booking.");
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Movie not found.");
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid input format. Please enter valid values.");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid input. Please provide both movie and number of seats.");
            }
        } else {
            response.sendRedirect("index.jsp");
        }
    }

    private Movie getMovieByName(String movieName) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM movies WHERE name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, movieName);

                System.out.println("Executing SQL Query: " + preparedStatement.toString());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    int resultSetSize = 0;
                    while (resultSet.next()) {
                        resultSetSize++;
                        int id = resultSet.getInt("id");
                        int frontSeatPrice = resultSet.getInt("front_seat_price");
                        int middleSeatPrice = resultSet.getInt("middle_seat_price");
                        int lastSeatPrice = resultSet.getInt("last_seat_price");

                        Movie movie = new Movie(movieName, frontSeatPrice, middleSeatPrice, lastSeatPrice);
                        movie.setId(id);
                        return movie;
                    }

                    System.out.println("Result Set Size: " + resultSetSize);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception appropriately
        }
        return null;
    }
    

    private int calculateTotalPrice(Movie movie, int numberOfSeats) {
        if (movie != null) {
            return movie.getFrontSeatPrice() * numberOfSeats;
        } else {
            System.err.println("Movie object is null.");
            return 0;
        }
    }

    private int insertBooking(int userId, int movieId, int numberOfSeats, int totalPrice, HttpServletResponse response)
            throws IOException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            if (!isUserExists(userId, connection)) {
                System.out.println("User with ID " + userId + " does not exist.");
                return -1;
            }

            if (!isMovieExists(movieId, connection)) {
                System.out.println("Movie with ID " + movieId + " does not exist.");
                return -1;
            }

            System.out.println("User ID received in insertBooking: " + userId);

            String sql = "INSERT INTO bookings (user_id, movie_id, number_of_seats, total_price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, movieId);
                preparedStatement.setInt(3, numberOfSeats);
                preparedStatement.setInt(4, totalPrice);

                preparedStatement.executeUpdate();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int bookingId = generatedKeys.getInt(1);
                        System.out.println("Booking ID: " + bookingId);
                        return bookingId;
                    } else {
                        throw new SQLException("Failed to get booking ID, no key obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Error while processing the booking. Please try again later. Details: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return -1;
        }
    }

    private boolean isMovieExists(int movieId, Connection connection) throws SQLException {
        if (movieId <= 0) {
            System.out.println("Invalid movie ID: " + movieId);
            return false;
        }

        String movieCheckSql = "SELECT 1 FROM movies WHERE id = ?";
        try (PreparedStatement movieCheckStatement = connection.prepareStatement(movieCheckSql)) {
            movieCheckStatement.setInt(1, movieId);
            try (ResultSet movieCheckResultSet = movieCheckStatement.executeQuery()) {
                return movieCheckResultSet.next();
            }
        }
    }

    private boolean isUserExists(int userId, Connection connection) throws SQLException {
        if (userId <= 0) {
            System.out.println("Invalid user ID: " + userId);
            return false;
        }

        String userCheckSql = "SELECT 1 FROM users WHERE id = ?";
        try (PreparedStatement userCheckStatement = connection.prepareStatement(userCheckSql)) {
            userCheckStatement.setInt(1, userId);
            try (ResultSet userCheckResultSet = userCheckStatement.executeQuery()) {
                return userCheckResultSet.next();
            }
        }
    }
}