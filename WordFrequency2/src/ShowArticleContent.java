
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * Servlet implementation class ShowArticleContent
 */
@WebServlet("/ShowArticleContent")
public class ShowArticleContent extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowArticleContent() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		PrintWriter out= response.getWriter(); 
		out.println("Working in doGet");

	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		String content = request.getParameter("content");
		
		if (content == "") {
			ServletContext sc = this.getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher("/index.jsp");
			rd.forward(request, response);
		}
		
		String [] words = content.split(" "); // split string into words. Does not work well if there is more than one space between words.
		PrintWriter out = response.getWriter(); 
		
		out.println("<html>");
		out.println("<p><a href=\"index.jsp\">Home</a></p>");
		out.println("<h2>The current article</h2>");
		out.println("<p>Total words:" + words.length + "</p>");
		out.println("<p><font size=+2>" + content + "</font></p>");

		
		// Save article and word frequency into SQLite database
		
		Connection  con = null;
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.clear();
        String spilrStr1 = String.valueOf(content);
		//����","�ָ���ַ�����(��".")
		String[] word1 = spilrStr1.split(",");
		//�ַ�����ת��Ϊ�ַ���
		String spilrStr2 = String.join(" ",word1);
		//��"."�ָ��ַ�����(ֻʣ�ո�)
		String[] word2 = spilrStr2.split("\\.");
		//ת��Ϊ�ַ���
		String spilrStr3 = String.join(" ",word2);
		//���ݿո�ֿ�
		String[] sortword = spilrStr3.split(" ");
		
		//ͳ�Ƶ��ʳ��ֵ�Ƶ��

        for (String str : sortword) {
            if (!map.containsKey(str)) {//��str������,
                map.put(str, 1);
            } else {
                //������c��ֵ���Ҽ�1
                map.put(str, map.get(str) + 1);
            }
        }
		//�������Ƶ��
        Set<String> keySet = map.keySet();
		Iterator<String> it1 = keySet.iterator();
		while(it1.hasNext()){
			String word = it1.next();
			int count = map.get(word);
			out.println(word + ":"+ count);
		}
        
		try {
			try {
				Class.forName("org.sqlite.JDBC");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}           
    		con = DriverManager.getConnection("jdbc:sqlite:D:\\\\EclipseWorkspace\\\\WordFrequency2\\\\b.db");
    		Statement statement = con.createStatement();
			statement.executeUpdate("create table if not exists wordcount (word string PRIMARY KEY, count integer, level integer DEFAULT 0)");
			String sql = "replace into wordcount (word,count) values(?,count+?);";				
			PreparedStatement state = con.prepareStatement(sql);
			for (String key : map.keySet()) {	
					state.setString(1, key);
					state.setInt(2,map.get(key));
					state.executeUpdate();
			}			
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
		out.println("</html>");
	}

}
