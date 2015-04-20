package me.jaks.jdns.webapp;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class MainAction
 */
@WebServlet("/main.jsp")
public class MainAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name="jdbc/mysql")
	private DataSource ds;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainAction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DomainSummarizer domains = new DomainSummarizer(ds);
		Map<String,String> domainMap = domains.getSummary();
		request.setAttribute("domainMap", domainMap);
		request.getRequestDispatcher("/WEB-INF/jsp/main.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
