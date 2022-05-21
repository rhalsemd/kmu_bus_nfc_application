<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $bus_type = $_POST["bus_type"];
    $bus_name = $_POST["bus_name"];
    $status = $_POST["status"];
    
    if($status == "add")
    {
    $statement = mysqli_prepare($con, "UPDATE bus_info SET userID=? WHERE bus_type=? AND bus_name=?");
    mysqli_stmt_bind_param($statement, "sss", $userID, $bus_type, $bus_name);
    mysqli_stmt_execute($statement);
    }
    else if($status == "del")
    {
        $statement = mysqli_prepare($con, "UPDATE bus_info SET userID='' WHERE bus_type=? AND bus_name=?");
        mysqli_stmt_bind_param($statement, "ss",  $bus_type, $bus_name);
        mysqli_stmt_execute($statement);
    }

    $response = array();
    $response["success"] = true;
       
    echo json_encode($response);

?>
