<?
require_once("login.php");
// Step 1: Validate params
if (isset($Headers['lat']) == false || isset($Headers['lon']) == false)
{
	echo PARAMETER_ERROR; exit();
}
$lat = $Headers['lat'];
$lon = $Headers['lon'];

$validCoord = validCoordinates($lat,$lon);
if ($validCoord != SUCCESS) { echo $validCoord; exit();}

// Step 2: Get all aurapoints ids in radius
//$radius = SPACE_RADIUS;
//$radius = 100000;
$radius = intval($Headers['radius']);
$numresults = intval($Headers['numresults']);

//echo "<br/><br/>Looping thru tags<br/><br/>";

function comp($a,$b)
{
	return ($a[0] < $b[0]);
}

$tagArray = getTopTags($lat,$lon,$radius, $numresults);
if (is_array($tagArray) == false)
{
	echo $tagArray;
	exit();
}
usort($tagArray,"comp");




$count = min($numresults,count($tagArray));
$str = "";

foreach ($tagArray as $key => $value) {
    $str.= $tagArray[$key][2].",".$tagArray[$key][0].",".$tagArray[$key][1]."#";
	if (--$count == 0){ break; }
}
$str = substr($str,0,strlen($str)-1);
echo $str;
?>