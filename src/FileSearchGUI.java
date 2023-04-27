import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.*;
import javax.swing.*;

public class FileSearchGUI {
    private JFrame frame = new JFrame("File Search");
    private JFileChooser fileChooser = new JFileChooser();
    private JTextArea originalTextArea = new JTextArea(20, 30);
    private JTextArea filteredTextArea = new JTextArea(20, 30);
    private JTextField searchField = new JTextField(20);

    public FileSearchGUI() {
        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);
        JButton loadButton = new JButton("Load File");
        JButton searchButton = new JButton("Search");
        JButton quitButton = new JButton("Quit");
        JPanel topPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JPanel mainPanel = new JPanel(new BorderLayout());

        topPanel.add(searchField);
        topPanel.add(searchButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(quitButton);
        centerPanel.add(originalScrollPane);
        centerPanel.add(filteredScrollPane);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.pack();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Action Listeners

        loadButton.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                Path filePath = fileChooser.getSelectedFile().toPath();
                try {
                    String fileContents = Files.readString(filePath);
                    originalTextArea.setText(fileContents);
                    filteredTextArea.setText("");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error loading file");
                }
            }
        });

        searchButton.addActionListener(e -> {
            String searchString = searchField.getText().trim().toLowerCase();
            if (!searchString.isEmpty()) {
                String originalText = originalTextArea.getText();
                try (Stream<String> lines = originalText.lines()) {
                    String filteredText = lines
                            .filter(line -> line.toLowerCase().contains(searchString)).collect(Collectors.joining("\n"));
                    filteredTextArea.setText(filteredText);
                }
            }
        });

        quitButton.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        FileSearchGUI fileSearchGUI = new FileSearchGUI();
        fileSearchGUI.frame.setVisible(true);
    }
}
