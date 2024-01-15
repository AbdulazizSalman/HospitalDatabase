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

public class TestTypes {

	JFrame frame;
	private JTextField textField_TestId;
	private JTextField textField_TestName;
	private JTable table;
	DefaultTableModel model;
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
					TestTypes window = new TestTypes();
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
	public TestTypes() {
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

		JLabel lblTestId = new JLabel("Test Id");
		lblTestId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTestId.setBounds(58, 59, 60, 23);
		panel.add(lblTestId);

		JLabel lblTestName = new JLabel("Test Name");
		lblTestName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTestName.setBounds(29, 85, 89, 23);
		panel.add(lblTestName);

		textField_TestId = new JTextField();
		textField_TestId.setBounds(128, 61, 146, 20);
		panel.add(textField_TestId);
		textField_TestId.setColumns(10);

		textField_TestName = new JTextField();
		textField_TestName.setColumns(10);
		textField_TestName.setBounds(128, 87, 146, 20);
		panel.add(textField_TestName);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { // get data from tables to textField
				DefaultTableModel df = (DefaultTableModel) table.getModel();
				int selectedIndex = table.getSelectedRow();

				textField_TestId.setText(df.getValueAt(selectedIndex, 0).toString()); // test id
				textField_TestName.setText(df.getValueAt(selectedIndex, 1).toString()); // test name
			}
		});
		scrollPane.setBounds(316, 62, 475, 308);
		panel.add(scrollPane);

		table = new JTable();
		table.setBackground(SystemColor.scrollbar);
		scrollPane.setViewportView(table);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// insert
				try {

					if (textField_TestId.getText().equals("") || textField_TestName.getText().equals("")) { // checks if
																											// boxes are
						// empty
						JOptionPane.showMessageDialog(null, "please enter all data");
						return;
					}
					String s = "insert into teststype (TestId, testName) values ('" + textField_TestId.getText()
							+ "' , '" + textField_TestName.getText() + "')";

					st.execute(s);

					JOptionPane.showMessageDialog(null, "added");

					// make boxes empty after adding
					textField_TestId.setText(null);
					textField_TestName.setText(null);

					displayTable();

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1);
				}
			}
		});
		btnAdd.setBounds(39, 258, 89, 23);
		panel.add(btnAdd);

		JButton btnDisplay = new JButton("Display");
		btnDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					// removes duplicated display
					if (table.getRowCount() >= 1)
						model.setRowCount(0);

					String query = "Select * From mydb.teststype";

					ResultSet rs = st.executeQuery(query);

					ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();

					model = (DefaultTableModel) table.getModel();

					int col = rsmd.getColumnCount();

					String[] colName = new String[col];

					for (int i = 0; i < col; i++) {
						colName[i] = rsmd.getColumnName(i + 1);
					}

					model.setColumnIdentifiers(colName);

					String Tname;
					int TestId;
					while (rs.next()) {
						TestId = rs.getInt(1);
						Tname = rs.getString(2);

						Object[] row = { TestId, Tname };
						model.addRow(row);
					}

					// insert

				} catch (SQLException e1) {

					e1.printStackTrace();
				}
			}
		});
		btnDisplay.setBounds(104, 219, 89, 23);
		panel.add(btnDisplay);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRowCount() == 1) { // one row selected.
					int row = table.getSelectedRow();
					String cell = table.getModel().getValueAt(row, 0).toString();
					String query = "DELETE FROM mydb.teststype where TestId = " + cell;// to get selected row.

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
		btnDelete.setBounds(162, 258, 89, 23);
		panel.add(btnDelete);

		JLabel lblHeader = new JLabel("TestsTypes");
		lblHeader.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setBounds(238, 11, 287, 38);
		panel.add(lblHeader);
		btnBack = new JButton("Back");
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
		btnBack.setBounds(108, 347, 85, 21);
		panel.add(btnBack);
	}

	public void displayTable() {
		try {

			// removes duplicated display
			if (table.getRowCount() >= 1)
				model.setRowCount(0);

			String query = "Select * From mydb.teststype";

			ResultSet rs = st.executeQuery(query);

			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();

			model = (DefaultTableModel) table.getModel();

			int col = rsmd.getColumnCount();

			String[] colName = new String[col];

			for (int i = 0; i < col; i++) {
				colName[i] = rsmd.getColumnName(i + 1);
			}

			model.setColumnIdentifiers(colName);

			String Tname;
			int TestId;
			while (rs.next()) {
				TestId = rs.getInt(1);
				Tname = rs.getString(2);

				Object[] row = { TestId, Tname };
				model.addRow(row);
			}

			// insert

		} catch (SQLException e1) {

			e1.printStackTrace();
		}

	}
}
