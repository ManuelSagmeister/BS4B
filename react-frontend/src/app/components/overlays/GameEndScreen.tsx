'use client';

import { useRouter } from "next/navigation";
import '@/styles/GameEndScreen.css';
import { useUpdatePlayer } from "@/hooks/database/useUpdatePlayer";

type Props = {
    isWinner: boolean;
    xp: number;
    coins: number;
};

export default function GameEndScreen({ isWinner, xp, coins }: Props) {
    const router = useRouter();
    const { player, error, loading } = useUpdatePlayer(isWinner, xp, coins);

    const handleContinue = () => {
        router.push('/lobby');
    };

    return (
        <div className="death-overlay">
            <div className="death-card">
                <div className="banner">{isWinner ? 'WANTED: CHAMPION' : 'YOU LOST, LOSER'}</div>
                <div className="icon">{isWinner ? '🤠' : '💀'}</div>
                <div className="death-title">{isWinner ? 'YOU WON THE SHOWDOWN!' : 'DEAD IN THE DUST'}</div>
                <div className="death-stats">
                    <div>XP Gained: <strong>{xp}</strong></div>
                    <div>Coins Found: <strong>{coins}</strong></div>
                    {player && (
                        <div className="death-summary">
                            <div>Total Wins: <strong>{player.winCount}</strong></div>
                            <div>Total Matches: <strong>{player.matchCount}</strong></div>
                        </div>
                    )}
                </div>
                {loading && <div className="death-info">Updating player stats...</div>}
                {error && <div className="death-error">{error}</div>}
                <button
                    className="death-btn"
                    onClick={handleContinue}
                    disabled={loading}
                >
                    Back to Lobby
                </button>
            </div>
        </div>
    );
}
