<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container-fluid" id="results-dashboard">
    <div class="row justify-content-center bg-light">
        <div class="col-10 d-flex align-items-stretch" id="g2n">
            <div class="card w-100">
                <div class="card-header">
                    <span>Download results as:</span>
                    <a id="csv-anchor">
                        <button type="button" class="btn btn-sm btn-outline-secondary csv-button">CSV</button>
                    </a>
                    <a id="exportData">
                        <button type="button" class="btn btn-sm btn-outline-secondary svg-button">SVG</button>
                    </a>
                    <a id="png-anchor">
                        <button type="button" class="btn btn-sm btn-outline-secondary png-button">PNG</button>
                    </a>
                    <a id="cytoscape-anchor">
                        <button type="button" class="btn btn-sm btn-outline-secondary cytoscape-button">Cytoscape JSON
                        </button>
                    </a>
                </div>

                <div id="network-g2n" class="card-body h-100">
                    <svg xmlns="http://www.w3.org/2000/svg" version="1.1" class="g2n-svg h-100 w-100"
                         preserveAspectRatio="xMinYMin">
                    </svg>
                </div>

                <div class="card-footer">
                    <svg id="g2n_legend" xmlns="http://www.w3.org/2000/svg" version="1.1" class="w-100">
                        <g class="legend">
                            <g class="legend-item" transform="translate(7,3.5)">
                                <circle cx="3.5" cy="7" r="7" fill="#FF546D"></circle>
                                <text x="18" y="12.6" font-family="sans-serif" font-size="1rem">Transcription factor
                                </text>
                            </g>
                            <g class="legend-item" transform="translate(175,3.5)">
                                <circle cx="3.5" cy="7" r="7" fill="lightgrey"></circle>
                                <text x="18" y="12.6" font-family="sans-serif" font-size="1rem">Intermediate protein
                                </text>
                            </g>
                            <g class="legend-item" transform="translate(350,3.5)">
                                <line x1="0" y1="7" x2="14" y2="7" stroke="lightgray" stroke-width="2"></line>
                                <text x="20" y="12.6" font-family="sans-serif" font-size="1rem">PPI</text>
                            </g>
                        </g>
                    </svg>
                </div>
            </div>
        </div>
    </div>
</div>