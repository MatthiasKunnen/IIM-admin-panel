package repository;

import domain.TargetGroup;
import persistence.PersistenceEnforcer;

public class TargetGroupRepository extends LoadedRepository<TargetGroup>{

    public TargetGroupRepository(PersistenceEnforcer persistence) {
        super(persistence, TargetGroup.class);
    }
}
