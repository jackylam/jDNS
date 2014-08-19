package me.jaks.jdns.console;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class LoginAction
 */

@WebServlet(name="LoginAction",urlPatterns={"/index.jsp","/login"},loadOnStartup=1)
public class LoginAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("jdnsconsole");
	
	

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	@Resource(name="jdbc/mysql")
	private DataSource ds;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		logger.info("Initalizing jdns..");
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
		      conn = ds.getConnection();
		      st = conn.createStatement();
	          rs = st.executeQuery("select 1");
	          
	          while(rs.next()) {
	          	log(rs.getString(1));
	          }
	          
		      
		    }
		    catch(Exception e) {
		        logger.error( "problem accessing database", e );
		        
		      }
		finally {
			JdbcHelper.close(rs);
			JdbcHelper.close(st);
			JdbcHelper.close(conn);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean found = false;
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if(logger.isDebugEnabled())
			logger.info(email + " " + password);
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql ="SELECT email from user where email = ? and password = ?";
		
		try {
		      conn = ds.getConnection();
		      st = conn.prepareStatement(sql);
		      st.setString(1, email);
		      st.setString(2, password);
	          rs = st.executeQuery();
	          
	          if(rs.next()) {
	          	found = true;
	          }
	          
		      
		    }
		    catch(SQLException e) {
		        logger.error( "problem accessing database", e );
		        
		      }
		finally {
			JdbcHelper.close(rs);
			JdbcHelper.close(st);
			JdbcHelper.close(conn);
		}
		if(found == true) {
			HttpSession session = request.getSession();
            session.setAttribute("user", email);
            //setting session to expiry in 30 mins
            session.setMaxInactiveInterval(30*60);
            Cookie userName = new Cookie("user", email);
            userName.setMaxAge(30*60);
            response.addCookie(userName);
            response.sendRedirect("main.jsp");
		}
		else
			request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
	}

}
