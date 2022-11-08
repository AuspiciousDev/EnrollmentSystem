package enrollmentFrame;

import java.awt.Color;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class dashboardFrame extends javax.swing.JFrame {

    Connection con = null;
    ResultSet rst = null;
    PreparedStatement pst = null;
    Statement stmt = null;
    PreparedStatement pstUpdate = null;

    String CurrUserID;
    String dbEmpFULLName;
    String SelectedEmployeeID;
    String defPassword = "P@$$W0RD2000";
    DefaultTableModel modelEmpTable;
    String SQL_URL = "jdbc:sqlserver://localhost:1433;user=admin;password=admin123;DatabaseName=ADPEnrollmentSystem;integrated‌​Security=true";
    String sqlGetUser = "SELECT EmpID, FName,MName,LName FROM EMPLOYEE WHERE EmpID = ?";

    String sqlEmpAdd = "INSERT INTO EMPLOYEE(EmpID,FName,MName,LName,EmpType,EmpStatus) VALUES (?,?,?,?,?,?)";
    String sqlUserAdd = "INSERT INTO USERS(Username,Password) VALUES (?,?)";

    String sqlGetEmp = "SELECT USERS.Username, EMPLOYEE.FName,EMPLOYEE.MName,EMPLOYEE.LName,EMPLOYEE.EmpType,EMPLOYEE.EmpStatus\n"
            + "FROM USERS LEFT JOIN EMPLOYEE ON EMPLOYEE.EmpID =  USERS.Username  ";
    String sqlPatchEmployee = "UPDATE EMPLOYEE SET FName =? ,MName = ?,LName=?, EmpType =?,EmpStatus = ? WHERE EmpID =?";
    String sqlPatchPassword = "UPDATE USERS SET Password = ? WHERE Username =?";

    /**
     * Creates new form dashboardFrame
     */
    public dashboardFrame() {
        initComponents();
        adminIcon.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("personIcon.png")).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
        dashboardIcon.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("wdashboardIcon.png")).getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)));
        transactionIcon.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("wtransactionIcon.png")).getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)));
        dataIcon.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("wdataIcon.png")).getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)));
        maintenanceIcon.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("wmaintenanceIcon.png")).getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)));
        recordsIcon.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("wrecordsIcon.png")).getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)));
        usersIcon.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("wsettingsIcon.png")).getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)));
        logoutIcon.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("wlogoutIcon.png")).getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)));
        perUserTableOnClick();
    }

    public dashboardFrame(String empID) {
        initComponents();
        CurrUserID = empID;
        getCurrentUserDB();
        perUserTableOnClick();

    }

    static String createRandomPassword(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    private void getCurrentUserDB() {
        con = null;
        pst = null;
        String dbEmpID = "";
        String dbEmpFName = "";
        String dbEmpMName = "";
        String dbEmpLName = "";

        try {
            con = DriverManager.getConnection(SQL_URL);
            pst = con.prepareStatement(sqlGetUser);
            pst.setString(1, CurrUserID); // 
            rst = pst.executeQuery();
            while (rst.next()) {
                CurrUserID = rst.getString("EmpID");
                dbEmpID = rst.getString("EmpID");
                dbEmpFName = rst.getString("FName");
                dbEmpMName = rst.getString("MName");
                dbEmpLName = rst.getString("LName");

            }
            dbEmpFULLName = dbEmpFName + " " + dbEmpMName + " " + dbEmpLName;
            lblName.setText(dbEmpFULLName);
            con.close();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, "Nav : Connection Error" + e, "Database Notice", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {

        String EmpID = txtEmpID1.getText();
        String FName = txtFName1.getText();
        String MName = txtMName1.getText();
        String LName = txtLName1.getText();
        String EmpStatus = comboEmpStatusUp.getSelectedItem().toString();
        String EmpType = comboEmpTypeUp.getSelectedItem().toString();
        con = null;
        pst = null;
        try {
            con = DriverManager.getConnection(SQL_URL);
            pst = con.prepareStatement(sqlPatchEmployee);
            pst.setString(1, FName);
            pst.setString(2, MName);
            pst.setString(3, LName);
            pst.setString(4, EmpType);
            pst.setString(5, EmpStatus);
            pst.setString(6, EmpID);
            int x = pst.executeUpdate();
            if (x == 1) {
                setEmployeeTable();
                JOptionPane.showMessageDialog(this, "Employee Updated!", "Database Notice!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Employee Update : Connection Error" + ex, "Dash Board Notice!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addEmployeeAndUserToDB() {

        String empID = txtEmpID.getText().toString();
        String FName = txtFName.getText().toString();
        String MName = txtMName.getText().toString();
        String LName = txtLName.getText().toString();
        String empType = comboEmpType.getSelectedItem().toString();
        String empStatus = comboEmpStatusUp.getSelectedItem().toString();
        con = null;
        pst = null;
        try {
            con = DriverManager.getConnection(SQL_URL);
            pst = con.prepareStatement(sqlEmpAdd);
            pst.setString(1, empID);
            pst.setString(2, FName);
            pst.setString(3, MName);
            pst.setString(4, LName);
            pst.setString(5, empType);
            pst.setString(6, empStatus);
            int x = pst.executeUpdate();
            if (x == 1) {
                con = null;
                pst = null;
                con = DriverManager.getConnection(SQL_URL);
                pst = con.prepareStatement(sqlUserAdd);
                pst.setString(1, empID);
                pst.setString(2, defPassword);
                int y = pst.executeUpdate();
                if (y == 1) {
                    txtEmpID.setText("");
                    txtFName.setText("");
                    txtMName.setText("");
                    txtLName.setText("");
                    comboEmpType.setSelectedIndex(0);
                    comboEmpStatusUp.setSelectedIndex(0);
                    setEmployeeTable();
                    JOptionPane.showMessageDialog(this, "Employee Added!", "Database Notice!", JOptionPane.INFORMATION_MESSAGE);

                }
            } else {
                JOptionPane.showMessageDialog(this, "Employee Add Failed!", "Database Notice!", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Employee Entry : Connection Error" + ex, "Dash Board Notice!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setEmployeeTable() {
        modelEmpTable = (DefaultTableModel) tableEmp.getModel();
        modelEmpTable.setRowCount(0);
        try {
            con = DriverManager.getConnection(SQL_URL);
            pst = con.prepareStatement(sqlGetEmp);
            rst = pst.executeQuery();
            while (rst.next()) {
                String empID = rst.getString("Username");
                String FName = rst.getString("FName");
                String MName = rst.getString("MName");
                String LName = rst.getString("LName");

                String empType = rst.getString("EmpType");
                String empStatus = rst.getString("EmpStatus");

                String tbData[] = {empID, FName, MName, LName, empType, empStatus};
                modelEmpTable.addRow(tbData);
            }
            con.close();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, "PRODUCT : Connection Error" + e, "Dash Board Notice!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void perUserTableOnClick() {

        tableEmp.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableEmp.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    System.out.println("Mouse Click Selected Row Count: " + tableEmp.getSelectedRow());
                }
            }
        });
        tableEmp.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    if (column == 0 || column <= 3) {
                        System.out.println("asdasdsd " + row + " " + column);
                        setEmployeeToUpdateFields(row);
                    }

                    // do some action if appropriate column
                }
            }
        });

    }

    private void setEmployeeToUpdateFields(int row) {
        jTabbedPane1.setSelectedIndex(1);
        String empID = String.valueOf(tableEmp.getModel().getValueAt(row, 0));
        String empFname = String.valueOf(tableEmp.getModel().getValueAt(row, 1));
        String empMName = String.valueOf(tableEmp.getModel().getValueAt(row, 2));
        String empLName = String.valueOf(tableEmp.getModel().getValueAt(row, 3));
        String empType = String.valueOf(tableEmp.getModel().getValueAt(row, 4));
        String empStatus = String.valueOf(tableEmp.getModel().getValueAt(row, 5));

        txtEmpID1.setText(empID);
        txtFName1.setText(empFname);
        txtMName1.setText(empMName);
        txtLName1.setText(empLName);
        System.out.println(empStatus);
        if (empType.equals("Registrar")) {
            comboEmpTypeUp.setSelectedIndex(0);
        }
        if (empType.equals("Teacher")) {
            comboEmpTypeUp.setSelectedIndex(1);
        }
        if (empType.equals("Staff")) {
            comboEmpTypeUp.setSelectedIndex(2);
        }
        if (empStatus.equals("ACTIVE")) {
            comboEmpStatusUp.setSelectedIndex(0);
        }
        if (empStatus.equals("INACTIVE")) {
            comboEmpStatusUp.setSelectedIndex(1);
        }

    }

    private void resetPassword() {
        String empID = txtEmpID1.getText();
        con = null;
        pst = null;
        try {
            con = DriverManager.getConnection(SQL_URL);
            pst = con.prepareStatement(sqlPatchPassword);
            pst.setString(1, defPassword);
            pst.setString(2, empID);
            System.out.println(empID + " " + defPassword);
            int x = pst.executeUpdate();
            if (x == 1) {
                JOptionPane.showMessageDialog(this, "Employee [" + empID + "] Password Reset Success!", "Database Notice!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Employee  [" + empID + "]Password Reset Failed!", "Database Notice!", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Employee Entry : Connection Error" + ex, "Dash Board Notice!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidePanel = new javax.swing.JPanel();
        adminIcon = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        dashboardIcon = new javax.swing.JLabel();
        transactionIcon = new javax.swing.JLabel();
        dataIcon = new javax.swing.JLabel();
        maintenanceIcon = new javax.swing.JLabel();
        recordsIcon = new javax.swing.JLabel();
        usersIcon = new javax.swing.JLabel();
        logoutIcon = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        btnLogout1 = new javax.swing.JButton();
        btnDashboard = new javax.swing.JButton();
        btnDashboard1 = new javax.swing.JButton();
        btnDashboard2 = new javax.swing.JButton();
        btnDashboard3 = new javax.swing.JButton();
        btnDashboard4 = new javax.swing.JButton();
        btnDashboard5 = new javax.swing.JButton();
        btnDashboard6 = new javax.swing.JButton();
        settingsIcon1 = new javax.swing.JLabel();
        contentPanel = new javax.swing.JPanel();
        dashPanel = new javax.swing.JPanel();
        userPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableEmp = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        addUsersPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtEmpID = new javax.swing.JTextField();
        comboEmpType = new javax.swing.JComboBox<>();
        txtLName = new javax.swing.JTextField();
        txtFName = new javax.swing.JTextField();
        txtMName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnCreateEmployee = new javax.swing.JButton();
        btnUpdateEmployee1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        comboEmpStatus = new javax.swing.JComboBox<>();
        updateUsersPanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtEmpID1 = new javax.swing.JTextField();
        comboEmpTypeUp = new javax.swing.JComboBox<>();
        txtLName1 = new javax.swing.JTextField();
        txtFName1 = new javax.swing.JTextField();
        txtMName1 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        btnUpdateEmployee = new javax.swing.JButton();
        btnUpdateEmployee3 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        comboEmpStatusUp = new javax.swing.JComboBox<>();
        btnResetPassword = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        sidePanel.setBackground(new java.awt.Color(153, 153, 153));
        sidePanel.setPreferredSize(new java.awt.Dimension(250, 800));

        adminIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        adminIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/personIcon.png"))); // NOI18N

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 255, 102));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("WELCOME");

        dashboardIcon.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        dashboardIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/wdashboardIcon.png"))); // NOI18N
        dashboardIcon.setPreferredSize(new java.awt.Dimension(22, 22));

        transactionIcon.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        transactionIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/wtransactionIcon.png"))); // NOI18N
        transactionIcon.setPreferredSize(new java.awt.Dimension(22, 22));

        dataIcon.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        dataIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/wdataIcon.png"))); // NOI18N
        dataIcon.setPreferredSize(new java.awt.Dimension(22, 22));

        maintenanceIcon.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        maintenanceIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/wmaintenanceIcon.png"))); // NOI18N
        maintenanceIcon.setPreferredSize(new java.awt.Dimension(22, 22));

        recordsIcon.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        recordsIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/wrecordsIcon.png"))); // NOI18N
        recordsIcon.setPreferredSize(new java.awt.Dimension(22, 22));

        usersIcon.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        usersIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/wgroup.png"))); // NOI18N
        usersIcon.setPreferredSize(new java.awt.Dimension(22, 22));

        logoutIcon.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        logoutIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/logoutIcon.png"))); // NOI18N
        logoutIcon.setPreferredSize(new java.awt.Dimension(36, 36));

        lblName.setBackground(new java.awt.Color(255, 255, 255));
        lblName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        lblName.setForeground(new java.awt.Color(255, 255, 255));
        lblName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblName.setText("Lorem Ipsum Ripsum");

        btnLogout1.setBackground(new java.awt.Color(51, 0, 0));
        btnLogout1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLogout1.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout1.setText("LOGOUT");
        btnLogout1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout1.setMaximumSize(new java.awt.Dimension(83, 25));
        btnLogout1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogout1ActionPerformed(evt);
            }
        });

        btnDashboard.setBackground(new java.awt.Color(51, 51, 51));
        btnDashboard.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDashboard.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard.setText("TRANSACTION");
        btnDashboard.setMaximumSize(new java.awt.Dimension(113, 25));
        btnDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboardActionPerformed(evt);
            }
        });

        btnDashboard1.setBackground(new java.awt.Color(51, 51, 51));
        btnDashboard1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDashboard1.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard1.setText("DASH BOARD");
        btnDashboard1.setMaximumSize(new java.awt.Dimension(113, 25));
        btnDashboard1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboard1ActionPerformed(evt);
            }
        });

        btnDashboard2.setBackground(new java.awt.Color(51, 51, 51));
        btnDashboard2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDashboard2.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard2.setText("DATA ENTRY");
        btnDashboard2.setMaximumSize(new java.awt.Dimension(113, 25));
        btnDashboard2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboard2ActionPerformed(evt);
            }
        });

        btnDashboard3.setBackground(new java.awt.Color(51, 51, 51));
        btnDashboard3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDashboard3.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard3.setText("RECORDS AND REPORTS");
        btnDashboard3.setMaximumSize(new java.awt.Dimension(113, 25));
        btnDashboard3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboard3ActionPerformed(evt);
            }
        });

        btnDashboard4.setBackground(new java.awt.Color(51, 51, 51));
        btnDashboard4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDashboard4.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard4.setText("MAINTENANCE");
        btnDashboard4.setMaximumSize(new java.awt.Dimension(113, 25));
        btnDashboard4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboard4ActionPerformed(evt);
            }
        });

        btnDashboard5.setBackground(new java.awt.Color(51, 51, 51));
        btnDashboard5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDashboard5.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard5.setText("SETTINGS");
        btnDashboard5.setMaximumSize(new java.awt.Dimension(113, 25));
        btnDashboard5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboard5ActionPerformed(evt);
            }
        });

        btnDashboard6.setBackground(new java.awt.Color(51, 51, 51));
        btnDashboard6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDashboard6.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard6.setText("EMPLOYEE");
        btnDashboard6.setMaximumSize(new java.awt.Dimension(113, 25));
        btnDashboard6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboard6ActionPerformed(evt);
            }
        });

        settingsIcon1.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        settingsIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/wsettingsIcon.png"))); // NOI18N
        settingsIcon1.setPreferredSize(new java.awt.Dimension(22, 22));

        javax.swing.GroupLayout sidePanelLayout = new javax.swing.GroupLayout(sidePanel);
        sidePanel.setLayout(sidePanelLayout);
        sidePanelLayout.setHorizontalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sidePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanelLayout.createSequentialGroup()
                                .addGap(0, 17, Short.MAX_VALUE)
                                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(sidePanelLayout.createSequentialGroup()
                                            .addComponent(logoutIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(btnLogout1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(sidePanelLayout.createSequentialGroup()
                                            .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(recordsIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(dataIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(transactionIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(dashboardIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(btnDashboard3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(btnDashboard2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(btnDashboard, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnDashboard1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, sidePanelLayout.createSequentialGroup()
                                        .addComponent(usersIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnDashboard6, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(sidePanelLayout.createSequentialGroup()
                                        .addComponent(maintenanceIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnDashboard4, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(sidePanelLayout.createSequentialGroup()
                                        .addComponent(settingsIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnDashboard5, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(35, 35, 35))))
                    .addGroup(sidePanelLayout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(adminIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        sidePanelLayout.setVerticalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adminIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(sidePanelLayout.createSequentialGroup()
                        .addComponent(lblName)
                        .addGap(30, 30, 30)
                        .addComponent(btnDashboard1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dashboardIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transactionIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDashboard2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dataIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDashboard3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recordsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDashboard6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usersIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDashboard4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maintenanceIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDashboard5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLogout1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logoutIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );

        contentPanel.setLayout(new java.awt.CardLayout());

        dashPanel.setBackground(new java.awt.Color(153, 255, 255));

        javax.swing.GroupLayout dashPanelLayout = new javax.swing.GroupLayout(dashPanel);
        dashPanel.setLayout(dashPanelLayout);
        dashPanelLayout.setHorizontalGroup(
            dashPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1272, Short.MAX_VALUE)
        );
        dashPanelLayout.setVerticalGroup(
            dashPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 826, Short.MAX_VALUE)
        );

        contentPanel.add(dashPanel, "card2");

        userPanel.setBackground(new java.awt.Color(204, 204, 204));

        tableEmp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee ID", "First Name", "Middle Name", "Last Name", "Employee Type", "Employee Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableEmp);

        addUsersPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setText("EMPLOYMENT INFORMATION");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setText("General Information");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Last name");

        txtEmpID.setToolTipText("asdasdasdas");

        comboEmpType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Registrar", "Teacher", "Staff" }));

        txtLName.setToolTipText("asdasdasdas");
        txtLName.setPreferredSize(new java.awt.Dimension(60, 22));

        txtFName.setToolTipText("asdasdasdas");

        txtMName.setToolTipText("asdasdasdas");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("First name");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Middle name");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Employee ID");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Employee Type");

        btnCreateEmployee.setBackground(new java.awt.Color(0, 0, 0));
        btnCreateEmployee.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCreateEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnCreateEmployee.setText("SUBMIT");
        btnCreateEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateEmployeeActionPerformed(evt);
            }
        });

        btnUpdateEmployee1.setBackground(new java.awt.Color(0, 0, 0));
        btnUpdateEmployee1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdateEmployee1.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateEmployee1.setText("CANCEL");
        btnUpdateEmployee1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateEmployee1ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Employee Status");

        comboEmpStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACTIVE", "INACTIVE" }));

        javax.swing.GroupLayout addUsersPanelLayout = new javax.swing.GroupLayout(addUsersPanel);
        addUsersPanel.setLayout(addUsersPanelLayout);
        addUsersPanelLayout.setHorizontalGroup(
            addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addUsersPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(addUsersPanelLayout.createSequentialGroup()
                            .addGroup(addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtLName, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                            .addGap(18, 18, 18)
                            .addGroup(addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addComponent(txtFName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addUsersPanelLayout.createSequentialGroup()
                            .addComponent(txtEmpID, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addGroup(addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10)
                                .addComponent(comboEmpType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtMName, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(comboEmpStatus, javax.swing.GroupLayout.Alignment.LEADING, 0, 250, Short.MAX_VALUE)))
                .addContainerGap(454, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addUsersPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCreateEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUpdateEmployee1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        addUsersPanelLayout.setVerticalGroup(
            addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addUsersPanelLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtEmpID, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(comboEmpType)
                    .addComponent(comboEmpStatus))
                .addGap(30, 30, 30)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCreateEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdateEmployee1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jTabbedPane1.addTab("Create", addUsersPanel);

        updateUsersPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel13.setText("EMPLOYMENT INFORMATION");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel7.setText("General Information");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Last name");

        txtEmpID1.setToolTipText("asdasdasdas");
        txtEmpID1.setEnabled(false);

        comboEmpTypeUp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Registrar", "Teacher", "Staff" }));

        txtLName1.setToolTipText("asdasdasdas");
        txtLName1.setPreferredSize(new java.awt.Dimension(60, 22));

        txtFName1.setToolTipText("asdasdasdas");

        txtMName1.setToolTipText("asdasdasdas");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("First name");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Middle name");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Employee ID");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("Employee Type");

        btnUpdateEmployee.setBackground(new java.awt.Color(0, 0, 0));
        btnUpdateEmployee.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdateEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateEmployee.setText("UPDATE");
        btnUpdateEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateEmployeeActionPerformed(evt);
            }
        });

        btnUpdateEmployee3.setBackground(new java.awt.Color(0, 0, 0));
        btnUpdateEmployee3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdateEmployee3.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateEmployee3.setText("CANCEL");
        btnUpdateEmployee3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateEmployee3ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Employee Status");

        comboEmpStatusUp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACTIVE", "INACTIVE" }));

        btnResetPassword.setBackground(new java.awt.Color(0, 0, 0));
        btnResetPassword.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnResetPassword.setForeground(new java.awt.Color(255, 255, 255));
        btnResetPassword.setText("[RESET PASSWORD]");
        btnResetPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetPasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout updateUsersPanelLayout = new javax.swing.GroupLayout(updateUsersPanel);
        updateUsersPanel.setLayout(updateUsersPanelLayout);
        updateUsersPanelLayout.setHorizontalGroup(
            updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateUsersPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(updateUsersPanelLayout.createSequentialGroup()
                        .addComponent(btnResetPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUpdateEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdateEmployee3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))
                    .addGroup(updateUsersPanelLayout.createSequentialGroup()
                        .addGroup(updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(updateUsersPanelLayout.createSequentialGroup()
                                    .addGroup(updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel14)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtLName1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                                    .addGap(18, 18, 18)
                                    .addGroup(updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel15)
                                        .addComponent(txtFName1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, updateUsersPanelLayout.createSequentialGroup()
                                    .addComponent(txtEmpID1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addGroup(updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel18)
                                        .addComponent(comboEmpTypeUp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addComponent(jLabel17)
                            .addComponent(jLabel13))
                        .addGap(18, 18, 18)
                        .addGroup(updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(updateUsersPanelLayout.createSequentialGroup()
                                .addGroup(updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtMName1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comboEmpStatusUp, javax.swing.GroupLayout.Alignment.LEADING, 0, 250, Short.MAX_VALUE))
                                .addGap(0, 454, Short.MAX_VALUE))
                            .addGroup(updateUsersPanelLayout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addGap(186, 186, 186))))))
        );
        updateUsersPanelLayout.setVerticalGroup(
            updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateUsersPanelLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtEmpID1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(comboEmpTypeUp)
                    .addComponent(comboEmpStatusUp))
                .addGap(30, 30, 30)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLName1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFName1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMName1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(updateUsersPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(updateUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUpdateEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUpdateEmployee3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15))
                    .addGroup(updateUsersPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnResetPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(40, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("Update", updateUsersPanel);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        jLabel1.setText("EMPLOYEE");

        javax.swing.GroupLayout userPanelLayout = new javax.swing.GroupLayout(userPanel);
        userPanel.setLayout(userPanelLayout);
        userPanelLayout.setHorizontalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );
        userPanelLayout.setVerticalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        contentPanel.add(userPanel, "card3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(sidePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sidePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
            .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDashboard5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboard5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDashboard5ActionPerformed

    private void btnDashboard4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboard4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDashboard4ActionPerformed

    private void btnDashboard3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboard3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDashboard3ActionPerformed

    private void btnDashboard2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboard2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDashboard2ActionPerformed

    private void btnDashboard1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboard1ActionPerformed
        contentPanel.removeAll();
        contentPanel.add(dashPanel);
        contentPanel.repaint();
        contentPanel.revalidate();        // TODO add your handling code here:
    }//GEN-LAST:event_btnDashboard1ActionPerformed

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDashboardActionPerformed

    private void btnLogout1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogout1ActionPerformed
        // TODO add your handling code here:
        dashboardFrame frameOne = new dashboardFrame();
        loginFrame nextForm = new loginFrame();

        nextForm.setVisible(true);
        frameOne.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_btnLogout1ActionPerformed

    private void btnDashboard6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboard6ActionPerformed

        contentPanel.removeAll();
        contentPanel.add(userPanel);
        contentPanel.repaint();
        contentPanel.revalidate();
        setEmployeeTable();
    }//GEN-LAST:event_btnDashboard6ActionPerformed

    private void btnUpdateEmployee1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateEmployee1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateEmployee1ActionPerformed

    private void btnCreateEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateEmployeeActionPerformed
        addEmployeeAndUserToDB();        // TODO add your handling code here:
    }//GEN-LAST:event_btnCreateEmployeeActionPerformed

    private void btnUpdateEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateEmployeeActionPerformed
        // TODO add your handling code here:
        updateUser();
    }//GEN-LAST:event_btnUpdateEmployeeActionPerformed

    private void btnUpdateEmployee3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateEmployee3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateEmployee3ActionPerformed

    private void btnResetPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetPasswordActionPerformed
        // TODO add your handling code here:
        resetPassword();
    }//GEN-LAST:event_btnResetPasswordActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(dashboardFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dashboardFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dashboardFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dashboardFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dashboardFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addUsersPanel;
    private javax.swing.JLabel adminIcon;
    private javax.swing.JButton btnCreateEmployee;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnDashboard1;
    private javax.swing.JButton btnDashboard2;
    private javax.swing.JButton btnDashboard3;
    private javax.swing.JButton btnDashboard4;
    private javax.swing.JButton btnDashboard5;
    private javax.swing.JButton btnDashboard6;
    private javax.swing.JButton btnLogout1;
    private javax.swing.JButton btnResetPassword;
    private javax.swing.JButton btnUpdateEmployee;
    private javax.swing.JButton btnUpdateEmployee1;
    private javax.swing.JButton btnUpdateEmployee3;
    private javax.swing.JComboBox<String> comboEmpStatus;
    private javax.swing.JComboBox<String> comboEmpStatusUp;
    private javax.swing.JComboBox<String> comboEmpType;
    private javax.swing.JComboBox<String> comboEmpTypeUp;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel dashPanel;
    private javax.swing.JLabel dashboardIcon;
    private javax.swing.JLabel dataIcon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel logoutIcon;
    private javax.swing.JLabel maintenanceIcon;
    private javax.swing.JLabel recordsIcon;
    private javax.swing.JLabel settingsIcon1;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JTable tableEmp;
    private javax.swing.JLabel transactionIcon;
    private javax.swing.JTextField txtEmpID;
    private javax.swing.JTextField txtEmpID1;
    private javax.swing.JTextField txtFName;
    private javax.swing.JTextField txtFName1;
    private javax.swing.JTextField txtLName;
    private javax.swing.JTextField txtLName1;
    private javax.swing.JTextField txtMName;
    private javax.swing.JTextField txtMName1;
    private javax.swing.JPanel updateUsersPanel;
    private javax.swing.JPanel userPanel;
    private javax.swing.JLabel usersIcon;
    // End of variables declaration//GEN-END:variables
}
