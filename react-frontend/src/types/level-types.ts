import React from "react";

export type LevelId = "CanyonClash" | "GhostTown" | "GoldMine";


export type Level = {
    id: LevelId;
    name: string;
    image: string;
    description: string;
    xpToUnlock: number;
    component: React.LazyExoticComponent<React.FC>

};

export const LEVELS: Level[] = [
    {
        id: "CanyonClash",
        name: "Canyon Clash",
        image: "images/levels/CanyonClashImage.jpg",
        description: "A dusty canyon with tight corners.",
        xpToUnlock: 0,
        component: React.lazy(() => import("@/levels/CanyonClash")),
    },
    {
        id: "GoldMine",
        name: "Gold Mine",
        image: "images/levels/GoldMineImage.jpg",
        description: "A GoldMine and many obstacles for wild chases.",
        xpToUnlock: 100,
        component: React.lazy(() => import("@/levels/GoldMine")),
    },
    {
        id: "GhostTown",
        name: "Ghost Town",
        image: "images/levels/GhostTown.jpg",
        description: "Abandoned buildings, perfect for sneaky shootouts.",
        xpToUnlock: 200,
        component: React.lazy(() => import("@/levels/GhostTown")),
    },
];
