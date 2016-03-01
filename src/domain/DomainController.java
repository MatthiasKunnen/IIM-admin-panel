package domain;

import repository.ReservationRepository;
import exceptions.AzureException;
import exceptions.LoginException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceController;
import repository.AdministratorRepository;
import repository.CurricularRepository;
import repository.FirmRepository;
import repository.MaterialRepository;
import repository.TargetGroupRepository;
import repository.UserRepository;

public class DomainController {

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private MaterialRepository materialRepository;
    private ReservationRepository reservationRepository;
    private AdministratorRepository administratorRepository;
    private Administrator activeAdministrator;
    private TargetGroupRepository targetGroupRepository;
    private CurricularRepository curricularRepository;
    private FirmRepository firmRepository;
    private UserRepository userRepository;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public DomainController() {
        PersistenceController.start();
        this.administratorRepository = new AdministratorRepository(PersistenceController.getEnforcer());
        this.materialRepository = new MaterialRepository(PersistenceController.getEnforcer());
        this.reservationRepository = new ReservationRepository(PersistenceController.getEnforcer());
        this.firmRepository = new FirmRepository(PersistenceController.getEnforcer());
        this.targetGroupRepository = new TargetGroupRepository(PersistenceController.getEnforcer());
        this.curricularRepository = new CurricularRepository(PersistenceController.getEnforcer());
        this.userRepository= new UserRepository(PersistenceController.getEnforcer());
    }
    //</editor-fold>

    //<editor-fold desc="Getters" defaultstate="collapsed">
    public ObservableList<Material> getMaterials() {
        return materialRepository.getMaterials();
    }

    public ObservableList<MaterialIdentifier> getMaterialIdentifiers() {
        return materialRepository.getMaterialIdentifiers();
    }   


    public ObservableList<Firm> getFirms() {
        return firmRepository.getExistingFirms();
    }

    public ObservableList<TargetGroup> getTargetGroups() {
        return targetGroupRepository.getOptions();
    }


    public ObservableList<Curricular> getCurriculars() {
        return curricularRepository.getOptions();
    }
     public User getUserByEmail(String email){
        return this.userRepository.getUserByEmail(email);
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
        return this.materialRepository.add(material);
    }

    /**
     * Removes a {@link domain.Material} from the database.
     *
     * @param material the Material to save.
     */
    public void removeMaterial(Material material) {
        this.materialRepository.remove(material);
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
     * @param material the material.
     * @param imagePath the path of the image.
     * @throws AzureException
     */
    public void updatePhoto(Material material, String imagePath) throws AzureException {
        this.materialRepository.updatePhoto(material, imagePath);
    }

    public boolean doesMaterialExist(Material material) {
        return this.materialRepository.doesMaterialExist(material);
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
    public ObservableList<Material> filterMaterialByName(String name){
        return FXCollections.unmodifiableObservableList(this.materialRepository.filterByName(name));
    }
    //</editor-fold>

    //<editor-fold desc="Firm-Curricular-TargetGroup" defaultstate="collapsed">
    /**
     * Saves a new {@link domain.Firm} in the database.
     *
     * @param firm the Firm to save.
     * @return the Firm with updates database fields.
     */
    public Firm addFirm(Firm firm) {
        return this.firmRepository.add(firm);

    }

    /**
     * Removes a {@link domain.Firm} from the database.
     *
     * @param firm the Firm to save.
     */
    public void removeFirm(Firm firm) {
        this.firmRepository.remove(firm);
    }

    /**
     * Updates a {@link domain.Firm} in the database.
     *
     * @param firm the firm to update.
     */
    public void updateFirm(Firm firm) {
        this.firmRepository.update(firm);
    }
    
    /**
     * Saves a new {@link domain.Curricular} in the database.
     *
     * @param curricular the Curricular to save.
     * @return the Curricular with updates database fields.
     */
    public Curricular addCurricular(Curricular curricular) {
        return this.curricularRepository.add(curricular);

    }

    /**
     * Removes a {@link domain.curricular} from the database.
     *
     * @param curricular the curricular to save.
     */
    public void removeCurricular(Curricular curricular) {
        this.curricularRepository.remove(curricular);
    }

    /**
     * Updates a {@link domain.Curricular} in the database.
     *
     * @param curricular the curricular to update.
     */
    public void updateCurricular(Curricular curricular) {
        this.curricularRepository.update(curricular);
    }

    /**
     * Saves a new {@link domain.TargetGroup} in the database.
     *
     * @param targetGroup the TargetGroup to save.
     * @return the TargetGroup with updates database fields.
     */
    public TargetGroup addTargetGroup(TargetGroup targetGroup) {
        return this.targetGroupRepository.add(targetGroup);

    }

    /**
     * Removes a {@link domain.targetGroup} from the database.
     *
     * @param targetGroup the targetGroup to save.
     */
    public void removeTargetGroup(TargetGroup targetGroup) {
        this.targetGroupRepository.remove(targetGroup);
    }

    /**
     * Updates a {@link domain.TargetGroup} in the database.
     *
     * @param targetGroup the targetGroup to update.
     */
    public void updateTargetGroup(TargetGroup targetGroup) {
        this.targetGroupRepository.update(targetGroup);
    }
    //</editor-fold>
    
    //<editor-fold desc="Reservation" defaultstate="collapsed">
    public Reservation addReservation(Reservation reservation) {
        return this.reservationRepository.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        this.reservationRepository.remove(reservation);
    }

    public void update(Reservation reservation) {
        this.reservationRepository.update(reservation);
    }
    public ObservableList<Reservation> getReservations(){
       return this.reservationRepository.getReservations();
    }
    public boolean doesReservationExist(Reservation reservation){
        return this.reservationRepository.doesReservationExist(reservation);
    }
    //</editor-fold>

    //<editor-fold desc="Local security - login" defaultstate="collapsed">
    public void login(String username, String password) throws LoginException {
        activeAdministrator = administratorRepository.login(username, password);
    }

    public Administrator addAdministrator(Administrator administrator) {
        return administratorRepository.add(administrator);
    }

    public void updateAdministrator(Administrator administrator) {
        administratorRepository.remove(administrator);
    }

    public void removeAdministrator(Administrator administrator) {
        administratorRepository.remove(administrator);
    }

    public boolean hasPermission(Administrator.Permission permission) {
        return activeAdministrator.hasPermission(permission);
    }
    //</editor-fold>
    //</editor-fold>
}
