package search.datastructures;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;

/**
 * @author nekosaur
 */
public class GraphWindow extends JFrame {

    public GraphWindow(mxGraph graph)
    {
        super("Hello, World!");

        Object parent = graph.getDefaultParent();

        mxGraphComponent graphComponent = new mxGraphComponent(graph);

        getContentPane().add(graphComponent);
    }

    public static void show(mxGraph g)
    {
        GraphWindow frame = new GraphWindow(g);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);
    }
}
