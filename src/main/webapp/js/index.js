function insertExample() {
    $.get('static/example_list.txt', function (data) {
        $('textarea#genelist').val(data);
    });
    $('#gene-count').text("375 genes");
    $('#results_submit').prop("disabled", false);
    return false;
}

function submitButtonListener(button, settings_form) {
    $('#' + button).click(function (evt) {
        $("#blocker").show();
        $("#loader").show().css({position: 'absolute', top: $(window).scrollTop() + $(window).height() / 2});
        // evt.preventDefault();
        var $form = $(settings_form),
            text_input = $("#genelist").val();


        // Hack that works like a charm
        var input = $("<input>")
            .attr("type", "hidden")
            .attr("name", "text-genes").val(text_input);
        $form.append($(input));

        if (text_input.length > 0) {
            $form.submit();
        }
    });
}

function cleanArray(actual) {
	  var newArray = [];
	  for (var i = 0; i < actual.length; i++) {
	    if (actual[i]) {
	      newArray.push(actual[i]);
	    }
	  }
	  return newArray;
	}

$(document).ready(function () {
    submitButtonListener("results_submit", "#x2k-form");
    // Check for Internet Explorer
    $(document).ready(function() {
        if ((!!window.MSInputMethodContext && !!document.documentMode) || navigator.userAgent.indexOf("MSIE")!=-1) {
            $('.show-on-ie').show();
        }
    });

    // text area listener
    $("#genelist").on("change keyup paste", function () {
        $('#genelist').val($('#genelist').val().trim().split(/[\s\n,]/).join('\n'));
        var len = cleanArray($('#genelist').val().trim().split('\n')).length;
        
        if (len === 0) {
        	$('#warning').text('');
        	$('#gene-count').text('');
        	$('#results_submit').prop("disabled", true); 
        } 
        else if (len > 0) {
        	$('#results_submit').prop("disabled", false);
	        if (len < 20) {
	            $('#warning').text('Warning! Inputting gene lists containing less than 20 genes may produce inaccurate results.');
	        }
	        else if (len > 3000) {
	            $('#warning').text('Warning! Inputting gene lists containing more than 3000 genes may produce inaccurate results.');
	        }
	        else {
	        	$('#warning').text('');
	        }
	
	        var genes = " genes";
	        if (len.toString()[len.toString().length - 1] === "1") {
	            genes = " gene";
	        }
	        
	        $('#gene-count').text(len + genes);
        }
    });

    $("body").tooltip({ selector: '[data-toggle=tooltip]' });
});