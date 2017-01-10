//请不要修改本文件名
package serviceImpl;

import java.awt.Point;
import java.rmi.RemoteException;
import java.util.ArrayList;

import service.ExecuteService;

public class ExecuteServiceImpl implements ExecuteService {

	/**
	 * 请实现该方法
	 */
	@Override
	public String execute(String code, String param) throws RemoteException {
		int pointer = 0;
		char[] registers = new char[3000];

		String recordCode =code;
		String result = "";
		
		//将对应的“[”和“]”记录起来，方便后面进行循环
		int markX = 0;
		ArrayList<Point> loop = new ArrayList<Point>();
		for(int flag = 0; flag <code.length(); flag++){
			for(int num = 0; num < code.length(); num++){
				if(code.charAt(num) == '['){
					markX = num;
				}
				if(code.charAt(num) == ']'){
					loop.add(new Point(markX,num));
					code = code.substring(0,markX) 
							+ "0" 
							+ code.substring(markX+1,num)
							+ "0"
							+ code.substring(num+1,code.length());
					break;
				}
			}
		}
		
		code = recordCode;
		
		char[] signs =null;
		signs = new char[code.length()];
		for(int num = 0; num < code.length(); num++){
			signs[num] = code.charAt(num);
		}
		
		//根据对应的操作符采取对应的操作
		for(int temp = 0; temp < signs.length; temp++){
			
			switch( signs[temp] ){
			
			case '>': 
				pointer++; break;
				
			case '<': 
				pointer--; break;
				
			case '+':
				registers[pointer]++; break;
				
			case '-':
				registers[pointer]--; break;
			
			case ',':
				try{
					registers[pointer] = param.charAt(0); 
					param = param.substring(1);
					break;
				} catch(Exception e) {
					registers[pointer] = '\n'; break;
				}
					
			case '.':
				result = result + String.valueOf(registers[pointer]);
				
			case '[': 
				if(registers[pointer] == 0){
				
					for(int i=0; i<loop.size(); i++){
						if(temp == loop.get(i).x){	
							temp = loop.get(i).y;
							break; 
						}
					}
				
				break;
				
			} else { break; } 
		
			case ']': 
				if(registers[pointer] != 0){
				
					for(int i=0; i<loop.size(); i++){
						if(temp == loop.get(i).y){	
							temp = loop.get(i).x;
							break; 
						}
					}
				
				break;
				
			} else { break; }
				
			case ' ': break;
				
			default : 
				return "Your input sentence contains undefined words,please input in syntax again.";
				
			}
		
		}
		
		return result;
	}

}

