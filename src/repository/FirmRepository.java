package repository;

import domain.Firm;
import persistence.PersistenceEnforcer;

public class FirmRepository extends LoadedRepository<Firm>{

    public FirmRepository(PersistenceEnforcer persistence) {
        super(persistence, Firm.class);
    }
}
