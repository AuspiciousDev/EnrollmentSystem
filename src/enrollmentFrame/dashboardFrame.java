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
    String sqlGetUser = "SELECT EmpID, FName,MName,LName,EmpType FROM EMPLOYEE WHERE EmpID = ?";

    String sqlEmpAdd = "INSERT INTO EMPLOYEE(EmpID,FName,MName,LName,EmpType,EmpStatus) VALUES (?,?,?,?,?,?)";
    String sqlUserAdd = "INSERT INTO USERS(Username,Password) VALUES (?,?)";

    String sqlGetEmp = "SELECT USERS.Username, EMPLOYEE.FName,EMPLOYEE.MName,EMPLOYEE.LName,EMPLOYEE.EmpType,EMPLOYEE.EmpStatus\n"
            + "FROM USERS LEFT JOIN EMPLOYEE ON EMPLOYEE.EmpID = ?  ";
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
        String dbEmpType = "";
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
                dbEmpType = rst.getString("EmpType").toUpperCase();
            }
            dbEmpFULLName = dbEmpFName + " " + dbEmpMName + " " + dbEmpLName;
            aLblCurrUser.setText(dbEmpFULLName);
            aLblCurrType.setText(dbEmpType);
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

    static String generateEmpID(int n) {

        // chose a Character random from this String
        String AlphaNumericString
                = "0123456789"; // create StringBuffer size of AlphaNumericString
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

    private void setUserProfile() {

        try {
            con = DriverManager.getConnection(SQL_URL);
            pst = con.prepareStatement(sqlGetEmp);
            pst.setString(1, CurrUserID); // 
            rst = pst.executeQuery();
            while (rst.next()) {
                String empID = rst.getString("Username");
                if (empID.equals(CurrUserID)) {
                    System.out.println("SOUT " + empID);
                    String FName = rst.getString("FName");
                    String MName = rst.getString("MName");
                    String LName = rst.getString("LName");

                    String empType = rst.getString("EmpType");
                    String empStatus = rst.getString("EmpStatus");

                    txtEmpIDsett.setText(empID);
                    txtFNamesett.setText(FName);
                    txtLNamesett.setText(LName);
                    txtMNamesett.setText(MName);
                    txtEmpTypesett.setText(empType);
                    txtEmpStatussett.setText(empStatus);
                }
            }
            con.close();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, "DASH : Connection Error " + e, "Dash Board Notice!", JOptionPane.ERROR_MESSAGE);
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
        aLblCurrUser = new javax.swing.JLabel();
        aLblCurrType = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        btnTransaction = new javax.swing.JButton();
        btnDashboard = new javax.swing.JButton();
        btnDataEntry = new javax.swing.JButton();
        btnRecords = new javax.swing.JButton();
        btnMaintenance = new javax.swing.JButton();
        btnSettings = new javax.swing.JButton();
        btnDashboard6 = new javax.swing.JButton();
        settingsIcon1 = new javax.swing.JLabel();
        settingsIcon2 = new javax.swing.JLabel();
        btnUserSettings = new javax.swing.JButton();
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
        btnEmpCreateSubmit = new javax.swing.JButton();
        btnEmpCreateCancel = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        comboEmpStatus = new javax.swing.JComboBox<>();
        lblEmpCreateGenerateEmpID = new javax.swing.JLabel();
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
        btnEmpUpdateSubmit = new javax.swing.JButton();
        btnEmpUpdateCancel = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        comboEmpStatusUp = new javax.swing.JComboBox<>();
        btnEmpUpdateResetPassword = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        transPanel = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        dataPanel = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        userSettings = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtEmpIDsett = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtLNamesett = new javax.swing.JTextField();
        txtFNamesett = new javax.swing.JTextField();
        txtMNamesett = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtCurrPasswordsett = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtNewPasswordsett = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtConfPasswordsett = new javax.swing.JTextField();
        btnEmpUpdateResetPasswordsett = new javax.swing.JButton();
        txtEmpStatussett = new javax.swing.JTextField();
        txtEmpTypesett = new javax.swing.JTextField();
        recordsPanel = new javax.swing.JPanel();
        recordsPane = new javax.swing.JTabbedPane();
        studentPanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        subjectPanel = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        maintenancePanel = new javax.swing.JPanel();
        maintenancePane = new javax.swing.JTabbedPane();
        panelSY = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panelDeparment = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        panelLevels = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        panelSections = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();

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

        aLblCurrUser.setBackground(new java.awt.Color(255, 255, 255));
        aLblCurrUser.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        aLblCurrUser.setForeground(new java.awt.Color(255, 255, 255));
        aLblCurrUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        aLblCurrUser.setText("Lorem Ipsum Ripsum");

        aLblCurrType.setBackground(new java.awt.Color(255, 255, 153));
        aLblCurrType.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        aLblCurrType.setForeground(new java.awt.Color(255, 255, 153));
        aLblCurrType.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        aLblCurrType.setText("LOREM");

        btnLogout.setBackground(new java.awt.Color(51, 0, 0));
        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("LOGOUT");
        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.setMaximumSize(new java.awt.Dimension(83, 25));
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        btnTransaction.setBackground(new java.awt.Color(51, 51, 51));
        btnTransaction.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTransaction.setForeground(new java.awt.Color(255, 255, 255));
        btnTransaction.setText("TRANSACTION");
        btnTransaction.setMaximumSize(new java.awt.Dimension(113, 25));
        btnTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransactionActionPerformed(evt);
            }
        });

        btnDashboard.setBackground(new java.awt.Color(51, 51, 51));
        btnDashboard.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDashboard.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard.setText("DASH BOARD");
        btnDashboard.setMaximumSize(new java.awt.Dimension(113, 25));
        btnDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboardActionPerformed(evt);
            }
        });

        btnDataEntry.setBackground(new java.awt.Color(51, 51, 51));
        btnDataEntry.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDataEntry.setForeground(new java.awt.Color(255, 255, 255));
        btnDataEntry.setText("DATA ENTRY");
        btnDataEntry.setMaximumSize(new java.awt.Dimension(113, 25));
        btnDataEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntryActionPerformed(evt);
            }
        });

        btnRecords.setBackground(new java.awt.Color(51, 51, 51));
        btnRecords.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRecords.setForeground(new java.awt.Color(255, 255, 255));
        btnRecords.setText("RECORDS");
        btnRecords.setMaximumSize(new java.awt.Dimension(113, 25));
        btnRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecordsActionPerformed(evt);
            }
        });

        btnMaintenance.setBackground(new java.awt.Color(51, 51, 51));
        btnMaintenance.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnMaintenance.setForeground(new java.awt.Color(255, 255, 255));
        btnMaintenance.setText("MAINTENANCE");
        btnMaintenance.setMaximumSize(new java.awt.Dimension(113, 25));
        btnMaintenance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMaintenanceActionPerformed(evt);
            }
        });

        btnSettings.setBackground(new java.awt.Color(51, 51, 51));
        btnSettings.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSettings.setForeground(new java.awt.Color(255, 255, 255));
        btnSettings.setText("SETTINGS");
        btnSettings.setMaximumSize(new java.awt.Dimension(113, 25));
        btnSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSettingsActionPerformed(evt);
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

        settingsIcon2.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        settingsIcon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/wperson1Icon.png"))); // NOI18N
        settingsIcon2.setPreferredSize(new java.awt.Dimension(22, 22));

        btnUserSettings.setBackground(new java.awt.Color(51, 51, 51));
        btnUserSettings.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUserSettings.setForeground(new java.awt.Color(255, 255, 255));
        btnUserSettings.setText("USER SETTINGS");
        btnUserSettings.setMaximumSize(new java.awt.Dimension(113, 25));
        btnUserSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserSettingsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sidePanelLayout = new javax.swing.GroupLayout(sidePanel);
        sidePanel.setLayout(sidePanelLayout);
        sidePanelLayout.setHorizontalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sidePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(aLblCurrUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanelLayout.createSequentialGroup()
                                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(sidePanelLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(sidePanelLayout.createSequentialGroup()
                                                        .addComponent(logoutIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(sidePanelLayout.createSequentialGroup()
                                                        .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(recordsIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(dataIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(transactionIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(dashboardIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(btnRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(btnDataEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addComponent(btnTransaction, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(btnDashboard, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, sidePanelLayout.createSequentialGroup()
                                                    .addComponent(usersIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(btnDashboard6, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(sidePanelLayout.createSequentialGroup()
                                                    .addComponent(maintenanceIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(btnMaintenance, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(sidePanelLayout.createSequentialGroup()
                                                    .addComponent(settingsIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(btnSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGroup(sidePanelLayout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addComponent(settingsIcon2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnUserSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(35, 35, 35))))
                    .addGroup(sidePanelLayout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(adminIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(sidePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(aLblCurrType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        sidePanelLayout.setVerticalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adminIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aLblCurrUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aLblCurrType)
                .addGap(30, 30, 30)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dashboardIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transactionIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDataEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dataIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recordsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDashboard6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usersIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnUserSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsIcon2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnMaintenance, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maintenanceIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logoutIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );

        contentPanel.setLayout(new java.awt.CardLayout());

        dashPanel.setBackground(new java.awt.Color(153, 255, 255));

        javax.swing.GroupLayout dashPanelLayout = new javax.swing.GroupLayout(dashPanel);
        dashPanel.setLayout(dashPanelLayout);
        dashPanelLayout.setHorizontalGroup(
            dashPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1343, Short.MAX_VALUE)
        );
        dashPanelLayout.setVerticalGroup(
            dashPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 829, Short.MAX_VALUE)
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
        txtEmpID.setEnabled(false);

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

        btnEmpCreateSubmit.setBackground(new java.awt.Color(0, 0, 0));
        btnEmpCreateSubmit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEmpCreateSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btnEmpCreateSubmit.setText("SUBMIT");
        btnEmpCreateSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpCreateSubmitActionPerformed(evt);
            }
        });

        btnEmpCreateCancel.setBackground(new java.awt.Color(0, 0, 0));
        btnEmpCreateCancel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEmpCreateCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnEmpCreateCancel.setText("CANCEL");
        btnEmpCreateCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpCreateCancelActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Employee Status");

        comboEmpStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACTIVE", "INACTIVE" }));

        lblEmpCreateGenerateEmpID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblEmpCreateGenerateEmpID.setText("[Generate Employee ID]");
        lblEmpCreateGenerateEmpID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEmpCreateGenerateEmpIDMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout addUsersPanelLayout = new javax.swing.GroupLayout(addUsersPanel);
        addUsersPanel.setLayout(addUsersPanelLayout);
        addUsersPanelLayout.setHorizontalGroup(
            addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addUsersPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEmpCreateSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEmpCreateCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
            .addGroup(addUsersPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(addUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEmpCreateGenerateEmpID)
                    .addGroup(addUsersPanelLayout.createSequentialGroup()
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
                                .addComponent(comboEmpStatus, javax.swing.GroupLayout.Alignment.LEADING, 0, 250, Short.MAX_VALUE)))))
                .addContainerGap(525, Short.MAX_VALUE))
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
                .addGap(4, 4, 4)
                .addComponent(lblEmpCreateGenerateEmpID)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                    .addComponent(btnEmpCreateSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEmpCreateCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        btnEmpUpdateSubmit.setBackground(new java.awt.Color(0, 0, 0));
        btnEmpUpdateSubmit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEmpUpdateSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btnEmpUpdateSubmit.setText("UPDATE");
        btnEmpUpdateSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpUpdateSubmitActionPerformed(evt);
            }
        });

        btnEmpUpdateCancel.setBackground(new java.awt.Color(0, 0, 0));
        btnEmpUpdateCancel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEmpUpdateCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnEmpUpdateCancel.setText("CANCEL");
        btnEmpUpdateCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpUpdateCancelActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Employee Status");

        comboEmpStatusUp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACTIVE", "INACTIVE" }));

        btnEmpUpdateResetPassword.setBackground(new java.awt.Color(0, 0, 0));
        btnEmpUpdateResetPassword.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEmpUpdateResetPassword.setForeground(new java.awt.Color(255, 255, 255));
        btnEmpUpdateResetPassword.setText("[RESET PASSWORD]");
        btnEmpUpdateResetPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpUpdateResetPasswordActionPerformed(evt);
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
                        .addComponent(btnEmpUpdateResetPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEmpUpdateSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEmpUpdateCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                .addGap(0, 525, Short.MAX_VALUE))
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
                            .addComponent(btnEmpUpdateSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEmpUpdateCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15))
                    .addGroup(updateUsersPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnEmpUpdateResetPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        contentPanel.add(userPanel, "card3");

        transPanel.setBackground(new java.awt.Color(153, 255, 255));

        javax.swing.GroupLayout transPanelLayout = new javax.swing.GroupLayout(transPanel);
        transPanel.setLayout(transPanelLayout);
        transPanelLayout.setHorizontalGroup(
            transPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1343, Short.MAX_VALUE)
            .addGroup(transPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(transPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        transPanelLayout.setVerticalGroup(
            transPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 829, Short.MAX_VALUE)
            .addGroup(transPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(transPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 795, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        contentPanel.add(transPanel, "card2");

        dataPanel.setBackground(new java.awt.Color(153, 255, 255));

        javax.swing.GroupLayout dataPanelLayout = new javax.swing.GroupLayout(dataPanel);
        dataPanel.setLayout(dataPanelLayout);
        dataPanelLayout.setHorizontalGroup(
            dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1343, Short.MAX_VALUE)
        );
        dataPanelLayout.setVerticalGroup(
            dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 829, Short.MAX_VALUE)
        );

        contentPanel.add(dataPanel, "card2");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        jLabel2.setText("USER SETTINGS");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel20.setText("Security");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("Employee ID");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setText("Employee Type");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Employee Status");

        txtEmpIDsett.setToolTipText("asdasdasdas");
        txtEmpIDsett.setEnabled(false);

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setText("Last name");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("First name");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel26.setText("Middle name");

        txtLNamesett.setToolTipText("asdasdasdas");
        txtLNamesett.setPreferredSize(new java.awt.Dimension(60, 22));

        txtFNamesett.setToolTipText("asdasdasdas");

        txtMNamesett.setToolTipText("asdasdasdas");
        txtMNamesett.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMNamesettActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel27.setText("Current Password");

        txtCurrPasswordsett.setToolTipText("asdasdasdas");
        txtCurrPasswordsett.setPreferredSize(new java.awt.Dimension(60, 22));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel28.setText("New Password");

        txtNewPasswordsett.setToolTipText("asdasdasdas");
        txtNewPasswordsett.setPreferredSize(new java.awt.Dimension(60, 22));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel29.setText("Confirm Password");

        txtConfPasswordsett.setToolTipText("asdasdasdas");
        txtConfPasswordsett.setPreferredSize(new java.awt.Dimension(60, 22));

        btnEmpUpdateResetPasswordsett.setBackground(new java.awt.Color(0, 0, 0));
        btnEmpUpdateResetPasswordsett.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEmpUpdateResetPasswordsett.setForeground(new java.awt.Color(255, 255, 255));
        btnEmpUpdateResetPasswordsett.setText("RESET PASSWORD");
        btnEmpUpdateResetPasswordsett.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpUpdateResetPasswordsettActionPerformed(evt);
            }
        });

        txtEmpStatussett.setToolTipText("asdasdasdas");
        txtEmpStatussett.setEnabled(false);

        txtEmpTypesett.setToolTipText("asdasdasdas");
        txtEmpTypesett.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(txtLNamesett, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(txtFNamesett, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(txtMNamesett, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel20)
                    .addComponent(jLabel2)
                    .addComponent(jLabel21)
                    .addComponent(jLabel27)
                    .addComponent(txtCurrPasswordsett, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addComponent(txtNewPasswordsett, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(txtConfPasswordsett, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnEmpUpdateResetPasswordsett, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtEmpIDsett, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmpTypesett, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(txtEmpStatussett, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(551, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmpIDsett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmpStatussett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmpTypesett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLNamesett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFNamesett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMNamesett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCurrPasswordsett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNewPasswordsett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtConfPasswordsett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(btnEmpUpdateResetPasswordsett, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(254, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout userSettingsLayout = new javax.swing.GroupLayout(userSettings);
        userSettings.setLayout(userSettingsLayout);
        userSettingsLayout.setHorizontalGroup(
            userSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userSettingsLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        userSettingsLayout.setVerticalGroup(
            userSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 70, Short.MAX_VALUE))
        );

        contentPanel.add(userSettings, "card2");

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STUDENT ID", "NAME", "LEVEL", "SECTION", "DEPARTMENT"
            }
        ));
        jScrollPane6.setViewportView(jTable5);

        javax.swing.GroupLayout studentPanelLayout = new javax.swing.GroupLayout(studentPanel);
        studentPanel.setLayout(studentPanelLayout);
        studentPanelLayout.setHorizontalGroup(
            studentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1331, Short.MAX_VALUE)
        );
        studentPanelLayout.setVerticalGroup(
            studentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 359, Short.MAX_VALUE))
        );

        recordsPane.addTab("Students", studentPanel);

        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SUBJECT ID", "NAME", "LEVEL", "ACTIVE", "ACTIONS"
            }
        ));
        jScrollPane9.setViewportView(jTable8);

        javax.swing.GroupLayout subjectPanelLayout = new javax.swing.GroupLayout(subjectPanel);
        subjectPanel.setLayout(subjectPanelLayout);
        subjectPanelLayout.setHorizontalGroup(
            subjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 1331, Short.MAX_VALUE)
        );
        subjectPanelLayout.setVerticalGroup(
            subjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 359, Short.MAX_VALUE))
        );

        recordsPane.addTab("Subjects", subjectPanel);

        javax.swing.GroupLayout recordsPanelLayout = new javax.swing.GroupLayout(recordsPanel);
        recordsPanel.setLayout(recordsPanelLayout);
        recordsPanelLayout.setHorizontalGroup(
            recordsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recordsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(recordsPane)
                .addContainerGap())
        );
        recordsPanelLayout.setVerticalGroup(
            recordsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recordsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(recordsPane)
                .addContainerGap())
        );

        contentPanel.add(recordsPanel, "card8");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "School Year ID", "TITLE", "DESCRIPTION", "ACTIVE", "ACTIONS"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout panelSYLayout = new javax.swing.GroupLayout(panelSY);
        panelSY.setLayout(panelSYLayout);
        panelSYLayout.setHorizontalGroup(
            panelSYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1343, Short.MAX_VALUE)
        );
        panelSYLayout.setVerticalGroup(
            panelSYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSYLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 371, Short.MAX_VALUE))
        );

        maintenancePane.addTab("School Year", panelSY);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DEPARTMENT ID", "NAME", "DESCRIPTION", "ACTIVE", "ACTIONS"
            }
        ));
        jScrollPane3.setViewportView(jTable2);

        javax.swing.GroupLayout panelDeparmentLayout = new javax.swing.GroupLayout(panelDeparment);
        panelDeparment.setLayout(panelDeparmentLayout);
        panelDeparmentLayout.setHorizontalGroup(
            panelDeparmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1343, Short.MAX_VALUE)
        );
        panelDeparmentLayout.setVerticalGroup(
            panelDeparmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDeparmentLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 371, Short.MAX_VALUE))
        );

        maintenancePane.addTab("Depatment", panelDeparment);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "LEVEL ID", "LEVEL", "DEPARTMENT", "ACTIVE", "ACTIONS"
            }
        ));
        jScrollPane4.setViewportView(jTable3);

        javax.swing.GroupLayout panelLevelsLayout = new javax.swing.GroupLayout(panelLevels);
        panelLevels.setLayout(panelLevelsLayout);
        panelLevelsLayout.setHorizontalGroup(
            panelLevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1343, Short.MAX_VALUE)
        );
        panelLevelsLayout.setVerticalGroup(
            panelLevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLevelsLayout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 371, Short.MAX_VALUE))
        );

        maintenancePane.addTab("Levels", panelLevels);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SECTION ID", "NAME", "LEVEL", "ACTIVE", "ACTIONS"
            }
        ));
        jScrollPane5.setViewportView(jTable4);

        javax.swing.GroupLayout panelSectionsLayout = new javax.swing.GroupLayout(panelSections);
        panelSections.setLayout(panelSectionsLayout);
        panelSectionsLayout.setHorizontalGroup(
            panelSectionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1343, Short.MAX_VALUE)
        );
        panelSectionsLayout.setVerticalGroup(
            panelSectionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSectionsLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 371, Short.MAX_VALUE))
        );

        maintenancePane.addTab("Sections", panelSections);

        javax.swing.GroupLayout maintenancePanelLayout = new javax.swing.GroupLayout(maintenancePanel);
        maintenancePanel.setLayout(maintenancePanelLayout);
        maintenancePanelLayout.setHorizontalGroup(
            maintenancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(maintenancePane)
        );
        maintenancePanelLayout.setVerticalGroup(
            maintenancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(maintenancePane)
        );

        contentPanel.add(maintenancePanel, "card7");

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
            .addComponent(sidePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 829, Short.MAX_VALUE)
            .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettingsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSettingsActionPerformed

    private void btnMaintenanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMaintenanceActionPerformed
        // TODO add your handling code here:
        contentPanel.removeAll();
        contentPanel.add(maintenancePanel);
        contentPanel.repaint();
        contentPanel.revalidate();
    }//GEN-LAST:event_btnMaintenanceActionPerformed

    private void btnRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecordsActionPerformed
        // TODO add your handling code here: 
        contentPanel.removeAll();
        contentPanel.add(recordsPanel);
        contentPanel.repaint();
        contentPanel.revalidate();
    }//GEN-LAST:event_btnRecordsActionPerformed

    private void btnDataEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDataEntryActionPerformed

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
        contentPanel.removeAll();
        contentPanel.add(dashPanel);
        contentPanel.repaint();
        contentPanel.revalidate();        // TODO add your handling code here:
    }//GEN-LAST:event_btnDashboardActionPerformed

    private void btnTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransactionActionPerformed
        contentPanel.removeAll();
        contentPanel.add(transPanel);
        contentPanel.repaint();
        contentPanel.revalidate();
    }//GEN-LAST:event_btnTransactionActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        dashboardFrame frameOne = new dashboardFrame();
        loginFrame nextForm = new loginFrame();

        nextForm.setVisible(true);
        frameOne.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnDashboard6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboard6ActionPerformed

        contentPanel.removeAll();
        contentPanel.add(userPanel);
        contentPanel.repaint();
        contentPanel.revalidate();
        setEmployeeTable();
    }//GEN-LAST:event_btnDashboard6ActionPerformed

    private void btnEmpCreateCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpCreateCancelActionPerformed
        // TODO add your handling code here:
        String EmpID = txtEmpID.getText();
        String FName = txtFName.getText();
        String MName = txtMName.getText();
        String LName = txtLName.getText();
        if (EmpID.isEmpty() && FName.isEmpty() && MName.isEmpty() && LName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields are empty!", "Dash Board Notice!", JOptionPane.ERROR_MESSAGE);
        } else {
            txtEmpID.setText("");
            txtFName.setText("");
            txtMName.setText("");
            txtLName.setText("");
            comboEmpType.setSelectedIndex(0);
            comboEmpStatus.setSelectedIndex(0);
            JOptionPane.showMessageDialog(this, "Fields are cleared!", "Dash Board Notice!", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnEmpCreateCancelActionPerformed

    private void btnEmpCreateSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpCreateSubmitActionPerformed
        addEmployeeAndUserToDB();        // TODO add your handling code here:
    }//GEN-LAST:event_btnEmpCreateSubmitActionPerformed

    private void btnEmpUpdateSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpUpdateSubmitActionPerformed
        // TODO add your handling code here:
        updateUser();
    }//GEN-LAST:event_btnEmpUpdateSubmitActionPerformed

    private void btnEmpUpdateCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpUpdateCancelActionPerformed
        // TODO add your handling code here:

        String EmpID = txtEmpID1.getText();
        String FName = txtFName1.getText();
        String MName = txtMName1.getText();
        String LName = txtLName1.getText();
        if (EmpID.isEmpty() && FName.isEmpty() && MName.isEmpty() && LName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields are empty!", "Dash Board Notice!", JOptionPane.ERROR_MESSAGE);
        } else {
            txtEmpID1.setText("");
            txtFName1.setText("");
            txtMName1.setText("");
            txtLName1.setText("");
            comboEmpTypeUp.setSelectedIndex(0);
            comboEmpStatusUp.setSelectedIndex(0);
            JOptionPane.showMessageDialog(this, "Fields are cleared!", "Dash Board Notice!", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnEmpUpdateCancelActionPerformed

    private void btnEmpUpdateResetPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpUpdateResetPasswordActionPerformed
        // TODO add your handling code here:
        resetPassword();
    }//GEN-LAST:event_btnEmpUpdateResetPasswordActionPerformed

    private void lblEmpCreateGenerateEmpIDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEmpCreateGenerateEmpIDMouseClicked
        // TODO add your handling code here:
        txtEmpID.setText(generateEmpID(10));
    }//GEN-LAST:event_lblEmpCreateGenerateEmpIDMouseClicked

    private void btnUserSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserSettingsActionPerformed
        // TODO add your handling code here:
        contentPanel.removeAll();
        contentPanel.add(userSettings);
        contentPanel.repaint();
        contentPanel.revalidate();
        setUserProfile();
    }//GEN-LAST:event_btnUserSettingsActionPerformed

    private void txtMNamesettActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMNamesettActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMNamesettActionPerformed

    private void btnEmpUpdateResetPasswordsettActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpUpdateResetPasswordsettActionPerformed
        // TODO add your handling code here:
        resetPassword();
    }//GEN-LAST:event_btnEmpUpdateResetPasswordsettActionPerformed

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
    private javax.swing.JLabel aLblCurrType;
    private javax.swing.JLabel aLblCurrUser;
    private javax.swing.JPanel addUsersPanel;
    private javax.swing.JLabel adminIcon;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnDashboard6;
    private javax.swing.JButton btnDataEntry;
    private javax.swing.JButton btnEmpCreateCancel;
    private javax.swing.JButton btnEmpCreateSubmit;
    private javax.swing.JButton btnEmpUpdateCancel;
    private javax.swing.JButton btnEmpUpdateResetPassword;
    private javax.swing.JButton btnEmpUpdateResetPasswordsett;
    private javax.swing.JButton btnEmpUpdateSubmit;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMaintenance;
    private javax.swing.JButton btnRecords;
    private javax.swing.JButton btnSettings;
    private javax.swing.JButton btnTransaction;
    private javax.swing.JButton btnUserSettings;
    private javax.swing.JComboBox<String> comboEmpStatus;
    private javax.swing.JComboBox<String> comboEmpStatusUp;
    private javax.swing.JComboBox<String> comboEmpType;
    private javax.swing.JComboBox<String> comboEmpTypeUp;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel dashPanel;
    private javax.swing.JLabel dashboardIcon;
    private javax.swing.JLabel dataIcon;
    private javax.swing.JPanel dataPanel;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable8;
    private javax.swing.JLabel lblEmpCreateGenerateEmpID;
    private javax.swing.JLabel logoutIcon;
    private javax.swing.JLabel maintenanceIcon;
    private javax.swing.JTabbedPane maintenancePane;
    private javax.swing.JPanel maintenancePanel;
    private javax.swing.JPanel panelDeparment;
    private javax.swing.JPanel panelLevels;
    private javax.swing.JPanel panelSY;
    private javax.swing.JPanel panelSections;
    private javax.swing.JLabel recordsIcon;
    private javax.swing.JTabbedPane recordsPane;
    private javax.swing.JPanel recordsPanel;
    private javax.swing.JLabel settingsIcon1;
    private javax.swing.JLabel settingsIcon2;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JPanel studentPanel;
    private javax.swing.JPanel subjectPanel;
    private javax.swing.JTable tableEmp;
    private javax.swing.JPanel transPanel;
    private javax.swing.JLabel transactionIcon;
    private javax.swing.JTextField txtConfPasswordsett;
    private javax.swing.JTextField txtCurrPasswordsett;
    private javax.swing.JTextField txtEmpID;
    private javax.swing.JTextField txtEmpID1;
    private javax.swing.JTextField txtEmpIDsett;
    private javax.swing.JTextField txtEmpStatussett;
    private javax.swing.JTextField txtEmpTypesett;
    private javax.swing.JTextField txtFName;
    private javax.swing.JTextField txtFName1;
    private javax.swing.JTextField txtFNamesett;
    private javax.swing.JTextField txtLName;
    private javax.swing.JTextField txtLName1;
    private javax.swing.JTextField txtLNamesett;
    private javax.swing.JTextField txtMName;
    private javax.swing.JTextField txtMName1;
    private javax.swing.JTextField txtMNamesett;
    private javax.swing.JTextField txtNewPasswordsett;
    private javax.swing.JPanel updateUsersPanel;
    private javax.swing.JPanel userPanel;
    private javax.swing.JPanel userSettings;
    private javax.swing.JLabel usersIcon;
    // End of variables declaration//GEN-END:variables
}
