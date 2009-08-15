<?php
/*
 * Created on May 25, 2009
 */
 require_once("constants.php");
 require_once("utils/mysql_helper.class.php");
 require_once("utils/sp_session.class.php");
 require_once("utils/rss_php.class.php");
 require_once("dbinfo.php");
// CLEAN UP POST AND GET... USE A PHP INPUT FILTER!
//header("Content-type: text/xml");

//
function connectDB() {
	global $DB;
	$DB = new mysql_helper("localhost", "happymap", "YvN:fHGjHd8:EvwK", "auramap");
	$DB->setDebug(true);
}
function login($username, $password) {
	global $DB;
	$loggedIn = $DB->count("users", "where username='$username' and password='$password'");
	return $loggedIn;
}
$Headers = $_POST;
if (isset($Headers['username'])== false)
{
	$Headers = $_GET;
}
//


$username = $Headers['username'];
$password = ($Headers['password']);
connectDB();
 
$loggedIn = login($username,$password);//$DB->count("users","where username='$username' and password='$password'");

if ($loggedIn == 0)
{
	echo LOGIN_ERROR;
	exit();
}

$Me = $DB->query("select * from users where username='$username' and password='$password'");
$Me = mysql_fetch_assoc($Me);


function validCoordinates($lat,$long)
{
	if ( (is_numeric($lat) && is_numeric($long)) == false)
	{
		return INVALID_COORD_ERROR;
	}
	// Lat and long should be floats at this point of code. Else the previous if statement would return an error.
	/*if ($lat < 0 || $lat > 90 || $long < 0 || $long > 180)
	{
		return INVALID_COORD_ERROR;
	}*/
	return SUCCESS;
}

?>