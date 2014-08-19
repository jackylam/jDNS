package me.jaks.jdns.console;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class ZoneDetailAction
 */
@WebServlet("/ZoneDetailAction")
public class ZoneDetailAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@Resource(name="jdbc/mysql")
	private DataSource ds;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ZoneDetailAction() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String zone = request.getParameter("zone");
		
		RecordDaoInt recordDao = new RecordDao(ds);
		Record[] records = recordDao.getRecords(zone);
		request.setAttribute("records", records);
		request.getRequestDispatcher("/WEB-INF/jsp/zone.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
