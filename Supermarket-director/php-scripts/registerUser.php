<?php
	 $name = $_POST["Name"];
	$email = $_POST["Email"];
	$uname = $_POST["UName"];
	$upass = $_POST["Pass"];
	$bisname = $_POST["BisName"];
	$tel = $_POST["Tel"];  
	
	/* $name = "kasaga";
	$email = "kasaga";
	$uname = "kasaga";
	$upass = "kasaga";
	$bisname = "kasaga";
	$tel = "070123"; */
	
	$server = "mysql11.000webhost.com";
	$user = "a2612428_ta";
	$dbase = "a2612428_ta";
	$pass = "Timothy93";
	$db = mysqli_connect($server,$user,$pass,$dbase);
	$sql = "insert into users(Name,Email,UName,Pass,BisName,Tel) values('$name','$email','$uname','$upass','$bisname','$tel')";
	mysqli_query($db,$sql);
       //echo("Saved");
	mysqli_close($db);
?>