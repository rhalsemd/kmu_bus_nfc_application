<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $bus_type = $_POST["bus_type"];
    $bus_name = $_POST["bus_name"];
    $fixed_value = $_POST["fixed_value"];
    $column_id = $_POST["column_id"];
    
    $statement = mysqli_prepare($con, "UPDATE map SET ".$column_id."=? WHERE bus_info_id = (SELECT bus_info_id FROM bus_info WHERE bus_type=? AND bus_name=?)");
       mysqli_stmt_bind_param($statement, "sss", $fixed_value, $bus_type, $bus_name);
       mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;
       
    echo json_encode($response);

?>
