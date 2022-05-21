<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $bus_type = $_POST["bus_type"];
    $bus_name = $_POST["bus_name"];
    $reason = $_POST["reason"];
    $able = $_POST["able"];
    $uptime = $_POST["uptime"];
    
    if($able==0)
    {
    $statement_unable = mysqli_prepare($con, "INSERT INTO notice(title,content,time,user) VALUES('".$bus_name." ".$bus_type." 현재 운행불가','이하내용 동일',?,'전체')");
    mysqli_stmt_bind_param($statement_unable, "s", $uptime);
    mysqli_stmt_execute($statement_unable);
    }
    else if($able==1)
    {
    $statement_able = mysqli_prepare($con, "INSERT INTO notice(title,content,time,user) VALUES('".$bus_name." ".$bus_type." 현재 운행재개','이하내용 동일',?,'전체')");
    mysqli_stmt_bind_param($statement_able, "s", $uptime);
    mysqli_stmt_execute($statement_able);
    }
    else{}

    $statement2 = mysqli_prepare($con, "UPDATE bus_info SET able=?, reason=? WHERE bus_type=? AND bus_name=?");
    mysqli_stmt_bind_param($statement2, "ssss", $able, $reason, $bus_type, $bus_name);
    mysqli_stmt_execute($statement2);

    $response = array();
    $response["success"] = true;
       
    echo json_encode($response);

?>
