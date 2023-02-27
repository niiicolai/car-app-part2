

function CarMotorRegister(props) {
    return (
        <div>
            <div>
                <div><small>Registration number:</small></div>
                <div><strong>{props.motorRegister.registration_number}</strong></div>
            </div>
            <div>
                <div><small>Status:</small></div>
                <div><strong>{props.motorRegister.status}</strong></div>
            </div>
            <div>
                <div><small>Status date:</small></div>
                <div><strong>{props.motorRegister.status_date}</strong></div>
            </div>
            <div>
                <div><small>type:</small></div>
                <div><strong>{props.motorRegister.type}</strong></div>
            </div>
            <div>
                <div><small>use:</small></div>
                <div><strong>{props.motorRegister.use}</strong></div>
            </div>
            <div>
                <div><small>First registration:</small></div>
                <div><strong>{props.motorRegister.first_registration}</strong></div>
            </div>
            <div>
                <div><small>VIN:</small></div>
                <div><strong>{props.motorRegister.vin}</strong></div>
            </div>
            <div>
                <div><small>Doors:</small></div>
                <div><strong>{props.motorRegister.doors}</strong></div>
            </div>
            <div>
                <div><small>Make:</small></div>
                <div><strong>{props.motorRegister.make}</strong></div>
            </div>
            <div>
                <div><small>Model:</small></div>
                <div><strong>{props.motorRegister.model}</strong></div>
            </div>
            <div>
                <div><small>Variant:</small></div>
                <div><strong>{props.motorRegister.variant}</strong></div>
            </div>
            <div>
                <div><small>Model type:</small></div>
                <div><strong>{props.motorRegister.model_type}</strong></div>
            </div>
            <div>
                <div><small>Color:</small></div>
                <div><strong>{props.motorRegister.color}</strong></div>
            </div>
            <div>
                <div><small>Fuel type:</small></div>
                <div><strong>{props.motorRegister.fuel_type}</strong></div>
            </div>
            <div>
                <div><small>Is leasing:</small></div>
                <div><strong>{props.motorRegister.is_leasing ? 'YES' : 'NO'}</strong></div>
            </div>
            <div>
                <div><small>Leasing from:</small></div>
                <div><strong>{props.motorRegister.leasing_from}</strong></div>
            </div>
            <div>
                <div><small>Leasing to:</small></div>
                <div><strong>{props.motorRegister.leasing_to}</strong></div>
            </div>
        </div>
    );
}