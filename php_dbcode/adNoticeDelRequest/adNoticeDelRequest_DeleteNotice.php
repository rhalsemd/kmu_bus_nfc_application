<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');
    
    $title = $_POST["title"];
    $content = $_POST["content"];
    $time = $_POST["time"];

    $statement = mysqli_prepare($con, "DELETE FROM notice WHERE title = ? AND content = ? AND time = ?");
    mysqli_stmt_bind_param($statement, "sss", $title, $content, $time);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;
 
    echo json_encode($response);
?>
