import APILogin from "@/services/APILogin.js";
import { useEffect, useState } from "react";
import { useRouter } from 'next/router';
import { AuthContext } from "./AuthContext";
import Loading from "@/pages/Loading";
import API from "@/services/API";

export default function Authentication({children}){
    const [user, setUser] = useState(undefined);
    const [loading, setLoading] = useState(true);
    const router = useRouter();
    const protectedRoutesTrainer = ["/clubs/new", "/NewPlan"];

    function findTokenCookie(){
        let cookies = document.cookie.split(";");
        let i = 0;
        while (i < cookies.length){
            if (i > 0){
                cookies[i] = cookies[i].substring(1);
            }
            if (cookies[i].startsWith("token=")){
                return cookies[i].replace("token=","");
            }
            ++i;
        }
        return undefined;
    }

    useEffect(() => {
        async function checkStateCredentials(){
            let cookie = findTokenCookie();
            if (cookie != undefined){
                let url = API.completeUrl("/user/whoAmI",{});
                let response = await API.get(url, cookie);
                let newUser = {
                    "token": cookie,
                    "username": response.username,
                    "role": response.role
                }
                setUser(newUser);
                setLoading(false);
            }
            else{
                if (!router.asPath.startsWith("/Login")){
                    router.push("/Login");
                }
                setLoading(false);
            }
        }
        checkStateCredentials();
    },[]);

    async function login (username,password){
        let response =  await APILogin.tryToLogin(username,password);
        document.cookie = "token="+response.token+"; path=/";
        let newUserLogged = {
            "token": response.token,
            "username": username,
            "role": response.role
        };
        setUser(newUserLogged);
        router.push("/");
    }

    async function logout(){
        document.cookie = "token= ;expires = Thu, 01 Jan 1970 00:00:00 GMT";
        setUser(undefined);
        router.push("/Login");
    }

    if (loading || (user == undefined && !router.asPath.startsWith("/Login"))){
        return(
            <Loading></Loading>
        );
    }

    if (user != undefined && user.role == "Athlete" && protectedRoutesTrainer.indexOf(router.asPath) > -1){
        router.push("/");
        return(
            <Loading></Loading>
        );
    }
    
    return(
        <AuthContext.Provider value={{user, login, logout}}>
            {children}
        </AuthContext.Provider>
    );

}