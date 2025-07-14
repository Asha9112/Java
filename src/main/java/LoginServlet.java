import java.io.*;
import java.net.URLEncoder;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String email = request.getParameter("email");
    String password = request.getParameter("password");

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
          "jdbc:mysql://localhost:3306/userauth", "root", "Asha");

      PreparedStatement ps = con.prepareStatement(
          "SELECT * FROM users WHERE email=? AND password=?");

      ps.setString(1, email);
      ps.setString(2, password);

      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        response.sendRedirect("https://www.linkedin.com/in/poojari-chinnakotla-asha-kalyani-02508a270");
      } else {
        response.sendRedirect("index.html?error=" + 
          URLEncoder.encode("❌ Invalid email or password.", "UTF-8"));
      }

      con.close();
    } catch (Exception e) {
      response.sendRedirect("index.html?error=" + 
        URLEncoder.encode("❌ Login error: " + e.getMessage(), "UTF-8"));
    }
  }
}
