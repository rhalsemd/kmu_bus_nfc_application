<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');
    
    $bus_name = $_POST["bus_name"];
    $bus_type = $_POST["bus_type"];
    $date = $_POST["date"];

    $statement = mysqli_prepare($con, "SELECT COUNT(userlog_bus_seat.userID) FROM userlog_bus_seat WHERE seated_time LIKE '".$date."%' AND bus_info_id = (SELECT bus_info_id FROM bus_info WHERE bus_name = ? AND bus_type = ?) ORDER BY seated_time ASC");
    mysqli_stmt_bind_param($statement, "ss", $bus_name, $bus_type);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $total);

    $response = array();
    
    while(mysqli_stmt_fetch($statement)) {
        $temp = array();
        $temp["total"] = $total;
        array_push($response, $temp);
    }

    echo json_encode($response);
?>
