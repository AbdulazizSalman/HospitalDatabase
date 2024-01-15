import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import javax.swing.SwingConstants;

public class NurseDelete {
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

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NurseDelete window = new NurseDelete(null);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Create the application.

	public NurseDelete(String c) {
		initialize(c);
	}

	// Initialize the contents of the frame.

	private void initialize(String c) {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "99001122Azoz");
			st = con.createStatement();

		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		cell = c;

		frame = new JFrame();
		frame.setBounds(100, 100, 813, 414);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaption);
		panel.setBounds(0, 0, 801, 379);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { // get data from tables to textField
				DefaultTableModel df = (DefaultTableModel) table.getModel();
				int selectedIndex = table.getSelectedRow();

			}
		});
		scrollPane.setBounds(316, 60, 475, 308);
		panel.add(scrollPane);

		table = new JTable();
		table.setBackground(SystemColor.scrollbar);
		scrollPane.setViewportView(table);

		panel_NurseError = new JPanel();
		panel_NurseError
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(160, 160, 160), new Color(0, 0, 0)));
		panel_NurseError.setBackground(SystemColor.controlHighlight);
		panel_NurseError.setBounds(10, 120, 296, 184);
		panel.add(panel_NurseError);
		panel_NurseError.setLayout(null);

		JLabel lblNewLabel = new JLabel("The nurse selected is working with a doctor. To ");
		lblNewLabel.setBounds(3, 11, 286, 14);
		panel_NurseError.add(lblNewLabel);
		
		JLabel lblHeader = new JLabel("Nurse");
		lblHeader.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setBounds(238, 11, 287, 38);
		panel.add(lblHeader);

		JLabel lblThisNurseYou = new JLabel("delete this nurse you need to choose a replacment");
		lblThisNurseYou.setBounds(3, 25, 286, 14);
		panel_NurseError.add(lblThisNurseYou);

		JLabel lblDoctorOrDelete = new JLabel("for the doctor.");
		lblDoctorOrDelete.setBounds(3, 38, 182, 14);
		panel_NurseError.add(lblDoctorOrDelete);

		JButton btnRepalce = new JButton("Replace");
		btnRepalce.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					Statement stmt = con.createStatement();

					if (comboBox_AvailableNurses.getSelectedItem().toString().equals("-")) { // checks if
						// boxes are
						// empty
						JOptionPane.showMessageDialog(null, "please choose replacment.");
						return;
					}
					String s = "update mydb.Doctor set NuID = " + comboBox_AvailableNurses.getSelectedItem().toString()
							+ " where NuID = " + cell;
					String t = "DELETE FROM mydb.Nurse where NuID = " + cell;

					stmt.execute(s);
					stmt.execute(t);

					JOptionPane.showMessageDialog(null, "Replaced And Deleted succesfuly");

					// make boxes empty after adding
					displayTable();

				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}
				Nurse f = new Nurse();
				f.frame.setVisible(true);
				frame.dispose();
			}

		});
		btnRepalce.setBounds(38, 82, 89, 23);
		panel_NurseError.add(btnRepalce);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Nurse f = new Nurse();
				f.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnCancel.setBounds(38, 116, 89, 23);
		panel_NurseError.add(btnCancel);

		comboBox_AvailableNurses = new JComboBox();
		comboBox_AvailableNurses.setBounds(174, 82, 81, 22);
		panel_NurseError.add(comboBox_AvailableNurses);
		comboBox_AvailableNurses.addItem("-");

		JLabel lblNewLabel_1 = new JLabel("Available Nurses");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(161, 68, 100, 14);
		panel_NurseError.add(lblNewLabel_1);

		displayTable();
		displayJbox_AvailableNurses();

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

}
