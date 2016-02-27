package domain;

import exceptions.AzureException;
import exceptions.LoginException;
import javafx.collections.ObservableList;
import persistence.PersistenceController;
import repository.AdministratorRepository;

public class DomainController {

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private MaterialRepository materialRepository;
    private ReservationRepository reservationRepository;
    private AdministratorRepository administratorRepository;
    private Administrator activeAdministrator;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public DomainController() {
        PersistenceController.start();
        this.administratorRepository = new AdministratorRepository(PersistenceController.getEnforcer());
        this.materialRepository = new MaterialRepository(PersistenceController.getEnforcer());
    }
    //</editor-fold>

    //<editor-fold desc="Getters" defaultstate="collapsed">
    public ObservableList<Material> getMaterials() {
        return materialRepository.getMaterials();
    }

    public ObservableList<MaterialIdentifier> getMaterialIdentifiers() {
        return materialRepository.getMaterialIdentifiers();
    }

    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">
    //<editor-fold desc="Material" defaultstate="collapsed">

    /**
     * Saves a new {@link domain.Material} in the database.
     *
     * @param material the Material to save.
     * @return the material with updates database fields.
     */
    public Material addMaterial(Material material) {
        return this.materialRepository.addMaterial(material);
    }


    /**
     * Removes a {@link domain.Material} from the database.
     *
     * @param material the Material to save.
     */
    public void removeMaterial(Material material) {
        this.materialRepository.removeMaterial(material);
    }

    /**
     * Updates a {@link domain.Material} in the database.
     *
     * @param material the Material to update.
     */
    public void update(Material material) {
        this.materialRepository.update(material);
    }

    /**
     * Updates the photo of a material.
     *
     * @param material  the material.
     * @param imagePath the path of the image.
     * @throws AzureException
     */
    public void updatePhoto(Material material, String imagePath) throws AzureException {
        this.materialRepository.updatePhoto(material, imagePath);
    }


    public boolean doesMaterialExist(Material material) {
        return this.materialRepository.getMaterialById(material.getId()) != null;
    }

    /**
     * Check if a name of a material already exists.
     *
     * @param name the name to check.
     * @return true if the name is already in use. False otherwise.
     */
    public boolean doesMaterialNameAlreadyExist(String name) {
        return this.materialRepository.doesMaterialNameAlreadyExist(name);
    }

    public Material getMaterialByName(String name) {
        return this.materialRepository.getMaterialByName(name);
    }
    //</editor-fold>

    //<editor-fold desc="Reservation" defaultstate="collapsed">
    public Reservation addReservation(Reservation reservation){
        return this.reservationRepository.addReservation(reservation);
    }
    public void removeReservation(Reservation reservation){
        this.reservationRepository.removeReservation(reservation);
    }
    public void update(Reservation reservation){
        this.reservationRepository.update(reservation);
    }
    public ObservableList<Reservation> getReservations(){
        return reservationRepository.getReservations();
    }
    //</editor-fold>

    //<editor-fold desc="Local security - login" defaultstate="collapsed">

    public void login(String username, String password) throws LoginException {
        activeAdministrator = administratorRepository.login(username, password);
    }

    public Administrator addAdministrator(Administrator administrator){
        return administratorRepository.add(administrator);
    }

    public void updateAdministrator(Administrator administrator){
        administratorRepository.remove(administrator);
    }

    public void removeAdministrator(Administrator administrator){
        administratorRepository.remove(administrator);
    }

    public boolean hasPermission(Administrator.Permission permission){
        return activeAdministrator.hasPermission(permission);
    }
    //</editor-fold>
    //</editor-fold>
}