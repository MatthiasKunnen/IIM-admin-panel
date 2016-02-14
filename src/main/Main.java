package main;

import domain.DomainController;
import domain.Material;
import domain.MaterialIdentifier;
import domain.Visibility;

public class Main {
    public static void main(String[] args) {
        DomainController dc = new DomainController();
        Material material = new Material("lolol");
        MaterialIdentifier mi = new MaterialIdentifier(material, Visibility.Docent);
        material.addIdentifier(mi);
        material.setFirm("die firma toch");
        material = dc.addMaterial(material);
        material.setFirm("Een andere firma");
        dc.update(material);
        System.out.println(dc.getMaterials());
        System.out.println(dc.getMaterialIdentifiers());
    }
}
