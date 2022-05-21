<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $bus_name = $_POST["bus_name"];
    $bus_type = $_POST["bus_type"];
    
    
    $statement = mysqli_prepare($con, "SELECT start_time, end_time FROM bus_info WHERE bus_name = ? AND bus_type = ?");
    mysqli_stmt_bind_param($statement, "ss", $bus_name, $bus_type);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $start_time, $end_time);
    
    $is_before = mysqli_prepare($con, "SELECT COUNT(bus_type) AS check_before FROM bus_info WHERE bus_name = ? AND bus_type = ? AND bus_type LIKE '%등교%'");
    mysqli_stmt_bind_param($is_before, "ss", $bus_name, $bus_type);
    mysqli_stmt_execute($is_before);
    mysqli_stmt_store_result($is_before);
    mysqli_stmt_bind_result($is_before, $check_before);
    
    $response = array();
    $response["success"] = false;
    $response["is_before"] = false;

    if(mysqli_stmt_fetch($statement) && mysqli_stmt_fetch($is_before))
    {
        $response["success"] = true;
        $response["start_time"] = $start_time;
        $response["end_time"] = $end_time;
        
        if($check_before != 0)
        {
            $response["is_before"] = true;
        }
        
    }else{}
    
    echo json_encode($response);

?>
