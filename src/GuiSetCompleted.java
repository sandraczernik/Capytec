import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

public class GuiSetCompleted extends JFrame {

	private JPanel contentPane;
	CapyTecDB dbClass = new CapyTecDB();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiSetCompleted frame = new GuiSetCompleted();
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
	public GuiSetCompleted() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCompleteTaskTitle = new JLabel("Set Task as Complete");
		lblCompleteTaskTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblCompleteTaskTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCompleteTaskTitle.setBounds(86, 11, 260, 42);
		contentPane.add(lblCompleteTaskTitle);;
		
		JLabel lblSelectedTask = new JLabel("Task ID");
		lblSelectedTask.setBounds(114, 64, 46, 14);
		contentPane.add(lblSelectedTask);
		
		int userLoggedIn = 5;
		
		JComboBox dropdownTaskID = new JComboBox();
		dropdownTaskID.setModel(new DefaultComboBoxModel());
		dropdownTaskID.addItem("Select a Task ID");
		for (int i = 0 ; i < dbClass.getAllTasks().size() ; i++) {
			CaretakerTask currentTask = dbClass.getAllTasks().get(i);
			if (currentTask.getTeamMembers().contains(userLoggedIn) && (currentTask.getDaysUntilRepeat() != 0 || currentTask.getDateCompleted() == null))
			{
				dropdownTaskID.addItem(currentTask.getID());
			}
		}
		dropdownTaskID.setBounds(208, 60, 138, 22);
		contentPane.add(dropdownTaskID);
		
		JButton btnCompleteTask = new JButton("Select a Task");
		btnCompleteTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Button pressed. Task " + (dropdownTaskID.getSelectedItem()) + ". No functionality yet.");
			}
		});
		btnCompleteTask.setEnabled(false);
		btnCompleteTask.setBounds(96, 170, 250, 42);
		contentPane.add(btnCompleteTask);
		
		
		dropdownTaskID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dropdownTaskID.getSelectedIndex() != 0)
				{
					CaretakerTask currentTask = dbClass.getAllTasks().get((int)dropdownTaskID.getSelectedItem()-1);
					if (currentTask.isNeedsPeerChecking() && currentTask.getPeerChecker() == null)
					{
						btnCompleteTask.setEnabled(false);
						btnCompleteTask.setText("This task has not been checked");
					}
					else if (currentTask.isNeedsSigning() && currentTask.getSignee() == null)
					{
						btnCompleteTask.setEnabled(false);
						btnCompleteTask.setText("This task has not been signed off");
					}
					else if (currentTask.getTeamMembers().contains(userLoggedIn))
					{
						btnCompleteTask.setEnabled(true);
						btnCompleteTask.setText("Set Task " + currentTask.getID() + " as Complete");
					}
					else
					{
						btnCompleteTask.setEnabled(false);
						btnCompleteTask.setText("This is not your task.");
					}
				}
				else
				{
					btnCompleteTask.setEnabled(false);
					btnCompleteTask.setText("Please Select a Task");
				}
			}
		});
		
		
		
	}

}