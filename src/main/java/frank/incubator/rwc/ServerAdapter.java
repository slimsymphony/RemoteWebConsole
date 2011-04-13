package frank.incubator.rwc;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
			System.out.println(System.getProperty("user.dir").replaceAll("\\\\","/"));
			@SuppressWarnings("unchecked")
			Set<String> paramNames = request.getParameterMap().keySet();
			String cmd = request.getParameter("cmd");
			String base = request.getParameter("base");
			List<String> envs = new ArrayList<String>();
			for(String name : paramNames) {
				if(name.equals("cmd"))
					continue;
				if(name.equals("base"))
					continue;
				envs.add(name+"="+request.getParameter(name));
			}
			 
			
			File basePath = null;
			if(base!=null&&!base.trim().equals(""))
				basePath = new File(base);
			else {
				basePath = new File(System.getProperty("user.dir").replaceAll("\\\\","/"));
			}
			String result = exec(cmd,basePath,envs.toArray(new String[0]));
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

	private String exec(String cmd,File base,String[] envs) {
		String ret = null;
		if(envs.length==0)
			envs = null;
		try {
			ShellResult sr = ShellUtils.execute(cmd,envs,base);
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
