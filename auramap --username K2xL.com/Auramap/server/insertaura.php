<?
require_once("login.php");

// Step 1: Secure parameters
$lat = floatval($Headers['lat']);
$long = floatval($Headers['long']);
$emotx = intval($Headers['emotx']);
$emoty = intval($Headers['emoty']); 
$tag = $Headers['tag'];
// Step 2: Insert into DB
$Data = array();
$Data['user_id']= $Me['id'];
$Data['tag'] = "'$tag'";
$Data['lat'] = $lat;
$Data['long'] = $long;
$Data['emotx'] = $emotx;
$Data['emoty'] = $emoty;

$insertOK = $DB->insert("aurapoints",$Data);
if (!$insertOK)
{
	return SERVER_ERROR;
}

?>