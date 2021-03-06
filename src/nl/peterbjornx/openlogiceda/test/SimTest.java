package nl.peterbjornx.openlogiceda.test;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
import nl.peterbjornx.openlogiceda.gui.SignalTracePane;
import nl.peterbjornx.openlogiceda.lib.Clock;
import nl.peterbjornx.openlogiceda.lib.SequentialComponent;
import nl.peterbjornx.openlogiceda.lib.Probe;
import nl.peterbjornx.openlogiceda.model.Circuit;
import nl.peterbjornx.openlogiceda.model.Net;
import nl.peterbjornx.openlogiceda.sim.Simulator;
import nl.peterbjornx.openlogiceda.util.ModificationException;
import nl.peterbjornx.openlogiceda.util.SimulationException;

import javax.swing.*;
import java.awt.*;

public class SimTest {

    public static void main(String[] args) throws ModificationException, SimulationException {
        Circuit test = new Circuit("root");
        Net a = new Net("a" );
        Net b = new Net("b" );
        Net c = new Net("c" );
        Clock ca = new Clock("ca", 2000 );
        Clock cb = new Clock("cb", 1123 );
        Probe pa = new Probe();
        Probe pb = new Probe();
        Probe pc = new Probe();
        test.connectNode(a, pa.getInput() );
        test.connectNode(b, pb.getInput() );
        test.connectNode(c, pc.getInput() );
        SequentialComponent comp = new SequentialComponent("and1", 2, 200,
                (v)-> (v[0] & v[1]));
        test.connectNode(a, comp.getInput(0));
        test.connectNode(b, comp.getInput(1));
        test.connectNode(c, comp.getOutput());
        test.connectNode(a, ca.getOutput());
        test.connectNode(b, cb.getOutput());
        Simulator sim = new Simulator();
        sim.start(test);
        for ( int i = 0; i < 50; i++ )
            sim.step();
        try {
            UIManager.setLookAndFeel(new GTKLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFrame tf = new JFrame("Test");
        SignalTracePane stf = new SignalTracePane();
        stf.addTrace(Color.RED, "A", pa.getHistory());
        stf.addTrace(Color.ORANGE, "B", pb.getHistory());
        stf.addTrace(Color.YELLOW, "A & B ", pc.getHistory());
        tf.add(stf.getMainPane());
        tf.setResizable(true);
        tf.setVisible(true);
    }
}
