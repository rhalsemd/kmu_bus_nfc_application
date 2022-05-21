<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $statement = mysqli_prepare($con, "SELECT userID, userName, sanctions FROM USER WHERE admin = 0 ORDER BY userID");
    
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement,$userID,$userName,$sanctions);

    $response = array();
    
    while(mysqli_stmt_fetch($statement)) {
        $temp = array();
        $temp["userID"] = $userID;
        $temp["userName"] = $userName;
        $temp["sanctions"] = $sanctions;
        array_push($response, $temp);
    }

    echo json_encode($response);
?>
