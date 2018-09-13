package edu.mssm.pharm.maayanlab.G2Nweb.web;

import edu.mssm.pharm.maayanlab.G2Nweb.enrichment.G2Nweb;
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

    private static void readAndSetSettings(HttpServletRequest req, G2Nweb app) {
        Enumeration<String> reqKeys = req.getParameterNames();
        for (String setting : Collections.list(reqKeys)) {
            app.setSetting(setting, req.getParameter(setting));
        }
    }

    protected void forwardRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/templates/results.jsp").forward(req, resp);
    }

    protected void forwardRequest(JSONify json, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part geneChunk = req.getPart("text-genes");
        ArrayList<String> textGenes = PartReader.readTokens(geneChunk);

        if (textGenes.size() <= 0)
            System.out.println("no lists received - error");

        G2Nweb app = new G2Nweb(req);

        app.run(textGenes);

        // Write output
        JSONify json = Context.getJSONConverter();

        JSONify G2N_json = Context.getJSONConverter();
        G2N_json.add("type", "G2N");
        G2N_json.add("network", app.webNetworkFiltered(textGenes));
        G2N_json.add("input_list", textGenes);
        json.add("G2N", G2N_json.toString());

        json.add("input", textGenes);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        req.setAttribute("json", json);
        forwardRequest(json, req, resp);
    }
}

