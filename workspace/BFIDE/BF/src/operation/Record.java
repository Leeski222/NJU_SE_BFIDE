package operation;

import java.util.ArrayList;

import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Record implements DocumentListener {
	//使用实针（恒指向记录表末），虚针（可以进行前后移动）
	private String lastCode;
	private static ArrayList<String> codeRecord;
	private static JTextPane textPane;
	private static boolean isSave;
	private static boolean isAdd;
	private static int vitualPointer;
	private static int physicalPointer;
	
	@SuppressWarnings("static-access")
	public Record(JTextPane textPane) {
		this.textPane = textPane; 
		codeRecord = new ArrayList<String>();
		codeRecord.add("");
		isSave = true;
		isAdd = false;
		vitualPointer = 0;
		physicalPointer = 0;
	}
	
	public static boolean isSave() {
		return isSave;
	}

	public static void setSave(boolean isSave) {
		Record.isSave = isSave;
	}
	
	public static void addPointer() {
		if(vitualPointer < codeRecord.size()- 1) {
			vitualPointer ++;
		}
	}
	
	public static void subPointer() {
		if(vitualPointer > 0){
			vitualPointer --;
		}
	}
	
	public static String getCode() {
		return codeRecord.get(vitualPointer);
	}
	
	public static void saveCode() {
		if(vitualPointer != physicalPointer) {
			for(int temp = vitualPointer + 1; temp <= physicalPointer; temp++){
				codeRecord.remove(vitualPointer + 1);
			}
			vitualPointer ++;
			physicalPointer = vitualPointer;
			codeRecord.add(physicalPointer, textPane.getText());
		}
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		if (isSave) {			
			
			//判断此时虚针和时针是否对其，如果对齐，就正常进行转换判定，如果未对齐，先将针对齐，并将虚针后的状态清空，创建新的状态线
			if(vitualPointer == physicalPointer) {
				//判断是否从删减转换为增添
				if(!isAdd){
					if(!(textPane.getText().equals(codeRecord.get(physicalPointer)))){
						vitualPointer ++;
						physicalPointer ++;
						codeRecord.add(physicalPointer, textPane.getText());
					}
					isAdd = true;
				}
			} else {
				for(int temp = vitualPointer + 1; temp <= physicalPointer; temp++){
					codeRecord.remove(vitualPointer + 1);
				}
				physicalPointer = vitualPointer;
				if(!textPane.getText().equals(codeRecord.get(physicalPointer))){
					vitualPointer ++;
					physicalPointer ++;
					codeRecord.add(physicalPointer, textPane.getText());
				}
				isAdd = true;
			}
			
			lastCode = codeRecord.get(physicalPointer);
			
			//判断增添形式，若为连续增添，则覆盖前一个的记录，指针不变；若为成段增添，则创建新的记录，指针下移
			if (textPane.getText().length() - lastCode.length() == 1) {
				codeRecord.set(physicalPointer, textPane.getText());
			} else if (textPane.getText().length() - lastCode.length() > 1) {
				vitualPointer ++;
				physicalPointer ++;
				codeRecord.add(physicalPointer, textPane.getText());
			}
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if(isSave) {
			
			//判断此时虚针和时针是否对其，如果对齐，就正常进行转换判定，如果未对齐，先将针对齐，并将虚针后的状态清空，创建新的状态线
			if(vitualPointer == physicalPointer) {
				//判断是否从增添转换为删减
				if(isAdd){
					if(!textPane.getText().equals(codeRecord.get(physicalPointer))){
						vitualPointer ++;
						physicalPointer ++;
						codeRecord.add(physicalPointer, textPane.getText());
					}
					isAdd = false;
				}
			} else {
				for(int temp = vitualPointer + 1; temp <= physicalPointer; temp++){
					codeRecord.remove(vitualPointer + 1);
				}
				physicalPointer = vitualPointer;
				if(!textPane.getText().equals(codeRecord.get(physicalPointer))){
					vitualPointer ++;
					physicalPointer ++;
					codeRecord.add(physicalPointer, textPane.getText());
				}
				isAdd = false;
			}
			
			lastCode = codeRecord.get(physicalPointer);
			
			//判断删减形式，若为连续删减，则覆盖前一个的记录，指针不变；若为成段删减，则创建新的记录，指针下移
			if (textPane.getText().length() - lastCode.length() == -1) {
				codeRecord.set(physicalPointer, textPane.getText());
			} else if (textPane.getText().length() - lastCode.length() < -1) {
				vitualPointer ++;
				physicalPointer ++;
				codeRecord.add(physicalPointer, textPane.getText());
			}
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {}
	
}
