package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import service.ExecuteService;
import service.IOService;
import service.UserService;
import serviceImpl.ExecuteServiceImpl;
import serviceImpl.IOServiceImpl;
import serviceImpl.UserServiceImpl;

public class DataRemoteObject extends UnicastRemoteObject implements IOService, UserService, ExecuteService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4029039744279087114L;
	private IOService iOService;
	private UserService userService;
	private ExecuteService executeService;
	
	protected DataRemoteObject() throws RemoteException {
		iOService = new IOServiceImpl();
		userService = new UserServiceImpl();
		executeService = new ExecuteServiceImpl();
	}

	@Override
	public boolean createFile(String code, String userId, String fileName) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.createFile(code, userId, fileName);
	}
	
	@Override
	public boolean writeFile(String file, String userId, String fileName) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.writeFile(file, userId, fileName);
	}

	@Override
	public String readFile(String userId, String fileName, String versionName) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.readFile(userId, fileName, versionName);
	}

	@Override
	public String readFileList(String userId) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.readFileList(userId);
	}

	@Override
	public String[] readVersionList(String userId, String fileName) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.readVersionList(userId, fileName);
	}

	public boolean deleteVersion(String userId, String fileName, String versionName) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.deleteVersion(userId, fileName, versionName);
	}
	
	@Override
	public boolean deleteFile(String userId, String fileName) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.deleteFile(userId, fileName);
	}
	
	@Override
	public boolean join(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.join(username, password);
	}
	
	@Override
	public boolean login(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.login(username, password);
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.logout(username);
	}

	@Override
	public String execute(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		return executeService.execute(code, param);
	}

}
