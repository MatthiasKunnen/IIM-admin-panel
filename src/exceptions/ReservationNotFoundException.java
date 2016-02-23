/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import domain.Reservation;

/**
 *
 * @author matthiasseghers
 */


public class ReservationNotFoundException extends ReservationException{
    public ReservationNotFoundException(String s) {
        super(s);
    }

    public ReservationNotFoundException(Reservation r, String s) {
        super(r, s);
    }

    public ReservationNotFoundException(String s, Reservation r) {
        super(s, r);
    }

    public ReservationNotFoundException(Reservation r) {
        super(r);
    }
}
