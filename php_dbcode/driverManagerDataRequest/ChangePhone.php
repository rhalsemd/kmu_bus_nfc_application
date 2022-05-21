<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $userPhone = $_POST["userPhone"];
    
    $statement = mysqli_prepare($con, "UPDATE USER SET userPhone =? WHERE userID =?");
    mysqli_stmt_bind_param($statement, "ss", $userPhone, $userID);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;
       
    echo json_encode($response);

?>
