package domain;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class ReservationTest {

    private Reservation reservation;
    private User user = new User();
    private List<MaterialIdentifier> reservationList;
    private MaterialIdentifier materialId;
    private Material material;
    private LocalDateTime pickUp = LocalDateTime.of(2016, Month.FEBRUARY, 1, 16, 0);
    private LocalDateTime bringBack = LocalDateTime.of(2016, Month.FEBRUARY, 14, 7, 0);
    private LocalDateTime reservationDate = LocalDateTime.of(2016, Month.JANUARY, 14, 2, 15);

    @Before
    public void init() {
        reservation = new Reservation();
        material = new Material("Tool");
        materialId = new MaterialIdentifier(material, Visibility.Student);
        reservationList = new ArrayList<>();
        reservationList.add(materialId);

    }

    @Test
    public void testSetMaterialIdentifierList() {
        reservation.setMaterialIdentifiersList(reservationList);
        Assert.assertEquals(reservationList, reservation.getMaterialIdentifiersList());
    }

    @Test
    public void testSetReservationDate() {
        reservation.setCreationDate(reservationDate);
        Assert.assertEquals(reservationDate, reservation.getCreationDate());
    }

    @Test
    public void testSetPickUpDate() {
        reservation.setStartDate(pickUp);
        Assert.assertEquals(pickUp, reservation.getStartDate());
    }

    @Test
    public void testSetBringBackDate() {
        reservation.setStartDate(pickUp);
        reservation.setEndDate(bringBack);
        Assert.assertEquals(bringBack, reservation.getEndDate());
    }

    //When bringBack is before pickUp, bringBack should be standard value (4 days after pickUp)
    @Test
    public void testSetBringBackDateBeforePickup() {
        reservation.setStartDate(pickUp);
        reservation.setEndDate(reservationDate);

        Assert.assertEquals(pickUp.plusDays(4), reservation.getEndDate());
    }
}
