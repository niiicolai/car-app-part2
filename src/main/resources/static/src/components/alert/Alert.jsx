

function Alert(props) {
    return (
        <div className={`alert alert-${props.type}`} role="alert">
            {props.msg}
        </div>
    );
}