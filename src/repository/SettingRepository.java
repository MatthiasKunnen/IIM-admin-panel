/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domain.Setting;
import domain.Settings.Key;
import java.util.HashMap;
import java.util.Map;
import persistence.PersistenceEnforcer;


public class SettingRepository extends LoadedRepository<Setting> {

    private Map<Key, Setting> settings = new HashMap<>();

    public SettingRepository(PersistenceEnforcer persistence, Class<Setting> eClass) {
        super(persistence, eClass);
    }
    
    public Setting get(Setting setting){
        return (Setting) this.eList.stream().filter(s->s.getKey()==setting.getKey());
    }
}
