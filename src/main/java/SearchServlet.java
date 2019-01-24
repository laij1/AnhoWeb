import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.google.gson.JsonArray;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "SearchServlet", urlPatterns = "/api/seach")
public class SearchServlet extends HttpServlet {
	
	private static final long serialVersionUID = 7887576413644810002L;
	Connection connection = null;
	Statement statement;
	Statement patientStatement;
	ResultSet patientResultSet;
	ResultSet resultSet;
	String patientIc = "";
	JsonArray responseJsonArray;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String loginUser = "anhouser";
        String loginPasswd = "ThePassword";
        String loginUrl = "jdbc:mysql://localhost:3306/db_anho";
		
        String patientName = request.getParameter("patientname");
        String startDate = request.getParameter("startdate");
        String endDate = request.getParameter("enddate");
        System.out.println("we got the name and date!" + patientName + startDate + endDate);
        
        try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// create database connection
			connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			
			//for getting patient ic
			patientStatement = connection.createStatement();
			String patientQuery ="select ic from patient where name='" + patientName + "'";
			patientResultSet = patientStatement.executeQuery(patientQuery);
			
			patientIc = "";
	        while(patientResultSet.next()) {
	        	patientIc = patientResultSet.getString("ic");
	        }
			
	        System.out.println("here is the ic" + patientIc);
			// declare statement
			statement = connection.createStatement();
			// prepare query
			String query = "select record.rid, record.create_on, record.charge_on, record.medicine_name, record.quantity, record.subtotal, medicine.category "
					+ "from medicine_record as record inner join medicine "
					+ "where record.mid = medicine.mid and "
					+ "record.receipt_on is null and "
					+ "record.patient_name='" + patientName + "' and record.create_on>='" 
					+ startDate + "' and record.create_on <='" + endDate + "' order by create_on asc;";
			
			System.out.println("receipt search query: " + query);
			// execute query
			resultSet = statement.executeQuery(query);
			
			responseJsonArray = new JsonArray();
			//add the ic to array
			JsonObject icObject = new JsonObject();
			icObject.addProperty("ic", patientIc);
			responseJsonArray.add(icObject);
			
			while (resultSet.next()) {
				String medicineName = resultSet.getString("medicine_name");
				Date createOn = resultSet.getDate("create_on");
	            Date chargeOn = resultSet.getDate("charge_on");
	            Integer quantity = resultSet.getInt("quantity");
	            Integer subtotal = resultSet.getInt("subtotal");
	            String category = resultSet.getString("category");
	            Integer rid = resultSet.getInt("rid");
	            System.out.println(medicineName+"/"+ createOn +"/"+ chargeOn +"/"+ subtotal);
				JsonObject responseJsonObject = new JsonObject();
				responseJsonObject.addProperty("rid", rid);
				responseJsonObject.addProperty("medicineName", medicineName);
				responseJsonObject.addProperty("createOn", createOn.toString());
			    responseJsonObject.addProperty("chargeOn", chargeOn==null?"尚未結帳":chargeOn.toString());
			    responseJsonObject.addProperty("quantity", quantity.toString());
			    responseJsonObject.addProperty("subtotal", subtotal.toString());
			    responseJsonObject.addProperty("category", "dialysis".equals(category)? "衛材" :"藥品");
			    //responseJsonObject.addProperty("ic", patientIc);
		
			    responseJsonArray.add(responseJsonObject);
			}
			
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println("we are closing connection");
			close();
		}

       
        
        System.out.println("here are the Jsonarray: " + responseJsonArray.toString());
        response.setCharacterEncoding("UTF-8"); 
       // System.out.println("setting utf"+ response.getCharacterEncoding());
        response.getWriter().write(responseJsonArray.toString());
        }
	
	private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            
            if(patientResultSet != null) {
            	patientResultSet.close();
            }

            if (statement != null) {
                statement.close();
               
            }
            
            if(patientStatement != null) {
            	patientStatement.close();
            }

            if (connection != null) {
                connection.close();
            }

        } catch (Exception e) {

        } finally {
        	resultSet = null;
        	statement = null;
        	patientResultSet = null;
        	patientStatement = null;
        }
    }
    
}
