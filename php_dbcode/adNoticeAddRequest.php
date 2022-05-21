<?php 
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $title = $_POST["title"];
    $content = $_POST["content"];
    $time = $_POST["time"];
    $user = $_POST["user"];
    
    $statement = mysqli_prepare($con, "INSERT INTO notice(title,content,time,user) VALUES(?,?,?,?)");
    mysqli_stmt_bind_param($statement, "ssss", $title, $content, $time, $user);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;
       
    echo json_encode($response);

?>
