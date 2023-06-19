import Link from 'next/link';
import { useContext, useState } from "react";
import { useRouter } from 'next/router';
import { AuthContext } from "@/authentication/AuthContext";

export default function TopBar() {
    const [leftMenuIsOpened,setLeftMenuIsOpened] = useState(false);
    const router = useRouter();
    const authentication = useContext(AuthContext);

    function handleLeftMenuButton(event){
        if (leftMenuIsOpened){
            document.getElementById("leftMenu").style.width = "0px";
            setLeftMenuIsOpened(false);
        }
        else{
            document.getElementById("leftMenu").style.width = "250px";
            setLeftMenuIsOpened(true);
        }
    }

    function handleLogoutButton(event){
        event.preventDefault();
        authentication.logout();
    }

    return(
        <>
            <nav id="topBar" className='navBar'>
                <ul className="ulNavBar">
                    <li className="liNavBar">
                        <img src="/menu_FILL0_wght400_GRAD0_opsz48.png" width={30} height={30} style={{verticalAlign:"middle"}}  onClick={handleLeftMenuButton}></img>
                    </li>
                    <li className="liNavBar">TrainingApp</li>
                    <div style={{display:'inline', float:'right'}}>
                        <li className="liNavBar">
                            <Link href="/Profile">
                                <img src="/account_circle_FILL0_wght400_GRAD0_opsz48.png" width={30} height={30} style={{verticalAlign:"middle"}}></img>
                            </Link>
                        </li>
                        <li className="liNavBar">
                            <Link href="/Login" onClick={handleLogoutButton}>
                                <img src="/logout_FILL0_wght400_GRAD0_opsz48.png" width={30} height={30} style={{verticalAlign:"middle"}}></img>
                            </Link>
                        </li>
                    </div>
                </ul>
            </nav>
            <div id="leftMenu" className='leftMenu'>
                <div id="profileInfo" style={{height:"250px", textAlign:"center"}}>
                    <div className='profilePicture'>

                    </div>
                </div>
                <div style={{textAlign:"center"}}>
                {authentication.user.role == "Trainer" &&
                    <Link href="/trainingplans/new" style={{display:"block", textDecoration:"none", color:"inherit"}}>
                        <div className='leftMenuLabel'>
                            <p>New plan</p> 
                        </div>
                    </Link>
                    }
                    <Link href="/" style={{display:"block", textDecoration:"none", color:"inherit"}}>
                        <div className='leftMenuLabel' style={{backgroundColor:"#59cc8b"}}>
                            {authentication.user.role == "Trainer" &&<p> My plans </p>}
                            {authentication.user.role == "Athlete" &&<p> All plans </p>}
                        </div>
                    </Link>
                    <Link href="/clubs" style={{display:"block", textDecoration:"none", color:"inherit"}}>
                        <div className='leftMenuLabel' style={{backgroundColor:"#59cc8b"}}>
                            <p>My clubs</p> 
                        </div>
                    </Link>
                </div>
            </div>
        </>
    );
}