<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $bus_name = $_POST["bus_name"];
    $bus_type = $_POST["bus_type"];
    $userID = $_POST["userID"];
    $booked_time = $_POST["booked_time"];
    $seatMax = 45;
    $notable = 0;
    
    $count = mysqli_prepare($con, "SELECT COUNT(userID) AS personnel FROM booking WHERE bus_info_id = (SELECT bus_info_id FROM bus_info WHERE bus_name = ? AND bus_type = ?)");
    mysqli_stmt_bind_param($count, "ss", $bus_name, $bus_type);
    mysqli_stmt_execute($count);
    mysqli_stmt_store_result($count);
    mysqli_stmt_bind_result($count, $personnel);
    
    $check_booked = mysqli_prepare($con, "SELECT COUNT(userID) AS check_user FROM booking WHERE userID = ?");
    mysqli_stmt_bind_param($check_booked, "s", $userID);
    mysqli_stmt_execute($check_booked);
    mysqli_stmt_store_result($check_booked);
    mysqli_stmt_bind_result($check_booked, $check_user);
    
    $bus_able = mysqli_prepare($con, "SELECT able FROM bus_info WHERE bus_name = ? AND bus_type = ?");
    mysqli_stmt_bind_param($bus_able, "ss", $bus_name, $bus_type);
    mysqli_stmt_execute($bus_able);
    mysqli_stmt_store_result($bus_able);
    mysqli_stmt_bind_result($bus_able, $isable);
    
    $response = array();
    $response["success"] = false;
    $response["maxed_out"] = false;
    $response["bus_not_able"] = false;
    $response["user_already"] = false;
  
    if(mysqli_stmt_fetch($count) && mysqli_stmt_fetch($check_booked) && mysqli_stmt_fetch($bus_able))
    {
        if(($personnel < $seatMax) && ($check_user == 0) && ($isable != 0))
        {
        $statement = mysqli_prepare($con, "INSERT INTO booking(userID,booked_time,bus_info_id) SELECT ?,?,bus_info_id FROM bus_info WHERE bus_name = ? AND bus_type = ?");
        mysqli_stmt_bind_param($statement, "ssss", $userID, $booked_time, $bus_name, $bus_type);
        mysqli_stmt_execute($statement);
            $response["success"] = true;
        }
        else if(($personnel >= $seatMax))
        {
            $response["maxed_out"] = true;
        }
        else if($check_user != 0)
        {
            $response["user_already"] = true;
        }
        else if($isable == 0)
        {
            $response["bus_not_able"] = true;
        }

    }else{}
    
    echo json_encode($response);

?>
