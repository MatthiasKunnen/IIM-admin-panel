package domain;

public class MaterialIdentifier {

    private Material info;
    private String place;
    private int id;
    private Visibility visibility;

    public String getPlace() {
        return this.place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

   	public Material getInfo() {
		return this.info;
	}

	/**
	 * 
	 * @param info The material
	 */
	public void setInfo(Material info) {
		this.info = info;
	}

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * JPA constructor
     */
    protected MaterialIdentifier() {

    }

    /**
     *
     * @param info The material
     */
    public MaterialIdentifier(Material info, Visibility visibility) {
        this.setInfo(info);
        this.setVisibility(visibility);
    }
}