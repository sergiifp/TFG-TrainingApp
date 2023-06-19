
import { AuthContext } from "@/authentication/AuthContext";
import { useContext } from 'react';
import Link from 'next/link';
export default function BriefPlan({planDTO, addLikes, removeLikes, likedPlans}) {
    const authentication = useContext(AuthContext);

    function checkId(plans){
        return plans.id == planDTO.id;
    }
    const indexElement = likedPlans != undefined? likedPlans.findIndex(checkId):undefined;

    async function removeLike(event){
        event.preventDefault();
        try{
            removeLikes(planDTO,indexElement);
        }
        catch(e){
        }
    }
    
    async function addLike(event){
        event.preventDefault();
        try{
            addLikes(planDTO);
        }
        catch(e){
        }
    }

    return(
        <div className="planBox">
            <Link style={{color:"inherit"}} href={"/trainers/"+planDTO.username}>
            <div id="trainerInfo" className="trainerInfoPlan">
                <h2>{planDTO.username}</h2>
            </div>
            </Link>
            <div id="planInfo" className="planInfo">
            <h2>{planDTO.name}</h2>
            <p>{planDTO.typeOfActivity}</p>
            <p>Objective: {planDTO.objective}</p>
            <p>Duration: {planDTO.duration} weeks</p>
            <p>Number of sessions: {planDTO.sessionsPerWeek} sessions/week</p>
            <p>Likes: {planDTO.numberOfLikes}</p> 
            {likedPlans != undefined && indexElement != -1 &&
                <>
                <br></br>
                <img src="/thumb_up_FILL1_wght400_GRAD0_opsz48.png" alt="positive slope" width={20} height={20} onClick={removeLike} />
                </>
            }
            {likedPlans != undefined && indexElement == -1 &&
                <>
                <br></br>
                <img src="/thumb_up_FILL0_wght400_GRAD0_opsz48.png" alt="positive slope" width={20} height={20} onClick={addLike}/>
                </>
            }   
            </div>
        </div>
    );
}
