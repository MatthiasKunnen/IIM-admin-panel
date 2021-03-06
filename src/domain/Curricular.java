package domain;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Curricular implements IEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    protected Curricular() {

    }
    
    public Curricular(Curricular c){
        this.name = c.name;
        this.id = c.id;
    }

    public Curricular(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Curricular))
            return false;
        Curricular curricular = (Curricular) obj;
        return curricular.getId() != 0 && curricular.getId() == getId() || super.equals(obj);
    }
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("ID", id)
                .add("Name", name)
                .toString();
                
    }
}
