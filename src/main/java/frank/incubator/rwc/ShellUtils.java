package frank.incubator.rwc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ShellUtils {
	
	public static void main(String[] args) {
		ShellResult sr = execute(args[0],new String[]{args[1]},new File(args[2]));
		System.out.println(sr.getOutputMsg());
		System.out.println(sr.getErrorMsg());
	}

	public static ShellResult execute(String givenCmd,String[] envs,File base) {
		int exitVal = -99;
		ShellResult sr = null;
		ByteArrayOutputStream bss = null;
		InputStream in = null;
		try {
			sr = new ShellResult();
			String osName = System.getProperty("os.name");
			String[] cmd = new String[3];
			if (osName.startsWith("Windows")) {
				if (osName.equals("Windows 95")) {
					cmd[0] = "command.com ";
					cmd[1] = "/C ";
					cmd[2] = givenCmd;
				} else {
					cmd[0] = "cmd.exe ";
					cmd[1] = "/C ";
					cmd[2] = givenCmd;
				}
			} else {
				cmd[0] = "";
				cmd[1] = "";
				cmd[2] = givenCmd;
			}

			Runtime rt = Runtime.getRuntime();
			System.out.println(cmd[0] + "" + cmd[1] + "" + cmd[2]+">>"+base.getAbsolutePath());
			Process proc = rt.exec(cmd[0] + "" + cmd[1] + "" + cmd[2],envs,base);
			// 开始获取输出

			bss = new ByteArrayOutputStream();
			in = proc.getInputStream();
			byte[] data = new byte[1000];
			int n = 0;
			while (-1 != (n = in.read(data))) {
				bss.write(data, 0, n);
			}
			bss.flush();
			String outputMsg = new String(bss.toByteArray());
			bss.close();
			in.close();
			bss = new ByteArrayOutputStream();
			in = proc.getErrorStream();
			n = 0;
			while (-1 != (n = in.read(data))) {
				bss.write(data, 0, n);
			}
			bss.flush();
			String errMsg = new String(bss.toByteArray());
			bss.close();
			in.close();
			// 获取结束
			exitVal = proc.waitFor();
			sr.setResult(exitVal);
			sr.setErrorMsg(errMsg);
			sr.setOutputMsg(outputMsg);
			if (exitVal != 0) {// 非正常结束
				throw new Exception("givenCmd:"+givenCmd+"[ERROR_MSG:" + errMsg + "],[OUPUT_MSG:"
						+ outputMsg + "]");
			}

		} catch (Exception t) {
			StringWriter sw =new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			sr.setErrorMsg(sw.toString());
		} finally {
			if(bss!=null)
				try {
					bss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(in!=null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return sr;
	}
}

class ShellResult {
	private int result;

	private String outputMsg;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getOutputMsg() {
		return outputMsg;
	}

	public void setOutputMsg(String outputMsg) {
		this.outputMsg = outputMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	private String errorMsg;
}
