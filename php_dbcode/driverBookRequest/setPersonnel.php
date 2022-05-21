<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $driverID = $_POST["driverID"];
    $bus_name = $_POST["bus_name"];
    $bus_type = $_POST["bus_type"];
    
    $statement = mysqli_prepare($con, "SELECT count(userID) AS personnel FROM bus_seat WHERE bus_info_id = (SELECT bus_info_id FROM bus_info WHERE userID = ? AND bus_name = ? AND bus_type = ?)");
    mysqli_stmt_bind_param($statement, "sss", $driverID, $bus_name, $bus_type);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $personnel);

    $response = array();
    $response["success"] = false;
 
    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["personnel"] = $personnel;
    }

    echo json_encode($response);


?>
