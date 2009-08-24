<?php
require_once("login.php");

// Step 1: Secure parameters... Validate all phone number submissions and privacy
if (!isset($Headers['data']))
{
	echo PARAMETER_ERROR; exit();
}
$Data = $Headers['data'];
$Data = explode("#",$Data);
$count = count($Data);
if ($count == 0)
{
	echo EMPTY_RESULT; exit();
}
$queries = array();
for ($i = 0; $i < $count; $i++)
{
	// Now get all numbers and validate them
	$curRow = explode(",",$Data[$i]);
	if (count($curRow) < 2) { echo PARAMETER_ERROR." index $i / $count"; exit(); }
	$num = ($curRow[0]);
	$privacy = intval($curRow[1]);
	if (is_numeric($num) == false || strlen("".$num) < 3) { echo PARAMETER_ERROR; exit(); }
	
	if (strlen("".$num) == 10) { $num = "1$num"; }
	
	$Insert = array();
	$Insert['user_id'] = $Me['id'];
	$Insert['buddynumber'] = $num;
	$Insert['privacy'] = $privacy;
	array_push($queries,$Insert);
}
// Step 3: Delete all in buddies table with the userid to this phone
$DB->delete("buddies", "where user_id=".$Me['id']);
// Step 4: Reinsert buddies into table
$OK = true;
for ($i = 0; $i < $count; $i++)
{
	$Insert = $queries[$i];
	if (!$DB->insert("buddies",$Insert)) { $OK = false; break; }
}
if (!$OK){ echo SERVER_ERROR; exit();}
echo SUCCESS;

?>