import '@/styles/loadingScreen.css';

export default function LoadingScreen() {
    return (
        <div className="loading-overlay">
            <div className="loading-card">
                <div className="loading-waiting">
                    <span>waiting</span>
                    <span className="dot dot1">.</span>
                    <span className="dot dot2">.</span>
                    <span className="dot dot3">.</span>
                </div>
                <div className="loading-title">LOADING THE SHOWDOWN...</div>
                <div className="loading-tip">Saddle up, partner!</div>
            </div>
        </div>
    );
}
