package frank.incubator.rwc;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作为所有前端服务的代理,接收用户发布的请求，同时在服务器端执行后将结果反馈给用户
 * 
 * @author frank
 * 
 */
public class ServerAdapter extends HttpServlet {
	private static final long serialVersionUID = 7099744625181127555L;
	private String base;

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
		try {
			String cmd = request.getParameter("cmd");
			String base = request.getParameter("base");
			File basePath = null;
			if(base!=null&&!base.trim().equals(""))
				basePath = new File(base);
			String result = exec(cmd,basePath);
			sendBack(result,response);
		}catch(Exception ex) {
			ex.printStackTrace();
			sendBack(ex.getMessage(),response);
		}
	}
	
	private void sendBack(String result, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		Writer w = null;
		try {
			w = response.getWriter();
			w.write(result);
		}finally {
			if(w!=null) {
				w.close();
			}
		}
	}

	private String exec(String cmd,File base) {
		String ret = null;
		try {
			ShellResult sr = ShellUtils.execute(cmd,base);
			ret = sr.getOutputMsg();
			String err =sr.getErrorMsg();
			if(err!=null && !err.trim().equals(""))
				ret += "["+err+"]";
		}catch(Exception e) {
			e.printStackTrace();
			ret = e.getMessage();
		}
		return ret;
	}
	
}
