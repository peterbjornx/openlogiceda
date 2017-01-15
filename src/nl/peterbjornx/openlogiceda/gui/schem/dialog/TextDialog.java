package nl.peterbjornx.openlogiceda.gui.schem.dialog;

import nl.peterbjornx.openlogiceda.gui.common.ColourDialog;
import nl.peterbjornx.openlogiceda.model.schem.TextPart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TextDialog extends JDialog {
    private final TextPart part;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textTextField;
    private JButton colourButton;
    private JTextField sizeTextField;

    public TextDialog(TextPart p) {
        setTitle("Edit text");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        sizeTextField.setText(String.valueOf(p.getFontSize()));
        textTextField.setText(p.getText());
        colourButton.setForeground(p.getTextColour());
        colourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colourButton.setForeground(ColourDialog.choose(contentPane, colourButton.getForeground()));
            }
        });
        part = p;
    }

    private void onOK() {
        try {
            Integer.parseInt(sizeTextField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid number for size!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        part.setFontSize(Integer.parseInt(sizeTextField.getText()));
        part.setTextColour(colourButton.getForeground());
        part.setText(textTextField.getText());
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(JComponent c, TextPart p) {
        TextDialog dialog = new TextDialog(p);
        dialog.pack();
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
    }

}
