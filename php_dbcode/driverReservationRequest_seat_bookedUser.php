<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $bus_name = $_POST["bus_name"];
    $bus_type = $_POST["bus_type"];
    $seated_time = $_POST["seated_time"];


    $check_booked = mysqli_prepare($con, "SELECT COUNT(userID) AS check_user FROM bus_seat WHERE userID = ?");
    mysqli_stmt_bind_param($check_booked, "s", $userID);
    mysqli_stmt_execute($check_booked);
    mysqli_stmt_store_result($check_booked);
    mysqli_stmt_bind_result($check_booked, $check_user);
    
    $response = array();
    $response["success"] = false;
    $response["user_already"] = false;

    if(mysqli_stmt_fetch($check_booked)) {

        $delete_booking = mysqli_prepare($con, "DELETE FROM booking WHERE userID = ?");
        mysqli_stmt_bind_param($delete_booking, "s", $userID);
        mysqli_stmt_execute($delete_booking);
        
        if($check_user == 0)
        {
            // bus_seat 테이블에 추가시킨다 (실제 탑승인원)
             $insert_seat = mysqli_prepare($con, "INSERT INTO bus_seat(userID, seated_time, bus_info_id) SELECT ?, ?, bus_info_id FROM bus_info WHERE bus_name = ? AND bus_type = ?");
                 mysqli_stmt_bind_param($insert_seat, "ssss", $userID, $seated_time, $bus_name, $bus_type);
                 mysqli_stmt_execute($insert_seat);
            $response["success"] = true;
            
        }
        else if($check_user != 0)
        {
            $response["user_already"] = true;
        }
    
    }else{}

    echo json_encode($response);

?>
