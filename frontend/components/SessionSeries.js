export default function SessionSeries({series, saveSeries, selectedSerie, setSelectedSerie, selectedSerieEntity, setSelectedSerieEntity}){
    
    const listSeriesName = series.map((serie, i) => <option key={i} value={i}>{"Serie "+i}</option>);

    function handleChangeSelectedSerie(event) {
        setSelectedSerie(event.target.value);
        setSelectedSerieEntity(series[event.target.value])
        event.preventDefault();
    }

    function handleAddNewSerieButton(event) {
        setSelectedSerieEntity({
            "distance": 0,
            "returnDistance": 0,
            "restTime":"00:00:00",
            "repetitions":0,
            "fc": 0
        })
        setSelectedSerie(-1)
        event.preventDefault();
    }
    
    function handleChangeSerieInput(event,id) {
        if (id == "distance"){series[selectedSerie].distance = event.target.value;}
        else if (id == "returnDistance"){series[selectedSerie].returnDistance = event.target.value;}
        else if (id == "restTime"){series[selectedSerie].restTime = event.target.value;}
        else if (id == "repetitions"){series[selectedSerie].repetitions = event.target.value;}
        else if (id == "fc"){series[selectedSerie].fc = event.target.value;}
        saveSeries(series);
        event.preventDefault();
    }

    function handleSaveSerieToList(event){
        event.preventDefault();
        var seriesTemp = series.slice();
        if (selectedSerie == -1){
            seriesTemp.push(selectedSerieEntity);
            setSelectedSerie(seriesTemp.length-1);
        }
        else{
            seriesTemp[selectedSerie] = selectedSerieEntity;
        }
        saveSeries(seriesTemp);
    }

    function handleDeleteSerie(event){
        event.preventDefault();
        var serieTemp = series.slice();
        serieTemp.splice(selectedSerie,1)
        saveSeries(serieTemp)
        if (serieTemp.length > 0){
            setSelectedSerie(0)
            setSelectedSerieEntity(serieTemp[0])
        }
        else{
            setSelectedSerie(-1)
            setSelectedSerieEntity(undefined)
        }

    }

    return (
        <>  
            <div style={{display:"inline-grid", float:"left"}}>
                <select value={selectedSerie} className="selectListTime" onChange={handleChangeSelectedSerie} size={series.length}>
                    <option disabled selected hidden value={-1}> </option>
                    {listSeriesName}
                </select>
                <button className="bttn30h5margin"onClick={handleAddNewSerieButton}> Add serie</button>
                <input type="button" value="Delete" className="bttn30h5margin" disabled={selectedSerieEntity == undefined || selectedSerie == -1} onClick={handleDeleteSerie}></input>
            </div>
            <div style={{display:"inline-block"}}>
                <form onSubmit={handleSaveSerieToList}>
                    <div>
                    <label style={{display:"inline-block", margin:"0px 10px 10px 10px"}}> Distance:
                        <input styleid="distance" type="number" className="inputw100px" disabled={selectedSerieEntity == undefined} value={selectedSerieEntity == undefined ? 0 : selectedSerieEntity.distance} onChange={(e)=> setSelectedSerieEntity({...selectedSerieEntity, distance:e.target.value})}></input>
                    </label>
                    <label style={{display:"inline-block", margin:"0px 10px 10px 10px"}}> Return distance:
                        <input id="returnDistance" type="number" className="inputw100px" disabled={selectedSerieEntity == undefined} value={selectedSerieEntity == undefined ? 0 : selectedSerieEntity.returnDistance} onChange={(e)=> setSelectedSerieEntity({...selectedSerieEntity, returnDistance:e.target.value})}></input>
                    </label>
                    <label style={{display:"inline-block", margin:"0px 10px 10px 10px"}}> Rest time:
                        <input id="restTime" type="time" className="inputw100px" disabled={selectedSerieEntity == undefined} value={selectedSerieEntity == undefined ? 0 : selectedSerieEntity.restTime} onChange={(e)=> setSelectedSerieEntity({...selectedSerieEntity, restTime:e.target.value})}></input>
                    </label>
                    <br></br>
                    <label style={{display:"inline-block", margin:"0px 10px 10px 10px"}} > Repetitions:
                        <input id="repetitions" type="number" className="inputw100px" disabled={selectedSerieEntity == undefined} value={selectedSerieEntity == undefined ? 0 : selectedSerieEntity.repetitions} onChange={(e)=> setSelectedSerieEntity({...selectedSerieEntity, repetitions:e.target.value})}></input>
                    </label>
                    <label style={{display:"inline-block", margin:"0px 10px 10px 10px"}}> Average heart reate:
                        <input id="fc" type="number" className="inputw100px" disabled={selectedSerieEntity == undefined} value={selectedSerieEntity == undefined ? 0 : selectedSerieEntity.fc} onChange={(e)=> setSelectedSerieEntity({...selectedSerieEntity, fc:e.target.value})}></input>
                    </label>
                    </div>
                    <input type="submit" disabled={selectedSerieEntity == undefined} style={{height:"40px", display:"block", float:"right"}}></input>
                </form>
            </div>

        </>
    );

}