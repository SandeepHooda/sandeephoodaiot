
  function getUserDetails() {
	    var xmlhttp = new XMLHttpRequest();

	    xmlhttp.onreadystatechange = function() {
	        if (xmlhttp.readyState == XMLHttpRequest.DONE ) {
	           if (xmlhttp.status == 200) {
	        	  
	        	   if(xmlhttp.responseText){
	        		   var resp =  JSON.parse(xmlhttp.responseText);
		        	   if (resp.name){
		        		   userSignStatus(true);
		        		   document.getElementById("loginInfo").innerHTML =resp.name ;
		                   document.getElementById("InfoImg").src=resp.avatar_url;
		                   document.getElementById("switches").style.visibility = 'visible';
		                   
		        	   }
	        	   }
	        	   else {
	        		   userSignStatus(false);
	        	   }

	               
	           }
	           else if (xmlhttp.status == 400) {
	              alert('There was an error 400');
	           }
	           else {
	               alert('something else other than 200 was returned');
	           }
	        }
	    };
	    xmlhttp.open("GET", "/GitHub/GetUserDetails", true);
	 
	    xmlhttp.send(); 
	}

  function userSignStatus(signedIn){
		if(signedIn){
			document.getElementById("signin").style.display = "none";
	        document.getElementById("signout").style.display = "block";
	        document.getElementById('signout').disabled = false;
	        
		}else {
			  document.getElementById("loginInfo").innerHTML ="Guest";
		        document.getElementById("InfoImg").src="images/city.png";
		        document.getElementById('signout').disabled = true;
		        document.getElementById("signin").style.display = "block";
		        document.getElementById("signout").style.display = "none";
		        document.getElementById("loginInfo").innerHTML ="Guest";
		}
		
		document.getElementById("InfoImg").style.display = "block";
		document.getElementById("welcomeMsg").style.display = "inline-block";
		
	}
 
window.onload = function() {
	getUserDetails();
  }
  

  