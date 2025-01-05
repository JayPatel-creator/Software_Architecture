package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DepotGUI extends JFrame {
    private final JTextArea parcelTextArea;
    private final JTextArea customerTextArea;
    private final JButton processCustomerButton;
    private final JButton addCustomerButton;
    private final JButton addParcelButton;
    private final JTextField searchParcelField;
    private final JButton searchParcelButton;
    private final JButton updateParcelStatusButton;
    private final JButton removeCustomerButton;
    private JComboBox<String> sortComboBox;

    public DepotGUI() {
        setTitle("Depot Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        // Initialize components
        parcelTextArea = createTextArea();
        customerTextArea = createTextArea();
        processCustomerButton = createButton("Process Customer","Process the next customer in line.");
        addCustomerButton = createButton("Add Customer", "Add a new customer to the queue.");
        addParcelButton = createButton("Add Parcel", "Add a new parcel to the system.");
        updateParcelStatusButton = createButton("Update Status","Update the status of a parcel.");
        removeCustomerButton = createButton("Remove Customer", "Remove a customer from the queue.");
        searchParcelField = new JTextField(15);
        searchParcelButton = createButton("Search", "Search for a specific parcel by ID.");

        // Build UI Components
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Parcels", createScrollPanel(parcelTextArea));
        tabbedPane.addTab("Customer Queue", createScrollPanel(customerTextArea));

        JPanel buttonPanel = createButtonPanel();
        JPanel searchPanel = createSearchPanel();
        JPanel sortPanel = createSortPanel();

        // Add components to the frame
        add(tabbedPane, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(sortPanel, BorderLayout.WEST);
    }

    private JPanel createSortPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Sort Parcels"));

        String[] sortOptions = {"Ascending", "Descending"};
        sortComboBox = new JComboBox<>(sortOptions);
        panel.add(sortComboBox);

        return panel;
    }

    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        return textArea;
    }

    private JButton createButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.setFocusPainted(false);
        return button;
    }

    private JScrollPane createScrollPanel(JTextArea textArea) {
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(processCustomerButton);
        panel.add(addCustomerButton);
        panel.add(addParcelButton);
        panel.add(updateParcelStatusButton);
        panel.add(removeCustomerButton);
        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Search Parcel"));
        panel.add(new JLabel("Parcel ID:"));
        panel.add(searchParcelField);
        panel.add(searchParcelButton);
        return panel;
    }

    // Getters for components
    public JTextArea getParcelTextArea() {
        return parcelTextArea;
    }

    public JTextArea getCustomerTextArea() {
        return customerTextArea;
    }

    public JButton getProcessCustomerButton() {
        return processCustomerButton;
    }

    public JButton getAddCustomerButton() {
        return addCustomerButton;
    }

    public JButton getAddParcelButton() {
        return addParcelButton;
    }

    public JTextField getSearchParcelField() {
        return searchParcelField;
    }

    public JButton getSearchParcelButton() {
        return searchParcelButton;
    }

    public JButton getUpdateParcelStatusButton() {
        return updateParcelStatusButton;
    }

    public JButton getRemoveCustomerButton() {
        return removeCustomerButton;
    }

    public JComboBox<String> getSortComboBox() {
        return sortComboBox;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DepotGUI gui = new DepotGUI();
            gui.setVisible(true);
        });
    }
}
