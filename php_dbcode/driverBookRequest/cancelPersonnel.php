<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $driverID = $_POST["driverID"];
    $bus_name = $_POST["bus_name"];
    $bus_type = $_POST["bus_type"];
    
    $statement = mysqli_prepare($con, "SELECT userID FROM bus_seat WHERE userID = ? AND bus_info_id = (SELECT bus_info_id FROM bus_info WHERE userID = ? AND bus_name = ? AND bus_type = ?)");
    mysqli_stmt_bind_param($statement, "ssss", $userID, $driverID, $bus_name, $bus_type);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID);
    
    $response = array();
    $response["success"] = false;
 
    if(mysqli_stmt_fetch($statement)) { //예약자 일때 실행
        $response["success"] = true;
        //일단 booking에 있는 정보를 없애고
        $delete_bus_seat = mysqli_prepare($con, "DELETE FROM bus_seat WHERE userID = ?");
        mysqli_stmt_bind_param($delete_bus_seat, "s", $userID);
        mysqli_stmt_execute($delete_bus_seat);
        
        $recount = mysqli_prepare($con, "SELECT COUNT(userID) AS personnel FROM bus_seat WHERE bus_info_id = (SELECT bus_info_id FROM bus_info WHERE userID = ? AND bus_name = ? AND bus_type = ?)");
        mysqli_stmt_bind_param($recount, "sss", $driverID, $bus_name, $bus_type);
        mysqli_stmt_execute($recount);
        mysqli_stmt_store_result($recount);
        mysqli_stmt_bind_result($recount, $personnel_after);
        while(mysqli_stmt_fetch($recount))
        {
            $response["personnel_after"] = $personnel_after;
        }
    }

    echo json_encode($response);

?>
