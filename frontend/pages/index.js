import Head from 'next/head'
import BriefPlan from '@/components/BriefPlan.js'
import { useContext, useEffect, useState } from "react";

import Link from 'next/link';
import API from '@/services/API';
import { AuthContext } from "@/authentication/AuthContext";

export default function Home() {
  const authentication = useContext(AuthContext);
  const [plans,setPlans] = useState([]);
  const [currentPage,setCurrentPage] = useState(1);
  const plansForPage = 5;
  const plansToPrint = plans.slice(currentPage*plansForPage-plansForPage,currentPage*plansForPage);
  const numberOfPages = Math.ceil(plans.length / plansForPage);
  const [filter, setFilter] = useState("public");
  const [likedplans, setLikedPlans] = useState(undefined);

  useEffect(() => {
    async function loadPlans(){
      let response = "";
      let url = "";
      if (authentication.user.role == "Trainer"){
        url = API.completeUrl("/trainer/"+authentication.user.username+ "/trainingplans",{});
        response = await API.get(url, authentication.user.token);
      }
      else {
        url = API.completeUrl("/trainingplans",{});
        response = await API.get(url, authentication.user.token);
      }
      setPlans(response);

      if (authentication.user.role == "Athlete"){
        url = API.completeUrl("/athlete/"+ authentication.user.username + "/likedplans",{});
        response = await API.get(url, authentication.user.token);
        setLikedPlans(response);
      }

    }
    loadPlans();
  },[]);

  async function handleChangeFilter(event){
    event.preventDefault();
    setFilter(event.target.value);
    let url, response;
    if (event.target.value == "public"){
      url = API.completeUrl("/trainingplans",{});
    }
    else{
      url = API.completeUrl("/athlete/"+authentication.user.username+"/trainingplans",{});
    }
    response = await API.get(url, authentication.user.token);
    setPlans(response);
    
  }

  function handlePreviousPage(){
    if(currentPage > 1){
      setCurrentPage(currentPage-1);
    }
  }

  function handleNextPage(){
    if(currentPage < numberOfPages){
      setCurrentPage(currentPage+1);
    }
  }

  function obtainNumbers(){
    const liList=[]
    liList.push(<li key={0} style={{display:"inline-block"}}> <a href="#" className="paginationNumber" onClick={handlePreviousPage}> {"<<"}</a></li>);
    for (let i=0; i < numberOfPages; ++i){
      liList.push(<li key={i+1} style={{display:"inline-block"}}> <a href="#" className={"paginationNumber"+((i+1) == currentPage? "Active":"")} onClick={() => setCurrentPage(i+1)}> {i}</a></li>);
    }
    liList.push(<li key={liList.length} style={{display:"inline-block"}}> <a href="#" className="paginationNumber" onClick={handleNextPage}> {">>"}</a></li>);
    return liList;
  }

  async function addLikes(planDTO){
    try{
        let url = API.completeUrl("/trainingplans/"+ planDTO.id + "/likes",{});
        let response = await API.post({}, url, authentication.user.token);
        setLikedPlans([...likedplans, planDTO])
        const position = plans.map(p=>p.id).indexOf(planDTO.id)
        plans[position].numberOfLikes += 1;
        setPlans(plans);
    }
    catch(e){
    }
  }
  async function removeLikes(planDTO, index){
    try{
        let url = API.completeUrl("/trainingplans/"+ planDTO.id + "/likes",{});
        let response = await API.delete(url, authentication.user.token);
        likedplans.splice(index,1)
        setLikedPlans(likedplans.slice())
        const position = plans.map(p=>p.id).indexOf(planDTO.id)
        plans[position].numberOfLikes -= 1;
        setPlans(plans);
    }
    catch(e){
    }
  }

  return (
    <>
      <Head>
        <title>TrainingApp</title>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
      </Head>
      <main>
        <div className="userContent">
          <div className='listPlanBar'>
            {authentication.user.role == "Athlete" &&
              <select value={filter} onChange={e=>handleChangeFilter(e)} className="CreateButton">
                <option value="public"> public </option>
                <option value="enrolled"> enrolled </option>
              </select>
            }
            {authentication.user.role != "Athlete" &&
              <Link href="/trainingplans/new">
                <button className='CreateButton'> New plan</button>
              </Link>
            } 
          </div>
          <div className="UserProfileBox">
              {plansToPrint.map((plan) => <Link style={{color:"inherit"}} href={"/trainingplans/"+plan.id}><BriefPlan planDTO={plan} likedPlans={likedplans} addLikes={addLikes} removeLikes={removeLikes}setLikedPlans={setLikedPlans}> </BriefPlan></Link>)}
              <div style={{textAlign:"center"}}>
              <ul>
                {obtainNumbers()}
              </ul> 
              </div>
          </div>
        </div>
        
      </main>
    </>
  )
}
