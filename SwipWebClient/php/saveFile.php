﻿<?php
include 'util/sessions.php';

initConnections();

if ($isConnected)
{
	if (isset($_POST['content']) && isset($_POST['subdirectory']) && isset($_POST['fileName'])) {
		$path = "/stockage/data/".$_POST['subdirectory'].$_POST['fileName'];
		$file = fopen($path, "w");
		if (!$file) {
			throw new Exception("Unable to create the file at path : \"".$path."\"");
		} else {
			fputs($file, $_POST['content']);
			fclose($file);
		}
	} else {
		throw new Exception('Any content to save...');
	}
} else {
	throw new Exception("You don't have administrator rights....");
}
?>