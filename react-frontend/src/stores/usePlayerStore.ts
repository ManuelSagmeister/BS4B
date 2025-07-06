import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { CharacterId } from '@/types/character-types';

export interface PlayerDB {
    playerName: string;
    winCount: number;
    matchCount: number;
    coins: number;
    xp: number;
    unlockedCharacters: CharacterId[];
    selectedCharacter?: CharacterId;
}

interface PlayerState {
    player: PlayerDB | null;
    setPlayer: (player: PlayerDB | any) => void; // jetzt kann es auch rohes Backendformat sein
    clearPlayer: () => void;
}

// Hilfsfunktion, um den rohen Player aus dem Backend zu parsen
function parsePlayer(rawPlayer: any): PlayerDB {
    return {
        ...rawPlayer,
        unlockedCharacters: (rawPlayer.unlockedCharacters || []).map(
            (char: any) => String(char.characterId) as CharacterId
        ),
    };
}

export const usePlayerStore = create<PlayerState>()(
    persist(
        (set) => ({
            player: null,
            setPlayer: (player) => {
                // Prüfen, ob player.unlockedCharacters ein Array von Objekten ist (Backend-Format)
                if (player && player.unlockedCharacters && player.unlockedCharacters.length > 0 && typeof player.unlockedCharacters[0] !== 'string') {
                    const parsedPlayer = parsePlayer(player);
                    set({ player: parsedPlayer });
                } else {
                    set({ player });
                }
            },
            clearPlayer: () => set({ player: null }),
        }),
        { name: 'player-storage' }
    )
);
