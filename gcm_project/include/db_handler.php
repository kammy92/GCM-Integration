<?php

/**
 * Class to handle all db operations
 * This class will have CRUD methods for database tables
 */
class DbHandler {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/db_connect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    // creating new user if not existed
    public function createUser($name, $mobile, $gcm_reg_id) {
        $response = array();

        // First check if user already existed in db
        if (!$this->isUserExists($mobile)) {
            // insert query
            $stmt = $this->conn->prepare("INSERT INTO users(name, mobile, gcm_reg_id) values(?, ?, ?)");
            $stmt->bind_param("sss", $name, $mobile, $gcm_reg_id);

            $result = $stmt->execute();

            $stmt->close();

            // Check for successful insertion
            if ($result) {
                // User successfully inserted
                $response["error"] = false;
                $response["user"] = $this->getUserByMobile($mobile);
            } else {
                // Failed to create user
                $response["error"] = true;
                $response["message"] = "Oops! An error occurred while registereing";
            }
        } else {
            // User with same mobile already existed in the db
            $this->updateGcmID($mobile, $name, $gcm_reg_id);
            $response["error"] = false;
            $response["message"] = "User already exists";
        }
        return $response;
    }

    // updating user GCM registration ID
    public function updateGcmID($mobile, $name, $gcm_reg_id) {
        $response = array();
        $stmt = $this->conn->prepare("UPDATE users SET gcm_reg_id = ?, name = ? WHERE mobile = ?");
        $stmt->bind_param("sss", $gcm_reg_id, $name, $mobile);

        if ($stmt->execute()) {
            // User successfully updated
            $response["error"] = false;
            $response["message"] = 'GCM registration ID updated successfully';
        } else {
            // Failed to update user
            $response["error"] = true;
            $response["message"] = "Failed to update GCM registration ID";
            $stmt->error;
        }
        $stmt->close();

        return $response;
    }

    // fetching single user by id
    public function getUser($user_id) {
        $stmt = $this->conn->prepare("SELECT id, name, mobile, gcm_reg_id, created_at FROM users WHERE id = ?");
        $stmt->bind_param("s", $user_id);
        if ($stmt->execute()) {
            // $user = $stmt->get_result()->fetch_assoc();
            $stmt->bind_result($id, $name, $mobile, $gcm_reg_id, $created_at);
            $stmt->fetch();
            $user = array();
            $user["user_id"] = $id;
            $user["name"] = $name;
            $user["mobile"] = $mobile;
            $user["gcm_reg_id"] = $gcm_reg_id;
            $user["created_at"] = $created_at;
            $stmt->close();
            return $user;
        } else {
            return NULL;
        }
    }

    // fetching multiple users by ids
    public function getUsers($user_ids) {

        $users = array();
        if (sizeof($user_ids) > 0) {
            $query = "SELECT id, name, mobile, gcm_reg_id, created_at FROM users WHERE id IN (";

            foreach ($user_ids as $user_id) {
                $query .= $user_id . ',';
            }

            $query = substr($query, 0, strlen($query) - 1);
            $query .= ')';

            $stmt = $this->conn->prepare($query);
            $stmt->execute();
            $result = $stmt->get_result();

            while ($user = $result->fetch_assoc()) {
                $tmp = array();
                $tmp["user_id"] = $user['id'];
                $tmp["name"] = $user['name'];
                $tmp["mobile"] = $user['mobile'];
                $tmp["gcm_reg_id"] = $user['gcm_reg_id'];
                $tmp["created_at"] = $user['created_at'];
                array_push($users, $tmp);
            }
        }

        return $users;
    }



    /**
     * Checking for duplicate user by mobile number
     * @param String $mobile mobile number to check in db
     * @return boolean
     */
    private function isUserExists($mobile) {
        $stmt = $this->conn->prepare("SELECT id from users WHERE mobile = ?");
        $stmt->bind_param("s", $mobile);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    /**
     * Fetching user by mobile
     * @param String $mobile User mobile number
     */
    public function getUserByMobile($mobile) {
        $stmt = $this->conn->prepare("SELECT id, name, gcm_reg_id, created_at FROM users WHERE mobile = ?");
        $stmt->bind_param("s", $mobile);
        if ($stmt->execute()) {
            // $user = $stmt->get_result()->fetch_assoc();
            $stmt->bind_result($user_id, $name, $gcm_reg_id, $created_at);
            $stmt->fetch();
            $user = array();
            $user["user_id"] = $user_id;
            $user["name"] = $name;
            $user["gcm_reg_id"] = $gcm_reg_id;
            $user["created_at"] = $created_at;
            $stmt->close();
            return $user;
        } else {
            return NULL;
        }
    }
}
?>
