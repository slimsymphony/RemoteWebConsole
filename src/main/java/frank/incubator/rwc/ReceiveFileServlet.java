package frank.incubator.rwc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

public class ReceiveFileServlet extends HttpServlet {

	private static final long serialVersionUID = -7406008442313730703L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		response.getWriter().write("不支持Get方式传输，请使用Post方式");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		ServletFileUpload sfu = new ServletFileUpload(new DiskFileItemFactory());
		String path = "";
		FileItem fi = null;
		FileOutputStream fos = null;
		InputStream in = null;
		try {
			List<FileItem> list = sfu.parseRequest(request);
			//FileItemIterator it = sfu.getItemIterator(request);
	
			for(FileItem item : list) {
				if(item.isFormField()) {
					if("uploadPath".equals(item.getFieldName()))
						path = item.getString();
				}else {
					fi = item;
				}
			}
			File file = new File(path,fi.getName());
			fos = new FileOutputStream(file);
			in = fi.getInputStream();
			IOUtils.copy(in, fos);
			fos.flush();
			response.setCharacterEncoding("UTF-8");
			response.setContentType( "text/plain; charset=UTF-8" );  
			response.getWriter().write("完成上传，文件存储在"+file.getAbsolutePath());
		} catch (FileUploadException e) {
			e.printStackTrace();
		} finally {
			if(fos!=null) fos.close();
			if(in!=null) in.close();
		}
	}

}
