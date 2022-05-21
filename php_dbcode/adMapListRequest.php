<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $bus_type = $_POST["bus_type"];
    $bus_name = $_POST["bus_name"];
    $route_id = $_POST["route_id"];
    $latitude_id = $_POST["latitude_id"];
    $longtitude_id = $_POST["longtitude_id"];
    $route_value = $_POST["route_value"];
    $latitude_value = $_POST["latitude_value"];
    $longtitude_value = $_POST["longtitude_value"];

    
    $statement = mysqli_prepare($con, "UPDATE map SET ".$route_id."=?,".$latitude_id."=?,".$longtitude_id."=? WHERE bus_info_id = (SELECT bus_info_id FROM bus_info WHERE bus_type=? AND bus_name=?)");
       mysqli_stmt_bind_param($statement, "sssss", $route_value, $latitude_value, $longtitude_value, $bus_type, $bus_name);
       mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;
       
    echo json_encode($response);

?>
