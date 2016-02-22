package domain;

import exceptions.InvalidEmailException;
import exceptions.InvalidPriceException;
import org.apache.commons.validator.routines.EmailValidator;
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
    private String firm;
    private String firmEmail;
    private BigDecimal price;
    private String articleNr;
    //</editor-fold>

    //<editor-fold desc="Getters and setters" defaultstate="collapsed">
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {

        this.encoding = encoding;
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

    public String getFirm() {
        return this.firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public String getFirmEmail() {
        return this.firmEmail;
    }

    public void setFirmEmail(String firmEmail) throws InvalidEmailException {
        if (EmailValidator.getInstance().isValid(firmEmail)) {
            this.firmEmail = firmEmail;
        } else {
            throw new InvalidEmailException("email_bad_format", firmEmail);
        }
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) throws InvalidPriceException {
        if (price.compareTo(BigDecimal.ZERO) == -1) {
            throw new InvalidPriceException("price_less_than_zero");
        }
        this.price = price;
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
        this.firm = material.firm;
        this.firmEmail = material.firmEmail;
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
    //</editor-fold>

    @Override
    public String toString() {
        return toStringHelper(this)
                .omitNullValues()
                .add("id", id)
                .add("name", name)
                .add("encoding", encoding)
                .add("firm", firm)
                .add("firmEmail", firmEmail)
                .add("price", price)
                .add("articleNr", articleNr)
                .add("identifiers", items)
                .toString();
    }
}