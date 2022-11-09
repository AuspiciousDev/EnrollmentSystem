/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package enrollmentFrame;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author King
 */
public class staffForm extends javax.swing.JFrame {

    String CurrUserID;
    String dbEmpFULLName;

    Connection con = null;
    ResultSet rst = null;
    PreparedStatement pst = null;
    Statement stmt = null;
    PreparedStatement pstUpdate = null;

    String SQL_URL = "jdbc:sqlserver://localhost:1433;user=admin;password=admin123;DatabaseName=ADPEnrollmentSystem;integrated‌​Security=true";
    String sqlGetUser = "SELECT EmpID, FName,MName,LName,EmpType FROM EMPLOYEE WHERE EmpID = ?";

    String sqlGetEmp = "SELECT USERS.Username, EMPLOYEE.FName,EMPLOYEE.MName,EMPLOYEE.LName,EMPLOYEE.EmpType,EMPLOYEE.EmpStatus\n"
            + "FROM USERS LEFT JOIN EMPLOYEE ON EMPLOYEE.EmpID = ?  ";
    String sqlGetPassword = "SELECT Password FROM USERS WHERE Username =?";
    String sqlPatchPassword = "UPDATE USERS SET Password = ? WHERE Username =?";

    /**
     * Creates new form staffForm
     */
    public staffForm() {
        initComponents();
    }

    public staffForm(String empID) {
        initComponents();
        CurrUserID = empID;
        getCurrentUserDB();

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
            sLblCurrUser.setText(dbEmpFULLName);
            tLblCurrType.setText(dbEmpType);
            con.close();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, "Nav : Connection Error" + e, "Database Notice", JOptionPane.ERROR_MESSAGE);
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
                    String FName = rst.getString("FName");
                    String MName = rst.getString("MName");
                    String LName = rst.getString("LName");

                    String empType = rst.getString("EmpType");
                    String empStatus = rst.getString("EmpStatus");

                    txtEmpID.setText(empID);
                    txtFName.setText(FName);
                    txtLName.setText(LName);
                    txtMName.setText(MName);
                    txtEmpType.setText(empType);
                    txtEmpStatus.setText(empStatus);
                }
            }
            con.close();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, "PRODUCT : Connection Error" + e, "Dash Board Notice!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetPassword() {
        String currPass = txtCurrPassword.getText();
        String newPass = txtNewPassword.getText();
        String confPass = txtLName4.getText();
        if (newPass.equals(confPass)) {
            try {
                con = DriverManager.getConnection(SQL_URL);
                pst = con.prepareStatement(sqlGetPassword);
                pst.setString(1, txtEmpID.getText()); // 
                rst = pst.executeQuery();
                while (rst.next()) {
                    String password = rst.getString("Password");
                    if (currPass.equals(password)) {
                        con = null;
                        pst = null;
                        con = DriverManager.getConnection(SQL_URL);
                        pst = con.prepareStatement(sqlPatchPassword);
                        pst.setString(1, newPass);
                        pst.setString(2, txtEmpID.getText());
                        int x = pst.executeUpdate();
                        if (x == 1) {
                            JOptionPane.showMessageDialog(this, "Password Reset Success!", "Database Notice!", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Password Reset Failed!", "Database Notice!", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Password doesn't matched!", "Notice!", JOptionPane.ERROR_MESSAGE);
                    }
                }
                con.close();
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(this, "PRODUCT : Connection Error" + e, "Dash Board Notice!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "New Password and Confirm Password doesn't matched!", "Notice!", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
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
        recordsIcon = new javax.swing.JLabel();
        logoutIcon = new javax.swing.JLabel();
        sLblCurrUser = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        btnTransaction = new javax.swing.JButton();
        btnDashboard = new javax.swing.JButton();
        btnDataEntry = new javax.swing.JButton();
        btnRecords = new javax.swing.JButton();
        btnSettings = new javax.swing.JButton();
        settingsIcon1 = new javax.swing.JLabel();
        tLblCurrType = new javax.swing.JLabel();
        contentPanel = new javax.swing.JPanel();
        dashboardPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        userPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtEmpID = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtLName = new javax.swing.JTextField();
        txtFName = new javax.swing.JTextField();
        txtMName = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtCurrPassword = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtNewPassword = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtLName4 = new javax.swing.JTextField();
        btnEmpUpdateResetPassword = new javax.swing.JButton();
        txtEmpStatus = new javax.swing.JTextField();
        txtEmpType = new javax.swing.JTextField();

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

        recordsIcon.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        recordsIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/wrecordsIcon.png"))); // NOI18N
        recordsIcon.setPreferredSize(new java.awt.Dimension(22, 22));

        logoutIcon.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        logoutIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/logoutIcon.png"))); // NOI18N
        logoutIcon.setPreferredSize(new java.awt.Dimension(36, 36));

        sLblCurrUser.setBackground(new java.awt.Color(255, 255, 255));
        sLblCurrUser.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        sLblCurrUser.setForeground(new java.awt.Color(255, 255, 255));
        sLblCurrUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sLblCurrUser.setText("Lorem Ipsum Ripsum");

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

        btnSettings.setBackground(new java.awt.Color(51, 51, 51));
        btnSettings.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSettings.setForeground(new java.awt.Color(255, 255, 255));
        btnSettings.setText("USER SETTINGS");
        btnSettings.setMaximumSize(new java.awt.Dimension(113, 25));
        btnSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSettingsActionPerformed(evt);
            }
        });

        settingsIcon1.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        settingsIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/wperson1Icon.png"))); // NOI18N
        settingsIcon1.setPreferredSize(new java.awt.Dimension(22, 22));

        tLblCurrType.setBackground(new java.awt.Color(255, 255, 153));
        tLblCurrType.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        tLblCurrType.setForeground(new java.awt.Color(255, 255, 153));
        tLblCurrType.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tLblCurrType.setText("LOREM");

        javax.swing.GroupLayout sidePanelLayout = new javax.swing.GroupLayout(sidePanel);
        sidePanel.setLayout(sidePanelLayout);
        sidePanelLayout.setHorizontalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
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
                            .addComponent(btnDashboard, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanelLayout.createSequentialGroup()
                        .addComponent(settingsIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(41, 41, 41))
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(adminIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sLblCurrUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(sidePanelLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tLblCurrType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(sLblCurrUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tLblCurrType)
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
                    .addComponent(btnSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logoutIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );

        contentPanel.setLayout(new java.awt.CardLayout());

        jLabel2.setText("Dashboard");

        javax.swing.GroupLayout dashboardPanelLayout = new javax.swing.GroupLayout(dashboardPanel);
        dashboardPanel.setLayout(dashboardPanelLayout);
        dashboardPanelLayout.setHorizontalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addContainerGap(1277, Short.MAX_VALUE))
        );
        dashboardPanelLayout.setVerticalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel2)
                .addContainerGap(782, Short.MAX_VALUE))
        );

        contentPanel.add(dashboardPanel, "card3");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        jLabel1.setText("USER SETTINGS");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel13.setText("Security");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Employee ID");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Employee Type");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Employee Status");

        txtEmpID.setToolTipText("asdasdasdas");
        txtEmpID.setEnabled(false);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Last name");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("First name");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Middle name");

        txtLName.setToolTipText("asdasdasdas");
        txtLName.setPreferredSize(new java.awt.Dimension(60, 22));

        txtFName.setToolTipText("asdasdasdas");

        txtMName.setToolTipText("asdasdasdas");
        txtMName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMNameActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Current Password");

        txtCurrPassword.setToolTipText("asdasdasdas");
        txtCurrPassword.setPreferredSize(new java.awt.Dimension(60, 22));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("New Password");

        txtNewPassword.setToolTipText("asdasdasdas");
        txtNewPassword.setPreferredSize(new java.awt.Dimension(60, 22));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Confirm Password");

        txtLName4.setToolTipText("asdasdasdas");
        txtLName4.setPreferredSize(new java.awt.Dimension(60, 22));

        btnEmpUpdateResetPassword.setBackground(new java.awt.Color(0, 0, 0));
        btnEmpUpdateResetPassword.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEmpUpdateResetPassword.setForeground(new java.awt.Color(255, 255, 255));
        btnEmpUpdateResetPassword.setText("RESET PASSWORD");
        btnEmpUpdateResetPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpUpdateResetPasswordActionPerformed(evt);
            }
        });

        txtEmpStatus.setToolTipText("asdasdasdas");
        txtEmpStatus.setEnabled(false);

        txtEmpType.setToolTipText("asdasdasdas");
        txtEmpType.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(txtLName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(txtFName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(txtMName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel13)
                    .addComponent(jLabel1)
                    .addComponent(jLabel9)
                    .addComponent(jLabel17)
                    .addComponent(txtCurrPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(txtNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(txtLName4, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnEmpUpdateResetPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtEmpID, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmpType, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(txtEmpStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(551, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmpID, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmpStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmpType, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCurrPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLName4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(btnEmpUpdateResetPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(232, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout userPanelLayout = new javax.swing.GroupLayout(userPanel);
        userPanel.setLayout(userPanelLayout);
        userPanelLayout.setHorizontalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPanelLayout.createSequentialGroup()
                .addGap(0, 3, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 3, Short.MAX_VALUE))
        );
        userPanelLayout.setVerticalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 89, Short.MAX_VALUE))
        );

        contentPanel.add(userPanel, "card2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(sidePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        dashboardFrame frameOne = new dashboardFrame();
        loginFrame nextForm = new loginFrame();

        nextForm.setVisible(true);
        frameOne.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransactionActionPerformed

    }//GEN-LAST:event_btnTransactionActionPerformed

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
        // TODO add your handling code here:
        contentPanel.removeAll();
        contentPanel.add(dashboardPanel);
        contentPanel.repaint();
        contentPanel.revalidate();
    }//GEN-LAST:event_btnDashboardActionPerformed

    private void btnDataEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDataEntryActionPerformed

    private void btnRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecordsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRecordsActionPerformed

    private void btnSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettingsActionPerformed
        // TODO add your handling code here:
        contentPanel.removeAll();
        contentPanel.add(userPanel);
        contentPanel.repaint();
        contentPanel.revalidate();
        setUserProfile();
    }//GEN-LAST:event_btnSettingsActionPerformed

    private void txtMNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMNameActionPerformed

    private void btnEmpUpdateResetPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpUpdateResetPasswordActionPerformed
        // TODO add your handling code here:
        resetPassword();

    }//GEN-LAST:event_btnEmpUpdateResetPasswordActionPerformed

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
            java.util.logging.Logger.getLogger(staffForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(staffForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(staffForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(staffForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new staffForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel adminIcon;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnDataEntry;
    private javax.swing.JButton btnEmpUpdateResetPassword;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnRecords;
    private javax.swing.JButton btnSettings;
    private javax.swing.JButton btnTransaction;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel dashboardIcon;
    private javax.swing.JPanel dashboardPanel;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel logoutIcon;
    private javax.swing.JLabel recordsIcon;
    private javax.swing.JLabel sLblCurrUser;
    private javax.swing.JLabel settingsIcon1;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JLabel tLblCurrType;
    private javax.swing.JLabel transactionIcon;
    private javax.swing.JTextField txtCurrPassword;
    private javax.swing.JTextField txtEmpID;
    private javax.swing.JTextField txtEmpStatus;
    private javax.swing.JTextField txtEmpType;
    private javax.swing.JTextField txtFName;
    private javax.swing.JTextField txtLName;
    private javax.swing.JTextField txtLName4;
    private javax.swing.JTextField txtMName;
    private javax.swing.JTextField txtNewPassword;
    private javax.swing.JPanel userPanel;
    // End of variables declaration//GEN-END:variables
}
