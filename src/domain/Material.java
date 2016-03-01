package domain;

import exceptions.InvalidPriceException;
import util.ImmutabilityHelper;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;

@Entity
public class Material implements Serializable, IEntity {

    //<editor-fold desc="Variables" defaultstate="collapsed">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MaterialIdentifier> items = new ArrayList<>();
    private String encoding;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;

    @ManyToOne
    private Firm firm;
    @ManyToMany
    private List<Curricular> curricular;
    @ManyToMany
    private List<TargetGroup> targetGroups;
    //private String firmEmail;
    @Column(scale = 2, precision = 10)
    private BigDecimal price;
    private String articleNr;
    //</editor-fold>

    //<editor-fold desc="Getters and setters" defaultstate="collapsed">
    public int getId() {
        return id;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding.toLowerCase();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Firm getFirm() {
        return this.firm;
    }

    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    public List<Curricular> getCurricular() {
        return curricular;
    }

    public void setCurricular(List<Curricular> curricular) {
        this.curricular = curricular;
    }

    public List<TargetGroup> getTargetGroups() {
        return targetGroups;
    }

    public void setTargetGroups(List<TargetGroup> targetGroups) {
        this.targetGroups = targetGroups;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) throws InvalidPriceException {
        if (price == null) {
            this.price = null;
            return;
        } else if (price.compareTo(BigDecimal.ZERO) == -1) {
            throw new InvalidPriceException(InvalidPriceException.Cause.LOWER_THAN_ZERO);
        } else if (price.precision() > 8) {
            throw new InvalidPriceException(InvalidPriceException.Cause.EXCEEDED_PRECISION);
        }
        this.price = price;
        if (price.scale() > 2) {
            throw new InvalidPriceException(InvalidPriceException.Cause.EXCEEDED_SCALE);
        }
    }

    public String getArticleNr() {
        return this.articleNr;
    }

    public void setArticleNr(String articleNr) {
        this.articleNr = articleNr;
    }

    public String getFileName() {
        return this.id == 0 || this.encoding == null ? null : String.format("%d.%s", this.id, this.encoding);
    }

    public String getPhotoUrl() {
        return this.id == 0 || this.encoding == null ? "" : "https://iim.blob.core.windows.net/images/" + getFileName();
    }
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    /**
     * @param name Name of the material.
     */
    public Material(String name) {
        this.setName(name);
    }

    /**
     * Copy constructor
     *
     * @param material The Material to copy the value from
     */
    public Material(Material material) {
        this.id = material.id;
        this.items = (List<MaterialIdentifier>) ImmutabilityHelper.copyCollectionDefensively(material.items, this);
        this.encoding = material.encoding;
        this.name = material.name;
        this.description = material.description;
        this.firm = ImmutabilityHelper.copyDefensively(material.firm);
        this.price = material.price;
        this.articleNr = material.articleNr;
    }

    /**
     * JPA constructor
     */
    protected Material() {
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">
    /**
     * @param identifier The identifier to add.
     */
    public void addIdentifier(MaterialIdentifier identifier) {
        items.add(identifier);
    }

    public List<MaterialIdentifier> getIdentifiers() {
        return Collections.unmodifiableList(items);
    }

    public void setIdentifiers(List<MaterialIdentifier> identifiers) {
        this.items = new ArrayList<>(identifiers);
    }

    /**
     * @param identifier The identifier to remove.
     */
    public void removeIdentifier(MaterialIdentifier identifier) {
        items.remove(identifier);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .omitNullValues()

                .add("id", id)
                .add("name", name)
                .add("encoding", encoding)
                .add("firm", firm.getName())
                .add("firm phonenumber", firm.getPhoneNumber())
                .add("firm email", firm.getEmail())
                .add("price", price)
                .add("articleNr", articleNr)
                .add("identifiers", items)

                .toString();
    }
    //</editor-fold>
}
