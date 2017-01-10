package runner;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import rmi.RemoteHelper;
import ui.MainFrame;

public class ClientRunner {
	
	private static boolean isLogin = false;
	private static String userId = "defaultUer";
	private static String fileName = "defaultFile";
	
	private RemoteHelper remoteHelper;
	
	public ClientRunner() {
		linkToServer();
		initGUI();
	}
	
	public static boolean isLogin() {
		return isLogin;
	}
	public static void setLogin(boolean isLogin) {
		ClientRunner.isLogin = isLogin;
	}
	public static String getUserId() {	
		return userId;
	}
	public static void setUserId(String userId) {	
		ClientRunner.userId = userId;
	}
	public static String getFileName() {	
		return fileName;
	}
	public static void setFileName(String fileName) {	
		ClientRunner.fileName = fileName;
	}

	//进行RMI连接
	private void linkToServer() {
		try {
			remoteHelper = RemoteHelper.getInstance();
			remoteHelper.setRemote(Naming.lookup("rmi://localhost:2333/DataRemoteObject"));
			System.out.println("linked");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	//绘制界面
	private void initGUI() {
		new MainFrame();
	}
	
	//启动客户端
	public static void main(String[] args){
		new ClientRunner();
	}
	
}
