<?php
/*
 * Created on May 25, 2009
 */
 require_once("constants.php");
 require_once("utils/mysql_helper.class.php");
 require_once("utils/sp_session.class.php");
 require_once("utils/rss_php.class.php");
// CLEAN UP POST AND GET... USE A PHP INPUT FILTER!
//header("Content-type: text/xml");
$Headers = $_POST;
if (isset($Headers['username'])== false)
{
	$Headers = $_GET;
}
$username = $Headers['username'];
$password = md5($Headers['password']);

$DB = new mysql_helper("localhost","mtserver2","X:JyPz.p7TyNSSPD","auramap");
$DB->setDebug(true);
 
$loggedIn = $DB->count("users","where username='$username' and password='$password'");

if ($loggedIn == 0)
{
	echo LOGIN_ERROR;
	exit();
}

$Me = $DB->query("select * from users where username='$username' and password='$password'");
$Me = mysql_fetch_assoc($Me);

?>