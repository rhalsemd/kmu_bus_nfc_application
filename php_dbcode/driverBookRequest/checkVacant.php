<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $driverID = $_POST["driverID"];
    $bus_name = $_POST["bus_name"];
    $bus_type = $_POST["bus_type"];
    $seated_time = $_POST["seated_time"];
    $seatMax = 45;
    
    $count = mysqli_prepare($con, "SELECT COUNT(userID) AS personnel FROM bus_seat WHERE bus_info_id = (SELECT bus_info_id FROM bus_info WHERE bus_name = ? AND bus_type = ? AND userID = ?)");
    mysqli_stmt_bind_param($count, "sss", $bus_name, $bus_type, $driverID);
    mysqli_stmt_execute($count);
    mysqli_stmt_store_result($count);
    mysqli_stmt_bind_result($count, $personnel);
    
    $check_booked = mysqli_prepare($con, "SELECT COUNT(userID) AS check_user FROM bus_seat WHERE userID = ?");
    mysqli_stmt_bind_param($check_booked, "s", $userID);
    mysqli_stmt_execute($check_booked);
    mysqli_stmt_store_result($check_booked);
    mysqli_stmt_bind_result($check_booked, $check_user);
    
    $response = array();
    $response["success"] = false;
    $response["maxed_out"] = false;
    $response["user_already"] = false;

    if(mysqli_stmt_fetch($count) && mysqli_stmt_fetch($check_booked)) {
        $response["personnel"] = $personnel; //실탑승인원수
        // booking에 있는 정보 없애기 예약자건 아니건 일단 실행
        $delete_booking = mysqli_prepare($con, "DELETE FROM booking WHERE userID = ?");
        mysqli_stmt_bind_param($delete_booking, "s", $userID);
        mysqli_stmt_execute($delete_booking);
        
        if(($personnel < $seatMax) && ($check_user == 0))
        {
            // bus_seat 테이블에 추가시킨다 (실제 탑승인원)
             $insert_seat = mysqli_prepare($con, "INSERT INTO bus_seat(userID, seated_time, bus_info_id) SELECT ?, ?, bus_info_id FROM bus_info WHERE bus_name = ? AND bus_type = ? AND userID = ?");
                 mysqli_stmt_bind_param($insert_seat, "sssss", $userID, $seated_time, $bus_name, $bus_type, $driverID);
                 mysqli_stmt_execute($insert_seat);
            
            $insert_seat_log = mysqli_prepare($con, "INSERT INTO userlog_bus_seat(userID, seated_time, bus_info_id) SELECT ?, ?, bus_info_id FROM bus_info WHERE bus_name = ? AND bus_type = ? AND userID = ?");
                 mysqli_stmt_bind_param($insert_seat_log, "sssss", $userID, $seated_time, $bus_name, $bus_type, $driverID);
                 mysqli_stmt_execute($insert_seat_log);
            
            $response["success"] = true;
            
            
            //다시 카운트해줌
            $recount = mysqli_prepare($con, "SELECT COUNT(userID) AS personnel FROM bus_seat WHERE bus_info_id = (SELECT bus_info_id FROM bus_info WHERE bus_name = ? AND bus_type = ? AND userID = ?)");
            mysqli_stmt_bind_param($recount, "sss", $bus_name, $bus_type, $driverID);
            mysqli_stmt_execute($recount);
            mysqli_stmt_store_result($recount);
            mysqli_stmt_bind_result($recount, $personnel_after);
            while(mysqli_stmt_fetch($recount))
            {
                $response["personnel_after"] = $personnel_after;
            }
        }
        else if(($personnel >= $seatMax))
        {
            $response["maxed_out"] = true;
        }
        else if($check_user != 0)
        {
            $response["user_already"] = true;
        }
    
    }else{}

    echo json_encode($response);

?>
