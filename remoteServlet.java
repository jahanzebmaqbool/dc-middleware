import java.rmi.*;
import java.io.*;
import java.text.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class remoteServlet extends HttpServlet {

   public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        String s1 = request.getParameter("choice");
        int x = Integer.parseInt(s1);
	int result = 0;
        try {
        caller obj = (caller)Naming.lookup("rmi://10.3.20.120/caller");
        switch(x)
	{
		case 0: result = obj.multiply();break;
		case 1: result = obj.addition();break;
		//case 2: result = obj.subtraction();break;
	} 
	 
        } catch (Exception e) {
          System.out.println("remoteServlet exception:"+e.getMessage());
          e.printStackTrace();
        }
        out.println("Result: "+result);
    }
}
