<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %><%@ page import="java.io.*" %>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	request.setCharacterEncoding("UTF-8");
	String filePath = request.getParameter("path");
	File parent = new File (filePath);
	if(!parent.exists())
	{
		out.println(filePath+" not exist!");
		return;
	}
%>
<html>
	<head>
		<title>RWC_PATH</title>
		<meta content="text/html; charset=UTF-8"/>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<script type="text/javascript">
			function selPath(path){
				window.parent.currentPath = path;
				var pathDiv = window.parent.document.getElementById("pathDiv");
				pathDiv.style.display="none";
				window.parent.document.getElementById("currPathSpan").innerHTML=path;
			}
		</script>
	</head>
	<body>
	<%
	if(parent.isDirectory())
	{
		File[] fls = parent.listFiles();
		out.print("当前路径");
		out.print("<button onclick=\"selPath('"+filePath+"')\">选定</button>");
		out.println("<br/>");
		for(int i=0;i<fls.length;i++)
		{
			if(fls[i].isDirectory())
			{
				out.print("<strong>D</strong> ");
				out.print("<a href='path.jsp?path="+filePath+"/"+fls[i].getName()+"'>");
				out.print(fls[i].getName());
				out.print("</a>");
				out.print("<button onclick=\"selPath('"+filePath+"/"+fls[i].getName()+"')\">选定</button>");
			}
			else if(fls[i].isFile())
			{
				out.print("<strong>F</strong> ");
				out.print("<a href='FileGetter.jsp?type=1&path="+filePath+"/"+fls[i].getName()+"'>");
				out.print(fls[i].getName());
				out.print("</a>&nbsp;&nbsp;");
				out.print(((float)fls[i].length())/(float)1024);
				out.print("KB");
				out.print("<button onclick=\"window.open('FileDeleter.jsp?path="+filePath+"/"+fls[i].getName()+"');\">删除</button>");
				out.print("<button onclick=\"window.open('FileGetter.jsp?type=2&path="+filePath+"/"+fls[i].getName()+"');\">下载</button>");
				out.print("<button onclick=\"window.open('FileUpdater.jsp?path="+filePath+"/"+fls[i].getName()+"');\">修改</button>");
			}
			else
			{
				out.print("<strong>?</strong> ");
				out.print(fls[i].getName());
			}
			out.println("<br/>");
		}
	}%>
	</body>
</html>