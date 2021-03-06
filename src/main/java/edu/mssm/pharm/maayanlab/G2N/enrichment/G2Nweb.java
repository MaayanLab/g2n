package edu.mssm.pharm.maayanlab.G2N.enrichment;

import edu.mssm.pharm.maayanlab.Genes2Networks.Genes2Networks;
import edu.mssm.pharm.maayanlab.Genes2Networks.NetworkNode;
import edu.mssm.pharm.maayanlab.common.core.Settings;
import edu.mssm.pharm.maayanlab.common.core.SettingsChanger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;

public class G2Nweb implements SettingsChanger {

    private final static String MAXIMUM_PATH_LENGTH = "max_path_length";
    private final static String MINIMUM_NETWORK_SIZE = "min_network_size";
    private final static String ENABLE_YED_OUTPUT = "output results in yEd";
    private final static String ENABLE_CYTOSCAPE_OUTPUT = "output results in Cytoscape";
    private final static String ENABLE_PAJEK_OUTPUT = "output results in Pajek";
    private final static String TF_NODE_COLOR = "color of TF nodes";
    private final static String KINASE_NODE_COLOR = "color of kinase nodes";
    private final static String SUBSTRATE_NODE_COLOR = "color of substrate nodes";
    private final static String PATH_LENGTH = "actual_path_length";
    private final Settings settings = new Settings() {
        {
            // Integer: minimum network size; otherwise, the path length is increased until the minimum met. [>0]
            set(G2Nweb.MINIMUM_NETWORK_SIZE, 50);
            // Integer: minimum path length [>0]
            set(G2Nweb.MAXIMUM_PATH_LENGTH, 4);
            // Boolean: output a yEd graphml file for network visualization. [true/false]
            set(G2Nweb.ENABLE_YED_OUTPUT, true);
            // Boolean: output a Cytoscape XGMML file for network visualization. [true/false]
            set(G2Nweb.ENABLE_CYTOSCAPE_OUTPUT, false);
            // Boolean: output a Pajek NET file for network visualization. [true/false]
            set(G2Nweb.ENABLE_PAJEK_OUTPUT, false);
            // String: web color of the transcription factors in the Cytoscape and yEd network outputs. [#000000 - #FFFFFF]
            set(G2Nweb.TF_NODE_COLOR, "#FF0000");
            // String: web color of the kinases in the Cytoscape and yEd network outputs. [#000000 - #FFFFFF]
            set(G2Nweb.KINASE_NODE_COLOR, "#00FF00");
            // String: web color of the substrates in the Cytoscape and yEd network outputs. [#000000 - #FFFFFF]
            set(G2Nweb.SUBSTRATE_NODE_COLOR, "#FFFF00");
            set(Genes2Networks.ENABLE_BIND, "false");
            set(Genes2Networks.ENABLE_BIOCARTA, "false");
            set(Genes2Networks.ENABLE_BIOGRID, "false");
            set(Genes2Networks.ENABLE_BIOPLEX, "false");
            set(Genes2Networks.ENABLE_DIP, "false");
            set(Genes2Networks.ENABLE_FIGEYS, "false");
            set(Genes2Networks.ENABLE_HPRD, "false");
            set(Genes2Networks.ENABLE_HUMAP, "false");
            set(Genes2Networks.ENABLE_IREF, "false");
            set(Genes2Networks.ENABLE_INNATEDB, "false");
            set(Genes2Networks.ENABLE_INTACT, "false");
            set(Genes2Networks.ENABLE_KEA, "false");
            set(Genes2Networks.ENABLE_KEGG, "false");
            set(Genes2Networks.ENABLE_MINT, "false");
            set(Genes2Networks.ENABLE_MIPS, "false");
            set(Genes2Networks.ENABLE_MURPHY, "false");
            set(Genes2Networks.ENABLE_PDZBASE, "false");
            set(Genes2Networks.ENABLE_PPID, "false");
            set(Genes2Networks.ENABLE_PREDICTEDPPI, "false");
            set(Genes2Networks.ENABLE_SNAVI, "false");
            set(Genes2Networks.ENABLE_STELZL, "false");
            set(Genes2Networks.ENABLE_VIDAL, "false");
        }
    };
    protected Genes2Networks g2n;
    private HashSet<String> network;

    public G2Nweb(HttpServletRequest req) {
        Enumeration<String> reqKeys = req.getParameterNames();
        for (String setting : Collections.list(reqKeys)) {
            this.setSetting(setting, req.getParameter(setting));
        }
    }

    public G2Nweb(Settings settings) {
        settings.loadSettings(settings);
    }

    @Override
    public void setSetting(String key, String value) {
        settings.set(key, value);
    }


    public void run(ArrayList<String> inputList) {
        g2n = new Genes2Networks(settings);
        int path_length = Math.min(
                settings.getInt(Genes2Networks.PATH_LENGTH),
                settings.getInt(MAXIMUM_PATH_LENGTH)
        );

        do {
            g2n.setSetting(Genes2Networks.PATH_LENGTH, Integer.toString(path_length));
            g2n.run(inputList);
            network = g2n.getNetwork();
            path_length++;
        } while (
                network.size() < settings.getInt(MINIMUM_NETWORK_SIZE)
                        && path_length < settings.getInt(MAXIMUM_PATH_LENGTH)
        );

        setSetting(G2Nweb.PATH_LENGTH, Integer.toString(path_length - 1));
    }

    public Network webNetworkFiltered(ArrayList<String> textGenes) {
        Network network = new Network();
        ArrayList<String> SimpleNames = new ArrayList<>();

        ArrayList<NetworkNode> inputNodes = new ArrayList<>();
        for (String inputNode : textGenes) {
            inputNodes.add(new NetworkNode(inputNode, "NA", "NA", "inputNode", "NA"));
        }

        for (NetworkNode in : inputNodes) {
            network.addNode(Network.nodeTypes.inputNode, in, in.getName());
            SimpleNames.add(in.getName());
        }

        HashSet<NetworkNode> networkSet = g2n.getNetworkSet();
        for (NetworkNode node : networkSet) {
            if (node.getName() != null) {
                if (!SimpleNames.contains(node.getName())) {
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

}
