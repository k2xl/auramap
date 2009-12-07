<?php
/**
 * A class to help facilitate mysql queries.
 * @author Danny Miller
 * @version 1.0
 * @date May 2008
 */
class mysql_helper {
	var $debug;
	var $db;
	var $num_queries;
	var $querylog;
	function mysql_helper($address, $username, $password, $database = "") {
		$this->db = mysql_pconnect($address, $username, $password);
		if ($database != "") {
			mysql_select_db($database, $this->db);
		}
		$this->querylog = "";
	}
	function count($table,$where = "")
	{
		$sql = "Select count(1) from $table $where";
		$result = $this->query($sql);
		$result = mysql_fetch_array($result);
		return $result[0];
	}
	function delete($table,$where)
	{
		$sql = "Delete from $table $where";
		return $this->query($sql);
	}
	/**
	 * Attempts to update, if no rows are updated then row is added.
	 */
	function updateInsert($table, $data, $where, $delay = false)
	{
		$result = $this->update($table,$data,$where);
		// If no rows were updated
		if ($result == 0)
		{
			$result = $this->insert($table,$data,$delay);
		}
		return $result;
	}
	function update($table, $data, $where) {
		$Keys = array_keys($data);
		$Values = array_values($data);
		$tempL = count($Keys); //key length and value length will always equal
		$SetValueStr = "";
		for ($var = 0; $var < $tempL; $var++) {
			$SetValueStr .= $Keys[$var] . "=" . $Values[$var];
			if ($var < $tempL -1) {
				$SetValueStr .= ",";
			}
		}
		$sql = "Update $table SET $SetValueStr $where";
		$result = $this->query($sql);
		return $result;
	}
	function insert($table, $data, $delay = false) {
		$Keys = $this->ArrayToComma(array_keys($data));
		$Values = $this->ArrayToComma(array_values($data));
		$sql = "Insert ";
		if ($delay == true) {
			$sql .= "delayed ";
		}
		$sql .= "into $table($Keys) values(" . $Values . ")";
		$result = $this->query($sql);
		return $result;
	}
	
	/**
	 * Translates an array to comma string
	 * Would be private if PHP version supports it
	 */
	function ArrayToComma($Array) {
		$str = "";
		$tempL = count($Array);
		for ($var = 0; $var < $tempL; $var++) {
			$str .= $Array[$var];
			if ($var < $tempL -1) {
				$str .= ",";
			}
		}
		return $str;
	}
	function setDebug($bool) {
		$this->debug = $bool;
	}
	function getQueryLog()
	{
		return $this->querylog;
	}
	function query($sql) {
		/**
		 * NOTE: 
		 * BE CAREFUL ABOUT PUTTING ANOTHER QUERY IN HERE AS 
		 * MYSQL_INSERT_ID() MAY BE USED IN FILES USING THIS CLASS!
		 */
		$this->num_queries++;
		$this->querylog.="Query #".$this->num_queries."\t$sql\n";
		$result = mysql_query($sql, $this->db);
		if (!$result) {
			$this->querylog.="error in above query [".mysql_error()."] \n";
			if ($this->debug){
				echo mysql_error() . "<br/>Query #".$this->num_queries." = [$sql]";
				exit ();
			}
			else return false;
		}
		
		/*if ($this->debug == true)
		{
			$this->querylog.="Query #".$this->num_queries."\t$sql\n";
			//echo "<br/>SQL = [$sql]</br>";
		}*/
		return $result;
	}
	static function mysql_result_to_array($q)
 	{
 		$arr = array();
		while($k = mysql_fetch_assoc($q))
		{
			array_push($arr,$k);
		}
		return $arr;
 	}
}
?>