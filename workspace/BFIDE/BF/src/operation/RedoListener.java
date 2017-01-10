package operation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ui.MainFrame;

public class RedoListener implements KeyListener{

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if( e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Y ) {
			System.out.println("redo");
			//调用撤销功能时，暂时关闭代码变化记录功能,调用完后再开启
			Record.setSave(false);
			Record.addPointer();
			MainFrame.codeArea.setText(Record.getCode());
			Record.setSave(true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
