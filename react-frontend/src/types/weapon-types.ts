export type WeaponId = "Revolver" | "Shotgun" | "DoubleActionRevolver" | "CardThrower";

export type Weapon = {
    id: WeaponId;
    name: string;
    avatar: string;
    description: string;
    modelUrl: string;
    stats: {
        damage: number;
        range: number
    }
};

export const WEAPONS: Weapon[] = [
    {
        id: "Revolver",
        name: "Revolver",
        avatar: "/images/weapons/revolver_image.png",
        description: "Shoots one bullet",
        modelUrl: "/models/Revolver.glb",
        stats: {
            damage: 10,
            range: 100
        }
    },
    {
        id: "Shotgun",
        name: "Shotgun",
        avatar: "/images/weapons/shotgun_image.png",
        description: "Shoots 4 bullets spread in a cone-shape",
        modelUrl: "/models/Shotgun.glb",
        stats: {
            damage: 5,
            range: 50
        }
    },
    {
        id: "DoubleActionRevolver",
        name: "Double Action Revolver",
        avatar: "/images/weapons/revolver_image.png",
        description: "Shoots 2 bullets",
        modelUrl: "/models/Revolver.glb",
        stats: {
            damage: 10,
            range: 100
        }
    },
    {
        id: "CardThrower",
        name: "Card",
        avatar: "/images/weapons/card_image.png",
        description: "Throws an exploding card",
        modelUrl: "/models/CardWeapon.glb",
        stats: {
            damage: 10,
            range: 100
        }
    },
]