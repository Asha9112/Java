import java.io.*;
import java.net.URLEncoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.*;


public class SignupServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        String message = "";
        String redirectURL = "index.html?form=signup";

        try {
            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|in|org|edu)$")) {
                message = "❌ Invalid email format!";
                redirectURL += "&error=" + URLEncoder.encode(message, "UTF-8");
            } else if (!phone.matches("\\d{10}")) {
                message = "❌ Phone number must be exactly 10 digits!";
                redirectURL += "&error=" + URLEncoder.encode(message, "UTF-8");
            } else if (!password.equals(confirmPassword)) {
                message = "❌ Passwords do not match!";
                redirectURL += "&error=" + URLEncoder.encode(message, "UTF-8");
            } else {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/userauth", "root", "Asha");

                PreparedStatement check = conn.prepareStatement("SELECT * FROM users WHERE email = ?");
                check.setString(1, email);
                ResultSet rs = check.executeQuery();

                if (rs.next()) {
                    message = "❌ Email already registered!";
                    redirectURL += "&error=" + URLEncoder.encode(message, "UTF-8");
                } else {
                    PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO users (name, email, phone, address, password) VALUES (?, ?, ?, ?, ?)");
                    stmt.setString(1, name);
                    stmt.setString(2, email);
                    stmt.setString(3, phone);
                    stmt.setString(4, address);
                    stmt.setString(5, password);
                    stmt.executeUpdate();

                    message = "✅ Signup successful!";
                    redirectURL += "&success=" + URLEncoder.encode(message, "UTF-8");
                }

                conn.close();
            }

        } catch (Exception e) {
            message = "❌ Server error: " + e.getMessage();
            redirectURL += "&error=" + URLEncoder.encode(message, "UTF-8");
        }

        response.sendRedirect(redirectURL);
    }
}

