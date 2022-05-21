<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $bus_type = $_POST["bus_type"];
    $bus_name = $_POST["bus_name"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM map WHERE bus_info_id = (SELECT bus_info_id FROM bus_info WHERE bus_type = ? AND bus_name = ?)");
    mysqli_stmt_bind_param($statement, "ss", $bus_type, $bus_name);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $bus_info_id,
    $route1,$route1_la,$route1_lo,$route2,$route2_la,$route2_lo,$route3,$route3_la,$route3_lo,
    $route4,$route4_la,$route4_lo,$route5,$route5_la,$route5_lo,$route6,$route6_la,$route6_lo,
    $route7,$route7_la,$route7_lo,$route8,$route8_la,$route8_lo,$route9,$route9_la,$route9_lo,
    $route10,$route10_la,$route10_lo,$camera_la,$camera_lo,$camera_height);

    $response = array();
    $response["success"] = false;
 
while(mysqli_stmt_fetch($statement))
{
$response["success"] = true;
$response["route1"]=$route1;$response["route1_la"]=$route1_la;$response["route1_lo"]=$route1_lo;
$response["route2"]=$route2;$response["route2_la"]=$route2_la;$response["route2_lo"]=$route2_lo;
$response["route3"]=$route3;$response["route3_la"]=$route3_la;$response["route3_lo"]=$route3_lo;
$response["route4"]=$route4;$response["route4_la"]=$route4_la;$response["route4_lo"]=$route4_lo;
$response["route5"]=$route5;$response["route5_la"]=$route5_la;$response["route5_lo"]=$route5_lo;
$response["route6"]=$route6;$response["route6_la"]=$route6_la;$response["route6_lo"]=$route6_lo;
$response["route7"]=$route7;$response["route7_la"]=$route7_la;$response["route7_lo"]=$route7_lo;
$response["route8"]=$route8;$response["route8_la"]=$route8_la;$response["route8_lo"]=$route8_lo;
$response["route9"]=$route9;$response["route9_la"]=$route9_la;$response["route9_lo"]=$route9_lo;
$response["route10"]=$route10;$response["route10_la"]=$route10_la;
$response["route10_lo"]=$route10_lo;
$response["camera_la"]=$camera_la;$response["camera_lo"]=$camera_lo;
$response["camera_height"]=$camera_height;
}

echo json_encode($response);



?>
