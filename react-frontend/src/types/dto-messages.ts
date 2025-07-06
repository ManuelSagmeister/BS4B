export type Vector2 = { x: number; y: number };

export interface InputData {
    moveDirection: Vector2;
    shootDirection: Vector2;
    shoot: boolean;
    // reload?: boolean;
}

export interface ClientReadyDTO {
    type: "ClientReadyDTO";
    playerName: string;
    sessionId: string;
    roomId: string;
    selectedLevel: string;
    selectedCharacterId: string;
    selectedWeaponName: string;
}


export interface WelcomeDTO {
    type: "WelcomeDTO";
    roomId: string;
    sessionId: string;
}

export interface PlayerDTO {
    sessionId: string;
    name: string;
    position: Vector2;
    rotationAngle: number;
    health: number;
    selectedCharacterId: string;
    ammo: number;
    maxAmmo: number;
    selectedWeaponId: string;
}

export interface BulletDTO {
    id: string;
    position: Vector2;
    velocity: Vector2;
    weaponId: string;
}

export interface BoxColliderDTO {
    x: number
    y:number
    width: number;
    height: number;
}

export interface ZombieDTO {
    id: string;
    position: { x: number; y: number };
    rotationAngle: number;
    health: number;
    state: "Walking" | "Attack" | "Death";
}


export interface GameRoomDTO {
    type: "GameRoomDTO";
    players: PlayerDTO[];
    bullets: BulletDTO[];
    boxColliders: BoxColliderDTO[];
    gameState: string;
    countdownSeconds?: number;
    gasCloudWidth: number;
    gasCloudHeight: number;
    zombies: ZombieDTO[];
}

export interface PlayerLostDTO {
    type: "PlayerLost";
    sessionId: string; // Who lost
    xp: number;
    coins: number;
}

export interface GameOverDTO {
    type: "GameOver";
    winnerSessionId: string;   // Who won
    xp: number;                // XP awarded to winner
    coins: number;             // Coins awarded to winner
}