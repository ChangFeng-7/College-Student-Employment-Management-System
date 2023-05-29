import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.GroupLayout;
/*
 * Created by JFormDesigner on Tue May 23 19:25:37 CST 2023
 */


/**
 * @author linzhengyang
 */
public class UI {
    public UI() {
        initComponents();
    }

    private void button1(ActionEvent e) {
        // TODO add your code here
    }

    private void button1MouseClicked(MouseEvent e) {

    }

    private void LoginWindowClosing(WindowEvent e) {
        System.exit(0);
    }

    private void EmployersWindowClosing(WindowEvent e) {
        System.exit(0);
    }

    private void AdminWindowClosing(WindowEvent e) {
        System.exit(0);
    }

    private void StudentWindowClosing(WindowEvent e) {
        System.exit(0);
    }

    private void button8MouseClicked(MouseEvent e) {
        System.exit(0);
    }

    private void button9MouseClicked(MouseEvent e) {
        System.exit(0);
    }

    private void button10MouseClicked(MouseEvent e) {
        System.exit(0);
    }

    private void button12MouseClicked(MouseEvent e) {
        Register.setVisible(false);
        dialog1.setVisible(true);
    }

    private void dialog1WindowClosing(WindowEvent e) {
        Login.setVisible(true);
    }

    private void button11MouseClicked(MouseEvent e) {
        Register.setVisible(true);
        Login.setVisible(false);
    }

    private void button13MouseClicked(MouseEvent e) {

    }

    private void button1MousePressed(MouseEvent e) {
        String url = "jdbc:mysql://localhost:3306";  // 数据库连接URL
        String username = "root";  // 数据库用户名
        String password = "Lzy-200387";  // 数据库密码
        String account = textField1.getText(); // 待检查的账号
        String passwordToCheck = passwordField1.getText();  // 待检查的密码
        String roleToCheck = comboBox1.getSelectedItem().toString();  // 待检查的身份
        try {
            // 加载数据库驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 创建数据库连接
            Connection connection = DriverManager.getConnection(url, username, password);

            // 创建 SQL 查询
            String sql = "SELECT * FROM 高校学生就业管理系统.账号信息表 WHERE 账号 = ? AND 密码 = ? AND 身份 = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, account);
            statement.setString(2, passwordToCheck);
            statement.setString(3, roleToCheck);

            // 执行查询
            ResultSet resultSet = statement.executeQuery();

            // 检查结果集是否有数据
            if (resultSet.next()) {
                // 账号信息存在
                Login.setVisible(false);
                if (comboBox1.getSelectedItem().toString() == "学生") {
                    Student.setVisible(true);
                }
                if (comboBox1.getSelectedItem().toString() == "管理员") {
                    Admin.setVisible(true);
                }
                if (comboBox1.getSelectedItem().toString() == "用人单位") {
                    Employers.setVisible(true);
                }
            } else {
                // 账号信息不存在
                dialog2.setVisible(true);
            }

            // 关闭连接和资源
            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException y) {
            y.printStackTrace();
        } catch (SQLException y) {
            y.printStackTrace();
        }
    }

    private void button13MousePressed(MouseEvent e) {
        Login.setVisible(true);
        dialog1.setVisible(false);
    }

    private void button14MousePressed(MouseEvent e) {
        dialog2.setVisible(false);
    }

    private void button12MousePressed(MouseEvent e) {
        // 提供账号、密码、身份信息
        String account = textField3.getText();
        String password = textField4.getText();
        String identity = comboBox2.getSelectedItem().toString();

        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/高校学生就业管理系统";  // 数据库连接URL
        String username = "root";  // 数据库用户名
        String password1 = "Lzy-200387";  // 数据库密码

        // 建立数据库连接
        try (Connection connection = DriverManager.getConnection(url, username, password1)) {
            // 创建插入数据的 SQL 语句
            String sql = "INSERT INTO 高校学生就业管理系统.账号信息表 (账号, 密码, 身份) VALUES (?, ?, ?)";

            // 创建 PreparedStatement 对象并设置参数
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, account);
                statement.setString(2, password);
                statement.setString(3, identity);

                // 执行插入操作
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("账号信息已成功插入数据库。");
                } else {
                    System.out.println("插入操作未成功执行。");
                }
            }
        } catch (SQLException y) {
            y.printStackTrace();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - 林正阳
        Login = new JFrame();
        label1 = new JLabel();
        label2 = new JLabel();
        textField1 = new JTextField();
        label3 = new JLabel();
        comboBox1 = new JComboBox<>();
        button1 = new JButton();
        button11 = new JButton();
        passwordField1 = new JPasswordField();
        Student = new JFrame();
        button2 = new JButton();
        button3 = new JButton();
        button4 = new JButton();
        button10 = new JButton();
        Admin = new JFrame();
        button5 = new JButton();
        button6 = new JButton();
        button7 = new JButton();
        button9 = new JButton();
        Employers = new JFrame();
        button8 = new JButton();
        Register = new JFrame();
        label4 = new JLabel();
        textField3 = new JTextField();
        textField4 = new JTextField();
        label5 = new JLabel();
        label6 = new JLabel();
        comboBox2 = new JComboBox<>();
        button12 = new JButton();
        dialog1 = new JDialog();
        label7 = new JLabel();
        button13 = new JButton();
        Admin1 = new JFrame();
        button17 = new JButton();
        Admin2 = new JFrame();
        button21 = new JButton();
        Admin3 = new JFrame();
        button25 = new JButton();
        Student1 = new JFrame();
        button18 = new JButton();
        Student2 = new JFrame();
        button19 = new JButton();
        Student3 = new JFrame();
        button20 = new JButton();
        dialog2 = new JDialog();
        label8 = new JLabel();
        button14 = new JButton();

        //======== Login ========
        {
            Login.setTitle("\u767b\u9646");
            Login.setAlwaysOnTop(true);
            Login.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    LoginWindowClosing(e);
                }
            });
            var LoginContentPane = Login.getContentPane();

            //---- label1 ----
            label1.setText("\u8d26\u53f7");

            //---- label2 ----
            label2.setText("\u5bc6\u7801");

            //---- label3 ----
            label3.setText("\u8eab\u4efd");

            //---- comboBox1 ----
            comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
                "\u5b66\u751f",
                "\u7ba1\u7406\u5458",
                "\u7528\u4eba\u5355\u4f4d"
            }));

            //---- button1 ----
            button1.setText("\u767b\u9646");
            button1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button1MouseClicked(e);
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    button1MousePressed(e);
                }
            });

            //---- button11 ----
            button11.setText("\u6ce8\u518c");
            button11.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button11MouseClicked(e);
                }
            });

            GroupLayout LoginContentPaneLayout = new GroupLayout(LoginContentPane);
            LoginContentPane.setLayout(LoginContentPaneLayout);
            LoginContentPaneLayout.setHorizontalGroup(
                LoginContentPaneLayout.createParallelGroup()
                    .addGroup(LoginContentPaneLayout.createSequentialGroup()
                        .addGap(195, 195, 195)
                        .addGroup(LoginContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addGroup(LoginContentPaneLayout.createSequentialGroup()
                                .addGroup(LoginContentPaneLayout.createParallelGroup()
                                    .addComponent(label1)
                                    .addComponent(label2))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(LoginContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(textField1, GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                                    .addComponent(passwordField1, GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)))
                            .addGroup(LoginContentPaneLayout.createSequentialGroup()
                                .addComponent(label3)
                                .addGap(18, 18, 18)
                                .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addComponent(button1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button11, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(212, Short.MAX_VALUE))
            );
            LoginContentPaneLayout.setVerticalGroup(
                LoginContentPaneLayout.createParallelGroup()
                    .addGroup(LoginContentPaneLayout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addGroup(LoginContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label1)
                            .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(LoginContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label2)
                            .addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(LoginContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label3)
                            .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addComponent(button1)
                        .addGap(18, 18, 18)
                        .addComponent(button11)
                        .addContainerGap(54, Short.MAX_VALUE))
            );
            Login.pack();
            Login.setLocationRelativeTo(Login.getOwner());
        }

        //======== Student ========
        {
            Student.setTitle("\u4e3b\u9875");
            Student.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    StudentWindowClosing(e);
                }
            });
            var StudentContentPane = Student.getContentPane();

            //---- button2 ----
            button2.setText("\u62db\u8058\u4fe1\u606f");

            //---- button3 ----
            button3.setText("\u5c31\u4e1a\u767b\u8bb0");

            //---- button4 ----
            button4.setText("\u4e2a\u4eba\u4fe1\u606f\u767b\u8bb0");

            //---- button10 ----
            button10.setText("\u9000\u51fa\u7cfb\u7edf");
            button10.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button10MouseClicked(e);
                }
            });

            GroupLayout StudentContentPaneLayout = new GroupLayout(StudentContentPane);
            StudentContentPane.setLayout(StudentContentPaneLayout);
            StudentContentPaneLayout.setHorizontalGroup(
                StudentContentPaneLayout.createParallelGroup()
                    .addGroup(StudentContentPaneLayout.createSequentialGroup()
                        .addGroup(StudentContentPaneLayout.createParallelGroup()
                            .addGroup(StudentContentPaneLayout.createSequentialGroup()
                                .addGap(228, 228, 228)
                                .addGroup(StudentContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(button3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(button4)
                                    .addComponent(button2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(StudentContentPaneLayout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(button10)))
                        .addContainerGap(228, Short.MAX_VALUE))
            );
            StudentContentPaneLayout.setVerticalGroup(
                StudentContentPaneLayout.createParallelGroup()
                    .addGroup(StudentContentPaneLayout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addComponent(button4)
                        .addGap(18, 18, 18)
                        .addComponent(button3)
                        .addGap(18, 18, 18)
                        .addComponent(button2)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                        .addComponent(button10)
                        .addContainerGap())
            );
            Student.pack();
            Student.setLocationRelativeTo(Student.getOwner());
        }

        //======== Admin ========
        {
            Admin.setTitle("\u4e3b\u9875");
            Admin.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    AdminWindowClosing(e);
                }
            });
            var AdminContentPane = Admin.getContentPane();

            //---- button5 ----
            button5.setText("\u9662\u7cfb\u4fe1\u606f\u7ba1\u7406");

            //---- button6 ----
            button6.setText("\u6bd5\u4e1a\u751f\u4fe1\u606f\u7ba1\u7406");

            //---- button7 ----
            button7.setText("\u62db\u8058\u4fe1\u606f\u7ba1\u7406");

            //---- button9 ----
            button9.setText("\u9000\u51fa\u7cfb\u7edf");
            button9.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button9MouseClicked(e);
                }
            });

            GroupLayout AdminContentPaneLayout = new GroupLayout(AdminContentPane);
            AdminContentPane.setLayout(AdminContentPaneLayout);
            AdminContentPaneLayout.setHorizontalGroup(
                AdminContentPaneLayout.createParallelGroup()
                    .addGroup(AdminContentPaneLayout.createSequentialGroup()
                        .addGroup(AdminContentPaneLayout.createParallelGroup()
                            .addGroup(AdminContentPaneLayout.createSequentialGroup()
                                .addGap(222, 222, 222)
                                .addGroup(AdminContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(button6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(button5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(button7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(AdminContentPaneLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(button9)))
                        .addContainerGap(221, Short.MAX_VALUE))
            );
            AdminContentPaneLayout.setVerticalGroup(
                AdminContentPaneLayout.createParallelGroup()
                    .addGroup(AdminContentPaneLayout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(button5)
                        .addGap(18, 18, 18)
                        .addComponent(button6)
                        .addGap(18, 18, 18)
                        .addComponent(button7)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 147, Short.MAX_VALUE)
                        .addComponent(button9)
                        .addContainerGap())
            );
            Admin.pack();
            Admin.setLocationRelativeTo(Admin.getOwner());
        }

        //======== Employers ========
        {
            Employers.setTitle("\u62db\u8058\u9700\u6c42");
            Employers.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    EmployersWindowClosing(e);
                }
            });
            var EmployersContentPane = Employers.getContentPane();

            //---- button8 ----
            button8.setText("\u9000\u51fa\u7cfb\u7edf");
            button8.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button8MouseClicked(e);
                }
            });

            GroupLayout EmployersContentPaneLayout = new GroupLayout(EmployersContentPane);
            EmployersContentPane.setLayout(EmployersContentPaneLayout);
            EmployersContentPaneLayout.setHorizontalGroup(
                EmployersContentPaneLayout.createParallelGroup()
                    .addGroup(EmployersContentPaneLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(button8)
                        .addContainerGap(455, Short.MAX_VALUE))
            );
            EmployersContentPaneLayout.setVerticalGroup(
                EmployersContentPaneLayout.createParallelGroup()
                    .addGroup(GroupLayout.Alignment.TRAILING, EmployersContentPaneLayout.createSequentialGroup()
                        .addContainerGap(368, Short.MAX_VALUE)
                        .addComponent(button8)
                        .addContainerGap())
            );
            Employers.pack();
            Employers.setLocationRelativeTo(Employers.getOwner());
        }

        //======== Register ========
        {
            Register.setTitle("\u6ce8\u518c");
            var RegisterContentPane = Register.getContentPane();

            //---- label4 ----
            label4.setText("\u8d26\u53f7");

            //---- label5 ----
            label5.setText("\u5bc6\u7801");

            //---- label6 ----
            label6.setText("\u8eab\u4efd");

            //---- comboBox2 ----
            comboBox2.setModel(new DefaultComboBoxModel<>(new String[] {
                "\u5b66\u751f",
                "\u7ba1\u7406\u5458",
                "\u7528\u4eba\u5355\u4f4d"
            }));

            //---- button12 ----
            button12.setText("\u6ce8\u518c");
            button12.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button12MouseClicked(e);
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    button12MousePressed(e);
                }
            });

            GroupLayout RegisterContentPaneLayout = new GroupLayout(RegisterContentPane);
            RegisterContentPane.setLayout(RegisterContentPaneLayout);
            RegisterContentPaneLayout.setHorizontalGroup(
                RegisterContentPaneLayout.createParallelGroup()
                    .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(RegisterContentPaneLayout.createParallelGroup()
                            .addComponent(button12, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE)
                            .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                                .addComponent(label4)
                                .addGap(12, 12, 12)
                                .addComponent(textField3, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
                            .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                                .addComponent(label5)
                                .addGap(12, 12, 12)
                                .addComponent(textField4, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
                            .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                                .addComponent(label6)
                                .addGap(18, 18, 18)
                                .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(68, Short.MAX_VALUE))
            );
            RegisterContentPaneLayout.setVerticalGroup(
                RegisterContentPaneLayout.createParallelGroup()
                    .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(RegisterContentPaneLayout.createParallelGroup()
                            .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(label4))
                            .addComponent(textField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(RegisterContentPaneLayout.createParallelGroup()
                            .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(label5))
                            .addComponent(textField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(RegisterContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label6)
                            .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(button12)
                        .addContainerGap(78, Short.MAX_VALUE))
            );
            Register.pack();
            Register.setLocationRelativeTo(Register.getOwner());
        }

        //======== dialog1 ========
        {
            dialog1.setTitle("\u901a\u77e5");
            dialog1.setAlwaysOnTop(true);
            dialog1.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dialog1WindowClosing(e);
                }
            });
            var dialog1ContentPane = dialog1.getContentPane();

            //---- label7 ----
            label7.setText("\u6ce8\u518c\u6210\u529f\uff01\u8bf7\u91cd\u65b0\u767b\u5f55\u3002");

            //---- button13 ----
            button13.setText("\u786e\u5b9a");
            button13.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button13MousePressed(e);
                }
            });

            GroupLayout dialog1ContentPaneLayout = new GroupLayout(dialog1ContentPane);
            dialog1ContentPane.setLayout(dialog1ContentPaneLayout);
            dialog1ContentPaneLayout.setHorizontalGroup(
                dialog1ContentPaneLayout.createParallelGroup()
                    .addGroup(dialog1ContentPaneLayout.createSequentialGroup()
                        .addContainerGap(81, Short.MAX_VALUE)
                        .addComponent(label7)
                        .addGap(69, 69, 69))
                    .addGroup(dialog1ContentPaneLayout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(button13)
                        .addContainerGap(111, Short.MAX_VALUE))
            );
            dialog1ContentPaneLayout.setVerticalGroup(
                dialog1ContentPaneLayout.createParallelGroup()
                    .addGroup(dialog1ContentPaneLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(label7)
                        .addGap(36, 36, 36)
                        .addComponent(button13)
                        .addContainerGap(39, Short.MAX_VALUE))
            );
            dialog1.pack();
            dialog1.setLocationRelativeTo(dialog1.getOwner());
        }

        //======== Admin1 ========
        {
            Admin1.setTitle("\u9662\u7cfb\u4fe1\u606f\u7ba1\u7406");
            Admin1.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    AdminWindowClosing(e);
                }
            });
            var Admin1ContentPane = Admin1.getContentPane();

            //---- button17 ----
            button17.setText("\u8fd4\u56de");
            button17.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button9MouseClicked(e);
                }
            });

            GroupLayout Admin1ContentPaneLayout = new GroupLayout(Admin1ContentPane);
            Admin1ContentPane.setLayout(Admin1ContentPaneLayout);
            Admin1ContentPaneLayout.setHorizontalGroup(
                Admin1ContentPaneLayout.createParallelGroup()
                    .addGroup(Admin1ContentPaneLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(button17)
                        .addContainerGap(462, Short.MAX_VALUE))
            );
            Admin1ContentPaneLayout.setVerticalGroup(
                Admin1ContentPaneLayout.createParallelGroup()
                    .addGroup(Admin1ContentPaneLayout.createSequentialGroup()
                        .addContainerGap(368, Short.MAX_VALUE)
                        .addComponent(button17)
                        .addContainerGap())
            );
            Admin1.pack();
            Admin1.setLocationRelativeTo(Admin1.getOwner());
        }

        //======== Admin2 ========
        {
            Admin2.setTitle("\u6bd5\u4e1a\u751f\u4fe1\u606f\u7ba1\u7406");
            Admin2.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    AdminWindowClosing(e);
                }
            });
            var Admin2ContentPane = Admin2.getContentPane();

            //---- button21 ----
            button21.setText("\u8fd4\u56de");
            button21.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button9MouseClicked(e);
                }
            });

            GroupLayout Admin2ContentPaneLayout = new GroupLayout(Admin2ContentPane);
            Admin2ContentPane.setLayout(Admin2ContentPaneLayout);
            Admin2ContentPaneLayout.setHorizontalGroup(
                Admin2ContentPaneLayout.createParallelGroup()
                    .addGroup(Admin2ContentPaneLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(button21)
                        .addContainerGap(462, Short.MAX_VALUE))
            );
            Admin2ContentPaneLayout.setVerticalGroup(
                Admin2ContentPaneLayout.createParallelGroup()
                    .addGroup(Admin2ContentPaneLayout.createSequentialGroup()
                        .addContainerGap(368, Short.MAX_VALUE)
                        .addComponent(button21)
                        .addContainerGap())
            );
            Admin2.pack();
            Admin2.setLocationRelativeTo(Admin2.getOwner());
        }

        //======== Admin3 ========
        {
            Admin3.setTitle("\u62db\u8058\u4fe1\u606f\u7ba1\u7406");
            Admin3.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    AdminWindowClosing(e);
                }
            });
            var Admin3ContentPane = Admin3.getContentPane();

            //---- button25 ----
            button25.setText("\u8fd4\u56de");
            button25.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button9MouseClicked(e);
                }
            });

            GroupLayout Admin3ContentPaneLayout = new GroupLayout(Admin3ContentPane);
            Admin3ContentPane.setLayout(Admin3ContentPaneLayout);
            Admin3ContentPaneLayout.setHorizontalGroup(
                Admin3ContentPaneLayout.createParallelGroup()
                    .addGroup(Admin3ContentPaneLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(button25)
                        .addContainerGap(462, Short.MAX_VALUE))
            );
            Admin3ContentPaneLayout.setVerticalGroup(
                Admin3ContentPaneLayout.createParallelGroup()
                    .addGroup(Admin3ContentPaneLayout.createSequentialGroup()
                        .addContainerGap(368, Short.MAX_VALUE)
                        .addComponent(button25)
                        .addContainerGap())
            );
            Admin3.pack();
            Admin3.setLocationRelativeTo(Admin3.getOwner());
        }

        //======== Student1 ========
        {
            Student1.setTitle("\u4e2a\u4eba\u4fe1\u606f\u767b\u8bb0");
            Student1.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    AdminWindowClosing(e);
                }
            });
            var Student1ContentPane = Student1.getContentPane();

            //---- button18 ----
            button18.setText("\u8fd4\u56de");
            button18.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button9MouseClicked(e);
                }
            });

            GroupLayout Student1ContentPaneLayout = new GroupLayout(Student1ContentPane);
            Student1ContentPane.setLayout(Student1ContentPaneLayout);
            Student1ContentPaneLayout.setHorizontalGroup(
                Student1ContentPaneLayout.createParallelGroup()
                    .addGroup(Student1ContentPaneLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(button18)
                        .addContainerGap(462, Short.MAX_VALUE))
            );
            Student1ContentPaneLayout.setVerticalGroup(
                Student1ContentPaneLayout.createParallelGroup()
                    .addGroup(Student1ContentPaneLayout.createSequentialGroup()
                        .addContainerGap(368, Short.MAX_VALUE)
                        .addComponent(button18)
                        .addContainerGap())
            );
            Student1.pack();
            Student1.setLocationRelativeTo(Student1.getOwner());
        }

        //======== Student2 ========
        {
            Student2.setTitle("\u5c31\u4e1a\u767b\u8bb0");
            Student2.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    AdminWindowClosing(e);
                }
            });
            var Student2ContentPane = Student2.getContentPane();

            //---- button19 ----
            button19.setText("\u8fd4\u56de");
            button19.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button9MouseClicked(e);
                }
            });

            GroupLayout Student2ContentPaneLayout = new GroupLayout(Student2ContentPane);
            Student2ContentPane.setLayout(Student2ContentPaneLayout);
            Student2ContentPaneLayout.setHorizontalGroup(
                Student2ContentPaneLayout.createParallelGroup()
                    .addGroup(Student2ContentPaneLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(button19)
                        .addContainerGap(462, Short.MAX_VALUE))
            );
            Student2ContentPaneLayout.setVerticalGroup(
                Student2ContentPaneLayout.createParallelGroup()
                    .addGroup(Student2ContentPaneLayout.createSequentialGroup()
                        .addContainerGap(368, Short.MAX_VALUE)
                        .addComponent(button19)
                        .addContainerGap())
            );
            Student2.pack();
            Student2.setLocationRelativeTo(Student2.getOwner());
        }

        //======== Student3 ========
        {
            Student3.setTitle("\u62db\u8058\u4fe1\u606f");
            Student3.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    AdminWindowClosing(e);
                }
            });
            var Student3ContentPane = Student3.getContentPane();

            //---- button20 ----
            button20.setText("\u8fd4\u56de");
            button20.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button9MouseClicked(e);
                }
            });

            GroupLayout Student3ContentPaneLayout = new GroupLayout(Student3ContentPane);
            Student3ContentPane.setLayout(Student3ContentPaneLayout);
            Student3ContentPaneLayout.setHorizontalGroup(
                Student3ContentPaneLayout.createParallelGroup()
                    .addGroup(Student3ContentPaneLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(button20)
                        .addContainerGap(462, Short.MAX_VALUE))
            );
            Student3ContentPaneLayout.setVerticalGroup(
                Student3ContentPaneLayout.createParallelGroup()
                    .addGroup(Student3ContentPaneLayout.createSequentialGroup()
                        .addContainerGap(368, Short.MAX_VALUE)
                        .addComponent(button20)
                        .addContainerGap())
            );
            Student3.pack();
            Student3.setLocationRelativeTo(Student3.getOwner());
        }

        //======== dialog2 ========
        {
            dialog2.setTitle("\u901a\u77e5");
            dialog2.setAlwaysOnTop(true);
            dialog2.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dialog1WindowClosing(e);
                }
            });
            var dialog2ContentPane = dialog2.getContentPane();

            //---- label8 ----
            label8.setText("\u767b\u9646\u4fe1\u606f\u8f93\u5165\u9519\u8bef\uff01");

            //---- button14 ----
            button14.setText("\u786e\u5b9a");
            button14.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button14MousePressed(e);
                }
            });

            GroupLayout dialog2ContentPaneLayout = new GroupLayout(dialog2ContentPane);
            dialog2ContentPane.setLayout(dialog2ContentPaneLayout);
            dialog2ContentPaneLayout.setHorizontalGroup(
                dialog2ContentPaneLayout.createParallelGroup()
                    .addGroup(dialog2ContentPaneLayout.createSequentialGroup()
                        .addGroup(dialog2ContentPaneLayout.createParallelGroup()
                            .addGroup(dialog2ContentPaneLayout.createSequentialGroup()
                                .addGap(104, 104, 104)
                                .addComponent(button14))
                            .addGroup(dialog2ContentPaneLayout.createSequentialGroup()
                                .addGap(86, 86, 86)
                                .addComponent(label8)))
                        .addContainerGap(90, Short.MAX_VALUE))
            );
            dialog2ContentPaneLayout.setVerticalGroup(
                dialog2ContentPaneLayout.createParallelGroup()
                    .addGroup(dialog2ContentPaneLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(label8)
                        .addGap(33, 33, 33)
                        .addComponent(button14)
                        .addContainerGap(39, Short.MAX_VALUE))
            );
            dialog2.pack();
            dialog2.setLocationRelativeTo(dialog2.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - 林正阳
    private JFrame Login;
    private JLabel label1;
    private JLabel label2;
    private JTextField textField1;
    private JLabel label3;
    private JComboBox<String> comboBox1;
    private JButton button1;
    private JButton button11;
    private JPasswordField passwordField1;
    private JFrame Student;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button10;
    private JFrame Admin;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button9;
    private JFrame Employers;
    private JButton button8;
    private JFrame Register;
    private JLabel label4;
    private JTextField textField3;
    private JTextField textField4;
    private JLabel label5;
    private JLabel label6;
    private JComboBox<String> comboBox2;
    private JButton button12;
    private JDialog dialog1;
    private JLabel label7;
    private JButton button13;
    private JFrame Admin1;
    private JButton button17;
    private JFrame Admin2;
    private JButton button21;
    private JFrame Admin3;
    private JButton button25;
    private JFrame Student1;
    private JButton button18;
    private JFrame Student2;
    private JButton button19;
    private JFrame Student3;
    private JButton button20;
    private JDialog dialog2;
    private JLabel label8;
    private JButton button14;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
    public static void main(String[] args) throws Exception {
        UI ui = new UI();
        ui.initComponents();
        ui.Login.setVisible(true);
        //链接数据库
    }
}
