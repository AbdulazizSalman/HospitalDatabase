import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.SystemColor;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JComboBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.toedter.calendar.JDateChooser;

public class Report {

	private JComboBox comboBox_PatientId, comboBox_TestId;
	public JFrame frame;
	private JTextField textField_ReportNumber;
	private JTable table;
	DefaultTableModel model;
	private JTextField textField_Description;
	private JTextField textField_Result;
	private JDateChooser dateChooser;
	Connection con;
	Statement st;

	// Launch the application.

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Report window = new Report();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Create the application.

	public Report() {
		initialize();
	}

	// Initialize the contents of the frame.

	private void initialize() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "99001122Azoz");
			st = con.createStatement();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		frame = new JFrame();
		frame.setBounds(100, 100, 813, 414);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaption);
		panel.setBounds(0, 0, 801, 379);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblPatientId = new JLabel("Patient Id");
		lblPatientId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPatientId.setBounds(54, 63, 66, 23);
		panel.add(lblPatientId);

		JLabel lblNurseId = new JLabel("Report Number");
		lblNurseId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNurseId.setBounds(24, 85, 96, 23);
		panel.add(lblNurseId);

		textField_ReportNumber = new JTextField();
		textField_ReportNumber.setColumns(10);
		textField_ReportNumber.setBounds(128, 87, 146, 20);
		panel.add(textField_ReportNumber);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { // fill empty boxes
				DefaultTableModel df = (DefaultTableModel) table.getModel();

				int selectedIndex = table.getSelectedRow();

				String pid = df.getValueAt(selectedIndex, 0).toString();

				for (int i = 0; i < comboBox_PatientId.getItemCount(); i++)
					if (comboBox_PatientId.getItemAt(i).toString().equalsIgnoreCase(pid))
						comboBox_PatientId.setSelectedIndex(i);

				textField_ReportNumber.setText(df.getValueAt(selectedIndex, 1).toString());
				textField_Description.setText(df.getValueAt(selectedIndex, 4).toString());
				textField_Result.setText(df.getValueAt(selectedIndex, 5).toString());

				String testId = df.getValueAt(selectedIndex, 2).toString();

				for (int i = 0; i < comboBox_TestId.getItemCount(); i++)
					if (comboBox_TestId.getItemAt(i).toString().equalsIgnoreCase(testId))
						comboBox_TestId.setSelectedIndex(i);
			}
		});
		scrollPane.setBounds(316, 60, 475, 308);
		panel.add(scrollPane);
		table = new JTable();
		table.setBackground(SystemColor.scrollbar);
		scrollPane.setViewportView(table);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					if (textField_ReportNumber.getText().equals("")
							|| comboBox_PatientId.getSelectedItem().toString().equals("-")
							|| comboBox_TestId.getSelectedItem().toString().equals("-")) {
						// checks if boxes are empty
						JOptionPane.showMessageDialog(null, "please enter all required data");
						return;
					}

					int selectedIndex = table.getSelectedRow();

					String sql = "insert into testreport(patient_pid, ReportNumber, TestId, TestDate, Description, Result) values ('"
							+ comboBox_PatientId.getSelectedItem().toString() + "' , '"
							+ textField_ReportNumber.getText() + "' , '" + comboBox_TestId.getSelectedItem().toString()
							+ "' , '" + ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText() + "' ,"
							+ " '" + textField_Description.getText() + "' , '" + textField_Result.getText() + "')";

					st.execute(sql);
					JOptionPane.showMessageDialog(null, "added");

					// make boxes empty after adding
					textField_ReportNumber.setText(null);
					textField_Description.setText(null);
					textField_Result.setText(null);
					dateChooser.setDate(null);

					displayTable(); // Display Table

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1);
				}
			}
		});
		btnAdd.setBounds(54, 258, 89, 23);
		panel.add(btnAdd);

		JButton btnDisplay = new JButton("Display");
		btnDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayTable();
			}
		});
		btnDisplay.setBounds(117, 225, 89, 23);
		panel.add(btnDisplay);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRowCount() == 1) { // one row selected.
					int row = table.getSelectedRow();
					String cell = table.getModel().getValueAt(row, 0).toString();
					String cell1 = table.getModel().getValueAt(row, 1).toString();
					String query = "DELETE FROM mydb.testreport where Patient_PId= " + cell + " AND ReportNumber = "
							+ cell1;// to get selected row.

					try {
						PreparedStatement ps = con.prepareStatement(query);
						ps.execute();
						JOptionPane.showMessageDialog(null, "Deleted Succesfully");
					} catch (Exception q) {// if it is not deleted
						JOptionPane.showMessageDialog(null, q);
					}
				} else if (table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "Please select one row to be deleted");
				} else
					JOptionPane.showMessageDialog(null, "Please select at most one row to be deleted");

				displayTable();
			}
		});
		btnDelete.setBounds(172, 258, 89, 23);
		panel.add(btnDelete);

		JLabel lblHeader = new JLabel("Report");
		lblHeader.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setBounds(238, 11, 287, 38);
		panel.add(lblHeader);

		JLabel lblDate = new JLabel("Date");
		lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDate.setBounds(60, 135, 60, 23);
		panel.add(lblDate);

		textField_Description = new JTextField();
		textField_Description.setColumns(10);
		textField_Description.setBounds(128, 159, 146, 20);
		panel.add(textField_Description);

		JLabel lblDescription = new JLabel("Description");
		lblDescription.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDescription.setBounds(46, 157, 74, 23);
		panel.add(lblDescription);

		JLabel lblResult = new JLabel("Result");
		lblResult.setHorizontalAlignment(SwingConstants.RIGHT);
		lblResult.setBounds(60, 179, 60, 23);
		panel.add(lblResult);

		textField_Result = new JTextField();
		textField_Result.setColumns(10);
		textField_Result.setBounds(128, 181, 146, 20);
		panel.add(textField_Result);

		JLabel lblTestId = new JLabel("Test Id");
		lblTestId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTestId.setBounds(60, 111, 60, 23);
		panel.add(lblTestId);

		comboBox_PatientId = new JComboBox();
		comboBox_PatientId.setBounds(128, 63, 146, 22);
		panel.add(comboBox_PatientId);
		comboBox_PatientId.addItem("-");

		comboBox_TestId = new JComboBox();
		comboBox_TestId.setBounds(128, 111, 146, 22);
		panel.add(comboBox_TestId);
		comboBox_TestId.addItem("-");

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					st.close();
					con.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				Menu f = new Menu();
				f.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnBack.setBounds(117, 347, 85, 21);
		panel.add(btnBack);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(128, 139, 146, 19);
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.getJCalendar().setMaxSelectableDate(new Date());
		panel.add(dateChooser);

		displayJbox_Pid();
		displayJbox_Tid();
	}

	public void displayJbox_Pid() {
		String sql = "select * from patient";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet res = stmt.executeQuery();

			while (res.next()) {
				comboBox_PatientId.addItem(res.getString("pid"));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public void displayJbox_Tid() {
		String sql = "select * from teststype";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet res = stmt.executeQuery();

			while (res.next()) {
				comboBox_TestId.addItem(res.getString("testid"));

			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public void displayTable() {
		if (table.getRowCount() >= 1)
			model.setRowCount(0);
		try {

			String query = "Select * From mydb.testreport";

			ResultSet rs = st.executeQuery(query);

			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();

			model = (DefaultTableModel) table.getModel();

			int col = rsmd.getColumnCount();

			String[] colName = new String[col];

			for (int i = 0; i < col; i++) {
				colName[i] = rsmd.getColumnName(i + 1);
			}

			model.setColumnIdentifiers(colName);

			String TestDate, Description, Result;
			int PId, ReportNumber, TestId;
			while (rs.next()) {
				PId = rs.getInt(1);
				ReportNumber = rs.getInt(2);
				TestId = rs.getInt(3);
				TestDate = rs.getString(4);
				Description = rs.getString(5);
				Result = rs.getString(6);
				Object[] row = { PId, ReportNumber, TestId, TestDate, Description, Result };
				model.addRow(row);
			}

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
	}
}