<?php
//receive data from android
$latlng = $_POST['Latlng'];
$latlng = json_decode($latlng);
$products = $_POST['Prodts'];
$products = json_decode($products);
//$price = $_POST['Price'];
//$price = json_decode($price);

$sname = $_POST['Name'];
$slocation = $_POST['Location'];
$swebsite = $_POST['Website'];
$semail = $_POST['Email'];
$stel = $_POST['Tel'];
$sdesc = $_POST['Desc'];

/*
$count1 =0;
$pp = array();
foreach($price as $data){
	$pp[$count1] = $data;
	$count1++;
}
*/
 
$count = 0;
$track_id = 1;
$nu_lat = array();
foreach($latlng as $value){
	$nu_lat[$count] = $value;
	$count++;
}



	include("connect.php");
	
	if($db){
	
	//Obtaining a unique id for supermarket
	$query = "select count(id) from super_mkt_details";
	$result = mysqli_query($db,$query);
	while($arr = mysqli_fetch_array($result)){
	$track_id = $track_id+$arr[0];
	}
		//$price = 'xxxxxx';

		//$sql6 ="insert into test(pdt) values ('$pp[0]')";
		//mysqli_query($db,$sql6);
		
		//Store super_mkt_details
	$sqlDetails = "insert into super_mkt_details(s_id_no,s_name,s_location,s_web,s_email,s_phone,s_desc) values('$sname$track_id','$sname','$slocation','$swebsite','$semail','$stel','$sdesc')";
	mysqli_query($db,$sqlDetails);
	
	//Store prodts
	foreach($products as $value1){
	
		$ind = strpos($value1,":");
		$ct = strlen($value1);
		$nam = substr($value1,0, $ind);
		$nam1 = substr($value1,$ind+1,($ct-$ind-1));
		
		//$sql1 ="insert into pdts(prodts,s_id_no) values ('$value1','$sname$track_id')";
		$sql1 ="insert into pdts(prodts,s_id_no,price) values ('$nam','$sname$track_id','$nam1')";
		mysqli_query($db,$sql1);
		
		}
	
	//Store lats and logs of supermarkets
	$sql = "insert into lat(lat,log,s_id_no) values($nu_lat[0],$latlng[1],'$sname$track_id')";
	mysqli_query($db,$sql);
	
	/*Store products
	foreach($products as $value1){
		$sql1 ="insert into pdts(prodts,s_id_no) values ('$value1','$sname$track_id')";
		mysqli_query($db,$sql1);
		}*/
	
	mysqli_close($db);
	}
	else{
	die(mysqli_error($db));
	}

