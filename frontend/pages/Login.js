import { useContext, useState } from "react";
import { AuthContext } from "@/authentication/AuthContext";
import API from "@/services/API";
import APILogin from "@/services/APILogin";


export default function Login() {
    const [username,setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [registrationRole, setRegistrationRole] = useState("Athlete");
    const [age, setAge] = useState(undefined);
    const [formType, setFormType] = useState("login");

    const authentication = useContext(AuthContext);
    async function handleSubmit(event){
        event.preventDefault();
        if (formType == "login"){
            try{
                await authentication.login(username,password);
            }
            catch(e){
                setPassword("");
            }
            
        }
        else{
            let url = API.completeUrl("/user",{});
            let body = {
                "username": username,
                "password": password,
                "age": age,
                "role": registrationRole,
            }
            try{
                let response = await APILogin.tryToRegister(body,url);
                setFormType("login");
            }
            catch(e){
                alert("A user with this username already exists")
            }
        }
    }
    
    function handleUsernameChange(event){
        setUsername(event.target.value);
    }

    function handlePasswordChange(event){
        setPassword(event.target.value);
    }

    function changeForm(event){
        event.preventDefault();
        if(formType == "login"){
            setFormType("register");
        }
        else{
            setFormType("login");
        }
    }

    return(
        <>
            <div style={{textAlign:"center", paddingTop:"70px"}}>
                <form className="loginForm" onSubmit={handleSubmit}>
                    {formType=="login" &&
                    <h1> User login</h1>}
                    {formType=="register" &&
                     <h1> User Registration</h1>
                    }
                    <label>
                        <input type="text" value={username} className="UserProfileInput" onChange={handleUsernameChange} placeholder="username" required="true"></input>
                    </label>
                    <label>
                        <input type="password" value={password} className="UserProfileInput" onChange={handlePasswordChange} placeholder="password" required="true"></input>
                    </label>
                    {formType == "register" &&
                        <>
                        <label>
                            <input type="number" value={age} className="UserProfileInput" required="true" min="0" max="120" onChange={(e)=>setAge(e.target.value)} placeholder="age"></input>
                        </label>
                        <label>
                        <select value={registrationRole} className="UserProfileInput" required="true" onChange={(e)=>setRegistrationRole(e.target.value)}>
                            <option value="Athlete">Athlete</option>
                            <option value="Trainer">Trainer</option>
                        </select>
                        </label>
                        </>
                    }
                    <input type="submit" className="inputButton" style={{margin:"20px"}}></input>
                    
                    {formType=="login" &&
                    <div>
                        <p> Click <a href="#" onClick={changeForm}> here </a> to create new account </p>
                    </div>}
                    {formType=="register" &&
                    <div>
                        <p> Click <a href="#" onClick={changeForm}> here </a> to login </p>
                    </div>}
                </form>
            </div>
        </>
    );
}
