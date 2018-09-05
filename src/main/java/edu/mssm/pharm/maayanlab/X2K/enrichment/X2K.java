package edu.mssm.pharm.maayanlab.X2K.enrichment;

import edu.mssm.pharm.maayanlab.ChEA.ChEA;
import edu.mssm.pharm.maayanlab.ChEA.TranscriptionFactor;
import edu.mssm.pharm.maayanlab.Genes2Networks.Genes2Networks;
import edu.mssm.pharm.maayanlab.Genes2Networks.NetworkNode;
import edu.mssm.pharm.maayanlab.KEA.KEA;
import edu.mssm.pharm.maayanlab.KEA.Kinase;
import edu.mssm.pharm.maayanlab.common.core.FileUtils;
import edu.mssm.pharm.maayanlab.common.core.Settings;
import edu.mssm.pharm.maayanlab.common.core.SettingsChanger;
import edu.mssm.pharm.maayanlab.common.core.SimpleXMLWriter;
import edu.mssm.pharm.maayanlab.common.graph.NetworkModelWriter;
import edu.mssm.pharm.maayanlab.common.graph.PajekNETWriter;
import edu.mssm.pharm.maayanlab.common.graph.ShapeNode.Shape;
import edu.mssm.pharm.maayanlab.common.graph.XGMMLWriter;
import edu.mssm.pharm.maayanlab.common.graph.yEdGraphMLWriter;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Logger;

public class X2K implements SettingsChanger {

    public final static String MINIMUM_NETWORK_SIZE = "min_network_size";
    public final static String MAXIMUM_PATH_LENGTH = "max_path_length";
    public final static String NUMBER_OF_TOP_TFS = "number of top TFs";
    public final static String NUMBER_OF_TOP_KINASES = "number of top kinases";
    public final static String ENABLE_YED_OUTPUT = "output results in yEd";
    public final static String ENABLE_CYTOSCAPE_OUTPUT = "output results in Cytoscape";
    public final static String ENABLE_PAJEK_OUTPUT = "output results in Pajek";
    public final static String TF_NODE_COLOR = "color of TF nodes";
    public final static String KINASE_NODE_COLOR = "color of kinase nodes";
    public final static String SUBSTRATE_NODE_COLOR = "color of substrate nodes";
    public final static String PATH_LENGTH = "actual_path_length";
    static Logger log = Logger.getLogger(X2K.class.getSimpleName());
    private final Settings settings = new Settings() {
        {
            // Integer: minimum network size; otherwise, the path length is increased until the minimum met. [>0]
            set(X2K.MINIMUM_NETWORK_SIZE, 50);
            // Integer: minimum path length [>0]
            set(X2K.MAXIMUM_PATH_LENGTH, 4);
            // Integer: number of transcription factors used in network expansion and drug discovery. [>0]
            set(X2K.NUMBER_OF_TOP_TFS, 10);
            // Integer: number of kinases used in drug discovery. [>0]
            set(X2K.NUMBER_OF_TOP_KINASES, 10);
            // Boolean: output a yEd graphml file for network visualization. [true/false]
            set(X2K.ENABLE_YED_OUTPUT, true);
            // Boolean: output a Cytoscape XGMML file for network visualization. [true/false]
            set(X2K.ENABLE_CYTOSCAPE_OUTPUT, false);
            // Boolean: output a Pajek NET file for network visualization. [true/false]
            set(X2K.ENABLE_PAJEK_OUTPUT, false);
            // String: web color of the transcription factors in the Cytoscape and yEd network outputs. [#000000 - #FFFFFF]
            set(X2K.TF_NODE_COLOR, "#FF0000");
            // String: web color of the kinases in the Cytoscape and yEd network outputs. [#000000 - #FFFFFF]
            set(X2K.KINASE_NODE_COLOR, "#00FF00");
            // String: web color of the substrates in the Cytoscape and yEd network outputs. [#000000 - #FFFFFF]
            set(X2K.SUBSTRATE_NODE_COLOR, "#FFFF00");
        }
    };
    protected ChEA chea;
    protected Genes2Networks g2n;
    protected KEA kea;
    int progress = 0;
    String note = "";
    private Collection<String> topRankedTFs;
    private HashSet<String> network;
    // progress tracking
    private SwingWorker<Void, Void> task = null;
    private boolean isCancelled = false;
    private String cheaOutput;
    private String g2nOutput;
    private String keaOutput;

    public X2K() {
        settings.loadSettings();
    }

    public X2K(Settings settings) {
        settings.loadSettings(settings);
    }

    // Task methods
    public void setTask(SwingWorker<Void, Void> task) {
        this.task = task;
    }

    private void setProgress(int progress, String note) throws InterruptedException {
        // System.out.println(note);
        if (task != null) {
            if (isCancelled)
                throw new InterruptedException("Task cancelled at " + progress + "%!");
            task.firePropertyChange("progress", this.progress, progress);
            task.firePropertyChange("note", this.note, note);
            this.progress = progress;
            this.note = note;
        }
    }

    public void cancel() {
        isCancelled = true;
    }

    @Override
    public void setSetting(String key, String value) {
        settings.set(key, value);
    }

    public String getSetting(String key) {
        return settings.get(key);
    }

    public void run(ArrayList<String> inputList) {
        g2n = new Genes2Networks(settings);
        Integer path_length = Math.min(
                settings.getInt(Genes2Networks.PATH_LENGTH),
                settings.getInt(MAXIMUM_PATH_LENGTH)
        );

        // System.out.println(settings.getInt(MINIMUM_NETWORK_SIZE));
        do {
            g2n.setSetting(Genes2Networks.PATH_LENGTH, Integer.toString(path_length));
            g2n.run(inputList);
            network = g2n.getNetwork();
            path_length++;
            // System.out.println(network.size());
        } while (
                network.size() < settings.getInt(MINIMUM_NETWORK_SIZE)
                        && path_length < settings.getInt(MAXIMUM_PATH_LENGTH)
        );

        setSetting(X2K.PATH_LENGTH, Integer.toString(path_length - 1));
    }

    public Network webNetworkFiltered() {
        Network network = new Network();
        ArrayList<String> tfSimpleNames = new ArrayList<String>();

        for (TranscriptionFactor tf : chea.getTopRanked(settings.getInt(NUMBER_OF_TOP_TFS))) {
            network.addNode(Network.nodeTypes.transcriptionFactor, tf, tf.getSimpleName());
            tfSimpleNames.add(tf.getSimpleName());
        }
        HashSet<NetworkNode> networkSet = g2n.getNetworkSet();
        for (NetworkNode node : networkSet) {
            if (node.getName() != null) {
                if (!tfSimpleNames.contains(node.getName())) {
                    network.addNode(Network.nodeTypes.networkNode, node, node.getName().split("-")[0]);
                }
            }
        }
        for (NetworkNode node : networkSet) {
            HashSet<NetworkNode> neighbors = node.getNeighbors();
            for (NetworkNode neighbor : neighbors) {
                if ((neighbor.getName() != null) && (node.getName() != null)) {
                    if (network.contains(neighbor.getName())) {
                        network.addInteraction(node.getName().split("-")[0], neighbor.getName().split("-")[0]);
                    }
                }
            }
        }
        return network;
    }

    public Collection<String> getTopRankedTFs() {
        return topRankedTFs;
    }

}
