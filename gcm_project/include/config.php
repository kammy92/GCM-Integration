<?php
/**
 * Database configuration
 */

$host = $_SERVER['HTTP_HOST'];
//echo $host;
//exit;


if ($host == 'localhost') {
	define('DB_USERNAME', 'root');
	define('DB_PASSWORD', '');
	define('DB_HOST', 'localhost');
	define('DB_NAME', 'gcm_project');
} else if($host == 'actiknow-demo.com'){
	define('DB_USERNAME', 'actiknow_appuser');
	define('DB_PASSWORD', 'Z!Tq,ptL7i0V');
	define('DB_HOST', 'localhost');
	define('DB_NAME', 'actiknow_karman');
}



define("GOOGLE_API_KEY", "AIzaSyDYdL0BFayA5syJXhkXBT4bnQuTtNgsRTc");

// push notification flags
define('PUSH_FLAG_CHATROOM', 1);
define('PUSH_FLAG_USER', 2);

?>
