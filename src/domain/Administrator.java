package domain;

import persistence.PermissionsConverter;
import util.BCrypt;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * An entity class for System Administrators
 */
@Entity(name = "Administrator")
@NamedQuery(name = "Administrator.findByName", query = "SELECT a FROM Administrator a WHERE lower(a.name) = lower(:name)")
public class Administrator implements IEntity {

    //<editor-fold desc="Variables" defaultstate="collapsed">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String hash;

    @Column(name = "permission")
    @ElementCollection(targetClass = Permission.class)
    @Convert(converter = PermissionsConverter.class)
    private Set<Permission> permissions = new HashSet<>();

    private boolean suspended;

    public enum Permission {
        MANAGE_MATERIALS, VIEW_RESERVATIONS, MANAGE_RESERVATIONS, MANAGE_USERS
    }
    //</editor-fold>

    //<editor-fold desc="Properties" defaultstate="collapsed">
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    public void setPassword(String password) {
        this.hash = BCrypt.hashPassword(password, BCrypt.generateSalt());
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkPassword(password, this.hash);
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .omitNullValues()
                .add("id", id)
                .add("name", name)
                .add("suspended", suspended)
                .add("Permissions", permissions)
                .toString();
    }

    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    /**
     * Protected empty JPA constructor.
     */
    protected Administrator() {

    }

    /**
     * Public copy-constructor.
     *
     * @param administrator the administrator to copy.
     */
    public Administrator(Administrator administrator) {
        this.id = administrator.id;
        this.hash = administrator.hash;
        this.setName(administrator.name);
        this.permissions = new HashSet<>(administrator.getPermissions());
    }
    //</editor-fold>
}
