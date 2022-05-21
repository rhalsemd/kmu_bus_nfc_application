<?php 
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $bus_name = $_POST["bus_name"];
    $bus_type = $_POST["bus_type"];

    
    $statement = mysqli_prepare($con, "INSERT INTO bus_info(bus_name,bus_type) VALUES(?,?)");
    mysqli_stmt_bind_param($statement, "ss", $bus_name, $bus_type);
    mysqli_stmt_execute($statement);

    $statement2 = mysqli_prepare($con, "INSERT INTO map(bus_info_id) VALUES((SELECT bus_info_id FROM bus_info WHERE bus_name = ? AND bus_type = ?))");
    mysqli_stmt_bind_param($statement2, "ss", $bus_name, $bus_type);
    mysqli_stmt_execute($statement2);
    
    $response = array();
    $response["success"] = true;
       
    echo json_encode($response);

?>
