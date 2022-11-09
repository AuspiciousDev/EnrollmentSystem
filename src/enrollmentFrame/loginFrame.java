/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package enrollmentFrame;

import java.awt.Color;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.sql.Connection;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.beans.Statement;
import javax.swing.JTextField;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class loginFrame extends javax.swing.JFrame {

    File file = new File(System.getProperty("user.home") + "/Documents/save.txt");

    /**
     * Creates new form loginFrame
     */
    private String strUsername;
    private String strPassword;

    Connection con = null;
    ResultSet rst = null;
    PreparedStatement pst = null;
    String SQL_URL = "jdbc:sqlserver://localhost:1433;user=admin;password=admin123;DatabaseName=ADPEnrollmentSystem;integrated‌​Security=true";
    String sqlLoginHistory = "INSERT INTO UserLoginHistory (Username,LoginDate,LoginTime) VALUES (?,?,?)";

    public void SAVE() {      //Save the UserName and Password (for one user)

        try {
            if (!file.exists()) {
                file.createNewFile();  //if the file !exist create a new one
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
            bw.write(edtUsername.getText()); //write the name
            bw.newLine(); //leave a new Line
            bw.write(edtPassword.getPassword()); //write the password
            bw.close(); //close the BufferdWriter

        } catch (IOException e) {
            e.printStackTrace();
        }

    }//End Of Save

    public void DELETE() {
        try {
            //file to be delete  
            if (file.exists()) {
                if (file.delete()) //returns Boolean value  
                {
                    System.out.println(file.getName() + " deleted");   //getting and printing the file name  
                } else {
                    System.out.println("failed");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UPDATE() { //UPDATE ON OPENING THE APPLICATION

        try {
            if (file.exists()) {    //if this file exists

                Scanner scan = new Scanner(file);   //Use Scanner to read the File

                edtUsername.setText(scan.nextLine());  //append the text to name field
                edtPassword.setText(scan.nextLine()); //append the text to password field
                scan.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }//End OF UPDATE

    private String perfSetDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String Date = dtf.format(now);

        return Date;
    }

    private String perfSetTimeDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("k:m:s ");
        LocalDateTime now = LocalDateTime.now();
        String Date = dtf.format(now);

        return Date;
    }

    public void perfLogin() {
        strUsername = edtUsername.getText();
        strPassword = edtPassword.getText();
        String sql = "SELECT USERS.Username, EMPLOYEE.FName,USERS.Password,EMPLOYEE.EmpStatus,EMPLOYEE.EmpType\n"
                + "FROM USERS LEFT JOIN EMPLOYEE ON EMPLOYEE.EmpID =  USERS.Username  \n"
                + "WHERE USERS.Username = ? \n"
                + "GROUP BY USERS.Username,EMPLOYEE.FName,USERS.Password,EMPLOYEE.EmpStatus,EMPLOYEE.EmpType";

        String dbUsername;
        String dbPassword;
        String dbEmpStatus;
        String dbEmpName;
        String dbPosition;
        if (loginRemember.isSelected()) {
            SAVE();
        } else {
            DELETE();
        }
        try {
            con = DriverManager.getConnection(SQL_URL);
            pst = con.prepareStatement(sql);
            pst.setString(1, strUsername); // 
            rst = pst.executeQuery();
            if (rst.next()) {
                dbUsername = rst.getString("Username");
                dbPassword = rst.getString("Password");
                if (strPassword.equals(dbPassword)) {
                    System.out.println("SUCCESS");
                }
                dbEmpStatus = rst.getString("EmpStatus");
                dbEmpName = rst.getString("FName");
                dbPosition = rst.getString("EmpType");
                if (dbEmpStatus.equals("ACTIVE")) {
                    if (dbPosition.equals("Registrar")) {
                        con = null;
                        pst = null;
                        con = DriverManager.getConnection(SQL_URL);
                        pst = con.prepareStatement(sqlLoginHistory);

                        pst.setString(1, dbUsername);
                        pst.setString(2, perfSetDate());
                        pst.setString(3, perfSetTimeDate());
                        int y = pst.executeUpdate();
                        if (y == 1) {
                            JOptionPane.showMessageDialog(this, "Welcome!\n" + dbEmpName, "Login Notice!", JOptionPane.INFORMATION_MESSAGE);
                            dashboardFrame frameOne = new dashboardFrame(dbUsername);
                            loginFrame nextForm = new loginFrame();
                            nextForm.setVisible(false);
                            frameOne.setVisible(true);
                            this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "Database Error!!", "Login Error!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    if (dbPosition.equals("Teacher")) {
                        con = null;
                        pst = null;
                        con = DriverManager.getConnection(SQL_URL);
                        pst = con.prepareStatement(sqlLoginHistory);

                        pst.setString(1, dbUsername);
                        pst.setString(2, perfSetDate());
                        pst.setString(3, perfSetTimeDate());
                        int y = pst.executeUpdate();
                        if (y == 1) {
                            JOptionPane.showMessageDialog(this, "Welcome!\n" + dbEmpName, "Login Notice!", JOptionPane.INFORMATION_MESSAGE);
                            teacherForm frameOne = new teacherForm(dbUsername);
                            loginFrame nextForm = new loginFrame();
                            nextForm.setVisible(false);
                            frameOne.setVisible(true);
                            this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "Database Error!!", "Login Error!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    if (dbPosition.equals("Staff")) {
                        con = null;
                        pst = null;
                        con = DriverManager.getConnection(SQL_URL);
                        pst = con.prepareStatement(sqlLoginHistory);

                        pst.setString(1, dbUsername);
                        pst.setString(2, perfSetDate());
                        pst.setString(3, perfSetTimeDate());
                        int y = pst.executeUpdate();
                        if (y == 1) {
                            JOptionPane.showMessageDialog(this, "Welcome!\n" + dbEmpName, "Login Notice!", JOptionPane.INFORMATION_MESSAGE);
                            staffForm frameOne = new staffForm(dbUsername);
                            loginFrame nextForm = new loginFrame();
                            nextForm.setVisible(false);
                            frameOne.setVisible(true);
                            this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "Database Error!!", "Login Error!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "User account Disabled!!", "Login Error!", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Username not Found");
            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Connection Error = " + e);
            System.out.println("Connection Error" + e);
        }
    }

    public loginFrame() {
        initComponents();
        UPDATE();
    }

    ;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        loginRemember = new javax.swing.JCheckBox();
        btnLogin = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        edtPassword = new javax.swing.JPasswordField();
        edtUsername = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(30, 81, 40));
        setPreferredSize(new java.awt.Dimension(1300, 800));
        setResizable(false);

        mainPanel.setBackground(new java.awt.Color(30, 81, 40));
        mainPanel.setPreferredSize(new java.awt.Dimension(1300, 700));
        mainPanel.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("ENROLLMENT SYSTEM");
        mainPanel.add(jLabel2);
        jLabel2.setBounds(350, 170, 590, 90);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("REMEMBER ME");
        mainPanel.add(jLabel4);
        jLabel4.setBounds(480, 430, 150, 32);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Welcome to");
        mainPanel.add(jLabel3);
        jLabel3.setBounds(350, 50, 590, 90);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setText("USERNAME :");
        mainPanel.add(jLabel5);
        jLabel5.setBounds(220, 340, 150, 32);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setText("PASSWORD:");
        mainPanel.add(jLabel6);
        jLabel6.setBounds(220, 390, 150, 32);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("ACADEMIA DE PULILAN");
        mainPanel.add(jLabel7);
        jLabel7.setBounds(350, 120, 590, 90);
        mainPanel.add(loginRemember);
        loginRemember.setBounds(460, 440, 19, 19);

        btnLogin.setBackground(new java.awt.Color(204, 204, 204));
        btnLogin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLogin.setText("LOGIN");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        mainPanel.add(btnLogin);
        btnLogin.setBounds(460, 490, 390, 40);

        btnExit.setBackground(new java.awt.Color(204, 204, 204));
        btnExit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnExit.setText("EXIT");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        mainPanel.add(btnExit);
        btnExit.setBounds(460, 550, 390, 40);
        mainPanel.add(edtPassword);
        edtPassword.setBounds(460, 380, 390, 40);

        edtUsername.setToolTipText("Username");
        mainPanel.add(edtUsername);
        edtUsername.setBounds(460, 330, 390, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/enrollmentFrame/loginBackground.png"))); // NOI18N
        mainPanel.add(jLabel1);
        jLabel1.setBounds(0, 0, 1340, 770);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 769, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        perfLogin();// TODO add your handling code here:
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_btnExitActionPerformed

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
            java.util.logging.Logger.getLogger(loginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(loginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(loginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(loginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new loginFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLogin;
    private javax.swing.JPasswordField edtPassword;
    private javax.swing.JTextField edtUsername;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JCheckBox loginRemember;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
}
