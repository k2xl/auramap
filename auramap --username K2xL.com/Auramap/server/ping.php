<?php
require_once("login.php");
$myusername = $Me['username'];
$myuserid = $Me['id'];

if (isset($Headers['clearnudges']))
{
	if ($Headers['clearnudges'] == 1)
	{
		$Update = array();
		$Update['expired'] = 1;
		$Update['read_timestamp'] = "UNIX_TIMESTAMP()";
		$q = $DB->update("nudges",$Update,"where target_number=$myusername and expired=0");
		if (!$q){ echo SERVER_ERROR; exit();}
		
		
		echo SUCCESS;
		exit();
	}
	else if ($Headers['clearnudges'] == 2)
	{
		$Update = array();
		$Update['expired'] = 3;
		$q = $DB->update("nudges",$Update, "where user_id=$myuserid and expired=2");
		
		echo SUCCESS." [q= $q]";
		exit();
	}
	echo PARAMETER_ERROR; exit();
}
$activeNudges = $DB->query("select (select username from users where id=nudges.user_id) as phone from nudges where target_number=$myusername and expired=0");
if (!$activeNudges)
{
	echo SERVER_ERROR; exit();
}
$count = mysql_num_rows($activeNudges);
if ($count== 0)
{
	echo NO_NUDGES;
	//	exit();
}
else{
	echo "1,;,";
	while ($rs = mysql_fetch_assoc($activeNudges))
	{
		echo $rs['phone'].",;,";
	}
	// get all your nudges that have been seen
}
echo "#";
$sql = "select timestamp,(select id from users where username=nudges.target_number) as target_id,read_timestamp,target_number from nudges where user_id=$myuserid and read_timestamp>0 and expired<=2";
$yourReadNudges = $DB->query($sql);
if (!$yourReadNudges)
{
	echo SERVER_ERROR; exit();
}
if (mysql_num_rows($yourReadNudges) == 0)
{
	echo "0";
	exit();
}
while ($rs = mysql_fetch_assoc($yourReadNudges))
{
	// now check to see if this person has updated their status since
	$targetid = $rs['target_id'];
	$nudgetime = $rs['timestamp'];
	$q = $DB->query("select * from aurapoints where user_id=$targetid and (timestamp>$nudgetime) order by timestamp asc limit 0,1");
	if (!$q) {echo SERVER_ERROR; exit();}
	$row = mysql_fetch_assoc($q);
	if (isset($row['timestamp']) == false) {continue; }
	
	//if ($row['timestamp'] < $rs['timestamp']) {  continue; }
	
	$Update = array();
	$Update['expired'] = 2;
	
	$q = $DB->update("nudges",$Update, "where target_number=".$rs['target_number']." and user_id=$myuserid and expired=1");
	if (!$q) { echo SERVER_ERROR; exit(); }	
	echo $rs['target_number'].",;,".$row['timestamp'].",;,".$row['emotrating'].",;,".$row['tag'].";;;";
	//echo $sql;
}

?>