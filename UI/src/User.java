/*adding this class to align with the UML we designed, also for the proper data structure we need
 when users start borrowing books in the future
 *** this is simpler to implement its tester
 */

/**
 * User representation in the system
 * This class aligns with the UML we designed
 * Supports features borrowing books and managing user roles. (e.g, admin and regular user)
 *
 */

public class User {

    // Stores the username of the user (the unique identifier)
    private String username;

    // Stores the user's password
    private String password;

    // Stores the user's role (admin or user)
    private String role;

    /**
     * Constructor that initializes a User Object
     * @param username The user's username
     * @param password The user's password
     * @param role The user's role(admin/user)
     */
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Gets the username of the user
     * @return uesrname
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the role of the user(admin/user)
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets the password of the user
     * @return password
     */
    public String getPassword() {
        return password;
    }
}