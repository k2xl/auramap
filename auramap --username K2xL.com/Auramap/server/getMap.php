<?php
Header ("Content-type: image/png");
require_once("login.php");
 
/*$myimage = ImageCreate (200, 40); 
$bgcolor = ImageColorAllocate ($myimage, 0, 0, 0); 
$textcolor = ImageColorAllocate ($myimage, 255, 255, 255); 
ImageString ($myimage, 4, 96, 19, "test", $textcolor); 
ImagePng ($myimage); 
exit();*/
//Header("Content-Type: image/png"); 


// Step 1: Validate params
if (isset($Headers['ilat']) == false || isset($Headers['ilon']) == false)
{
	echo PARAMETER_ERROR; exit();
}
if (isset($Headers['flat']) == false || isset($Headers['flon']) == false)
{
	echo PARAMETER_ERROR; exit();
}
if (isset($Headers['w']) == false || isset($Headers['h']) == false)
{
	echo PARAMETER_ERROR; exit();
}
if (isset($Headers['radius']) == false)
{
	echo PARAMETER_ERROR; exit();
}
//$Headers = array();

/*$Headers['ilat'] = 33.78156339080061;
$Headers['ilon'] = -84.40765857696533;
$Headers['flat'] =  33.771075867373405;
$Headers['flon']= -84.38984870910645;
*/ 
$ilat = $Headers['ilat'];
$ilon = $Headers['ilon'];

$flat = $Headers['flat'];
$flon = $Headers['flon'];
/*$validCoord = validCoordinates($ilat,$ilon);
if ($validCoord != SUCCESS) { echo $validCoord; exit();}
$validCoord = validCoordinates($flat,$flon);
if ($validCoord != SUCCESS) { echo $validCoord; exit();}
*/
// Step 1: Secure parameters

$lon = ($ilon+$flon)/2;
$lat = ($ilat+$flat)/2;


//$sql = "SELECT emotrating,lat,long,((ACOS(SIN($lat* PI() / 180) * SIN(lat * PI() / 180) + COS($lat* PI() / 180) * COS(lat * PI() / 180) * COS(($lon- lon) * PI() / 180)) * 180 / PI()) * 60 * 1.1515) AS distance FROM aurapoints having distance<=$radius ORDER BY distance ASC ";
$sql = "Select emotrating,lat,lon from aurapoints where lat<$ilat and lat>$flat and lon>$ilon and lon<$flon";

$pts = $DB->query($sql);
//$pts = $DB->query("select * from aurapoints"); // where UNIX_TIMESTAMP()-timestamp > 500?
if(!$pts)
{
	echo SERVER_ERROR;
}
$str = "";

$ArrayColors = array();
$ArrayColors[0] = 0x33CCCC;
$ArrayColors[1] = 0x66CC99;
$ArrayColors[2] = 0x99CC66;
$ArrayColors[3] = 0xCC9933;
$ArrayColors[4] = 0xFF9900;
$radius = floatval($Headers['radius']);
$W = floatval($Headers['w']);
$H = floatval($Headers['h']);
$myImage = imagecreate($W,$H);
//$bgcolor = ImageColorAllocate ($myImage, 0, 0, 0); 
$bgcolor = ImageColorAllocate ($myImage, 0,0,0); 
$textcolor = ImageColorAllocate ($myImage, 255, 255, 255); 
$circlealpha = .5;//0.05;
ImageString ($myImage, 4, 96, 19, "Num points: ".mysql_num_rows($pts), $textcolor); 
while ($rs = mysql_fetch_assoc($pts))
{
	$perc = (100+$rs['emotrating'])/200.0;
	$percX = (floatval($rs['lon']-$ilon)/($flon-$ilon));
	$percY =  (floatval($rs['lat']-$ilat)/($flat-$ilat));
	
	$x = $percX*$W;
	$y = $percY*$H;
	$Color = $ArrayColors[intval(4*$rs['emotrating'])];
	
	
	$r = rand(0,256);
	$b = rand(0,256);
	$g = rand(0,256);
	$c = imagecolorallocatealpha($myImage,$r,$g,$b,128*(1-$circlealpha));
	imagefilledellipse($myImage,$x,$y,$radius*2,$radius*2,$c);
	
}

ImagePng($myImage);
// destroy the reference pointer to the image in memory to free up resources
ImageDestroy($myImage); 

//echo SUCCESS."$str";

?>