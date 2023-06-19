import { useContext, useEffect, useState } from "react";
import Session from "./Session";
import { AuthContext } from "@/authentication/AuthContext";
import API from "@/services/API";

export default function EditablePlan({Plan}) {
    const [name,setName] = useState(Plan.name);
    const [typeOfActivity,setTypeOfActivity] = useState(Plan.typeOfActivity);
    const [objective,setObjective] = useState(Plan.objective);
    const [description,setDescription] = useState(Plan.description);
    const [trainingWeeks,setTrainingWeeks] =useState(Plan.trainingWeeks);
    const [selectedWeek, setSelectedWeek] = useState(0);
    const [trainer,setTrainer] = useState(Plan.trainer);
    const [progress,setProgress] = useState(undefined);
    const [comments, setComments] = useState(undefined);
    const [commentForm, setCommentForm] = useState("");
    const authentication = useContext(AuthContext);
    
    async function loadProgress(){
        try{
            let url = API.completeUrl("/athlete/"+authentication.user.username+"/trainingplans/"+Plan.id+"/planprogress",{});
            let response = await API.get(url, authentication.user.token);
            setProgress(response);
        }
        catch(e){
            setProgress(undefined);
        }
    }

    useEffect(() => {
        loadProgress();
    },[]);

    async function loadComments(){
        let url = API.completeUrl("/trainingweeks/"+trainingWeeks[selectedWeek].id+"/comments",{});
        let response = await API.get(url, authentication.user.token);
        setComments(response)
    }

    useEffect(() => {
        if (trainingWeeks[selectedWeek] != undefined){
            loadComments();
        }
    },[selectedWeek]);

    function sessionResults(id){
        for(let i = 0; i < progress.sessionResults.length; ++i){
            if (progress.sessionResults[i].trainingSessionId == id){
                return progress.sessionResults[i];

            }
        }
        return undefined;
    }

    function printSessions(){
        const sessions = [];
        for (let i = 0; i< trainingWeeks[selectedWeek].trainingSessions.length; ++i){
            const sessionC = trainingWeeks[selectedWeek].trainingSessions[i];

            sessions.push(
                <>
                <Session key={sessionC.id} weekSessionNumber={i} session={sessionC} planProgress={progress} results={progress!=undefined? sessionResults(sessionC.id): undefined} loadProgress={loadProgress}></Session>
                </>
            );
        }
        return sessions;
    }

    function changeToNextWeek(event){
        if (trainingWeeks.length-1 > selectedWeek){
            setSelectedWeek(selectedWeek+1);
        }
        event.preventDefault();
    }

    function changeToPreviousWeek(event){
        if (selectedWeek > 0){
            setSelectedWeek(selectedWeek-1);
        }
        event.preventDefault();
    }

    async function handleEnrollPlan(event){
        let url = API.completeUrl("/trainingplans/"+Plan.id+"/planprogress",{});
        await API.post({}, url, authentication.user.token);
        loadProgress();
        event.preventDefault();
    }

    async function handleUnrollPlan(event){
        let url = API.completeUrl("/athlete/"+ authentication.user.username +"/trainingplans/"+Plan.id+"/planprogress",{});
        await API.delete(url, authentication.user.token);
        loadProgress();
        event.preventDefault();
    }

    function printComments(){
        const commentsList = [];
        for (let i = 0; i < comments.length; ++i){
            commentsList.push(
                <div style={{marginTop:"10px"}}>
                    <div>
                        <b style={{display:"inline-block"}}>{comments[i].user}</b>
                        <p style={{display:"inline-block", marginLeft:"10px", color:"darkgray"}}>{new Date(comments[i].creationDate).toUTCString()}</p>
                    </div>
                    <p>{comments[i].message}</p>
                </div>
            );
        }
        return commentsList;
    }

    async function createComment(event){
        event.preventDefault();
        let url = API.completeUrl("/trainingweeks/"+trainingWeeks[selectedWeek].id+"/comments",{});
        let body = {"message": commentForm};
        let response = await API.post(body, url, authentication.user.token);
        loadComments();
        setCommentForm("");
    }

    return(
        <>
        <div className="userContent">
            <div className="UserProfileBox">
                <div style={{display:"block", overflowY:"auto", overflowX:"hidden"}}>
                    <h1 style={{textAlign:"center", padding:"40px"}}>{name}</h1>
                    <div style={{display: "block", overflow:"auto"}}>
                        <label className="labelTypeActivity"> {typeOfActivity}</label>
                        <label className="labelTypeObjective"> {objective} </label>
                        {progress == null && <button id="enrolled" className="inputButton" onClick={handleEnrollPlan}> Enroll</button>}
                        {progress != null && <button id="unrolled" className="inputButton" onClick={handleUnrollPlan}> Unroll</button>}
                    </div>
                    <div style={{display:"block"}}>
                        <div id="userInfo" style={{backgroundColor:"darkgray",width:"20%",height:"219px",verticalAlign:"top" ,display:"inline-block", marginRight:"10px", borderRadius:"5px", padding:"15px"}}>
                            <div>
                                <h3> {trainer.username}</h3>
                                <br></br>
                                <p>{trainer.age +" years old"}</p>
                                <p>Since date: {new Date(trainer.sinceDate).toLocaleDateString()}</p>
                                <p>{"Type of activity plans: " + trainer.typeOfActivityPlans}</p>
                            </div>
                        </div>
                        <div style={{display:"inline-block",width:"50%"}}>
                            <label> Description:
                                <p style={{backgroundColor:"lightgray", height:"200px", borderRadius:"5px", padding:"15px"}}> {description} </p>
                            </label>
                        </div>
                    </div>
                    {trainingWeeks[selectedWeek] != undefined && comments != undefined &&
                    <div id="footerNav">
                        <table>
                            <tbody>
                                <tr>
                                    <td style={{verticalAlign:"middle"}}>
                                        <button className="weekButton" onClick={changeToPreviousWeek}> {"<<"}</button>
                                    </td>
                                    <td style={{width:"100%"}}>
                                        <div id="weekContent" style={{backgroundColor:"lightgray", borderRadius:"5px", width:"100%", padding:"15px"}}>
                                        <h2>Week {selectedWeek}</h2>
                                        <p> {trainingWeeks[selectedWeek].description}</p>
                                        <br></br>
                                        {printSessions()}
                                        </div>
                                    </td>
                                    <td style={{verticalAlign:"middle"}}>
                                        <button className="weekButton" onClick={changeToNextWeek}> {">>"}</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td>
                                        <div style={{borderRadius:"5px", width:"100%", marginTop:"10px", padding:"15px"}}>
                                            <h2>Comments</h2>
                                                <textarea value={commentForm} onChange={(e)=>setCommentForm(e.target.value)} style={{width:"420px",height:"160px"}}></textarea>
                                                <button className="inputButton" style={{display:"inline-block", marginLeft:"5px", verticalAlign:"bottom"}} onClick={createComment}>Post comment</button>
                                            {printComments()}
                                        </div>
                                    </td>
                                    <td></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    }
                </div>
            </div>
        </div>
        </>
    );
}
