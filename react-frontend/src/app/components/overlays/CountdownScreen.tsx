
type Props = {
    countdownSeconds: number | undefined;
}

export default function CountdownScreen ({countdownSeconds} : Props){
    return(
        <div className="w-screen h-screen">
            <div style={{
                position: "absolute",
                top: 0, left: 0, right: 0, bottom: 0,
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                fontSize: "6rem",
                color: "white",
                textShadow: "2px 2px 4px rgba(0,0,0,0.5)",
                zIndex: 10
            }}>
                {countdownSeconds && countdownSeconds > 0 ? countdownSeconds : "GO!"}
            </div>
        </div>
    );
}