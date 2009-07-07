<?
require_once("login.php");

// Step 1: Secure parameters
$lat = floatval($Headers['lat']);
$long = floatval($Headers['lon']);
$emotx = intval($Headers['emotx']);
$emoty = intval($Headers['emoty']); 
$tag = $Headers['tag'];
// Step 2: Insert into DB
$Data = array();
$Data['user_id']= $Me['id'];
$Data['tag'] = "'$tag'";
$Data['lat'] = "'$lat'";
$Data['lon'] = "'$long'";
$Data['emotx'] = "'$emotx'";
$Data['emoty'] = "'$emoty'";

$insertOK = $DB->insert("aurapoints",$Data);
if (!$insertOK)
{
	echo SERVER_ERROR;
	exit();
}

echo SUCCESS;

?>