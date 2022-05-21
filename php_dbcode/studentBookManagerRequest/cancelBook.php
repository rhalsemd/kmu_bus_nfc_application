<?php 
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $canceled_time = $_POST["canceled_time"];
    
    $statement2 = mysqli_prepare($con, "DELETE FROM booking WHERE userID = ?");
    mysqli_stmt_bind_param($statement2, "s", $userID);
    mysqli_stmt_execute($statement2);

    $response = array();
    $response["success"] = true;
       
    echo json_encode($response);

?>
