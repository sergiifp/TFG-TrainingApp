import WeekTrainingSessions from "@/components/WeekTrainingSessions";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "@/authentication/AuthContext";
import API from "@/services/API";

export default function EditablePlan({Plan}) {
    const [name,setName] = useState(Plan.name);
    const [typeOfActivity,setTypeOfActivity] = useState(Plan.typeOfActivity);
    const [objective,setObjective] = useState(Plan.objective);
    const [description,setDescription] = useState(Plan.description);
    const [trainingWeeks,setTrainingWeeks] =useState(Plan.trainingWeeks);
    const [selectedSession, setSelectedSession] = useState(-1);
    const [selectedSerie, setSelectedSerie] = useState(0);
    const [selectedWeek, setSelectedWeek] = useState(-1);
    const [club, setClub] = useState(Plan.clubId != undefined? Plan.clubId:undefined);
    const [myClubs, setMyClubs] = useState(undefined);
    const [typeOfView,setTypeOfView] = useState("edit");
    const [progressList, setProgressList] = useState(undefined);
    const [commentForm, setCommentForm] = useState("");
    const [comments,setComments] = useState(undefined);
    const [selectedWeekComments, setSelectedWeekComments] = useState(-1);

    const authentication = useContext(AuthContext);
    
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
            setMyClubs(response.slice());
            if (club != undefined && Plan.id != undefined){
                url = API.completeUrl("/trainingplans/"+Plan.id+"/planprogress",{});
                response = await API.get(url, authentication.user.token);
                setProgressList(response);
            }
        }
        loadClubs();
    },[]);

    function getMyClubsList(){
        let options = [];
        options.push(<option key={0} value={undefined}></option>);
        if (myClubs != undefined){
            for (let i=0; i< myClubs.length; ++i){
                options.push(<option key={i+1} value={myClubs[i].id}> {myClubs[i].name}</option>);
            }
        }
        return options;
    }

    const listWeeksName = trainingWeeks.map((trainingWeek, i) => <option key={i} value={i}>{"Week "+i}</option>);

    function saveTrainingSessions(trainingSessionsToSave){
        let TrainingWeeksTemp = trainingWeeks.slice();
        TrainingWeeksTemp[selectedWeek].trainingSessions = trainingSessionsToSave;
        setTrainingWeeks(TrainingWeeksTemp);
    }

    function handleAddNewWeekButton(event){
        setSelectedWeekEntity({
            "description": "",
            "trainingSessions": []
        });
        setSelectedWeek(-1);
        setSelectedSession(-1);
        setSelectedSessionEntity(undefined)
        event.preventDefault();
    }
    function handleChangeSelectedWeek(event){
        setSelectedSession(-1);
        setSelectedSessionEntity(undefined)
        setSelectedSerie(-1);
        setSelectedSerieEntity(undefined)
        setSelectedWeek(event.target.value);
        setSelectedWeekEntity(trainingWeeks[event.target.value])
        event.preventDefault();
    }

    function handleChangeSelectedWeekDescription(event){
        const tempWeeks = trainingWeeks.map((trainingWeek,i) => { 
            if (i == selectedWeek){ 
                return(
                    {
                        ...trainingWeek,
                        description: event.target.value
                    }
                );
            }
            else{
                return trainingWeeks[i];
            }
        });
        setTrainingWeeks(tempWeeks);
        event.preventDefault();
    }

    async function handleCreateButtonClick(event){
        event.preventDefault();
        let body = {
            "name": name,
            "typeOfActivity": typeOfActivity,
            "objective": objective,
            "description": description,
            "trainingWeeks": trainingWeeks
        };
        let url;
        if (club != undefined && club != ""){
            url = API.completeUrl("/trainingplans",{"clubId":club});
        }
        else{
            url = API.completeUrl("/trainingplans",{});
        }
        await API.post(body, url, authentication.user.token);
        
    }

    async function handleSaveButtonClick(event){

        let body = {
            "id":Plan.id,
            "name": name,
            "typeOfActivity": typeOfActivity,
            "objective": objective,
            "description": description,
            "trainingWeeks": trainingWeeks
        };
        let url = API.completeUrl("/trainingplans",{});
        await API.put(url, body, authentication.user.token)
        event.preventDefault();
    }

    function printResults(){
        let result = [];

        for (let i = 0; i < trainingWeeks.length; ++i){
            result.push(<h2 style={{marginTop:"20px"}}> Week {i}</h2>)
            for (let j = 0; j < trainingWeeks[i].trainingSessions.length;++j){
                result.push(<h3> session {j}</h3>);
                for (let k = 0; k < progressList.length; ++k){
                    for (let l  = 0; l < progressList[k].sessionResults.length; ++l){
                        if (progressList[k].sessionResults[l].trainingSessionId == trainingWeeks[i].trainingSessions[j].id){
                            let part1 = "Athlete with name "+ progressList[k].username + " complete this session with time " + progressList[k].sessionResults[l].finalTime; 
                            if (progressList[k].sessionResults[l].finalKm != null){
                                let part2 = " but ended up running " + progressList[k].sessionResults[l].finalKm +" km";
                                part1 = part1 + part2;
                            } 
                            result.push(<p>{part1}</p>);
                        }
                    }
                }
            }
        }
        return result;
    }

    const [selectedWeekEntity, setSelectedWeekEntity] = useState(undefined)
    
    function handleSaveWeekToList(event){
        event.preventDefault();
        var temp = trainingWeeks.slice();
        if (selectedWeek == -1){
            temp.push(selectedWeekEntity);
            setSelectedWeek(temp.length-1)
        }
        else{
            temp[selectedWeek] = selectedWeekEntity;
        }
        setTrainingWeeks(temp);
    }

    function handleDeleteWeek(event){
        event.preventDefault();
        var temp = trainingWeeks.slice();
        temp.splice(selectedWeek,1)
        setTrainingWeeks(temp)
        if (temp.length > 0){
            setSelectedWeek(0)
            setSelectedWeekEntity(temp[0])
        }
        else{
            setSelectedWeek(-1)
            setSelectedWeekEntity(undefined)
        }
        
    }

    async function loadComments(){
        let url = API.completeUrl("/trainingweeks/"+trainingWeeks[selectedWeekComments].id+"/comments",{});
        let response = await API.get(url, authentication.user.token);
        setComments(response)
    }

    async function createComment(event){
        event.preventDefault();
        let url = API.completeUrl("/trainingweeks/"+trainingWeeks[selectedWeekComments].id+"/comments",{});
        let body = {"message": commentForm};
        let response = await API.post(body, url, authentication.user.token);
        loadComments();
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

    function weekOptionList(){
        let options = [];
        for (let i = 0; i < trainingWeeks.length; ++i){
            options.push(<option value={i}> week {i}</option>)
        }
        return options;
    }

    useEffect(() => {
        if (selectedWeekComments != -1){
            loadComments();
        }
    },[selectedWeekComments]);

    const [selectedSessionEntity, setSelectedSessionEntity] = useState(undefined)
    const [selectedSerieEntity, setSelectedSerieEntity] = useState(undefined)

    return(
        <>
        <div className="userContent">
            <div className="UserProfileBox">
                {Plan.id != undefined &&
                    <select id="typeOfView" className="inputButton" onChange={(e)=>setTypeOfView(e.target.value)}>
                        <option value="edit">edit</option>
                        {club != undefined && <option value="results">results</option>}
                        <option value="comments">comments</option>
                    </select>
                }
                {typeOfView == "comments" &&
                     <select value={selectedWeekComments} className="inputButton" style={{marginLeft:"5px"}} onChange={(e)=> setSelectedWeekComments(e.target.value)} disabled={trainingWeeks.length == 0}>
                        <option disabled selected hidden value={-1}> </option>
                        {weekOptionList()}
                    </select>
                }
                {Plan.id != undefined &&
                    <h1 style={{textAlign:"center", padding:"40px"}}>Edit plan</h1>
                }
                {Plan.id == undefined &&
                    <h1 style={{textAlign:"center", padding:"40px"}}>New plan</h1>
                }
                {typeOfView == "edit" &&
                    <>
                   
                    <div style={{display:"block", overflowY:"auto", overflowX:"hidden"}}>
                    <form id="generalParamsForm">
                    <div style={{display: "block"}}>
                        <label className="label50pw" > Name:
                            <input id="name" type="text" required="true" value={name} className="UserProfileInput" onChange={(e)=>setName(e.target.value)}></input>
                        </label>
                        <label className="label100pxw"> Type of activity:
                            <select id="typeOfActivity" value={typeOfActivity} required="true" className="UserProfileInput" onChange={(e)=>setTypeOfActivity(e.target.value)}> 
                                <option value = "road"> Road </option>
                                <option value = "trail"> Trail </option>
                            </select>
                        </label>

                        <label className="label100pxw"> Club:
                            <select id="clubId" value={club} className="UserProfileInput" onChange={(e)=>setClub(e.target.value)} disabled={Plan.id != undefined}>
                            {getMyClubsList()}
                            </select>
                        </label>
                        
                    </div>
                    <label> Objective:
                        <input id="objective" type="text" required="true" value={objective} className="UserProfileInput" onChange={(e)=>setObjective(e.target.value)}></input>
                    </label>
                    <label> Description:
                        <textarea id="description" type="text" required="true" value={description} onChange={(e)=>setDescription(e.target.value)} className="UserProfileTextarea"></textarea>
                    </label>
                    </form>
                    <label style={{fontSize:"20px"}} > Weeks</label>
                    <br></br>
                    <div style={{display:"inline-grid"}}>
                        <select className="selectListTime" value={selectedWeek} data-dropup-auto="false" onChange={handleChangeSelectedWeek} size={trainingWeeks.length}>
                            <option disabled selected hidden value={-1}> </option>
                            {listWeeksName}
                        </select>
                        <button onClick={handleAddNewWeekButton} className="bttn30h5margin"> Add week</button>
                        <input type="button" value="Delete" className="bttn30h5margin" disabled={selectedWeek == undefined ||selectedWeek == -1} onClick={handleDeleteWeek}></input>
                    </div>
                    <form onSubmit={handleSaveWeekToList} style={{display:"inline-block", verticalAlign:"top"}}>
                        <label  style={{display:"inline-grid", marginLeft:"10px", marginRight:"40px"}}> Week description:
                            <textarea id="descriptionWeek" type="text" className="textArea300w166h" required="true" disabled={selectedWeekEntity == undefined} value={selectedWeekEntity == undefined? "":selectedWeekEntity.description} onChange={(e)=>setSelectedWeekEntity({...selectedWeekEntity,description:e.target.value})}></textarea>
                            <input type="submit" disabled={selectedWeekEntity == undefined} className="bttn30h5margin"></input>
                        </label>
                    </form>
                    {selectedWeek != -1 &&
                        <WeekTrainingSessions trainingSessions={trainingWeeks[selectedWeek].trainingSessions} saveTrainingSessions= {saveTrainingSessions} selectedSession={selectedSession} 
                        setSelectedSession={setSelectedSession} selectedSessionEntity={selectedSessionEntity} setSelectedSessionEntity={setSelectedSessionEntity} selectedSerie={selectedSerie} 
                        setSelectedSerie={setSelectedSerie} selectedSerieEntity={selectedSerieEntity} setSelectedSerieEntity={setSelectedSerieEntity}></WeekTrainingSessions>}
                    </div>
                    {Plan.id == undefined ? 
                    <button type="submit" form="generalParamsForm" style={{float:"right", height:"40px", width:"100px"}} onClick={handleCreateButtonClick}> Create </button>
                    : <button style={{float:"right", height:"40px", width:"100px"}} onClick={handleSaveButtonClick}> Save </button>
                    }
                    </>
                }
                {typeOfView=="results" &&
                    printResults()
                }
                {typeOfView=="comments" &&
                    <>
                        <h2>Comments</h2>
                    {selectedWeekComments != -1 && 
                        <>
                        <textarea value={commentForm} onChange={(e)=>setCommentForm(e.target.value)} style={{width:"420px",height:"160px"}}></textarea>
                        <button className="inputButton" style={{display:"inline-block", marginLeft:"5px", verticalAlign:"bottom"}} onClick={createComment}>Post comment</button>
                        {comments != undefined && printComments()}
                        </>
                    }
                    </>
                }
            </div>
        </div>
        </>
    );
}
