package serviceImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import service.IOService;

public class IOServiceImpl implements IOService{
	
	public static boolean createUser(String userId,String password){
		//检查是否存在该用户
		//如果存在，显示该用户已经存在
		//如果不存在，创建该用户的文件夹和用户资料（一个题目为ID，内容为密码的文本）,文件列表资料（里面为该用户所有文件的name，用“,”分隔）
		File userFile = new File("F:\\workspace\\Data\\" + userId );
		
		if(!userFile.exists()){
			userFile.mkdirs();
			File defaultFile = new File("F:\\workspace\\Data\\" + userId +"\\" + "defaultFile");
			File messageFile = new File("F:\\workspace\\Data\\" + userId +"\\" + userId + ".txt");
			File listFile = new File("F:\\workspace\\Data\\" + userId +"\\" + "list.txt");
			try {
				defaultFile.mkdirs();
				messageFile.createNewFile();
				listFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			FileWriter fileWriter;
			try {
				fileWriter = new FileWriter(messageFile);
				fileWriter.write(password);
				fileWriter.flush();
				fileWriter.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return false;
		
	}
	
	public static String getRightPassword(String userId){
		//获得用户对应的正确密码
		File messageFile = new File("F:\\workspace\\Data\\" + userId +"\\" + userId + ".txt");

		if(!messageFile.exists()){
			return "There is not exist.";
		}
		
		String rightPassword = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		
		try {
			fileReader = new FileReader(messageFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		bufferedReader = new BufferedReader(fileReader);
			
		try {
			rightPassword = bufferedReader.readLine();
			fileReader.close();
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return rightPassword;		
	}
	
	//在服务端创建一个新文件
	@SuppressWarnings("resource")
	public boolean createFile(String code, String userId, String fileName) {
		
		//文件名不能为空
		if(fileName == "") {
			return false;
		}
		
		//文件名不允许包含下划线
		if(fileName.contains("_")){
			return false;
		}
		
		LocalDate localDate = LocalDate.now();
		LocalTime localTime = LocalTime.now();
		
		String saveTime = localDate.toString() + "_" 
						+ localTime.toString().substring(0, 2)
						+ localTime.toString().substring(3, 5)
						+ localTime.toString().substring(6, 8);
		
		//未登录就使用默认的文件夹，所有人都可以进行访问
		//在用户文件夹里检查是否有相同文件名，如果没有，创建新文件夹，文件夹的名字为该文件名
		File newFile = new File("F:\\workspace\\Data\\" + userId +"\\" + fileName );
		if(!newFile.exists()){
			newFile.mkdirs();
		} else {
			return false;
		}
		
		newFile = new File("F:\\workspace\\Data\\" + userId +"\\" + fileName + "\\" + fileName +"_" + saveTime + ".txt");
		File listFile = new File("F:\\workspace\\Data\\" + userId +"\\" + "list.txt");
		
		//在服务端创建新版本文件
		
		try {
			newFile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
				
		//在文件列表list记录文件名
		FileWriter fileWriter;
		
		try {
			fileWriter = new FileWriter(newFile);
			fileWriter.write(code);
			fileWriter.flush();
			fileWriter = new FileWriter(listFile,true);
			fileWriter.write(new String(fileName+","));
			fileWriter.flush();
			fileWriter.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//在服务端保存新版本
	@Override
	public boolean writeFile(String code, String userId, String fileName) {
		
		LocalDate localDate = LocalDate.now();
		LocalTime localTime = LocalTime.now();
		
		String saveTime = localDate.toString() + "_" 
						+ localTime.toString().substring(0, 2)
						+ localTime.toString().substring(3, 5)
						+ localTime.toString().substring(6, 8);
		
		//未选择文件，就使用默认文件名
		File saveFile = new File("F:\\workspace\\Data\\" + userId +"\\" + fileName + "\\" + fileName +"_" + saveTime + ".txt");
		
		//在服务端创建该文件的新版本文件
		try {
			saveFile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//检查两次代码是否更改
		String lastCode = null;
		String[] list = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
	
		File lastVersion = new File("F:\\workspace\\Data\\" + userId +"\\" + fileName);
		list = lastVersion.list();
		
		try {
			fileReader = new FileReader(new File("F:\\workspace\\Data\\" + userId +"\\" + fileName + "\\" +list[0]));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		bufferedReader = new BufferedReader(fileReader);

		System.out.println(list[0]);
		
		try {
			lastCode = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(code.equals(lastCode)) {
			return false;
		}
		
		//将传入的代码内容写入新版本文件，并且在文件列表list记录文件名
		FileWriter fileWriter;
		
		try {
			fileWriter = new FileWriter(saveFile);
			fileWriter.write(code);
			fileWriter.flush();
			fileWriter.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	//从服务端读取文件
	@Override
	public String readFile(String userId, String fileName, String versionName) {

		File file = new File("F:\\workspace\\Data\\" + userId + "\\" + fileName + "\\" + versionName + ".txt");
		
		String code = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		bufferedReader = new BufferedReader(fileReader);
			
		try {
			code = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			fileReader.close();
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return code;
	}

	//从服务端读取文件列表
	@Override
	public String readFileList(String userId) {
		File listFile = new File("F:\\workspace\\Data\\" + userId +"\\" + "list.txt");
		
		String listStr = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		
		try {
			fileReader = new FileReader(listFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		bufferedReader = new BufferedReader(fileReader);
			
		try {
			listStr = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return listStr;
	}

	//从服务端读取文件的版本列表
	public String[] readVersionList(String userId,String fileName) {
		File readFile; 
		readFile = new File("F:\\workspace\\Data\\" + userId +"\\" + fileName );
		String[] versionList = readFile.list();
		return versionList;
	}
	
	//从服务端删除文件的某个版本
	public boolean deleteVersion(String userId, String fileName, String versionName) {
		File deleteFile = new File("F:\\workspace\\Data\\" + userId +"\\" + fileName + "\\" + versionName + ".txt");
		boolean result = deleteFile.delete();
		return result;
	}
	
	//从服务端删除文件
	@Override
	public boolean deleteFile(String userId, String fileName) {
		
		//默认文件不能删除
		if(fileName.equals("default_file")) {
			return false;
		}
		
		File deleteFile = new File("F:\\workspace\\Data\\" + userId + "\\" + fileName );
		String[] fileList = deleteFile.list(); 
		
		//删除该文件夹下面的所有文件
		for(String file:fileList){
			new File("F:\\workspace\\Data\\" + userId + "\\" + fileName + "\\" + file).delete();
		}
		
		//删除该文件夹
		deleteFile.delete();
		
		//删除掉list中的文件名
		File listFile = new File("F:\\workspace\\Data\\" + userId +"\\" + "list.txt");
		
		String listStr = null;
		String newList = "";
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		
		try {
			fileReader = new FileReader(listFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		bufferedReader = new BufferedReader(fileReader);
			
		try {
			listStr = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(String str: listStr.split(",")) {
			if(!str.equals(fileName)){
				newList = newList + fileName + ",";
			}
		}
		
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		
		try {
			fileWriter = new FileWriter(listFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		bufferedWriter = new BufferedWriter(fileWriter);
		
		try {
			bufferedWriter.write(newList);
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
}
