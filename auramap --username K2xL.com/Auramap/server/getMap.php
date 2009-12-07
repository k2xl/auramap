<?php

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
$myImage = imagecreatetruecolor($W,$H);
$img = $myImage; // alias
$im = $img;

$red = imagecolorallocate($myImage, 255, 0, 0);
$black = imagecolorallocate($myImage, 255, 255, 255);
$black = imagecolorallocate($myImage, 255, 255, 254);

// Make the background transparent
imagefilledrectangle($im, 0, 0, $W, $H, imagecolorallocate($im, 255, 255, 255));

imagecolortransparent($myImage, $black);



$circlealpha = 1;//1;//0.05;
$depthGrad = 20;//20;
$radius *= .5;
//ImageString ($myImage, 4, $W/2, 0, "Num points: ".mysql_num_rows($pts), $textcolor); 
while ($rs = mysql_fetch_assoc($pts))
{
	$perc = (100+$rs['emotrating'])/200.0;
	$percX = (floatval($rs['lon']-$ilon)/($flon-$ilon));
	$percY =  (floatval($rs['lat']-$ilat)/($flat-$ilat));
	
	$x = $percX*$W;
	$y = $percY*$H;
	$inde = intval(4*$rs['emotrating']);
	$tempAlpha = 100;//100;//128*(1.0*$j/$depthGrad);	
	//if ($inde != 4) { $tempAlpha = 127;continue; }
	$Color = $ArrayColors[$inde];
	
	
	$r = $Color >> 16&0xFF;//rand(0,256);
	$g = $Color >> 8 &0xFF;//rand(0,256);
	$b = $Color & 0xFF;//rand(0,256);
	//$c = imagecolorallocatealpha($myImage,$r,$g,$b,128*(1-$circlealpha));
	

	for ($j = $depthGrad; $j > 0 ; $j--)
	{
		
		$tempc = imagecolorallocatealpha($myImage,$r,$g,$b,$tempAlpha);//128*(1-$circlealpha));
		//$tempR = 5+$j/$radius;
		$tempR = 1.0*$radius*($j/$depthGrad);
		imagefilledellipse($myImage,$x+rand(0,5)-2.5,$y+rand(0,5)-2.5,$tempR*2,$tempR*2,$tempc);
	}
	
}
$numPointsTotal = countAurapointsFromRect($ilat,$ilon,$flat,$flon);
$numrows = min(10,intval($numPointsTotal/10)+1);
$numcols = 3;
$pwidth = $W/$numcols;
$pheight = $H/$numrows;
$spanLat = abs($flat-$ilat);
$spanLon = abs($flon-$ilon);
$CellLatSpan = $spanLat/$numrows;
$CellLonSpan = $spanLon/$numcols;

$radiusTag = abs(($flat-$ilat)/$numrows)* 69.05500; //  69.05500 is average lat to mile conversion
for ($rows = 0; $rows < $numrows; $rows++)
{
	$percDown = 1.0*($rows)/($numrows);
	for ($cols = 0; $cols < $numcols; $cols++)
	{
		$percAcross = 1.0*($cols)/($numcols);
		$valueDown = $ilat-$spanLat*$percDown; // minus since lat doesn't go + while down
		$valueAcross = $ilon+$spanLon*$percAcross;
		//$tagArray = getTopTagsSquare($ilat,$ilon,$flat,$flon,1);
		$tagArray = getTopTagsSquare($valueDown, $valueAcross,$valueDown-$CellLatSpan,$valueAcross+$CellLonSpan,1);
		if (!is_array($tagArray)) 
		{ 
			$str = ""; /*round($valueDown,5).",".round($valueAcross,5).",".round($radiusTag,5); */
			//$str = round($valueAcross,4);
			$emor = 0;
			$strength = 1;
		}
		else
		{
			
			foreach ($tagArray as $key => $value) {
				$arr = $tagArray[$key];
				break;
			}
			$str = $arr[2];
			$emor = $arr[1];
			$strength = $arr[0];
		}
		
		$px = $W*$percAcross;
		$py = $H*$percDown;
		//imageellipse($myImage,$px+$pwidth/2,$py+$pheight/2,5,5,$red);
		$font = min($strength+1,4);

		$inde = intval(4*$emor);
		$tempAlpha = 100;//128*(1.0*$j/$depthGrad);	
		//if ($inde != 4) { $tempAlpha = 127;continue; }
		$Color = $ArrayColors[$inde];

		$r = $Color >> 16&0xFF;//rand(0,256);
		$g = $Color >> 8 &0xFF;//rand(0,256);
		$b = $Color & 0xFF;//rand(0,256);

		$tempc = imagecolorallocatealpha($myImage,$r/3,$g/3,$b/3,0);//128*(1-$tempAlpha));

		drawstring($myImage,$font,$pwidth/2+$px-strlen($str)*imagefontwidth($font)/2,$pheight/2+$py-imagefontheight($font)/2,$str,$tempc);
	}
}
function drawstring($im,$font,$x,$y,$text,$col)
{
	global $blacklike,$red;
	//imagestring($im,$font,$x+1,$y+1,$text,$red);
	imagestring($im,$font,$x,$y,$text,$red);
//	imagestring($im,$font,$x-1,$y-1,$text,$col);
//	imagestring($im,$font,$x,$y+1,$text,$blacklike);
//	imagestring($im,$font,$x,$y-1,$text,$blacklike);
//	imagestring($im,$font,$x,$y,$text,$blacklike);
}
//exit();
Header ("Content-type: image/png");

ImagePng($myImage);
// destroy the reference pointer to the image in memory to free up resources
ImageDestroy($myImage); 


?>