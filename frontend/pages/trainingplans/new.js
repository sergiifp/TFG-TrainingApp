import EditablePlan from "@/components/EditablePlan";
export default function NewPlan() {
    const EmptyPlan = {
        "name": "",
        "typeOfActivity": "road",
        "objective": "",
        "description": "",
        "trainingWeeks":[]
    }

    return (
        <>
            <EditablePlan Plan={EmptyPlan}></EditablePlan>
        </>
    );
}
