import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class updateDatabaseRecord
 */
@WebServlet("/updateDatabaseRecord")
public class updateDatabaseRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public updateDatabaseRecord() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();  // Response to client (e.g., web browser), for displaying info in the web page.
		String recordID = request.getParameter("updateID"); // retrieve information in the submitted form 
		String newValue = request.getParameter("rating");
		out.println(recordID + " " + newValue);
		
		// Update difficulty level of an article in the database
		if (newValue != null) {
			Connection  con = null;
			try {
				try {
					Class.forName("org.sqlite.JDBC");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				con = DriverManager.getConnection("jdbc:sqlite:D:\\EclipseWorkspace\\WordFrequency2\\b.db");
				PreparedStatement statement = con.prepareStatement("update wordcount set level=? where id=?"); 
				statement.setInt(1, Integer.parseInt(newValue));
				statement.setInt(2, Integer.parseInt(recordID));
				statement.executeUpdate();
				
		
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			} finally {
				try {
					if (con != null) {
						con.close();
					}
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		
		ServletContext sc = this.getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher("/search.jsp");
		rd.forward(request, response);
	}

}
