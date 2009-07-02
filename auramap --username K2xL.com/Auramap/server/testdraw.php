<?
// example1.php



 
// set the HTTP header type to PNG
header("Content-type: image/png"); 
 
// set the width and height of the new image in pixels

$circlealpha = 0.05;
$radius = 50;
$imgSrc = "gtmap.JPG";

$myImage = imagecreatefromjpeg($imgSrc); 
// send the new PNG image to the browser
//ImagePNG($im); 


$width = imagesx($myImage);
$height = imagesy($myImage);
// Parse through all the circles
for ($a = 0; $a < 1000; $a++){
	$x = rand($radius,$width-$radius);
	$y = rand($radius,$height-$radius);
	$r = rand(0,256);
	$b = rand(0,256);
	$g = rand(0,256);
	$c = imagecolorallocatealpha($myImage,$r,$g,$b,128*(1-$circlealpha));
	imagefilledellipse($myImage,$x,$y,$radius*2,$radius*2,$c);
}

imagepng($myImage);

// destroy the reference pointer to the image in memory to free up resources
ImageDestroy($im); 

?>