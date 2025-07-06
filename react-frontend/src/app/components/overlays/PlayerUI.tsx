import { Html } from "@react-three/drei";
import { useEffect, useState } from "react";

type PlayerUIProps = {
    isLocalPlayer: boolean;
    isZombie?: boolean;
    playerName: string;
    health: number;
    position: [number, number, number];
    ammo: number;
    maxAmmo: number;
};

export default function PlayerUI({
                                     playerName,
                                     health,
                                     position,
                                     ammo,
                                     maxAmmo,
                                     isLocalPlayer,
                                     isZombie = false,
                                 }: PlayerUIProps) {
    const healthColor =
        health > 30 ? "#e2b76d" : health > 10 ? "#e67e22" : "#b71c1c";

    const [initialMaxHealth, setInitialMaxHealth] = useState<number | null>(null);

    useEffect(() => {
        // On first mount, set initialMaxHealth
        if (initialMaxHealth === null && health > 0) {
            setInitialMaxHealth(health);
        }
    }, [health, initialMaxHealth]);

    function AmmoLine({ loaded }: { loaded: boolean }) {
        return (
            <div
                style={{
                    width: 18,
                    height: 6,
                    background: loaded ? "#ffd700" : "#5c4032",
                    borderRadius: 3,
                    boxShadow: loaded ? "0 0 4px #ffd700aa" : "none",
                    transition: "background 0.2s, box-shadow 0.2s",
                }}
            />
        );
    }

    const maxHpForBar = initialMaxHealth ?? 100; // fallback if unknown

    return (
        <Html
            position={[position[0], position[1] + 3, position[2]]}
            center
            distanceFactor={8}
            style={{ pointerEvents: "none" }}
            zIndexRange={[100, 0]}
        >
            <div
                style={{
                    background: isZombie
                        ? "rgba(30, 10, 10, 0.85)"
                        : "rgba(44, 28, 13, 0.93)",
                    padding: "10px 20px 12px 20px",
                    borderRadius: "16px",
                    minWidth: 140,
                    textAlign: "center",
                    color: "#fff8e1",
                    fontFamily: "'Cinzel Decorative', 'Georgia', serif",
                    fontSize: 17,
                    border: "3px solid #a67c52",
                    boxShadow: "0 4px 18px #000c, 0 0 0 4px #6d4c41 inset",
                    letterSpacing: "0.07em",
                    userSelect: "none",
                    position: "relative",
                    overflow: "visible",
                }}
            >
                {/* Name */}
                <div
                    style={{
                        fontSize: "20px",
                        fontWeight: "700",
                        color: isZombie ? "#ff4c4c" : "#ffd700",
                        textShadow: "0 2px 5px rgba(0,0,0,0.8)",
                        marginBottom: "10px",
                    }}
                >
                    {playerName}
                </div>

                {/* Health Bar */}
                <div
                    style={{
                        background: "#fff3e0",
                        border: "2px solid #a67c52",
                        borderRadius: "8px",
                        overflow: "hidden",
                        height: 15,
                        marginBottom: 8,
                        boxShadow: "0 2px 6px #0003",
                        position: "relative",
                    }}
                >
                    <div
                        style={{
                            width: `${Math.max(0, Math.min((health / maxHpForBar) * 100, 100))}%`,
                            background: healthColor,
                            height: "100%",
                            transition: "width 0.2s, background 0.2s",
                            boxShadow: "0 0 12px #e2b76d88",
                        }}
                    />
                    <span
                        style={{
                            position: "absolute",
                            left: "50%",
                            top: "50%",
                            transform: "translate(-50%, -50%)",
                            color: "#4e2e0e",
                            fontWeight: 700,
                            fontSize: 13,
                            textShadow: "0 1px 2px #fff8",
                            letterSpacing: "0.04em",
                        }}
                    >
                        {health }HP
                    </span>
                </div>

                {/* Ammo - only for local player and non-zombies */}
                {!isZombie && isLocalPlayer && (
                    <div
                        style={{
                            display: "flex",
                            justifyContent: "center",
                            alignItems: "center",
                            gap: 6,
                            marginTop: 6,
                        }}
                    >
                        {Array.from({ length: maxAmmo }).map((_, i) => (
                            <AmmoLine key={i} loaded={ammo > i} />
                        ))}
                    </div>
                )}
            </div>
        </Html>
    );
}
