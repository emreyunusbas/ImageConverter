import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageConverterUI extends JFrame {

    private JLabel statusLabel;
    private JButton selectButton;
    private JButton convertButton;

    private JFileChooser fileChooser;
    private File[] selectedFiles;
    private File outputFolder;

    public ImageConverterUI() {
        super("WebP to JPG Converter");

        setLayout(new FlowLayout());

        statusLabel = new JLabel("Select WebP files and choose output folder.");
        selectButton = new JButton("Select Files");
        convertButton = new JButton("Convert");

        add(statusLabel);
        add(selectButton);
        add(convertButton);

        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("WebP Files", "webp"));

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(ImageConverterUI.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFiles = fileChooser.getSelectedFiles();
                    statusLabel.setText(selectedFiles.length + " files selected.");
                }
            }
        });

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFiles == null || selectedFiles.length == 0) {
                    JOptionPane.showMessageDialog(ImageConverterUI.this, "No files selected!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JFileChooser outputChooser = new JFileChooser();
                outputChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int outputResult = outputChooser.showDialog(ImageConverterUI.this, "Select Output Folder");
                if (outputResult == JFileChooser.APPROVE_OPTION) {
                    outputFolder = outputChooser.getSelectedFile();
                    convertFiles();
                }
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void convertFiles() {
        for (File file : selectedFiles) {
            String outputFilePath = outputFolder.getPath() + File.separator + getFileNameWithoutExtension(file) + ".jpg";
            convertFile(file, outputFilePath);
        }

        JOptionPane.showMessageDialog(this, "Conversion completed!", "Success", JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText("Select WebP files and choose output folder.");
    }

    private void convertFile(File inputFile, String outputFilePath) {
        try {
            BufferedImage webpImage = ImageIO.read(inputFile);
            File outputFile = new File(outputFilePath);
            ImageIO.write(webpImage, "jpg", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileNameWithoutExtension(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ImageConverterUI().setVisible(true);
            }
        });
    }
}
