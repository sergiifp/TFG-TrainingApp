import { useContext, useEffect, useState } from "react";
import { useRouter } from 'next/router';
import { AuthContext } from "@/authentication/AuthContext";
import API from "@/services/API";

export default function trainer({}){
    const [username, setUsername] = useState(undefined);
    const [description,setDescription] = useState(undefined);
    const [age,setAge] = useState(1);
    const [typeOfActivityPlans,setTypeOfActivityPlans] = useState([]);
    const [languages,setLanguages] = useState([]);
    const router = useRouter();
    const authentication = useContext(AuthContext);

    useEffect(() => {
        async function loadInfo(){
            if (router.isReady){
                setUsername(router.query.id);
                let url ="";
                url = API.completeUrl("/trainer/"+router.query.id,{});
                let response = await API.get(url, authentication.user.token);
                setDescription(response.description);
                setAge(response.age);
                setTypeOfActivityPlans([response.typeOfActivityPlans]);
                setLanguages(response.languages);
            }
        }
        loadInfo();
    },[router.isReady]);

    return(
        <>
        <div id="userContent" className="userContent">
            <div className="UserProfileBox" style={{width:"40%"}}> 
                <h1 style={{textAlign:"center", paddingBottom:"60px"}}>{username}</h1>
                    <div style={{marginBottom:"10px"}}>
                    <label htmlFor="Age"> Age: </label>
                    <p id="Age" style={{display:"inline-block"}}>{age}</p>
                    </div>
                    <div style={{marginBottom:"10px"}}>
                    <label htmlFor="typeOfPlans"> Type of activity: </label>
                    <p id="typeOfPlans" style={{display:"inline-block"}}>{typeOfActivityPlans}</p>
                    </div>
                    <div style={{marginBottom:"10px"}}>
                    <label> Languages:
                    <select id="language" value={languages} className="UserProfileSelect" multiple={true} disabled={true}> 
                        <option value = "ca"> ca </option>
                        <option value = "esp"> esp </option>
                        <option value = "eng"> eng </option>
                    </select>
                    </label>
                    </div>
                    <div style={{display: "block", width:"100%", verticalAlign:"top"}}>
                    <label> Description:
                        <textarea id="description" type="text" value={description} disabled={true} className="UserProfileTextarea"></textarea>
                    </label>
                    </div>  
            </div>
        </div>
        </>
    );

}