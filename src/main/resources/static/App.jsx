'use strict';

class Api {
    static path = `${window.location.protocol}//${window.location.host}/api/v1`;

    static async get(endpoint, callback) {
        const response = await fetch(this.path + endpoint);
        callback(await response.json());
    };

    static async post(endpoint, data, callback) {
        const response = await fetch(this.path + endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        callback(await response.json());
    };
}

function Reservation(props) {
    return (
        <div className="card">
            <div className="card-body">
                <h5 className="card-title">
                    <span>Reservation ID: </span>
                    <span>#{props.reservation.id}</span>
                </h5>
                <h6 className="card-subtitle mb-2 text-muted">
                    <span>Car ID: </span>
                    <span>#{props.reservation.carId}</span>
                </h6>
                <div className="card-text">
                    <p>
                        Rental date: {props.reservation.rentalDate}.
                    </p>
                </div>
            </div>
        </div>
    );
}

function Alert(props) {
    return (
        <div className={`alert alert-${props.type}`} role="alert">
            {props.msg}
        </div>
    );
}

class CarReservationForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            car: props.car,
            reservation: null,
            responseType: null,
            responseMsg: null,
            rentalDateId: `car-${this.props.prefix}-${this.props.car.id}-rentalDate`,
            usernameId: `car-${this.props.prefix}-${this.props.car.id}-username`
        };
    }

    rent() {
        const rentalDate = document.getElementById(this.state.rentalDateId);
        const username = document.getElementById(this.state.usernameId);

        if (rentalDate.value == "") {
            this.setState({ responseType: "danger", responseMsg: "Rental date cannot be empty!" });
            return;
        }

        if (username.value == "") {
            this.setState({ responseType: "danger", responseMsg: "Username cannot be empty!" });
            return;
        }

        if (this.state.reservation != null) {
            this.setState({ responseType: "danger", responseMsg: "An reservation has already been created!" });
            return;
        }

        const body = {
            memberUsername: username.value,
            carId: this.state.car.id,
            rentalDate: `${rentalDate.value.replace('T', ' ')}:00.0`
        }

        Api.post('/reservations', body,
            (json) => {
                this.setState({ reservation: json });
                this.props.carView.setState({ reservation: json });
            });
    }

    render() {
        if (this.state.reservation == null) {
            return (
                <div className="card">
                    <div className="card-body">
                        {this.state.responseMsg &&
                            <Alert type={this.state.responseType} msg={this.state.responseMsg} />
                        }

                        <div className="mb-3">
                            <label htmlFor={this.state.rentalDateId} className="form-label">Rental date:</label>
                            <input type="datetime-local" id={this.state.rentalDateId} className="form-control form-control-sm" />
                        </div>

                        <div className="mb-3">
                            <label htmlFor={this.state.usernameId} className="form-label">User:</label>
                            <select id={this.state.usernameId} className="form-control form-control-sm">
                                <option value="">Select user</option>
                                {this.props.members.map((member, index) =>
                                    <option key={index} value={member.username}>{member.username}</option>
                                )}
                            </select>
                        </div>

                        <button type="button" className="btn btn-primary btn-sm"
                            onClick={this.rent.bind(this)}>Rent</button>
                    </div>
                </div>
            );
        }
    }
}

class CarView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            car: props.car,
            reservation: null
        };
    }

    render() {
        return (
            <div className="card mb-3">
                <div className="card-body">
                    <h5 className="card-title">
                        <span>{this.state.car.brand} </span>
                        <span>{this.state.car.model}</span>
                    </h5>
                    <h6 className="card-subtitle mb-3 text-muted">
                        <span>
                            <span>Price pr. day: </span>
                            <span>{this.state.car.pricePrDay} </span>
                        </span>
                        <span className="vr"></span>
                        <span>
                            <span> Best discount: </span>
                            <span>{this.state.car.bestDiscount} </span>
                        </span>
                        <span className="vr"></span>
                        <span>
                            <span> Created: </span>
                            <span>{this.state.car.bestDiscount} </span>
                        </span>
                    </h6>
                    <div className="card-text">
                        {this.state.reservation &&
                            <Reservation reservation={this.state.reservation} />
                        }
                    </div>
                    <CarReservationForm car={this.state.car} members={this.props.members} carView={this} prefix={this.props.prefix} />
                </div>
            </div>
        );
    }
}

function CarCollection(props) {
    return (
        <div className="car-collection">
            {props.cars.map((car, index) =>
                <CarView key={index} car={car} members={props.members} prefix={props.prefix} />
            )}
        </div>
    );
}

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = { 
            allCars: [],
            carsBestDiscount: [],
            carsNoReservations: [],
            averagePricePrDay: 0,
            members: [] 
        };

        Api.get('/cars', (json) => this.setState({ allCars: json }));
        Api.get('/cars/best-discount', (json) => this.setState({ carsBestDiscount: json }));
        Api.get('/cars/no-reservations', (json) => this.setState({ carsNoReservations: json }));
        Api.get('/cars/average-price-pr-day', (json) => this.setState({ averagePricePrDay: json }));
        Api.get('/members', (json) => this.setState({ members: json }));
    }

    render() {
        return (
            <div>
                <nav className="navbar bg-light mb-3">
                    <div className="container-fluid">
                        <span className="navbar-brand mb-0 h1">Car Rental App</span>
                        <span>Average rental price pr day: {this.state.averagePricePrDay}</span>
                    </div>
                </nav>

                <div className="container">
                    <h2 className="text-center">Cars with best discount</h2>
                    <CarCollection cars={this.state.carsBestDiscount} members={this.state.members} prefix={"best-discount"} />
                    <hr />
                    <h2 className="text-center">Cars with no reservations</h2>
                    <CarCollection cars={this.state.carsNoReservations} members={this.state.members} prefix={"no-reservations"} />
                    <hr />
                    <h2 className="text-center">All Cars</h2>
                    <CarCollection cars={this.state.allCars} members={this.state.members} prefix={"all"} />
                </div>
            </div>
        );
    }
}

const e = React.createElement;
const domContainer = document.querySelector('#root');
const root = ReactDOM.createRoot(domContainer);
root.render(e(App));