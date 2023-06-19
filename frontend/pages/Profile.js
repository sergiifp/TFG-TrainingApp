import AthleteInfoProfile from "@/components/AthleteInfoProfile";
import TrainerInfoProfile from "@/components/TrainerInfoProfile";
import { AuthContext } from "@/authentication/AuthContext";
import { useContext } from "react";

export default function Profile() {
    const authentication = useContext(AuthContext);
    return(
        <>  
            <div id="userContent" className="userContent">
                <div className="UserProfileBox"> 
                    {authentication.user.role=="Athlete" ? ( <AthleteInfoProfile></AthleteInfoProfile>):(<TrainerInfoProfile></TrainerInfoProfile>)}
                </div>
            </div>
        </>
    );

}