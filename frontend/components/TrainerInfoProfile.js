import { useContext, useEffect, useState } from "react";
import { useRouter } from 'next/router';
import { AuthContext } from "@/authentication/AuthContext";
import API from "@/services/API";

export default function TrainerInfoProfile() {
    const [username,setUsername] = useState("");
    const [password,setPassword] = useState("");
    const [description,setDescription] = useState(undefined);
    const [age,setAge] = useState(1);
    const [typeOfActivityPlans,setTypeOfActivityPlans] = useState("");
    const [languages,setLanguages] = useState([]);
    const [sinceDate, setSinceDate] = useState("");
    const router = useRouter();

    const authentication = useContext(AuthContext);

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
            setDescription(response.description);
            setAge(response.age);
            setTypeOfActivityPlans(response.typeOfActivityPlans);
            setLanguages(response.languages);
            setSinceDate(response.sinceDate);
        }
        loadForm();
    },[]);

    function handleSubmitForm(event){
        let data = {};
        if (password != ""){
            data = {
                "username": username,
                "password": password,
                "description": description,
                "age": age,
                "typeOfActivityPlans": typeOfActivityPlans,
                "languages": languages,
            };
        }
        else{
            data = {
                "username":username,
                "description":description,
                "age":age,
                "typeOfActivityPlans": typeOfActivityPlans,
                "languages": languages,
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

    function handleChangeLanguage(event){
        var newLanguages = [];
        var options = event.target.options;
        for (var i = 0; i < options.length;++i){
            if (options[i].selected){
                newLanguages.push(options[i].value);
            }
        }
        setLanguages(newLanguages);
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
        
        <h1 style={{textAlign:"center", paddingBottom:"60px"}}>User profile</h1>
        <form  onSubmit={handleSubmitForm}>
        <table className="UserProfileTable">
        <tbody>
            <tr>
                <td>
            <label> Username:
                <input id="username" type="text" value={username} className="UserProfileInput" onChange={(e)=>setUsername(e.target.value)} disabled="true"></input>
            </label>
            </td>
            </tr>
            <tr>
            <td>
            <label> Password:
                <input id="password" type="password" value={password}  className="UserProfileInput" onChange={(e)=>setPassword(e.target.value)}></input>
            </label>
            </td>
            </tr>

            <tr>
                <td>
            <label> Age:
                <input id="age" type="number" value={age} min="1" className="UserProfileInput" onChange={(e)=>setAge(e.target.value)}></input>
            </label>
            </td>
            <td>
            <label> Type of activity:
            <select id="typeOfActivityPlans" value={typeOfActivityPlans} className="UserProfileInput" onChange={(e)=>setTypeOfActivityPlans(e.target.value)}> 
                    <option value = "road"> road </option>
                    <option value = "trail"> trail </option>
                </select>
            </label>
            </td>
            </tr>
            <tr>
                <td>
                <label> Languages:
                <select id="language" value={languages} className="UserProfileSelect" onChange={handleChangeLanguage} multiple={true}> 
                    <option value = "ca"> ca </option>
                    <option value = "esp"> esp </option>
                    <option value = "eng"> eng </option>
                </select>
            </label>
            </td>
            </tr>
            <tr>
                <td>
            <input type="submit" value="Submit" className="UserProfileInput"></input>
            </td>
            </tr>
        </tbody>
        </table>
        <div style={{display: "inline-block", width:"49%", verticalAlign:"top"}}>
            <label> Description:
                <textarea id="description" type="text" value={description} className="UserProfileTextarea" onChange={(e)=>setDescription(e.target.value)}></textarea>
            </label>
            </div>   
        </form> 
        <p style={{float:"right", display:"block"}}> Since date: {new Date(sinceDate).toUTCString()} </p>
        <br></br>
        <p style={{float:"right", display:"block"}}> Click <a href="#" onClick={deleteAccount}> here </a> to delete account </p> 
        </>
    );

}