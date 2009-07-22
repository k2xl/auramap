<?
require_once("login.php");

// Step 1: Secure parameters
$pts = $DB->query("select * from aurapoints"); // where UNIX_TIMESTAMP()-timestamp > 500?
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
while ($rs = mysql_fetch_assoc($pts))
{
	$perc = (100+$rs['emotrating'])/200.0;
	$Color = $ArrayColors[intval(4*$rs['emotrating'])];
	$str .= "#".$rs['lat'].",".$rs['lon'].",".$Color;
}
echo SUCCESS."$str";

?>