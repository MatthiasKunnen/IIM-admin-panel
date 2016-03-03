package domain;

import com.google.common.base.MoreObjects;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TargetGroup implements IEntity, Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false, unique = true)
    private String name;

    public TargetGroup(){
        
    }
    
    public TargetGroup(TargetGroup t){
        this.id = t.id;
        this.name = t.name;
    }
    
    public String getName() {
        return name;
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
        if (obj == null || !(obj instanceof TargetGroup))
            return false;
        TargetGroup targetGroup = (TargetGroup) obj;
        return targetGroup.getId() != 0 && targetGroup.getId() == getId() || super.equals(obj);
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
