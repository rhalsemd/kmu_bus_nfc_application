<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $user = $_POST["user"];
    
    $statement = mysqli_prepare($con, "SELECT title,content,time,user FROM notice WHERE user = ? OR user='전체' ORDER BY time DESC");
    mysqli_stmt_bind_param($statement, "s", $user);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement,$title,$content,$time,$user);

    $response = array();

    while(mysqli_stmt_fetch($statement)) {
        $temp = array();
        $temp["title"] = $title;
        $temp["content"] = $content;
        $temp["time"] = $time;
        $temp["user"] = $user;
        array_push($response, $temp);
    }

    echo json_encode($response);
?>
