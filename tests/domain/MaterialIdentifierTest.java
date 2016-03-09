package domain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

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
        Assert.assertEquals("kitchen", matID.getPlace());
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
