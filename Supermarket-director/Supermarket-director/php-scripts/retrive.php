<?php
	 $un = "UName";
	$uname = $_POST[$un];
	$upass = $_POST["Pass"]; 
	/* $uname = "leontymo";
	$upass = "tim"; */
	$server = "mysql11.000webhost.com";
	$user = "a2612428_ta";
	$dbase = "a2612428_ta";
	$pass = "Timothy93";
	$db = mysqli_connect($server,$user,$pass,$dbase);
         if($db){
        $sql = "select * from users where UName = '$uname' and Pass = '$upass'";
	$result = mysqli_query($db,$sql);
	$userdetails = array();
	while($arr = mysqli_fetch_array($result)){
		$userdetails['name'] = $arr['Name'];
		$userdetails['uname'] = $arr['UName'];
		$userdetails['email'] = $arr['Email'];
		$userdetails['pass'] = $arr['Pass'];
		$userdetails['tel'] = $arr['Tel'];
		$userdetails['bisname'] = $arr['BisName'];
		
	}
	echo json_encode($userdetails);
	mysqli_close($db);
     }else{
      echo("Fail");
    }
?>