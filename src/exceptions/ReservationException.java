/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author matthiasseghers
 */


import domain.Reservation;

public abstract class ReservationException extends IllegalArgumentException {
    private Reservation reservation;
    public ReservationException() {
    }

    public ReservationException(String s) {
        super(s);
    }

    public ReservationException(Reservation r) {
        this.reservation = r;
    }

    public ReservationException(Reservation r, String s){
        this(s, r);
    }

    public ReservationException(String s, Reservation r){
        super(s);
        this.reservation = r;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
