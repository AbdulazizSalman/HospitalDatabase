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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import com.toedter.calendar.JDateChooser;

public class Patient {

	public JFrame frame;
	private JTextField textField_PatientId;
	private JTextField textField_Name;
	private JTable table;
	DefaultTableModel model;
	private JComboBox ComboBox_Sex;
	private JDateChooser dateChooser;
	Connection con;
	Statement st;

	// Launch the application.

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Patient window = new Patient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Create the application.

	public Patient() {
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

		JLabel lblPatientId = new JLabel("Patient Id:");
		lblPatientId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPatientId.setBounds(68, 60, 60, 23);
		panel.add(lblPatientId);

		JLabel lblName = new JLabel("Name");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(68, 86, 60, 23);
		panel.add(lblName);

		textField_PatientId = new JTextField();
		textField_PatientId.setBounds(128, 61, 146, 20);
		panel.add(textField_PatientId);
		textField_PatientId.setColumns(10);

		textField_Name = new JTextField();
		textField_Name.setColumns(10);
		textField_Name.setBounds(128, 87, 146, 20);
		panel.add(textField_Name);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				DefaultTableModel df = (DefaultTableModel) table.getModel();
				int selectedIndex = table.getSelectedRow();

				textField_PatientId.setText(df.getValueAt(selectedIndex, 0).toString());
				textField_Name.setText(df.getValueAt(selectedIndex, 1).toString());

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

					if (textField_PatientId.getText().equals("")) { // check
						JOptionPane.showMessageDialog(null, "please enter all equired data");
						return;
					}
					int selectedIndex = table.getSelectedRow();

					String s = "insert into patient(PId, Pname, Sex, BirthDate) values ('"
							+ textField_PatientId.getText() + "' , '" + textField_Name.getText() + "' , '"
							+ ComboBox_Sex.getSelectedItem().toString() + "' , '"
							+ ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText() + "')";

					st.execute(s);

					JOptionPane.showMessageDialog(null, "added");

					// make boxes empty after adding
					textField_PatientId.setText(null);
					textField_Name.setText(null);
					ComboBox_Sex.setSelectedItem("-");
					dateChooser.setDate(null);

					displayTable();

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1);
				}

			}
		});
		btnAdd.setBounds(39, 225, 89, 23);
		panel.add(btnAdd);

		JButton btnDisplay = new JButton("Display");
		btnDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayTable();
			}
		});
		btnDisplay.setBounds(97, 192, 89, 23);
		panel.add(btnDisplay);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRowCount() == 1) { // one row selected.
					int row = table.getSelectedRow();
					String cell = table.getModel().getValueAt(row, 0).toString();
					String query = "DELETE FROM mydb.patient where PId = " + cell;// to get selected row.

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
		btnDelete.setBounds(162, 225, 89, 23);
		panel.add(btnDelete);

		JLabel lblHeader = new JLabel("Patient");
		lblHeader.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setBounds(238, 11, 287, 38);
		panel.add(lblHeader);

		JLabel lblSex = new JLabel("Sex:");
		lblSex.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSex.setBounds(68, 111, 60, 23);
		panel.add(lblSex);

		JLabel lblBirthDate = new JLabel("BirthDate:");
		lblBirthDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBirthDate.setBounds(68, 134, 60, 23);
		panel.add(lblBirthDate);
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(101, 347, 85, 21);
		panel.add(btnBack);

		ComboBox_Sex = new JComboBox();
		ComboBox_Sex.setBounds(128, 112, 146, 21);
		ComboBox_Sex.addItem("-");
		ComboBox_Sex.addItem("M");
		ComboBox_Sex.addItem("F");
		panel.add(ComboBox_Sex);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(128, 134, 146, 19);
		panel.add(dateChooser);
		dateChooser.setDateFormatString("yyyy-MM-dd");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					st.close();
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Menu f = new Menu();
				f.frame.setVisible(true);
				frame.dispose();
			}
		});
	}

	public void displayTable() {
		if (table.getRowCount() >= 1)
			model.setRowCount(0);
		try {

			String query = "Select * From mydb.Patient";

			ResultSet rs = st.executeQuery(query);

			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();

			model = (DefaultTableModel) table.getModel();

			int col = rsmd.getColumnCount();

			String[] colName = new String[col];

			for (int i = 0; i < col; i++) {
				colName[i] = rsmd.getColumnName(i + 1);
			}

			model.setColumnIdentifiers(colName);

			String Pname, Sex, BirthDate;
			int PId;
			while (rs.next()) {
				PId = rs.getInt(1);
				Pname = rs.getString(2);
				Sex = rs.getString(3);
				BirthDate = rs.getString(4);

				Object[] row = { PId, Pname, Sex, BirthDate };
				model.addRow(row);
			}

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
	}
}
