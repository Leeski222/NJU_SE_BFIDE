package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rmi.RemoteHelper;
import runner.ClientRunner;

public class NewFrame {
	
	private JFrame frame ;
	private JButton OKButton;
	private JButton cancelButton;
	private JLabel explainLabel;
	private JLabel fileNameLabel;
	private JTextField fileNameArea;
	private JPanel explainPanel;
	private JPanel fileNamePanel;
	private JPanel buttonPanel;
	private String code;
	
	public NewFrame(String code){
		
		this.code =code;
		
		frame = new JFrame("New");
		OKButton = new JButton();
		cancelButton = new JButton();
		explainLabel = new JLabel();
		fileNameLabel = new JLabel();
		explainPanel = new JPanel();
		fileNamePanel = new JPanel();
		buttonPanel = new JPanel();
		fileNameArea = new JTextField(15);

		frame.setSize(1*MainFrame.WIDTH/2, MainFrame.HEIGHT/4);
		frame.setLocation(MainFrame.LocationX +250, MainFrame.LocationY +200);
		
		OKButton.setText("OK");
		OKButton.addActionListener(new ButtonActionListener());
		OKButton.setBackground(Color.LIGHT_GRAY);
		cancelButton.setText("Cancel");
		cancelButton.setBackground(Color.LIGHT_GRAY);
		cancelButton.addActionListener(new ButtonActionListener());
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.setHgap(100);
		buttonPanel.setLayout(gridLayout);
		buttonPanel.add(OKButton);
		buttonPanel.add(cancelButton);
		
		fileNameLabel.setText("FileName: ");
		fileNameLabel.setFont(new Font("ITALIC",Font.ITALIC,15));
		fileNamePanel.add(fileNameLabel);
		fileNamePanel.add(fileNameArea);

		explainLabel.setText("Please input fileName");
		explainLabel.setFont(new Font("BOLD",Font.BOLD,15));
		explainPanel.add(explainLabel);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		frame.setLayout(gridBagLayout);
		
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(explainPanel, gridBagConstraints);
		gridBagLayout.setConstraints(fileNamePanel, gridBagConstraints);
		gridBagLayout.setConstraints(buttonPanel, gridBagConstraints);
		frame.add(explainPanel);
		frame.add(fileNamePanel);
		frame.add(buttonPanel);

		frame.setVisible(true);
	}
	
	public class ButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if(command.equals("OK")) {
				try {
					String tempName = ClientRunner.getFileName();
					ClientRunner.setFileName(fileNameArea.getText());
					boolean result = RemoteHelper.getInstance().getIOService().createFile(code, ClientRunner.getUserId(), ClientRunner.getFileName());	
					if(result) {
						FileListFrame.fileListModel.addElement(ClientRunner.getFileName());
						explainLabel.setText("Create successfully!");
					} else {
						ClientRunner.setFileName(tempName);
						explainLabel.setText("The fileName have been exsist or empty or contains '_'.");
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			} else if(command.equals("Cancel")) {
				frame.dispose();
			}
		}
		
	}
}
