package frank.incubator.rwc;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ShellUtils
{

	public static void main( String args[] ) throws Exception
	{
		execute( args[0] );
	}

	public static ShellResult execute( String givenCmd ) throws Exception
	{
		int exitVal = -99;
		ShellResult sr = null;
		try
		{
			sr = new ShellResult();
			String osName = System.getProperty( "os.name" );
			String[] cmd = new String[ 3 ];
			if ( osName.startsWith( "Windows" ) )
			{
				if ( osName.equals( "Windows 95" ) )
				{
					cmd[0] = "command.com ";
					cmd[1] = "/C ";
					cmd[2] = givenCmd;
				}
				else
				{
					cmd[0] = "cmd.exe ";
					cmd[1] = "/C ";
					cmd[2] = givenCmd;
				}
			}
			else
			{
				cmd[0] = "";
				cmd[1] = "";
				cmd[2] = givenCmd;
			}

			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec( cmd[0] + "" + cmd[1] + "" + cmd[2] );
			// 开始获取输出
			ByteArrayOutputStream bss = null;
			InputStream in = null;
			bss = new ByteArrayOutputStream();
			in = proc.getInputStream();
			byte[] data = new byte[1000];
			while(in.read(data)!=-1) {
				bss.write(data);
			}
			bss.flush();
			//CopyUtils.copy( in, bss );
			String outputMsg = new String( bss.toByteArray() );
			bss.close();
			in.close();
			in = proc.getErrorStream();
			while(in.read(data)!=-1) {
				bss.write(data);
			}
			bss.flush();
			//CopyUtils.copy( in, bss );
			String errMsg = new String( bss.toByteArray() );
			bss.close();
			in.close();
			// 获取结束
			exitVal = proc.waitFor();
			sr.setResult( exitVal );
			sr.setErrorMsg( errMsg );
			sr.setOutputMsg( outputMsg );
			if ( exitVal != 0 )
			{// 非正常结束
				throw new Exception( "[ERROR_MSG:" + errMsg + "],[OUPUT_MSG:" + outputMsg + "]" );
			}

		}
		catch ( Exception t )
		{
			throw t;
		}
		return sr;
	}
}

class ShellResult
{
	private int result;

	private String outputMsg;

	public int getResult()
	{
		return result;
	}

	public void setResult( int result )
	{
		this.result = result;
	}

	public String getOutputMsg()
	{
		return outputMsg;
	}

	public void setOutputMsg( String outputMsg )
	{
		this.outputMsg = outputMsg;
	}

	public String getErrorMsg()
	{
		return errorMsg;
	}

	public void setErrorMsg( String errorMsg )
	{
		this.errorMsg = errorMsg;
	}

	private String errorMsg;
}
