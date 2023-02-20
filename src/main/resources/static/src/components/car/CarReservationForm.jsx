

class CarReservationForm extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            car: props.car,
            reservation: null,
            responseType: null,
            responseMsg: null,
            show: false,
            rentalDate: ""
        };
    }

    hide() {
        this.setState({ show: false })
    }

    show() {
        this.setState({ show: true })
    }

    handleRentalDate(event) {
        this.setState({ rentalDate: event.target.value });
    }

    rent() {
        if (this.state.rentalDate == "") {
            this.setState({ responseType: "danger", responseMsg: "Rental date cannot be empty!" });
            return;
        }

        if (this.state.reservation != null) {
            this.setState({ responseType: "danger", responseMsg: "An reservation has already been created!" });
            return;
        }

        const body = {
            memberUsername: this.props.username,
            carId: this.state.car.id,
            rentalDate: `${this.state.rentalDate}`
        }

        Api.post('/reservations', body, (json) => {
            this.setState({ reservation: json });
            this.props.carView.setState({ reservation: json });
            this.props.refresh();
        }, this.errorHandling.bind(this));
    }

    errorHandling(error) {
        this.setState({ responseType: 'danger', responseMsg: error.message });
    }

    render() {
        if (this.state.reservation == null) {
            if (!this.state.show) {
                return (
                    <div className="d-grid gap-2">
                        <button type="button"
                            className="btn btn-secondary btn-sm"
                            onClick={this.show.bind(this)}>Rent</button>
                    </div>
                );
            }

            if (this.state.show) {
                return (
                    <div>
                        <div>
                            {this.state.responseMsg &&
                                <Alert type={this.state.responseType}
                                    msg={this.state.responseMsg} />
                            }

                            <div className="mb-3">
                                <div className="mb-1">
                                    <small className="text-muted text-uppercase">Select rental date</small>
                                </div>
                                <input type="date"
                                    value={this.state.rentalDate}
                                    onChange={this.handleRentalDate.bind(this)}
                                    className="form-control form-control-sm" />
                            </div>

                            <div className="d-flex gap-2">
                                <button type="button block"
                                    className="btn btn-primary btn-sm"
                                    onClick={this.rent.bind(this)}>Confirm</button>

                                <button type="button"
                                    className="btn btn-secondary btn-sm"
                                    onClick={this.hide.bind(this)}>Cancel</button>
                            </div>
                        </div>
                    </div>
                );
            }
        }
    }
}