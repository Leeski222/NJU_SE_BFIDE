package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import rmi.RemoteHelper;
import runner.ClientRunner;

public class FileListFrame {
	
	private JFrame frame;
	private JButton OKButton;
	private JButton deleteButton;
	private JButton cancelButton;
	private JList<String> fileList;
	private JLabel label;
	private JPanel jpanel;
	private JPanel topJPanel;
	private JPanel underJPanel;
	private JScrollPane jscrollPanel;
	private String[] list;
	public static  DefaultListModel<String> fileListModel = new DefaultListModel<String>();
	
	public FileListFrame(){
		
		frame = new JFrame();
		OKButton = new JButton("OK");
		deleteButton = new JButton("Delete");
		cancelButton = new JButton("Cancel");
		fileList = new JList<String>();
		label = new JLabel();
		jpanel = new JPanel();
		topJPanel = new JPanel();
		underJPanel = new JPanel();
		jscrollPanel = new JScrollPane(fileList);
		ListListener listListener= new ListListener();
		
		frame.setSize(MainFrame.WIDTH/3, MainFrame.HEIGHT);
		frame.setLocation(MainFrame.LocationX - (MainFrame.WIDTH/3)-2, MainFrame.LocationY);
		
		try {
			String listStr = RemoteHelper.getInstance().getIOService().readFileList(ClientRunner.getUserId());
			if(listStr != null){
				list = listStr.split(",");
			} else {
				list = new String[1];
				list[0] = " ";
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
				
		for(String str:list){
			if(!fileListModel.contains(str)){
				fileListModel.addElement(str);
			}
		}
		fileList.setModel(fileListModel);
		fileList.setFont(new Font("BOLD",Font.BOLD,18));
		fileList.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		fileList.addListSelectionListener(listListener);
		
		label.setFont(new Font("BOLD",Font.BOLD,16));
		label.setText("Please choose the file you want.");
		topJPanel.add(label);
		
		OKButton.setBackground(Color.LIGHT_GRAY);
		OKButton.addActionListener(listListener);
		deleteButton.setBackground(Color.LIGHT_GRAY);
		deleteButton.addActionListener(listListener);
		cancelButton.setBackground(Color.LIGHT_GRAY);
		cancelButton.addActionListener(listListener);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.setHgap(8);
		underJPanel.setLayout(gridLayout);
		underJPanel.add(OKButton);
		underJPanel.add(deleteButton);
		underJPanel.add(cancelButton);
		
		jpanel.setLayout(new BorderLayout());
		jpanel.add(topJPanel, BorderLayout.NORTH);
		jpanel.add(jscrollPanel,BorderLayout.CENTER);
		jpanel.add(underJPanel,BorderLayout.SOUTH);
		
		frame.add(jpanel);
		frame.setVisible(true);
	}
	
	public class ListListener implements ActionListener,ListSelectionListener {
		
		String fileName;
		
		@SuppressWarnings("unchecked")
		@Override
		public void valueChanged(ListSelectionEvent e) {
			fileName = ((JList<String>)e.getSource()).getSelectedValue();
		}	
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if(command.equals("OK")){
				//导入选定文件和历史版本列表
				ClientRunner.setFileName(fileName);
				try {
					String[] versionList = RemoteHelper.getInstance().getIOService().readVersionList(ClientRunner.getUserId(),ClientRunner.getFileName());
					frame.dispose();
					new OpenFrame(versionList);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			} else if(command.equals("Delete")){
				try {
					RemoteHelper.getInstance().getIOService().deleteFile(ClientRunner.getUserId(), fileName);
					fileListModel.removeElement(fileName);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			} else if(command.equals("Cancel")){
				frame.dispose();
			}
		}
	}
	
}
