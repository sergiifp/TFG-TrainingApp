export default function BriefEvent({eventDTO}) {
    var date = new Date(eventDTO.eventDate).toLocaleDateString();
    var CreatedDate = new Date(eventDTO.sinceDate);

    return (
        <div className="planBox">
            <div id="trainerInfo" className="trainerInfoPlan">
                <h2>{eventDTO.user.username}</h2>
                <p> Role: {eventDTO.user.role}</p>
            </div>
            <div id="planInfo" className="planInfo">
            <h2>{eventDTO.title}</h2>
            <p>Date: <time >{date}</time></p>
            <p>Created at: {CreatedDate.toUTCString()}</p>
            <p>Description: {eventDTO.description}</p>
            </div>
        </div>
    );
}