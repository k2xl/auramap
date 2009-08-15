<?
// example1.php



 
// set the HTTP header type to PNG
header("Content-type: image/png");
require_once("login.php");
// set the width and height of the new image in pixels

$circlealpha = .5;//0.05;
$radius = 50;
$imgSrc = "gtmap.JPG";

$topleftlat = 33.78156339080061;
$topleftlong = -84.40765857696533;
$bottomrightlat =  33.771075867373405;
$bottomrightlong = -84.38984870910645; 
$myImage = imagecreatefromjpeg($imgSrc); 
// send the new PNG image to the browser 


$width = imagesx($myImage);
$height = imagesy($myImage);


$sql = "Select * from aurapoints where lat<$topleftlat and lat>$bottomrightlat and lon>$topleftlong and lon<$bottomrightlong";

$DataPoints = $DB->query("$sql");

/*
 * $black = imagecolorallocate($myImage, 0x00, 0x00, 0x00);
*	imagefttext($myImage, 13, 0, 0, 55, $black, "fonts/ARIAL.TTF", "Results:$x,$y");
 */
// Parse through all the circles

while ($rs = mysql_fetch_assoc($DataPoints))
//for ($a = 0; $a < 100; $a++)
//
{
	
 	
	//rand($radius,$width-$radius);
	
	$percX = (floatval($rs['lon']-$topleftlong)/($bottomrightlong-$topleftlong));
	$percY =  (floatval($rs['lat']-$topleftlat)/($bottomrightlat-$topleftlat));
	
	$x = $percX*$width;
	$y = $percY*$height;
	
	//$x = rand($radius,$width-$radius);
	//$y = rand($radius,$height-$radius);
	
	$r = rand(0,256);
	$b = rand(0,256);
	$g = rand(0,256);
	$c = imagecolorallocatealpha($myImage,$r,$g,$b,128*(1-$circlealpha));
	imagefilledellipse($myImage,$x,$y,$radius*2,$radius*2,$c);
}

imagepng($myImage);

// destroy the reference pointer to the image in memory to free up resources
ImageDestroy($myImage); 

?>