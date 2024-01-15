import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
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
import java.awt.*;
import javax.swing.JComboBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.toedter.calendar.JDateChooser;
import com.toedter.components.JLocaleChooser;

public class Appointment {

	private JComboBox comboBox_Pid, comboBox_Did, comboBox_hour, comboBox_minute, comboBox_ampm;
	public JFrame frame;
	private JTable table;
	DefaultTableModel model;
	private JDateChooser dateChooser;
	Connection con;
	Statement st;
	private JButton btnBack;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Appointment window = new Appointment();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws SQLException
	 */
	public Appointment() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
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
		lblPatientId.setBounds(68, 87, 60, 23);
		panel.add(lblPatientId);

		JLabel lblDoctorId = new JLabel("Doctor Id:");
		lblDoctorId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDoctorId.setBounds(68, 60, 60, 23);
		panel.add(lblDoctorId);

		JScrollPane scrollPane_Table = new JScrollPane();
		scrollPane_Table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultTableModel df = (DefaultTableModel) table.getModel();

				int selectedIndex = table.getSelectedRow();

				String did = df.getValueAt(selectedIndex, 0).toString();

				for (int i = 0; i < comboBox_Did.getItemCount(); i++)
					if (comboBox_Did.getItemAt(i).toString().equalsIgnoreCase(did))
						comboBox_Did.setSelectedIndex(i);

				String pid = df.getValueAt(selectedIndex, 1).toString();

				for (int i = 0; i < comboBox_Pid.getItemCount(); i++)
					if (comboBox_Pid.getItemAt(i).toString().equalsIgnoreCase(pid))
						comboBox_Pid.setSelectedIndex(i);

			}
		});
		scrollPane_Table.setBounds(316, 60, 475, 308);
		panel.add(scrollPane_Table);

		table = new JTable();
		table.setBackground(SystemColor.scrollbar);
		scrollPane_Table.setViewportView(table);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					if (comboBox_Pid.getSelectedItem().toString().equals("-")
							|| comboBox_Did.getSelectedItem().toString().equals("-") || dateChooser.getDate() == null
							|| comboBox_hour.getSelectedItem().toString().equals("-")
							|| comboBox_minute.getSelectedItem().toString().equals("-")
							|| comboBox_ampm.getSelectedItem().toString().equals("-")) { // checks if boxes are
																							// empty
						JOptionPane.showMessageDialog(null, "please enter all required data");
						return;
					}
					int selectedIndex = table.getSelectedRow();

					String sql = "insert into appointment(doctor_did, patient_pid, hours, apdate) values ('"
							+ comboBox_Did.getSelectedItem().toString() + "' , '"
							+ comboBox_Pid.getSelectedItem().toString() + "' , '"
							+ comboBox_hour.getSelectedItem().toString() + ":"
							+ comboBox_minute.getSelectedItem().toString() + " "
							+ comboBox_ampm.getSelectedItem().toString() + "' , '"
							+ ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText() + "')";

					st.execute(sql);

					JOptionPane.showMessageDialog(null, "added");

					// make boxes empty after adding
					dateChooser.setDate(null);
					comboBox_Pid.setSelectedItem("-");
					comboBox_Did.setSelectedItem("-");
					comboBox_hour.setSelectedItem("-");
					comboBox_minute.setSelectedItem("-");
					comboBox_ampm.setSelectedItem("-");

					displayTable();

				} catch (Exception e1) {

					JOptionPane.showMessageDialog(null, e1);
				}
			}
		});

		btnAdd.setBounds(45, 226, 89, 23);
		panel.add(btnAdd);

		JButton btnDisplay = new JButton("Display");
		btnDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayTable();
			}
		});

		btnDisplay.setBounds(99, 192, 89, 23);
		panel.add(btnDisplay);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (table.getSelectedRowCount() == 1) { // one row selected.
					int row = table.getSelectedRow();
					String cell = table.getModel().getValueAt(row, 0).toString();
					String cell1 = table.getModel().getValueAt(row, 1).toString();
					String cell2 = table.getModel().getValueAt(row, 2).toString();
					String cell3 = table.getModel().getValueAt(row, 3).toString();
					System.out.println("DELETE FROM mydb.appointment where Doctor_Did = " + cell + " And Patient_PId = "
							+ cell1 + " AND Hours= " + cell2 + " AND ApDate= " + cell3);
					String query = "DELETE FROM mydb.appointment where Doctor_Did = " + cell + " And Patient_PId = "
							+ cell1 + " AND Hours= " + "'" + cell2 + "'" + " AND ApDate= " + "'" + cell3 + "'";// to
																												// get
																												// selected
					// row.

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
		btnDelete.setBounds(151, 225, 89, 23);
		panel.add(btnDelete);

		JLabel lblHeader = new JLabel("Appointment");
		lblHeader.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setBounds(238, 11, 287, 38);
		panel.add(lblHeader);

		JLabel lblApdate = new JLabel("ApDate:");
		lblApdate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblApdate.setBounds(68, 137, 60, 23);
		panel.add(lblApdate);

		JLabel lblHours = new JLabel("Hours:");
		lblHours.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHours.setBounds(68, 112, 60, 23);
		panel.add(lblHours);

		comboBox_Pid = new JComboBox(); // Patient Id
		comboBox_Pid.setBounds(128, 87, 146, 22);
		panel.add(comboBox_Pid);
		comboBox_Pid.addItem("-");

		comboBox_Did = new JComboBox(); // Doctor Id
		comboBox_Did.setBounds(128, 60, 146, 22);
		comboBox_Did.addItem("-");
		panel.add(comboBox_Did);

		btnBack = new JButton("Back");
		btnBack.setBounds(99, 347, 85, 21);
		panel.add(btnBack);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(128, 137, 146, 19);
		panel.add(dateChooser);
		dateChooser.setDateFormatString("yyyy-MM-dd");

		comboBox_hour = new JComboBox(); // Hours
		comboBox_hour.addItem("-");
		comboBox_hour.addItem("1");
		comboBox_hour.addItem("2");
		comboBox_hour.addItem("3");
		comboBox_hour.addItem("4");
		comboBox_hour.addItem("5");
		comboBox_hour.addItem("6");
		comboBox_hour.addItem("7");
		comboBox_hour.addItem("8");
		comboBox_hour.addItem("9");
		comboBox_hour.addItem("10");
		comboBox_hour.addItem("11");
		comboBox_hour.addItem("12");

		comboBox_hour.setBounds(128, 112, 52, 22);
		panel.add(comboBox_hour);

		comboBox_minute = new JComboBox(); // minutes
		comboBox_minute.addItem("-");
		comboBox_minute.addItem("00");
		comboBox_minute.addItem("01");
		comboBox_minute.addItem("02");
		comboBox_minute.addItem("03");
		comboBox_minute.addItem("04");
		comboBox_minute.addItem("05");
		comboBox_minute.addItem("06");
		comboBox_minute.addItem("07");
		comboBox_minute.addItem("08");
		comboBox_minute.addItem("09");
		comboBox_minute.addItem("10");
		comboBox_minute.addItem("11");
		comboBox_minute.addItem("12");
		comboBox_minute.addItem("13");
		comboBox_minute.addItem("14");
		comboBox_minute.addItem("15");
		comboBox_minute.addItem("16");
		comboBox_minute.addItem("17");
		comboBox_minute.addItem("18");
		comboBox_minute.addItem("19");
		comboBox_minute.addItem("20");
		comboBox_minute.addItem("21");
		comboBox_minute.addItem("22");
		comboBox_minute.addItem("23");
		comboBox_minute.addItem("24");
		comboBox_minute.addItem("25");
		comboBox_minute.addItem("26");
		comboBox_minute.addItem("27");
		comboBox_minute.addItem("28");
		comboBox_minute.addItem("29");
		comboBox_minute.addItem("30");
		comboBox_minute.addItem("31");
		comboBox_minute.addItem("32");
		comboBox_minute.addItem("33");
		comboBox_minute.addItem("34");
		comboBox_minute.addItem("35");
		comboBox_minute.addItem("36");
		comboBox_minute.addItem("37");
		comboBox_minute.addItem("38");
		comboBox_minute.addItem("39");
		comboBox_minute.addItem("40");
		comboBox_minute.addItem("41");
		comboBox_minute.addItem("42");
		comboBox_minute.addItem("43");
		comboBox_minute.addItem("44");
		comboBox_minute.addItem("45");
		comboBox_minute.addItem("46");
		comboBox_minute.addItem("47");
		comboBox_minute.addItem("48");
		comboBox_minute.addItem("49");
		comboBox_minute.addItem("50");
		comboBox_minute.addItem("51");
		comboBox_minute.addItem("52");
		comboBox_minute.addItem("53");
		comboBox_minute.addItem("54");
		comboBox_minute.addItem("55");
		comboBox_minute.addItem("56");
		comboBox_minute.addItem("57");
		comboBox_minute.addItem("58");
		comboBox_minute.addItem("59");

		comboBox_minute.setBounds(179, 112, 52, 22);
		panel.add(comboBox_minute);

		comboBox_ampm = new JComboBox(); // pm and am
		comboBox_ampm.addItem("-");
		comboBox_ampm.addItem("AM");
		comboBox_ampm.addItem("PM");
		comboBox_ampm.setBounds(230, 112, 44, 22);
		panel.add(comboBox_ampm);
		dateChooser.getJCalendar().setMinSelectableDate(new java.util.Date());
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
		displayJbox_Did();
		displayJbox_Pid();

	}

	public void displayJbox_Did() {
		String sql = "select * from doctor";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet res = stmt.executeQuery();

			while (res.next()) {
				comboBox_Did.addItem(res.getString("did"));
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public void displayJbox_Pid() {
		String sql = "select * from patient";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet res = stmt.executeQuery();

			while (res.next()) {
				comboBox_Pid.addItem(res.getString("pid"));
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public void displayTable() {
		if (table.getRowCount() >= 1)
			model.setRowCount(0);
		try {
			String query = "Select * From mydb.Appointment";

			ResultSet rs = st.executeQuery(query);

			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();

			model = (DefaultTableModel) table.getModel();

			int col = rsmd.getColumnCount();

			String[] colName = new String[col];

			for (int i = 0; i < col; i++) {
				colName[i] = rsmd.getColumnName(i + 1);
			}

			model.setColumnIdentifiers(colName);

			String Hour, Apdate;
			int Did, Pid;
			while (rs.next()) {
				Did = rs.getInt(1);
				Pid = rs.getInt(2);
				Hour = rs.getString(3);
				Apdate = rs.getString(4);

				Object[] row = { Did, Pid, Hour, Apdate };
				model.addRow(row);
			}

		} catch (SQLException e1) {

			e1.printStackTrace();
		}

	}
}
