import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class AnhoDatabaseService {
	
	private static AnhoDatabaseService anhoDatabaseService;
	private Connection connection = null;
    private Statement statement = null;
//    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
    
    private AnhoDatabaseService() {
    }
    
    public static AnhoDatabaseService getInstance() {
    	if(anhoDatabaseService == null) {
    		anhoDatabaseService = new AnhoDatabaseService();
    	}
    	
    	return anhoDatabaseService;
    }
	
	public void getConnection() throws ClassNotFoundException, SQLException {
		// This will load the MySQL driver, each DB has its own driver
        Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        connection = DriverManager
        		.getConnection("jdbc:mysql://localhost:3306/db_anho","anhouser", "ThePassword");
  
	}
	
	public int updateReceiptOnByRid(List<Integer> rid, int importMonth) throws SQLException, ClassNotFoundException {
		int[] result = null;
	
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat Dateformatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		cal.set(Calendar.DAY_OF_MONTH, 1);
	
		//if imported from last month
		if(importMonth == 0) {
			cal.add(Calendar.MONTH, -1);
		} 
		
		String receiptDate = Dateformatter.format(cal.getTime());
		
		
		String SQL = "update medicine_record set receipt_on=? where rid=?"; 
		// '?' is the placeholder for the parameter
		try {
			getConnection();
			PreparedStatement stmt = connection.prepareStatement(SQL);
			connection.setAutoCommit(false);
			for(Integer id : rid) {
				stmt.setString(1, receiptDate);
				stmt.setInt(2,id);
				stmt.addBatch(); 
			}
//			stmt.setString(1, "Abc"); // Value for the first parameter, namely 'firstName'
//			stmt.setString(2, "Def"); // Value for the second parameter, namely 'lastName'
//			stmt.setInt(3, 1); // Value for the third parameter, namely 'id'
//			stmt.addBatch(); // Add to Batch
//
//			stmt.setString(1, "Xyz");
//			stmt.setString(2, "Uvw");
//			stmt.setInt(3, 2);
//			stmt.addBatch(); // Add second query to the Batch
			result = stmt.executeBatch(); // execute the Batch and commit
			connection.commit();
			return result.length;
			
		} catch (SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.close();
		}
	       return -1;
	}
	
	public JsonArray getAllMonthlyPaymentPatient(String startDate, String endDate) throws ClassNotFoundException, SQLException {
		getConnection();
		statement = connection.createStatement();
		String query = "select distinct patient_name, patient.ic from medicine_record "
				+ "inner join patient where medicine_record.pid=patient.pid "
				+ "and medicine_record.payment='MONTH' "
			    + "and medicine_record.create_on >='"+ startDate + "' "
			    + "and medicine_record.create_on <='" + endDate + "' order by patient_name asc;";
		
		System.out.println("getAllMonthlyPaymentPatient Query:" + query);
		resultSet = statement.executeQuery(query);
		
		JsonArray responseJsonArray = new JsonArray();
		while (resultSet.next()) {
			String patientName = resultSet.getString("patient_name");
			
			JsonObject responseJsonObject = new JsonObject();
			responseJsonObject.addProperty("patientName", patientName);
			
		    responseJsonArray.add(responseJsonObject);
		}
		
		close();
		return responseJsonArray;

	}
	
	
	public JsonArray getAllMonthlyReport(String startDate, String endDate) throws ClassNotFoundException, SQLException {
		getConnection();
		statement = connection.createStatement();
		String query = "select patient_name, create_on, charge_on, medicine_name, subtotal, quantity, payment from medicine_record "
			    + "where medicine_record.create_on >='"+ startDate + "' "
			    + "and medicine_record.create_on <='" + endDate + "'"
			    + " order by patient_name asc, create_on asc;";
		
		System.out.println("getAllMonthlyReport Query:" + query);
		resultSet = statement.executeQuery(query);
		
		JsonArray responseJsonArray = new JsonArray();
		while (resultSet.next()) {
			String patientName = resultSet.getString("patient_name");
			Date createOn = resultSet.getDate("create_on");
            Date chargeOn = resultSet.getDate("charge_on");
            String medicineName = resultSet.getString("medicine_name");
            Integer quantity = resultSet.getInt("quantity");
            Integer subtotal = resultSet.getInt("subtotal");
            String payment = resultSet.getString("payment");

			
			JsonObject responseJsonObject = new JsonObject();
			responseJsonObject.addProperty("patientName", patientName);
			responseJsonObject.addProperty("createOn", createOn.toString());
			responseJsonObject.addProperty("chargeOn", chargeOn==null?"尚未結帳":chargeOn.toString());
			responseJsonObject.addProperty("medicineName", medicineName);
			responseJsonObject.addProperty("quantity", quantity.toString());
			responseJsonObject.addProperty("subtotal", subtotal.toString());
			responseJsonObject.addProperty("payment", payment.equals("MONTH")?"月":"現");
			
		    responseJsonArray.add(responseJsonObject);
		}
		
		close();
		return responseJsonArray;

	}
	
	
	public List<InvoiceRecord> getAllMonthlyMedicineRecord(String startDate, String endDate, String patientName) throws SQLException, ClassNotFoundException {
		// Statements allow to issue SQL queries to the database
		getConnection();
        statement = connection.createStatement();
        String query = "select rid, medicine_name, create_on, subtotal, charge_on "
        		+ "from medicine_record where create_on >='" + startDate + "' "
        		+ "and create_on <='" + endDate +"' "
        		+ "and patient_name ='" + patientName + "' "
        		+ "and payment='MONTH' "
        		//+ "and charge_on is null "
        		+ "order by create_on asc;";
        
        resultSet = statement
                .executeQuery(query);
      
        List<InvoiceRecord> invoiceResult = getMedicineRecordResultSet(resultSet);
        close();
        return invoiceResult;
	}

	
	public ResultSet getAllReceiptRecord(String startDate) throws SQLException, ClassNotFoundException {
		// Statements allow to issue SQL queries to the database
		getConnection();
        statement = connection.createStatement();
        String query = "select rid, patient_name, medicine_name, create_on, subtotal  "
        		+ "from medicine_record where "
        		+ "receipt_on='" + startDate + "' "
        		+ "order by patient_name asc, create_on asc;";
        
        System.out.println("receipt history query :" 
        		+ query );
        
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
	}
	
	
	
	private List<InvoiceRecord> getMedicineRecordResultSet(ResultSet resultSet) throws SQLException {
			
			List<InvoiceRecord> list = new ArrayList();
	        // ResultSet is initially before the first data set
	        while (resultSet.next()) {
	        	Integer rid = resultSet.getInt("rid");
	            String medicineName = resultSet.getString("medicine_name");
	            Date createOn = resultSet.getDate("create_on");
	            Integer subtotal = resultSet.getInt("subtotal");
	            Date chargeOn = resultSet.getDate("charge_on");
	            
	            
	            InvoiceRecord r = new InvoiceRecord(rid, createOn.toString(), medicineName, subtotal);
	           
	            list.add(r);    
	        }
	        
	        return list;
	    }
	// You need to close the resultSet
    public void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {

        } finally {
        	resultSet = null;
        	statement = null;
        }
    }
	

}
