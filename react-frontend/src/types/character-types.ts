export type CharacterId = "1" | "2" | "3" | "4";

export type Character = {
    id: CharacterId;
    name: string;
    avatar: string;
    description: string;
    modelUrl: string;
    price: number;
    owned: boolean;
};

export const CHARACTERS: Character[] = [
    {
        id: "1",
        name: "Sheriff",
        avatar: "/images/avatar/Sheriff_Avatar.jpg",
        description: "Tough decisions and nerves of steel.",
        modelUrl: "/models/Cowboy_One.glb",
        price: 0,
        owned: true,
    },
    {
        id: "2",
        name: "Bandit",
        avatar: "/images/avatar/Bandit_Avatar.jpg",
        description: "A ruthless outlaw who excels in chaos and dust clouds.",
        modelUrl: "/models/Bandit_Player.glb",
        price: 300,
        owned: false,
    },
    {
        id: "3",
        name: "Outlaw",
        avatar: "/images/avatar/Outlaw_Avatar.jpg",
        description: "Moves swiftly between cover, thriving in chaos.",
        modelUrl: "/models/Outlaw.glb",
        price: 100,
        owned: false,
    },
    {
        id: "4",
        name: "Gunslinger",
        avatar: "/images/avatar/Gunslinger_Avatar.jpg",
        description: "A sharpshooter feared by all.",
        modelUrl: "/models/Gunslinger_Player.glb",
        price: 1000,
        owned: false,
    },
];
