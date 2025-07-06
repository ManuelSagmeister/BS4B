"use client";

import {useEffect, useState} from "react";
import {ClientReadyDTO, GameRoomDTO, WelcomeDTO} from "@/types/dto-messages";
import {Canvas} from "@react-three/fiber";
import {GameWorld} from "@/app/components/world/GameWorld";
import LoadingScreen from "@/app/components/overlays/LoadingScreen";
import CountdownScreen from "@/app/components/overlays/CountdownScreen";
import GameEndScreen from "@/app/components/overlays/GameEndScreen";
import "@/styles/game.css";
import {useLevelStore} from "@/stores/useLevelStore";
import {LEVELS} from "@/types/level-types";
import {useCharacterStore} from "@/stores/useCharacterStore";
import {usePlayerStore} from "@/stores/usePlayerStore";
import {useWeaponStore} from "@/stores/useWeaponStore";

export function GameLogic({sendMessage, lastMessage}: Readonly<{
    sendMessage: (msg: string) => void;
    lastMessage: string | null;
}>) {
    const [gameState, setGameState] = useState<GameRoomDTO | null>(null);
    const [welcomeDTO, setWelcomeDTO] = useState<WelcomeDTO | null>(null);
    const [gameEnded, setGameEnded] = useState(false);
    const [isWinner, setIsWinner] = useState(false);
    const [endXp, setEndXp] = useState(0);
    const [endCoins, setEndCoins] = useState(0);

    // selected level
    const selectedLevelId = useLevelStore(state => state.selectedLevel);
    const selectedLevel = LEVELS.find(l => l.id === selectedLevelId);
    const selectedCharacterId = useCharacterStore(state => state.selectedCharacter);
    const playerName = usePlayerStore(state => state.player?.playerName);
    const selectedWeaponName = useWeaponStore(state => state.selectedWeapon);

    // AT the start, we receive messages
    useEffect(() => {
        if (!lastMessage || gameEnded) return;
        try {
            const msg = JSON.parse(lastMessage);

            if (msg.type === "WelcomeDTO") setWelcomeDTO(msg);
            if (msg.type === "GameRoomDTO") {
                setGameState(msg);
            }

            // Handle end messages
            if (msg.type === "GameOver") {
                // Winner
                setGameEnded(true);
                setIsWinner(msg.winnerSessionId === welcomeDTO?.sessionId);
                setEndXp(msg.xp);
                setEndCoins(msg.coins);
            }
            if (msg.type === "PlayerLost") {
                // You lost
                if (msg.sessionId === welcomeDTO?.sessionId) {
                    setGameEnded(true);
                    setIsWinner(false);
                    setEndXp(msg.xp);
                    setEndCoins(msg.coins);
                }
            }
            console.log("GameEnded is : " , gameEnded)
        } catch (e) {
            console.error("Message parse error:", e);
        }
    }, [lastMessage]);

    // If we get welcome dto, we start the matchmaking by sending RDCOMClient message
    useEffect(() => {
        if (welcomeDTO) {
            const readyMessage : ClientReadyDTO = {
                type: "ClientReadyDTO",
                playerName: playerName ?? "Problem with name",
                sessionId: welcomeDTO.sessionId,
                roomId: welcomeDTO.roomId,
                selectedLevel: selectedLevel?.id ?? "CanyonClash",
                selectedCharacterId: selectedCharacterId,
                selectedWeaponName: selectedWeaponName,
            };
            sendMessage(JSON.stringify(readyMessage));
        }
    }, [welcomeDTO]);

    if (!welcomeDTO || !gameState) {
        return <LoadingScreen/>;
    }

    if(gameEnded){
        return <GameEndScreen isWinner={isWinner} xp={endXp} coins={endCoins}/>
    }

    return (
        <div className="game-canvas-root">
            <Canvas>
                <GameWorld
                    gameState={gameState}
                    welcomeDTO={welcomeDTO}
                    sendMessage={sendMessage}
                />
            </Canvas>

            {gameState.gameState === "COUNTDOWN" && (
                <>
                    <CountdownScreen countdownSeconds={gameState.countdownSeconds} />
                </>
            )}
        </div>
    );
}