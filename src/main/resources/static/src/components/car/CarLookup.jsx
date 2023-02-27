

class CarLookup extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            car: props.car,
            motorRegisters: [],
            responseType: null,
            responseMsg: null,
            show: false
        };
    }

    hide() {
        this.setState({ show: false })
    }

    show() {
        this.setState({ show: true })
    }

    lookup() {
        if (this.state.car.registrationNumber == "") {
            this.setState({ responseType: "danger", responseMsg: "Cannot lookup without registration number!" });
            return;
        }

        Api.get(`/motor/register/${this.state.car.registrationNumber}`, (json) => {
            this.setState({ motorRegisters: json });
        }, this.errorHandling.bind(this));
    }

    errorHandling(error) {
        this.setState({ responseType: 'danger', responseMsg: error.message });
    }

    render() {
        if (this.state.motorRegisters == null || this.state.motorRegisters.length == 0) {
            if (!this.state.show) {
                return (
                    <div className="d-grid gap-2 mt-2">
                        <button type="button"
                            className="btn btn-secondary btn-sm"
                            onClick={this.show.bind(this)}>Lookup Motor Register Details</button>
                    </div>
                );
            }

            if (this.state.show) {
                return (
                    <div className="mt-2">
                        <div>
                            {this.state.responseMsg &&
                                <Alert type={this.state.responseType}
                                    msg={this.state.responseMsg} />
                            }

                            <div className="d-flex gap-2">
                                <button type="button block"
                                    className="btn btn-primary btn-sm"
                                    onClick={this.lookup.bind(this)}>Confirm</button>

                                <button type="button"
                                    className="btn btn-secondary btn-sm"
                                    onClick={this.hide.bind(this)}>Cancel</button>
                            </div>
                        </div>
                    </div>
                );
            }
        }
        else {
            return (
                <div className="motor-register-collection mt-2">
                    <ul className="list-group">
                        {this.state.motorRegisters.map((motorRegister, index) =>
                            <li className="list-group-item" key={index}>
                                <CarMotorRegister motorRegister={motorRegister} />
                            </li>
                        )}
                    </ul>
                </div>
            );
        }
    }
}