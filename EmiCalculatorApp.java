import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmiCalculatorApp extends JFrame implements ActionListener {

    private JTextField principalField, rateField, timeField, nameField;
    private JComboBox<String> timeUnitComboBox, loanTypeComboBox;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    
    // Default interest rates for different loan types
    private final double BANK_LOAN_RATE = 8.5;
    private final double CAR_LOAN_RATE = 7.0;
    private final double EDUCATION_LOAN_RATE = 10.0;
    private final double PERSONAL_LOAN_RATE = 12.0;

    public EmiCalculatorApp() {
        setTitle("EMI Calculator");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);  // Center window on screen

        // Main Panel for Padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));  // Add padding around content
        mainPanel.setBackground(Color.decode("#F5F5F5"));
        add(mainPanel, BorderLayout.CENTER);

        // Input Panel with Grid Layout
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));  // Increased grid size for new button
        inputPanel.setBackground(Color.decode("#F5F5F5"));

        // User Name
        JLabel nameLabel = new JLabel("Your Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        inputPanel.add(nameLabel);

        nameField = new JTextField();
        inputPanel.add(nameField);

        // Loan Type
        JLabel loanTypeLabel = new JLabel("Loan Type:");
        loanTypeLabel.setFont(new Font("Arial", Font.BOLD, 13));
        inputPanel.add(loanTypeLabel);

        String[] loanTypes = {"Bank Loan", "Car Loan", "Education Loan", "Personal Loan"};
        loanTypeComboBox = new JComboBox<>(loanTypes);
        loanTypeComboBox.addActionListener(e -> updateInterestRate());
        inputPanel.add(loanTypeComboBox);

        // Principal Amount
        JLabel principalLabel = new JLabel("Principal Amount:");
        principalLabel.setFont(new Font("Arial", Font.BOLD, 13));
        inputPanel.add(principalLabel);

        principalField = new JTextField();
        inputPanel.add(principalField);

        // Annual Interest Rate
        JLabel rateLabel = new JLabel("Annual Interest Rate (%):");
        rateLabel.setFont(new Font("Arial", Font.BOLD, 13));
        inputPanel.add(rateLabel);

        rateField = new JTextField();
        inputPanel.add(rateField);

        // Time
        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 13));
        inputPanel.add(timeLabel);

        timeField = new JTextField();
        inputPanel.add(timeField);

        // Time Unit
        JLabel timeUnitLabel = new JLabel("Time Unit:");
        timeUnitLabel.setFont(new Font("Arial", Font.BOLD, 13));
        inputPanel.add(timeUnitLabel);

        String[] timeUnits = {"Months", "Years"};
        timeUnitComboBox = new JComboBox<>(timeUnits);
        inputPanel.add(timeUnitComboBox);

        // Calculate Button with Custom Style
        JButton calculateButton = new JButton("Calculate EMI");
        calculateButton.setBackground(Color.decode("#4CAF50"));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFocusPainted(false);
        calculateButton.setFont(new Font("Arial", Font.BOLD, 14));
        calculateButton.addActionListener(this);

        inputPanel.add(new JLabel());  // Filler cell for alignment
        inputPanel.add(calculateButton);

        // Clear Table Button
        JButton clearButton = new JButton("Clear Table");
        clearButton.setBackground(Color.decode("#FF5733"));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.addActionListener(e -> clearTable());  // Add clearTable() method for clearing

        inputPanel.add(new JLabel());  // Filler cell for alignment
        inputPanel.add(clearButton);  // Add the button to the layout

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Table for showing EMI, total interest, total payable amount, and user name
        String[] columnNames = {"Name", "EMI", "Total Interest", "Total Payable Amount"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultTable = new JTable(tableModel);
        resultTable.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Use a font that supports ₹ symbol
        resultTable.setRowHeight(25);

        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Calculation Results"));
        tableScrollPane.setBackground(Color.decode("#F5F5F5"));
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        setVisible(true);

        // Set the default interest rate based on the selected loan type
        updateInterestRate();
    }

    private void updateInterestRate() {
        String selectedLoanType = (String) loanTypeComboBox.getSelectedItem();
        double defaultRate = 0.0;

        switch (selectedLoanType) {
            case "Bank Loan":
                defaultRate = BANK_LOAN_RATE;
                break;
            case "Car Loan":
                defaultRate = CAR_LOAN_RATE;
                break;
            case "Education Loan":
                defaultRate = EDUCATION_LOAN_RATE;
                break;
            case "Personal Loan":
                defaultRate = PERSONAL_LOAN_RATE;
                break;
        }

        // Set the default rate in the rate field, and allow users to change it
        rateField.setText(String.valueOf(defaultRate));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String name = nameField.getText();  // Get the user's name

            // Check if the name is not empty
            if (name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double principal = Double.parseDouble(principalField.getText());
            double annualRate = Double.parseDouble(rateField.getText());
            int time = Integer.parseInt(timeField.getText());
            String timeUnit = (String) timeUnitComboBox.getSelectedItem();

            // Convert years to months if necessary
            int months = timeUnit.equals("Years") ? time * 12 : time;
            double monthlyRate = annualRate / (12 * 100);

            // EMI Calculation
            double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, months)) / 
                         (Math.pow(1 + monthlyRate, months) - 1);

            // Calculate total payable amount and total interest
            double totalPayable = emi * months;
            double totalInterest = totalPayable - principal;

            // Format results with Rupees symbol
            String emiFormatted = String.format("%.2f", emi);
            String totalInterestFormatted = String.format("%.2f", totalInterest);
            String totalPayableFormatted = String.format("%.2f", totalPayable);

            // Add the user's name and the results to the table
            tableModel.addRow(new Object[]{name, emiFormatted, totalInterestFormatted, totalPayableFormatted});

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid input values.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to clear the table
    private void clearTable() {
        tableModel.setRowCount(0);  // This clears all rows in the table
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmiCalculatorApp::new);
    }
}
