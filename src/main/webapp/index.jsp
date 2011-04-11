<html>
	<head>
		<title>RWC</title>
		<meta content="text/html; charset=UTF-8"/>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<style>
			.screen{
				position : absolute;top:130px;left:200px;bottom:120px;right:200px;
				border: dashed;
				background-color:black;
				font-family:Courier;
				font-size:9pt;
				color:#00FF00;
				##overflow: auto;
			}
			.cmdline{
				position :relative; 
			}
		</style>
		<script src="dojo_1.5.js"></script> 
		<script type="text/javascript">
			function send(){
				var cmd = dojo.trim(dojo.byId("console").value);
				dojo.byId("console").value = "";
				var content = {"cmd":cmd};
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
			
			function init(){
				var console = dojo.byId("console");
				dojo.connect(console,"onkeypress",press);
				var cb = dojo.byId("clearBtn");
				dojo.connect(cb,"onclick",clearScreen);
			}
			
			dojo.addOnLoad(init);
		</script>
	</head>
	<body>
		<!--div id="screen" class="screen"></div-->
		<textarea id="screen" class="screen"></textarea>
		<input class="cmdline" type="text" size="80" id="console" /><button id="clearBtn">clear</button>
	</body>
</html>
