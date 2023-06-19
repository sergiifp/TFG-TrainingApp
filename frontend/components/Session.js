import { useContext, useEffect, useState } from "react";
import Image from 'next/image';
import { AuthContext } from "@/authentication/AuthContext";
import API from "@/services/API";

export default function Session({weekSessionNumber,session, planProgress, results, loadProgress}){
    
    const [sessionInfo,setSessionInfo] = useState(session);
    const [kmResult,setKmResult] = useState(0);
    const [timeResult,setTimeResult] = useState("00:00:00");

    const authentication = useContext(AuthContext);

    useEffect(() => {
        if (results != undefined){
            setKmResult(results.finalKm);
            setTimeResult(results.finalTime);
        }
    },[results]);

    function getRunningSeries(session){
        const series = [];
        for (let j = 0; j < session.runningSeries.length; ++j){
            series.push(
                <>
                <p style={{display:"inline-block"}}>Serie {j}</p>
                <p style={{display:"inline-block", marginLeft:"20px"}}>({session.runningSeries[j].fc} fc)</p>
                <label></label>
                <p>({session.runningSeries[j].distance} + {session.runningSeries[j].returnDistance} m) x {session.runningSeries[j].repetitions} with {session.runningSeries[j].restTime} resting</p>
                </>
            );
        }
        return series;
    }

    async function createSessionResult(event, sessionId){
        event.preventDefault();
        const body = {"finalKm": kmResult,
                    "finalTime": timeResult};
        let url = API.completeUrl("/trainingsessions/"+sessionId+"/sessionresult",{});
        await API.post(body, url, authentication.user.token);
        loadProgress();
    }
    
    async function createSessionResultWeight(event, sessionId){
        event.preventDefault();
        const body = {"finalTime": timeResult};
        let url = API.completeUrl("/trainingsessions/"+sessionId+"/sessionresult",{});
        await API.post(body, url, authentication.user.token);
        loadProgress();
    }
    


    function handleChangeTimeResult(event){
        event.preventDefault();
        setTimeResult(event.target.value);
    }

    function handleChangeKmResult(event){
        event.preventDefault();
        setKmResult(event.target.value);
    }

    return (
        <>
            <h3 style={{display:"inline-block"}}>Session {weekSessionNumber}</h3>
                {sessionInfo.typeOfTraining == "runningSession" &&
                    <div style={{display:"inline-block", marginLeft:"20px"}}>
                    <label>
                        <Image src="/elevation_FILL0_wght400_GRAD0_opsz48.png" alt="positive slope" width={20} height={20} style={{verticalAlign:"text-bottom"}}/>
                        {sessionInfo.positiveSlope} m
                    </label>
                    <label>
                        <Image src="/elevation_FILL0_wght400_GRAD0_opsz48.png" alt="negative slope" style={{transform:"scaleX(-1)", verticalAlign:"text-bottom"}} width={20} height={20}/>
                        {sessionInfo.negativeSlope} m
                    </label>
                    <label>{sessionInfo.type}</label>
                    </div> 
                }
                {planProgress != undefined && <input type="checkbox" checked={results!=undefined}></input>}
                <p>{sessionInfo.training_type}</p>
                <p>Time: {sessionInfo.time}</p>
                <p>{sessionInfo.description}</p>
                {sessionInfo.typeOfTraining == "runningSession" &&
                    getRunningSeries(session)
                }
                {sessionInfo.typeOfTraining == "weightSession" &&
                    <p>Load: {sessionInfo.load} </p>
                }
                {sessionInfo.typeOfTraining == "runningSession" && planProgress != undefined && 
                    <form onSubmit={(e)=> {createSessionResult(e, sessionInfo.id);}} style={{marginTop:"10px"}}>
                        <label style={{display:"inline-block"}}> Time:
                            <input  type="time" step= "2" className="inputw100px" disabled={results!=undefined} value={timeResult} onChange={handleChangeTimeResult}></input>
                        </label>
                        <label style={{display:"inline-block"}}> Final Km:
                            <input type="number" className="inputw100px" disabled={results!=undefined} value={kmResult} onChange={handleChangeKmResult}></input>
                        </label>
                        {results==undefined && <button style={{height:"40px", width:"100px"}} type="submit"> Ok </button>}
                    </form>
                }
                {sessionInfo.typeOfTraining != "runningSession" && planProgress != undefined && 
                    <form onSubmit={(e)=> {createSessionResultWeight(e, sessionInfo.id);}} style={{marginTop:"10px"}}>
                        <label style={{display:"inline-block"}}> Time:
                            <input  type="time" step= "2" className="inputw100px" disabled={results!=undefined} value={timeResult} onChange={handleChangeTimeResult}></input>
                        </label>
                        {results==undefined && <button style={{height:"40px", width:"100px"}} type="submit"> Ok </button>}
                    </form>
                }
                    <br></br>

        </>
    );
}