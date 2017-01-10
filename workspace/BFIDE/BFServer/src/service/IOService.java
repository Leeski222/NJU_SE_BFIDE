//需要客户端的Stub
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IOService extends Remote{
	
	public boolean createFile(String code, String userId, String fileName) throws RemoteException;
	
	public boolean writeFile(String file, String userId, String fileName) throws RemoteException;
	
	public String readFile(String userId, String fileName, String versionName) throws RemoteException;
	
	public String readFileList(String userId) throws RemoteException;
	
	public String[] readVersionList(String userId,String fileName) throws RemoteException;
	
	public boolean deleteVersion(String userId, String fileName, String versionName) throws RemoteException ;
	
	public boolean deleteFile(String userId, String fileName) throws RemoteException;
}
