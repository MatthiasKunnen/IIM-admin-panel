package exceptions;

import domain.Reservation;

public class ReservationAlreadyExistsException extends ReservationException {
    public ReservationAlreadyExistsException(String s) {
        super(s);
    }

    public ReservationAlreadyExistsException(Reservation r, String s) {
        super(r, s);
    }

    public ReservationAlreadyExistsException(String s, Reservation r) {
        super(s, r);
    }

    public ReservationAlreadyExistsException(Reservation r) {
        super(r);
    }
}
