 <?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');
    
    $date = $_POST["date"];

    $statement = mysqli_prepare($con, "SELECT userlog_bus_seat.userID, bus_info.bus_name, bus_info.bus_type FROM userlog_bus_seat, bus_info WHERE userlog_bus_seat.seated_time LIKE '".$date."%' AND userlog_bus_seat.bus_info_id = bus_info.bus_info_id ORDER BY userlog_bus_seat.seated_time ASC");
    
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $bus_name_return, $bus_type_return);

    $response = array();
    
    while(mysqli_stmt_fetch($statement)) {
        $temp = array();
        $temp["userID"] = $userID;
        $temp["bus_name"] = $bus_name_return;
        $temp["bus_type"] = $bus_type_return;
        array_push($response, $temp);
    }

    echo json_encode($response);
?>
