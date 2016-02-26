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
@Access(AccessType.PROPERTY)
@NamedQuery(name = "Administrator.findByName", query = "SELECT a FROM Administrator a WHERE lower(a.name) = lower(:name)")
public class Administrator implements IEntity{

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private int id;
    private String name;

    @Access(AccessType.FIELD)
    @Column(nullable = false)
    private String hash;
    private Set<Permission> permissions = new HashSet<>();
    private boolean isSuspended;

    public enum Permission {
        MANAGE_MATERIALS, VIEW_RESERVATIONS, MANAGE_RESERVATIONS, MANAGE_USERS
    }
    //</editor-fold>

    //<editor-fold desc="Properties" defaultstate="collapsed">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "permission")
    @ElementCollection(targetClass = Permission.class)
    @Convert(converter = PermissionsConverter.class)
    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
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

    @Override
    public String toString() {
        return toStringHelper(this)
                .omitNullValues()
                .add("id", id)
                .add("name", name)
                .add("suspended", isSuspended)
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
        this.setId(administrator.id);
        this.hash = administrator.hash;
        this.setName(administrator.name);
        this.permissions = new HashSet<>(administrator.getPermissions());
    }
    //</editor-fold>
}
