package frank.incubator.rwc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthServlet extends HttpServlet {

	private static final long serialVersionUID = 1331448914002593070L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		service(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		service(request, response);
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String user = request.getParameter("user");
		String pass = request.getParameter("pass");
		if(user==null||pass==null)
		{
			response.getWriter().write("{'result':false}");
			return;
		}
		InputStream in = null;
		try {
			in = AuthServlet.class.getClassLoader().getResourceAsStream("encrypt.properties");
			Properties props = new Properties();
			props.load(in);
			String auser = (String)props.get("user");
			String apass = (String)props.get("pass");
			if ( user.equals( auser ) ) {
				String enPass = new String(EncryptUtils.encryptBASE64(pass.getBytes()));
				if( apass.equals(enPass.trim() )){
					response.getWriter().write("{'result':true}");
					return;
				}
			}
			response.getWriter().write("{'result':false}");
			return;
		}catch(Exception e) {
			throw new ServletException(e);
		} finally {
			if (in != null)
				in.close();
		}
	}
}
