<?php
/*
 * Created on May 25, 2009
 */
 
 // ERRORS
 define("SUCCESS","SUCCESS");
 define("LOGIN_ERROR","LOGIN_ERROR");
 define("INVALID_COORD_ERROR","INVALID_COORD");
 define("INVALID_CHANGE_REQUEST_ERROR","INVALID_CHANGE_REQUEST_ERROR");
 define("POST_MESSAGE_LENGTH_ERROR","POST_MESSAGE_LENGTH_ERROR");
 define("SERVER_ERROR","SERVER_ERROR");
 define("ALREADY_VOTED_ERROR","ALREADY_VOTED_ERROR");
 define("DUPLICATE_ERROR","DUPLICATE_ERROR");
 define("EMPTY_RESULT","EMPTY_RESULT");
 define("PARAMETER_ERROR","PARAMETER_ERROR");
 define("INVALID_PHONE_ERROR","INVALID_PHONE_ERROR");
 define("MAX_NUDGES_REACHED","MAX_NUDGES_REACHED");
 define("UNKNOWN_PHONE","UNKNOWN_PHONE");
 // VALUES
 define("NUDGE_EXPIRATION",60*60*24); // in minutes
 define("MAX_ACTIVE_NUDGES",5); // in minutes
 
 function phonerize($phonenumber)
{
	if (strlen("".$phonenumber) == 11) 
	{
		if ($phonenumber[0] == "1")
			$phonenumber = substr($phonenumber,1);
		else
			return false;
	}
	
	if(ereg('^[2-9]{1}[0-9]{2}[0-9]{3}[0-9]{4}$', $phonenumber))
     return $phonenumber;
  	else
     return false;
}
?>
