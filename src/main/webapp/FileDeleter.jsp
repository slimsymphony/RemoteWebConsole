<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %><%@ page import="java.io.*" %>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	request.setCharacterEncoding("UTF-8");
	String filePath = request.getParameter("FilePath");
	File file = new File(filePath);
	if(!file.exists())
	{
		out.println(filePath+" not found!");
		return;
	}
	String result = " is deleted!";
	try{
		file.delete();
	}catch(Exception ex){
		result += " delete Error:"+ex.toString();
	}
	out.println(filePath+result);
	
%>		