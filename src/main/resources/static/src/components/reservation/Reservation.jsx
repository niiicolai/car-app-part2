

function Reservation(props) {
    return (
        <div className="card">
            <div className="card-body">

                <h5 className="card-title">
                    <span>Reservation ID: </span>
                    <span>#{props.reservation.id}</span>
                </h5>

                <div className="card-text">
                    <p>Rental date: {props.reservation.rentalDate}.</p>
                </div>
            </div>
        </div>
    );
}