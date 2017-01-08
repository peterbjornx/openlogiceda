package nl.peterbjornx.openlogiceda;

import nl.peterbjornx.openlogiceda.lib.Clock;
import nl.peterbjornx.openlogiceda.lib.CombinatorialComponent;
import nl.peterbjornx.openlogiceda.lib.Probe;
import nl.peterbjornx.openlogiceda.model.Circuit;
import nl.peterbjornx.openlogiceda.model.Net;
import nl.peterbjornx.openlogiceda.sim.Simulator;
import nl.peterbjornx.openlogiceda.util.ModificationException;
import nl.peterbjornx.openlogiceda.util.SimulationException;

public class Main {

    public static void main(String[] args) throws ModificationException, SimulationException {
        Circuit test = new Circuit("root");
        Net a = new Net("a" );
        Net b = new Net("b" );
        Net c = new Net("c" );
        Clock ca = new Clock("ca", 2000 );
        Clock cb = new Clock("cb", 4000 );
        test.connectNode(a, new Probe().getInput() );
        test.connectNode(b, new Probe().getInput() );
        test.connectNode(c, new Probe().getInput() );
        CombinatorialComponent comp = new CombinatorialComponent("and1", 2, 200,
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
    }
}
