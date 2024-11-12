/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication39;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


public class JavaApplication39 extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/yash23";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12345678";

    private JTextField nameField, fatherNameField, classField, feesField, emailField;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    public JavaApplication39() {
        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
        JFrame welcomeFrame = new JFrame("Welcome");
        welcomeFrame.setSize(400, 200);
        welcomeFrame.setLayout(null);
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel welcomeLabel = new JLabel("Welcome to My School Management Project");
        welcomeLabel.setBounds(50, 50, 300, 30);
        welcomeFrame.add(welcomeLabel);

        JButton okButton = new JButton("OK");
        okButton.setBounds(150, 100, 100, 30);
        welcomeFrame.add(okButton);

        // Action for OK button
        okButton.addActionListener(e -> {
            welcomeFrame.dispose();
            showClassSelection();
        });

        // KeyListener to handle Enter key
        welcomeFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    welcomeFrame.dispose();
                    showClassSelection();
                }
            }
        });

        welcomeFrame.setContentPane(new ImagePanel("C:\\Users\\486ya\\Downloads\\School Management System - login page.PNG"));
        welcomeFrame.setVisible(true);
        welcomeFrame.setFocusable(true);
        welcomeFrame.requestFocusInWindow(); // Ensure the frame is focused to listen for key events
    }

    private void showClassSelection() {
        JFrame classFrame = new JFrame("Select Class");
        classFrame.setSize(400, 400);
        classFrame.setLayout(new GridLayout(12, 1));
        classFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for (int i = 1; i <= 12; i++) {
            final int classNumber = i; // Declare final variable
            JButton classButton = new JButton("Class " + i);
            classButton.addActionListener(e -> {
                classFrame.dispose();
                openStudentDetails(classNumber); // Use the final variable
            });
            classFrame.add(classButton);
        }

        classFrame.setVisible(true);
    }

    private void openStudentDetails(int classNumber) {
        JFrame detailsFrame = new JFrame("Class " + classNumber + " Student Details");
        detailsFrame.setSize(800, 600);
        detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        detailsFrame.setLayout(null);

        nameField = new JTextField();
        fatherNameField = new JTextField();
        classField = new JTextField(String.valueOf(classNumber));
        feesField = new JTextField();
        emailField = new JTextField();

        setupForm(detailsFrame);
        setupTable(detailsFrame, classNumber);

        detailsFrame.setVisible(true);
    }

    private void setupForm(JFrame frame) {
        // Add form components
        JLabel nameLabel = new JLabel("Student Name:");
        nameLabel.setBounds(20, 20, 150, 30);
        frame.add(nameLabel);
        nameField.setBounds(150, 20, 200, 30);
        frame.add(nameField);

        JLabel fatherNameLabel = new JLabel("Father's Name:");
        fatherNameLabel.setBounds(20, 60, 150, 30);
        frame.add(fatherNameLabel);
        fatherNameField.setBounds(150, 60, 200, 30);
        frame.add(fatherNameField);

        JLabel classLabel = new JLabel("Class:");
        classLabel.setBounds(20, 100, 150, 30);
        frame.add(classLabel);
        classField.setBounds(150, 100, 200, 30);
        frame.add(classField);

        JLabel feesLabel = new JLabel("Fees:");
        feesLabel.setBounds(20, 140, 150, 30);
        frame.add(feesLabel);
        feesField.setBounds(150, 140, 200, 30);
        frame.add(feesField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 180, 150, 30);
        frame.add(emailLabel);
        emailField.setBounds(150, 180, 200, 30);
        frame.add(emailField);

        // Add buttons
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(150, 220, 100, 30);
        frame.add(submitButton);

        JButton viewButton = new JButton("See Details");
        viewButton.setBounds(270, 220, 100, 30);
        frame.add(viewButton);

        JButton updateButton = new JButton("Update");
        updateButton.setBounds(150, 260, 100, 30);
        frame.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(270, 260, 100, 30);
        frame.add(deleteButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBounds(210, 300, 100, 30);
        frame.add(refreshButton);
        
        // Add new button to fetch all students
        JButton fetchAllButton = new JButton("Fetch All Students");
        fetchAllButton.setBounds(150, 340, 220, 30);
        frame.add(fetchAllButton);

        // Button actions
        submitButton.addActionListener(e -> addStudent(classField.getText()));
        viewButton.addActionListener(e -> loadStudents(classField.getText()));
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        refreshButton.addActionListener(e -> loadStudents(classField.getText()));
        
        // Action for Fetch All Students button
        fetchAllButton.addActionListener(e -> fetchAllStudents());
    }

    private void setupTable(JFrame frame, int classNumber) {
        studentTable = new JTable();
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Name", "Father's Name", "Class", "Fees", "Email"});
        studentTable.setModel(tableModel);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBounds(20, 350, 740, 200);
        frame.add(scrollPane);
    }

    private Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private void addStudent(String studentClass) {
        String name = nameField.getText();
        String fatherName = fatherNameField.getText();
        String fees = feesField.getText();
        String email = emailField.getText();

        try (Connection con = connectToDatabase();
             PreparedStatement ps = con.prepareStatement("INSERT INTO students (name, father_name, class, fees, email) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, fatherName);
            ps.setString(3, studentClass);
            ps.setString(4, fees);
            ps.setString(5, email);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student added successfully");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding student");
        }
    }

    private void loadStudents(String studentClass) {
        tableModel.setRowCount(0);

        try (Connection con = connectToDatabase();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM students WHERE class = ?")) {
            ps.setString(1, studentClass);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String fatherName = rs.getString("father_name");
                String fees = rs.getString("fees");
                String email = rs.getString("email");
                tableModel.addRow(new Object[]{id, name, fatherName, studentClass, fees, email});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading students");
        }
    }

    private void fetchAllStudents() {
        JFrame allStudentsFrame = new JFrame("All Students");
        allStudentsFrame.setSize(800, 600);
        allStudentsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        allStudentsFrame.setLayout(new BorderLayout());

        JTable allStudentsTable = new JTable();
        DefaultTableModel allStudentsModel = new DefaultTableModel();
        allStudentsModel.setColumnIdentifiers(new String[]{"ID", "Name", "Father's Name", "Class", "Fees", "Email"});
        allStudentsTable.setModel(allStudentsModel);

        JScrollPane scrollPane = new JScrollPane(allStudentsTable);
        allStudentsFrame.add(scrollPane, BorderLayout.CENTER);

        try (Connection con = connectToDatabase();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM students");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String fatherName = rs.getString("father_name");
                String fees = rs.getString("fees");
                String email = rs.getString("email");
                allStudentsModel.addRow(new Object[]{id, name, fatherName, rs.getString("class"), fees, email});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching all students");
        }

        allStudentsFrame.setVisible(true);
    }

    private void updateStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to update");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = nameField.getText();
        String fatherName = fatherNameField.getText();
        String studentClass = classField.getText();
        String fees = feesField.getText();
        String email = emailField.getText();

        try (Connection con = connectToDatabase();
             PreparedStatement ps = con.prepareStatement("UPDATE students SET name = ?, father_name = ?, class = ?, fees = ?, email = ? WHERE id = ?")) {
            ps.setString(1, name);
            ps.setString(2, fatherName);
            ps.setString(3, studentClass);
            ps.setString(4, fees);
            ps.setString(5, email);
            ps.setInt(6, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student updated successfully");
            loadStudents(studentClass); // Reload the table after update
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating student");
        }
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection con = connectToDatabase();
             PreparedStatement ps = con.prepareStatement("DELETE FROM students WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student deleted successfully");
            loadStudents(classField.getText()); // Reload the table after deletion
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting student");
        }
    }

    private void clearFields() {
        nameField.setText("");
        fatherNameField.setText("");
        classField.setText("");
        feesField.setText("");
        emailField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JavaApplication39());
    }
}

// Panel for background image
class ImagePanel extends JPanel {
    private Image backgroundImage;

    public ImagePanel(String filePath) {
        this.backgroundImage = Toolkit.getDefaultToolkit().getImage(filePath);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}





