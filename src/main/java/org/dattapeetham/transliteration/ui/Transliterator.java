package org.dattapeetham.transliteration.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.UIManager;
import org.dattapeetham.transliteration.FileTransliterator;
import org.dattapeetham.transliteration.ICUHelper;

public class Transliterator extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final String newline = "\n";
    JButton openButton;
    JButton transliterateButton;
    JButton tranliterateClpBoardButton;
    JTextArea log = new JTextArea(10, 80);
    JFileChooser fc;
    JComboBox<Object> inputOptions;
    JComboBox<Object> outputOptions;
    private File inputFile;

    public Transliterator() {
        super(new BorderLayout());
        this.log.setMargin(new Insets(5, 5, 5, 5));
        this.log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(this.log);
        this.fc = new JFileChooser();
        this.outputOptions = new JComboBox<>(ICUHelper.getAvailableTargets("Telugu").toArray());
        this.outputOptions.addItem("Telugu");
        this.outputOptions.setSelectedItem("Devanagari");
        JPanel buttonPanel = new JPanel();
        this.openButton = createButton("Select Source File : ", buttonPanel);
        this.transliterateButton = createButton(" Transliterate ", buttonPanel);
        this.tranliterateClpBoardButton = createButton(" Trans Clipboard ", buttonPanel);
        buttonPanel.add(this.outputOptions);
        this.add((Component) buttonPanel, "First");
        this.add((Component) logScrollPane, "Center");
    }

    private JButton createButton(String buttonLabel, JPanel buttonPanel) {
        JButton button = new JButton(buttonLabel);
        button.addActionListener(this);
        buttonPanel.add(button);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int returnVal;
        if (e.getSource() == this.openButton) {
            returnVal = this.fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                this.inputFile = this.fc.getSelectedFile();
                this.log.append("Opening: " + this.inputFile.getName() + "." + newline);
            } else {
                this.log.append("Open command cancelled by user.\n");
            }
            this.log.setCaretPosition(this.log.getDocument().getLength());
        } else if (e.getSource() == this.transliterateButton) {
            if (this.inputFile == null) {
                this.log.append("Please click Select Source Button to select input file and then click transliterate \n");
                return;
            }
        } else if (e.getSource() == this.tranliterateClpBoardButton) {
            this.log.append("Trans Clipboard button selected.\n");
            transliterateClipBoard();
            return;
        }
        returnVal = this.fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = this.fc.getSelectedFile();
            long start = System.currentTimeMillis();
            this.log.append("Transliterating to : " + file.getName() + "." + newline);
            FileTransliterator.transliterateFile(getSelectedLanguage(), this.inputFile, file);
            this.log.append("Done transliterating in " + (double) (System.currentTimeMillis() - start) / 1000.0 + " seconds, Saved to: " + file.getName() + "." + newline);
            try {
                Runtime.getRuntime().exec("open " + file.getAbsolutePath());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else {
            this.log.append("Save command cancelled by user.\n");
        }
        this.log.setCaretPosition(this.log.getDocument().getLength());
    }

    private String getSelectedLanguage() {
        return this.outputOptions.getSelectedItem().toString();
    }

    private void transliterateClipBoard() {
        ClipboardTransfer transfer = new ClipboardTransfer();
        String string = transfer.getClipboardContents();
        String outputStr = ICUHelper.transliterate(string, getSelectedLanguage());
        transfer.setClipboardContents(outputStr);
        this.log.append("Transliterated and applied to clipboard");
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Transliterator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Transliterator());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Transliterator.createAndShowGUI();
            }
        });
    }
}
