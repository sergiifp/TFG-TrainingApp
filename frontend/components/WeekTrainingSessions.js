import SessionSeries from "./SessionSeries";

export default function WeekTrainingSessions({trainingSessions, saveTrainingSessions, selectedSession, setSelectedSession, selectedSessionEntity, setSelectedSessionEntity, selectedSerie, setSelectedSerie, selectedSerieEntity, setSelectedSerieEntity}) {

    const listSessionsName = trainingSessions.map((trainingSession, i) => <option key={i} value={i}>{"Session "+i}</option>);

    function saveSeries(series){
        trainingSessions[selectedSession].runningSeries = series;
        saveTrainingSessions(trainingSessions)
    }
    
    
    function handleChangeSelectedSession (event){
        setSelectedSerie(-1);
        setSelectedSerieEntity(undefined)
        setSelectedSession(event.target.value);
        setSelectedSessionEntity(trainingSessions[event.target.value])
        event.preventDefault();
    }

    function handleAddNewSessionButton(event){
        setSelectedSessionEntity({
            "typeOfTraining": "runningSession",
            "time": "00:00:00",
            "description": "",
            "type": "",
            "positiveSlope": 0,
            "negativeSlope": 0,
            "runningSeries":[]
        })
        setSelectedSession(-1)
        event.preventDefault();
    }
    
    function handleChangeSessionInput(event, id){
        if (id == "time") {trainingSessions[selectedSession].time = event.target.value;}
        else if (id == "description") {trainingSessions[selectedSession].description = event.target.value;}
        else if (id == "type") {trainingSessions[selectedSession].type = event.target.value;}
        else if (id == "positiveSlope") {trainingSessions[selectedSession].positiveSlope = event.target.value;}
        else if (id == "negativeSlope") {trainingSessions[selectedSession].negativeSlope = event.target.value;}
        else if (id == "typeOfTraining") {
            if (event.target.value == "runningSession") {
                trainingSessions[selectedSession] =  {
                    "typeOfTraining": "runningSession",
                    "time": "00:00:00",
                    "description": "",
                    "type": "",
                    "positiveSlope": 0,
                    "negativeSlope": 0,
                    "runningSeries":[]
                } 
            }
            else{
                trainingSessions[selectedSession] =  {
                    "typeOfTraining": "weightSession",
                    "load": "",
                    "time": "00:00:00",
                    "description": ""
                }
            }
        }
        else if(id == "load"){{trainingSessions[selectedSession].load = event.target.value;}}
        saveTrainingSessions(trainingSessions);
        event.preventDefault();
    }

    function handleChangeOfType(event){
        if (event.target.value == "runningSession") {
            setSelectedSessionEntity({
                "typeOfTraining": "runningSession",
                "time": "00:00:00",
                "description": "",
                "type": "",
                "positiveSlope": 0,
                "negativeSlope": 0,
                "runningSeries":[]
            }); 
        }
        else{
            setSelectedSessionEntity({
                "typeOfTraining": "weightSession",
                "load": "",
                "time": "00:00:00",
                "description": ""
            });
        }
    }

    function handleSaveSessionToList(event){
        event.preventDefault();
        var sessionTemp = trainingSessions.slice();
        if (selectedSession == -1){
            sessionTemp.push(selectedSessionEntity);
            setSelectedSession(sessionTemp.length-1)
        }
        else{
            sessionTemp[selectedSession] = selectedSessionEntity;
        }
        saveTrainingSessions(sessionTemp);
    }

    function handleDeleteSession(event){
        event.preventDefault();
        var sessionTemp = trainingSessions.slice();
        sessionTemp.splice(selectedSession,1);
        saveTrainingSessions(sessionTemp);
        if (sessionTemp.length > 0){
            setSelectedSession(0)
            setSelectedSessionEntity(sessionTemp[0])
        }
        else{
            setSelectedSession(-1)
            setSelectedSessionEntity(undefined)
        }

    }

    return(
        <div>
            <div style={{overflow:"auto"}}>
            <div style={{display:"inline-grid"}}>
                <select value={selectedSession} className="selectListTime" onChange={handleChangeSelectedSession} size={trainingSessions.length}>
                    <option disabled selected hidden value={-1}> </option>
                    {listSessionsName}
                </select>
                <button className="bttn30h5margin" onClick={handleAddNewSessionButton}> Add session</button>
                <input type="button" value="Delete" className="bttn30h5margin" disabled={selectedSession == undefined ||selectedSession == -1} onClick={handleDeleteSession}></input>
            </div>
            <form onSubmit={handleSaveSessionToList} style={{display:"inline-block", verticalAlign:"top"}}>
            <select className="bttn30h5margin" style={{display:"block"}} disabled={selectedSessionEntity == undefined} value={selectedSessionEntity == undefined ? "weight" : selectedSessionEntity.typeOfTraining} onChange={handleChangeOfType}>
                <option value="runningSession"> Running</option>
                <option value="weightSession"> Weight </option>
            </select>

            {selectedSessionEntity == undefined || selectedSessionEntity.typeOfTraining == "runningSession" ? (
                <>
                <div style={{display:"inline-block"}}>
                <label style={{display: "inline-block", margin:"5px"}}> Time:
                    <input id="runningTime" type="time" step= "2" required="true" className="inputw100px" value={selectedSessionEntity == undefined ? 0 : selectedSessionEntity.time} disabled={selectedSessionEntity == undefined } onChange={(e)=> setSelectedSessionEntity({...selectedSessionEntity,time:e.target.value})}></input>
                </label>
                
                <label style={{display: "inline-block", margin:"5px" }}> Positive slope:
                    <input id="positiveSlope" type="number" required="true" className="inputw100px" value={selectedSessionEntity == undefined ? 0 : selectedSessionEntity.positiveSlope} disabled={selectedSessionEntity == undefined } onChange={(e)=> setSelectedSessionEntity({...selectedSessionEntity,positiveSlope:e.target.value})}></input>
                </label>
                <label  style={{display: "inline-block", margin:"5px"}}> Negative slope:
                    <input id="negativeSlope" type="number" required="true" className="inputw100px" value={selectedSessionEntity == undefined ? 0 : selectedSessionEntity.negativeSlope} disabled={selectedSessionEntity == undefined } onChange={(e)=> setSelectedSessionEntity({...selectedSessionEntity,negativeSlope:e.target.value})}></input>
                </label>
                <label style={{display: "block", margin:"5px" }}> Type:
                    <input id="type" type="text" required="true" style={{display: "block"}}value={selectedSessionEntity == undefined ? "" : selectedSessionEntity.type} disabled={selectedSessionEntity == undefined} className="UserProfileInput" onChange={(e)=> setSelectedSessionEntity({...selectedSessionEntity,type:e.target.value})}></input>
                </label>
                </div>
                <label style={{display: "inline-block", margin:"5px", verticalAlign:"top"}}> Description:
                    <textarea id="runningDescription" style={{width:"400px", height:"120px", display:"block", resize:"none"}} type="text" required="true" value={selectedSessionEntity == undefined ? "" : selectedSessionEntity.description} disabled={selectedSessionEntity == undefined}  onChange={(e)=> setSelectedSessionEntity({...selectedSessionEntity,description:e.target.value})}></textarea>
                    <input type="submit" disabled={selectedSessionEntity == undefined} className="bttn30h5margin"></input>
                </label>
                <br></br>
                </>
            ):(
                <>
                <div>
                <label style={{display: "inline-block", margin:"5px"}}> Time:
                    <input id="weightTime" type="time" step= "2" required="true" className="inputw100px" value={selectedSessionEntity == undefined ? 0 : selectedSessionEntity.time} disabled={selectedSessionEntity == undefined } onChange={(e)=> setSelectedSessionEntity({...selectedSessionEntity,time:e.target.value})}></input>
                </label>
                <label style={{display: "inline-block", margin:"5px", verticalAlign:"top"}}> Description:
                    <textarea id="weightDescription" type="text" style={{width:"400px", height:"120px", display:"block", resize:"none"}} required="true" value={selectedSessionEntity == undefined ? "" : selectedSessionEntity.description} disabled={selectedSessionEntity == undefined } onChange={(e)=> setSelectedSessionEntity({...selectedSessionEntity,description:e.target.value})}></textarea>
                </label>
                <label style={{display: "inline-block", margin:"5px", verticalAlign:"top"}}> Load:
                    <textarea id="load" type="text" style={{width:"400px", height:"120px", display:"block", resize:"none"}} required="true" value={selectedSessionEntity == undefined ? 0 : selectedSessionEntity.load} onChange={(e)=> setSelectedSessionEntity({...selectedSessionEntity,load:e.target.value})} disabled={selectedSessionEntity == undefined}></textarea>
                </label>
                </div>
                <input type="submit" disabled={selectedSessionEntity == undefined} className="bttn30h5margin" style={{display:"block", float:"right"}}></input>
                </>
            )}
            
            </form>
            </div>
            {selectedSession != -1 && selectedSessionEntity.typeOfTraining == "runningSession" &&
              <SessionSeries series={trainingSessions[selectedSession].runningSeries} saveSeries={saveSeries} selectedSerie={selectedSerie} setSelectedSerie={setSelectedSerie} selectedSerieEntity={selectedSerieEntity} setSelectedSerieEntity={setSelectedSerieEntity}></SessionSeries>}
        </div>
    );

}