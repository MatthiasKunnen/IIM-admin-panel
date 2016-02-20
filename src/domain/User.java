package domain;

import persistence.PermissionsConverter;
import util.BCrypt;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * An entity class for System Administrators
 */
@Entity(name = "Administrator")
@Access(AccessType.PROPERTY)
public class User {

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private int id;
    private String name;

    @Access(AccessType.FIELD)
    @Column(nullable = true)
    private String hash;
    private Set<Permission> permissions = new HashSet<>();

    public enum Permission {
        Material, User
    }
    //</editor-fold>

    //<editor-fold desc="Properties" defaultstate="collapsed">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ElementCollection(targetClass = Permission.class)
    @Convert(converter = PermissionsConverter.class)
    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">
    @Transient
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    public void setPassword(String password) {
        this.hash = BCrypt.hashPassword(password, BCrypt.generateSalt());
    }

    @Transient
    public boolean checkPassword(String password) {
        return BCrypt.checkPassword(password, this.hash);
    }

    public void addPermission(Permission permission){
        this.permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
    }
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    /**
     * Protected empty JPA constructor.
     */
    protected User() {

    }

    /**
     * Public copy-constructor.
     *
     * @param user the user to copy.
     */
    public User(User user) {
        this.setId(user.id);
        this.hash = user.hash;
        this.setName(user.name);
        this.permissions = new HashSet<>(user.getPermissions());
    }
    //</editor-fold>
}
