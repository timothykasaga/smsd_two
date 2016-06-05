<?php
	include("connect.php");
	//echo("good");
	$sql = "select lat.lat,lat.log,super_mkt_details.s_name,super_mkt_details.s_id_no,super_mkt_details.s_location,super_mkt_details.s_phone from lat,super_mkt_details where lat.s_id_no = super_mkt_details.s_id_no";
	$result = mysqli_query($db,$sql);
	while($row = mysqli_fetch_array($result)){
		$arr[] = array("lat"=>$row[0],"log"=>$row[1],"sname"=>$row[2],"sid"=>$row[3],"loc"=>$row[4],"contact"=>$row[5]);
		//$arr[] = $row;
	}
	$json = json_encode($arr);
	echo($json);
	//$sjond = json_decode($json);
	//var_dump($sjond);
?>