
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "InvoiceTwoServlet", urlPatterns = "/api/invoiceTwo")
public class InvoiceTwoServlet extends HttpServlet {

	PrintWriter out;
	AnhoDatabaseService anhoDBservice = AnhoDatabaseService.getInstance();
	List<InvoiceRecord> invoiceResult;
	Map<String, Integer> visit;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String patientName = request.getParameter("patientName");
		String monthDate = request.getParameter("date");
		System.out.println("patientName in invoice1 is " + monthDate + " " + patientName);
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		try {
			invoiceResult = anhoDBservice.getAllMonthlyMedicineRecord(monthDate + "-01", monthDate + "-31", patientName);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    out = response.getWriter();

	    writeInvoiceHtmlStyle();
	    out.println("<h3><i>安禾聯合診所</i></h3>\n");
 		out.println("<h4>" + patientName +"&nbsp;&nbsp;&nbsp;&nbsp;"
 				//+ "醫療明細&nbsp;&nbsp;" 
 				+ monthDate +"月份 "+"</h4>\n");
	    
	    writeInvoiceHtmlTableHeader();
	    
	    //write some content
	    int total = 0;
	    visit = new LinkedHashMap();
	    for(InvoiceRecord r : invoiceResult) {
	    	String key = r.getCreateOn();
			if(visit.containsKey(key)) {
				Integer value = visit.get(key);
				int dateTotal = r.getSubtotal().intValue() + value.intValue();
				visit.put(key, dateTotal);
			} else {
				visit.put(key, r.getSubtotal());
			}
			total += r.getSubtotal();
		}
	   
	    for(Map.Entry<String, Integer> e : visit.entrySet()) {
	    	out.println( "<tr>\n"
					+ "<td>" + e.getKey() + "</td>\n"
					+"<td>" + e.getValue() + "</td>\n"
					+"</tr>\n");
	    }
	    out.println("</tbody>\n</table>\n");
	    		out.println("<h5>" + "看診共" + visit.size() +"次&nbsp;&nbsp;&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp;" 
 				+"總金額：" + total + "元" + "</h5>\n"
	    		+ "</body>\n</html>");
	   
	}
	
	private void writeInvoiceHtmlStyle() throws IOException {
		out.println("<!DOCTYPE html>\n");
		out.println("<html>\n" + "<head>\n" + "<style>\n");
		out.println("h3, h4 {\n margin-top: 1px;\n "
				+ "margin-bottom: 1px;\n " 
				+ "padding: 0px;\n"
				+ "\n}\n");
		out.println("table {\n    border-collapse: collapse;\n}\n" + 
		"td, th {\n    border: 1px solid #dddddd;\n"
		        + "    text-align: left;\n"
		        + "    padding: 8px;\n"
		        + "    font-size: 10pt;\n}\n");
		out.println("</style>\n"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n"
						+ "</head>\n" + "<body>\n" );
	}
	
	
	private void writeInvoiceHtmlTableHeader() {
		out.println("<table>\n"
				+ "<tr>\n"
				+ "<th>就診日</th>\n"
				+"<th>自費額</th>\n"
				+"</tr>\n");
	}
	
	public void writeInvoiceRecordtoTable(InvoiceRecord r) throws IOException {
		
		out.println( "<tr>\n"
				+ "<td>" + r.getCreateOn() + "</td>\n"
				+"<td>" + r.getSubtotal() + "</td>\n"
				+"</tr>\n");
	}
	
	

}
