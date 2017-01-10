package operation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ui.MainFrame;

public class UndoListener implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if ( e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
			System.out.println("undo");
			//调用撤销功能时，暂时关闭代码变化记录功能,调用完后再开启
			Record.setSave(false);
			Record.subPointer();
			MainFrame.codeArea.setText(Record.getCode());
			Record.setSave(true);
		} 
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}


