import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//
@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
//        System.out.println(username + password);

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */
        if (username.equals("anhouser") && password.equals("anho20097869")) {
            // Login success:

            // set this user into the session
            request.getSession().setAttribute("user", new User(username));

            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");

            response.getWriter().write(responseJsonObject.toString());
        } else {
            // Login fail
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            if (!username.equals("anhouser")) {
                responseJsonObject.addProperty("message", "使用者 " + username + "不存在");
            } else if (!password.equals("anho20097869")) {
                responseJsonObject.addProperty("message", "密碼錯誤");
            }
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(responseJsonObject.toString());
        }
    }
}
