package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame {
    private final JTextArea textArea;
    private final JTextField textField;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;

    public Client() {
        setTitle("Chat Client");
        setBackground(Color.darkGray);

        textArea = new JTextArea();
        textArea.setMargin(new Insets(10,10,10,10));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        textArea.setBackground(Color.pink);

        // Create a panel for the text field and button
        JPanel panel = new JPanel();
        textField = new JTextField(20);
        JButton button = new JButton("Send");
        panel.setForeground(Color.lightGray);

        // Add action listener to the button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                if (!text.isEmpty()) {
                    sendMessage(text);
                    textField.setText(""); // Clear the text field
                }
            }
        });

        // Add the text field and button to the panel
        panel.add(textField);
        panel.add(button);

        // Add the panel to the frame
        add(panel, BorderLayout.SOUTH);

        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        try {
            Socket socket = new Socket("localhost", 1598);
            buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String messageFromServer;
                        while ((messageFromServer = buffReader.readLine()) != null) {
                            textArea.append("Server: " + messageFromServer + "\n");
                        }
                    } catch (IOException e) {
                        textArea.append("Error reading from server: " + e.getMessage() + "\n");
                    }
                }
            }).start();

        } catch (IOException e) {
            textArea.append("Error connecting to server: " + e.getMessage() + "\n");
        }
    }

    public void sendMessage(String message) {
        try {
            buffWriter.write(message);
            buffWriter.newLine();
            buffWriter.flush();
        } catch (IOException e) {
            textArea.append("Error sending message: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}