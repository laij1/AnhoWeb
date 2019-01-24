/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */

function handleSearchResult(resultDataString) {
	jQuery("#tablediv").show();
    jQuery("#receiptdiv").show();
	$("#tbodyid").empty();
	$("#search_error_message").text("");
    resultDataJson = JSON.parse(resultDataString);
//    for (var i = 0; i < resultDataJson.length; i++) {
//        var counter = resultDataJson[i];
//    }
//    jQuery("#patient_ic").val(resultDataJson[0]["ic"]);
    if(resultDataJson.length == 0) {
        $("#search_error_message").text("沒有月結紀錄");	
    }else if(resultDataJson.length > 0) {
    	buildHtmlTable(resultDataJson);
    	invoiceOne();
    	invoiceTwo();
    }
}

    function buildHtmlTable(myList) {
  	  var columns = addAllColumnHeaders(myList);

  	  for (var i = 1; i < myList.length; i++) {
  	    var row$ = $('<tr/>');
  	    for (var colIndex = 0; colIndex < columns.length; colIndex++) {
  	      var cellValue = myList[i][columns[colIndex]];
  	      console.log(cellValue);
  	      if (cellValue == null) {
  	        cellValue = "";	        
  	      }

  	      row$.append($('<td/>').html(cellValue)).attr('id', i);

  	    }

  	    $("#excelDataTable").append(row$);
  	  }

  	}

  	// Adds a header row to the table and returns the set of columns.

  	function addAllColumnHeaders(myList) {
  	  var columnSet = [];
  	  var headerTr$ = $('<tr/>');
  	  var header ="";
  	  for (var i = 1; i < myList.length; i++) {
  	    var rowHash = myList[i];
  	    for (var key in rowHash) {
  	      if ($.inArray(key, columnSet) == -1) {
  	    	   if (key == "patientName") {
  	    		  header = "病患姓名";
  	    	  }

  	        columnSet.push(key);
  	        headerTr$.append($('<th/>').html(header));
  	        
  	        //check();
  	      }
  	      // check();
  	    }
  	    //check();
  	  }
//     	//add 明細一二
//      headerTr$.append($('<th/>').html("明細一"));
//      columnSet.push("明細一");
//      headerTr$.append($('<th/>').html("明細二"));
//      columnSet.push("明細二");
  	  $("#excelDataTable").append(headerTr$);



  	  return columnSet;
  	  //check();

  	}

  	function invoiceOne() {
  	// foreach row in the table
  	// we create a new checkbox
  	// and wrap it with a td element
  	// then put that td at the beginning of the row

  	  var $rows = $('#excelDataTable tr');
  	  for (var i = 1; i < $rows.length; i++) {
//  	    var $checkbox = $('<input />', {
//  	      type: 'checkbox',
//  	      id: 'id' + i,
//  	    });
  		    
  		    var $form = $('<form />', { class:'ui form', action:'/receipt/api/invoiceOne',target:'_blank', method:'POST' }),
  		        frmName = $('<input />', { id:'patientName', name: 'patientName', value: $('#' + i).text() , type:'hidden' }),
  		        frmDate = $('<input />', { id:'date', name: 'date', value:$('#monthdate').val() , type:'hidden' }),
  		        frmButton = $('<input />', { id:'invoice1', type:'submit', value:'明細一' });
  		    $form.append(frmName, frmDate, frmButton);
  		    $form.wrap('<td></td>').parent().appendTo($rows[i]);
  	  }
  	  
  	  
//  	  // First checkbox changes all the others
//  	  $('#id0').change(function(){
//  	  var isChecked = $(this).is(':checked');
//  	  $('#excelDataTable input[type=checkbox]').prop('checked', isChecked);
//  	  })
   }
  	
  	function invoiceTwo() {
  	  	// foreach row in the table
  	  	// we create a new checkbox
  	  	// and wrap it with a td element
  	  	// then put that td at the beginning of the row

  	  	  var $rows = $('#excelDataTable tr');
  	  	  for (var i = 1; i < $rows.length; i++) {
//  	  	    var $checkbox = $('<input />', {
//  	  	      type: 'checkbox',
//  	  	      id: 'id' + i,
//  	  	    });
  	  		    
  	  		    var $form = $('<form />', { class:'ui form', action:'/receipt/api/invoiceTwo',target:'_blank', method:'POST' }),
  	  		        frmName = $('<input />', { id:'patientName', name: 'patientName', value: $('#' + i).text() , type:'hidden' }),
  	  		        frmDate = $('<input />', { id:'date', name: 'date', value:$('#monthdate').val() , type:'hidden' }),
  	  		        frmButton = $('<input />', { id:'invoice1', type:'submit', value:'明細二' });
  	  		    $form.append(frmName, frmDate, frmButton);
  	  		    $form.wrap('<td></td>').parent().appendTo($rows[i]);
  	  	  }
  	  	  
  	  	  
//  	  	  // First checkbox changes all the others
//  	  	  $('#id0').change(function(){
//  	  	  var isChecked = $(this).is(':checked');
//  	  	  $('#excelDataTable input[type=checkbox]').prop('checked', isChecked);
//  	  	  })
  	   }
    
    // If login success, redirect to index.html page
//    if (resultDataJson["status"] === "success") {
//    	jQuery("#search_error_message").text(resultDataJson["result"]);
//    	
//    }
//    // If login fail, display error message on <div> with id "login_error_message"
//    else {
//
//        console.log("show error message");
//        console.log(resultDataJson["message"]);
//        jQuery("#search_error_message").text(resultDataJson["message"]);
//    }



/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitSearchForm(formSubmitEvent) {
    console.log("submit monthly invoice form");

    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();

    jQuery.post(
        "api/invoice",
        // Serialize the login form to the data sent by POST request
        jQuery("#invoice").serialize(),
        (resultDataString) => handleSearchResult(resultDataString));
   

}

// Bind the submit action of the form to a handler function
jQuery("#invoice").submit((event) => submitSearchForm(event));
