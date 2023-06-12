import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        int studentID;
        if (company == "") {
            company = null;
        }
        if(textField34.getText()==""){
            studentID = 0;
        }else{
            studentID = Integer.parseInt(textField34.getText());
        }

        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/高校学生就业管理系统";  // 数据库连接URL
        String username = "root";  // 数据库用户名
        String password1 = "Lzy-200387";  // 数据库密码

        // 建立数据库连接
        try (Connection connection = DriverManager.getConnection(url, username, password1)) {
            // 创建插入数据的 SQL 语句
            String sql = "INSERT INTO 高校学生就业管理系统.账号信息表 (账号, 密码, 身份 , 公司 ,学号) VALUES (?, ?, ?, ?, ?)";

            // 创建 PreparedStatement 对象并设置参数
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, account);
                statement.setString(2, password);
                statement.setString(3, identity);
                statement.setString(4, company);
                statement.setInt(5, studentID);
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
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 获取数据库连接
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 创建 PreparedStatement 对象
            String query = "SELECT 院系编号, 院系名称 FROM 院系信息表";
            PreparedStatement statement = connection.prepareStatement(query);

            // 执行查询语句获取院系信息表的数据
            ResultSet resultSet = statement.executeQuery();

            // 创建表格模型
            DefaultTableModel tableModel = new DefaultTableModel();

            // 设置列名
            tableModel.addColumn("院系编号");
            tableModel.addColumn("院系名称");

            // 清空表格模型
            tableModel.setRowCount(0);

            // 遍历查询结果，将数据填充到表格模型中
            while (resultSet.next()) {
                Object[] rowData = new Object[]{
                        resultSet.getInt("院系编号"),
                        resultSet.getString("院系名称")
                };
                tableModel.addRow(rowData);
            }

            // 关闭连接和资源
            resultSet.close();
            connection.close();

            // 将表格模型设置给 table2
            table2.setModel(tableModel);
            // 将 tableChanged 方法添加为 table2 的 TableModelListener
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        table2.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();

                    // 判断被修改的列是否为院系编号或院系名称列
                    if (column == 0 || column == 1) {
                        // 获取对应行的院系编号和院系名称
                        Object departmentId = table2.getValueAt(row, 0);
                        Object departmentName = table2.getValueAt(row, 1);

                        // 更新数据库中的对应记录
                        updateDepartmentRecord(departmentId, departmentName);
                    }
                }
            }
        });

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
        updateGraduateDetailsTable(table3);
        table3.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();

                    // 获取修改后的值
                    Object newValue = table3.getModel().getValueAt(row, column);

                    // 获取学号列的值
                    Object studentId = table3.getModel().getValueAt(row, 0);

                    // 更新数据库中的毕业生信息
                    updateStudentInfo(studentId, column, newValue);
                }
            }
        });
    }

    private void button7MousePressed(MouseEvent e) {
        Admin3.setVisible(true);
        Admin.setVisible(false);
        loadJobInformation(table4);
        table4.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // 表格被修改时触发的事件处理逻辑
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    // 处理表格中特定单元格的修改
                    if (row != -1 && column != -1) {
                        Object data = table4.getValueAt(row, column);
                        // 调用方法处理修改后的数据
                        handleTableModification(row, column, data);
                    }
                }
            }
        });
    }

    private void button15MousePressed(MouseEvent e) {
        textField6.setText(null);
        textField7.setText(null);
        EmployersSearch.setVisible(true);
    }
    private void comboBox2(ActionEvent e) {
        if (comboBox2.getSelectedItem().toString() == "用人单位") {
            label9.setVisible(true);
            textField2.setVisible(true);
        } else {
            label9.setVisible(false);
            textField2.setVisible(false);
        }
        if (comboBox2.getSelectedItem().toString() == "学生") {
            label42.setVisible(true);
            textField34.setVisible(true);
        } else {
            label42.setVisible(false);
            textField34.setVisible(false);
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
        if(textField10.getText()==""){
            JOptionPane.showMessageDialog(EmployersNeed,"必须填写全部信息","注意",JOptionPane.INFORMATION_MESSAGE);
        }
        insertJobData(textField8.getText(),textField9.getText(),Integer.parseInt(textField10.getText()),convertStringToDate(textField11.getText()),getCompanyByAccount(textField1.getText()));
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
    private void button28MousePressed(MouseEvent e) {
        AdminSearch.setVisible(true);
    }

    private void button29MousePressed(MouseEvent e) {
        String departmentName = textField12.getText();
        int departmentId = 0;
        if (!textField13.getText().isEmpty()) {
            departmentId = Integer.parseInt(textField13.getText());
        }
        searchDepartments(departmentName, departmentId);
    }

    private void button30MousePressed(MouseEvent e) {
        String departmentName = textField12.getText();
        String departmentIdText = textField13.getText();

        // 检查院系名称是否为空
        if (departmentName.isEmpty()) {
            // 提示用户输入院系名称
            JOptionPane.showMessageDialog(Admin1, "请输入院系名称", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 检查院系编号是否为空
        int departmentId;
        if (departmentIdText.isEmpty()) {
            // 查询数据库中的院系数量，自动生成新的院系编号
            departmentId = generateNewDepartmentId();
            if (departmentId == -1) {
                // 生成新的院系编号失败
                JOptionPane.showMessageDialog(Admin1, "无法生成新的院系编号", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            // 解析院系编号文本为整数
            try {
                departmentId = Integer.parseInt(departmentIdText);
            } catch (NumberFormatException y) {
                // 院系编号格式错误
                JOptionPane.showMessageDialog(Admin1, "院系编号格式错误", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 检查院系编号是否重复
        if (isDepartmentIdDuplicate(departmentId)) {
            JOptionPane.showMessageDialog(Admin1, "院系编号已存在", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 将新数据写入数据库
        insertDepartmentRecord(departmentId, departmentName);

        // 清空文本框
        textField12.setText("");
        textField13.setText("");

        // 刷新表格或执行其他操作
        // ...
    }

    private void button31MousePressed(MouseEvent e) {
        int selectedRow = table2.getSelectedRow();
        if (selectedRow == -1) {
            // 没有选择任何行
            JOptionPane.showMessageDialog(Admin1, "请选择要删除的行", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int departmentId = (int) table2.getValueAt(selectedRow, 0);
        String departmentName = (String) table2.getValueAt(selectedRow, 1);

        // 确认删除操作
        int option = JOptionPane.showConfirmDialog(Admin1, "确定删除院系 " + departmentName + " 吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // 执行删除操作
            deleteDepartmentRecord(departmentId);

        }
    }

    private void button32MousePressed(MouseEvent e) {
        Register2.setVisible(true);
    }

    private void button33MousePressed(MouseEvent e) {
            String account = textField5.getText();
            String oldPassword = textField14.getText();
            String newPassword = textField16.getText();
            String identity = comboBox3.getSelectedItem().toString();

            if (account.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty() || identity.isEmpty()) {
                JOptionPane.showMessageDialog(Register2, "请输入完整的账号、旧密码、新密码和身份信息", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
            String USERNAME = "root";
            String PASSWORD = "Lzy-200387";

            try {
                // 建立数据库连接
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                // 检查账号、密码和身份是否匹配数据库中的记录
                String query = "SELECT * FROM 账号信息表 WHERE 账号 = ? AND 密码 = ? AND 身份 = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, account);
                statement.setString(2, oldPassword);
                statement.setString(3, identity);
                resultSet = statement.executeQuery();

                // 如果存在匹配的记录，则更新密码
                if (resultSet.next()) {
                    // 更新密码
                    String updateQuery = "UPDATE 账号信息表 SET 密码 = ? WHERE 账号 = ?";
                    statement = connection.prepareStatement(updateQuery);
                    statement.setString(1, newPassword);
                    statement.setString(2, account);
                    statement.executeUpdate();

                    JOptionPane.showMessageDialog(Register2, "密码修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    Register2.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(Register2, "账号信息输入错误", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException y) {
                y.printStackTrace();
            } finally {
                // 关闭连接和资源
                closeConnection(connection, statement, resultSet);
            }
    }

    private void button34MousePressed(MouseEvent e) {
        AdminSearch2.setVisible(true);
    }

    private void button36MousePressed(MouseEvent e) {
        searchStudentInfo(textField17.getText(),textField18.getText(),textField21.getText(),textField22.getText(),textField19.getText(),textField20.getText(),textField23.getText(),textField24.getText(),textField25.getText(),textField50.getText());
    }

    private void button37MousePressed(MouseEvent e) {
        addStudentInfo(textField17.getText(),textField18.getText(),textField21.getText(),textField22.getText(),textField19.getText(),textField20.getText(),textField23.getText(),textField24.getText(),textField25.getText(),textField50.getText());
    }

    private void button35MousePressed(MouseEvent e) {
        int selectedRow = table3.getSelectedRow();
        if (selectedRow == -1) {
            // 没有选择任何行
            JOptionPane.showMessageDialog(Admin1, "请选择要删除的行", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int studentId = (int) table3.getValueAt(selectedRow, 0);
        String studentName = (String) table3.getValueAt(selectedRow, 1);

        // 确认删除操作
        int option = JOptionPane.showConfirmDialog(Admin1, "确定删除学生 " + studentName + " 吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // 执行删除操作
            deleteStudentRecord(studentId);
        }
    }

    private void button40MousePressed(MouseEvent e) {
        searchJobInformation(textField26.getText(),textField27.getText(),textField31.getText(),textField28.getText(),textField29.getText(),textField32.getText(),textField33.getText(),textField30.getText(),textField49.getText());
    }

    private void button38MousePressed(MouseEvent e) {
        AdminSearch3.setVisible(true);
    }

    private void button41MousePressed(MouseEvent e) {
        insertOccupation(textField26.getText(),textField27.getText(),Integer.parseInt(textField31.getText()),textField28.getText(),textField29.getText(),textField32.getText(),Integer.parseInt(textField33.getText()),textField30.getText(),Integer.parseInt(textField49.getText()));
    }

    private void button39MousePressed(MouseEvent e) {
            int selectedRow = table4.getSelectedRow();
            if (selectedRow == -1) {
                // 没有选择任何行
                JOptionPane.showMessageDialog(Admin3, "请选择要删除的行", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int occupationId = (int) table4.getValueAt(selectedRow, 0);
            String occupationName = (String) table4.getValueAt(selectedRow, 1);

            // 确认删除操作
            int option = JOptionPane.showConfirmDialog(Admin3, "确定删除职业 " + occupationName + " 吗？", "确认删除", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // 执行删除操作
                deleteOccupationRecord(occupationId);
            }
        }

    private void button4MousePressed(MouseEvent e) {
        Student.setVisible(false);
        Student1.setVisible(true);
        displayCurrentGraduateStudentInfo(textField1.getText(),textField41,textField35,textField36,textField43,textField37,textField38,textField39,textField40,textField42);
        displayCurrentUserPhoto(getCurrentUserStudentId(textField1.getText()));
        textField41.setEditable(false);
        textField35.setEditable(false);
        textField36.setEditable(false);
        textField37.setEditable(false);
        textField38.setEditable(false);
        textField39.setEditable(false);
        textField40.setEditable(false);
        textField43.setEditable(false);
        textField42.setEditable(false);
    }

    private void button18MousePressed(MouseEvent e) {
        Student1.setVisible(false);
        Student.setVisible(true);
    }

    private void button42MousePressed(MouseEvent e) {
        JOptionPane.showMessageDialog(Student1,"请在联系方式一栏输入修改后的联系方式,按Enter完成修改","提示",JOptionPane.INFORMATION_MESSAGE);
        textField39.setEditable(true);
        textField39.setText("");
        textField39.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // 检测按下的键是否为 Enter 键
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // 获取文本字段的内容
                    String text = textField39.getText();

                    // 调用更新数据库的方法，并传递学号和文本字段内容
                    int studentId = getCurrentUserStudentId(textField1.getText());
                    updateContactInfoInDatabase(studentId, text);
                }
            }
        });
    }

    private void button43MousePressed(MouseEvent e) {
        processJobRegistration(textField44.getText(),textField45.getText());
    }

    private void button3MousePressed(MouseEvent e) {
        Student2.setVisible(true);
        Student.setVisible(false);
    }

    private void button19MousePressed(MouseEvent e) {
        Student2.setVisible(false);
        Student.setVisible(true);
    }

    private void button2MousePressed(MouseEvent e) {
        Student.setVisible(false);
        Student3.setVisible(true);
        loadJobDataToTable(table5);
    }

    private void button20MousePressed(MouseEvent e) {
        Student.setVisible(true);
        Student3.setVisible(false);
    }

    private void button44MousePressed(MouseEvent e) {
        Admin.setVisible(false);
        Admin4.setVisible(true);
        loadMajorDataToTable(table6);
        updateDatabaseRecord(table6);
    }

    private void button45MousePressed(MouseEvent e) {
        Admin.setVisible(true);
        Admin4.setVisible(false);
    }

    private void button48MousePressed(MouseEvent e) {
        queryMajorInformation(table6,textField47,textField46,textField48);
    }

    private void button46MousePressed(MouseEvent e) {
        AdminSearch4.setVisible(true);
    }

    private void button49MousePressed(MouseEvent e) {
        insertMajorInformation(textField47,textField46,textField48);
    }

    private void button47MousePressed(MouseEvent e) {
        int selectedRow = table6.getSelectedRow();
        if (selectedRow == -1) {
            // 没有选择任何行
            JOptionPane.showMessageDialog(Admin4, "请选择要删除的行", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int majorId = (int) table6.getValueAt(selectedRow, 0);
        String majorName = (String) table6.getValueAt(selectedRow, 1);

        // 确认删除操作
        int option = JOptionPane.showConfirmDialog(Admin4, "确定删除专业 " + majorName + " 吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // 执行删除操作
            deleteMajorRecord(majorId);
        }
    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
    }

    private void button6MouseEntered(MouseEvent e) {
        // TODO add your code here
    }

    private void button51MousePressed(MouseEvent e) {
        textField26.setText("");
        textField27.setText("");
        textField30.setText("");
        textField31.setText("");
        textField28.setText("");
        textField29.setText("");
        textField32.setText("");
        textField33.setText("");
        textField49.setText("");
    }

    private void button52MousePressed(MouseEvent e) {
        textField17.setText("");
        textField18.setText("");
        textField21.setText("");
        textField22.setText("");
        textField19.setText("");
        textField20.setText("");
        textField23.setText("");
        textField24.setText("");
        textField25.setText("");
    }

    private void button50MousePressed(MouseEvent e) {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            // 没有选择任何行
            JOptionPane.showMessageDialog(Employers, "请选择要删除的行", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 获取选中行的数据
        int jobId = (int) table1.getValueAt(selectedRow, 0);
        String jobTitle = (String) table1.getValueAt(selectedRow, 1);

        // 确认删除操作
        int option = JOptionPane.showConfirmDialog(Employers, "确定删除职业需求 " + jobTitle + " 吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // 执行删除操作
            deleteJobRecord(jobId);
        }
    }

    private void button53MousePressed(MouseEvent e) {
        showEmploymentStats();
    }

    private void button54MousePressed(MouseEvent e) {
        showEmploymentRatesByMajor();
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
        button32 = new JButton();
        label57 = new JLabel();
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
        button44 = new JButton();
        Employers = new JFrame();
        button8 = new JButton();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        button15 = new JButton();
        button16 = new JButton();
        button24 = new JButton();
        button26 = new JButton();
        button50 = new JButton();
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
        label42 = new JLabel();
        textField34 = new JTextField();
        dialog1 = new JDialog();
        label7 = new JLabel();
        button13 = new JButton();
        Admin1 = new JFrame();
        button17 = new JButton();
        button28 = new JButton();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        button31 = new JButton();
        Admin3 = new JFrame();
        button25 = new JButton();
        scrollPane4 = new JScrollPane();
        table4 = new JTable();
        button38 = new JButton();
        button39 = new JButton();
        button53 = new JButton();
        Student1 = new JFrame();
        button18 = new JButton();
        label43 = new JLabel();
        label44 = new JLabel();
        label45 = new JLabel();
        label46 = new JLabel();
        label47 = new JLabel();
        label48 = new JLabel();
        textField35 = new JTextField();
        textField36 = new JTextField();
        textField37 = new JTextField();
        textField38 = new JTextField();
        textField39 = new JTextField();
        textField40 = new JTextField();
        label49 = new JLabel();
        textField41 = new JTextField();
        label50 = new JLabel();
        textField42 = new JTextField();
        label51 = new JLabel();
        textField43 = new JTextField();
        button42 = new JButton();
        label60 = new JLabel();
        Student2 = new JFrame();
        button19 = new JButton();
        label52 = new JLabel();
        label53 = new JLabel();
        textField44 = new JTextField();
        textField45 = new JTextField();
        button43 = new JButton();
        Student3 = new JFrame();
        button20 = new JButton();
        scrollPane5 = new JScrollPane();
        table5 = new JTable();
        dialog2 = new JDialog();
        label8 = new JLabel();
        button14 = new JButton();
        Admin2 = new JFrame();
        button21 = new JButton();
        scrollPane3 = new JScrollPane();
        table3 = new JTable();
        button34 = new JButton();
        button35 = new JButton();
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
        AdminSearch = new JFrame();
        label19 = new JLabel();
        textField12 = new JTextField();
        button29 = new JButton();
        label20 = new JLabel();
        textField13 = new JTextField();
        button30 = new JButton();
        Register2 = new JFrame();
        label12 = new JLabel();
        textField5 = new JTextField();
        textField14 = new JTextField();
        label21 = new JLabel();
        label22 = new JLabel();
        comboBox3 = new JComboBox<>();
        button33 = new JButton();
        label23 = new JLabel();
        textField15 = new JTextField();
        label24 = new JLabel();
        textField16 = new JTextField();
        AdminSearch2 = new JFrame();
        label25 = new JLabel();
        textField17 = new JTextField();
        button36 = new JButton();
        label26 = new JLabel();
        textField18 = new JTextField();
        button37 = new JButton();
        textField19 = new JTextField();
        textField20 = new JTextField();
        label27 = new JLabel();
        label28 = new JLabel();
        label29 = new JLabel();
        label30 = new JLabel();
        label31 = new JLabel();
        label32 = new JLabel();
        label33 = new JLabel();
        textField21 = new JTextField();
        textField22 = new JTextField();
        textField23 = new JTextField();
        textField24 = new JTextField();
        textField25 = new JTextField();
        button52 = new JButton();
        textField50 = new JTextField();
        label59 = new JLabel();
        AdminSearch3 = new JFrame();
        label34 = new JLabel();
        textField26 = new JTextField();
        button40 = new JButton();
        label35 = new JLabel();
        textField27 = new JTextField();
        button41 = new JButton();
        textField28 = new JTextField();
        textField29 = new JTextField();
        label36 = new JLabel();
        label37 = new JLabel();
        label38 = new JLabel();
        label39 = new JLabel();
        label40 = new JLabel();
        label41 = new JLabel();
        textField30 = new JTextField();
        textField31 = new JTextField();
        textField32 = new JTextField();
        textField33 = new JTextField();
        textField49 = new JTextField();
        label58 = new JLabel();
        button51 = new JButton();
        Admin4 = new JFrame();
        button45 = new JButton();
        scrollPane6 = new JScrollPane();
        table6 = new JTable();
        button46 = new JButton();
        button47 = new JButton();
        button54 = new JButton();
        AdminSearch4 = new JFrame();
        label54 = new JLabel();
        textField46 = new JTextField();
        button48 = new JButton();
        label55 = new JLabel();
        textField47 = new JTextField();
        button49 = new JButton();
        label56 = new JLabel();
        textField48 = new JTextField();

        //======== Login ========
        {
            Login.setTitle("\u767b\u9646");
            Login.setAlwaysOnTop(true);
            Login.setAutoRequestFocus(false);
            Login.setIconImage(new ImageIcon("/Users/linzhengyang/Pictures/ppt\u7d20\u6750/\u5c71\u5927\u56fe\u4e66\u9986.jpeg").getImage());
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

            //---- button32 ----
            button32.setText("\u4fee\u6539\u5bc6\u7801");
            button32.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button32MousePressed(e);
                }
            });

            //---- label57 ----
            label57.setText("\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf");
            label57.setFont(label57.getFont().deriveFont(label57.getFont().getSize() + 30f));

            GroupLayout LoginContentPaneLayout = new GroupLayout(LoginContentPane);
            LoginContentPane.setLayout(LoginContentPaneLayout);
            LoginContentPaneLayout.setHorizontalGroup(
                LoginContentPaneLayout.createParallelGroup()
                    .addGroup(LoginContentPaneLayout.createSequentialGroup()
                        .addGroup(LoginContentPaneLayout.createParallelGroup()
                            .addGroup(LoginContentPaneLayout.createSequentialGroup()
                                .addGap(477, 477, 477)
                                .addGroup(LoginContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addGroup(LoginContentPaneLayout.createSequentialGroup()
                                        .addGroup(LoginContentPaneLayout.createParallelGroup()
                                            .addComponent(label1)
                                            .addComponent(label2))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(LoginContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                            .addComponent(textField1)
                                            .addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(LoginContentPaneLayout.createSequentialGroup()
                                        .addComponent(label3)
                                        .addGap(18, 18, 18)
                                        .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addComponent(button1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(button11, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(button32, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE)))
                            .addGroup(LoginContentPaneLayout.createSequentialGroup()
                                .addGap(359, 359, 359)
                                .addComponent(label57)))
                        .addContainerGap(389, Short.MAX_VALUE))
            );
            LoginContentPaneLayout.setVerticalGroup(
                LoginContentPaneLayout.createParallelGroup()
                    .addGroup(LoginContentPaneLayout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(label57)
                        .addGap(129, 129, 129)
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
                        .addGap(18, 18, 18)
                        .addComponent(button32)
                        .addContainerGap(209, Short.MAX_VALUE))
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
            button2.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/person.2.crop.square.stack@2x.png"));
            button2.setHorizontalTextPosition(SwingConstants.CENTER);
            button2.setVerticalTextPosition(SwingConstants.BOTTOM);
            button2.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button2MousePressed(e);
                }
            });

            //---- button3 ----
            button3.setText("\u5c31\u4e1a\u767b\u8bb0");
            button3.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/square.and.pencil@2x.png"));
            button3.setHorizontalTextPosition(SwingConstants.CENTER);
            button3.setVerticalTextPosition(SwingConstants.BOTTOM);
            button3.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button3MousePressed(e);
                }
            });

            //---- button4 ----
            button4.setText("\u4e2a\u4eba\u4fe1\u606f");
            button4.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/person.text.rectangle@2x.png"));
            button4.setHorizontalTextPosition(SwingConstants.CENTER);
            button4.setVerticalTextPosition(SwingConstants.BOTTOM);
            button4.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button4MousePressed(e);
                }
            });

            //---- button10 ----
            button10.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/door.right.hand.open@2x.png"));
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
                    .addGroup(GroupLayout.Alignment.TRAILING, StudentContentPaneLayout.createSequentialGroup()
                        .addGap(158, 158, 158)
                        .addComponent(button4, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                        .addGap(285, 285, 285)
                        .addComponent(button3, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 335, Short.MAX_VALUE)
                        .addComponent(button2, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
                        .addGap(101, 101, 101))
                    .addGroup(StudentContentPaneLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(button10, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(1096, Short.MAX_VALUE))
            );
            StudentContentPaneLayout.setVerticalGroup(
                StudentContentPaneLayout.createParallelGroup()
                    .addGroup(StudentContentPaneLayout.createSequentialGroup()
                        .addContainerGap(308, Short.MAX_VALUE)
                        .addGroup(StudentContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(button2, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                            .addComponent(button3, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
                        .addGap(349, 349, 349))
                    .addGroup(StudentContentPaneLayout.createSequentialGroup()
                        .addGap(310, 310, 310)
                        .addComponent(button4, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 257, Short.MAX_VALUE)
                        .addComponent(button10, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
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
            button5.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/building.columns@2x.png"));
            button5.setText("\u9662\u7cfb\u4fe1\u606f\u7ba1\u7406");
            button5.setHorizontalTextPosition(SwingConstants.CENTER);
            button5.setVerticalTextPosition(SwingConstants.BOTTOM);
            button5.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button5MousePressed(e);
                }
            });

            //---- button6 ----
            button6.setText("\u6bd5\u4e1a\u751f\u4fe1\u606f\u7ba1\u7406");
            button6.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/person.text.rectangle@2x.png"));
            button6.setHorizontalTextPosition(SwingConstants.CENTER);
            button6.setVerticalTextPosition(SwingConstants.BOTTOM);
            button6.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button6MouseEntered(e);
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    button6MousePressed(e);
                }
            });

            //---- button7 ----
            button7.setText("\u62db\u8058\u4fe1\u606f\u7ba1\u7406");
            button7.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/person.2.crop.square.stack@2x.png"));
            button7.setVerticalTextPosition(SwingConstants.BOTTOM);
            button7.setHorizontalTextPosition(SwingConstants.CENTER);
            button7.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button7MousePressed(e);
                }
            });

            //---- button9 ----
            button9.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/door.right.hand.open@2x.png"));
            button9.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button9MouseClicked(e);
                }
            });

            //---- button44 ----
            button44.setText("\u4e13\u4e1a\u4fe1\u606f\u7ba1\u7406");
            button44.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/display@2x.png"));
            button44.setHorizontalTextPosition(SwingConstants.CENTER);
            button44.setVerticalTextPosition(SwingConstants.BOTTOM);
            button44.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button44MousePressed(e);
                }
            });

            GroupLayout AdminContentPaneLayout = new GroupLayout(AdminContentPane);
            AdminContentPane.setLayout(AdminContentPaneLayout);
            AdminContentPaneLayout.setHorizontalGroup(
                AdminContentPaneLayout.createParallelGroup()
                    .addGroup(AdminContentPaneLayout.createSequentialGroup()
                        .addGroup(AdminContentPaneLayout.createParallelGroup()
                            .addGroup(AdminContentPaneLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(button9, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE))
                            .addGroup(AdminContentPaneLayout.createSequentialGroup()
                                .addGap(133, 133, 133)
                                .addComponent(button7, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
                                .addGap(138, 138, 138)
                                .addComponent(button5, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
                                .addGap(104, 104, 104)
                                .addComponent(button44, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
                                .addGap(122, 122, 122)
                                .addComponent(button6, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(175, Short.MAX_VALUE))
            );
            AdminContentPaneLayout.setVerticalGroup(
                AdminContentPaneLayout.createParallelGroup()
                    .addGroup(AdminContentPaneLayout.createSequentialGroup()
                        .addContainerGap(302, Short.MAX_VALUE)
                        .addGroup(AdminContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(button5, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
                            .addComponent(button7, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
                            .addComponent(button44, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
                            .addComponent(button6, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))
                        .addGap(251, 251, 251)
                        .addComponent(button9, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
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
            button8.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/door.right.hand.open@2x.png"));
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
            button15.setText("\u5355\u4e2a");
            button15.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/magnifyingglass@2x.png"));
            button15.setHorizontalTextPosition(SwingConstants.CENTER);
            button15.setVerticalTextPosition(SwingConstants.BOTTOM);
            button15.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button15MousePressed(e);
                }
            });

            //---- button16 ----
            button16.setText("\u4fee\u6539");
            button16.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/square.and.pencil@2x.png"));
            button16.setHorizontalTextPosition(SwingConstants.CENTER);
            button16.setVerticalTextPosition(SwingConstants.BOTTOM);
            button16.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button16MousePressed(e);
                }
            });

            //---- button24 ----
            button24.setText("\u5168\u90e8");
            button24.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/magnifyingglass@2x.png"));
            button24.setHorizontalTextPosition(SwingConstants.CENTER);
            button24.setVerticalTextPosition(SwingConstants.BOTTOM);
            button24.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button24MousePressed(e);
                }
            });

            //---- button26 ----
            button26.setText("\u53d1\u5e03\u9700\u6c42");
            button26.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/icloud.and.arrow.up@2x.png"));
            button26.setHorizontalTextPosition(SwingConstants.CENTER);
            button26.setVerticalTextPosition(SwingConstants.BOTTOM);
            button26.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button26MousePressed(e);
                }
            });

            //---- button50 ----
            button50.setText("\u5220\u9664\u9700\u6c42");
            button50.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/delete.backward@2x.png"));
            button50.setHorizontalTextPosition(SwingConstants.CENTER);
            button50.setVerticalTextPosition(SwingConstants.BOTTOM);
            button50.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button50MousePressed(e);
                }
            });

            GroupLayout EmployersContentPaneLayout = new GroupLayout(EmployersContentPane);
            EmployersContentPane.setLayout(EmployersContentPaneLayout);
            EmployersContentPaneLayout.setHorizontalGroup(
                EmployersContentPaneLayout.createParallelGroup()
                    .addGroup(EmployersContentPaneLayout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addGroup(EmployersContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addGroup(EmployersContentPaneLayout.createSequentialGroup()
                                .addComponent(button8, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
                                .addGap(84, 84, 84)
                                .addComponent(button24, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
                                .addGap(101, 101, 101)
                                .addComponent(button15, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
                                .addGap(96, 96, 96)
                                .addComponent(button16, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(button50, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
                                .addGap(84, 84, 84)
                                .addComponent(button26, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE))
                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 993, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(98, Short.MAX_VALUE))
            );
            EmployersContentPaneLayout.setVerticalGroup(
                EmployersContentPaneLayout.createParallelGroup()
                    .addGroup(GroupLayout.Alignment.TRAILING, EmployersContentPaneLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 599, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addGroup(EmployersContentPaneLayout.createParallelGroup()
                            .addGroup(EmployersContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addGroup(EmployersContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(button15, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button16, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button24, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
                                .addGroup(EmployersContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(button50, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button26, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)))
                            .addComponent(button8, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26))
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
                "\u7ba1\u7406\u5458",
                "\u5b66\u751f",
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

            //---- label42 ----
            label42.setText("\u5b66\u53f7");
            label42.setVisible(false);

            //---- textField34 ----
            textField34.setVisible(false);

            GroupLayout RegisterContentPaneLayout = new GroupLayout(RegisterContentPane);
            RegisterContentPane.setLayout(RegisterContentPaneLayout);
            RegisterContentPaneLayout.setHorizontalGroup(
                RegisterContentPaneLayout.createParallelGroup()
                    .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                        .addGroup(RegisterContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(label9)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textField2, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
                            .addGroup(GroupLayout.Alignment.LEADING, RegisterContentPaneLayout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addGroup(RegisterContentPaneLayout.createParallelGroup()
                                    .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                                        .addComponent(label4)
                                        .addGap(12, 12, 12)
                                        .addComponent(textField3, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
                                    .addComponent(button12, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE)
                                    .addGroup(RegisterContentPaneLayout.createSequentialGroup()
                                        .addGroup(RegisterContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                            .addComponent(label5)
                                            .addComponent(label6)
                                            .addComponent(label42))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(RegisterContentPaneLayout.createParallelGroup()
                                            .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(textField4, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(textField34, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))))
                                .addGap(6, 6, 6)))
                        .addContainerGap(66, Short.MAX_VALUE))
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
                            .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label6))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(RegisterContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label42)
                            .addComponent(textField34, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                        .addGroup(RegisterContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label9))
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
            button17.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/door.right.hand.open@2x.png"));
            button17.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button17MousePressed(e);
                }
            });

            //---- button28 ----
            button28.setText("\u67e5\u8be2/\u589e\u52a0\u9662\u7cfb");
            button28.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/magnifyingglass@2x.png"));
            button28.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button28MousePressed(e);
                }
            });

            //======== scrollPane2 ========
            {
                scrollPane2.setViewportView(table2);
            }

            //---- button31 ----
            button31.setText("\u5220\u9664\u9662\u7cfb");
            button31.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/delete.backward@2x.png"));
            button31.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button31MousePressed(e);
                }
            });

            GroupLayout Admin1ContentPaneLayout = new GroupLayout(Admin1ContentPane);
            Admin1ContentPane.setLayout(Admin1ContentPaneLayout);
            Admin1ContentPaneLayout.setHorizontalGroup(
                Admin1ContentPaneLayout.createParallelGroup()
                    .addGroup(Admin1ContentPaneLayout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addGroup(Admin1ContentPaneLayout.createParallelGroup()
                            .addGroup(Admin1ContentPaneLayout.createSequentialGroup()
                                .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 1027, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(82, Short.MAX_VALUE))
                            .addGroup(Admin1ContentPaneLayout.createSequentialGroup()
                                .addComponent(button17, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 345, Short.MAX_VALUE)
                                .addComponent(button28)
                                .addGap(244, 244, 244)
                                .addComponent(button31, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
                                .addGap(107, 107, 107))))
            );
            Admin1ContentPaneLayout.setVerticalGroup(
                Admin1ContentPaneLayout.createParallelGroup()
                    .addGroup(Admin1ContentPaneLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 603, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(Admin1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(button17, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button28, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(button31, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE))
                        .addGap(22, 22, 22))
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
            button25.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/door.right.hand.open@2x.png"));
            button25.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button25MousePressed(e);
                }
            });

            //======== scrollPane4 ========
            {
                scrollPane4.setViewportView(table4);
            }

            //---- button38 ----
            button38.setText("\u67e5\u8be2/\u589e\u52a0\u62db\u8058\u4fe1\u606f");
            button38.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/magnifyingglass@2x.png"));
            button38.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button38MousePressed(e);
                }
            });

            //---- button39 ----
            button39.setText("\u5220\u9664\u62db\u8058\u4fe1\u606f");
            button39.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/delete.backward@2x.png"));
            button39.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button39MousePressed(e);
                }
            });

            //---- button53 ----
            button53.setText("\u67e5\u8be2\u5c31\u4e1a\u7387");
            button53.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/magnifyingglass@2x.png"));
            button53.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button53MousePressed(e);
                }
            });

            GroupLayout Admin3ContentPaneLayout = new GroupLayout(Admin3ContentPane);
            Admin3ContentPane.setLayout(Admin3ContentPaneLayout);
            Admin3ContentPaneLayout.setHorizontalGroup(
                Admin3ContentPaneLayout.createParallelGroup()
                    .addGroup(Admin3ContentPaneLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(Admin3ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(scrollPane4, GroupLayout.PREFERRED_SIZE, 1050, GroupLayout.PREFERRED_SIZE)
                            .addGroup(Admin3ContentPaneLayout.createSequentialGroup()
                                .addComponent(button25, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
                                .addGap(142, 142, 142)
                                .addComponent(button38, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(button53, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE)
                                .addGap(143, 143, 143)
                                .addComponent(button39)))
                        .addContainerGap(67, Short.MAX_VALUE))
            );
            Admin3ContentPaneLayout.setVerticalGroup(
                Admin3ContentPaneLayout.createParallelGroup()
                    .addGroup(Admin3ContentPaneLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(scrollPane4, GroupLayout.PREFERRED_SIZE, 602, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(Admin3ContentPaneLayout.createParallelGroup()
                            .addComponent(button25, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button39, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button53, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(GroupLayout.Alignment.TRAILING, Admin3ContentPaneLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(button38, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
            );
            Admin3.pack();
            Admin3.setLocationRelativeTo(Admin3.getOwner());
        }

        //======== Student1 ========
        {
            Student1.setTitle("\u4e2a\u4eba\u4fe1\u606f\u67e5\u8be2");
            Student1.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    AdminWindowClosing(e);
                }
            });
            var Student1ContentPane = Student1.getContentPane();

            //---- button18 ----
            button18.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/door.right.hand.open@2x.png"));
            button18.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button18MousePressed(e);
                }
            });

            //---- label43 ----
            label43.setText("\u59d3\u540d");

            //---- label44 ----
            label44.setText("\u6027\u522b");

            //---- label45 ----
            label45.setText("\u6240\u5728\u9662\u7cfb");

            //---- label46 ----
            label46.setText("\u6240\u5b66\u4e13\u4e1a");

            //---- label47 ----
            label47.setText("\u8054\u7cfb\u65b9\u5f0f");

            //---- label48 ----
            label48.setText("\u5c31\u4e1a\u72b6\u6001");

            //---- label49 ----
            label49.setText("\u5b66\u53f7");

            //---- label50 ----
            label50.setText("\u804c\u4e1a");

            //---- label51 ----
            label51.setText("\u51fa\u751f\u65e5\u671f");

            //---- button42 ----
            button42.setText("\u4fee\u6539\u8054\u7cfb\u65b9\u5f0f");
            button42.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button42MousePressed(e);
                }
            });

            //---- label60 ----
            label60.setIcon(null);

            GroupLayout Student1ContentPaneLayout = new GroupLayout(Student1ContentPane);
            Student1ContentPane.setLayout(Student1ContentPaneLayout);
            Student1ContentPaneLayout.setHorizontalGroup(
                Student1ContentPaneLayout.createParallelGroup()
                    .addGroup(Student1ContentPaneLayout.createSequentialGroup()
                        .addGroup(Student1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addGroup(GroupLayout.Alignment.LEADING, Student1ContentPaneLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(button18, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
                            .addGroup(Student1ContentPaneLayout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addGroup(Student1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addGroup(Student1ContentPaneLayout.createSequentialGroup()
                                        .addComponent(label60)
                                        .addGap(44, 44, 44)
                                        .addGroup(Student1ContentPaneLayout.createParallelGroup()
                                            .addGroup(GroupLayout.Alignment.TRAILING, Student1ContentPaneLayout.createParallelGroup()
                                                .addComponent(label49)
                                                .addComponent(label43)
                                                .addComponent(label44))
                                            .addComponent(label51, GroupLayout.Alignment.TRAILING)))
                                    .addComponent(label50))))
                        .addGap(39, 39, 39)
                        .addGroup(Student1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(textField41, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                            .addComponent(textField35, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                            .addComponent(textField36, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                            .addComponent(textField43, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                            .addComponent(textField42, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 215, Short.MAX_VALUE)
                        .addGroup(Student1ContentPaneLayout.createParallelGroup()
                            .addGroup(Student1ContentPaneLayout.createSequentialGroup()
                                .addGroup(Student1ContentPaneLayout.createParallelGroup()
                                    .addGroup(Student1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(label46)
                                        .addComponent(label47)
                                        .addComponent(label48))
                                    .addComponent(label45))
                                .addGap(36, 36, 36)
                                .addGroup(Student1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(textField39, GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                    .addComponent(textField38, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                    .addComponent(textField37, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                    .addComponent(textField40, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)))
                            .addComponent(button42, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
                        .addGap(234, 234, 234))
            );
            Student1ContentPaneLayout.setVerticalGroup(
                Student1ContentPaneLayout.createParallelGroup()
                    .addGroup(Student1ContentPaneLayout.createSequentialGroup()
                        .addGap(265, 265, 265)
                        .addGroup(Student1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label45)
                            .addComponent(textField37, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(textField41, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label49))
                        .addGap(20, 20, 20)
                        .addGroup(Student1ContentPaneLayout.createParallelGroup()
                            .addComponent(label60)
                            .addGroup(Student1ContentPaneLayout.createSequentialGroup()
                                .addGroup(Student1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label46)
                                    .addComponent(textField38, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label43)
                                    .addComponent(textField35, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(Student1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label47)
                                    .addComponent(textField39, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label44)
                                    .addComponent(textField36, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(43, 43, 43)
                                .addGroup(Student1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(textField43, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label51)
                                    .addComponent(textField40, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label48))
                                .addGap(53, 53, 53)
                                .addGroup(Student1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(textField42, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label50)
                                    .addComponent(button42))))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
                        .addComponent(button18, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
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
            button19.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/door.right.hand.open@2x.png"));
            button19.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button19MousePressed(e);
                }
            });

            //---- label52 ----
            label52.setText("\u5c31\u4e1a\u516c\u53f8");

            //---- label53 ----
            label53.setText("\u804c\u4e1a\u540d\u79f0");

            //---- button43 ----
            button43.setText("\u767b\u8bb0");
            button43.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/square.and.pencil@2x.png"));
            button43.setHorizontalTextPosition(SwingConstants.CENTER);
            button43.setVerticalTextPosition(SwingConstants.BOTTOM);
            button43.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button43MousePressed(e);
                }
            });

            GroupLayout Student2ContentPaneLayout = new GroupLayout(Student2ContentPane);
            Student2ContentPane.setLayout(Student2ContentPaneLayout);
            Student2ContentPaneLayout.setHorizontalGroup(
                Student2ContentPaneLayout.createParallelGroup()
                    .addGroup(Student2ContentPaneLayout.createSequentialGroup()
                        .addGroup(Student2ContentPaneLayout.createParallelGroup()
                            .addGroup(Student2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                .addGroup(GroupLayout.Alignment.LEADING, Student2ContentPaneLayout.createSequentialGroup()
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(label53)
                                    .addGap(18, 18, 18)
                                    .addComponent(textField45, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE))
                                .addGroup(GroupLayout.Alignment.LEADING, Student2ContentPaneLayout.createSequentialGroup()
                                    .addGap(169, 169, 169)
                                    .addComponent(label52)
                                    .addGap(18, 18, 18)
                                    .addComponent(textField44, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)))
                            .addGroup(Student2ContentPaneLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(button19, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
                                .addGap(135, 135, 135)
                                .addComponent(button43, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(184, Short.MAX_VALUE))
            );
            Student2ContentPaneLayout.setVerticalGroup(
                Student2ContentPaneLayout.createParallelGroup()
                    .addGroup(Student2ContentPaneLayout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addGroup(Student2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField44, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label52))
                        .addGap(69, 69, 69)
                        .addGroup(Student2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField45, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label53))
                        .addGap(38, 38, 38)
                        .addComponent(button43, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(62, Short.MAX_VALUE))
                    .addGroup(GroupLayout.Alignment.TRAILING, Student2ContentPaneLayout.createSequentialGroup()
                        .addContainerGap(308, Short.MAX_VALUE)
                        .addComponent(button19, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
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
            button20.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/door.right.hand.open@2x.png"));
            button20.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button20MousePressed(e);
                }
            });

            //======== scrollPane5 ========
            {
                scrollPane5.setViewportView(table5);
            }

            GroupLayout Student3ContentPaneLayout = new GroupLayout(Student3ContentPane);
            Student3ContentPane.setLayout(Student3ContentPaneLayout);
            Student3ContentPaneLayout.setHorizontalGroup(
                Student3ContentPaneLayout.createParallelGroup()
                    .addGroup(Student3ContentPaneLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(Student3ContentPaneLayout.createParallelGroup()
                            .addComponent(scrollPane5, GroupLayout.PREFERRED_SIZE, 1157, GroupLayout.PREFERRED_SIZE)
                            .addComponent(button20, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(28, Short.MAX_VALUE))
            );
            Student3ContentPaneLayout.setVerticalGroup(
                Student3ContentPaneLayout.createParallelGroup()
                    .addGroup(Student3ContentPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scrollPane5, GroupLayout.PREFERRED_SIZE, 669, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(button20, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
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
            button21.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/door.right.hand.open@2x.png"));
            button21.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button21MousePressed(e);
                }
            });

            //======== scrollPane3 ========
            {
                scrollPane3.setViewportView(table3);
            }

            //---- button34 ----
            button34.setText("\u67e5\u8be2/\u589e\u52a0\u6bd5\u4e1a\u751f");
            button34.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/magnifyingglass@2x.png"));
            button34.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button34MousePressed(e);
                }
            });

            //---- button35 ----
            button35.setText("\u5220\u9664\u6bd5\u4e1a\u751f");
            button35.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/delete.backward@2x.png"));
            button35.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button35MousePressed(e);
                }
            });

            GroupLayout Admin2ContentPaneLayout = new GroupLayout(Admin2ContentPane);
            Admin2ContentPane.setLayout(Admin2ContentPaneLayout);
            Admin2ContentPaneLayout.setHorizontalGroup(
                Admin2ContentPaneLayout.createParallelGroup()
                    .addGroup(Admin2ContentPaneLayout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addGroup(Admin2ContentPaneLayout.createParallelGroup()
                            .addGroup(Admin2ContentPaneLayout.createSequentialGroup()
                                .addComponent(scrollPane3, GroupLayout.PREFERRED_SIZE, 1045, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(75, Short.MAX_VALUE))
                            .addGroup(Admin2ContentPaneLayout.createSequentialGroup()
                                .addComponent(button21, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
                                .addGap(257, 257, 257)
                                .addComponent(button34)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 333, Short.MAX_VALUE)
                                .addComponent(button35)
                                .addGap(122, 122, 122))))
            );
            Admin2ContentPaneLayout.setVerticalGroup(
                Admin2ContentPaneLayout.createParallelGroup()
                    .addGroup(Admin2ContentPaneLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(scrollPane3, GroupLayout.PREFERRED_SIZE, 585, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addGroup(Admin2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(button35, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
                            .addComponent(button34, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
                            .addComponent(button21, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15))
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
            button23.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/magnifyingglass@2x.png"));
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
                        .addGap(73, 73, 73)
                        .addComponent(button23, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))
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
            button27.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/icloud.and.arrow.up@2x.png"));
            button27.setHorizontalTextPosition(SwingConstants.CENTER);
            button27.setVerticalTextPosition(SwingConstants.BOTTOM);
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
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                                .addComponent(button27, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52))
                            .addGroup(EmployersNeedContentPaneLayout.createSequentialGroup()
                                .addGap(0, 24, Short.MAX_VALUE)
                                .addComponent(label18)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textField11, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                .addGap(226, 226, 226))))
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
                                    .addComponent(textField10, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                            .addGroup(EmployersNeedContentPaneLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(button27, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)))
                        .addGap(31, 31, 31)
                        .addGroup(EmployersNeedContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label18)
                            .addComponent(textField11, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(24, Short.MAX_VALUE))
            );
            EmployersNeed.pack();
            EmployersNeed.setLocationRelativeTo(EmployersNeed.getOwner());
        }

        //======== AdminSearch ========
        {
            AdminSearch.setTitle("\u67e5\u8be2/\u589e\u52a0\u9662\u7cfb");
            AdminSearch.setAlwaysOnTop(true);
            var AdminSearchContentPane = AdminSearch.getContentPane();

            //---- label19 ----
            label19.setText("\u9662\u7cfb\u540d\u79f0");

            //---- button29 ----
            button29.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/magnifyingglass@2x.png"));
            button29.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button29MousePressed(e);
                }
            });

            //---- label20 ----
            label20.setText("\u9662\u7cfb\u7f16\u53f7");

            //---- button30 ----
            button30.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/plus.square@2x.png"));
            button30.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button30MousePressed(e);
                }
            });

            GroupLayout AdminSearchContentPaneLayout = new GroupLayout(AdminSearchContentPane);
            AdminSearchContentPane.setLayout(AdminSearchContentPaneLayout);
            AdminSearchContentPaneLayout.setHorizontalGroup(
                AdminSearchContentPaneLayout.createParallelGroup()
                    .addGroup(AdminSearchContentPaneLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(AdminSearchContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(label19)
                            .addComponent(label20))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(AdminSearchContentPaneLayout.createParallelGroup()
                            .addComponent(textField12)
                            .addGroup(AdminSearchContentPaneLayout.createSequentialGroup()
                                .addComponent(textField13, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(90, 90, 90)
                        .addGroup(AdminSearchContentPaneLayout.createParallelGroup()
                            .addComponent(button29, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
                            .addComponent(button30, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40))
            );
            AdminSearchContentPaneLayout.setVerticalGroup(
                AdminSearchContentPaneLayout.createParallelGroup()
                    .addGroup(AdminSearchContentPaneLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(AdminSearchContentPaneLayout.createParallelGroup()
                            .addGroup(AdminSearchContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label19)
                                .addComponent(textField12, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addComponent(button29, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE))
                        .addGroup(AdminSearchContentPaneLayout.createParallelGroup()
                            .addGroup(AdminSearchContentPaneLayout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addGroup(AdminSearchContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(textField13, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label20)))
                            .addGroup(AdminSearchContentPaneLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(button30, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(78, Short.MAX_VALUE))
            );
            AdminSearch.pack();
            AdminSearch.setLocationRelativeTo(AdminSearch.getOwner());
        }

        //======== Register2 ========
        {
            Register2.setTitle("\u4fee\u6539\u5bc6\u7801");
            Register2.setAlwaysOnTop(true);
            var Register2ContentPane = Register2.getContentPane();

            //---- label12 ----
            label12.setText("\u8d26\u53f7");

            //---- label21 ----
            label21.setText("\u5bc6\u7801");

            //---- label22 ----
            label22.setText("\u8eab\u4efd");

            //---- comboBox3 ----
            comboBox3.setModel(new DefaultComboBoxModel<>(new String[] {
                "\u5b66\u751f",
                "\u7ba1\u7406\u5458",
                "\u7528\u4eba\u5355\u4f4d"
            }));
            comboBox3.addActionListener(e -> comboBox2(e));

            //---- button33 ----
            button33.setText("\u4fee\u6539");
            button33.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button33MousePressed(e);
                }
            });

            //---- label23 ----
            label23.setText("\u516c\u53f8\u540d\u79f0");
            label23.setVisible(false);

            //---- textField15 ----
            textField15.setVisible(false);

            //---- label24 ----
            label24.setText("\u65b0\u5bc6\u7801");

            GroupLayout Register2ContentPaneLayout = new GroupLayout(Register2ContentPane);
            Register2ContentPane.setLayout(Register2ContentPaneLayout);
            Register2ContentPaneLayout.setHorizontalGroup(
                Register2ContentPaneLayout.createParallelGroup()
                    .addGroup(Register2ContentPaneLayout.createSequentialGroup()
                        .addGroup(Register2ContentPaneLayout.createParallelGroup()
                            .addGroup(Register2ContentPaneLayout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(label23)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textField15, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
                            .addGroup(Register2ContentPaneLayout.createSequentialGroup()
                                .addGap(73, 73, 73)
                                .addGroup(Register2ContentPaneLayout.createParallelGroup()
                                    .addGroup(Register2ContentPaneLayout.createSequentialGroup()
                                        .addComponent(label12)
                                        .addGap(12, 12, 12)
                                        .addComponent(textField5, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
                                    .addComponent(button33, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE)
                                    .addGroup(Register2ContentPaneLayout.createSequentialGroup()
                                        .addComponent(label21)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(textField14, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(Register2ContentPaneLayout.createSequentialGroup()
                                        .addComponent(label22)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                            .addGroup(GroupLayout.Alignment.TRAILING, Register2ContentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(label24)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textField16, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(59, Short.MAX_VALUE))
            );
            Register2ContentPaneLayout.setVerticalGroup(
                Register2ContentPaneLayout.createParallelGroup()
                    .addGroup(Register2ContentPaneLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(Register2ContentPaneLayout.createParallelGroup()
                            .addGroup(Register2ContentPaneLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(label12))
                            .addComponent(textField5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(Register2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label21)
                            .addComponent(textField14, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addGroup(Register2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField16, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label24))
                        .addGap(30, 30, 30)
                        .addGroup(Register2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label23)
                            .addComponent(textField15, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Register2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label22)
                            .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(button33)
                        .addGap(40, 40, 40))
            );
            Register2.pack();
            Register2.setLocationRelativeTo(Register2.getOwner());
        }

        //======== AdminSearch2 ========
        {
            AdminSearch2.setTitle("\u67e5\u8be2/\u589e\u52a0\u6bd5\u4e1a\u751f\u4fe1\u606f");
            AdminSearch2.setAlwaysOnTop(true);
            var AdminSearch2ContentPane = AdminSearch2.getContentPane();

            //---- label25 ----
            label25.setText("\u5b66\u53f7");

            //---- button36 ----
            button36.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/magnifyingglass@2x.png"));
            button36.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button36MousePressed(e);
                }
            });

            //---- label26 ----
            label26.setText("\u59d3\u540d");

            //---- button37 ----
            button37.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/plus.square@2x.png"));
            button37.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button37MousePressed(e);
                }
            });

            //---- label27 ----
            label27.setText("\u6240\u5728\u9662\u7cfb");

            //---- label28 ----
            label28.setText("\u6240\u5b66\u4e13\u4e1a");

            //---- label29 ----
            label29.setText("\u6027\u522b");

            //---- label30 ----
            label30.setText("\u51fa\u751f\u65e5\u671f");

            //---- label31 ----
            label31.setText("\u8054\u7cfb\u65b9\u5f0f");

            //---- label32 ----
            label32.setText("\u5c31\u4e1a\u72b6\u6001");

            //---- label33 ----
            label33.setText("\u804c\u4e1a");

            //---- button52 ----
            button52.setText("\u91cd\u7f6e");
            button52.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button52MousePressed(e);
                }
            });

            //---- label59 ----
            label59.setText("\u804c\u4e1a\u7f16\u53f7");

            GroupLayout AdminSearch2ContentPaneLayout = new GroupLayout(AdminSearch2ContentPane);
            AdminSearch2ContentPane.setLayout(AdminSearch2ContentPaneLayout);
            AdminSearch2ContentPaneLayout.setHorizontalGroup(
                AdminSearch2ContentPaneLayout.createParallelGroup()
                    .addGroup(AdminSearch2ContentPaneLayout.createSequentialGroup()
                        .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup()
                            .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup()
                                .addGroup(AdminSearch2ContentPaneLayout.createSequentialGroup()
                                    .addGap(190, 190, 190)
                                    .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(label32)
                                        .addComponent(label31)
                                        .addComponent(label27)
                                        .addComponent(label28)
                                        .addComponent(label25)
                                        .addComponent(label30)))
                                .addGroup(GroupLayout.Alignment.TRAILING, AdminSearch2ContentPaneLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(label26)))
                            .addGroup(GroupLayout.Alignment.TRAILING, AdminSearch2ContentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup()
                                    .addComponent(label29, GroupLayout.Alignment.TRAILING)
                                    .addComponent(label33, GroupLayout.Alignment.TRAILING)
                                    .addComponent(label59, GroupLayout.Alignment.TRAILING))))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup()
                            .addGroup(AdminSearch2ContentPaneLayout.createSequentialGroup()
                                .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup()
                                    .addComponent(textField23, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textField24, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textField25, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
                                    .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(textField20)
                                        .addComponent(textField19)
                                        .addComponent(textField17, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField18))
                                    .addComponent(textField21, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textField22, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE))
                                .addGap(87, 87, 87)
                                .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup()
                                    .addComponent(button37, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button36, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button52, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)))
                            .addComponent(textField50, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(139, Short.MAX_VALUE))
            );
            AdminSearch2ContentPaneLayout.setVerticalGroup(
                AdminSearch2ContentPaneLayout.createParallelGroup()
                    .addGroup(AdminSearch2ContentPaneLayout.createSequentialGroup()
                        .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addGroup(AdminSearch2ContentPaneLayout.createSequentialGroup()
                                .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup()
                                    .addGroup(AdminSearch2ContentPaneLayout.createSequentialGroup()
                                        .addGap(109, 109, 109)
                                        .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(textField18, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label26))
                                        .addGap(27, 27, 27)
                                        .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(textField21, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label29))
                                        .addGap(17, 17, 17)
                                        .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(textField22, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label30))
                                        .addGap(22, 22, 22)
                                        .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(textField19, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label27)))
                                    .addGroup(AdminSearch2ContentPaneLayout.createSequentialGroup()
                                        .addGap(51, 51, 51)
                                        .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label25)
                                            .addComponent(textField17, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                                .addGap(27, 27, 27)
                                .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(textField20, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label28))
                                .addGap(28, 28, 28)
                                .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label31)
                                    .addComponent(textField23, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(19, 19, 19)
                                .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label32)
                                    .addComponent(textField24, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                            .addGroup(AdminSearch2ContentPaneLayout.createSequentialGroup()
                                .addGap(81, 81, 81)
                                .addComponent(button36, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(button37, GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE)))
                        .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup()
                            .addGroup(AdminSearch2ContentPaneLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(textField25, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label33)))
                            .addGroup(AdminSearch2ContentPaneLayout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(button52)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(AdminSearch2ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField50, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label59))
                        .addContainerGap(19, Short.MAX_VALUE))
            );
            AdminSearch2.pack();
            AdminSearch2.setLocationRelativeTo(AdminSearch2.getOwner());
        }

        //======== AdminSearch3 ========
        {
            AdminSearch3.setTitle("\u67e5\u8be2/\u589e\u52a0\u62db\u8058\u4fe1\u606f");
            AdminSearch3.setAlwaysOnTop(true);
            var AdminSearch3ContentPane = AdminSearch3.getContentPane();

            //---- label34 ----
            label34.setText("\u804c\u4e1a\u7f16\u53f7");

            //---- button40 ----
            button40.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/magnifyingglass@2x.png"));
            button40.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button40MousePressed(e);
                }
            });

            //---- label35 ----
            label35.setText("\u804c\u4e1a\u540d\u79f0");

            //---- button41 ----
            button41.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/plus.square@2x.png"));
            button41.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button41MousePressed(e);
                }
            });

            //---- label36 ----
            label36.setText("\u7528\u4eba\u5355\u4f4d");

            //---- label37 ----
            label37.setText("\u53d1\u5e03\u65e5\u671f");

            //---- label38 ----
            label38.setText("\u804c\u4e1a\u7c7b\u578b\u540d\u79f0");

            //---- label39 ----
            label39.setText("\u9700\u6c42\u6570\u91cf");

            //---- label40 ----
            label40.setText("\u622a\u6b62\u65e5\u671f");

            //---- label41 ----
            label41.setText("\u5df2\u8058\u7528\u6570\u91cf");

            //---- label58 ----
            label58.setText("\u804c\u4e1a\u7c7b\u578b\u53f7");

            //---- button51 ----
            button51.setText("\u91cd\u7f6e");
            button51.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button51MousePressed(e);
                }
            });

            GroupLayout AdminSearch3ContentPaneLayout = new GroupLayout(AdminSearch3ContentPane);
            AdminSearch3ContentPane.setLayout(AdminSearch3ContentPaneLayout);
            AdminSearch3ContentPaneLayout.setHorizontalGroup(
                AdminSearch3ContentPaneLayout.createParallelGroup()
                    .addGroup(AdminSearch3ContentPaneLayout.createSequentialGroup()
                        .addGroup(AdminSearch3ContentPaneLayout.createParallelGroup()
                            .addGroup(AdminSearch3ContentPaneLayout.createSequentialGroup()
                                .addGap(190, 190, 190)
                                .addGroup(AdminSearch3ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addComponent(label36)
                                    .addComponent(label37)
                                    .addComponent(label34)
                                    .addComponent(label35)
                                    .addComponent(label38)
                                    .addComponent(label39)
                                    .addComponent(label40)))
                            .addGroup(GroupLayout.Alignment.TRAILING, AdminSearch3ContentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(AdminSearch3ContentPaneLayout.createParallelGroup()
                                    .addComponent(label41, GroupLayout.Alignment.TRAILING)
                                    .addComponent(label58, GroupLayout.Alignment.TRAILING))))
                        .addGap(18, 18, 18)
                        .addGroup(AdminSearch3ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(textField49, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                            .addComponent(textField29)
                            .addComponent(textField28)
                            .addComponent(textField26, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                            .addComponent(textField27)
                            .addComponent(textField30, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                            .addComponent(textField32, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                            .addComponent(textField33, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                            .addComponent(textField31, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                        .addGap(84, 84, 84)
                        .addGroup(AdminSearch3ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(button41, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                            .addComponent(button40, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                            .addComponent(button51, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
                        .addContainerGap(109, Short.MAX_VALUE))
            );
            AdminSearch3ContentPaneLayout.setVerticalGroup(
                AdminSearch3ContentPaneLayout.createParallelGroup()
                    .addGroup(AdminSearch3ContentPaneLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(AdminSearch3ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label34)
                            .addComponent(textField26, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(AdminSearch3ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label35)
                            .addComponent(textField27, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(AdminSearch3ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label38)
                            .addComponent(textField30, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(AdminSearch3ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField31, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label39))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(AdminSearch3ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField28, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label36))
                        .addGap(27, 27, 27)
                        .addGroup(AdminSearch3ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField29, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label37))
                        .addGap(28, 28, 28)
                        .addGroup(AdminSearch3ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField32, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label40))
                        .addGap(19, 19, 19)
                        .addGroup(AdminSearch3ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField33, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label41))
                        .addGap(27, 27, 27)
                        .addGroup(AdminSearch3ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField49, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label58)
                            .addComponent(button51))
                        .addContainerGap(79, Short.MAX_VALUE))
                    .addGroup(GroupLayout.Alignment.TRAILING, AdminSearch3ContentPaneLayout.createSequentialGroup()
                        .addContainerGap(76, Short.MAX_VALUE)
                        .addComponent(button40, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(button41, GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE)
                        .addGap(139, 139, 139))
            );
            AdminSearch3.pack();
            AdminSearch3.setLocationRelativeTo(AdminSearch3.getOwner());
        }

        //======== Admin4 ========
        {
            Admin4.setTitle("\u4e13\u4e1a\u4fe1\u606f\u7ba1\u7406");
            Admin4.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    AdminWindowClosing(e);
                }
            });
            var Admin4ContentPane = Admin4.getContentPane();

            //---- button45 ----
            button45.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/door.right.hand.open@2x.png"));
            button45.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button45MousePressed(e);
                }
            });

            //======== scrollPane6 ========
            {
                scrollPane6.setViewportView(table6);
            }

            //---- button46 ----
            button46.setText("\u67e5\u8be2/\u589e\u52a0\u4e13\u4e1a\u4fe1\u606f");
            button46.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/magnifyingglass@2x.png"));
            button46.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button46MousePressed(e);
                }
            });

            //---- button47 ----
            button47.setText("\u5220\u9664\u4e13\u4e1a\u4fe1\u606f");
            button47.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/delete.backward@2x.png"));
            button47.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button47MousePressed(e);
                }
            });

            //---- button54 ----
            button54.setText("\u67e5\u8be2\u4e13\u4e1a\u5c31\u4e1a\u7387");
            button54.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/magnifyingglass@2x.png"));
            button54.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button54MousePressed(e);
                }
            });

            GroupLayout Admin4ContentPaneLayout = new GroupLayout(Admin4ContentPane);
            Admin4ContentPane.setLayout(Admin4ContentPaneLayout);
            Admin4ContentPaneLayout.setHorizontalGroup(
                Admin4ContentPaneLayout.createParallelGroup()
                    .addGroup(Admin4ContentPaneLayout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addGroup(Admin4ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addGroup(Admin4ContentPaneLayout.createSequentialGroup()
                                .addComponent(button45, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
                                .addGap(160, 160, 160)
                                .addComponent(button46)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(button54, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)
                                .addGap(100, 100, 100)
                                .addComponent(button47, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
                            .addComponent(scrollPane6, GroupLayout.PREFERRED_SIZE, 1028, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(82, Short.MAX_VALUE))
            );
            Admin4ContentPaneLayout.setVerticalGroup(
                Admin4ContentPaneLayout.createParallelGroup()
                    .addGroup(Admin4ContentPaneLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(scrollPane6, GroupLayout.PREFERRED_SIZE, 601, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Admin4ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addComponent(button47, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(Admin4ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(button54, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                .addComponent(button46, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
                            .addComponent(button45, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(26, Short.MAX_VALUE))
            );
            Admin4.pack();
            Admin4.setLocationRelativeTo(Admin4.getOwner());
        }

        //======== AdminSearch4 ========
        {
            AdminSearch4.setTitle("\u67e5\u8be2/\u589e\u52a0\u4e13\u4e1a");
            AdminSearch4.setAlwaysOnTop(true);
            var AdminSearch4ContentPane = AdminSearch4.getContentPane();

            //---- label54 ----
            label54.setText("\u4e13\u4e1a\u540d\u79f0");

            //---- button48 ----
            button48.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/magnifyingglass@2x.png"));
            button48.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button48MousePressed(e);
                }
            });

            //---- label55 ----
            label55.setText("\u4e13\u4e1a\u7f16\u53f7");

            //---- button49 ----
            button49.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/\u5927\u4e8c\u4e0b/\u6570\u636e\u5e93\u4f5c\u4e1a\u6587\u6863/\u9ad8\u6821\u5b66\u751f\u5c31\u4e1a\u7ba1\u7406\u7cfb\u7edf\u56fe\u6807/plus.square@2x.png"));
            button49.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    button49MousePressed(e);
                }
            });

            //---- label56 ----
            label56.setText("\u6240\u5c5e\u9662\u7cfb");

            GroupLayout AdminSearch4ContentPaneLayout = new GroupLayout(AdminSearch4ContentPane);
            AdminSearch4ContentPane.setLayout(AdminSearch4ContentPaneLayout);
            AdminSearch4ContentPaneLayout.setHorizontalGroup(
                AdminSearch4ContentPaneLayout.createParallelGroup()
                    .addGroup(AdminSearch4ContentPaneLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(AdminSearch4ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(label54)
                            .addComponent(label56)
                            .addComponent(label55))
                        .addGap(18, 18, 18)
                        .addGroup(AdminSearch4ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(textField48, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                            .addComponent(textField46, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                            .addComponent(textField47, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                        .addGroup(AdminSearch4ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(button48, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                            .addComponent(button49, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))
                        .addGap(43, 43, 43))
            );
            AdminSearch4ContentPaneLayout.setVerticalGroup(
                AdminSearch4ContentPaneLayout.createParallelGroup()
                    .addGroup(AdminSearch4ContentPaneLayout.createSequentialGroup()
                        .addGroup(AdminSearch4ContentPaneLayout.createParallelGroup()
                            .addGroup(AdminSearch4ContentPaneLayout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addGroup(AdminSearch4ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label54)
                                    .addComponent(textField46, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(AdminSearch4ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(textField47, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label55))
                                .addGap(34, 34, 34)
                                .addGroup(AdminSearch4ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label56)
                                    .addComponent(textField48, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                            .addGroup(AdminSearch4ContentPaneLayout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(button48, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(button49, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(81, Short.MAX_VALUE))
            );
            AdminSearch4.pack();
            AdminSearch4.setLocationRelativeTo(AdminSearch4.getOwner());
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
    private JButton button32;
    private JLabel label57;
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
    private JButton button44;
    private JFrame Employers;
    private JButton button8;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JButton button15;
    private JButton button16;
    private JButton button24;
    private JButton button26;
    private JButton button50;
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
    private JLabel label42;
    private JTextField textField34;
    private JDialog dialog1;
    private JLabel label7;
    private JButton button13;
    private JFrame Admin1;
    private JButton button17;
    private JButton button28;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JButton button31;
    private JFrame Admin3;
    private JButton button25;
    private JScrollPane scrollPane4;
    private JTable table4;
    private JButton button38;
    private JButton button39;
    private JButton button53;
    private JFrame Student1;
    private JButton button18;
    private JLabel label43;
    private JLabel label44;
    private JLabel label45;
    private JLabel label46;
    private JLabel label47;
    private JLabel label48;
    private JTextField textField35;
    private JTextField textField36;
    private JTextField textField37;
    private JTextField textField38;
    private JTextField textField39;
    private JTextField textField40;
    private JLabel label49;
    private JTextField textField41;
    private JLabel label50;
    private JTextField textField42;
    private JLabel label51;
    private JTextField textField43;
    private JButton button42;
    private JLabel label60;
    private JFrame Student2;
    private JButton button19;
    private JLabel label52;
    private JLabel label53;
    private JTextField textField44;
    private JTextField textField45;
    private JButton button43;
    private JFrame Student3;
    private JButton button20;
    private JScrollPane scrollPane5;
    private JTable table5;
    private JDialog dialog2;
    private JLabel label8;
    private JButton button14;
    private JFrame Admin2;
    private JButton button21;
    private JScrollPane scrollPane3;
    private JTable table3;
    private JButton button34;
    private JButton button35;
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
    private JFrame AdminSearch;
    private JLabel label19;
    private JTextField textField12;
    private JButton button29;
    private JLabel label20;
    private JTextField textField13;
    private JButton button30;
    private JFrame Register2;
    private JLabel label12;
    private JTextField textField5;
    private JTextField textField14;
    private JLabel label21;
    private JLabel label22;
    private JComboBox<String> comboBox3;
    private JButton button33;
    private JLabel label23;
    private JTextField textField15;
    private JLabel label24;
    private JTextField textField16;
    private JFrame AdminSearch2;
    private JLabel label25;
    private JTextField textField17;
    private JButton button36;
    private JLabel label26;
    private JTextField textField18;
    private JButton button37;
    private JTextField textField19;
    private JTextField textField20;
    private JLabel label27;
    private JLabel label28;
    private JLabel label29;
    private JLabel label30;
    private JLabel label31;
    private JLabel label32;
    private JLabel label33;
    private JTextField textField21;
    private JTextField textField22;
    private JTextField textField23;
    private JTextField textField24;
    private JTextField textField25;
    private JButton button52;
    private JTextField textField50;
    private JLabel label59;
    private JFrame AdminSearch3;
    private JLabel label34;
    private JTextField textField26;
    private JButton button40;
    private JLabel label35;
    private JTextField textField27;
    private JButton button41;
    private JTextField textField28;
    private JTextField textField29;
    private JLabel label36;
    private JLabel label37;
    private JLabel label38;
    private JLabel label39;
    private JLabel label40;
    private JLabel label41;
    private JTextField textField30;
    private JTextField textField31;
    private JTextField textField32;
    private JTextField textField33;
    private JTextField textField49;
    private JLabel label58;
    private JButton button51;
    private JFrame Admin4;
    private JButton button45;
    private JScrollPane scrollPane6;
    private JTable table6;
    private JButton button46;
    private JButton button47;
    private JButton button54;
    private JFrame AdminSearch4;
    private JLabel label54;
    private JTextField textField46;
    private JButton button48;
    private JLabel label55;
    private JTextField textField47;
    private JButton button49;
    private JLabel label56;
    private JTextField textField48;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
    public static void main(String[] args) throws Exception {
        UI ui = new UI();
        ui.initComponents();
        ui.Login.setVisible(true);
    }

    //显示学生照片的方法
    private void displayCurrentUserPhoto(int studentId) {
        try {
            // 创建数据库连接
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/高校学生就业管理系统", "root", "Lzy-200387");

            // 查询照片
            String query = "SELECT 图片 FROM 毕业生照片表 WHERE 学号 = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();

            // 显示照片
            if (resultSet.next()) {
                InputStream photoData = resultSet.getBinaryStream("图片");
                if (photoData != null) {
                    // 读取照片数据并显示在 JLabel 上
                    BufferedImage image = ImageIO.read(photoData);
                    ImageIcon icon = new ImageIcon(image);
                    label60.setIcon(icon);
                } else {
                    // 使用默认照片
                    label60.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/大二下/数据库作业文档/高校学生就业管理系统图标/人物图片.jpeg"));
                }
            } else {
                // 使用默认照片
                label60.setIcon(new ImageIcon("/Users/linzhengyang/Desktop/大二下/数据库作业文档/高校学生就业管理系统图标/人物图片.jpeg"));
            }

            // 关闭连接和资源
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    private byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return byteArray;
    }



    //调用存储过程查询各个专业的就业率
    public void showEmploymentRatesByMajor() {
        try {
            // 创建数据库连接
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/高校学生就业管理系统", "root", "Lzy-200387");

            // 调用存储过程
            CallableStatement statement = connection.prepareCall("CALL sp_get_employment_rates_by_major()");

            // 执行存储过程
            statement.execute();

            // 获取结果集
            ResultSet resultSet = statement.getResultSet();

            // 创建JDialog并设置布局
            JDialog dialog = new JDialog();
            dialog.setLayout(new BorderLayout());

            // 创建表格模型和表格
            DefaultTableModel model = new DefaultTableModel();
            JTable table = new JTable(model);

            // 添加表格列
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                model.addColumn(columnName);
            }

            // 添加表格行
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                model.addRow(rowData);
            }

            // 将表格添加到滚动面板中
            JScrollPane scrollPane = new JScrollPane(table);
            dialog.add(scrollPane, BorderLayout.CENTER);

            // 设置对话框属性并显示
            dialog.setTitle("各专业就业率");
            dialog.setSize(800, 400);
            dialog.setModal(true);
            dialog.setVisible(true);

            // 关闭连接和语句
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //调用存储过程查询全体学生就业率
    public void showEmploymentStats() {
        // 创建数据库连接
        String url = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String username = "root";
        String password = "Lzy-200387";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            // 创建存储过程调用
            String procedureQuery = "{CALL sp_get_employment_stats()}";
            try (CallableStatement procedureStmt = conn.prepareCall(procedureQuery)) {
                // 执行存储过程调用并获取结果集
                ResultSet resultSet = procedureStmt.executeQuery();

                // 解析结果集
                if (resultSet.next()) {
                    int total = resultSet.getInt("毕业生总数");
                    int unemployed = resultSet.getInt("待业人数");
                    int employed = resultSet.getInt("就业人数");
                    float employmentRate = resultSet.getFloat("就业率");

                    // 创建并显示结果对话框
                    showDialog(total, unemployed, employed, employmentRate);
                }

                // 关闭结果集
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void showDialog(int total, int unemployed, int employed, float employmentRate) {
        JDialog dialog = new JDialog((JFrame) null, "就业统计", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null); // 设置对话框居中显示

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel totalLabel = new JLabel("毕业生总数：" + total);
        JLabel unemployedLabel = new JLabel("待业人数：" + unemployed);
        JLabel employedLabel = new JLabel("就业人数：" + employed);
        JLabel employmentRateLabel = new JLabel("就业率：" + employmentRate);

        panel.add(totalLabel);
        panel.add(unemployedLabel);
        panel.add(employedLabel);
        panel.add(employmentRateLabel);

        dialog.add(panel);

        dialog.setVisible(true);
    }

    //管理员删除专业信息的方法
    private void deleteMajorRecord(int majorId) {
        Connection connection = null;
        PreparedStatement statement = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 执行删除操作
            String deleteQuery = "DELETE FROM 专业信息表 WHERE 专业编号 = ?";
            statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, majorId);
            statement.executeUpdate();

            // 提示删除成功
            JOptionPane.showMessageDialog(Admin2, "删除成功", "提示", JOptionPane.INFORMATION_MESSAGE);

            // 刷新表格或执行其他操作
            // ...
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, null);
        }
    }

    //管理员增加专业信息的方法
    public void insertMajorInformation(JTextField idTextField, JTextField nameTextField, JTextField departmentTextField) {
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";
        // 获取输入的专业编号、专业名称和所属院系
        String idText = idTextField.getText().trim();
        String nameText = nameTextField.getText().trim();
        String departmentText = departmentTextField.getText().trim();

        // 检查三个 JTextField 是否都有输入的数据
        if (idText.isEmpty() || nameText.isEmpty() || departmentText.isEmpty()) {
            // 如果有任何一个 JTextField 没有输入数据，则不执行插入操作
            JOptionPane.showMessageDialog(AdminSearch4,"信息不能为空！","提示",JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            // 建立数据库连接
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 构建插入语句
            String insertQuery = "INSERT INTO 专业信息表 (专业编号, 专业名称, 所属院系) VALUES (?, ?, ?)";

            // 创建预编译语句对象
            PreparedStatement statement = connection.prepareStatement(insertQuery);

            // 设置参数
            statement.setInt(1, Integer.parseInt(idText));
            statement.setString(2, nameText);
            statement.setInt(3, Integer.parseInt(departmentText));

            // 执行插入操作
            statement.executeUpdate();

            // 关闭连接和资源
            statement.close();
            connection.close();

            JOptionPane.showMessageDialog(AdminSearch4,"信息已成功插入数据库!","提示",JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //管理员查询专业信息的方法
    public static void queryMajorInformation(JTable table, JTextField idTextField, JTextField nameTextField, JTextField departmentTextField) {
            try {
                String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
                String USERNAME = "root";
                String PASSWORD = "Lzy-200387";
                // 建立数据库连接
                Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                // 构建查询语句
                String query = "SELECT * FROM 专业信息表 WHERE 1=1";

                // 获取输入的专业编号、专业名称和所属院系
                String idText = idTextField.getText().trim();
                String nameText = nameTextField.getText().trim();
                String departmentText = departmentTextField.getText().trim();

                // 动态拼接查询条件
                if (!idText.isEmpty()) {
                    query += " AND 专业编号 = " + Integer.parseInt(idText);
                }

                if (!nameText.isEmpty()) {
                    query += " AND 专业名称 LIKE '%" + nameText + "%'";
                }

                if (!departmentText.isEmpty()) {
                    query += " AND 所属院系 = " + Integer.parseInt(departmentText);
                }

                // 创建预编译语句对象
                PreparedStatement statement = connection.prepareStatement(query);

                // 执行查询操作
                ResultSet resultSet = statement.executeQuery();

                // 清空表格数据
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);

                // 遍历查询结果，并将数据添加到表格中
                while (resultSet.next()) {
                    int id = resultSet.getInt("专业编号");
                    String name = resultSet.getString("专业名称");
                    int department = resultSet.getInt("所属院系");

                    Object[] rowData = {id, name, department};
                    model.addRow(rowData);
                }

                // 关闭连接和资源
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    //为table6添加监听的方法
    public static void updateDatabaseRecord(JTable table) {
        TableModel model = table.getModel();

        // 添加单元格编辑监听器
        CellEditorListener cellEditorListener = new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                Object newValue = table.getValueAt(row, column);
                Object primaryKey = table.getValueAt(row, 0);

                // 更新数据库记录
                if (primaryKey != null) {
                    updateDatabaseMajorRecord(primaryKey, column, newValue);
                }
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
                // 编辑取消时不执行任何操作
            }
        };

        table.getDefaultEditor(Object.class).addCellEditorListener(cellEditorListener);
    }

    //管理员修改专业信息的方法
    private static void updateDatabaseMajorRecord(Object primaryKey, int column, Object newValue) {
        try {
            // 建立数据库连接
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/高校学生就业管理系统", "root", "Lzy-200387");

            // 构建更新语句
            String updateQuery = "UPDATE 专业信息表 SET ";
            String columnToUpdate;

            // 根据列索引选择要更新的列
            switch (column) {
                case 1:
                    columnToUpdate = "专业名称";
                    break;
                case 2:
                    columnToUpdate = "所属院系";
                    break;
                default:
                    // 如果列索引无效，则不执行更新操作
                    return;
            }

            // 构建完整的更新语句
            updateQuery += columnToUpdate + " = ? WHERE 专业编号 = ?";

            // 创建预编译语句对象
            PreparedStatement statement = connection.prepareStatement(updateQuery);

            // 设置参数
            statement.setObject(1, newValue);
            statement.setObject(2, primaryKey);

            // 执行更新操作
            statement.executeUpdate();

            // 关闭连接和资源
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //管理员填充专业信息表到table6中的方法
    public static void loadMajorDataToTable(JTable table6) {
        DefaultTableModel model = new DefaultTableModel();
        model.setRowCount(0);
        String[] columnNames = {"专业编号", "专业名称", "所属院系"};
        model.setColumnIdentifiers(columnNames);
        table6.setModel(model);
            try {
                // 连接数据库
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/高校学生就业管理系统", "root", "Lzy-200387");

                // 执行查询语句
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM 专业信息表");

                // 遍历结果集并将数据添加到表格模型
                while (resultSet.next()) {
                    int 专业编号 = resultSet.getInt("专业编号");
                    String 专业名称 = resultSet.getString("专业名称");
                    int 所属院系 = resultSet.getInt("所属院系");

                    Object[] rowData = {专业编号, 专业名称, 所属院系};
                    model.addRow(rowData);
                }

                // 关闭连接
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    //毕业生查看招聘信息的方法
    public static void loadJobDataToTable(JTable table5) {
            DefaultTableModel model = new DefaultTableModel();
        String[] columnNames = {"职业名称", "需求数量", "用人单位", "截止日期", "已聘用数量", "职业类型名称"};
        model.setColumnIdentifiers(columnNames);
        table5.setModel(model);
            // 建立数据库连接
            String url = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
            String username = "root";
            String password = "Lzy-200387";
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                // 执行查询
                String query = "SELECT 职业名称, 需求数量, 用人单位, 截止日期, 已聘用数量, 职业类型名称 FROM 职业信息表";
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                // 遍历结果集并将数据添加到表格模型中
                while (resultSet.next()) {
                    String 职业名称 = resultSet.getString("职业名称");
                    int 需求数量 = resultSet.getInt("需求数量");
                    String 用人单位 = resultSet.getString("用人单位");
                    String 截止日期 = resultSet.getString("截止日期");
                    int 已聘用数量 = resultSet.getInt("已聘用数量");
                    String 职业类型名称 = resultSet.getString("职业类型名称");

                    model.addRow(new Object[]{职业名称, 需求数量, 用人单位, 截止日期, 已聘用数量, 职业类型名称});
                }
                // 关闭数据库连接
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    //毕业生登记就业信息的方法
    private void processJobRegistration(String employer, String occupationName) {
        Connection connection = null;
        PreparedStatement statement = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;

        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 检查职业信息表中是否存在指定的职业名称
            String checkQuery = "SELECT * FROM 职业信息表 WHERE 职业名称 = ? AND 用人单位 = ?";
            statement = connection.prepareStatement(checkQuery);
            statement.setString(1, occupationName);
            statement.setString(2, employer);
            resultSet = statement.executeQuery();

            // 如果职业信息表中不存在指定的职业名称，则显示提示框并返回
            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(Student2, "职业信息不存在", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 获取职业信息表中的需求数量和已聘用数量
            int demand = resultSet.getInt("需求数量");
            int hiredCount = resultSet.getInt("已聘用数量");
            int occupationId = resultSet.getInt("职业编号");

            // 检查已聘用数量是否大于需求数量
            if (hiredCount >= demand) {
                JOptionPane.showMessageDialog(Student2, "已聘用数量已满", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 更新毕业生信息表中的就业状态和职业
            int studentId = getCurrentUserStudentId(textField1.getText());
            String updateQuery = "UPDATE 毕业生信息表 SET 就业状态 = '已就业', 职业 = ? ,职业编号 = ? WHERE 学号 = ?";
            callableStatement = connection.prepareCall(updateQuery);
            callableStatement.setString(1, occupationName);
            callableStatement.setInt(2, studentId);
            callableStatement.setInt(3, occupationId);
            callableStatement.executeUpdate();

            // 提示登记成功
            JOptionPane.showMessageDialog(Student2, "登记成功", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, resultSet);
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //毕业生更新联系方式的方法
    private void updateContactInfoInDatabase(int studentId, String newContactInfo) {
        Connection connection = null;
        PreparedStatement statement = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 更新联系方式
            String updateQuery = "UPDATE 毕业生信息表 SET 联系方式 = ? WHERE 学号 = ?";
            statement = connection.prepareStatement(updateQuery);
            statement.setString(1, newContactInfo);
            statement.setInt(2, studentId);
            statement.executeUpdate();

            // 提示更新成功
            JOptionPane.showMessageDialog(Admin2, "联系方式更新成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            textField39.setEditable(false);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, null);
        }
    }

    //毕业生查询当前登录账户的毕业生详细信息并显示在 JTextField 中
    private void displayCurrentGraduateStudentInfo(String loggedInUsername, JTextField textFieldStudentId, JTextField textFieldName, JTextField textFieldGender, JTextField textFieldBirthDate, JTextField textFieldDepartment, JTextField textFieldMajor, JTextField textFieldContact, JTextField textFieldEmploymentStatus, JTextField textFieldOccupation) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 查询毕业生详细信息
            String query = "SELECT 学号, 姓名, 性别, 出生日期, 院系名称, 专业名称, 联系方式, 就业状态, 职业 FROM 毕业生详细信息视图 WHERE 学号 = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, getCurrentUserStudentId(loggedInUsername));
            resultSet = statement.executeQuery();

            // 显示查询结果
            if (resultSet.next()) {
                textFieldStudentId.setText(String.valueOf(resultSet.getInt("学号")));
                textFieldName.setText(resultSet.getString("姓名"));
                textFieldGender.setText(resultSet.getString("性别"));

                // 格式化日期
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String birthDate = dateFormat.format(resultSet.getDate("出生日期"));
                textFieldBirthDate.setText(birthDate);

                textFieldDepartment.setText(resultSet.getString("院系名称"));
                textFieldMajor.setText(resultSet.getString("专业名称"));
                textFieldContact.setText(resultSet.getString("联系方式"));
                textFieldEmploymentStatus.setText(resultSet.getString("就业状态"));

                String occupationId = resultSet.getString("职业");
                if(occupationId == ""){
                    occupationId = "无";
                }
                textFieldOccupation.setText(occupationId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, resultSet);
        }
    }

    // 查询当前登录账户的学号
    private int getCurrentUserStudentId(String loggedInUsername) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int studentId = -1;  // 默认值为 -1 表示未找到对应的学号

        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 查询学号
            String query = "SELECT 学号 FROM 账号信息表 WHERE 账号 = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, loggedInUsername);
            resultSet = statement.executeQuery();
            // 获取学号
            if (resultSet.next()) {
                studentId = resultSet.getInt("学号");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, resultSet);
        }

        return studentId;
    }

    // 管理员删除职业信息表记录
    private void deleteOccupationRecord(int occupationId) {
        Connection connection = null;
        PreparedStatement statement = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 执行删除操作
            String deleteQuery = "DELETE FROM 职业信息表 WHERE 职业编号 = ?";
            statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, occupationId);
            statement.executeUpdate();

            // 提示删除成功
            JOptionPane.showMessageDialog(Admin3, "删除成功", "提示", JOptionPane.INFORMATION_MESSAGE);

            // 刷新表格或执行其他操作
            // ...
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, null);
        }
    }

    //管理员增加职业信息表中数据的方法
    public void insertOccupation(String occupationId, String occupationName, int demand, String employer, String startDateString, String endDateString, int hiredCount, String occupationTypeName , int occupationTypeId) {
        // 检测是否有任何一个文本字段为空
        if (occupationId.isEmpty() || occupationName.isEmpty() || employer.isEmpty() || startDateString.isEmpty() || endDateString.isEmpty() || occupationTypeName.isEmpty()) {
            JOptionPane.showMessageDialog(Admin3, "请填写所有必要信息", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 检测需求数量和已聘用数量是否为非负数
        if (demand < 0 || hiredCount < 0) {
            JOptionPane.showMessageDialog(Admin3, "需求数量和已聘用数量必须为非负数", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection connection = null;
        PreparedStatement statement = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 创建 PreparedStatement 对象
            String query = "INSERT INTO 职业信息表 (职业编号, 职业名称, 需求数量, 用人单位, 发布日期, 截止日期, 已聘用数量, 职业类型名称,职业类型号) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);

            // 设置参数
            statement.setInt(1, Integer.parseInt(occupationId));
            statement.setString(2, occupationName);
            statement.setInt(3, demand);
            statement.setString(4, employer);
            statement.setDate(5, convertStringToDate(startDateString));
            statement.setDate(6, convertStringToDate(endDateString));
            statement.setInt(7, hiredCount);
            statement.setString(8, occupationTypeName);
            statement.setInt(9,occupationTypeId);

            // 执行插入操作
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // 显示错误消息框
            JOptionPane.showMessageDialog(AdminSearch3, "不合法的信息！", "插入信息错误", JOptionPane.ERROR_MESSAGE);
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, null);
        }
    }

    //管理员更新数据库中的职业信息表数据的方法
    private void handleTableModification(int row, int column, Object data) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 创建更新语句
            String updateQuery = "";
            switch (column) {
                case 0:
                    updateQuery = "UPDATE 职业信息表 SET 职业编号 = ? WHERE 职业编号 = ?";
                    break;
                case 1:
                    updateQuery = "UPDATE 职业信息表 SET 职业名称 = ? WHERE 职业编号 = ?";
                    break;
                case 2:
                    updateQuery = "UPDATE 职业信息表 SET 需求数量 = ? WHERE 职业编号 = ?";
                    break;
                case 3:
                    updateQuery = "UPDATE 职业信息表 SET 用人单位 = ? WHERE 职业编号 = ?";
                    break;
                case 4:
                    updateQuery = "UPDATE 职业信息表 SET 发布日期 = ? WHERE 职业编号 = ?";
                    break;
                case 5:
                    updateQuery = "UPDATE 职业信息表 SET 截止日期 = ? WHERE 职业编号 = ?";
                    break;
                case 6:
                    updateQuery = "UPDATE 职业信息表 SET 已聘用数量 = ? WHERE 职业编号 = ?";
                    break;
                case 7:
                    updateQuery = "UPDATE 职业信息表 SET 职业类型名称 = ? WHERE 职业编号 = ?";
                    break;
                case 8:
                    updateQuery = "UPDATE 职业信息表 SET 职业类型号 = ? WHERE 职业编号 = ?";
                    break;
            }

            // 创建 PreparedStatement 对象
            statement = connection.prepareStatement(updateQuery);

            // 设置更新参数
            switch (column) {
                case 0:
                case 2:
                case 6:
                case 8:
                    try {
                        int demand = Integer.parseInt((String) data);
                        statement.setInt(1, demand);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return;
                    }
                    break;
                case 1:
                case 3:
                case 7:
                    statement.setString(1, (String) data);
                    break;
                case 4:
                case 5:
                    String dateString = (String) data;
                    Date date = convertStringToDate(dateString);
                    statement.setDate(1, date);
                    break;
            }
            int jobId = (int) table4.getValueAt(row, 0);
            statement.setInt(2, jobId);

            // 执行更新语句
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("表格修改已更新到数据库");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, resultSet);
        }
    }

    //管理员查询职业信息
    public void searchJobInformation(String jobId, String jobName, String demandQuantity, String employer, String publishDate, String deadline, String employedQuantity, String jobType,String jobTypeId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 创建查询语句
            String query = "SELECT * FROM 职业信息表 WHERE 1=1";
            if (jobId != null && !jobId.isEmpty()) {
                query += " AND 职业编号 = ?";
            }
            if (jobName != null && !jobName.isEmpty()) {
                query += " AND 职业名称 LIKE ?";
            }
            if (demandQuantity != null && !demandQuantity.isEmpty()) {
                query += " AND 需求数量 = ?";
            }
            if (employer != null && !employer.isEmpty()) {
                query += " AND 用人单位 LIKE ?";
            }
            if (publishDate != null && !publishDate.isEmpty()) {
                query += " AND 发布日期 = ?";
            }
            if (deadline != null && !deadline.isEmpty()) {
                query += " AND 截止日期 = ?";
            }
            if (employedQuantity != null && !employedQuantity.isEmpty()) {
                query += " AND 已聘用数量 = ?";
            }
            if (jobType != null && !jobType.isEmpty()) {
                query += " AND 职业类型名称 LIKE ?";
            }
            if(jobTypeId !=null && !jobTypeId.isEmpty()){
                query += " AND 职业类型号 = ?";
            }

            // 创建PreparedStatement对象
            statement = connection.prepareStatement(query);

            // 设置查询参数
            int parameterIndex = 1;
            if (jobId != null && !jobId.isEmpty()) {
                statement.setInt(parameterIndex, Integer.parseInt(jobId));
                parameterIndex++;
            }
            if (jobName != null && !jobName.isEmpty()) {
                statement.setString(parameterIndex, "%" + jobName + "%");
                parameterIndex++;
            }
            if (demandQuantity != null && !demandQuantity.isEmpty()) {
                statement.setInt(parameterIndex, Integer.parseInt(demandQuantity));
                parameterIndex++;
            }
            if (employer != null && !employer.isEmpty()) {
                statement.setString(parameterIndex, "%" + employer + "%");
                parameterIndex++;
            }
            if (publishDate != null && !publishDate.isEmpty()) {
                Date publishDateSql = convertStringToDate(publishDate);
                statement.setDate(parameterIndex, publishDateSql);
                parameterIndex++;
            }
            if (deadline != null && !deadline.isEmpty()) {
                Date deadlineSql = convertStringToDate(deadline);
                statement.setDate(parameterIndex, deadlineSql);
                parameterIndex++;
            }
            if (employedQuantity != null && !employedQuantity.isEmpty()) {
                statement.setInt(parameterIndex, Integer.parseInt(employedQuantity));
                parameterIndex++;
            }
            if (jobType != null && !jobType.isEmpty()) {
                statement.setString(parameterIndex, "%" + jobType + "%");
                parameterIndex++;
            }
            if (jobTypeId != null && !jobTypeId.isEmpty()) {
                statement.setInt(parameterIndex, Integer.parseInt(jobTypeId));
            }

            // 执行查询语句
            resultSet = statement.executeQuery();

            // 获取结果集的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();

            // 获取列数
            int columnCount = metaData.getColumnCount();

            // 创建表格模型
            DefaultTableModel tableModel = new DefaultTableModel();

            // 添加列名到表格模型
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                tableModel.addColumn(metaData.getColumnName(columnIndex));
            }

            // 添加数据到表格模型
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    rowData[columnIndex - 1] = resultSet.getObject(columnIndex);
                }
                tableModel.addRow(rowData);
            }

            // 设置表格模型到JTable
            table4.setModel(tableModel);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, resultSet);
        }
    }

    //管理员获取全部招聘信息
    public void loadJobInformation(JTable table4) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 创建查询语句
            String query = "SELECT * FROM 职业信息表";

            // 创建PreparedStatement对象
            statement = connection.prepareStatement(query);

            // 执行查询语句
            resultSet = statement.executeQuery();

            // 获取结果集的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();

            // 获取列数
            int columnCount = metaData.getColumnCount();

            // 创建表格模型
            DefaultTableModel tableModel = new DefaultTableModel();

            // 添加列名到表格模型
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                tableModel.addColumn(metaData.getColumnName(columnIndex));
            }

            // 添加数据到表格模型
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    rowData[columnIndex - 1] = resultSet.getObject(columnIndex);
                }
                tableModel.addRow(rowData);
            }

            // 设置表格模型到JTable
            table4.setModel(tableModel);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, resultSet);
        }
    }


    //管理员删除毕业生信息
    private void deleteStudentRecord(int studentId) {
        Connection connection = null;
        PreparedStatement statement = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 执行删除操作
            String deleteQuery = "DELETE FROM 毕业生信息表 WHERE 学号 = ?";
            statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, studentId);
            statement.executeUpdate();

            // 提示删除成功
            JOptionPane.showMessageDialog(Admin2, "删除成功", "提示", JOptionPane.INFORMATION_MESSAGE);

            // 刷新表格或执行其他操作
            // ...
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, null);
        }
    }

    //管理员增加毕业生信息
    public void addStudentInfo(String studentId, String name, String gender, String birthdate, String departmentId, String majorId, String contact, String employmentStatus, String occupation, String occupationId) {
        // 检测必填项是否为空
        if (studentId.isEmpty() || name.isEmpty() || gender.isEmpty() || birthdate.isEmpty() || contact.isEmpty() || employmentStatus.isEmpty()) {
            JOptionPane.showMessageDialog(AdminSearch2, "除职业和职业编号外均需填写", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Connection connection = null;
        PreparedStatement statement = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 创建插入语句
            String query = "INSERT INTO 毕业生信息表 (学号, 姓名, 性别, 出生日期, 所在院系, 所学专业, 联系方式, 就业状态, 职业, 职业编号) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);

            // 设置插入参数
            statement.setString(1, studentId);
            statement.setString(2, name);
            statement.setString(3, gender);
            statement.setDate(4, convertStringToDate(birthdate));
            statement.setInt(5, Integer.parseInt(departmentId));
            statement.setInt(6, Integer.parseInt(majorId));
            statement.setString(7, contact);
            statement.setString(8, employmentStatus);
            if (occupation.isEmpty()) {
                statement.setNull(9, java.sql.Types.INTEGER);
                statement.setNull(10, java.sql.Types.INTEGER);
            } else {
                statement.setString(9, occupation);
                statement.setString(10,occupationId);
            }

            // 执行插入语句
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("毕业生信息添加成功");
            } else {
                System.out.println("毕业生信息添加失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, null);
        }
    }

    // 管理员查询毕业生信息
    private void searchStudentInfo(String studentId, String name, String gender, String birthdate, String department, String major, String contact, String employmentStatus, String occupation, String occupationId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";
        if(birthdate == "XXXX-XX-XX"){
            birthdate = "";
        }
        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 创建查询语句
            String query = "SELECT * FROM 毕业生信息表 WHERE 1=1";
            if (!studentId.isEmpty()) {
                query += " AND 学号 = ?";
            }
            if (!name.isEmpty()) {
                query += " AND 姓名 LIKE ?";
            }
            if (!gender.isEmpty()) {
                query += " AND 性别 = ?";
            }
            if (!birthdate.isEmpty()) {
                query += " AND 出生日期 = ?";
            }
            if (!department.isEmpty()) {
                query += " AND 所在院系 = ?";
            }
            if (!major.isEmpty()) {
                query += " AND 所学专业 = ?";
            }
            if (!contact.isEmpty()) {
                query += " AND 联系方式 = ?";
            }
            if (!employmentStatus.isEmpty()) {
                query += " AND 就业状态 = ?";
            }
            if (!occupation.isEmpty()) {
                query += " AND 职业 = ?";
            }
            if (!occupationId.isEmpty()) {
                query += " AND 职业编号 = ?";
            }
            statement = connection.prepareStatement(query);

            // 设置查询参数
            int parameterIndex = 1;
            if (!studentId.isEmpty()) {
                statement.setString(parameterIndex, studentId);
                parameterIndex++;
            }
            if (!name.isEmpty()) {
                statement.setString(parameterIndex, "%" + name + "%");
                parameterIndex++;
            }
            if (!gender.isEmpty()) {
                statement.setString(parameterIndex, gender);
                parameterIndex++;
            }
            if (!birthdate.isEmpty()) {
                Date birthDate = convertStringToDate(birthdate);
                statement.setDate(parameterIndex, birthDate);
                parameterIndex++;
            }
            if (!department.isEmpty()) {
                statement.setString(parameterIndex, department);
                parameterIndex++;
            }
            if (!major.isEmpty()) {
                statement.setString(parameterIndex, major);
                parameterIndex++;
            }
            if (!contact.isEmpty()) {
                statement.setString(parameterIndex, contact);
                parameterIndex++;
            }
            if (!employmentStatus.isEmpty()) {
                statement.setString(parameterIndex, employmentStatus);
                parameterIndex++;
            }
            if (!occupation.isEmpty()) {
                statement.setString(parameterIndex, occupation);
                parameterIndex++;
            }
            if (!occupationId.isEmpty()) {
                statement.setInt(parameterIndex, Integer.parseInt(occupationId));
            }

            // 执行查询语句
            resultSet = statement.executeQuery();

            // 清空表格模型
            DefaultTableModel tableModel = (DefaultTableModel) table3.getModel();
            tableModel.setRowCount(0);

            // 遍历结果集，将数据填充到表格模型中
            while (resultSet.next()) {
                Object[] rowData = new Object[]{
                        resultSet.getString("学号"),
                        resultSet.getString("姓名"),
                        resultSet.getString("性别"),
                        resultSet.getDate("出生日期"),
                        resultSet.getString("所在院系"),
                        resultSet.getString("所学专业"),
                        resultSet.getString("联系方式"),
                        resultSet.getString("就业状态"),
                        resultSet.getString("职业"),
                        resultSet.getObject("职业编号") != null ? resultSet.getInt("职业编号") : null,
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

    // 更新数据库中的毕业生信息
    private void updateStudentInfo(Object studentId, int column, Object newValue) {
        Connection connection = null;
        PreparedStatement statement = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 创建更新语句
            String updateQuery = "";
            switch (column) {
                case 1:
                    updateQuery = "UPDATE 毕业生信息表 SET 姓名 = ? WHERE 学号 = ?";
                    break;
                case 2:
                    updateQuery = "UPDATE 毕业生信息表 SET 性别 = ? WHERE 学号 = ?";
                    break;
                case 3:
                    updateQuery = "UPDATE 毕业生信息表 SET 出生日期 = ? WHERE 学号 = ?";
                    break;
                case 4:
                    updateQuery = "UPDATE 毕业生信息表 SET 所在院系 = ? WHERE 学号 = ?";
                    break;
                case 5:
                    updateQuery = "UPDATE 毕业生信息表 SET 所学专业 = ? WHERE 学号 = ?";
                    break;
                case 6:
                    updateQuery = "UPDATE 毕业生信息表 SET 联系方式 = ? WHERE 学号 = ?";
                    break;
                case 7:
                    updateQuery = "UPDATE 毕业生信息表 SET 就业状态 = ? WHERE 学号 = ?";
                    break;
                case 8:
                    updateQuery = "UPDATE 毕业生信息表 SET 职业 = ? WHERE 学号 = ?";
                    break;
                case 9:
                    updateQuery = "UPDATE 毕业生信息表 SET 职业编号 = ? WHERE 学号 = ?";
                    break;
            }

            // 创建PreparedStatement对象
            statement = connection.prepareStatement(updateQuery);
            statement.setObject(1, newValue);
            statement.setObject(2, studentId);

            // 执行更新语句
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, null);
        }
    }

    //显示毕业生信息表中的所有内容
    public void updateGraduateDetailsTable(JTable table3) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 创建查询语句
            String query = "SELECT * FROM 毕业生信息表";

            // 创建PreparedStatement对象
            statement = connection.prepareStatement(query);

            // 执行查询语句
            resultSet = statement.executeQuery();

            // 获取结果集的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();

            // 获取列数
            int columnCount = metaData.getColumnCount();

            // 创建表格模型
            DefaultTableModel tableModel = new DefaultTableModel();

            // 添加列名到表格模型
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                tableModel.addColumn(metaData.getColumnName(columnIndex));
            }

            // 添加数据到表格模型
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    rowData[columnIndex - 1] = resultSet.getObject(columnIndex);
                }
                tableModel.addRow(rowData);
            }

            // 设置表格模型到JTable
            table3.setModel(tableModel);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, resultSet);
        }
    }

    //管理员删除院系记录
    private void deleteDepartmentRecord(int departmentId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 检查是否存在相关的毕业生信息
            String checkQuery = "SELECT COUNT(*) AS count FROM 毕业生信息表 WHERE 所在院系 = ?";
            statement = connection.prepareStatement(checkQuery);
            statement.setInt(1, departmentId);
            resultSet = statement.executeQuery();

            // 获取查询结果
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                if (count > 0) {
                    // 存在相关数据，提示用户处理相关数据
                    int option = JOptionPane.showConfirmDialog(Admin1,
                            "存在与该院系相关的毕业生信息，确定要删除该院系及相关数据吗？相关的毕业生信息也会丢失",
                            "确认删除",
                            JOptionPane.YES_NO_OPTION);
                    if (option != JOptionPane.YES_OPTION) {
                        return; // 用户取消删除操作
                    }
                }
            }

            // 停用外键约束
            String disableFKQuery = "SET FOREIGN_KEY_CHECKS = 0;";
            statement = connection.prepareStatement(disableFKQuery);
            statement.execute();

            // 执行删除操作
            String deleteQuery = "DELETE FROM 院系信息表 WHERE 院系编号 = ?";
            statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, departmentId);
            statement.executeUpdate();

            // 启用外键约束
            String enableFKQuery = "SET FOREIGN_KEY_CHECKS = 1;";
            statement = connection.prepareStatement(enableFKQuery);
            statement.execute();

            // 提示删除成功
            JOptionPane.showMessageDialog(Admin1, "删除成功", "提示", JOptionPane.INFORMATION_MESSAGE);

            // 刷新表格或执行其他操作
            // ...
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, resultSet);
        }
    }

    // 管理员添加院系时检查院系编号是否重复
    private boolean isDepartmentIdDuplicate(int departmentId) {
        boolean isDuplicate = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 查询数据库中是否存在相同的院系编号
            String query = "SELECT 院系编号 FROM 院系信息表 WHERE 院系编号 = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, departmentId);
            resultSet = statement.executeQuery();

            // 检查查询结果
            if (resultSet.next()) {
                isDuplicate = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, resultSet);
        }

        return isDuplicate;
    }

    // 管理员添加院系时将新数据插入数据库
    private void insertDepartmentRecord(int departmentId, String departmentName) {
        Connection connection = null;
        PreparedStatement statement = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 创建 PreparedStatement 对象
            String query = "INSERT INTO 院系信息表 (院系编号, 院系名称) VALUES (?, ?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, departmentId);
            statement.setString(2, departmentName);

            // 执行插入语句
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, null);
        }
    }

    // 管理员添加院系时生成新的院系编号
    private int generateNewDepartmentId() {
        int newDepartmentId = -1;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 查询数据库中的院系数量
            String countQuery = "SELECT COUNT(*) AS count FROM 院系信息表";
            statement = connection.prepareStatement(countQuery);
            resultSet = statement.executeQuery();

            // 获取查询结果
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                newDepartmentId = count + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, resultSet);
        }

        return newDepartmentId;
    }

    //管理员进行院系查询方法
    public void searchDepartments(String departmentName, int departmentId) {
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
            String query = "SELECT * FROM 院系信息表 WHERE 1=1";
            if (departmentName != null && !departmentName.isEmpty()) {
                query += " AND 院系名称 LIKE ?";
            }
            if (departmentId != 0) {
                query += " AND 院系编号 = ?";
            }
            statement = connection.prepareStatement(query);

            // 设置查询参数
            int parameterIndex = 1;
            if (departmentName != null && !departmentName.isEmpty()) {
                statement.setString(parameterIndex, "%" + departmentName + "%");
                parameterIndex++;
            }
            if (departmentId != 0) {
                statement.setInt(parameterIndex, departmentId);
            }

            // 执行查询语句
            resultSet = statement.executeQuery();

            // 清空表格模型
            DefaultTableModel tableModel = (DefaultTableModel) table2.getModel();
            tableModel.setRowCount(0);

            // 遍历结果集，将数据填充到表格模型中
            while (resultSet.next()) {
                Object[] rowData = new Object[]{
                        resultSet.getInt("院系编号"),
                        resultSet.getString("院系名称")
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

    //管理员更新院系信息的方法
    private void updateDepartmentRecord(Object departmentId, Object departmentName) {
        Connection connection = null;
        PreparedStatement statement = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 创建 PreparedStatement 对象
            String query;
            if (departmentId != null && departmentName != null) {
                // 同时修改院系编号和院系名称
                query = "UPDATE 院系信息表 SET 院系编号 = ?, 院系名称 = ? WHERE 院系编号 = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, Integer.parseInt(departmentId.toString()));
                statement.setString(2, departmentName.toString());
                statement.setInt(3, Integer.parseInt(departmentId.toString()));
            } else if (departmentId != null) {
                // 单独修改院系编号
                query = "UPDATE 院系信息表 SET 院系编号 = ? WHERE 院系编号 = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, Integer.parseInt(departmentId.toString()));
                statement.setInt(2, Integer.parseInt(departmentId.toString()));
            } else if (departmentName != null) {
                // 单独修改院系名称
                query = "UPDATE 院系信息表 SET 院系名称 = ? WHERE 院系名称 = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, departmentName.toString());
                statement.setString(2, departmentName.toString());
            }

            // 执行更新语句
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, null);
        }
    }

    //用人单位删除职业需求记录
    private void deleteJobRecord(int jobId) {
        Connection connection = null;
        PreparedStatement statement = null;
        String URL = "jdbc:mysql://localhost:3306/高校学生就业管理系统";
        String USERNAME = "root";
        String PASSWORD = "Lzy-200387";

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // 执行删除操作
            String deleteQuery = "DELETE FROM 职业信息表 WHERE 职业编号 = ?";
            statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, jobId);
            statement.executeUpdate();

            // 提示删除成功
            JOptionPane.showMessageDialog(Employers, "删除成功", "提示", JOptionPane.INFORMATION_MESSAGE);

            // 刷新表格或执行其他操作
            // ...
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            closeConnection(connection, statement, null);
        }
    }

    //用人单位进行查询需求的方法
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

    //用人单位进行发布需求的方法
    public void insertJobData(String jobName, String jobTypeName, int demandQuantity, Date deadline, String currentCompany) {
        if(jobName.isEmpty() || jobTypeName.isEmpty() || currentCompany.isEmpty() || deadline == null){
            JOptionPane.showMessageDialog(EmployersNeed,"必须填写全部信息","注意",JOptionPane.INFORMATION_MESSAGE);
        }
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
            int jobNumber = jobCount + 6;

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

    //用人单位查询界面填充表格的方法（从数据库中获取职业信息并填充到表格模型中）
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

    //通过目前登陆的账号获取当前账号的公司的方法
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

    //可以直接在jtable上进行修改的方法（用人单位版）
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

    //禁止修改表格中某几列的方法
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

    //将字符串转化为日期的方法
    public static Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 设置解析日期时不严格匹配模式，即宽松模式
            dateFormat.setLenient(false);
            java.util.Date utilDate = dateFormat.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            return  sqlDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //关闭数据库链接的方法
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

    public static void setLabelBackground(JLabel label, Color backgroundColor) {
        label.setOpaque(true); // 设置标签为不透明
        label.setBackground(backgroundColor); // 设置标签的背景颜色
        setlableBackgroundImage();
    }

    private static void setlableBackgroundImage(){
        JFrame frame = new JFrame("Label Background Setter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel label1 = new JLabel("Label 1");
        setLabelBackground(label1, Color.RED);
        panel.add(label1);

        JLabel label2 = new JLabel("Label 2");
        setLabelBackground(label2, Color.BLUE);
        panel.add(label2);
        frame.add(panel);
        frame.setVisible(true);
    }
}

