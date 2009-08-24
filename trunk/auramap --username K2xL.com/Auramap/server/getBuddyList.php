<?php
require_once("login.php");

$str = "";
$buddies = $DB->query("select buddynumber,privacy from buddies where user_id=".$Me['id']);
if (!$buddies) { echo SERVER_ERROR; }
while ($rs = mysql_fetch_assoc($buddies))
{
	$num = ($rs['buddynumber']);
	$privacy = ($rs['privacy']);
	$userID = getUserWithPhone($num);
	if ($userID == false) { $userID = -1; }
	else { $userID = $userID['id']; }
	
	$permissions = -1;
	$lastupdate = 0;
	$happystate = -1;
	
	if ($userID > 0){
		$lastAura = getLatestAurapoint($userID);
		$happystate = $lastAura['emotrating'];
		$lastupdate = $lastAura['timestamp'];
		$lastupdate = time()-$lastupdate;
	}
	
	$str.= "$num,$happystate,$lastupdate,$privacy#";
	
}
echo $str;

function getLatestAurapoint($userID)
{
	global $DB;
	// Phone has been registered... Now I would check here if the $userID has given the $id permissions... but i'll save that for another day
	$sql = "select * from aurapoints where user_id=$userID order by timestamp desc limit 0,1";
	$latestAurapoint = $DB->query($sql);
	if (!$latestAurapoint) { echo SERVER_ERROR; exit(); }
	$latestAurapoint = mysql_fetch_assoc($latestAurapoint);

	return $latestAurapoint;
}
?>