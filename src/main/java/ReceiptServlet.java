import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet(name = "ReceiptServlet", urlPatterns = "/api/receipt")
public class ReceiptServlet extends HttpServlet{

	private static final long serialVersionUID = -4918270811204914860L;
	AnhoDatabaseService anhoDBService = AnhoDatabaseService.getInstance();

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  System.out.println("in receipt servlet");
		  request.setCharacterEncoding("UTF-8");
		  
		  InputStream inputStream = request.getInputStream();
		  JsonParser jsonParser = new JsonParser();
		  JsonObject jsonObject = (JsonObject)jsonParser.parse(new InputStreamReader(inputStream, "UTF-8"));
		  JsonArray jsonArray = jsonObject.getAsJsonArray("id");
		  int importMonth = jsonObject.get("importMonth").getAsInt();
		  
		  System.out.println("the importMonth is" + importMonth);
		  
		  Iterator<JsonElement> it= jsonArray.iterator();
		  List<Integer> RIDs = new ArrayList();
		  while(it.hasNext()) {
			  Integer data = it.next().getAsInt();
			  RIDs.add(data);
		  }
		  
		  int result = 0;
		  try {
			result = anhoDBService.updateReceiptOnByRid(RIDs, importMonth );
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  
		  
		  System.out.println(jsonObject.toString());
		  response.setCharacterEncoding("UTF-8"); 
		  if(result == -1 || result != RIDs.size()) {
			  response.getWriter().write("fail");
		  } else {
			  response.getWriter().write("success");
		  }
		  
    }
}
