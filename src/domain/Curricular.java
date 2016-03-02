package domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
        if (!(obj instanceof Curricular))
            return false;
        Curricular curricular = (Curricular) obj;
        return curricular.getId() != 0 && curricular.getId() == this.id || super.equals(obj);
    }
}
