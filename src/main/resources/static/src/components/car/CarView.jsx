

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
            <div className="card mb-3 shadow-sm">
                <div className="card-body">

                    <h5 className="card-title">
                        <span>{this.state.car.brand} </span>
                        <span>{this.state.car.model}</span>
                    </h5>

                    <hr />

                    <div className="d-flex gap-2 text-center justify-content-between">
                        <div>
                            <div>
                                <small className="text-muted text-uppercase">Price pr. day</small>
                            </div>
                            <strong>{this.state.car.pricePrDay} DKK</strong>
                        </div>
                        <div>
                            <div>
                                <small className="text-muted text-uppercase">Best discount</small>
                            </div>
                            <strong>{this.state.car.bestDiscount} DKK</strong>
                        </div>
                        <div>
                            <div>
                                <small className="text-muted text-uppercase">Created</small>
                            </div>
                            <strong>{this.state.car.created} </strong>
                        </div>
                    </div>

                    <hr />

                    <div className="card-text">
                        {this.state.reservation &&
                            <Reservation reservation={this.state.reservation} />
                        }
                    </div>

                    <CarReservationForm
                        username={this.props.username}
                        car={this.state.car}
                        members={this.props.members}
                        carView={this}
                        prefix={this.props.prefix}
                        refresh={this.props.refresh} />
                </div>
            </div>
        );
    }
}