package nl.peterbjornx.openlogiceda.test;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
import nl.peterbjornx.openlogiceda.gui.schem.ComponentEditor;
import nl.peterbjornx.openlogiceda.gui.schem.SchematicEditor;
import nl.peterbjornx.openlogiceda.util.ModificationException;
import nl.peterbjornx.openlogiceda.util.SimulationException;

import javax.swing.*;

public class SchemEditTest {

    public static void main(String[] args) throws ModificationException, SimulationException {
        try {
            UIManager.setLookAndFeel(new GTKLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SchematicEditor ed = new SchematicEditor();
        JFrame tf = new JFrame("Test");
        tf.add(ed.getMainPane());
        tf.setJMenuBar(ed.getMenuBar());
        tf.setResizable(true);
        tf.setVisible(true);
        ed.postInit();
    }
}
