<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    
    $statement = mysqli_prepare($con, "SELECT bus_name, bus_type, end_time, booked_time FROM booking,bus_info WHERE booking.userID = ? AND booking.bus_info_id = bus_info.bus_info_id");
    mysqli_stmt_bind_param($statement, "s", $userID);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $bus_name, $bus_type, $end_time, $booked_time);

    $response = array();
    $response["success"] = false;   
 
    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["bus_name"] = $bus_name;
        $response["bus_type"] = $bus_type;
        $response["booked_time"] = $booked_time;
        $response["end_time"] = $end_time;
    }

    echo json_encode($response);

?>
