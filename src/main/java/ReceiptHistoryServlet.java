import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@WebServlet(name = "ReceiptHistoryServlet", urlPatterns = "/api/receiptHistory")
public class ReceiptHistoryServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6485450588583798694L;
	AnhoDatabaseService anhoDBService = AnhoDatabaseService.getInstance();
	JsonArray responseJsonArray;
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String monthReceipt = request.getParameter("monthreceipt");
		System.out.println("monthreceipt is " + monthReceipt);
		
		//end date will always be 31, even with month less than 31, database will pull out the last date
		String start = monthReceipt + "-01";
		
		responseJsonArray = new JsonArray();
		
		//get connection
		try {
			ResultSet resultSet = anhoDBService.getAllReceiptRecord(start);
			while(resultSet.next()) {
				Integer rid = resultSet.getInt("rid");
				String patientName = resultSet.getString("patient_name");
				String medicineName = resultSet.getString("medicine_name");
				Date createOn = resultSet.getDate("create_on");
	            Integer subtotal = resultSet.getInt("subtotal");
	            
	            System.out.println(medicineName+"/"+ createOn +"/"+ patientName +"/"+ subtotal);
				JsonObject responseJsonObject = new JsonObject();
				responseJsonObject.addProperty("rid", rid);
				responseJsonObject.addProperty("patientName", patientName);
				responseJsonObject.addProperty("medicineName", medicineName);
				responseJsonObject.addProperty("createOn", createOn.toString());
			    responseJsonObject.addProperty("subtotal", subtotal.toString());
			    
			    //responseJsonObject.addProperty("ic", patientIc);
		
			    responseJsonArray.add(responseJsonObject);
			}
			anhoDBService.close();
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
