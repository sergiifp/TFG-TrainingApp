import { useContext, useState, useEffect } from "react";
import { AuthContext } from "@/authentication/AuthContext";
import API from "@/services/API";
import Link from "next/link";

export default function myClubs(){
    const authentication = useContext(AuthContext);
    const [clubs, setClubs] = useState([])

    const [id,setId] = useState(undefined);
    const [name,setName] = useState(undefined);
    const [password,setPassword] = useState(undefined);


    useEffect(() => {
        async function loadClubs(){
            let url = "";
            if (authentication.user.role == "Trainer"){
                url = API.completeUrl("/trainer/"+authentication.user.username+"/clubs",{});
            }
            else{
                url = API.completeUrl("/athlete/"+authentication.user.username+"/clubs",{});
            }
            let response = await API.get(url, authentication.user.token);
            setClubs(response.slice());
        }
        loadClubs();
    },[]);

    function getRenderedClubs(){
        const clubList = [];
        for (let i = 0; i < clubs.length; ++i){
            clubList.push(
                <Link style={{color:"inherit", display:"block", marginBottom:"10px", textDecoration:"none"}} href={"/clubs/"+clubs[i].id}>
                    <div style={{backgroundColor:"lightgrey", width:"50%", borderRadius:"5px", padding:"20px"}}>
                        <h2> {clubs[i].name} </h2>
                        <p> Created by {clubs[i].trainerCreator}</p>
                        <p> {clubs[i].enrolledAthletes} enrolled athletes</p>
                        <p> {clubs[i].customPlans} plans and {clubs[i].events} events</p>
                    </div>
                </Link>
            );
        }
        return clubList;
    }

    async function handleSubmitFormAth(event){
        event.preventDefault();
        const url = API.completeUrl("/athlete/"+authentication.user.username+"/clubs/"+id,{});
        let body = {"password":password};
        try{
            let response = await API.post(body, url, authentication.user.token);
        }
        catch(e){
            if (e.status == 409){
                alert("You are already member of this club or wrong credentials");
            }
        }
    }

    async function handleSubmitFormTra(event){
        event.preventDefault();
        let body ={
            "name": name,
            "password": password
        }
        let url = API.completeUrl("/clubs",{});
        try{
            await API.post(body, url, authentication.user.token);
        }
        catch(e){
            if (e.status == 409){
                alert("Try another name");
            }
        }
        window.location.reload();
    }


    return(
        <>
            <div className="userContent">
                <div className="UserProfileBox">
                    <h1 style={{textAlign:"center"}}>All my clubs</h1>
                    {authentication.user.role == "Athlete" &&
                        <form onSubmit={handleSubmitFormAth} style={{width:"50%"}}>
                            <label className="label50pw" > Club id:
                                <input id="clubId" type="text" value={id} required="true" className="UserProfileInput" onChange={e=>{setId(e.target.value);}}></input>
                            </label>
                            <label className="label50pw"> Password:
                                <input id="clubPassword" type="password" value={password} required="true" className="UserProfileInput" onChange={e=>{setPassword(e.target.value);}}></input>
                            </label>
                            <button type="submit" className="inputButton"> Enroll</button>
                        </form>}
                    {authentication.user.role == "Trainer" &&
                        <form onSubmit={handleSubmitFormTra} style={{width:"50%"}}>
                            <label className="label50pw" > Club name:
                                <input id="clubName" type="text" value={name} required="true" className="UserProfileInput" onChange={e=>{setName(e.target.value);}}></input>
                            </label>
                            <label className="label50pw"> Password:
                                <input id="clubPassword" type="password" value={password} required="true" className="UserProfileInput" onChange={e=>{setPassword(e.target.value);}}></input>
                            </label>
                            <button type="submit" className="inputButton"> Create</button>
                        </form>
                    }
                    {getRenderedClubs()}
                </div>
            </div>
        </>
    );
}