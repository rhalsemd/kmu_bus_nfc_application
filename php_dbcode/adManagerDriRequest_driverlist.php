<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $user = $_POST["user"];
    
    $statement = mysqli_prepare($con, "SELECT bus_name, bus_type, userID FROM bus_info WHERE userID!='' ORDER BY bus_info_id ASC");
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement,$bus_name,$bus_type,$userID);

    $response = array();

    while(mysqli_stmt_fetch($statement)) {
        $temp = array();
        $temp["bus_name"] = $bus_name;
        $temp["bus_type"] = $bus_type;
        $temp["userID"] = $userID;
        array_push($response, $temp);
    }

    echo json_encode($response);
?>
