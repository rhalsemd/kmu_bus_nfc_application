<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $userName = $_POST["userName"];
    $userBirth = $_POST["userBirth"];
    
    $statement = mysqli_prepare($con, "SELECT userID, userPhone FROM USER WHERE userName = ? AND userBirth = ?");
    mysqli_stmt_bind_param($statement, "ss", $userName, $userBirth);
    mysqli_stmt_execute($statement);


    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $userPhone);

    $response = array();
    $response["success"] = false;
 
    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["userID"] = $userID;
        $response["userPhone"] = $userPhone;
    }

    echo json_encode($response);



?>
