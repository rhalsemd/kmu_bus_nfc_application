<?php 
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $bus_name = $_POST["bus_name"];
    $bus_type = $_POST["bus_type"];

    
    $statement = mysqli_prepare($con, "DELETE FROM bus_info WHERE bus_name = ? AND bus_type = ?");
    mysqli_stmt_bind_param($statement, "ss", $bus_name, $bus_type);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;
       
    echo json_encode($response);

?>
