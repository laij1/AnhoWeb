import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;

@WebServlet(name = "MonthlyInvoiceServlet", urlPatterns = "/api/invoice")
public class MonthlyInvoiceServlet extends HttpServlet{

	private static final long serialVersionUID = -4128064870874830096L;
	AnhoDatabaseService anhoDBService = AnhoDatabaseService.getInstance();
	JsonArray responseJsonArray;
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String monthDate = request.getParameter("monthdate");
		System.out.println("MonthDate is " + monthDate);
		
		//end date will always be 31, even with month less than 31, database will pull out the last date
		String start = monthDate + "-01";
		String end  = monthDate + "-31";
		
		//get connection
		try {
			responseJsonArray = anhoDBService.getAllMonthlyPaymentPatient(start, end);
			System.out.println(responseJsonArray.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setCharacterEncoding("UTF-8"); 
	    // System.out.println("setting utf"+ response.getCharacterEncoding());
	    response.getWriter().write(responseJsonArray.toString());
        
    }
	

}
