<?
require_once("login.php");

// Step 1: Secure parameters
$lat = floatval($Headers['lat']);
$long = floatval($Headers['lon']);
$emotx = floatval($Headers['emotx']); 
$tag = $Headers['tag'];
// Step 2: Insert into DB
$Data = array();
$Data['user_id']= $Me['id'];
$Data['tag'] = "'$tag'";
$Data['lat'] = "'$lat'";
$Data['lon'] = "'$long'";
$Data['emotrating'] = "'$emotx'";
$Data['timestamp'] = "UNIX_TIMESTAMP()";
$insertOK = $DB->insert("aurapoints",$Data);
if (!$insertOK)
{
	echo SERVER_ERROR;
	exit();
}

$tagsArray = explode(",",$tag);

$breaker = 1000;
while (count($tagsArray) > 0 && $tagsArray[count($tagsArray)-1] == "" && $breaker > 0){ $breaker--; array_pop($tagsArray); }
$count = count($tagsArray);
$added = 0;
$aurapoint_id = mysql_insert_id();
$str = "INSERT INTO tags(aurapoint_id,tag,emotrating) VALUES";
for ($i = 0; $i < $count; $i++)
{
	$curTag = trim(strtolower($tagsArray[$i]));
	if (strlen($curTag) == 0) { continue; }
	$str.= "($aurapoint_id,'".$curTag."','$emotx')";
	if ($i < $count-1)
	{
		$str.=",";
	}
	$added++;
}
if ($added > 0){
	$insertOK = $DB->query($str);
	if (!$insertOK)
	{
		echo $str;//SERVER_ERROR;
		exit();
	}
}
echo SUCCESS;

?>