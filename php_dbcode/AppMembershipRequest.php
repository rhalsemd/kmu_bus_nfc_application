<?php 
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $userPassword = $_POST["userPassword"];
    $userName = $_POST["userName"];
    $userBirth = $_POST["userBirth"];
    $userPhone = $_POST["userPhone"];
    
    $duplicatecheck = mysqli_prepare($con, "SELECT userID FROM USER WHERE userID = ? AND duplicate = 0");
       mysqli_stmt_bind_param($duplicatecheck, "s", $userID);
       mysqli_stmt_execute($duplicatecheck);
       mysqli_stmt_store_result($duplicatecheck);
       mysqli_stmt_bind_result($duplicatecheck, $userID);

       $response = array();
       $response["success"] = false;
    
        if(mysqli_stmt_fetch($duplicatecheck)) {
        $response["success"] = true;
        $statement = mysqli_prepare($con, "UPDATE USER SET userPassword = ?, userName = ?, userBirth = ?,userPhone = ?, duplicate = 1 WHERE userID = ?");
        mysqli_stmt_bind_param($statement, "sssss", $userPassword, $userName, $userBirth, $userPhone, $userID);
        mysqli_stmt_execute($statement);
        }
        else
        { }
    
    echo json_encode($response);

?>
