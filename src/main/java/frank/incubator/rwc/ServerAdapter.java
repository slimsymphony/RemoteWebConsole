package frank.incubator.rwc;

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
			String executable = receive(request);
			String result = exec(executable);
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

	private String exec(String cmd) {
		String ret = null;
		try {
			ShellResult sr = ShellUtils.execute(cmd);
			ret = sr.getOutputMsg();
			ret += "<"+sr.getErrorMsg()+">";
		}catch(Exception e) {
			e.printStackTrace();
			ret = e.getMessage();
		}
		return ret;
	}

	private String receive(HttpServletRequest request) {
		// 参数格式定制
		String cmd = request.getParameter("cmd");
		return cmd;
	}
}
