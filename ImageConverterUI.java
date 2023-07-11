import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ImageConverterUI extends JFrame {
    private JLabel label;
    private JButton selectButton;
    private JButton convertButton;
    private File[] selectedFiles;

    public ImageConverterUI() {
        setTitle("Image Converter");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        label = new JLabel("No files selected.");
        label.setHorizontalAlignment(JLabel.CENTER);

        selectButton = new JButton("Select Files");
        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFiles = fileChooser.getSelectedFiles();
                    label.setText(selectedFiles.length + " files selected.");
                }
            }
        });

        convertButton = new JButton("Convert and Save");
        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedFiles == null || selectedFiles.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please select files to convert.");
                    return;
                }

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedDirectory = fileChooser.getSelectedFile();
                    for (File file : selectedFiles) {
                        try {
                            String newFilePath = selectedDirectory.getAbsolutePath() + File.separator + file.getName().replace(".webp", ".jpg");
                            Path sourcePath = file.toPath();
                            Path destinationPath = new File(newFilePath).toPath();
                            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Conversion completed successfully!");
                }
            }
        });

        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(label);
        panel.add(selectButton);
        panel.add(convertButton);
        add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ImageConverterUI converterUI = new ImageConverterUI();
                converterUI.setVisible(true);
            }
        });
    }
}
