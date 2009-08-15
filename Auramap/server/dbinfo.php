<?
function connectDB() {
	global $DB;
	$DB = new mysql_helper("localhost", "mtserver2", "X:JyPz.p7TyNSSPD", "auramap");
	$DB->setDebug(true);
}
function login($username, $password) {
	global $DB;
	$loggedIn = $DB->count("users", "where username='$username' and password='$password'");
	return $loggedIn;
}
$Headers = $_POST;
if (isset($Headers['username'])== false)
{
	$Headers = $_GET;
}
?>