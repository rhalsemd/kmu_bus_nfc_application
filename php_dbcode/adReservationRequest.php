<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');
    
    $bus_name = $_POST["bus_name"];
    $bus_type = $_POST["bus_type"];
    $userID = $_POST["userID"];
    $canceled_time = $_POST["canceled_time"];

    $statement2 = mysqli_prepare($con, "DELETE FROM booking WHERE userID = ? AND bus_info_id = (SELECT bus_info_id FROM bus_info WHERE bus_name = ? AND bus_type = ?)");
    mysqli_stmt_bind_param($statement2, "sss", $userID, $bus_name, $bus_type);
    mysqli_stmt_execute($statement2);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);
?>
