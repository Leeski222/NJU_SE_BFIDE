package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import rmi.RemoteHelper;
import runner.ClientRunner;

public class OpenFrame {
	
	private JFrame frame;
	private JPanel buttonPanel;
	private JButton OKButton;
	private JButton deleteButton;
	private JList<String> versionList;
	public static DefaultListModel<String> versionListModel = new DefaultListModel<String>();

	
	public OpenFrame() {
		
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setSize(MainFrame.WIDTH/3, MainFrame.HEIGHT);
		frame.setLocation(MainFrame.LocationX - (MainFrame.WIDTH/3)-2, MainFrame.LocationY);

		OKButton = new JButton("OK");
		OKButton.setBackground(Color.LIGHT_GRAY);
		deleteButton = new JButton("Delete");
		deleteButton.setBackground(Color.LIGHT_GRAY);
		
		String text = "Open File";
		JMenu Open = new JMenu();
		for(int temp =0; temp < this.frame.getWidth(); temp = temp + 8){
			text = " "+ text;
		}
		Open.setText(text);
		Open.addMouseListener(new ClickListener());
		JMenuBar menuBar = new JMenuBar();
		menuBar.setLayout(new GridLayout());
		menuBar.add(Open);
		frame.add(menuBar,BorderLayout.NORTH);
		versionList = new JList<String>();
		versionList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK,1), "Version"));

		frame.add(versionList,BorderLayout.CENTER);
		
		buttonPanel = new JPanel();
		GridLayout gridLayout = new GridLayout();
		gridLayout.setHgap(100);
		buttonPanel.setLayout(gridLayout);
		buttonPanel.add(OKButton);
		buttonPanel.add(deleteButton);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		
		frame.setVisible(true);
	}
	
	public OpenFrame(String[] list) {
		
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setSize(MainFrame.WIDTH/3, MainFrame.HEIGHT);
		frame.setLocation(MainFrame.LocationX - (MainFrame.WIDTH/3)-2, MainFrame.LocationY);
		ListListener listListener = new ListListener();
		
		OKButton = new JButton("OK");
		OKButton.setBackground(Color.LIGHT_GRAY);
		OKButton.addActionListener(listListener);
		deleteButton = new JButton("Delete");
		deleteButton.setBackground(Color.LIGHT_GRAY);
		deleteButton.addActionListener(listListener);
		
		String text = "Open File";
		JMenu Open = new JMenu();
		for(int temp =0; temp < this.frame.getWidth(); temp = temp + 8){
			text = " "+ text;
		}
		Open.setText(text);
		Open.addMouseListener(new ClickListener());
		JMenuBar menuBar = new JMenuBar();
		menuBar.setLayout(new GridLayout());
		menuBar.add(Open);
		frame.add(menuBar,BorderLayout.NORTH);
		
		versionList = new JList<String>();
		versionList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK,1), "Version"));
		
		if(list != null){
			for(String str:list){
				str = str.split("_")[0] + "_" + str.split("_")[1] + "_" + str.split("_")[2].substring(0, 6);
				if(!versionListModel.contains(str)){
					versionListModel.addElement(str);
				}
			}
		}
		
		versionList.setModel(versionListModel);
		versionList.setFont(new Font("BOLD",Font.BOLD,18));
		versionList.addListSelectionListener(listListener);
		
		frame.add(versionList,BorderLayout.CENTER);
		
		buttonPanel = new JPanel();
		GridLayout gridLayout = new GridLayout();
		gridLayout.setHgap(100);
		buttonPanel.setLayout(gridLayout);
		buttonPanel.add(OKButton);
		buttonPanel.add(deleteButton);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		
		frame.setVisible(true);
	}
	
	public class ClickListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 1){
				if(ClientRunner.isLogin()) {
					new FileListFrame();
					frame.dispose();
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		
	}
	
	public class ListListener implements ActionListener, ListSelectionListener {
		
		String versionName;
		
		@SuppressWarnings("unchecked")
		@Override
		public void valueChanged(ListSelectionEvent e) {
			versionName = ((JList<String>)e.getSource()).getSelectedValue();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			
			if(command.equals("OK")) {
				try {
					String code = RemoteHelper.getInstance().getIOService().readFile(ClientRunner.getUserId(), ClientRunner.getFileName(), versionName);
					MainFrame.codeArea.setText(code);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			} else if(command.equals("Delete")) {
				try {
					RemoteHelper.getInstance().getIOService().deleteVersion(ClientRunner.getUserId(), ClientRunner.getFileName(), versionName);
					versionListModel.removeElement(versionName);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}
}
