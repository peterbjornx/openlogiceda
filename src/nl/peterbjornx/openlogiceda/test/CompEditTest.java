package nl.peterbjornx.openlogiceda.test;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
import nl.peterbjornx.openlogiceda.gui.SignalTracePane;
import nl.peterbjornx.openlogiceda.gui.schem.ComponentEditor;
import nl.peterbjornx.openlogiceda.lib.Clock;
import nl.peterbjornx.openlogiceda.lib.Probe;
import nl.peterbjornx.openlogiceda.lib.SequentialComponent;
import nl.peterbjornx.openlogiceda.model.Circuit;
import nl.peterbjornx.openlogiceda.model.Net;
import nl.peterbjornx.openlogiceda.sim.Simulator;
import nl.peterbjornx.openlogiceda.util.ModificationException;
import nl.peterbjornx.openlogiceda.util.SimulationException;

import javax.swing.*;
import java.awt.*;

public class CompEditTest {

    public static void main(String[] args) throws ModificationException, SimulationException {
        try {
            UIManager.setLookAndFeel(new GTKLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        ComponentEditor ed = new ComponentEditor();
        JFrame tf = new JFrame("Test");
        tf.add(ed.getMainPane());
        tf.setResizable(true);
        tf.setVisible(true);
        ed.postInit();
    }
}
