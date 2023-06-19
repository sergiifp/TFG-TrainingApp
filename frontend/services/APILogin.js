import API from "@/services/API.js"

export default class APILogin{
    
    static async tryToLogin(user, password){
        let url = API.completeUrl("/login", {});
        const response = await fetch(url, {
            method: "POST",
            mode: "cors",      
            headers: {
                "Content-Type": "application/json",
                "authorization": 'Basic ' + btoa(user + ":" + password),
                "Accept" : "application/json",
                'Access-Control-Allow-Origin': "*",
            },
        });
        if (!response.ok) {
            throw new Error(`HTTP error ${response.status}`);
        }
        const data = await response.json();
        return data;
    }

    static async tryToRegister(body,url){
        const response = await fetch(url, {
            method: "POST",
            mode: "cors",
            body: JSON.stringify(body),           
            headers: {
                "Content-Type": "application/json",
                "Accept" : "application/json",
                'Access-Control-Allow-Origin': "*",
            }
        });
        if (!response.ok) {
            throw new Error(`HTTP error ${response.status}`);
        }
        if (response.headers.get("content-type") == "application/json"){
            return response.json();
        }
    }
}