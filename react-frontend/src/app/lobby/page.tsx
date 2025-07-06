"use client";
import { notFound, useRouter } from "next/navigation";
import "@/styles/lobby.css";
import { useLevelStore } from "@/stores/useLevelStore";
import { usePlayerStore } from "@/stores/usePlayerStore";
import { Canvas } from "@react-three/fiber";
import { Environment, Sky, Stars } from "@react-three/drei";
import LobbyScene from "@/levels/LobbyScene";
import LobbyPlayer from "@/app/components/LobbyPlayer";
import CampfireVFX from "@/app/components/staticProps/CampfireVFX";
import Image from "next/image";
import { LEVELS } from "@/types/level-types";
import {useCharacterStore} from "@/stores/useCharacterStore";
import {CHARACTERS} from "@/types/character-types";

export default function LobbyPage() {
    const router = useRouter();
    const player = usePlayerStore((state) => state.player);
    const selectedLevelId = useLevelStore((state) => state.selectedLevel);
    const selectedLevelName = LEVELS.find((c) => c.id === selectedLevelId);
    const selectedLevel = LEVELS.find((c) => c.id === selectedLevelId);
    const selectedLevelImagePath = selectedLevel?.image;
    const selectedCharacterId = useCharacterStore((state) => state.selectedCharacter)
    const character = CHARACTERS.find(c => c.id === selectedCharacterId)

    if (!player) {
        notFound();
    }

    return (
        <div className="lobby-root">
            <Canvas
                camera={{
                    position: [-0.5, 5, 10],
                    fov: 60,
                    near: 0.1,
                    far: 1000,
                    zoom: 1.2,
                    rotation: [-0.15, 0, 0],
                }}
            >
                <Environment preset="dawn" environmentIntensity={0.18} />
                <Stars radius={50} depth={50} count={2000} factor={4} saturation={0} fade />
                <spotLight position={[-2, 16, 0]} angle={0.5} power={100} />
                <Sky azimuth={20} />
                <CampfireVFX />
                <CampfireVFX lightPosition={[5.5, 1, -1]} />
                <LobbyPlayer position={[-4, 0, 0]} rotation={[0, 0, 0]} />
                <LobbyScene />
            </Canvas>

            {/* Top-left player profile */}
            <div className="lobby-player-profile">
                <Image
                    src={`${character?.avatar}`}
                    alt="Player"
                    width={256}
                    height={256}
                    className="player-avatar"
                />
                <span className="lobby-player-name">{player.playerName}</span>
            </div>

            {/* Bottom-left selected level image */}
            <div className="lobby-map-preview">
                <Image
                    src={`/${selectedLevelImagePath}`}
                    alt="Map Preview"
                    width={256}
                    height={256}
                />
                <div className="lobbyMapTexts">
                    <span className="lobby-level-text">
                        Selected Level:
                    </span>
                    <span className="lobby-level-name">
                        {selectedLevelName?.name ?? "Unknown Map"}
                    </span>
                </div>
            </div>

            {/* Top-right hud */}
            <div className="lobby-hud">
                <div className="lobby-hud-item">
                    <Image src="/images/icons/Coin.png" alt="Currency" width={40} height={40} />
                    <span>{player.coins}</span>
                </div>
                <div className="lobby-hud-item">
                    <Image src="/images/icons/XPIcon.png" alt="XP" width={40} height={40} />
                    <span>{player.xp}</span>
                </div>

            </div>

            {/* Bottom-center: Start Game */}
            <div className="lobby-saloon-bar">
                <button
                    className="lobby-action-btn"
                    onClick={() => router.push("/game")}
                >
                    Start Game
                </button>
            </div>

            {/* Left-middle buttons */}
            <div className="lobby-left-actions">
                <button
                    className="lobby-action-btn"
                    onClick={() => router.push("/lobby/characterSelection")}
                >
                    Characters
                </button>
                <button
                    className="lobby-action-btn"
                    onClick={() => router.push("/lobby/weaponSelection")}
                >
                    Weapons
                </button>
                <button
                    className="lobby-action-btn"
                    onClick={() => router.push("/lobby/levelSelection")}
                >
                    Level
                </button>
                <button
                    className="lobby-exit-btn"
                    onClick={() => router.push("/")}
                >
                    Exit
                </button>
            </div>
        </div>
    );
}
