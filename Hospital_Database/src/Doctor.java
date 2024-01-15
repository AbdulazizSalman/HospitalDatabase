import java.awt.EventQueue;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.SystemColor;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Doctor {

	public JFrame frame;
	private JTextField textField_Did;
	private JTextField textField_Specialization;
	private JTextField textField_Name;
	private JTextField textField_Degree;
	private JTable table;
	private JComboBox comboBox_NurseId;
	private JButton btnDisplay;
	DefaultTableModel model;
	Connection con;
	Statement st;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Doctor window = new Doctor();
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
	public Doctor() {
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

		JLabel lblDoctorId = new JLabel("Doctor Id");
		lblDoctorId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDoctorId.setBounds(68, 60, 60, 23);
		panel.add(lblDoctorId);

		JLabel lblNurseId = new JLabel("Nurse Id");
		lblNurseId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNurseId.setBounds(68, 156, 60, 23);
		panel.add(lblNurseId);

		JLabel lblName = new JLabel("Name");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(68, 84, 60, 23);
		panel.add(lblName);

		JLabel lblSpecialization = new JLabel("Specialization");
		lblSpecialization.setBounds(64, 109, 64, 23);
		panel.add(lblSpecialization);

		JLabel lblDegree = new JLabel("Degree");
		lblDegree.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDegree.setBounds(68, 132, 60, 23);
		panel.add(lblDegree);

		textField_Did = new JTextField();
		textField_Did.setBounds(128, 61, 146, 20);
		panel.add(textField_Did);
		textField_Did.setColumns(10);

		textField_Specialization = new JTextField();
		textField_Specialization.setColumns(10);
		textField_Specialization.setBounds(128, 110, 146, 20);
		panel.add(textField_Specialization);

		textField_Name = new JTextField();
		textField_Name.setColumns(10);
		textField_Name.setBounds(128, 85, 146, 20);
		panel.add(textField_Name);

		textField_Degree = new JTextField();
		textField_Degree.setColumns(10);
		textField_Degree.setBounds(128, 133, 146, 20);
		panel.add(textField_Degree);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { // get data from tables to textField
				DefaultTableModel df = (DefaultTableModel) table.getModel();

				int selectedIndex = table.getSelectedRow();

				String nuid = df.getValueAt(selectedIndex, 4).toString();

				textField_Did.setText(df.getValueAt(selectedIndex, 0).toString());

				for (int i = 0; i < comboBox_NurseId.getItemCount(); i++) {
					boolean flag = true;
					if (comboBox_NurseId.getItemAt(i).toString().equalsIgnoreCase(nuid))
						comboBox_NurseId.setSelectedIndex(i);

					textField_Name.setText(df.getValueAt(selectedIndex, 1).toString());
					textField_Specialization.setText(df.getValueAt(selectedIndex, 2).toString());
					textField_Degree.setText(df.getValueAt(selectedIndex, 3).toString());

				}
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
				// insert
				try {

					if (textField_Did.getText().equals("")
							|| comboBox_NurseId.getSelectedItem().toString().equals("-")) { // checks if boxes are
						// empty
						JOptionPane.showMessageDialog(null, "please enter all required data");
						return;
					}
					int selectedIndex = table.getSelectedRow();

					String s = "insert into doctor(Did, name, specialization, Degree, NuID) values ('"
							+ textField_Did.getText() + "' , '" + textField_Name.getText() + "' , '"
							+ textField_Specialization.getText() + "' , '" + textField_Degree.getText() + "' ," + " '"
							+ comboBox_NurseId.getSelectedItem().toString() + "')";

					st.execute(s);

					JOptionPane.showMessageDialog(null, "added");

					// make boxes empty after adding
					textField_Did.setText(null);
					textField_Name.setText(null);
					textField_Specialization.setText(null);
					textField_Degree.setText(null);

					displayTable();

					// con.close();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1);
				}
			}
		});
		btnAdd.setBounds(39, 256, 89, 23);
		panel.add(btnAdd);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRowCount() == 1) { // one row selected.
					int row = table.getSelectedRow();
					String cell = table.getModel().getValueAt(row, 0).toString();
					String query = "DELETE FROM mydb.doctor where Did = " + cell;// to get selected row.

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
		btnDelete.setBounds(153, 256, 89, 23);
		panel.add(btnDelete);

		btnDisplay = new JButton("Display");
		btnDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayTable();
			}
		});
		btnDisplay.setBounds(93, 223, 89, 23);
		panel.add(btnDisplay);

		JLabel lblHeader = new JLabel("Doctor");
		lblHeader.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setBounds(238, 11, 287, 38);
		panel.add(lblHeader);

		JList list = new JList();
		list.setBounds(250, 337, -53, -21);
		panel.add(list);

		comboBox_NurseId = new JComboBox();
		comboBox_NurseId.setBounds(128, 156, 146, 22);
		panel.add(comboBox_NurseId);
		comboBox_NurseId.addItem("-");

		JButton btnBack = new JButton("Back");
		btnBack.setBounds(97, 348, 85, 21);
		panel.add(btnBack);
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

		displayJbox();
	}

	public void displayJbox() {
		comboBox_NurseId.removeAllItems();
		comboBox_NurseId.addItem("-");

		String sql = "select * from nurse";
		String sqlDoctor = "select * from mydb.doctor";
		boolean flag = true;
		ResultSet rs = null;
		try {
			rs = st.executeQuery(sqlDoctor);
		} catch (SQLException e1) {
			flag = true;
		}

		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet res = stmt.executeQuery();

			int NuID;

			while (res.next()) {
				NuID = res.getInt(1);
				rs = st.executeQuery(sqlDoctor);
				flag = true;
				while (rs.next()) {
					if (NuID == rs.getInt(5))
						flag = false;
				}
				if (flag)
					comboBox_NurseId.addItem(res.getString("NuID"));

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}

	}

	public void displayTable() {
		if (table.getRowCount() >= 1)
			model.setRowCount(0);
		displayJbox();

		try {

			String query = "Select * From mydb.doctor";

			ResultSet rs = st.executeQuery(query);

			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();

			model = (DefaultTableModel) table.getModel();

			int col = rsmd.getColumnCount();

			String[] colName = new String[col];

			for (int i = 0; i < col; i++) {
				colName[i] = rsmd.getColumnName(i + 1);
			}

			model.setColumnIdentifiers(colName);

			String name, specialization, Degree;
			int Did, NuID;
			while (rs.next()) {
				Did = rs.getInt(1);
				name = rs.getString(2);
				specialization = rs.getString(3);
				Degree = rs.getString(4);
				NuID = rs.getInt(5);
				Object[] row = { Did, name, specialization, Degree, NuID };
				model.addRow(row);
			}

			displayJbox();

		} catch (SQLException e1) {

			e1.printStackTrace();
		}

	}
}
