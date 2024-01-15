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
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JDesktopPane;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.EtchedBorder;
import javax.swing.JComboBox;

public class Nurse {

	public JFrame frame;
	private JTextField textField_NurseId;
	private JTextField textField_Name;
	private JTable table;
	private JPanel panel, panel_NurseError;
	private String cell;
	private JComboBox comboBox_AvailableNurses;
	DefaultTableModel model;
	Connection con;
	Statement st;

	// Launch the application.

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Nurse window = new Nurse();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Create the application.

	public Nurse() {
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

		panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaption);
		panel.setBounds(0, 0, 801, 379);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNurseId = new JLabel("Nurse Id:");
		lblNurseId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNurseId.setBounds(68, 60, 60, 23);
		panel.add(lblNurseId);

		JLabel lblName = new JLabel("Name");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(68, 86, 60, 23);
		panel.add(lblName);

		textField_NurseId = new JTextField();
		textField_NurseId.setBounds(128, 61, 146, 20);
		panel.add(textField_NurseId);
		textField_NurseId.setColumns(10);

		textField_Name = new JTextField();
		textField_Name.setColumns(10);
		textField_Name.setBounds(128, 87, 146, 20);
		panel.add(textField_Name);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { // get data from tables to textField
				DefaultTableModel df = (DefaultTableModel) table.getModel();
				int selectedIndex = table.getSelectedRow();

				textField_NurseId.setText(df.getValueAt(selectedIndex, 0).toString());
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

					Statement stmt = con.createStatement();

					if (textField_NurseId.getText().equals("")) { // checks if
																	// boxes are
						// empty
						JOptionPane.showMessageDialog(null, "please enter Nurse Id");
						return;
					}
					String s = "insert into nurse (NuId, name) values ('" + textField_NurseId.getText() + "' , '"
							+ textField_Name.getText() + "')";

					stmt.execute(s);

					JOptionPane.showMessageDialog(null, "added");

					// make boxes empty after adding
					textField_NurseId.setText(null);
					textField_Name.setText(null);

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
		btnDisplay.setBounds(99, 180, 89, 23);
		panel.add(btnDisplay);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRowCount() == 1) { // one row selected.
					int row = table.getSelectedRow();
					cell = table.getModel().getValueAt(row, 0).toString();
					String query = "DELETE FROM mydb.nurse where NuID = " + cell;// to get selected row.
					int count = Count_AvailableNurses();

					try {
						PreparedStatement ps = con.prepareStatement(query);
						ps.execute();
						JOptionPane.showMessageDialog(null, "Deleted Succesfully");
					} catch (Exception q) {// if it is not deleted
						if (count == 0) {
							JOptionPane.showMessageDialog(null, "Can't delete. Nurse is working with a doctor.");
						} else {
							NurseDelete f = new NurseDelete(cell);
							f.frame.setVisible(true);
							frame.dispose();
							// JOptionPane.showMessageDialog(null, q);
						}
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

		JLabel lblHeader = new JLabel("Nurse");
		lblHeader.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setBounds(238, 11, 287, 38);
		panel.add(lblHeader);
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(99, 348, 85, 21);
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
	}

	public void displayTable() {
		// remove duplicated display
		if (table.getRowCount() >= 1)
			model.setRowCount(0);
		try {

			String query = "Select * From mydb.nurse";

			ResultSet rs = st.executeQuery(query);

			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();

			model = (DefaultTableModel) table.getModel();

			int col = rsmd.getColumnCount();

			String[] colName = new String[col];

			for (int i = 0; i < col; i++) {
				colName[i] = rsmd.getColumnName(i + 1);
			}

			model.setColumnIdentifiers(colName);

			String name;
			int NuID;
			while (rs.next()) {
				NuID = rs.getInt(1);
				name = rs.getString(2);

				Object[] row = { NuID, name };
				model.addRow(row);
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public void displayJbox_AvailableNurses() {

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
					comboBox_AvailableNurses.addItem(res.getString("NuID"));

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}

	}

	public int Count_AvailableNurses() {
		int count = 0;

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
					count++;

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

}
