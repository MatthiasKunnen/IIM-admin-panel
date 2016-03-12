package repository;

import domain.Setting;
import exceptions.RepositoryItemAlreadyExistsException;
import persistence.PersistenceEnforcer;
import util.ImmutabilityHelper;

/**
 * Repository managing all the settings of the application.
 *
 * @author Matthias Kunnen
 */
public class SettingRepository extends LoadedRepository<Setting> {

    public SettingRepository(PersistenceEnforcer persistence) {
        super(persistence, Setting.class);
    }

    @Override
    public Setting add(Setting add) {
        if (getSetting(add.getKey()) != null)
            throw new RepositoryItemAlreadyExistsException("Deze instelling komt al voor.", add);
        return super.add(add);
    }

    public Setting getSetting(Setting.Key key) {
        return getSetting(key, null);
    }

    public Setting getSetting(Setting.Key key, Setting defaultValue) {
        load();
        return ImmutabilityHelper.copyDefensively(this.eList.stream()
                .filter(s -> s.getKey() == key)
                .findAny()
                .orElse(defaultValue));
    }
}
