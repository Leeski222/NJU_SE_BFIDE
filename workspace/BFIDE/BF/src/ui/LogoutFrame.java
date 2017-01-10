package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import runner.ClientRunner;

public class LogoutFrame {
	JFrame frame;
	JPanel panel;
	JPanel buttonPanel;
	JLabel label;
	JButton logoutButton;
	
	public LogoutFrame() {
		frame = new JFrame("Logout");
		frame.setSize(MainFrame.WIDTH/2, MainFrame.HEIGHT/5);
		frame.setLocation(MainFrame.LocationX+250, MainFrame.LocationY+250);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		frame.setLayout(gridBagLayout);
		
		logoutButton = new JButton("Logout");
		logoutButton.setBackground(Color.LIGHT_GRAY);
		logoutButton.addActionListener(new LogoutListener());
		
		buttonPanel = new JPanel();
		buttonPanel.add(logoutButton);
		
		label = new JLabel("You have logined,please login first!");
		label.setFont(new Font("BOLD",Font.BOLD,20));
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(label,BorderLayout.CENTER);
		
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(panel, gridBagConstraints);
		gridBagLayout.setConstraints(buttonPanel, gridBagConstraints);
		frame.add(panel);
		frame.add(buttonPanel);
		
		frame.setVisible(true);
	}
	
	class LogoutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Logout")) {
				ClientRunner.setUserId("defaultUser");
				ClientRunner.setFileName("defaultFile");
				ClientRunner.setLogin(false);
				frame.dispose();
			}
		}
		
	}
}
