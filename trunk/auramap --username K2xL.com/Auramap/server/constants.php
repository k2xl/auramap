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
 define("ALREADY_NUDGED","ALREADY_NUDGED");
 define("DUPLICATE_ERROR","DUPLICATE_ERROR");
 define("EMPTY_RESULT","EMPTY_RESULT");
 define("PARAMETER_ERROR","PARAMETER_ERROR");
 define("INVALID_PHONE_ERROR","INVALID_PHONE_ERROR");
 define("MAX_NUDGES_REACHED","MAX_NUDGES_REACHED");
 define("UNKNOWN_PHONE","UNKNOWN_PHONE");
 define("NO_NUDGES","0");
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


function distanceOfTimeInWords($distanceInSeconds, $showLessThanAMinute = false) 
{
	return $distanceInSeconds; /*
	
    $distanceInMinutes = round($distanceInSeconds / 60);
        
        if ( $distanceInMinutes <= 1 ) {
            if ( !$showLessThanAMinute ) {
                return ($distanceInMinutes == 0) ? 'less than a minute ago' : '1 minute ago';
            } else {
                if ( $distanceInSeconds < 5 ) {
                    return 'less than 5 seconds ago';
                }
                if ( $distanceInSeconds < 10 ) {
                    return 'less than 10 seconds ago';
                }
                if ( $distanceInSeconds < 20 ) {
                    return 'less than 20 seconds ago';
                }
                if ( $distanceInSeconds < 40 ) {
                    return 'about half a minute ago';
                }
                if ( $distanceInSeconds < 60 ) {
                    return 'less than a minute ago';
                }
                
                return '1 minute ago';
            }
        }
        if ( $distanceInMinutes < 45 ) {
            return $distanceInMinutes . ' minutes ago';
        }
        if ( $distanceInMinutes < 90 ) {
            return 'about 1 hour ago';
        }
        if ( $distanceInMinutes < 1440 ) {
            return 'about ' . round(floatval($distanceInMinutes) / 60.0) . ' hours ago';
        }
        if ( $distanceInMinutes < 2880 ) {
            return '1 day';
        }
        if ( $distanceInMinutes < 43200 ) {
            return 'about ' . round(floatval($distanceInMinutes) / 1440) . ' days ago';
        }
        if ( $distanceInMinutes < 86400 ) {
            return 'about 1 month';
        }
        if ( $distanceInMinutes < 525600 ) {
            return round(floatval($distanceInMinutes) / 43200) . ' months ago';
        }
        if ( $distanceInMinutes < 1051199 ) {
            return 'about 1 year ago';
        }
        
        return 'over ' . round(floatval($distanceInMinutes) / 525600) . ' years ago';
        */
}
 
?>
