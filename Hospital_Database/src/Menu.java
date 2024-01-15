import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class Menu {

	public JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu window = new Menu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Menu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 809, 659);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnDoctor = new JButton("Doctor");
		btnDoctor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Doctor f = new Doctor();
				f.frame.setVisible(true);
				frame.dispose();

			}
		});
		btnDoctor.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnDoctor.setBounds(76, 150, 167, 72);
		frame.getContentPane().add(btnDoctor);

		JLabel lblHeader = new JLabel("Hospital Database");
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 33));
		lblHeader.setBounds(89, 40, 588, 85);
		frame.getContentPane().add(lblHeader);

		JButton btnNurse = new JButton("Nurse");
		btnNurse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Nurse f = new Nurse();
				f.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnNurse.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNurse.setBounds(315, 150, 167, 72);
		frame.getContentPane().add(btnNurse);

		JButton btnPatient = new JButton("Patient");
		btnPatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Patient f = new Patient();
				f.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnPatient.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnPatient.setBounds(578, 150, 167, 72);
		frame.getContentPane().add(btnPatient);

		JButton btnAppointment = new JButton("Appointment");
		btnAppointment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Appointment f = new Appointment();
				f.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnAppointment.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnAppointment.setBounds(76, 256, 167, 72);
		frame.getContentPane().add(btnAppointment);

		JButton btnTestReport = new JButton("Test Report");
		btnTestReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Report f = new Report();
				f.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnTestReport.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnTestReport.setBounds(315, 256, 167, 72);
		frame.getContentPane().add(btnTestReport);

		JButton btnTestType = new JButton("Test Type");
		btnTestType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TestTypes f = new TestTypes();
				f.frame.setVisible(true);
				frame.dispose();
			}

		});
		btnTestType.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnTestType.setBounds(578, 256, 167, 72);
		frame.getContentPane().add(btnTestType);
	}
}
