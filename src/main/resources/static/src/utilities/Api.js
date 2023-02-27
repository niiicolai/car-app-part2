
class Api {
    static host = window.location.host;
    static protocol = window.location.protocol;
    static api = "/api/v1";

    static contentType = "application/json";
    static authorizationToken = "";

    static fullpath() {
        return `${this.protocol}//${this.host}${this.api}`;
    }

    static headers() {
        const authorization = Api.authorizationToken != "" ? `Bearer ${Api.authorizationToken}` : "";
        return {
            'Content-Type': Api.contentType,
            'Authorization': authorization
        };
    }

    static async get(endpoint, callback, errorCallback) {
        const uri = `${this.fullpath()}${endpoint}`;
        const response = await fetch(uri, {
            method: 'GET',
            headers: this.headers()
        });
        if (!response.ok) {
            errorCallback(await response.json())
        } else {
            callback(await response.json());
            errorCallback({});
        }
    };

    static post(endpoint, data, callback, errorCallback) {
        const uri = `${this.fullpath()}${endpoint}`;
        fetch(uri, {
            method: 'POST',
            headers: this.headers(),
            body: JSON.stringify(data)
        })
        .then((response) => {
            return response.json()
        })
        .then((json) => {
            if (json.error) 
                errorCallback(json)
            else
                callback(json);
        })
        .catch((error) => {
            console.log("error:" + error)
            errorCallback(error)
        });
    };

    static async patch(endpoint, data, callback, errorCallback) {
        const uri = `${this.fullpath()}${endpoint}`;
        const response = await fetch(uri, {
            method: 'PATCH',
            headers: this.headers(),
            body: JSON.stringify(data)
        });
        

        if (!response.ok) {
            errorCallback(await response.json())
        } else {
            callback(await response.json());
            errorCallback({});
        }
    };

    static async delete(endpoint, callback, errorCallback) {
        const uri = `${this.fullpath()}${endpoint}`;
        const response = await fetch(uri, {
            method: 'DELETE',
            headers: this.headers()
        });

        if (!response.ok) {
            errorCallback(await response.json())
        } else {
            callback(await response.json());
            errorCallback({});
        }
    };
}