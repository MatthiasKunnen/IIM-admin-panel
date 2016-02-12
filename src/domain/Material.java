package domain;

import exceptions.InvalidEmailException;
import exceptions.InvalidPriceException;
import org.apache.commons.validator.routines.EmailValidator;

import java.math.BigDecimal;
import java.util.*;

public class Material {

    private Set<MaterialIdentifier> items= new HashSet<>();
    private String encoding;
    private String name;
    private String description;
    private String firm;
    private String firmEmail;
    private BigDecimal price;
    private String articleNr;

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
        if (price.compareTo(BigDecimal.ZERO)==-1){
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



    /**
     * @param name Name of the material.
     */
    public Material(String name) {
        this.setName(name);
    }

    /**
     * JPA constructor
     */
    protected Material() {
    }

	/**
	 * 
	 * @param identifier The identifier to add.
	 */
	public void addIdentifier(MaterialIdentifier identifier) {
        items.add(identifier);
	}

	public Set<MaterialIdentifier> getIdentifiers() {
		return Collections.unmodifiableSet(items);
	}

	/**
	 * 
	 * @param identifier The identifier to remove.
	 */
	public void removeIdentifier(MaterialIdentifier identifier) {
		items.remove(identifier);
	}

}