package repository;

import domain.Curricular;
import persistence.PersistenceEnforcer;

public class CurricularRepository extends LoadedRepository<Curricular> {

    public CurricularRepository(PersistenceEnforcer persistence) {
        super(persistence, Curricular.class);
    }
}
