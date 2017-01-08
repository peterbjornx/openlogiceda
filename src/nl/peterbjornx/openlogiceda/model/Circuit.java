package nl.peterbjornx.openlogiceda.model;

import nl.peterbjornx.openlogiceda.util.ModificationException;

import java.util.LinkedList;
import java.util.List;

/**
 * This class describes an electric circuit
 * It functions as a netlist.
 */
public class Circuit extends Component {

    /**
     * The nets contained in this circuit.
     */
    private List<Net> nets = new LinkedList<Net>();

    /**
     * The components contained in this circuit
     */
    private List<Component> components = new LinkedList<>();

    /**
     * Add a component to this circuit.
     * @param component
     * @throws ModificationException
     */
    public void addComponent(Component component) throws ModificationException {
        if ( component.circuit == this )
            return;
        else if ( component.circuit != null )
            throw new ModificationException("component is already on another circuit");

        component.circuit = this;
        components.add(component);
    }

    /**
     * Connect a node to a net
     * @param net The net to connect to.
     * @param node The node to connect.
     * @throws ModificationException An invalid modification was attempted.
     */
    public void connectNode(Net net, Node node) throws ModificationException {

        if ( node.net == net )
            return;
        else if ( node.net != null )
            throw new ModificationException("node is already on another net");

        if ( node.component.circuit == null )
            node.component.circuit = this;
        else if ( node.component.circuit != this ) {
            throw new ModificationException("cannot connect nodes across circuits");
        }

        node.net = net;
        net.nodes.add( node );

    }

    /**
     * Combines two nets.
     * @param a The net to merge into
     * @param b The net to merge away
     * @throws ModificationException Tree validity broken
     */
    public void joinNets( Net a, Net b ) throws ModificationException {
        if (a.circuit != this || b.circuit != this)
            throw new ModificationException("cannot join nets across circuits");
        if (a == b)
            return;
        for (Node node : b.nodes) {
            node.net = a;
            a.nodes.add( node );
        }
        nets.remove( b );
    }

}
