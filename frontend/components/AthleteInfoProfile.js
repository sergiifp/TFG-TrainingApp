import { useContext, useEffect, useState } from "react";
import { AuthContext } from "@/authentication/AuthContext";
import API from "@/services/API";
import { useRouter } from 'next/router';

export default function AthleteInfoProfile() {
    
    const [username,setUsername] = useState("");
    const [password,setPassword] = useState("");
    const [description,setDescription] = useState("");
    const [age,setAge] = useState(1);
    const [fcMax,setFcMax] = useState(undefined);
    const [weight,setWeight] = useState(undefined);
    const [height,setHeight] = useState(undefined);
    const date = new Date();
    const [staticInfo, setStaticInfo] = useState({
        "sessionsDone":0,
        "totalKm":0,
        "sinceDate": date,
    });
    const authentication = useContext(AuthContext);
    const router = useRouter();

    useEffect(() => {
        async function loadForm(){
            setUsername(authentication.user.username)
            let url ="";
            if (authentication.user.role == "Trainer"){
                url = API.completeUrl("/trainer/"+authentication.user.username,{});
            }
            else {
                url = API.completeUrl("/athlete/"+authentication.user.username,{});
            }
            let response = await API.get(url, authentication.user.token);
            setStaticInfo({
                "sessionsDone":response.sessionsDone,
                "totalKm":response.totalKm,
                "sinceDate": new Date(response.sinceDate)
            })
            setDescription(response.description);
            setAge(response.age);
            setFcMax(response.fcMax);
            setWeight(response.weight);
            setHeight(response.height);
        }
        loadForm();
    },[]);

    function handleSubmitForm(event){
        let data = {};
        if (password != ""){
            data = {
                "password": password,
                "description": description,
                "age": age,
                "fcMax": fcMax,
                "weight": weight,
                "height": height
            };
        }
        else{
            data = {
                "description":description,
                "age":age,
                "fcMax":fcMax,
                "weight":weight,
                "height":height
            };
        }
        let url ="";
        if (authentication.user.role == "Trainer"){
            url = API.completeUrl("/trainer/"+authentication.user.username,{});
        }
        else {
            url = API.completeUrl("/athlete/"+authentication.user.username,{});
        }
        let response = API.put(url, data, authentication.user.token);
        event.preventDefault();
    }

    function deleteAccount(event){
        event.preventDefault();
        if (confirm("Confirm that you want to delete the account")){
            let url = API.completeUrl("/user",{});
            let response = API.delete(url, authentication.user.token);
            authentication.logout();
        }
    }

    return(
        <>  
        <h1 style={{textAlign:"center", padding:"40px"}}>User profile</h1>
        <form onSubmit={handleSubmitForm}>
        <table className="UserProfileTable">
        <tbody>
            <tr>
                <td>
            <label> Username:
                <input id="username" type="text" value={username} className="UserProfileInput" onChange={(e)=>setUsername(e.target.value)} disabled={true}></input>
            </label>
            </td>
            </tr>
            <tr>
            <td>
            <label> Password:
                <input id="password" type="password" value={password} className="UserProfileInput" onChange={(e)=>setPassword(e.target.value)}></input>
            </label>
            </td>
            </tr>

            <tr>
                <td>
            <label> Age:
                <input id="age" type="number" value={age} className="UserProfileInput" onChange={(e)=>setAge(e.target.value)} min="1"></input>
            </label>
            </td>
            <td>
            <label> FCmax:
                <input id="fcMax" type="number" value={fcMax} className="UserProfileInput" onChange={(e)=>setFcMax(e.target.value)} min="1"></input>
            </label>
            </td>
            </tr>
            <tr>
                <td>
            <label> Weight:
                <input id="weight" type="number" value={weight} className="UserProfileInput" onChange={(e)=>setWeight(e.target.value)} min="1" ></input>
            </label>
            </td>
            <td>
            <label> Height:
                <input id="height" type="number" value={height} className="UserProfileInput" onChange={(e)=>setHeight(e.target.value)} min="1"></input> 
            </label>
            </td>
            </tr>
            <tr>
                <td>
            </td>
            </tr>
        </tbody>
        </table>
        <div style={{display: "inline-block", width:"49%", verticalAlign:"top"}}>
            <label> Description:
                <textarea id="description" type="text" value={description} onChange={(e)=>setDescription(e.target.value)} className="UserProfileTextarea"></textarea>
            </label>
        </div>
        <input type="submit" value="Submit" style={{float:"right", width:"200px"}} className="UserProfileInput"></input>
        </form>
        <p> Since date: {staticInfo.sinceDate.toUTCString()} </p>
        <p> Sessions done: {staticInfo.sessionsDone} sessions</p>
        <p> Total km since first login: {staticInfo.totalKm} km</p>
        <p style={{display:"block",marginTop:"20px"}}> Click <a href="#" onClick={deleteAccount}> here </a> to delete account </p> 
        </>
    );

}