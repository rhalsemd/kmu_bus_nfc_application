<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');
    
    $sanctions = $_POST["sanctions"];
    $userID = $_POST["userID"];

    $statement = mysqli_prepare($con, "UPDATE USER SET sanctions = ? WHERE userID = ?");
    mysqli_stmt_bind_param($statement, "ss", $sanctions, $userID);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;
 
    echo json_encode($response);
?>
