

function CarCollection(props) {
    return (
        <div className="car-collection">
            {props.cars.map((car, index) =>
                <CarView key={index} 
                    car={car} 
                    members={props.members} 
                    prefix={props.prefix} 
                    username={props.username} 
                    refresh={props.refresh} />
            )}
        </div>
    );
}