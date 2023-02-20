

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            allCars: [],
            carsBestDiscount: [],
            carsNoReservations: [],
            reservations: [],
            averagePricePrDay: 0,
            authenticatedUser: null,
            responseType: null,
            responseMsg: null
        };

        //this.refresh();
    }

    tryRefresh() {
        this.refresh(this.state.authenticatedUser);
    }

    refresh(authenticatedUser) {
        if (authenticatedUser == null)
            return;

        Api.get('/cars', (json) => this.setState({ allCars: json }), this.errorHandling.bind(this));
        Api.get('/cars/best-discount', (json) => this.setState({ carsBestDiscount: json }), this.errorHandling.bind(this));
        Api.get('/cars/no-reservations', (json) => this.setState({ carsNoReservations: json }), this.errorHandling.bind(this));
        Api.get('/cars/average-price-pr-day', (json) => this.setState({ averagePricePrDay: json }), this.errorHandling.bind(this));
        Api.get(`/reservations/find-all-by-member/${authenticatedUser.username}`, (json) => {
            this.setState({ reservations: json })
        }, this.errorHandling.bind(this));
    }

    logout() {
        this.setState({ authenticatedUser: null })
    }

    errorHandling(error) {
        this.setState({ responseType: 'danger', responseMsg: error.message });
    }

    render() {
        if (this.state.authenticatedUser == null) {
            return (
                <div className="container mt-3">
                    <LoginForm prefix="login"
                        parentView={this}
                        refresh={this.refresh.bind(this)} />
                </div>
            );
        }

        return (
            <div>
                <div>
                    {this.state.responseMsg &&
                        <Alert type={this.state.responseType}
                            msg={this.state.responseMsg} />
                    }
                </div>
                <div className="d-flex justify-content-between">
                    <div className="container p-5">
                        <CarCollection cars={this.state.allCars}
                            prefix={"all"}
                            username={this.state.authenticatedUser.username}
                            refresh={this.tryRefresh.bind(this)} />
                    </div>

                    <div className="container p-5 bg-light">
                        <div className="position-fixed">
                            <UserMenu authenticatedUser={this.state.authenticatedUser}
                                refresh={this.tryRefresh.bind(this)}
                                logout={this.logout.bind(this)} />
                            <hr />

                            <CarDetails allCars={this.state.allCars}
                                carsBestDiscount={this.state.carsBestDiscount}
                                carsNoReservations={this.state.carsNoReservations}
                                averagePricePrDay={this.state.averagePricePrDay} />
                            <hr />

                            <ReservationBadges reservations={this.state.reservations} />
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

const e = React.createElement;
const domContainer = document.querySelector('#root');
const root = ReactDOM.createRoot(domContainer);
root.render(e(App));