<?php
/*
 * Created on Jul 23, 2009
 */
require_once ("constants.php");
require_once ("utils/mysql_helper.class.php");
require_once ("utils/sp_session.class.php");
require_once ("utils/rss_php.class.php");
require_once("dbinfo.php");
// CLEAN UP POST AND GET... USE A PHP INPUT FILTER!

//
//
function connectDB() {
	global $DB;
	$DB = new mysql_helper("localhost", "mtserver2", "X:JyPz.p7TyNSSPD", "auramap");
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

//

if (isset($Headers['username'])== false)
{
	$Headers = $_GET;
}

// Step 1: Check to see if they sent a phone number and validate it
if (!isset ($Headers['username']) || !isset ($Headers['password'])) {
	echo PARAMETER_ERROR;
	exit();
}
$username = phonerize($Headers['username']);
if (!$username){
	echo PARAMETER_ERROR;
	exit();
}

// Step 2: Check to see if phone number is already in database
connectDB();

$phoneAlreadyExists = phoneExists($username);
if ($phoneAlreadyExists) {
	if (!isset($Headers['password'])) 
	{
		echo PARAMETER_ERROR;
		exit();
	}
	$password = $Headers['password'];

	// Step 2B: Check to see if phone number and password matches that in database
	
	$loggedIn = login($username,$password);
	if (!$loggedIn){ echo LOGIN_ERROR; exit(); }
	echo SUCCESS;
	exit();
}
// Step 3: If phone does not exist...
$phonepasskey = registerPhone($username);

echo $phonepasskey;
exit();

function phoneExists($phone)
{
	global $DB;
	return $DB->count("users","where username='$phone'")>0;
}
function registerPhone($phone)
{
	global $DB;
	$insert = array();
	if ($phone == false) { echo INVALID_PHONE_ERROR; exit(); }
	$insert['username'] = "'$phone'";
	$insert['phone'] =  "'$phone'";
	$key = md5(rand(0,999999999));
	$insert['password'] = "'$key'";
	$insert['unixtimestamp'] = "UNIX_TIMESTAMP()";
	$q = $DB->insert("users",$insert);
	if (!$q) { return SERVER_ERROR; }
	return $key;
}

?>
