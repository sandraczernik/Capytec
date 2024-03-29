import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JTextArea;

public class GuiLogin extends JFrame {
	
	private boolean notLoggedIn = true;
	int loggedInId = 0;
	
	private JPanel contentPane;
	private JTextField usernameField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiLogin frame = new GuiLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GuiLogin() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 466, 352);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLoginTitle = new JLabel("CAPYTEC LOGIN");
		lblLoginTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoginTitle.setToolTipText("");
		lblLoginTitle.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblLoginTitle.setBounds(0, 0, 450, 59);
		contentPane.add(lblLoginTitle);
		
		usernameField = new JTextField();
		usernameField.setToolTipText("Please enter username here");
		usernameField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		usernameField.setBounds(170, 107, 225, 37);
		contentPane.add(usernameField);
		usernameField.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblUsername.setBounds(46, 107, 110, 37);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPassword.setBounds(46, 177, 102, 47);
		contentPane.add(lblPassword);
		
		JLabel lblUserBox = new JLabel("");
		lblUserBox.setForeground(new Color(204, 51, 0));
		lblUserBox.setBounds(170, 144, 225, 19);
		contentPane.add(lblUserBox);
		
		JLabel lblPwdBox = new JLabel("");
		lblPwdBox.setForeground(new Color(204, 51, 0));
		lblPwdBox.setBounds(170, 221, 225, 19);
		contentPane.add(lblPwdBox);
		
		JLabel lblNotify = new JLabel("");
		lblNotify.setHorizontalAlignment(SwingConstants.CENTER);
		lblNotify.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNotify.setForeground(new Color(204, 51, 0));
		lblNotify.setBounds(10, 70, 430, 14);
		contentPane.add(lblNotify);
		
		JLabel lblSignedIn = new JLabel("A User is Currently Logged In!");
		lblSignedIn.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblSignedIn.setHorizontalAlignment(SwingConstants.CENTER);
		lblSignedIn.setBounds(0, 144, 450, 37);
		contentPane.add(lblSignedIn);
		lblSignedIn.setVisible(false);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(170, 185, 225, 37);
		contentPane.add(passwordField);
		
		JButton btnLogin = new JButton("LOGIN");
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String usernameIn = usernameField.getText();
				char [] passwordIn = passwordField.getPassword();
				
				if(usernameIn.isEmpty()) {
					lblUserBox.setText("Please enter a username!");
				} else {
					lblUserBox.setText("");
				}
				if(passwordIn.length == 0) {
					lblPwdBox.setText("Please enter a password!");
				} else {
					lblPwdBox.setText("");
				}
				
				if((!usernameIn.isEmpty()) && !(passwordIn.length == 0) && loggedInId == 0) {
					lblNotify.setText("");
					try {
						
						CapyTecDB db = new CapyTecDB();
						
						String dbHash = new String(); 
						
						if(db.getHash(usernameIn) != null) {
							dbHash = db.getHash(usernameIn);
						}
						
						//SHA-256 hashing is used.
						MessageDigest digest = MessageDigest.getInstance("SHA-256");
						
						byte[] hash = digest.digest(new String(passwordIn).getBytes());
						
						
						String hashIn = Base64.getEncoder().encodeToString(hash);
						
						
						if(dbHash.equals(hashIn)){
							
							loggedInId = db.getAccId(usernameIn);
							setNotLoggedIn(false);
							
							lblSignedIn.setVisible(true);
							lblNotify.setVisible(false);
							lblUsername.setVisible(false);
							lblPassword.setVisible(false);
							lblPwdBox.setVisible(false);
							lblUserBox.setVisible(false);
							btnLogin.setVisible(false);
							
							passwordField.setVisible(false);
							usernameField.setVisible(false);
						}
						
					} catch (NoSuchAlgorithmException e1) {
						e1.printStackTrace();
					}
					
					
					
				}else{
					lblNotify.setText("Invalid Username or Password!");
				}
				

			}
		});
		
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnLogin.setBounds(166, 251, 110, 37);
		contentPane.add(btnLogin);
	}
	
	public boolean isNotLoggedIn() {
		return this.notLoggedIn;
	}
	public void setNotLoggedIn(boolean notLoggedIn) {
		this.notLoggedIn = notLoggedIn;
	}
	
	public int getLoggedInId() {
		return this.loggedInId;
	}
	
	public boolean checkUser(int userId) {
		
		CapyTecDB db = new CapyTecDB();
		
		boolean isManager = false;
		
		ArrayList<Manager> mgrs = db.getAllManagers();
		
		for(int i = 0 ; i < mgrs.size() ; i++) {
			if(userId == mgrs.get(i).getID()) {
				isManager = true;
			}
		}
		return isManager;
	}
}
