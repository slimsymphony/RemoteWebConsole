<%@ page contentType="text/html; charset=UTF-8" %><%@ page import="java.util.*" %><%@ page import="java.io.*" %><%
/*	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	request.setCharacterEncoding("UTF-8");*/
	//获取指定路径下的文件
	String filePath = request.getParameter("FilePath");
	int type = Integer.parseInt(request.getParameter("type"));
	if(type==1)//直接显示文本
	{
		FileReader fr = null;
		try{
			fr = new FileReader(filePath);
			
			if(fr==null)
			{
				out.println("File not Found!");
				return;
			}
			
			response.setContentType( "text/html; charset=UTF-8" );
			//org.apache.commons.io.CopyUtils.copy(fr,out);
			char[] data = new char[4096];
			int n=0;
			while( -1 != (n = fr.read(data))){
				out.write(data,0,n);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(fr!=null)
				try{fr.close();}catch(Exception ex1){ex1.printStackTrace();}
			/*if(out!=null)
				try{out.close();}catch(Exception ex2){ex2.printStackTrace();}*/
		}
	}
	else if(type==2)// 下载
	{
		FileInputStream fin = null;
		ServletOutputStream os = response.getOutputStream();
		try{
			response.setContentType( "application/x-msdownload" );
			response.setHeader( "Content-Disposition", "attachment; filename=" + new File(filePath).getName() );
			fin = new FileInputStream(filePath);
			//org.apache.commons.io.CopyUtils.copy(fin,os);
			byte[] data = new byte[4096];
			int n=0;
			while( -1 != (n = fin.read(data))){
				os.write(data,0,n);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(fin!=null)
				try{fin.close();}catch(Exception ex1){ex1.printStackTrace();}
		}
	}
%>