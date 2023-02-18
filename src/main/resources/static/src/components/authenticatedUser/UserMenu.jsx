

class UserMenu extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            animationClass: "",
            animationTime: 2500
        };
    }

    refresh() {
        this.props.refresh();
        this.setState({ animationClass: "fa-spin" });

        const removeAnimation = () => {
            this.setState({animationClass: ""});
        }

        setTimeout(removeAnimation.bind(this), this.state.animationTime);
    }

    render() {
        return (
            <div className="mb-3 d-flex gap-3">
                <div>
                    <div className="mb-2">
                        <small className="text-muted text-uppercase">Welcome back</small>
                    </div>
                    <div className="text-capitalize h6">{this.props.authenticatedUser.username}</div>
                </div>
    
                <span className="vr"></span>
    
                <div>
                    <div className="mb-2">
                        <small className="text-muted text-uppercase">Roles</small>
                    </div>
                    <div className="d-flex gap-2">
                        {this.props.authenticatedUser.roles.map((role, index) =>
                            <span key={index} className="badge bg-secondary">{role} </span>
                        )}
                    </div>
                </div>
                
                <span className="vr"></span>
    
                <div>
                    <div className="mb-2">
                        <small className="text-muted text-uppercase">Actions</small>
                    </div>
                    <div className="d-flex gap-2">
                        <button type="button" 
                                className="btn btn-primary btn-xs" 
                                onClick={this.refresh.bind(this)}>
                                    <i className={`fa-solid fa-arrows-rotate ${this.state.animationClass}`}></i>
                                </button>
    
                        <button type="button" 
                                className="btn btn-danger btn-xs" 
                                onClick={this.props.logout}>
                                    <i className="fa-solid fa-arrow-right-from-bracket"></i>
                                </button>
                    </div>
                </div>
            </div>
        );
    }
}