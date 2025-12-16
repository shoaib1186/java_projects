import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.util.ArrayList;

public class StudentGradingSystem extends JFrame {
    // Fields
    private JTextField studentNameField;
    private JTextField studentGradeField;
    private JButton addButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private ArrayList<Student> students;

    // Constructor to set up the GUI
    public StudentGradingSystem() {
        setTitle("Student Grading System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        students = new ArrayList<>();
        
        // Panel for form inputs
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        
        inputPanel.add(new JLabel("Student Name:"));
        studentNameField = new JTextField();
        inputPanel.add(studentNameField);
        
        inputPanel.add(new JLabel("Grade:"));
        studentGradeField = new JTextField();
        inputPanel.add(studentGradeField);
        
        // Add button to add student
        addButton = new JButton("Add Student");
        inputPanel.add(addButton);
        
        // Table for displaying students and their grades
        String[] columnNames = {"Student Name", "Grade", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);

        // Add components to the frame
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        
        // Add button listener
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });
    }

    // Method to add a student to the table and list
    private void addStudent() {
        String name = studentNameField.getText().trim();
        String gradeText = studentGradeField.getText().trim();

        if (name.isEmpty() || gradeText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all fields.");
            return;
        }

        try {
            double gradeValue = Double.parseDouble(gradeText);
            
            // Determine status based on the grade
            String status = (gradeValue >= 50) ? "Pass" : "Fail";

            // Create a new student and add it to the list and table
            Student newStudent = new Student(name, gradeValue, status);
            students.add(newStudent);
            tableModel.addRow(new Object[]{name, gradeValue, status});
            
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Grade must be a valid number.");
        }
    }

    // Clears input fields after adding a student
    private void clearFields() {
        studentNameField.setText("");
        studentGradeField.setText("");
    }

    // Student class to store the data
    class Student {
        private String name;
        private double grade;
        private String status;

        public Student(String name, double grade, String status) {
            this.name = name;
            this.grade = grade;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public double getGrade() {
            return grade;
        }

        public String getStatus() {
            return status;
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentGradingSystem().setVisible(true);
            }
        });
    }
}
