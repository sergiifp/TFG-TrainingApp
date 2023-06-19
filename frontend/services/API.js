
export default class API{
    static API_URL = "";

    static completeUrl(endpoint, params){
        let url = this.API_URL + endpoint;
        if(Object.keys(params).length != 0){
            url += "?";
            for (var key in params){
                url +=  (key + "=" + params[key] + "&"); 
            }
            url = url.substring(0, url.length-1);
        }
        return url;
    }

    static async post(body,url, token){
        const response = await fetch(url, {
            method: "POST",
            mode: "cors",
            body: JSON.stringify(body),           
            headers: {
                "Content-Type": "application/json",
                "Accept" : "application/json",
                'Access-Control-Allow-Origin': "*",
                "Authorization": "Bearer " + token,
            }
        });
        if (!response.ok) {
            throw new CustomError(response.status,`HTTP error ${response.status}`);
        }
        if (response.headers.get("content-type") == "application/json"){
            return response.json();
        }
    }

    static async get(url, token){
        const response = await fetch(url, {
            method: "GET",
            mode: "cors",        
            headers: {
                "Content-Type": "application/json",
                "Accept" : "application/json",
                'Access-Control-Allow-Origin': "*",
                "Authorization": "Bearer " + token,
            }
        });
        if (response.status === 404 && url.startsWith(this.API_URL+"/planprogress")){
            return null;
        }
        if (!response.ok) {
            throw new CustomError(response.status, `HTTP error ${response.status}`);
        }
        return response.json();
    }

    static async delete(url, token){
        const response = await fetch(url, {
            method: "DELETE",
            mode: "cors",    
            headers: {
                "Content-Type": "application/json",
                "Accept" : "application/json",
                'Access-Control-Allow-Origin': "*",
                "Authorization": "Bearer " + token,
            }
        });
        if (!response.ok) {
            throw new CustomError(response.status, `HTTP error ${response.status}`);
        }
    }

    static async put(url, body, token){
        const response = await fetch(url, {
            method: "PUT",
            mode: "cors",
            body: JSON.stringify(body),          
            headers: {
                "Content-Type": "application/json",
                "Accept" : "application/json",
                'Access-Control-Allow-Origin': "*",
                "Authorization": "Bearer " + token,
                }
        });
        if (!response.ok) {
            throw new CustomError(response.status, `HTTP error ${response.status}`);
        }
        if (response.headers.get("content-type") == "application/json"){
            return response.json();
        }
    }

}

class CustomError extends Error {
    constructor (status, errorMessage){
        super(errorMessage);
        this.status = status;
        this.name = "Error";
    }
}