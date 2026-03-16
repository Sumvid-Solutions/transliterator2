package org.dattapeetham.transliteration.ui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ClipboardTransfer implements ClipboardOwner {

    public static void main(String[] aArguments) {
        var textTransfer = new ClipboardTransfer();
        System.out.println("Clipboard contains:" + textTransfer.getClipboardContents());
        textTransfer.setClipboardContents("blah, blah, blah");
        System.out.println("Clipboard contains:" + textTransfer.getClipboardContents());
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable transferable) {
    }

    public void setClipboardContents(String aString) {
        var stringSelection = new StringSelection(aString);
        var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    public String getClipboardContents() {
        var result = "";
        var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        var contents = clipboard.getContents(null);
        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
