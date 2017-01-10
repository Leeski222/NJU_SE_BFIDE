package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import operation.Record;
import operation.RedoListener;
import operation.UndoListener;
import rmi.RemoteHelper;
import runner.ClientRunner;
import syntaxHighlighter.SyntaxHighlighter;

//后面进行防御式编程，所有跟网络相关的服务代码全部用try catch捕捉异常并且在对应位置显示未连接上服务器

public class MainFrame {
	
	private JFrame frame;
	private JScrollPane jscrollPane1;
	private JScrollPane jscrollPane2;
	private JScrollPane jscrollPane3;
	private JPanel topJpanel;
	private JPanel underJpanel;
	private JTextArea parameterArea;
	private JTextArea resultArea;
	public static JTextPane codeArea;
	public final static int WIDTH = 1000;
	public final static int HEIGHT = 800;
	public final static int LocationX = 450;
	public final static int LocationY = 200;

	public MainFrame() {
		
		//创建主窗体(窗体的显示位置后期可以通过dimension工具类进行优化)
		frame = new JFrame("BF Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocation(LocationX, LocationY);
		
		//设置主窗体的布局
		GridBagLayout gridBagLayout1 = new GridBagLayout();
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		frame.setLayout(gridBagLayout1);

		//设置按键区
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenu versionMenu = new JMenu("Version");
		JMenu runMenu = new JMenu("Run");
		JMenu accountMenu = new JMenu("Account");
		
		JMenuItem newMenuItem = new JMenuItem("New");
		JMenuItem saveMenuItem = new JMenuItem("Save");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		JMenuItem executeMenuItem = new JMenuItem("Execute");
		JMenuItem loginMenuItem = new JMenuItem("Login");
		JMenuItem logoutMenuItem = new JMenuItem("Logout");
		
		fileMenu.add(newMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(exitMenuItem);

		runMenu.add(executeMenuItem);
		
		accountMenu.add(loginMenuItem);
		accountMenu.add(logoutMenuItem);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		menuBar.setLayout(gridBagLayout);
		
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1;
		gridBagLayout.setConstraints(fileMenu, gridBagConstraints);
		gridBagLayout.setConstraints(runMenu, gridBagConstraints);
		gridBagLayout.setConstraints(fileMenu, gridBagConstraints);
		
		menuBar.add(fileMenu);
		menuBar.add(runMenu);		
		menuBar.add(versionMenu);
		
		JMenu emptyMenu = new JMenu();
		emptyMenu.setEnabled(false);
		gridBagConstraints.weightx = 100;
		gridBagLayout.setConstraints(emptyMenu, gridBagConstraints);
		
		menuBar.add(emptyMenu);
		menuBar.add(accountMenu);		
		
		frame.setJMenuBar(menuBar);

		//为按键添加监听
		newMenuItem.addActionListener(new MenuItemActionListener());
		loginMenuItem.addActionListener(new MenuItemActionListener());
		logoutMenuItem.addActionListener(new MenuItemActionListener());
		executeMenuItem.addActionListener(new MenuItemActionListener());
		saveMenuItem.addActionListener(new SaveActionListener());
		versionMenu.addMouseListener(new ClickActionListener());
		
		//代码区
		codeArea = new JTextPane();
		codeArea.setMargin(new Insets(10, 10, 10, 10));
		codeArea.setFont(new Font("BLOD",Font.PLAIN,18));
		codeArea.setBackground(Color.WHITE);
		codeArea.addKeyListener(new UndoListener());
		codeArea.addKeyListener(new RedoListener());
		codeArea.getStyledDocument().addDocumentListener(new Record(codeArea));
		codeArea.getStyledDocument().addDocumentListener(new SyntaxHighlighter(codeArea));
		
		jscrollPane1 = new JScrollPane(codeArea); 
		
		//上部面板
		topJpanel = new JPanel();
		topJpanel.setLayout(new BorderLayout());
		topJpanel.add(jscrollPane1,BorderLayout.CENTER);
		
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.weightx = 1;
		gridBagConstraints1.weighty = 2;
		gridBagConstraints1.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout1.setConstraints(topJpanel, gridBagConstraints1);
		frame.add(topJpanel);

		//下部面板
		GridBagLayout gridBagLayout2 = new GridBagLayout();
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		underJpanel = new JPanel();
		underJpanel.setLayout(gridBagLayout2);
		
		//数据区
		parameterArea = new JTextArea();
		parameterArea.setMargin(new Insets(15,10,15,10));
		parameterArea.setFont(new Font("BLOD",Font.PLAIN,16));
		parameterArea.setLineWrap(true);
		parameterArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK,1),"Parameter")));
		parameterArea.setBackground(new Color(211,211,211));
		jscrollPane2 = new JScrollPane(parameterArea); 
		
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.weightx = 11;
		gridBagConstraints2.weighty = 1;
		gridBagLayout2.setConstraints(jscrollPane2, gridBagConstraints2);
		underJpanel.add(jscrollPane2);
		
		//结果区
		resultArea = new JTextArea();
		resultArea.setBackground(new Color(245,245,245));
		resultArea.setFont(new Font("BLOD",Font.PLAIN,16));
		resultArea.setLineWrap(true);
		resultArea.setMargin(new Insets(10,10,10,10));
		resultArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK,1), "Result"));
		resultArea.setEditable(false);
		jscrollPane3 = new JScrollPane(resultArea); 
		
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.weightx = 12;
		gridBagConstraints2.weighty = 1;
		gridBagConstraints2.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout2.setConstraints(jscrollPane3, gridBagConstraints2);
		underJpanel.add(jscrollPane3);
		
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.weightx = 1;
		gridBagConstraints1.weighty = 1;
		gridBagConstraints1.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout1.setConstraints(underJpanel, gridBagConstraints1);
		frame.add(underJpanel);

		frame.setVisible(true);
	}
	
	class MenuItemActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			
			if (command.equals("New")){
				//建立新文件，将界面清空
				codeArea.setText("");
				parameterArea.setText("");
				String code = codeArea.getText();
				new NewFrame(code);
			}  else if (command.equals("Execute")) {
				//传给解析方法
				try {
					Record.saveCode();
					String result = RemoteHelper.getInstance().getExecuteService().execute(codeArea.getText(), parameterArea.getText());
					resultArea.setText(result);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}		
			} else if (command.equals("Login")) {	
				if (!ClientRunner.isLogin()) {
					new LoginFrame();
				} else {
					new LogoutFrame();
				}
			} else if (command.equals("Logout")) {
				ClientRunner.setUserId("defaultUser");
				ClientRunner.setFileName("defaultFile");
				ClientRunner.setLogin(false);
			}
		}
	}

	class SaveActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String code = codeArea.getText();
			
			LocalDate localDate = LocalDate.now();
			LocalTime localTime = LocalTime.now();
			
			String saveTime = localDate.toString() + "_" 
							+ localTime.toString().substring(0, 2)
							+ localTime.toString().substring(3, 5)
							+ localTime.toString().substring(6, 8);
			try {
				//调用远程方法保存版本，并在本地版本列表同步数据
				RemoteHelper.getInstance().getIOService().writeFile(code, ClientRunner.getUserId(), ClientRunner.getFileName());
				OpenFrame.versionListModel.addElement(ClientRunner.getFileName() + "_" + saveTime);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	class ClickActionListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 1){
				String[] versionList;
				try {
					versionList = RemoteHelper.getInstance().getIOService().readVersionList(ClientRunner.getUserId(),ClientRunner.getFileName());
					new OpenFrame(versionList);
				} catch (RemoteException e1) {
					e1.printStackTrace();
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
	
}
