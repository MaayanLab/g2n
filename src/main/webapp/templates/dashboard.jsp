<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container-fluid" id="results-dashboard">
    <div class="row justify-content-center bg-light">
        <div class="col-10 d-flex align-items-stretch" id="g2n">
            <div class="card w-100">
                <div class="card-header">
                    <span>Download results as:</span>
                    <a id="csv-anchor">
                        <button type="button" class="btn btn-outline-primary csv-button">CSV</button>
                    </a>
                    <a id="exportData">
                        <button type="button" class="btn btn-outline-primary svg-button">SVG</button>
                    </a>
                    <a id="png-anchor">
                        <button type="button" class="btn btn-outline-primary png-button">PNG</button>
                    </a>
                    <a id="cytoscape-anchor">
                        <button type="button" class="btn btn-outline-primary cytoscape-button">Cytoscape JSON</button>
                    </a>
                </div>

                <div id="network-g2n" class="card-body h-100">
                    <svg xmlns="http://www.w3.org/2000/svg" version="1.1" class="g2n-svg h-100 w-100"
                         preserveAspectRatio="xMinYMin">
                    </svg>
                </div>
            </div>
        </div>
    </div>
</div>