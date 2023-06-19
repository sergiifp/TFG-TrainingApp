import EditablePlan from "@/components/EditablePlan";
import NonEditablePlan from "@/components/NonEditablePlan";
import API from "@/services/API";
import { useRouter } from "next/router";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "@/authentication/AuthContext";
import Loading from "../Loading";


export default function Plan(){
    const router = useRouter();
    const authentication = useContext(AuthContext);
    const [planif, setPlanif] = useState(undefined);  

    useEffect(() => {
      if (router.isReady){
        async function getPlan(){
            
            let response = "";
            const id = router.query.id;
            try{
              let url = API.completeUrl("/trainingplans/"+id,{});
              setPlanif(await API.get(url,authentication.user.token))
            }
            catch({name,message}){
              if (message == "HTTP error 401"){
                router.push("/")
                return (<Loading></Loading>);
              }
            }
            
        }
        getPlan();
      }
    },[router.isReady]);

    
    return(
        <>
        {planif != undefined && planif.trainer.username == authentication.user.username &&
          <EditablePlan Plan={planif}></EditablePlan>}
        {planif != undefined && planif.trainer.username != authentication.user.username &&
          <NonEditablePlan Plan={planif}></NonEditablePlan>}
        </>
    );
}