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
	if (count($curRow) == 0) { echo PARAMETER_ERROR; exit(); }
	$num = $curRow[0];
	$privacy = intval($curRow[1]);
	if (intval($num) == 0) { echo PARAMETER_ERROR; exit(); }
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