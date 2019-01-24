/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */

function handleSearchResult(resultDataString) {
	jQuery("#tablediv").show();
    jQuery("#receiptdiv").show();
	$("#tbodyid").empty();
	$("#search_error_message").text("");
	$("#patientIc").text("");
    resultDataJson = JSON.parse(resultDataString);
//    for (var i = 0; i < resultDataJson.length; i++) {
//        var counter = resultDataJson[i];
//    }
    jQuery("#patient_ic").val(resultDataJson[0]["ic"]);
    if(resultDataJson.length == 1) {
        $("#search_error_message").text("沒有購買紀錄");	
    }else if(resultDataJson.length > 1) {
    	buildHtmlTable(resultDataJson);
    	check();
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

  	      row$.append($('<td/>').html(cellValue));

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
  	    	   if (key == "medicineName") {
  	    		  header = "藥名";
  	    	  } else if (key == "createOn") {
  	    		  header = "看診日期";
  	    	  } else if (key == "chargeOn") {
  	    		  header = "結帳日期";
  	    	  } else if (key == "quantity") {
  	    		  header = "數量";
  	    	  } else if (key == "subtotal") {
  	    		  header = "金額";
  	    	  } else if (key == "category") {
  	    		  header = "分類";
  	    	  }
  	    	console.log(header);
  	        columnSet.push(key);
  	        headerTr$.append($('<th/>').html(header));
  	        //check();
  	      }
  	      // check();
  	    }
  	    //check();
  	  }
  	  $("#excelDataTable").append(headerTr$);

  	  return columnSet;
  	  //check();

  	}

  	function check() {
  	// foreach row in the table
  	// we create a new checkbox
  	// and wrap it with a td element
  	// then put that td at the beginning of the row

  	  var $rows = $('#excelDataTable tr');
  	  for (var i = 0; i < $rows.length; i++) {
  	    var $checkbox = $('<input />', {
  	      type: 'checkbox',
  	      id: 'id' + i,
  	    });

  	    $checkbox.wrap('<td></td>').parent().prependTo($rows[i]);
  	  }
  	  
  	  
  	  // First checkbox changes all the others
  	  $('#id0').change(function(){
  	  var isChecked = $(this).is(':checked');
  	  $('#excelDataTable input[type=checkbox]').prop('checked', isChecked);
  	  })
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
    console.log("submit search form");

    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();

    jQuery.post(
        "api/search",
        // Serialize the login form to the data sent by POST request
        jQuery("#search_patient").serialize(),
        (resultDataString) => handleSearchResult(resultDataString));
   

}

// Bind the submit action of the form to a handler function
jQuery("#search_patient").submit((event) => submitSearchForm(event));