

function ReservationBadges(props) {
    return (
        <div>
            <div className="mb-2">
                <small className="text-muted text-uppercase">
                    <span>Your reservations </span>
                    <span>({props.reservations.length})</span>
                </small>
            </div>
            {props.reservations.length > 0 &&
                <div className="d-flex gap-2 flex-wrap">
                    {props.reservations.map((reservation, index) =>
                        <span key={index} className="badge bg-secondary">
                            #{reservation.id}
                        </span>
                    )}
                </div>
            }
            {props.reservations.length == 0 &&
                <small>
                    You have no reservations.
                </small>
            }
        </div>
    );
}