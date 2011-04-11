<%@ page contentType="text/html; charset=UTF-8" %>
<html>
	<head>
		<title>RWC</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<style>
			.screen{
				position : absolute;top:130px;left:150px;bottom:120px;right:200px;width:75%;height:60%;
				border: dashed;
				background-color:black;
				font-family:Courier;
				font-size:9pt;
				color:#00FF00;
				##overflow: auto;
			}
			.pathDiv{
				position : absolute;top:110px;left:100px;bottom:100px;right:100px;
				border:outset;
				background-color:#ffffff;
				font-family: Arial;
				font-size:9pt;
				##color:#00FF00;
				##overflow: auto;
				z-index: 100;
			}
			.cmdline{
				position :relative; 
			}
		</style>
		<script src="dojo/dojo.js" djConfig="parseOnLoad: true"></script> 
		<script type="text/javascript">
			 
			var djConfig = {
				//require:['dojo.io.iframe']
			}
			var currentPath='';
			//dojo.require("dojo.io.iframe");
			dojo.require("dojo.io.iframe"); 
			
			function send(){
				var cmd = dojo.trim(dojo.byId("console").value);
				dojo.byId("console").value = "";
				var content = {"cmd":cmd};
				if(currentPath!='')
					content = {"cmd":cmd,"base":currentPath};
				if(cmd!=''){
					var xhrArgs = {
						url: "adapter",
						content: content,
						handleAs: "text",
						preventCache: true,
						load: function(data){
							//alert(data);
							var screen = dojo.byId("screen");
							//screen.innerHTML += data+"<br/>";
							screen.value += data+"\r\n";
							screen.scrollTop = screen.scrollHeight - screen.clientHeight;
						},
						error: function(error){
							alert(error);
							screen.innerHTML = "An unexpected error occurred: " + error;
						}
					}
					dojo.xhrPost(xhrArgs);
				}
			}
			
			function press(e){
				if (e.keyCode == dojo.keys.ENTER)
	            {
					send();
	            }
			}
			
			function clearScreen(){
				var screen = dojo.byId("screen");
				//screen.innerHTML = '';
				screen.value = '';
			}
			
			function selPath(){
				if (currentPath == '') {
					currentPath = window.prompt("请输入基准路径");
				}
				var pathDiv = dojo.byId("pathDiv");
				pathDiv.style.display='block';
				var pf = dojo.byId("pathFrame");
				pf.src = 'path.jsp?path='+currentPath;
			}
			
			function resetPath(){
				dojo.byId("currPathSpan").innerHTML = '';
				currentPath = '';
			}
			
			function uploadFile(){
				var file = dojo.byId("file");
				if(file.value!=''){
					if (currentPath == '') {
						currentPath = window.prompt("请输入基准路径");
						dojo.byId("currPathSpan").innerHTML = currentPath;
					}
					if(currentPath == '' || dojo.trim(currentPath)=='')
						return false;
					dojo.byId("uploadPath").value=currentPath;
					
					dojo.io.iframe.send({
						url:'receiveFile',
						form: "upForm", //某个form元素包含本地文件路径
					    handleAs: "html", //服务器将返回html
					    load: function(data){
							alert(data.body.firstChild.innerHTML);
							//alert(data.getElementsByTagName[0].value);
						},
					    error: function(error){
							alert(error);
							screen.value = "An unexpected error occurred: " + error;
						}
					});
				}
			}
			
			function init(){
				var console = dojo.byId("console");
				dojo.connect(console,"onkeypress",press);
				var cb = dojo.byId("clearBtn");
				dojo.connect(cb,"onclick",clearScreen);
				var pb = dojo.byId("pathBtn");
				dojo.connect(pb,"onclick",selPath);
				var rb = dojo.byId("resetBtn");
				dojo.connect(rb,"onclick",resetPath);
				var ub = dojo.byId("uploadBtn");
				dojo.connect(ub,"onclick",uploadFile);
			}
			
			dojo.addOnLoad(init);
		</script>
	</head>
	<body>
		<iframe id="fileupload" style="display:none"></iframe>
		<!--div id="screen" class="screen"></div-->
		<div id="pathDiv" style="display:none" class="pathDiv">
			<iframe width="99%" height="99%" id="pathFrame" src="path.jsp?path=/"></iframe>
		</div>
		<textarea id="screen" class="screen"></textarea>
		<input class="cmdline" type="text" size="80" id="console" />
		<button id="pathBtn">设定执行路径</button>
		<button id="clearBtn">清空屏幕</button>
		<span id="currPathSpan"></span>
		<button id="resetBtn">重置路径</button>
		<button id="uploadBtn">上传文件</button>
		<form name="upForm" id="upForm" method="post" action="receiveFile" enctype="multipart/form-data">
			<input type="file" name="file" id="file"/>
			<input type="hidden" name="uploadPath" id="uploadPath" value="" />
		</form>
	</body>
</html>
