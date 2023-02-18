'use strict';

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
        return {
            'Content-Type': Api.contentType,
            'Authorization': Api.authorizationToken
        };
    }

    static async get(endpoint, callback) {
        const uri = `${this.fullpath()}${endpoint}`;
        const response = await fetch(uri, {
            method: 'GET',
            headers: this.headers()
        });
        callback(await response.json());
    };

    static async post(endpoint, data, callback) {
        const uri = `${this.fullpath()}${endpoint}`;
        const response = await fetch(uri, {
            method: 'POST',
            headers: this.headers(),
            body: JSON.stringify(data)
        });
        callback(await response.json());
    };

    static async patch(endpoint, data, callback) {
        const uri = `${this.fullpath()}${endpoint}`;
        const response = await fetch(uri, {
            method: 'PATCH',
            headers: this.headers(),
            body: JSON.stringify(data)
        });
        callback(await response.json());
    };

    static async delete(endpoint, callback) {
        const uri = `${this.fullpath()}${endpoint}`;
        const response = await fetch(uri, {
            method: 'DELETE',
            headers: this.headers()
        });
        callback(await response.json());
    };
}