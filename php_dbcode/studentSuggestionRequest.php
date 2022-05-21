<?php 
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $title = $_POST["title"];
    $content = $_POST["content"];
    $time = $_POST["time"];
   
    $statement = mysqli_prepare($con, "INSERT INTO suggest(title,content,time,userID) VALUES(?,?,?,?)");
    mysqli_stmt_bind_param($statement, "ssss", $title, $content, $time, $userID);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;
       
    echo json_encode($response);

?>
