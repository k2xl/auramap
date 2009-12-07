<?php
class sp_session {
	var $Hash = "";
	var $UserID;
	/** In seconds */
	static $Lifetime = 300; // in seconds (3 minutes)
	static $sqlhelper;
	var $valid;
	var $Row;
	static $tablename = "sessions";
	function sp_session($sqlhelper) {
		sp_session :: $sqlhelper = $sqlhelper;
		sp_session :: clearExpiredSessions();
		$this->Hash = $this->getSessionID();
		$this->valid = false;
		$this->UserID =sp_session::validSession($this->Hash); 
		if ($this->UserID)
		{
			$this->valid = true;
			$Expiration = time() + 60 * 60 * 24 * 30;
			setcookie("session_id",$this->Hash,$Expiration);
			$this->ping();
		}
		else
		{
			//createSession(0); // create a session with someone not logged in
		}
	}
	
	function getSessionID() {
		if (strlen($this->Hash) > 2)
			return $this->Hash;
		else
			if (isset ($_COOKIE['session_id']) && strlen($_COOKIE['session_id']) > 10 )
				return $_COOKIE['session_id'];
			else
				return sp_session :: generateSessionID();
	}
	static function deleteSession($session)
	{
		sp_session :: $sqlhelper->query("update users set last_logged_in_timestamp=UNIX_TIMESTAMP() where id=(select user_id from ".sp_session::$tablename." where session_id = '$session')");
		sp_session::$sqlhelper->delete(sp_session::$tablename,"where session_id='$session'");
	}
	static function createSession($userid)
	{
		$hashed = sp_session::generateSessionID();
		$Obj = array ();
		$Obj['user_ip'] = "'" . sp_session :: getip() . "'";
		$Obj['browser'] = "'" . sp_session :: get_browser() . "'";
		$Obj['user_id'] = $userid;
		$Obj['session_id'] = "'$hashed'";
		$Obj['created_timestamp'] = "UNIX_TIMESTAMP()";
		$Obj['ping_timestamp'] = "UNIX_TIMESTAMP()";
		$Obj['lifetime'] = sp_session::$Lifetime;
		sp_session :: $sqlhelper->insert(sp_session::$tablename, $Obj);
		$Expiration = time() + 60 * 60 * 24 * 30;
		setcookie("session_id",$hashed,$Expiration);
		return $hashed;
	} 
	/**
	 * Refreshes the session on the database
	 */
	function ping() {
		$Obj = array ();
		$Obj['ping_timestamp'] = "UNIX_TIMESTAMP()";
		sp_session :: $sqlhelper->update(sp_session::$tablename, $Obj, "where session_id='$this->Hash'");
	}
	/**
	 * @return The row of the user in the table.
	 */
	function getRow() {
		if (!isset ($this->Row)) {
			$sql = "Select * from ".sp_session::$tablename." where session_id = '$this->Hash'";
			$result = sp_session :: $sqlhelper->query($sql);
			$this->Row = mysql_fetch_assoc($result);
			return $this->Row;
		} else {
			return $this->Row;
		}
	}
	static function validSession($ID)
	{
		$q = sp_session :: $sqlhelper->query("select user_id from ".sp_session::$tablename." where session_id='$ID'");
		if (!$q) { return false;}
		$q = mysql_fetch_assoc($q);
		return $q['user_id']; 
	}
	static function setLifetime($val) {
		$Lifetime = $val;
	}
	static function generateSessionID() {
		return md5(rand(0, 999999999));
	}
	static function getActiveUsers() {
		return sp_session :: $sqlhelper->count(sp_session::$tablename);
	}
	static function clearExpiredSessions() {
		sp_session :: $sqlhelper->query("update users set last_logged_in_timestamp=UNIX_TIMESTAMP() where id=(select user_id from ".sp_session::$tablename." where UNIX_TIMESTAMP()-ping_timestamp > lifetime)");
		return sp_session :: $sqlhelper->delete(sp_session::$tablename, "where UNIX_TIMESTAMP()-ping_timestamp > lifetime");
	}
	static function get_browser() {
		$user_agent = $_SERVER['HTTP_USER_AGENT'];
		$browsers = array (
			'Opera' => 'Opera',
			'Mozilla Firefox' => '(Firebird)|(Firefox)',
			'Galeon' => 'Galeon',
			'Mozilla' => 'Gecko',
			'MyIE' => 'MyIE',
			'Lynx' => 'Lynx',
			'Netscape' => '(Mozilla/4\.75)|(Netscape6)|(Mozilla/4\.08)|(Mozilla/4\.5)|(Mozilla/4\.6)|(Mozilla/4\.79)',
			'Konqueror' => 'Konqueror',
			'SearchBot' => '(nuhk)|(Googlebot)|(Yammybot)|(Openbot)|(Slurp/cat)|(msnbot)|(ia_archiver)',
			'Internet Explorer 8' => '(MSIE 8\.[0-9]+)',
			'Internet Explorer 7' => '(MSIE 7\.[0-9]+)',
			'Internet Explorer 6' => '(MSIE 6\.[0-9]+)',
			'Internet Explorer 5' => '(MSIE 5\.[0-9]+)',
			'Internet Explorer 4' => '(MSIE 4\.[0-9]+)',

			
		);

		foreach ($browsers as $browser => $pattern) {
			if (eregi($pattern, $user_agent))
				return $browser;
		}
		return 'Unknown';
	}
	static function getip() {
		if (isset ($_SERVER)) {
			if (isset ($_SERVER["HTTP_X_FORWARDED_FOR"])) {
				$ip_addr = $_SERVER["HTTP_X_FORWARDED_FOR"];
			}
			elseif (isset ($_SERVER["HTTP_CLIENT_IP"])) {
				$ip_addr = $_SERVER["HTTP_CLIENT_IP"];
			} else {
				$ip_addr = $_SERVER["REMOTE_ADDR"];
			}
		} else {
			if (getenv('HTTP_X_FORWARDED_FOR')) {
				$ip_addr = getenv('HTTP_X_FORWARDED_FOR');
			}
			elseif (getenv('HTTP_CLIENT_IP')) {
				$ip_addr = getenv('HTTP_CLIENT_IP');
			} else {
				$ip_addr = getenv('REMOTE_ADDR');
			}
		}
		return $ip_addr;
	}
/**
	 * Logs the user in
	 * @param username Raw username
	 * @param password Password = hashed SHA1(MD5(raw_pass))
	 */
 	function login($username,$password)
 	{
 		$DB = sp_session :: $sqlhelper;
 		if ($this->isBanned()) {return false;}
 		$username = mysql_escape_string($username);
 		$password = mysql_escape_string($password);
 		//$count = $DB->count("sp_users","where username='$username' and password='$password'");
 		$query = $DB->query("select id from users where username='$username' and password='$password'");
 		$row = mysql_fetch_assoc($query);
 		if ($row['id'] > 0)
 		{
 			// If the user is already logged in under the same name, then clear the session
 			$AlreadyLoggedInAsSomeoneElse = ($row['id'] == $this->Session->UserID);
 			if ($this->isLoggedIn() && $AlreadyLoggedInAsSomeoneElse)
 			{
	 			return true;
 			}
 			else
 			{
 				// If a user was logged in but wants to log in as someone else
 				sp_session::deleteSession($_COOKIE['session_id']);
 				sp_session::createSession($row['id']);
 			}
 			return true;
 		}
 		if ($this->isLoggedIn())
 		{
 			$this->logout();
 		}
 		return false;
 	}
 	/**
 	 * Returns user row
 	 *
 	function getRow()
 	{
 		if ($this->isLoggedIn() == false)
 		{
 			return false;
 		}
 		else if (isset($this->Row))
 		{
 			return $this->Row;
 		}
 		$DB = $this->DB;
 		$username = $this->username;
 		$password = $this->password;
 		$sql = "select * from users where id=(Select user_id from sessions where session_id='".$_COOKIE['session_id']."')";
 		$q = $DB->query($sql);
 		$this->Row = mysql_fetch_assoc($q);
 		return $this->Row;	
 	}*/
 	/**
 	 * Returns true if logged in and false if not logged in
 	 */
 	function isLoggedIn()
 	{
 		// Using $loggedIn as a "cache"
 		if (isset($loggedIn) == true)
 		{
 			return $loggedIn;
 		}
 		return  $loggedIn = $this->Session->valid && !$this->isBanned($this->Row['id']);
 	}
 	function isBanned()
 	{
 		$sesRow = $this->getRow();
 		return sp_session :: $sqlhelper->count("banned","where user_ip='".$sesRow['user_ip']."'") > 0;
 	}
 	function logout()
 	{
 		$s = mysql_escape_string($_COOKIE['session_id']);
 		sp_session::deleteSession($s);
 		$_COOKIE['session_id'] = "";
 		return SUCCESS;
 	}
	
}
?>