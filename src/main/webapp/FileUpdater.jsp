<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %><%@ page import="java.io.*" %>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	request.setCharacterEncoding("UTF-8");
	String filePath = request.getParameter("FilePath");
	File parent = new File (filePath);
	StringBuffer sb = new StringBuffer(1000);
	if(!parent.exists())
	{
		out.println(filePath+"文件未找到");
		return;
	}
	else if(!parent.isFile())
	{
		out.println(filePath+"不是文件");
		return;
	}
	if(request.getParameter("isSubmit")!=null)
	{
		FileOutputStream fos = new FileOutputStream(parent);
		String content = request.getParameter("content");
		byte[] data = content.getBytes("UTF-8");
		fos.write(data);
		fos.flush();
		fos.close();
		out.println("<script>alert('修改成功');window.close();</script>");
	}
	StringWriter sw = new StringWriter();
	FileReader fr = new FileReader(parent);
	org.apache.commons.io.CopyUtils.copy(fr,sw);
	sw.close();
	fr.close();
	sb.append(sw.toString());
%>
<HTML>
 <HEAD>
  <TITLE> 修改jsp文件 </TITLE>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">    
 </HEAD>
<script language="JavaScript">
<!--
	function onSub()
	{
		form1.submit();
	}
//-->
</script>
 <BODY>
<form name="form1" action="" method="post">
	<%=filePath%><br/>
  <textarea cols="80" rows="10" name="content"><%=sb.toString()%></textarea>
  <input type="button" name="update" value="修改" onclick="onSub()"/>
  <input type="hidden" name="isSubmit" value="1"/>
  <input type="hidden" name="FilePath" value="<%=filePath%>"/>
</form>
 </BODY>
</HTML>
