

class LoginForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            authenticatedUser: null,
            responseType: null,
            responseMsg: null,
            username: "user1",
            password: "pass1"
        };
    }

    handleUsername(event) {
        this.setState({ username: event.target.value });
    }

    handlePassword(event) {
        this.setState({ password: event.target.value });
    }

    handleKeydown(event) {
        if (event.key == 'Enter' ||
            event.key == 'NumpadEnter')
            this.login();
    }

    login() {
        if (this.state.username == "") {
            this.setState({ responseType: "danger", responseMsg: "Username cannot be empty!" });
            return;
        }

        if (this.state.password == "") {
            this.setState({ responseType: "danger", responseMsg: "Password cannot be empty!" });
            return;
        }

        if (this.state.authenticatedUser != null) {
            this.setState({ responseType: "danger", responseMsg: "You are already signed in." });
            return;
        }

        const body = {
            username: this.state.username,
            password: this.state.password
        }

        Api.post('/authenticate', body, (json) => {
            Api.authorizationToken = json.token;

            this.setState({ authenticatedUser: json });
            this.props.parentView.setState({ authenticatedUser: json });
            this.props.refresh(json);
        });
    }

    render() {
        if (this.state.authenticatedUser == null) {
            return (
                <div className="card login-container">
                    <div className="card-body">

                        <h5 className="card-title text-center mb-3">
                            Login
                        </h5>

                        <h6 className="card-subtitle mb-3 text-muted text-center">
                            Enter your credentials to continue
                        </h6>

                        {this.state.responseMsg &&
                            <Alert type={this.state.responseType}
                                msg={this.state.responseMsg} />
                        }

                        <div className="mb-3">
                            <label className="form-label">Username:</label>
                            <input type="text"
                                value={this.state.username}
                                onChange={this.handleUsername.bind(this)}
                                className="form-control form-control-sm"
                                onKeyDown={this.handleKeydown.bind(this)} />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Password:</label>
                            <input type="password"
                                value={this.state.password}
                                onChange={this.handlePassword.bind(this)}
                                className="form-control form-control-sm"
                                onKeyDown={this.handleKeydown.bind(this)} />
                        </div>

                        <div className="d-grid gap-2">
                            <button type="button"
                                className="btn btn-primary btn-sm"
                                onClick={this.login.bind(this)}>Login</button>
                        </div>
                    </div>
                </div>
            );
        }
    }
}