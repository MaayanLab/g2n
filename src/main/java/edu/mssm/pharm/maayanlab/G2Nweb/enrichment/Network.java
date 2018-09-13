package edu.mssm.pharm.maayanlab.G2Nweb.enrichment;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;
import edu.mssm.pharm.maayanlab.Genes2Networks.NetworkNode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Network {
    // TODO: Write annotations so json is correct
    @Expose
    public ArrayList<Node> nodes;
    @Expose
    public ArrayList<Interaction> interactions;

    private HashMap<String, Integer> nodeLocation;

    public Network() {
        nodes = new ArrayList<>();
        interactions = new ArrayList<>();
        nodeLocation = new HashMap<>();
    }

    public void addNode(nodeTypes type, Object o, String name) {
        nodeLocation.put(name, nodes.size());
        nodes.add(new Node(type, o, name));
    }

    public void addInteraction(String node1, String node2) {
        int loc1 = nodeLocation.get(node1);
        int loc2 = nodeLocation.get(node2);
        interactions.add(new Interaction(loc1, loc2));
    }

    public boolean contains(String s) {
        return nodeLocation.containsKey(s);
    }

    public enum nodeTypes {
        inputNode,
        networkNode
    }

    public class NodeToJson implements JsonSerializer<Node> {
        @Override
        public JsonElement serialize(Node node, Type type, JsonSerializationContext jsc) {
            JsonObject jsonObject = new JsonObject();
            if (node.type == nodeTypes.inputNode) {
                NetworkNode in = (NetworkNode) node.object;
                jsonObject.addProperty("name", in.getName());
                jsonObject.addProperty("type", "tf");
                jsonObject.addProperty("pvalue", -1);
            } else {
                NetworkNode nn = (NetworkNode) node.object;
                jsonObject.addProperty("name", nn.getName());
                jsonObject.addProperty("type", "other");
                jsonObject.addProperty("pvalue", -1);
            }
            return jsonObject;
        }
    }

    public class Node {
        nodeTypes type;
        Object object;
        String name;

        public Node(nodeTypes type, Object object, String name) {
            this.type = type;
            this.object = object;
            this.name = name;
        }
    }

    public class Interaction {
        @Expose
        int source;
        @Expose
        int target;

        public Interaction(int source, int target) {
            this.source = source;
            this.target = target;
        }
    }
}
