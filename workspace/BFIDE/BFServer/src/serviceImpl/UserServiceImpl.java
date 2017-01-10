package serviceImpl;

import java.rmi.RemoteException;

import service.UserService;

public class UserServiceImpl implements UserService{
	
	public boolean join(String username,String password) throws RemoteException {
		boolean result = IOServiceImpl.createUser(username,password);
		return result;
	}

	//将获取的正确密码与输入密码进行比对
	@Override
	public boolean login(String username, String password) throws RemoteException {
		String rightPassword = IOServiceImpl.getRightPassword(username);
		
		if(password.equals("There is not exist.")){
			return false;
		}
		
		if(password.equals(rightPassword)){
			return true;
		} else {
			return false;
		}
		
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		return true;
	}

}
