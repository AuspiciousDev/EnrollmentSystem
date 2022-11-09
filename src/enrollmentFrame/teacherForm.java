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

/**
 *
 * @author King
 */
public class teacherForm extends javax.swing.JFrame {

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
            + "FROM USERS LEFT JOIN EMPLOYEE ON EMPLOYEE.EmpID =  ?  ";

    String sqlGetPassword = "SELECT Password FROM USERS WHERE Username =?";
    String sqlPatchPassword = "UPDATE USERS SET Password = ? WHERE Username =?";

    /**
     * /**
     * Creates new form teacherForm
     */
    public teacherForm() {
        initComponents();
    }

    public teacherForm(String empID) {
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
            tLblCurrUser.setText(dbEmpFULLName);
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
        String confPass = txtConfPassword.getText();
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

        sidePanel6 = new javax.swing.JPanel();
        adminIcon6 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        dashboardIcon6 = new javax.swing.JLabel();
        transactionIcon6 = new javax.swing.JLabel();
        dataIcon6 = new javax.swing.JLabel();
        recordsIcon6 = new javax.swing.JLabel();
        logoutIcon6 = new javax.swing.JLabel();
        tLblCurrUser = new javax.swing.JLabel();
        btnLogout6 = new javax.swing.JButton();
        btnTransaction6 = new javax.swing.JButton();
        btnDashboard12 = new javax.swing.JButton();
        btnDataEntry6 = new javax.swing.JButton();
        btnRecords6 = new javax.swing.JButton();
        btnSettings6 = new javax.swing.JButton();
        settingsIcon7 = new javax.swing.JLabel();
        tLblCurrType = new javax.swing.JLabel();
        contentPanel = new javax.swing.JPanel();
        userPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtEmpID = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtLName = new javax.swing.JTextField();
        txtFName = new javax.swing.JTextField();
        txtMName = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtCurrPassword = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtNewPassword = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtConfPassword = new javax.swing.JTextField();
        btnEmpUpdateResetPassword = new javax.swing.JButton();
        txtEmpStatus = new javax.swing.JTextField();
        txtEmpType = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ADP-TEACHER");

        sidePanel6.setBackground(new java.awt.Color(153, 153, 153));
        sidePanel6.setPreferredSize(new java.awt.Dimension(250, 800));

        adminIcon6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        adminIcon6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/personIcon.png"))); // NOI18N

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(102, 255, 102));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("WELCOME");

        dashboardIcon6.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        dashboardIcon6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/wdashboardIcon.png"))); // NOI18N
        dashboardIcon6.setPreferredSize(new java.awt.Dimension(22, 22));

        transactionIcon6.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        transactionIcon6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/wtransactionIcon.png"))); // NOI18N
        transactionIcon6.setPreferredSize(new java.awt.Dimension(22, 22));

        dataIcon6.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        dataIcon6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/wdataIcon.png"))); // NOI18N
        dataIcon6.setPreferredSize(new java.awt.Dimension(22, 22));

        recordsIcon6.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        recordsIcon6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/wrecordsIcon.png"))); // NOI18N
        recordsIcon6.setPreferredSize(new java.awt.Dimension(22, 22));

        logoutIcon6.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        logoutIcon6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/logoutIcon.png"))); // NOI18N
        logoutIcon6.setPreferredSize(new java.awt.Dimension(36, 36));

        tLblCurrUser.setBackground(new java.awt.Color(255, 255, 255));
        tLblCurrUser.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        tLblCurrUser.setForeground(new java.awt.Color(255, 255, 255));
        tLblCurrUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tLblCurrUser.setText("Lorem Ipsum Ripsum");

        btnLogout6.setBackground(new java.awt.Color(51, 0, 0));
        btnLogout6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLogout6.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout6.setText("LOGOUT");
        btnLogout6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout6.setMaximumSize(new java.awt.Dimension(83, 25));
        btnLogout6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogout6ActionPerformed(evt);
            }
        });

        btnTransaction6.setBackground(new java.awt.Color(51, 51, 51));
        btnTransaction6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTransaction6.setForeground(new java.awt.Color(255, 255, 255));
        btnTransaction6.setText("TRANSACTION");
        btnTransaction6.setMaximumSize(new java.awt.Dimension(113, 25));
        btnTransaction6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransaction6ActionPerformed(evt);
            }
        });

        btnDashboard12.setBackground(new java.awt.Color(51, 51, 51));
        btnDashboard12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDashboard12.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard12.setText("DASH BOARD");
        btnDashboard12.setMaximumSize(new java.awt.Dimension(113, 25));
        btnDashboard12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboard12ActionPerformed(evt);
            }
        });

        btnDataEntry6.setBackground(new java.awt.Color(51, 51, 51));
        btnDataEntry6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDataEntry6.setForeground(new java.awt.Color(255, 255, 255));
        btnDataEntry6.setText("DATA ENTRY");
        btnDataEntry6.setMaximumSize(new java.awt.Dimension(113, 25));
        btnDataEntry6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntry6ActionPerformed(evt);
            }
        });

        btnRecords6.setBackground(new java.awt.Color(51, 51, 51));
        btnRecords6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRecords6.setForeground(new java.awt.Color(255, 255, 255));
        btnRecords6.setText("RECORDS AND REPORTS");
        btnRecords6.setMaximumSize(new java.awt.Dimension(113, 25));
        btnRecords6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecords6ActionPerformed(evt);
            }
        });

        btnSettings6.setBackground(new java.awt.Color(51, 51, 51));
        btnSettings6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSettings6.setForeground(new java.awt.Color(255, 255, 255));
        btnSettings6.setText("USER SETTINGS");
        btnSettings6.setMaximumSize(new java.awt.Dimension(113, 25));
        btnSettings6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSettings6ActionPerformed(evt);
            }
        });

        settingsIcon7.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        settingsIcon7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/wperson1Icon.png"))); // NOI18N
        settingsIcon7.setPreferredSize(new java.awt.Dimension(22, 22));

        tLblCurrType.setBackground(new java.awt.Color(255, 255, 153));
        tLblCurrType.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        tLblCurrType.setForeground(new java.awt.Color(255, 255, 153));
        tLblCurrType.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tLblCurrType.setText("LOREM");

        javax.swing.GroupLayout sidePanel6Layout = new javax.swing.GroupLayout(sidePanel6);
        sidePanel6.setLayout(sidePanel6Layout);
        sidePanel6Layout.setHorizontalGroup(
            sidePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanel6Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(sidePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sidePanel6Layout.createSequentialGroup()
                        .addComponent(logoutIcon6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLogout6, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(sidePanel6Layout.createSequentialGroup()
                        .addGroup(sidePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(recordsIcon6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dataIcon6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(transactionIcon6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dashboardIcon6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(sidePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnRecords6, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnDataEntry6, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnTransaction6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDashboard12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanel6Layout.createSequentialGroup()
                        .addComponent(settingsIcon7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSettings6, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(41, 41, 41))
            .addGroup(sidePanel6Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(adminIcon6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(sidePanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sidePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tLblCurrUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(sidePanel6Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(tLblCurrType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        sidePanel6Layout.setVerticalGroup(
            sidePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adminIcon6, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tLblCurrUser, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tLblCurrType)
                .addGap(30, 30, 30)
                .addGroup(sidePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDashboard12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dashboardIcon6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(sidePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTransaction6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transactionIcon6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(sidePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDataEntry6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dataIcon6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(sidePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRecords6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recordsIcon6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(sidePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSettings6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsIcon7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(sidePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLogout6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logoutIcon6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );

        contentPanel.setLayout(new java.awt.CardLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        jLabel1.setText("USER SETTINGS");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel18.setText("Security");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Employee ID");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Employee Type");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Employee Status");

        txtEmpID.setToolTipText("asdasdasdas");
        txtEmpID.setEnabled(false);

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("Last name");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("First name");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setText("Middle name");

        txtLName.setToolTipText("asdasdasdas");
        txtLName.setPreferredSize(new java.awt.Dimension(60, 22));

        txtFName.setToolTipText("asdasdasdas");

        txtMName.setToolTipText("asdasdasdas");
        txtMName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMNameActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Current Password");

        txtCurrPassword.setToolTipText("asdasdasdas");
        txtCurrPassword.setPreferredSize(new java.awt.Dimension(60, 22));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setText("New Password");

        txtNewPassword.setToolTipText("asdasdasdas");
        txtNewPassword.setPreferredSize(new java.awt.Dimension(60, 22));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("Confirm Password");

        txtConfPassword.setToolTipText("asdasdasdas");
        txtConfPassword.setPreferredSize(new java.awt.Dimension(60, 22));

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
                            .addComponent(jLabel20)
                            .addComponent(txtLName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(txtFName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(txtMName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel18)
                    .addComponent(jLabel1)
                    .addComponent(jLabel9)
                    .addComponent(jLabel23)
                    .addComponent(txtCurrPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(txtNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(txtConfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnEmpUpdateResetPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtEmpID, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmpType, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
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
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmpID, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmpStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmpType, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCurrPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtConfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(btnEmpUpdateResetPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(232, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout userPanelLayout = new javax.swing.GroupLayout(userPanel);
        userPanel.setLayout(userPanelLayout);
        userPanelLayout.setHorizontalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
                .addComponent(sidePanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1343, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sidePanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
            .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogout6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogout6ActionPerformed
        // TODO add your handling code here:
        dashboardFrame frameOne = new dashboardFrame();
        loginFrame nextForm = new loginFrame();

        nextForm.setVisible(true);
        frameOne.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_btnLogout6ActionPerformed

    private void btnTransaction6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransaction6ActionPerformed

    }//GEN-LAST:event_btnTransaction6ActionPerformed

    private void btnDashboard12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboard12ActionPerformed

    }//GEN-LAST:event_btnDashboard12ActionPerformed

    private void btnDataEntry6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntry6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDataEntry6ActionPerformed

    private void btnRecords6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecords6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRecords6ActionPerformed

    private void btnSettings6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettings6ActionPerformed
        contentPanel.removeAll();
        contentPanel.add(userPanel);
        contentPanel.repaint();
        contentPanel.revalidate();        // TODO add your handling code here:
    }//GEN-LAST:event_btnSettings6ActionPerformed

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
            java.util.logging.Logger.getLogger(teacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(teacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(teacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(teacherForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new teacherForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel adminIcon;
    private javax.swing.JLabel adminIcon1;
    private javax.swing.JLabel adminIcon2;
    private javax.swing.JLabel adminIcon3;
    private javax.swing.JLabel adminIcon4;
    private javax.swing.JLabel adminIcon5;
    private javax.swing.JLabel adminIcon6;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnDashboard1;
    private javax.swing.JButton btnDashboard10;
    private javax.swing.JButton btnDashboard11;
    private javax.swing.JButton btnDashboard12;
    private javax.swing.JButton btnDashboard2;
    private javax.swing.JButton btnDashboard3;
    private javax.swing.JButton btnDashboard4;
    private javax.swing.JButton btnDashboard5;
    private javax.swing.JButton btnDashboard6;
    private javax.swing.JButton btnDashboard7;
    private javax.swing.JButton btnDashboard8;
    private javax.swing.JButton btnDashboard9;
    private javax.swing.JButton btnDataEntry;
    private javax.swing.JButton btnDataEntry1;
    private javax.swing.JButton btnDataEntry2;
    private javax.swing.JButton btnDataEntry3;
    private javax.swing.JButton btnDataEntry4;
    private javax.swing.JButton btnDataEntry5;
    private javax.swing.JButton btnDataEntry6;
    private javax.swing.JButton btnEmpUpdateResetPassword;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnLogout1;
    private javax.swing.JButton btnLogout2;
    private javax.swing.JButton btnLogout3;
    private javax.swing.JButton btnLogout4;
    private javax.swing.JButton btnLogout5;
    private javax.swing.JButton btnLogout6;
    private javax.swing.JButton btnMaintenance;
    private javax.swing.JButton btnMaintenance1;
    private javax.swing.JButton btnMaintenance2;
    private javax.swing.JButton btnMaintenance3;
    private javax.swing.JButton btnMaintenance4;
    private javax.swing.JButton btnMaintenance5;
    private javax.swing.JButton btnRecords;
    private javax.swing.JButton btnRecords1;
    private javax.swing.JButton btnRecords2;
    private javax.swing.JButton btnRecords3;
    private javax.swing.JButton btnRecords4;
    private javax.swing.JButton btnRecords5;
    private javax.swing.JButton btnRecords6;
    private javax.swing.JButton btnSettings;
    private javax.swing.JButton btnSettings1;
    private javax.swing.JButton btnSettings2;
    private javax.swing.JButton btnSettings3;
    private javax.swing.JButton btnSettings4;
    private javax.swing.JButton btnSettings5;
    private javax.swing.JButton btnSettings6;
    private javax.swing.JButton btnTransaction;
    private javax.swing.JButton btnTransaction1;
    private javax.swing.JButton btnTransaction2;
    private javax.swing.JButton btnTransaction3;
    private javax.swing.JButton btnTransaction4;
    private javax.swing.JButton btnTransaction5;
    private javax.swing.JButton btnTransaction6;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel dashboardIcon;
    private javax.swing.JLabel dashboardIcon1;
    private javax.swing.JLabel dashboardIcon2;
    private javax.swing.JLabel dashboardIcon3;
    private javax.swing.JLabel dashboardIcon4;
    private javax.swing.JLabel dashboardIcon5;
    private javax.swing.JLabel dashboardIcon6;
    private javax.swing.JLabel dataIcon;
    private javax.swing.JLabel dataIcon1;
    private javax.swing.JLabel dataIcon2;
    private javax.swing.JLabel dataIcon3;
    private javax.swing.JLabel dataIcon4;
    private javax.swing.JLabel dataIcon5;
    private javax.swing.JLabel dataIcon6;
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
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblName1;
    private javax.swing.JLabel lblName2;
    private javax.swing.JLabel lblName3;
    private javax.swing.JLabel lblName4;
    private javax.swing.JLabel lblName5;
    private javax.swing.JLabel logoutIcon;
    private javax.swing.JLabel logoutIcon1;
    private javax.swing.JLabel logoutIcon2;
    private javax.swing.JLabel logoutIcon3;
    private javax.swing.JLabel logoutIcon4;
    private javax.swing.JLabel logoutIcon5;
    private javax.swing.JLabel logoutIcon6;
    private javax.swing.JLabel maintenanceIcon;
    private javax.swing.JLabel maintenanceIcon1;
    private javax.swing.JLabel maintenanceIcon2;
    private javax.swing.JLabel maintenanceIcon3;
    private javax.swing.JLabel maintenanceIcon4;
    private javax.swing.JLabel maintenanceIcon5;
    private javax.swing.JLabel recordsIcon;
    private javax.swing.JLabel recordsIcon1;
    private javax.swing.JLabel recordsIcon2;
    private javax.swing.JLabel recordsIcon3;
    private javax.swing.JLabel recordsIcon4;
    private javax.swing.JLabel recordsIcon5;
    private javax.swing.JLabel recordsIcon6;
    private javax.swing.JLabel settingsIcon1;
    private javax.swing.JLabel settingsIcon2;
    private javax.swing.JLabel settingsIcon3;
    private javax.swing.JLabel settingsIcon4;
    private javax.swing.JLabel settingsIcon5;
    private javax.swing.JLabel settingsIcon6;
    private javax.swing.JLabel settingsIcon7;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JPanel sidePanel1;
    private javax.swing.JPanel sidePanel2;
    private javax.swing.JPanel sidePanel3;
    private javax.swing.JPanel sidePanel4;
    private javax.swing.JPanel sidePanel5;
    private javax.swing.JPanel sidePanel6;
    private javax.swing.JLabel tLblCurrType;
    private javax.swing.JLabel tLblCurrUser;
    private javax.swing.JLabel transactionIcon;
    private javax.swing.JLabel transactionIcon1;
    private javax.swing.JLabel transactionIcon2;
    private javax.swing.JLabel transactionIcon3;
    private javax.swing.JLabel transactionIcon4;
    private javax.swing.JLabel transactionIcon5;
    private javax.swing.JLabel transactionIcon6;
    private javax.swing.JTextField txtConfPassword;
    private javax.swing.JTextField txtCurrPassword;
    private javax.swing.JTextField txtEmpID;
    private javax.swing.JTextField txtEmpStatus;
    private javax.swing.JTextField txtEmpType;
    private javax.swing.JTextField txtFName;
    private javax.swing.JTextField txtLName;
    private javax.swing.JTextField txtMName;
    private javax.swing.JTextField txtNewPassword;
    private javax.swing.JPanel userPanel;
    private javax.swing.JLabel usersIcon;
    private javax.swing.JLabel usersIcon1;
    private javax.swing.JLabel usersIcon2;
    private javax.swing.JLabel usersIcon3;
    private javax.swing.JLabel usersIcon4;
    private javax.swing.JLabel usersIcon5;
    // End of variables declaration//GEN-END:variables
}
