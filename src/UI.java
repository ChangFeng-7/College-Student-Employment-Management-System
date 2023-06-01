import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
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
        label9.setVisible(false);
        textField2.setVisible(false);
    }

    private void button13MouseClicked(MouseEvent e) {

    }

    //登陆时在数据库中查询是否有对应账号、密码是否正确
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
                    // 创建表格模型
                    CustomTableModel tableModel = new CustomTableModel();

                    // 设置表格模型的列名
                    tableModel.setColumnIdentifiers(new Object[]{"职业编号", "职业名称", "职业类型名称", "需求数量", "已聘用数量", "发布日期", "截止日期"});

                    // 将表格模型设置给 JTable
                    table1.setModel(tableModel);

                    // 获取职业信息并填充到表格模型中
                    fillTableData(tableModel, getCompanyByAccount(textField1.getText()));

                    //下面添加数据的修改部分
                    tableModel.addTableModelListener(new TableModelListener() {
                        @Override
                        public void tableChanged(TableModelEvent e) {
                            if (e.getType() == TableModelEvent.UPDATE) {
                                int row = e.getFirstRow();
                                int column = e.getColumn();

                                // 获取修改后的数据
                                Object newValue = tableModel.getValueAt(row, column);

                                // 获取唯一标识符的值
                                Object primaryKey = tableModel.getValueAt(row, 0);

                                // 更新数据库中的对应记录
                                updateDatabaseRecord(primaryKey, column, newValue);
                            }
                        }
                    });
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

    //获取用户的注册信息并写入到数据库中
    private void button12MousePressed(MouseEvent e) {
        // 提供账号、密码、身份信息
        String account = textField3.getText();
        String password = textField4.getText();
        String identity = comboBox2.getSelectedItem().toString();
        String company = textField2.getText();
        if (company == "") {
            company = null;
        }

        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/高校学生就业管理系统";  // 数据库连接URL
        String username = "root";  // 数据库用户名
        String password1 = "Lzy-200387";  // 数据库密码

        // 建立数据库连接
        try (Connection connection = DriverManager.getConnection(url, username, password1)) {
            // 创建插入数据的 SQL 语句
            String sql = "INSERT INTO 高校学生就业管理系统.账号信息表 (账号, 密码, 身份 , 公司) VALUES (?, ?, ?, ?)";

            // 创建 PreparedStatement 对象并设置参数
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, account);
                statement.setString(2, password);
                statement.setString(3, identity);
                statement.setString(4, company);

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

    private void button5MousePressed(MouseEvent e) {
        Admin1.setVisible(true);
        Admin.setVisible(false);
    }

    private void button17MousePressed(MouseEvent e) {
        Admin1.setVisible(false);
        Admin.setVisible(true);
    }

    private void button21MousePressed(MouseEvent e) {
        Admin2.setVisible(false);
        Admin.setVisible(true);
    }

    private void button25MousePressed(MouseEvent e) {
        Admin3.setVisible(false);
        Admin.setVisible(true);
    }

    private void button6MousePressed(MouseEvent e) {
        Admin2.setVisible(true);
        Admin.setVisible(false);
    }

    private void button7MousePressed(MouseEvent e) {
        Admin3.setVisible(true);
        Admin.setVisible(false);
    }

    private void button15MousePressed(MouseEvent e) {
        textField6.setText(null);
        textField7.setText(null);
        EmployersSearch.setVisible(true);
    }

    // 更新数据库记录的方法
    //通过重写 isCellEditable 方法来实现某列不可修改
    //首先，创建一个自定义的 TableModel 类，继承自 DefaultTableModel，并重写 isCellEditable 方法。
    // 在 isCellEditable 方法中，根据需要设置特定单元格的可编辑性。
    public class CustomTableModel extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
            // 设置特定单元格的可编辑性
            if (column == 0 || column == 2 || column == 4 || column == 5) {
                // 第一列职业编号、第三列职业类型名称、第五列、第六列已聘用数量不可编辑
                return false;
            }
            // 其他单元格可编辑
            return true;
        }
    }

    private static void updateDatabaseRecord(Object primaryKey, int column, Object newValue) {
        Connection connection = null;
        PreparedStatement statement = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";  // 数据库连接URL
        String USERNAME = "root";  // 数据库用户名
        String PASSWORD = "Lzy-200387";  // 数据库密码
        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 构建更新语句
            String updateQuery = "UPDATE 高校学生就业管理系统.职业信息表 SET ";
            String columnToUpdate;

            // 根据列索引选择要更新的列
            switch (column) {
                case 0:
                    return;
                case 1:
                    columnToUpdate = "职业名称";
                    break;
                case 2:
                    return;
                case 3:
                    columnToUpdate = "需求数量";
                    break;
                case 4:
                    return;
                case 5:
                    columnToUpdate = "发布日期";
                    break;
                case 6:
                    columnToUpdate = "截止日期";
                    break;
                default:
                    // 如果列索引无效，则不执行更新操作
                    return;
            }

            // 构建完整的更新语句
            updateQuery += columnToUpdate + " = ? WHERE 职业编号 = ?";

            // 创建预编译语句对象
            statement = connection.prepareStatement(updateQuery);

            // 设置参数
            statement.setObject(1, newValue);
            statement.setObject(2, primaryKey);

            // 执行更新操作
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getCompanyByAccount(String account) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String URL = "jdbc:mysql://localhost:3306";  // 数据库连接URL
        String USERNAME = "root";  // 数据库用户名
        String PASSWORD = "Lzy-200387";  // 数据库密码
        String company = "";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 创建 PreparedStatement 对象
            String query = "SELECT 公司 FROM 高校学生就业管理系统.账号信息表 WHERE 账号 = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, account);

            // 执行查询语句
            resultSet = statement.executeQuery();

            // 获取查询结果
            if (resultSet.next()) {
                company = resultSet.getString("公司");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, resultSet);
        }

        return company;
    }


    // 从数据库中获取职业信息并填充到表格模型中
    private static void fillTableData(CustomTableModel tableModel, String currentCompany) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String URL = "jdbc:mysql://localhost:3306";  // 数据库连接URL
        String USERNAME = "root";  // 数据库用户名
        String PASSWORD = "Lzy-200387";  // 数据库密码
        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 创建 Statement 对象
            statement = connection.createStatement();

            // 执行查询语句
            String query = "SELECT * FROM 高校学生就业管理系统.职业信息表 WHERE 用人单位 = '" + currentCompany + "'";
            resultSet = statement.executeQuery(query);
            // 获取职业类型映射关系
            // 遍历结果集，将数据填充到表格模型中
            while (resultSet.next()) {
                Object[] rowData = new Object[]{
                        resultSet.getInt("职业编号"),
                        resultSet.getString("职业名称"),
                        resultSet.getString("职业类型名称"),
                        resultSet.getInt("需求数量"),
                        resultSet.getInt("已聘用数量"),
                        resultSet.getDate("发布日期"),
                        resultSet.getDate("截止日期"),
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, resultSet);
        }
    }

    private static void closeConnection(Connection connection, Statement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void comboBox2(ActionEvent e) {
        if (comboBox2.getSelectedItem().toString() == "用人单位") {
            label9.setVisible(true);
            textField2.setVisible(true);
        } else {
            label9.setVisible(false);
            textField2.setVisible(false);
        }
    }


    private void button22MousePressed(MouseEvent e) {
        dialog3.setVisible(false);
    }

    private void dialog3WindowClosing(WindowEvent e) {
        dialog3.setVisible(false);
    }

    private void button16MousePressed(MouseEvent e) {
        dialog3.setVisible(true);
    }

    private void button23MousePressed(MouseEvent e) {
            fillTableData((DefaultTableModel) table1.getModel(), textField6.getText(), textField7.getText(),getCompanyByAccount(textField1.getText()));
            EmployersSearch.setVisible(false);
    }

    public static void fillTableData(DefaultTableModel tableModel, String jobName, String jobTypeName, String company) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 创建 PreparedStatement 对象
            String query = "SELECT * FROM 职业信息表 WHERE 1=1 AND 用人单位 = '" + company + "'";
            if (jobName != null && !jobName.isEmpty()) {
                query += " AND 职业名称 = ?";
            }
            if (jobTypeName != null && !jobTypeName.isEmpty()) {
                query += " AND 职业类型名称 = ?";
            }
            statement = connection.prepareStatement(query);

            // 设置查询参数
            int parameterIndex = 1;
            if (jobName != null && !jobName.isEmpty()) {
                statement.setString(parameterIndex, jobName);
                parameterIndex++;
            }
            if (jobTypeName != null && !jobTypeName.isEmpty()) {
                statement.setString(parameterIndex, jobTypeName);
            }
            // 执行查询语句
            resultSet = statement.executeQuery();

            // 清空表格模型
            tableModel.setRowCount(0);

            // 遍历结果集，将数据填充到表格模型中
            while (resultSet.next()) {
                Object[] rowData = new Object[]{
                        resultSet.getInt("职业编号"),
                        resultSet.getString("职业名称"),
                        resultSet.getString("职业类型名称"),
                        resultSet.getInt("需求数量"),
                        resultSet.getInt("已聘用数量"),
                        resultSet.getDate("发布日期"),
                        resultSet.getDate("截止日期"),
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, resultSet);
        }
    }

    private void button24MousePressed(MouseEvent e) {
        // 创建表格模型
        CustomTableModel tableModel = new CustomTableModel();

        // 设置表格模型的列名
        tableModel.setColumnIdentifiers(new Object[]{"职业编号", "职业名称", "职业类型名称", "需求数量", "已聘用数量", "发布日期", "截止日期"});

        // 将表格模型设置给 JTable
        table1.setModel(tableModel);

        // 获取职业信息并填充到表格模型中
        fillTableData(tableModel, getCompanyByAccount(textField1.getText()));

        //下面添加数据的修改部分
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();

                    // 获取修改后的数据
                    Object newValue = tableModel.getValueAt(row, column);

                    // 获取唯一标识符的值
                    Object primaryKey = tableModel.getValueAt(row, 0);

                    // 更新数据库中的对应记录
                    updateDatabaseRecord(primaryKey, column, newValue);
                }
            }
        });
    }

    private void button26MousePressed(MouseEvent e) {
        if (textField11.getText().equals("")){
            textField11.setForeground(Color.gray); //将提示文字设置为灰色
            textField11.setText("XXXX-XX-XX");     //显示提示文字
        }
        EmployersNeed.setVisible(true);
    }

    private void button27MousePressed(MouseEvent e) {
        insertJobData(textField8.getText(),textField9.getText(),Integer.parseInt(textField10.getText()),convertStringToDate(textField11.getText()),getCompanyByAccount(textField1.getText()));
    }
    public static Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date utilDate = dateFormat.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            return  sqlDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void insertJobData(String jobName, String jobTypeName, int demandQuantity, Date deadline, String currentCompany) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";
        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 查询现有数据的数量
            String countQuery = "SELECT COUNT(*) FROM 职业信息表";
            statement = connection.prepareStatement(countQuery);
            resultSet = statement.executeQuery();
            resultSet.next();
            int jobCount = resultSet.getInt(1);

            // 自动生成职业编号
            int jobNumber = jobCount + 1;

            // 创建插入数据的 PreparedStatement 对象
            String insertQuery = "INSERT INTO 职业信息表 (职业编号, 职业名称, 职业类型名称, 需求数量, 用人单位,发布日期,截止日期,已聘用数量 ) VALUES (?, ?, ?, ?, ?, ?,?,?)";
            statement = connection.prepareStatement(insertQuery);
            statement.setInt(1, jobNumber);
            statement.setString(2, jobName);
            statement.setString(3, jobTypeName);
            statement.setInt(4, demandQuantity);
            statement.setString(5, currentCompany);
            java.util.Date date = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            statement.setDate(6, sqlDate);
            statement.setDate(7, new java.sql.Date(deadline.getTime()));
            statement.setInt(8, 0);

            // 执行插入语句
            statement.executeUpdate();

            System.out.println("职业信息已成功插入数据库，职业编号为：" + jobNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, resultSet);
        }
    }

    private void textField11FocusGained(FocusEvent e) {
        //得到焦点时，当前文本框的提示文字和创建该对象时的提示文字一样，说明用户正要键入内容
        if (textField11.getText().equals("XXXX-XX-XX")){
            textField11.setText("");     //将提示文字清空
            textField11.setForeground(Color.black);  //设置用户输入的字体颜色为黑色
        }
    }

    private void textField11FocusLost(FocusEvent e) {
        if (textField11.getText().equals("")){
            textField11.setForeground(Color.gray); //将提示文字设置为灰色
            textField11.setText("XXXX-XX-XX");     //显示提示文字
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
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        button15 = new JButton();
        button16 = new JButton();
        button24 = new JButton();
        button26 = new JButton();
        Register = new JFrame();
        label4 = new JLabel();
        textField3 = new JTextField();
        textField4 = new JTextField();
        label5 = new JLabel();
        label6 = new JLabel();
        comboBox2 = new JComboBox<>();
        button12 = new JButton();
        label9 = new JLabel();
        textField2 = new JTextField();
        dialog1 = new JDialog();
        label7 = new JLabel();
        button13 = new JButton();
        Admin1 = new JFrame();
        button17 = new JButton();
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
        Admin2 = new JFrame();
        button21 = new JButton();
        dialog3 = new JDialog();
        label10 = new JLabel();
        button22 = new JButton();
        label11 = new JLabel();
        EmployersSearch = new JFrame();
        label13 = new JLabel();
        textField6 = new JTextField();
        button23 = new JButton();
        label14 = new JLabel();
        textField7 = new JTextField();
        EmployersNeed = new JFrame();
        label15 = new JLabel();
        label16 = new JLabel();
        label17 = new JLabel();
        label18 = new JLabel();
        textField8 = new JTextField();
        textField9 = new JTextField();
        textField10 = new JTextField();
        textField11 = new JTextField();
        button27 = new JButton();

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
            button5.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button5MousePressed(e);
                }
            });

            //---- button6 ----
            button6.setText("\u6bd5\u4e1a\u751f\u4fe1\u606f\u7ba1\u7406");
            button6.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button6MousePressed(e);
                }
            });

            //---- button7 ----
            button7.setText("\u62db\u8058\u4fe1\u606f\u7ba1\u7406");
            button7.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button7MousePressed(e);
                }
            });

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

            //======== scrollPane1 ========
            {
                scrollPane1.setViewportView(table1);
            }

            //---- button15 ----
            button15.setText("\u67e5\u8be2\u9700\u6c42");
            button15.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button15MousePressed(e);
                }
            });

            //---- button16 ----
            button16.setText("\u4fee\u6539\u9700\u6c42");
            button16.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button16MousePressed(e);
                }
            });

            //---- button24 ----
            button24.setText("\u67e5\u8be2\u5168\u90e8\u9700\u6c42");
            button24.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button24MousePressed(e);
                }
            });

            //---- button26 ----
            button26.setText("\u53d1\u5e03\u9700\u6c42");
            button26.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button26MousePressed(e);
                }
            });

            GroupLayout EmployersContentPaneLayout = new GroupLayout(EmployersContentPane);
            EmployersContentPane.setLayout(EmployersContentPaneLayout);
            EmployersContentPaneLayout.setHorizontalGroup(
                EmployersContentPaneLayout.createParallelGroup()
                    .addGroup(EmployersContentPaneLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(EmployersContentPaneLayout.createParallelGroup()
                            .addComponent(scrollPane1)
                            .addGroup(EmployersContentPaneLayout.createSequentialGroup()
                                .addComponent(button8)
                                .addGap(18, 18, 18)
                                .addComponent(button24)
                                .addGap(18, 18, 18)
                                .addComponent(button15)
                                .addGap(18, 18, 18)
                                .addComponent(button16)
                                .addGap(18, 18, 18)
                                .addComponent(button26)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(34, 34, 34))
            );
            EmployersContentPaneLayout.setVerticalGroup(
                EmployersContentPaneLayout.createParallelGroup()
                    .addGroup(GroupLayout.Alignment.TRAILING, EmployersContentPaneLayout.createSequentialGroup()
                        .addContainerGap(36, Short.MAX_VALUE)
                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 305, GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(EmployersContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(button8)
                            .addComponent(button24)
                            .addComponent(button15)
                            .addComponent(button16)
                            .addComponent(button26))
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
            comboBox2.addActionListener(e -> comboBox2(e));

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

            //---- label9 ----
            label9.setText("\u516c\u53f8\u540d\u79f0");
            label9.setVisible(false);

            //---- textField2 ----
            textField2.setVisible(false);

            GroupLayout RegisterContentPaneLayout = new GroupLayout(RegisterContentPane);
            RegisterContentPane.setLayout(RegisterContentPaneLayout);
            RegisterContentPaneLayout.setHorizontalGroup(
                RegisterContentPaneLayout.createParallelGroup()
                    .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                        .addGroup(RegisterContentPaneLayout.createParallelGroup()
                            .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addGroup(RegisterContentPaneLayout.createParallelGroup()
                                    .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                                        .addComponent(label4)
                                        .addGap(12, 12, 12)
                                        .addComponent(textField3, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
                                    .addComponent(button12, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE)
                                    .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                                        .addComponent(label5)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(textField4, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                                        .addComponent(label6)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                            .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(label9)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textField2, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(72, Short.MAX_VALUE))
            );
            RegisterContentPaneLayout.setVerticalGroup(
                RegisterContentPaneLayout.createParallelGroup()
                    .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(RegisterContentPaneLayout.createParallelGroup()
                            .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(label4))
                            .addComponent(textField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(RegisterContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label5)
                            .addComponent(textField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(RegisterContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label6)
                            .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                        .addGroup(RegisterContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label9)
                            .addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(button12)
                        .addGap(40, 40, 40))
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
                public void mousePressed(MouseEvent e) {
                    button17MousePressed(e);
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
                public void mousePressed(MouseEvent e) {
                    button25MousePressed(e);
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
                public void mousePressed(MouseEvent e) {
                    button21MousePressed(e);
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

        //======== dialog3 ========
        {
            dialog3.setTitle("\u901a\u77e5");
            dialog3.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dialog3WindowClosing(e);
                }
            });
            var dialog3ContentPane = dialog3.getContentPane();

            //---- label10 ----
            label10.setText("\u76f4\u63a5\u5728\u8868\u683c\u4e0a\u8fdb\u884c\u4fee\u6539\u5373\u53ef");

            //---- button22 ----
            button22.setText("\u786e\u5b9a");
            button22.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button22MousePressed(e);
                }
            });

            //---- label11 ----
            label11.setText("\u4e0d\u53ef\u9009\u4e2d\u7684\u90e8\u5206\u65e0\u6743\u9650\u4fee\u6539");

            GroupLayout dialog3ContentPaneLayout = new GroupLayout(dialog3ContentPane);
            dialog3ContentPane.setLayout(dialog3ContentPaneLayout);
            dialog3ContentPaneLayout.setHorizontalGroup(
                dialog3ContentPaneLayout.createParallelGroup()
                    .addGroup(dialog3ContentPaneLayout.createSequentialGroup()
                        .addGroup(dialog3ContentPaneLayout.createParallelGroup()
                            .addGroup(dialog3ContentPaneLayout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addGroup(dialog3ContentPaneLayout.createParallelGroup()
                                    .addComponent(label11)
                                    .addComponent(label10)))
                            .addGroup(dialog3ContentPaneLayout.createSequentialGroup()
                                .addGap(117, 117, 117)
                                .addComponent(button22)))
                        .addContainerGap(81, Short.MAX_VALUE))
            );
            dialog3ContentPaneLayout.setVerticalGroup(
                dialog3ContentPaneLayout.createParallelGroup()
                    .addGroup(dialog3ContentPaneLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(label10)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label11)
                        .addGap(18, 18, 18)
                        .addComponent(button22)
                        .addContainerGap(42, Short.MAX_VALUE))
            );
            dialog3.pack();
            dialog3.setLocationRelativeTo(dialog3.getOwner());
        }

        //======== EmployersSearch ========
        {
            var EmployersSearchContentPane = EmployersSearch.getContentPane();

            //---- label13 ----
            label13.setText("\u804c\u4e1a\u540d\u79f0");

            //---- button23 ----
            button23.setText("\u67e5\u8be2");
            button23.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button23MousePressed(e);
                }
            });

            //---- label14 ----
            label14.setText("\u804c\u4e1a\u7c7b\u578b\u540d\u79f0");

            GroupLayout EmployersSearchContentPaneLayout = new GroupLayout(EmployersSearchContentPane);
            EmployersSearchContentPane.setLayout(EmployersSearchContentPaneLayout);
            EmployersSearchContentPaneLayout.setHorizontalGroup(
                EmployersSearchContentPaneLayout.createParallelGroup()
                    .addGroup(EmployersSearchContentPaneLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(EmployersSearchContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(label13)
                            .addComponent(label14))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(EmployersSearchContentPaneLayout.createParallelGroup()
                            .addComponent(textField6)
                            .addGroup(EmployersSearchContentPaneLayout.createSequentialGroup()
                                .addComponent(textField7, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                        .addComponent(button23)
                        .addGap(42, 42, 42))
            );
            EmployersSearchContentPaneLayout.setVerticalGroup(
                EmployersSearchContentPaneLayout.createParallelGroup()
                    .addGroup(EmployersSearchContentPaneLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(EmployersSearchContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label13)
                            .addComponent(textField6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button23, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(EmployersSearchContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label14))
                        .addContainerGap(70, Short.MAX_VALUE))
            );
            EmployersSearch.pack();
            EmployersSearch.setLocationRelativeTo(EmployersSearch.getOwner());
        }

        //======== EmployersNeed ========
        {
            EmployersNeed.setTitle("\u9700\u6c42\u53d1\u5e03");
            var EmployersNeedContentPane = EmployersNeed.getContentPane();

            //---- label15 ----
            label15.setText("\u804c\u4e1a\u540d\u79f0");

            //---- label16 ----
            label16.setText("\u804c\u4e1a\u7c7b\u578b\u540d\u79f0");

            //---- label17 ----
            label17.setText("\u9700\u6c42\u6570\u91cf");

            //---- label18 ----
            label18.setText("\u622a\u6b62\u65e5\u671f");

            //---- textField11 ----
            textField11.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    textField11FocusGained(e);
                }
                @Override
                public void focusLost(FocusEvent e) {
                    textField11FocusLost(e);
                }
            });

            //---- button27 ----
            button27.setText("\u53d1\u5e03\u9700\u6c42");
            button27.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button27MousePressed(e);
                }
            });

            GroupLayout EmployersNeedContentPaneLayout = new GroupLayout(EmployersNeedContentPane);
            EmployersNeedContentPane.setLayout(EmployersNeedContentPaneLayout);
            EmployersNeedContentPaneLayout.setHorizontalGroup(
                EmployersNeedContentPaneLayout.createParallelGroup()
                    .addGroup(EmployersNeedContentPaneLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(EmployersNeedContentPaneLayout.createParallelGroup()
                            .addGroup(EmployersNeedContentPaneLayout.createSequentialGroup()
                                .addGroup(EmployersNeedContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addGroup(EmployersNeedContentPaneLayout.createSequentialGroup()
                                        .addComponent(label15)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(textField8, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(EmployersNeedContentPaneLayout.createSequentialGroup()
                                        .addComponent(label16)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(textField9, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(EmployersNeedContentPaneLayout.createSequentialGroup()
                                        .addComponent(label17)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(textField10, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE))
                            .addGroup(EmployersNeedContentPaneLayout.createSequentialGroup()
                                .addGap(0, 24, Short.MAX_VALUE)
                                .addComponent(label18)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textField11, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                .addGap(78, 78, 78)))
                        .addComponent(button27)
                        .addGap(62, 62, 62))
            );
            EmployersNeedContentPaneLayout.setVerticalGroup(
                EmployersNeedContentPaneLayout.createParallelGroup()
                    .addGroup(EmployersNeedContentPaneLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(EmployersNeedContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label15)
                            .addComponent(textField8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(EmployersNeedContentPaneLayout.createParallelGroup()
                            .addGroup(EmployersNeedContentPaneLayout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(EmployersNeedContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label16)
                                    .addComponent(textField9, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(31, 31, 31)
                                .addGroup(EmployersNeedContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label17)
                                    .addComponent(textField10, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(31, 31, 31))
                            .addGroup(GroupLayout.Alignment.TRAILING, EmployersNeedContentPaneLayout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button27, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45)))
                        .addGroup(EmployersNeedContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label18)
                            .addComponent(textField11, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(24, Short.MAX_VALUE))
            );
            EmployersNeed.pack();
            EmployersNeed.setLocationRelativeTo(EmployersNeed.getOwner());
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
    private JScrollPane scrollPane1;
    private JTable table1;
    private JButton button15;
    private JButton button16;
    private JButton button24;
    private JButton button26;
    private JFrame Register;
    private JLabel label4;
    private JTextField textField3;
    private JTextField textField4;
    private JLabel label5;
    private JLabel label6;
    private JComboBox<String> comboBox2;
    private JButton button12;
    private JLabel label9;
    private JTextField textField2;
    private JDialog dialog1;
    private JLabel label7;
    private JButton button13;
    private JFrame Admin1;
    private JButton button17;
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
    private JFrame Admin2;
    private JButton button21;
    private JDialog dialog3;
    private JLabel label10;
    private JButton button22;
    private JLabel label11;
    private JFrame EmployersSearch;
    private JLabel label13;
    private JTextField textField6;
    private JButton button23;
    private JLabel label14;
    private JTextField textField7;
    private JFrame EmployersNeed;
    private JLabel label15;
    private JLabel label16;
    private JLabel label17;
    private JLabel label18;
    private JTextField textField8;
    private JTextField textField9;
    private JTextField textField10;
    private JTextField textField11;
    private JButton button27;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
    public static void main(String[] args) throws Exception {
        UI ui = new UI();
        ui.initComponents();
        ui.Login.setVisible(true);
    }
}
