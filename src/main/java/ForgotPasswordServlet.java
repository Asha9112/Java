import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.*;

public class ForgotPasswordServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String email = request.getParameter("email");
    String newPassword = request.getParameter("newPassword");
    String confirmPassword = request.getParameter("confirmPassword");

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    if (!newPassword.equals(confirmPassword)) {
      out.println("Passwords do not match!");
      return;
    }

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
          "jdbc:mysql://localhost:3306/userauth", "root", "yourpassword");

      PreparedStatement ps = con.prepareStatement(
          "UPDATE users SET password=? WHERE email=?");

      ps.setString(1, newPassword);
      ps.setString(2, email);

      int result = ps.executeUpdate();

      if (result > 0) {
        out.println("Password updated successfully.");
      } else {
        out.println("No account found with that email.");
      }

      con.close();
    } catch (Exception e) {
      e.printStackTrace(out);
    }
  }
}
