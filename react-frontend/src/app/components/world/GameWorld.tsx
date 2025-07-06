import {GameRoomDTO, InputData, WelcomeDTO} from "@/types/dto-messages";
import useMouse from "@/hooks/useMouse";
import {Environment, useKeyboardControls} from "@react-three/drei";
import {MyKeyboardControls} from "@/types/keyboard-controls";
import {useFrame} from "@react-three/fiber";
import Player from "@/app/components/Player";
import Bullet from "@/app/components/Bullet";
import React, {Suspense} from "react";
import {useLevelStore} from "@/stores/useLevelStore";
import {LEVELS} from "@/types/level-types";
import DynamicGasCloudProp from "@/app/components/staticProps/DynamicGasCloudProp";
import ZombieNPC from "@/app/components/ZombieNPC";

export function GameWorld({ gameState, welcomeDTO, sendMessage }: Readonly<{
    gameState: GameRoomDTO;
    welcomeDTO: WelcomeDTO;
    sendMessage: (msg: string) => void;
}>) {
    const { mouseWorldPosition, clicked } = useMouse();
    const [, getKeys] = useKeyboardControls<MyKeyboardControls>();
    const selectedLevelId = useLevelStore(state => state.selectedLevel);
    const selectedLevel = LEVELS.find(l => l.id === selectedLevelId);


    // Input handling moved INSIDE a Canvas component
    useFrame(() => {
        const state = getKeys();
        const localPlayer = gameState.players.find(p => p.sessionId === welcomeDTO.sessionId);

        // Shooting logic
        const shoot = clicked;
        let shootDirection = { x: 0, y: 0 };
        if (shoot && localPlayer) {
            const dx = mouseWorldPosition.x - localPlayer.position.x;
            const dy = mouseWorldPosition.z - localPlayer.position.y;
            const length = Math.hypot(dx, dy);
            shootDirection = length === 0 ? { x: 0, y: 0 } : {
                x: dx / length,
                y: dy / length
            };
        }

        // Movement logic
        const move = {
            x: (state.left ? -1 : 0) + (state.right ? 1 : 0),
            y: (state.forward ? -1 : 0) + (state.back ? 1 : 0)
        };

        const input: InputData = {
            moveDirection: move,
            shootDirection,
            shoot,
        };

        sendMessage(JSON.stringify(input));
    });

    return (
        <>
            <Environment preset="dawn" environmentIntensity={0.6}/>
            <Suspense fallback={null}>
                {selectedLevel && <selectedLevel.component/>}
            </Suspense>

            {gameState.players
                .map((p) => {
                    console.log("Rendering Player", p.name, p.selectedWeaponId);
                    console.log("Selected Character", p.selectedCharacterId);
                    return (
                        <Player
                            key={p.sessionId}
                            sessionId={p.sessionId}
                            name={p.name}
                            health={p.health}
                            position={p.position}
                            rotationAngle={p.rotationAngle}
                            isLocalPlayer={p.sessionId === welcomeDTO.sessionId}
                            selectedCharacterId={p.selectedCharacterId}
                            ammo={p.ammo}
                            maxAmmo={p.maxAmmo}
                            selectedWeaponId={p.selectedWeaponId}
                        />
                    );
                })}

            {/* Visualize colliders */}
            {Boolean(gameState.gasCloudHeight) && (
            <DynamicGasCloudProp
                gasCloudWidth={gameState.gasCloudWidth}
                gasCloudHeight={gameState.gasCloudHeight}
                outerSize={50}
                textureURL="/images/textures/fire-ring.png"
                opacity={1.0}
            />
            )}

            {gameState.bullets.map((bullet) => (
                <Bullet
                    key={bullet.id}
                    position={bullet.position}
                    weaponId={bullet.weaponId}
                />
            ))}

            {gameState.zombies.map((zombie) => (
                <ZombieNPC key={zombie.id} id={zombie.id} position={zombie.position} rotationAngle={zombie.rotationAngle} health={zombie.health} state={zombie.state}/>
            ))}

        </>
    );
}