<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>G2N Web</title>

    <%@ include file="/templates/head.jsp" %>

    <link rel="stylesheet" href="dist/css/atooltip.css">
    <link rel="stylesheet" href="dist/css/results.css">
    <link rel="stylesheet" href="dist/css/index.css">

    <script src="dist/js/bargraph.js"></script>
    <script src="dist/js/index.js"></script>
    <script src="dist/js/ljp.js"></script>
    <script src="dist/js/network.js"></script>
    <script src="dist/js/results.js"></script>
</head>

<body data-spy="scroll" data-target="#x2k-navbar" data-offset="150">
<!-- Anchor for scrollspy -->
<div id="x2k-scroll"></div>

<div class="container-fluid">
    <div class="row justify-content-center bg-light">
        <div class="col-sm-10 show-on-ie" style="display: none; color: red; font-weight: bold; text-align:center;">
            For best app performance, please use a browser other than Internet Explorer.
        </div>
    </div>
    <nav class="navbar navbar-light sticky-top bg-light justify-content-center navbar-expand-sm"
         id="g2n-navbar">
        <a class="navbar-brand" href="/G2N">
            <img id="logo" src="static/logo.png" height="60px" class="d-inline-block full-logo">
        </a>
        <div id="scrollspy-nav" class="collapse navbar-collapse">
            <ul class="nav nav-pills">
                <li class="nav-item">
                    <a class="nav-link active" href="#x2k-scroll">Submit</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#command-line">Command Line</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#datasets">Datasets</a>
                </li>
            </ul>
        </div>
    </nav>
    <div class="row justify-content-center bg-light">
        <div class="col-sm-10">
        </div>
        <div class="col-sm-10 bg-white">
            <div id="submit-form" class="my-3 mx-2">
                <h4>Submit</h4>
                <div class="my-3">
                    <p id="x2k-desc" class="desc">
                        Enter a list of mammalian genes or proteins in Entrez gene symbol format to receive results of a
                        protein-protein interactions (PPI) subnetwork that connects the enriched genes/proteins with
                        known protein-protein interactions using Genes2Networks (G2N). G2N connects input list of
                        genes/proteins using the shortest path algorithms using known PPI from the selected databases.
                    </p>
                </div>
                <form id="x2k-form" enctype="multipart/form-data" action="/G2N/results" method="POST">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-sm-6 my-2">
                                <label for="genelist">Gene list (<a href="javascript:void(0)"
                                                                    onclick="insertExample();"
                                                                    id="example-link">try an example</a>)</label>
                                <textarea class="form-control form-control-sm" id="genelist" rows="12"
                                          name="text-genes"></textarea>
                                <span id="gene-count" style="color: darkgrey; font-size: 0.9rem;"></span>
                                <br/>
                                <span id="warning" style="color: coral; font-size: 0.8rem;"></span>
                                <!--Buttons-->
                                <div class="mb-4 mt-2">
                                    <button type="submit" class="btn btn-sm btn-outline-primary" id="results_submit"
                                            disabled>
                                        Submit
                                    </button>
                                </div>
                            </div>
                            <div class="col-sm-6 my-2">
                                <!--Settings-->
                                <label>Settings</label>
                                <div class="card my-1">

                                    <div class="card-body">
                                        <div class="form-group row align-items-center">
                                            <label for="min_network_size"
                                                   class="col-form-label col-sm-9">Minimum
                                                number of proteins in subnetwork
                                                <sup data-toggle="tooltip" data-placement="top"
                                                     container="body"
                                                     title="The minimum size of the expanded Protein-Protein interaction subnetwork generated using Genes2Networks.">
                                                    <i class="fa fa-question-circle"></i>
                                                </sup>
                                            </label>
                                            <div class="col-sm-3">
                                                <input class="form-control form-control-sm"
                                                       type="text" value="10"
                                                       id="min_network_size"
                                                       name="min_network_size">
                                            </div>
                                        </div>
                                        <div class="form-group row align-items-center">
                                            <label for="number of top TFs"
                                                   class="col-form-label col-sm-9">Minimum
                                                number of transcription factors in subnetwork
                                                <sup data-toggle="tooltip" data-placement="top"
                                                     container="body"
                                                     title="The minimum size of the expanded transcription factor subnetwork generated using Genes2Networks.">
                                                    <i class="fa fa-question-circle"></i>
                                                </sup>
                                            </label>
                                            <div class="col-sm-3">
                                                <input class="form-control form-control-sm"
                                                       type="text" value="10"
                                                       id="number of top TFs"
                                                       name="number of top TFs">
                                            </div>
                                        </div>
                                        <div class="form-group row align-items-center">
                                            <label for="x2k_path_length"
                                                   class="col-form-label col-sm-9">
                                                Minimum path length
                                                <sup data-toggle="tooltip" data-placement="top"
                                                     container="body"
                                                     title="The minimum Protein-Protein Interaction path length for the subnetwork expansion step of Genes2Networks.">
                                                    <i class="fa fa-question-circle"></i>
                                                </sup>
                                            </label>
                                            <div class="col-sm-3">
                                                <input class="form-control form-control-sm"
                                                       type="text" value="2"
                                                       id="x2k_path_length" name="path_length">
                                            </div>
                                        </div>
                                        <div class="form-group row align-items-center">
                                            <label for="x2k_min_number_of_articles_supporting_interaction"
                                                   class="col-form-label col-sm-9">
                                                Minimum number of articles
                                                <sup data-toggle="tooltip" data-placement="top"
                                                     container="body"
                                                     title="The minimum number of published articles supporting a Protein-Protein Interaction for the expanded subnetwork.">
                                                    <i class="fa fa-question-circle"></i>
                                                </sup>
                                            </label>
                                            <div class="col-sm-3">
                                                <input class="form-control form-control-sm"
                                                       type="text" value="0"
                                                       id="x2k_min_number_of_articles_supporting_interaction"
                                                       name="min_number_of_articles_supporting_interaction">
                                            </div>
                                        </div>
                                        <div class="form-group row align-items-center">
                                            <label for="x2k_max_number_of_interactions_per_protein"
                                                   class="col-form-label col-sm-9">
                                                Maximum number of interactions per protein
                                                <sup data-toggle="tooltip" data-placement="top"
                                                     container="body"
                                                     title="The maximum number of physical interactions allowed for the proteins in the expanded subnetwork.">
                                                    <i class="fa fa-question-circle"></i>
                                                </sup>
                                            </label>
                                            <div class="col-sm-3">
                                                <input class="form-control form-control-sm"
                                                       type="text" value="200"
                                                       id="x2k_max_number_of_interactions_per_protein"
                                                       name="max_number_of_interactions_per_protein">
                                            </div>
                                        </div>
                                        <div class="form-group row align-items-center">
                                            <label for="x2k_max_number_of_interactions_per_article"
                                                   class="col-form-label col-sm-9">
                                                Maximum number of interactions per article
                                                <sup data-toggle="tooltip" data-placement="top"
                                                     container="body"
                                                     title="The maximum number of physical interactions reported in the publications used for the subnetwork expansion in Genes2Networks.">
                                                    <i class="fa fa-question-circle"></i>
                                                </sup>
                                            </label>
                                            <div class="col-sm-3">
                                                <input class="form-control form-control-sm"
                                                       type="text" value="100"
                                                       id="x2k_max_number_of_interactions_per_article"
                                                       name="max_number_of_interactions_per_article">
                                            </div>
                                        </div>
                                        <hr/>
                                        <div class="form-group" id="g2n-x2k-ppi">
                                            <label>
                                                PPI Networks
                                                <sup data-toggle="tooltip" data-placement="top"
                                                     container="body"
                                                     title="The Protein-Protein Interaction databases to integrate for generation of the expanded subnetwork.">
                                                    <i class="fa fa-question-circle"></i>
                                                </sup>
                                            </label>
                                            <div class="row">
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input class="form-check-input"
                                                               type="checkbox"
                                                               name="enable_Biocarta"
                                                               value=false>
                                                        Biocarta
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input class="form-check-input"
                                                               type="checkbox"
                                                               name="enable_BioGRID"
                                                               value=true checked>
                                                        BioGRID 2017
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input class="form-check-input"
                                                               type="checkbox"
                                                               name="enable_BioPlex"
                                                               value=false>
                                                        BioPlex
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input class="form-check-input"
                                                               type="checkbox"
                                                               name="enable_DIP"
                                                               value=false>
                                                        DIP 2017
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input class="form-check-input"
                                                               type="checkbox"
                                                               name="enable_huMAP"
                                                               value=false>
                                                        huMAP 2017
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input class="form-check-input"
                                                               type="checkbox"
                                                               name="enable_InnateDB"
                                                               value=false>
                                                        InnateDB 2017
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input class="form-check-input"
                                                               type="checkbox"
                                                               name="enable_IntAct"
                                                               value=true checked>
                                                        IntAct 2017
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input class="form-check-input"
                                                               type="checkbox"
                                                               name="enable_KEGG"
                                                               value=false>
                                                        KEGG
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input class="form-check-input"
                                                               type="checkbox"
                                                               name="enable_MINT"
                                                               value=true checked>
                                                        MINT 2017
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input class="form-check-input"
                                                               type="checkbox"
                                                               name="enable_ppid"
                                                               value=true checked>
                                                        ppid
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input class="form-check-input"
                                                               type="checkbox"
                                                               name="enable_SNAVI"
                                                               value=false>
                                                        SNAVI 2017
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input class="form-check-input"
                                                               type="checkbox"
                                                               name="enable_iREF"
                                                               value=false>
                                                        iREF 2017
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input
                                                                type="checkbox"
                                                                class="form-check-input"
                                                                name="enable_Stelzl"
                                                                value=true checked>
                                                        Stelzl
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input
                                                                type="checkbox"
                                                                class="form-check-input"
                                                                name="enable_vidal"
                                                                value=false>
                                                        Vidal
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input
                                                                type="checkbox"
                                                                class="form-check-input"
                                                                name="enable_BIND"
                                                                value=false>
                                                        BIND
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input
                                                                type="checkbox"
                                                                class="form-check-input"
                                                                name="enable_figeys"
                                                                value=false>
                                                        figeys
                                                    </label>
                                                </div>
                                                <div class="form-check col-sm-4">
                                                    <label class="form-check-label">
                                                        <input
                                                                type="checkbox"
                                                                class="form-check-input"
                                                                name="enable_HPRD"
                                                                value=false>
                                                        HPRD
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>

            <div id="api">
                <h4>API</h4>
                <div class="row">
                    <div class="col-sm-12">
                        <div style="overflow-y: auto; width: 100%;">
                            <iframe src="https://nbviewer.jupyter.org/github/MaayanLab/g2n/blob/master/src/main/webapp/notebooks/g2n_api_notebook.ipynb?flush_cache=true"
                                    style="border: 1px solid lightgrey; width: 100%; border-radius: 3px; margin-top: 5px; height: 500px;"></iframe>
                        </div>
                    </div>
                </div>
            </div>

            <div id="command-line">
                <h4>Command line version</h4>

                <p>From <a href=http://www.maayanlab.net/X2K/download/G2N-1.5-SNAPSHOT-jar-with-dependencies.jar>here</a> (JAR format, 3.6
                    MB) you can download a command line standalone version of G2N.
                    <br/>General format on how to run the G2N command line tool:
                    <br/><code>java -jar [G2N JAR file] [input gene list] [output SIG file] [backgroundSigFile1, backgroundSigFile2, ...]</code>
                </p>

                <p>Example of how to run the G2N command line tool:
                    <br/><code>java -jar G2N-1.5-SNAPSHOT-jar-with-dependencies.jar genelist.txt results.sig huMAP.sig Biocarta.sig</code>
                </p>
            </div>

            <div id="datasets">
                <h4>Download datasets</h4>
                <%@ include file="/templates/downloads.jsp" %>
            </div>
        </div>
    </div>
    <%@ include file="/templates/footer.jsp" %>
</div>
<!-- Loader -->
<div id="loader"></div>
<div id="blocker"></div>

</body>
</html>
