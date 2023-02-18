

function CarDetails(props) {
    return (
        <ul className="list-group">

            <li className="list-group-item">
                <span>The average rental price is </span> 
                <strong>{Math.round(props.averagePricePrDay)} DKK.</strong>
            </li>

            <li className="list-group-item">
                <span>The current number of cars without reservations is </span>
                <strong>{props.carsNoReservations.length}.</strong>
            </li>

            {props.carsNoReservations.length > 0 &&
                <li className="list-group-item">
                    <span>A car without reservations is </span>
                    <strong>
                        {props.carsNoReservations[0].brand} {props.carsNoReservations[0].model}.
                    </strong>
                </li>
            }

            <li className="list-group-item">
                <span>The current number of cars with best discount is </span>
                <strong>{props.carsBestDiscount.length}.</strong>
            </li>

            {props.carsBestDiscount.length > 0 &&
                <li className="list-group-item">
                    <span>A car with the best discount is </span>
                    <strong>
                        {props.carsBestDiscount[0].brand} {props.carsBestDiscount[0].model}.
                    </strong>
                </li>
            }

        </ul>
    );
}