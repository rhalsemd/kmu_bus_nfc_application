<?php 
    $con = mysqli_connect("localhost", "busapplication", "nM485273!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $userPassword = $_POST["userPassword"];
    $userName = $_POST["userName"];
    $userBirth = $_POST["userBirth"];
    $userPhone = $_POST["userPhone"];
    
    $statement = mysqli_prepare($con, "INSERT INTO bus_info( , ) VALUES( , )");
    mysqli_stmt_bind_param($statement, "sssss", $userPassword, $userName, $userBirth, $userPhone, $userID);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;
       
    echo json_encode($response);

?>
