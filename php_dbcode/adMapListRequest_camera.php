<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $bus_type = $_POST["bus_type"];
    $bus_name = $_POST["bus_name"];
    $camera_height = $_POST["camera_height"];
    $camera_la = $_POST["camera_la"];
    $camera_lo = $_POST["camera_lo"];

    
    $statement = mysqli_prepare($con, "UPDATE map SET camera_height=?, camera_la=?, camera_lo=?  WHERE bus_info_id = (SELECT bus_info_id FROM bus_info WHERE bus_type=? AND bus_name=?)");
       mysqli_stmt_bind_param($statement, "sssss", $camera_height, $camera_la, $camera_lo, $bus_type, $bus_name);
       mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;
       
    echo json_encode($response);

?>
