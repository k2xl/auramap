<?
require_once("login.php");

// Step 1: Validate params
$lat = $Headers['lat'];
$lon = $Headers['lon'];

$validCoord = validCoordinates($lat,$lon);
if ($validCoord != SUCCESS) { echo $validCoord; exit();}

// Step 2: Get all aurapoints ids in radius
//$radius = SPACE_RADIUS;
//$radius = 100000;
$radius = intval($Headers['radius']);
$numresults = intval($Headers['numresults']);
$sql = "SELECT *,((ACOS(SIN($lat* PI() / 180) * SIN(lat * PI() / 180) + COS($lat* PI() / 180) * COS(lat * PI() / 180) * COS(($lon- lon) * PI() / 180)) * 180 / PI()) * 60 * 1.1515) AS distance FROM aurapoints having distance<=$radius ORDER BY distance ASC ";
$query = $DB->query($sql);

if (!$query){echo SERVER_ERROR; exit();}

// Step 3: Create tag lists
$numpts = mysql_num_rows($query);
if ($numpts == 0) { echo EMPTY_RESULT; exit(); }
$count = 0;
$sql = "Select * from tags where ";
while ($rs = mysql_fetch_assoc($query))
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
if (!$query){echo SERVER_ERROR; exit();}

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
//echo "<br/><br/>Looping thru tags<br/><br/>";

function comp($a,$b)
{
	return ($a[0] < $b[0]);
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