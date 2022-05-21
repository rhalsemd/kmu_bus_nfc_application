<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $userBirth = $_POST["userBirth"];
    $userPassword = $_POST["userPassword"];
    
    $statement = mysqli_prepare($con, "SELECT userPhone FROM USER WHERE userID = ? AND userBirth = ?");
    mysqli_stmt_bind_param($statement, "ss", $userID, $userBirth);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userPhone);
    
    $passinsert = mysqli_prepare($con, "UPDATE USER SET userPassword = ? WHERE userID = ? AND userBirth = ?");
    mysqli_stmt_bind_param($passinsert, "sss", $userPassword, $userID, $userBirth);
    mysqli_stmt_execute($passinsert);

    $response = array();
    $response["success"] = false;
 
    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["userPhone"] = $userPhone;
        
    }

    echo json_encode($response);



?>
