<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $nfcID = $_POST["nfcID"];
    $seated_time = $_POST["seated_time"];
    $seatMax = 45;
    $notable = 0;
    
    
    $booking_personnel_query = mysqli_prepare($con, "SELECT COUNT(userID) AS booking_personnel FROM booking WHERE bus_info_id = ?");
    mysqli_stmt_bind_param($booking_personnel_query, "s", $nfcID);
    mysqli_stmt_execute($booking_personnel_query);
    mysqli_stmt_store_result($booking_personnel_query);
    mysqli_stmt_bind_result($booking_personnel_query, $booking_personnel);
    
    $bus_seat_personnel_query = mysqli_prepare($con, "SELECT COUNT(userID) AS bus_seat_personnel FROM bus_seat WHERE bus_info_id = ?");
    mysqli_stmt_bind_param($bus_seat_personnel_query, "s", $nfcID);
    mysqli_stmt_execute($bus_seat_personnel_query);
    mysqli_stmt_store_result($bus_seat_personnel_query);
    mysqli_stmt_bind_result($bus_seat_personnel_query, $bus_seat_personnel);
    
    $bus_able = mysqli_prepare($con, "SELECT able FROM bus_info WHERE bus_info_id = ?");
    mysqli_stmt_bind_param($bus_able, "s",  $nfcID);
    mysqli_stmt_execute($bus_able);
    mysqli_stmt_store_result($bus_able);
    mysqli_stmt_bind_result($bus_able, $isable);
    
    $count = mysqli_prepare($con, "SELECT COUNT(userID) AS check_booked FROM booking WHERE userID = ?");
    mysqli_stmt_bind_param($count, "s", $userID);
    mysqli_stmt_execute($count);
    mysqli_stmt_store_result($count);
    mysqli_stmt_bind_result($count, $check_booked);
    
    $check_seat = mysqli_prepare($con, "SELECT COUNT(userID) AS check_user FROM bus_seat WHERE userID = ?");
    mysqli_stmt_bind_param($check_seat, "s", $userID);
    mysqli_stmt_execute($check_seat);
    mysqli_stmt_store_result($check_seat);
    mysqli_stmt_bind_result($check_seat, $check_user);
    
    $booking_check_query = mysqli_prepare($con, "SELECT COUNT(userID) AS booking_check FROM booking WHERE userID = ? AND bus_info_id = ?"); //유저가 예약해둔 버스와 현재 탑승버스가 동일한지를 대조한다
    mysqli_stmt_bind_param($booking_check_query, "ss", $userID, $nfcID);
    mysqli_stmt_execute($booking_check_query);
    mysqli_stmt_store_result($booking_check_query);
    mysqli_stmt_bind_result($booking_check_query, $booking_check);
    
    $response = array();
    $response["success"] = false;
    $response["maxed_out"] = false;
    $response["maxed_out_not_booked"] = false;
    $response["user_already"] = false;
    $response["bus_not_able"] = false;
    $response["booking_check_fail"] = false;

    if(mysqli_stmt_fetch($count) && mysqli_stmt_fetch($check_seat) && mysqli_stmt_fetch($booking_personnel_query) && mysqli_stmt_fetch($bus_seat_personnel_query) && mysqli_stmt_fetch($bus_able) && mysqli_stmt_fetch($booking_check_query))
    { // 상단의 쿼리문 다 실행되면
        
        if(($check_booked != 0) && ($check_user == 0) && ($isable != 0)) //예약자 명단에 있고, 아직 탑승되지 않은 인원
        {
            
            if($booking_check != 0) //만약 예약해둔 버스가 현재 탑승버스와 동일하면
                {
                $delete_booking = mysqli_prepare($con, "DELETE FROM booking WHERE userID = ?");
                mysqli_stmt_bind_param($delete_booking, "s", $userID);
                mysqli_stmt_execute($delete_booking);
                
                // bus_seat 테이블에 추가시킨다 (실제 탑승인원)
                 $insert_seat_booked = mysqli_prepare($con, "INSERT INTO bus_seat(userID, seated_time, bus_info_id) SELECT ?,?, bus_info_id FROM bus_info WHERE bus_info_id = ?");
                     mysqli_stmt_bind_param($insert_seat_booked, "sss", $userID, $seated_time, $nfcID);
                     mysqli_stmt_execute($insert_seat_booked);
                
                $insert_seat_booked_log = mysqli_prepare($con, "INSERT INTO userlog_bus_seat(userID, seated_time, bus_info_id) SELECT ?,?, bus_info_id FROM bus_info WHERE bus_info_id = ?");
                mysqli_stmt_bind_param($insert_seat_booked_log, "sss", $userID, $seated_time, $nfcID);
                mysqli_stmt_execute($insert_seat_booked_log);
                
                $response["success"] = true;
                }
                else if($booking_check == 0) //예약해둔 버스가 현재 탑승버스와 동일하지 않으면
                {
                    $response["booking_check_fail"] = true;
                }
        }
        else if(($check_booked == 0) && ($check_user == 0) && ($isable != 0)) //예약자 명단에 없고, 아직 탑승되지 않은 인원
        {
                if($booking_personnel + $bus_seat_personnel < $seatMax) //예약명단 + 실버스탑승객수 합한것이 좌석 max값보다 작을때
                {
                    $insert_seat_not_booked = mysqli_prepare($con, "INSERT INTO bus_seat(userID, seated_time, bus_info_id) SELECT ?,?, bus_info_id FROM bus_info WHERE bus_info_id = ?");
                    mysqli_stmt_bind_param($insert_seat_not_booked, "sss", $userID, $seated_time, $nfcID);
                    mysqli_stmt_execute($insert_seat_not_booked);
                    
                    $insert_seat_not_booked_log = mysqli_prepare($con, "INSERT INTO userlog_bus_seat(userID, seated_time, bus_info_id) SELECT ?,?, bus_info_id FROM bus_info WHERE bus_info_id = ?");
                    mysqli_stmt_bind_param($insert_seat_not_booked_log, "sss", $userID, $seated_time, $nfcID);
                    mysqli_stmt_execute($insert_seat_not_booked_log);
                    
                    $response["success"] = true;
                }
                else if($booking_personnel + $bus_seat_personnel >= $seatMax)
                {
                    $response["maxed_out_not_booked"] = true;
                }
        }
        else if(($bus_seat_personnel >= $seatMax))
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
    
    }
    else
    {
        
    }

    echo json_encode($response);

?>
