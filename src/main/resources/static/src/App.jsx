

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            allCars: [],
            carsBestDiscount: [],
            carsNoReservations: [],
            reservations: [],
            averagePricePrDay: 0,
            members: [],
            authenticatedUser: null
        };

        this.refresh();
    }

    tryRefresh() {
        this.refresh(this.state.authenticatedUser);
    }

    refresh(authenticatedUser) {
        if (authenticatedUser == null)
            return;

        Api.get('/cars', (json) => this.setState({ allCars: json }));
        Api.get('/cars/best-discount', (json) => this.setState({ carsBestDiscount: json }));
        Api.get('/cars/no-reservations', (json) => this.setState({ carsNoReservations: json }));
        Api.get('/cars/average-price-pr-day', (json) => this.setState({ averagePricePrDay: json }));
        Api.get('/members', (json) => this.setState({ members: json }));
        Api.get(`/reservations/find-all-by-member/${authenticatedUser.username}`, (json) => {
            this.setState({ reservations: json })
        });
    }

    logout() {
        this.setState({ authenticatedUser: null })
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
            <div className="d-flex justify-content-between">
                <div className="container p-5">
                    <CarCollection cars={this.state.allCars} 
                                   members={this.state.members} 
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
        );
    }
}

const e = React.createElement;
const domContainer = document.querySelector('#root');
const root = ReactDOM.createRoot(domContainer);
root.render(e(App));