import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GuiCheckTask extends JFrame {

	CapyTecDB dbClass = new CapyTecDB();
	
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiCheckTask frame = new GuiCheckTask();
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
	public GuiCheckTask() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCheckTaskTitle = new JLabel("Check Task");
		lblCheckTaskTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblCheckTaskTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCheckTaskTitle.setBounds(86, 11, 260, 42);
		contentPane.add(lblCheckTaskTitle);
		
		JLabel lblSelectedTask = new JLabel("Task ID");
		lblSelectedTask.setBounds(96, 64, 46, 14);
		contentPane.add(lblSelectedTask);
		
		int userLoggedIn = 7;
		
		JComboBox dropdownTaskID = new JComboBox();
		
		dropdownTaskID.setModel(new DefaultComboBoxModel());
		dropdownTaskID.addItem("Select a Task ID");
		for (int i = 0 ; i < dbClass.getAllTasks().size() ; i++) {
			CaretakerTask currentTask = dbClass.getAllTasks().get(i);
			if (currentTask.isNeedsPeerChecking() && !currentTask.getTeamMembers().contains(userLoggedIn))
			{
				dropdownTaskID.addItem(currentTask.getID());
			}
		}
		dropdownTaskID.setBounds(208, 60, 138, 22);
		contentPane.add(dropdownTaskID);
		
		JButton btnCheckTask = new JButton("Set Task as Checked");
		btnCheckTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Button pressed. Task " + (dropdownTaskID.getSelectedItem()) + ".");
				for (int i = 0 ; i < dbClass.getAllTasks().size() ; i++)
				{
					CaretakerTask currentTask = dbClass.getAllTasks().get(i);
					if (currentTask.getID() == (int) dropdownTaskID.getSelectedItem())
					{
						CaretakerTask taskToUpdate = currentTask;
						taskToUpdate.setPeerCheckerID(userLoggedIn);
						String checkerName = "";
						for (int i2 = 0 ; i2 < dbClass.getAllCaretakers().size() ; i2++) {
							Caretaker currentCaretaker = dbClass.getAllCaretakers().get(i2);
							if (currentCaretaker.getID() == userLoggedIn)
								checkerName = currentCaretaker.getFullName();
						}
						for (int i2 = 0 ; i2 < dbClass.getAllManagers().size() ; i2++) {
							Manager currentManager = dbClass.getAllManagers().get(i2);
							if (currentManager.getID() == userLoggedIn)
								checkerName = currentManager.getFullName();
						}
						taskToUpdate.setPeerChecker(checkerName);
						if (checkerName.isEmpty())
						{
							System.out.println("Current user logged in is invalid.");
						}
						else
						{
							System.out.println("New checker is " + checkerName);
						}
					}
				}
			}
		});
		btnCheckTask.setEnabled(false);
		btnCheckTask.setBounds(96, 170, 250, 42);
		contentPane.add(btnCheckTask);
		
		dropdownTaskID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dropdownTaskID.getSelectedIndex() != 0)
					btnCheckTask.setEnabled(true);
				else
					btnCheckTask.setEnabled(false);
			}
		});
		
	}
}