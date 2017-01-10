package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import rmi.RemoteHelper;
import runner.ClientRunner;

public class LoginFrame {
	
	private JFrame frame;
	private JButton joinButton;
	private JButton loginButton;
	private JLabel explainLabel;
	private JLabel userNameLabel;
	private JLabel passwordLabel;
	private JTextField userNameArea;
	private JPasswordField passwordArea;
	private JPanel explainPanel;
	private JPanel userNamePanel;
	private JPanel passwordPanel;
	private JPanel buttonPanel;
	
	public LoginFrame(){
		
		frame = new JFrame("Login");
		frame.setSize(MainFrame.WIDTH/2, MainFrame.HEIGHT/3);
		frame.setLocation(MainFrame.LocationX+200, MainFrame.LocationY+150);
			
		joinButton = new JButton();
		loginButton = new JButton();
		explainLabel = new JLabel();
		userNameLabel = new JLabel();
		passwordLabel = new JLabel();
		explainPanel = new JPanel();
		userNamePanel = new JPanel();
		passwordPanel = new JPanel();
		buttonPanel = new JPanel();
		userNameArea = new JTextField(15);
		passwordArea = new JPasswordField(15);	
		
		joinButton.setText("JOIN");
		joinButton.setBackground(Color.LIGHT_GRAY);
		joinButton.addActionListener(new LoginListener());
		loginButton.setText("LOGIN");
		loginButton.setBackground(Color.LIGHT_GRAY);
		loginButton.addActionListener(new LoginListener());
		
		buttonPanel.add(joinButton);
		buttonPanel.add(loginButton);
		
		userNameLabel.setText("UserName: ");
		userNameLabel.setFont(new Font("ITALIC",Font.ITALIC,15));
		userNamePanel.add(userNameLabel);
		userNamePanel.add(userNameArea);
		
		passwordLabel.setText("Password:   ");
		passwordLabel.setFont(new Font("ITALIC",Font.ITALIC,15));
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordArea);

		explainLabel.setText("Please input your UserName and Password.");
		explainLabel.setFont(new Font("BOLD",Font.BOLD,15));
		explainPanel.add(explainLabel);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		frame.setLayout(gridBagLayout);
		
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(explainPanel, gridBagConstraints);
		gridBagLayout.setConstraints(userNamePanel, gridBagConstraints);
		gridBagLayout.setConstraints(passwordPanel, gridBagConstraints);
		gridBagLayout.setConstraints(buttonPanel, gridBagConstraints);
		frame.add(explainPanel);
		frame.add(userNamePanel);
		frame.add(passwordPanel);
		frame.add(buttonPanel);
		frame.setVisible(true);
	}
	
	public class LoginListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();
			
			if(command.equals("JOIN")){
				String userName = userNameArea.getText();
				String password = new String(passwordArea.getPassword());
				try {
					boolean result = RemoteHelper.getInstance().getUserService().join(userName, password);
					if(result){
						explainLabel.setText("Create your ID successfully! You can login now.");
					} else {
						explainLabel.setText("This ID has been used,please select another one!");
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				
			} else if(command.equals("LOGIN")) {
				//登陆实现
				String userName = userNameArea.getText();
				String password = new String(passwordArea.getPassword());
				try {
					boolean result = RemoteHelper.getInstance().getUserService().login(userName, password);
					if(result){
						explainLabel.setText("Login successfully!");
						ClientRunner.setUserId(userName);
						ClientRunner.setLogin(true);
						new OpenFrame();
					} else {
						explainLabel.setText("The ID is not exist or password is wrong.");
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			} 
		}
	}
	
}

