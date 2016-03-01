/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Pieter
 */
public class MaterialIdentifierTest {

    public MaterialIdentifierTest() {
    }

    private MaterialIdentifier matID;
    private Material tool;

    @Before
    public void initialize() {
        tool = new Material("Tool");
        matID = new MaterialIdentifier(tool, Visibility.Student);
    }

    @Test
    public void testConstructor() {
        matID = new MaterialIdentifier(tool, Visibility.Student);
        Assert.assertEquals(matID.getVisibility(), Visibility.Student);
        Assert.assertEquals(matID.getInfo(), tool);
    }

    @Test
    public void testSetPlace() {
        matID.setPlace("kitchen");
        Assert.assertEquals(matID.getPlace(), "kitchen");
    }

    @Test
    public void testSetInfo() {
        matID.setInfo(tool);
        Assert.assertEquals(matID.getInfo(), tool);

    }

    @Test
    public void testSetVisibility() {
        matID.setVisibility(Visibility.Administrator);
        Assert.assertEquals(matID.getVisibility(), Visibility.Administrator);
    }

}
