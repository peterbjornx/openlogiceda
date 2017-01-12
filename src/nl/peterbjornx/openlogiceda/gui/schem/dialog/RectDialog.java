package nl.peterbjornx.openlogiceda.gui.schem.dialog;

import nl.peterbjornx.openlogiceda.model.schem.CompRectPart;
import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.event.*;

public class RectDialog extends JDialog {
    private final CompRectPart part;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField lineWidthTextField;
    private JButton colourButton;

    public RectDialog(CompRectPart p) {
        setTitle("Edit rectangle");
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
        part = p;
        colourButton.setForeground(p.getLineColour());
        lineWidthTextField.setText(String.valueOf(p.getLineWidth()));

        colourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colourButton.setForeground(ColourDialog.choose(contentPane,colourButton.getForeground()));
            }
        });
    }

    private void onOK() {
        // add your code here
        try {
            Integer.parseInt(lineWidthTextField.getText());
        } catch ( Exception e ) {
            JOptionPane.showMessageDialog(this,
                    "Invalid number for linewidth!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        part.setLineWidth(Integer.parseInt(lineWidthTextField.getText()));
        part.setLineColour(colourButton.getForeground());
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(JComponent c, CompRectPart p) {
        RectDialog dialog = new RectDialog(p);
        dialog.pack();
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
    }
}
