<?php
require_once("login.php");

$expiration = NUDGE_EXPIRATION;

// Step 1: Validate
if (!isset($Headers['target']))
{
	echo PARAMETER_ERROR;
	exit();
}
$target = $Headers['target'];
$target = phonerize($target);
if ($target == false) { echo INVALID_PHONE_ERROR; exit(); };

// Step 2: Check if max nudges reached...
$myid = $Me['id'];
$alreadyNudged = $DB->count("nudges", "where expired<=1 and user_id=$myid and target_number=$target");
if ($alreadyNudged > 0)
{
	echo ALREADY_NUDGED; exit();
}
$numActive = $DB->count("nudges","where expired=0 and user_id=$myid");
if ($numActive > MAX_ACTIVE_NUDGES)
{
	echo MAX_NUDGES_REACHED; exit();
}

$Insert = array();
$Insert['user_id'] = "'$myid'";
$Insert['target_number'] = "'$target'";
$Insert['timestamp'] = "UNIX_TIMESTAMP()";
$q = $DB->insert("nudges",$Insert);
if (!$q)
{
	echo SERVER_ERROR; exit();
}
echo SUCCESS;
?>