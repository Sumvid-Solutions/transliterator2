package org.dattapeetham.transliteration.ui;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.dattapeetham.transliteration.FileTransliterator;
import org.dattapeetham.transliteration.ICUHelper;

public class Transliterator extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JTextArea log = new JTextArea(10, 80);
    private final JFileChooser fc = new JFileChooser();
    private final JComboBox<String> outputOptions;
    private File inputFile;

    public Transliterator() {
        super(new BorderLayout());
        log.setMargin(new Insets(5, 5, 5, 5));
        log.setEditable(false);
        var logScrollPane = new JScrollPane(log);

        outputOptions = new JComboBox<>(ICUHelper.getAvailableTargets("Telugu").toArray(new String[0]));
        outputOptions.addItem("Telugu");
        outputOptions.setSelectedItem("Devanagari");

        var buttonPanel = new JPanel();
        createButton("Select Source File", buttonPanel, e -> selectSourceFile());
        createButton("Transliterate", buttonPanel, e -> transliterateFile());
        createButton("Trans Clipboard", buttonPanel, e -> transliterateClipBoard());
        buttonPanel.add(outputOptions);

        add(buttonPanel, BorderLayout.NORTH);
        add(logScrollPane, BorderLayout.CENTER);
    }

    private JButton createButton(String label, JPanel panel, java.awt.event.ActionListener action) {
        var button = new JButton(label);
        button.addActionListener(action);
        panel.add(button);
        return button;
    }

    private void selectSourceFile() {
        var returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            inputFile = fc.getSelectedFile();
            log.append("Opening: " + inputFile.getName() + ".\n");
        } else {
            log.append("Open command cancelled by user.\n");
        }
        log.setCaretPosition(log.getDocument().getLength());
    }

    private void transliterateFile() {
        if (inputFile == null) {
            log.append("Please select a source file first.\n");
            return;
        }
        var returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            var file = fc.getSelectedFile();
            var start = System.currentTimeMillis();
            log.append("Transliterating to: " + file.getName() + ".\n");
            try {
                FileTransliterator.transliterateFile(getSelectedLanguage(), inputFile, file);
                log.append("Done in " + (System.currentTimeMillis() - start) / 1000.0 + " seconds. Saved to: " + file.getName() + ".\n");
                openFile(file);
            } catch (Exception ex) {
                log.append("Error during transliteration: " + ex.getMessage() + "\n");
            }
        } else {
            log.append("Save command cancelled by user.\n");
        }
        log.setCaretPosition(log.getDocument().getLength());
    }

    private void openFile(File file) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getSelectedLanguage() {
        Object selected = outputOptions.getSelectedItem();
        return selected != null ? selected.toString() : "Devanagari";
    }

    private void transliterateClipBoard() {
        var transfer = new ClipboardTransfer();
        var input = transfer.getClipboardContents();
        var output = ICUHelper.transliterate(input, getSelectedLanguage());
        transfer.setClipboardContents(output);
        log.append("Transliterated and applied to clipboard.\n");
        log.setCaretPosition(log.getDocument().getLength());
    }

    private static void createAndShowGUI() {
        var frame = new JFrame("Transliterator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Transliterator());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
            createAndShowGUI();
        });
    }
}
