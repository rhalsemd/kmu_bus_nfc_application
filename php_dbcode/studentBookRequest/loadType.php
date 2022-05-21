<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $bus_name = $_POST["bus_name"];
    $statement = mysqli_prepare($con, "SELECT DISTINCT bus_type FROM bus_info WHERE bus_name=?");
    mysqli_stmt_bind_param($statement, "s", $bus_name);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement,$bus_type);

    $response = array();

    while(mysqli_stmt_fetch($statement)) {
        $temp = array();
        $temp["bus_type"] = $bus_type;
        array_push($response, $temp);
    }

    echo json_encode($response);
?>
