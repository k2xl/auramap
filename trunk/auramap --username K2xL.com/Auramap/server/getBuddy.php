<?php
require_once("login.php");

// Step 1: Validate
if (!isset($Headers['buddynumber'])){ echo PARAMETER_ERROR; exit();}
$buddynumber = phonerize($Headers['buddynumber']);

// Step 2: Get user
$buddy = getUserWithPhone($buddynumber);
if (!$buddy) { echo UNKNOWN_PHONE; exit(); }

/* 
 * Format:
 * <last timestamp>,<last state>, <last tags>, <nudge allowed>
 * 
 */
// Step 3: Get last update
$buddyid = $buddy['id'];
$q = $DB-> query("select (UNIX_TIMESTAMP()-timestamp) as timestamp, emotrating, tag from aurapoints where user_id=$buddyid order by timestamp asc limit 0,1");
if (!$q) { echo SERVER_ERROR; exit(); }

if (mysql_num_rows($q) == 0)
{
	echo UNKNOWN_PHONE;
	exit();
}
$row = mysql_fetch_assoc($q);

$myid = $Me['id'];
$nudgeAllowed = $DB->count("nudges","where user_id=$myid and target_number=$buddynumber and expired=0");
if ($nudgeAllowed>0){ $nudgeAllowed = 0;}
else { $nudgeAllowed = 1; }
echo distanceOfTimeInWords($row['timestamp']).",;,".$row['emotrating'].",;,".$row['tag'].",;,".$nudgeAllowed;

?>