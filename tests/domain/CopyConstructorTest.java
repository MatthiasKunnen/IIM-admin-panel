package domain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import util.ImmutabilityHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@RunWith(value = Parameterized.class)
public class CopyConstructorTest {

    private Object test;
    private static Map<Class, Map<String, Object>> values;

    public static void begin() {
        values = new HashMap<>();

        Map<String, Object> adminValues = new HashMap<>();
        adminValues.put("id", 2);
        adminValues.put("name", "Admin");
        adminValues.put("suspended", true);
        adminValues.put("hash", "Test");
        Set<Administrator.Permission> permissions = new HashSet<>();
        permissions.add(Administrator.Permission.MANAGE_MATERIALS);
        permissions.add(Administrator.Permission.MANAGE_USERS);
        adminValues.put("permissions", permissions);

        values.put(Administrator.class, adminValues);
    }

    public CopyConstructorTest(Object test) {
        this.test = test;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {
        begin();
        Administrator admin = new Administrator();
        admin.setName((String) values.get(admin.getClass()).get("name"));
        admin.setSuspended((Boolean) values.get(admin.getClass()).get("suspended"));
        setPrivateField(admin, values.get(admin.getClass()).get("hash"), "hash");
        admin.setPermissions((Set<Administrator.Permission>) values.get(admin.getClass()).get("permissions"));
        setPrivateField(admin, values.get(admin.getClass()).get("id"), "id");

        return Arrays.asList(new Object[][]{
                {admin}
        });
    }

    @Test
    public void testCopyConstructor() {
        Object copy = ImmutabilityHelper.copyDefensively(this.test);
        List<Field> privateFields = new ArrayList<>();
        Field[] allFields = this.test.getClass().getDeclaredFields();
        for (Field field : allFields) {
            if (Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
                privateFields.add(field);
            }
        }

        privateFields.forEach(pf -> {
            try {
                Assert.assertEquals(String.format("Field %s was not copied correctly.", pf.getName()), values.get(test.getClass()).get(pf.getName()), pf.get(copy));
            } catch (IllegalAccessException e) {
                Assert.fail();
                e.printStackTrace();
            }
        });
    }

    private static <T extends IEntity> void setPrivateField(T o, Object value, String field) {
        try {
            Field idField = o.getClass().getDeclaredField(field);
            idField.setAccessible(true);
            idField.set(o, value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
