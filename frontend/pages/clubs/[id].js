import BriefPlan from "@/components/BriefPlan";
import BriefEvent from "@/components/BriefEvent";
import { useRouter } from "next/router";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "@/authentication/AuthContext";
import API from "@/services/API";
import Link from "next/link";



export default function Club(){
    const router = useRouter();
    const authentication = useContext(AuthContext);
    const [club, setClub] = useState(undefined);
    const [trainingPlans, setTrainingPlans] = useState(undefined);
    const [events, setEvents] = useState(undefined);

    useEffect(() => {
        if (router.isReady){
          async function getInfo(){
            const id = router.query.id;
            try{
                let url = API.completeUrl("/clubs/"+id,{});
                setClub(await API.get(url, authentication.user.token));
                url = API.completeUrl("/clubs/"+id+"/trainingplans",{});
                setTrainingPlans(await API.get(url, authentication.user.token));
                url = API.completeUrl("/clubs/"+id+"/events",{});
                setEvents(await API.get(url, authentication.user.token));
            }
            catch(e){
                router.push("/clubs")
                return (<></>);
            }
          }
          getInfo();
        }
    },[router.isReady]);

    if (!router.isReady || events == undefined){
        return (<></>);
    }

    function printPlans(){
        const planList = [];
        for (let i = 0; i < trainingPlans.length; ++i){
            planList.push(
                <Link style={{color:"inherit"}} href={"/trainingplans/"+trainingPlans[i].id}>
                <div>
                    <BriefPlan planDTO={trainingPlans[i]}></BriefPlan>
                </div>
                </Link>
            );
        }
        return planList;
    }

    function printEvents(){
        const eventList = [];
        for (let i = 0; i < events.length; ++i){
            eventList.push(
                <div>
                    <BriefEvent eventDTO={events[i]}></BriefEvent>
                </div>
            );
        }
        return eventList;
    }
      
    return(
        <>
            <div className="userContent">
                <div className="UserProfileBox">
                    {club != undefined &&
                        <h1 style={{textAlign:"center", padding:"40px"}}> {club.name}</h1>}
                    <h2> Training plans</h2>
                    <div style={{overlow:"scroll", height:"500px", overflowY:"auto", marginBottom:"30px"}}>
                        {trainingPlans != undefined
                            && printPlans()}
                    </div>
                    <h2> Events</h2>
                    {club != undefined &&
                        <Link href={"/events?club="+club.id}>
                        <button className="inputButton">New event</button>
                        </Link>}
                    <div style={{overlow:"scroll", height:"500px", overflowY:"auto"}}>
                        {events != undefined
                            && printEvents()}
                    </div>
                </div>
            </div>
        </>
    );
}