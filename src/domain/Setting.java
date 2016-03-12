/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import domain.Settings.Key;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Setting implements Serializable, IEntity {

    @Id
    private int id;
    private Key key;
    private Object data;

    public Setting() {
    }

    
    public Setting(Setting setting) {
        this.id = setting.id;
        this.key = setting.key;
        this.data = setting.data;
    }

    public Key getKey() {
        return key;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public int getId() {
        return this.id;
    }

}
