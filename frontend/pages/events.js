import { useContext, useState, useEffect } from "react";
import { useRouter } from "next/router";
import { AuthContext } from "@/authentication/AuthContext";
import API from "@/services/API";


export default function newEvent(){
    const router = useRouter();
    const authentication = useContext(AuthContext);
    const [clubId, setClubId] = useState(undefined);
    const [title, setTitle] = useState(undefined);
    const [description, setDescription] = useState(undefined);
    const [eventDate, setEventDate] = useState(undefined);
    
    useEffect(() => {
        if (router.isReady){
            async function checkClubId(){
                if (router.query.club == undefined){
                    router.push("/");
                }
                else{
                    let url = "";
                    if (authentication.user.role == "Trainer"){
                        url = API.completeUrl("/trainer/"+authentication.user.username+"/clubs",{});
                    }
                    else{
                        url = API.completeUrl("/athlete/"+authentication.user.username+"/clubs",{});
                    }
                    let response = await API.get(url, authentication.user.token);
                    let found = false;
                    for (let i = 0; i < response.length; ++i){
                        if (response[i].id == router.query.club ){
                            found = true;
                        }
                    }
                    if (!found){
                        router.push("/");
                        return(<></>)
                    }
                }
                setClubId(router.query.club);
            }
            checkClubId()
        }
    },[router.isReady]);

    if (!router.isReady || router.isReady && clubId == undefined ){
        return(<></>)
    }

    async function handleSubmitForm(event){
        event.preventDefault();
        let body ={
            "title": title,
            "description": description,
            "eventDate": eventDate
        }
        let url = API.completeUrl("/clubs/"+clubId +"/events",{});
        await API.post(body,url, authentication.user.token);
        window.location.reload();
        event.preventDefault();
    }

    return (
        <>
        <div className="userContent">
            <div className="UserProfileBox">
                <h1 style={{textAlign:"center", padding:"40px"}}>New Event</h1>
                <form onSubmit={handleSubmitForm} style={{}}>
                    <label className="label50pw" > Title:
                        <input id="title" type="text" className="UserProfileInput" required ="true" onChange={e=>{setTitle(e.target.value);}}></input>
                    </label>
                    <label className="label50pw" >Event date:
                        <input id="date" type="date" className="UserProfileInput" required ="true" onChange={e=>{setEventDate(e.target.value);}}></input>
                    </label>
                    <label className="label50pw"> Description:
                        <textarea id="description" type="text" required ="true" onChange={e=>{setDescription(e.target.value);}} className="UserProfileTextarea"></textarea>
                    </label>
                    <div style={{display:"block"}}>
                    <button type="submit" style={{float:"right", height:"40px", width:"100px"}}> Create</button>
                    </div>
                </form>
            </div>
        </div>
        </>
    );
}