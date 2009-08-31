<?php
require_once("login.php");

$str = "";
$buddies = $DB->query("select buddynumber,privacy from buddies where user_id=".$Me['id']);
if (!$buddies) { echo SERVER_ERROR; }

$buddydatas = array();
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
	$lastAura = false;
	if ($userID > 0){
		$lastAura = getLatestAurapoint($userID);
	}
	if (!$lastAura)
	{
		$happystate = $lastAura['emotrating'];
		$lastupdate = ($lastAura['timestamp']);
		//$lastupdate = time()-$lastupdate;
		$tempBud = array();
		$tempBud['num'] = $num;
		$tempBud['state'] = $happystate;
		$tempBud['lastupdate'] = $lastupdate;
		$tempBud['privacy'] = $privacy;
		array_push($buddydatas,$tempBud);
		//$str.= "$num,;,$happystate,;,$lastupdate,;,$privacy#";
	}
	else
	{
		$tempBud = array();
		$tempBud['num'] = $num;
		$tempBud['state'] = -1;
		$tempBud['lastupdate'] = 0;
		$tempBud['privacy'] = 0;
		array_push($buddydatas,$tempBud);
		//$str .= $num.",;,".EMPTY_RESULT."#";
	}
}
function buddysorter($a,$b)
{
	if ($a['lastupdate'] < $b['lastupdate'] && $a['lastupdate']!=0 || $b['lastupdate'] == 0)
	{
		return 0;
	}
	return 1;
}
usort($buddydatas,"buddysorter");
$tempC = count($buddydatas);
for ($i = 0; $i < $tempC; $i++)
{
	$tempBud = $buddydatas[$i];
	if ($tempBud['state'] == -1)
	{
		$str .= $tempBud['num'].",;,".EMPTY_RESULT."#";
	}
	else
	{
		$num = $tempBud['num'];
		$happystate =$tempBud['state'];
		$lastupdate = distanceOfTimeInWords($tempBud['lastupdate'],true);
		$privacy = $tempBud['privacy']; 
		$str.= "$num,;,$happystate,;,$lastupdate,;,$privacy#";
	}
}
echo $str;

function getLatestAurapoint($userID)
{
	global $DB;
	// Phone has been registered... Now I would check here if the $userID has given the $id permissions... but i'll save that for another day
	// * actually i wouldn't check here i'd do it in getUserWithPhone 
	$sql = "select id,emotrating,(UNIX_TIMESTAMP()-timestamp) as timestamp from aurapoints where user_id=$userID order by timestamp asc limit 0,1";
	$latestAurapoint = $DB->query($sql);
	if (!$latestAurapoint) { echo SERVER_ERROR; exit(); }
	$latestAurapoint = mysql_fetch_assoc($latestAurapoint);

	return $latestAurapoint;
}
?>