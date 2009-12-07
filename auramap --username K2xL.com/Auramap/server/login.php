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

if (isset($Headers['username']) == false ||isset($Headers['password']) == false) { echo PARAMETER_ERROR; exit(); }
$username = phonerize($Headers['username']);
if (!$username) { echo PARAMETER_ERROR; exit(); }
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

function getUserWithPhone($num)
{
	global $DB;
	// First get user from users table from the phone number
	$user = $DB->query("select * from users where username=$num");
	if (!$user) { echo SERVER_ERROR; exit(); }
	// If the phone hasn't been registered OR this user has no permissions to see...
	if (mysql_num_rows($user) == 0){ return false; };
	
	
	return mysql_fetch_array($user);
}

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
function getAurapointsFromRect($ilat,$ilon,$flat, $flon, $numresults)
{
	global $DB;
	$sql = "SELECT * FROM aurapoints where lat<$ilat AND lat>$flat AND lon>$ilon AND lon<$flon ";
	$query = $DB->query($sql);

	return $query;
}
function countAurapointsFromRect($ilat,$ilon,$flat, $flon)
{
	global $DB;
	$num = $DB->count("aurapoints"," where lat<$ilat AND lat>$flat AND lon>$ilon AND lon<$flon");

	return $num;
}
function getAurapointsFromCircle($lat,$lon,$radius, $numresults)
{
	global $DB;
	$sql = "SELECT *,((ACOS(SIN($lat* PI() / 180) * SIN(lat * PI() / 180) + COS($lat* PI() / 180) * COS(lat * PI() / 180) * COS(($lon- lon) * PI() / 180)) * 180 / PI()) * 60 * 1.1515) AS distance FROM aurapoints having distance<=$radius ORDER BY distance ASC ";
	$query = $DB->query($sql);
	return $query;
}
function getTopTagsFromAurapoints($aurapts)
{
	global $DB;
	// Step 3: Create tag lists
	$numpts = mysql_num_rows($aurapts);
	if ($numpts == 0) { return EMPTY_RESULT; }
	$count = 0;
	$sql = "Select * from tags where ";
	while ($rs = mysql_fetch_assoc($aurapts))
	{
		$count++;
		$curID = $rs['id'];
		$sql .= "aurapoint_id = $curID";
		if ($count < $numpts)
		{
			$sql .= " OR ";
		}
	}
	$query = $DB->query($sql);
	if (!$query){return SERVER_ERROR; }
	$numpts = mysql_num_rows($query);
	if ($numpts == 0) { return EMPTY_RESULT;  }
	//echo $sql;
	// now place all tags in an array
	$tagArray = array();
	while ($rs = mysql_fetch_assoc($query))
	{
		
		$tag = $rs['tag'];
		//echo ">$tag<br/>";
		if (!isset($tagArray[$tag]))
		{
			$tagArray[$tag] = array();
			$tagArray[$tag][0] = 1; // 1 count
			$tagArray[$tag][1] = $rs['emotrating'];
			$tagArray[$tag][2] = $tag;

		}
		else
		{
			$currentCount = $tagArray[$tag][0];
			$currentWeight = $tagArray[$tag][1];
			$tagArray[$tag][1] = $currentWeight*($currentCount++)/($currentCount)+ $rs['emotrating']*(1/($currentCount));
			$tagArray[$tag][0] = $currentCount;
			$tagArray[$tag][2] = $tag;
		}
	}
	return $tagArray;
}
function getTopTagsSquare($lat,$lon,$spanLat,$spanLon,$numresults)
{
	global $DB;
	$query = getAurapointsFromRect($lat,$lon,$spanLat,$spanLon,$numresults);
	if (!$query){return SERVER_ERROR;}
	return getTopTagsFromAurapoints($query);
}
function getTopTags($lat,$lon,$radius,$numresults)
{
	global $DB;
	$query = getAurapointsFromCircle($lat,$lon,$radius,$numresults);
	if (!$query){return SERVER_ERROR;}
	return getTopTagsFromAurapoints($query);
}


?>