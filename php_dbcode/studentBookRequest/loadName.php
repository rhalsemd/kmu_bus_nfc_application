<?php
    $con = mysqli_connect("localhost", "busapplication", "DBjeong1!", "busapplication");
    mysqli_query($con,'SET NAMES utf8');

    $statement = mysqli_prepare($con, "SELECT DISTINCT bus_name FROM bus_info ORDER BY bus_name ASC");
    
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement,$bus_name);

    $response = array();

    while(mysqli_stmt_fetch($statement)) {
        $temp = array();
        $temp["bus_name"] = $bus_name;
        array_push($response, $temp);
    }

    echo json_encode($response);
?>
