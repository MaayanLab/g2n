package edu.mssm.pharm.maayanlab.X2K.web;

import edu.mssm.pharm.maayanlab.ChEA.ChEA;
import edu.mssm.pharm.maayanlab.Genes2Networks.Genes2Networks;
import edu.mssm.pharm.maayanlab.Genes2Networks.NetworkNode;
import edu.mssm.pharm.maayanlab.KEA.KEA;
import edu.mssm.pharm.maayanlab.X2K.enrichment.Network;
import edu.mssm.pharm.maayanlab.X2K.enrichment.X2K;
import edu.mssm.pharm.maayanlab.common.web.JSONify;
import edu.mssm.pharm.maayanlab.common.web.PartReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;


@WebServlet(urlPatterns = {"/results"})
@MultipartConfig
public class ResultsServlet extends HttpServlet {

    private static final long serialVersionUID = 6063942151226647232L;
    // X2K procedures
    private static final HashMap<String, String> defaultSettings = new HashMap<String, String>();

    static {
        defaultSettings.put(ChEA.SORT_BY, ChEA.PVALUE);
        defaultSettings.put(ChEA.INCLUDED_ORGANISMS, null);
        defaultSettings.put(ChEA.BACKGROUND_DATABASE, null);
        defaultSettings.put(Genes2Networks.PATH_LENGTH, null);
        defaultSettings.put(Genes2Networks.MAXIMUM_NUMBER_OF_EDGES, null);
        defaultSettings.put(Genes2Networks.MAXIMUM_NUMBER_OF_INTERACTIONS, null);
        defaultSettings.put(Genes2Networks.MINIMUM_NUMBER_OF_ARTICLES, null);
        defaultSettings.put(Genes2Networks.ENABLE_BIND, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_BIOCARTA, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_BIOGRID, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_BIOPLEX, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_DIP, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_FIGEYS, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_HPRD, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_HUMAP, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_IREF, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_INNATEDB, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_INTACT, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_KEA, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_KEGG, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_MINT, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_MIPS, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_MURPHY, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_PDZBASE, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_PPID, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_PREDICTEDPPI, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_SNAVI, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_STELZL, new String("false"));
        defaultSettings.put(Genes2Networks.ENABLE_VIDAL, new String("false"));
        defaultSettings.put(KEA.KINASE_INTERACTIONS, null);
        defaultSettings.put(KEA.SORT_BY, KEA.PVALUE);
        defaultSettings.put(X2K.MINIMUM_NETWORK_SIZE, null);
        // defaultSettings.put(X2K.NUMBER_OF_TOP_TFS, null);
        defaultSettings.put(X2K.NUMBER_OF_TOP_TFS, null);
        //defaultSettings.put(X2K.NUMBER_OF_TOP_KINASES, null);
    }

    private String g2n;
    private String x2k;

    private static void readAndSetSettings(HttpServletRequest req, X2K app) {
        Enumeration<String> reqKeys = req.getParameterNames();
        // Save all settings
        for (String setting : Collections.list(reqKeys)) {
            app.setSetting(setting, req.getParameter(setting));
        }
        // Set defaults if weren't specified
        for (String setting : defaultSettings.keySet()) {
            if (req.getParameter(setting) == null && defaultSettings.get(setting) != null) {
                app.setSetting(setting, defaultSettings.get(setting));
            }
        }
    }

    protected void forwardRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/templates/results.jsp").forward(req, resp);
    }

    protected void forwardRequest(JSONify json, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("GET request - Results");

        // X2K
        JSONify json = Context.getJSONConverter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        json.add("availableSettings", defaultSettings);
        forwardRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part geneChunk = req.getPart("text-genes");
        ArrayList<String> textGenes = PartReader.readTokens(geneChunk);

        if (textGenes.size() <= 0)
            System.out.println("no lists received - error");

        X2K app = new X2K();

        readAndSetSettings(req, app);
        app.run(textGenes);

        // Write output
        JSONify json = Context.getJSONConverter();

        JSONify ChEA_json = Context.getJSONConverter();
        ChEA_json.add("type", "ChEA");
        ChEA_json.add("tfs", app.getRankedTFs());
        json.add("ChEA", ChEA_json.toString());

        JSONify G2N_json = Context.getJSONConverter();
        G2N_json.add("type", "G2N");
        G2N_json.add("network", app.webNetworkFiltered());
        G2N_json.add("input_list", app.getTopRankedTFs());
        json.add("G2N", G2N_json.toString());

        json.add("input", textGenes);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        req.setAttribute("json", json);
        forwardRequest(json, req, resp);
    }

    // Fields getters and setters

    public String getG2n() {
        return g2n;
    }

    public void setG2n(String g2n) {
        this.g2n = g2n;
    }

    public String getX2k() {
        return x2k;
    }

    public void setX2k(String x2k) {
        this.x2k = x2k;
    }
}

