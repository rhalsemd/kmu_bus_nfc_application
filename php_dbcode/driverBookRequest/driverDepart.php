<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $longitude = $_POST["longitude"];
    $latitude = $_POST["latitude"];
   
    
    $statement = mysqli_prepare($con, "UPDATE bus_info SET location_la=?, location_lo=? WHERE userID =?");
    mysqli_stmt_bind_param($statement, "sss", $latitude, $longitude, $userID);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;
       
    echo json_encode($response);

?>
