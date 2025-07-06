"use client";

import {useState} from "react";
import {useRouter} from "next/navigation";
import {LEVELS} from "@/types/level-types";
import "@/styles/levelSelection.css";
import {useLevelStore} from "@/stores/useLevelStore";
import {usePlayerStore} from "@/stores/usePlayerStore";

export default function LevelSelection() {
    const router = useRouter();
    const selectedLevel = useLevelStore((state) => state.selectedLevel);
    const setSelectedLevel = useLevelStore((state) => state.setSelectedLevel);
    const xp = usePlayerStore((state) => state.player?.xp ?? 0);

    // Find the selected level index, fallback to 0 if not found
    const [, setFocusedIndex] = useState(
        Math.max(LEVELS.findIndex((l) => l.id === selectedLevel), 0)
    );

    function handleSelect(index: number) {
        const level = LEVELS[index];
        if (xp < level.xpToUnlock) return;
        setSelectedLevel(level.id);
        setFocusedIndex(index);
        router.push('/lobby');
    }

    function handleBack() {
        router.push("/lobby");
    }

    return (
        <div className="level-selection-container">
            <div className="shop-container">
                <h1 className="western-title">Choose Level</h1>
                <div className="level-grid" role="list">
                    {LEVELS.map((level, idx) => {
                        const isLocked = xp < level.xpToUnlock;
                        const isSelected = selectedLevel === level.id;
                        const selectedLevelImagePath = level?.image;

                        return (
                            <div
                                key={level.id}
                                className={`level-card${isSelected ? " selected" : ""}${isLocked ? " locked" : ""}`}
                                aria-pressed={isSelected}
                                aria-label={
                                    isLocked
                                        ? `${level.name} (locked, unlocks at ${level.xpToUnlock} XP)`
                                        : `${level.name}${isSelected ? " (selected)" : ""}`
                                }
                                tabIndex={0}
                            >
                                <div className="level-image-wrapper">
                                    <img
                                        src={`/${selectedLevelImagePath}`}
                                        alt={level.name}
                                        className="level-image"
                                        draggable={false}
                                    />
                                    {isLocked && (
                                        <span className="lock-overlay">
                                        <span className="lock-icon" aria-hidden="true">🔒</span>
                                    </span>
                                    )}
                                </div>
                                <div className="level-info">
                                    <span className="level-name">{level.name}</span>
                                    <span className="level-desc">{level.description}</span>
                                    {isLocked && (
                                        <span className="locked-text">
                                        Unlocks at {level.xpToUnlock} XP
                                    </span>
                                    )}
                                </div>
                                <button
                                    className="shop-btn select"
                                    type="button"
                                    onClick={() => handleSelect(idx)}
                                    disabled={isSelected}
                                >
                                    {isSelected ? 'Selected' : 'Select'}
                                </button>
                            </div>
                        );
                    })}
                </div>
                <div className="button-row">
                    <button
                        className="western-btn exit"
                        type="button"
                        onClick={handleBack}
                    >
                        ⬅ Back to Lobby
                    </button>
                </div>
            </div>
        </div>
    );
}
