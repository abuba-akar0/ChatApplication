package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame {
    private final JTextArea serverTextArea;
    private final JTextArea clientTextArea;
    private final JTextField textField;
    private final JButton button;

    public ServerGUI() {
        setTitle("Chat Server");
        setBackground(Color.darkGray);

        // Create a label for the credits
        JLabel creditsLabel = new JLabel("Developed by Abubakar, Fahad and Bakhtawar.");
        creditsLabel.setForeground(Color.gray);

        // Add the credits label to the frame
        add(creditsLabel, BorderLayout.NORTH);

        serverTextArea = new JTextArea();
        serverTextArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane serverScrollPane = new JScrollPane(serverTextArea);
        serverScrollPane.setPreferredSize(new Dimension(300, 300)); // Set preferred size here
        add(serverScrollPane, BorderLayout.WEST);
        serverTextArea.setBackground(Color.LIGHT_GRAY);

        clientTextArea = new JTextArea();
        clientTextArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane clientScrollPane = new JScrollPane(clientTextArea);
        clientScrollPane.setPreferredSize(new Dimension(300, 300)); // Set preferred size here
        add(clientScrollPane, BorderLayout.EAST);
        clientTextArea.setBackground(Color.lightGray);

        // Create a panel for the label, text field and button
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter message:");
        textField = new JTextField(20);
        button = new JButton("Send");

        // Add action listener to the button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                if (!text.isEmpty()) {
                    addServerMessage(text);
                    ClientHandler.boradcastMessage(text);
                    textField.setText(""); // Clear the text field
                }
            }
        });

        // Change button color when pressed
        button.getModel().addChangeListener(e -> {
            ButtonModel model = (ButtonModel) e.getSource();
            if (model.isPressed()) {
                button.setBackground(Color.GRAY);
            } else {
                button.setBackground(null);
            }
        });

        // Add the label, text field and button to the panel
        panel.add(label);
        panel.add(textField);
        panel.add(button);

        // Add the panel to the frame
        add(panel, BorderLayout.SOUTH);

        setSize(600, 400); // Adjust the size of the JFrame to accommodate the larger text area
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void addServerMessage(String message) {
        serverTextArea.append("Server: " + message + "\n");
    }

    public void addClientMessage(String message) {
        clientTextArea.append("Client: " + message + "\n");
    }
}