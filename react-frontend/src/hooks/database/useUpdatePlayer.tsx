import { useEffect, useState, useCallback } from 'react';
import { useRouter } from 'next/navigation';
import { PlayerDB, usePlayerStore } from '@/stores/usePlayerStore';
import { PLAYER_INFO_URL, UPDATE_PLAYER_URL, UNLOCK_CHARACTER_URL } from '@/config';
import { CharacterId } from '@/types/character-types';

type BackendCharacter = {
    characterId: number;
};

export function useUpdatePlayer(isWinner?: boolean, xp?: number, coins?: number) {
    const router = useRouter();
    const playerFromStore = usePlayerStore((state) => state.player);
    const setPlayerInStore = usePlayerStore((state) => state.setPlayer);

    const [player, setPlayer] = useState<PlayerDB | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(false);

    const fetchAndUpdatePlayer = useCallback(async () => {
        if (!playerFromStore?.playerName) {
            setError('Kein Spielername gefunden. Zurück zur Startseite.');
            router.push('/');
            return;
        }

        setLoading(true);
        setError(null);

        try {
            // Fetch current player data
            const res = await fetch(`${PLAYER_INFO_URL}${playerFromStore.playerName}`);
            if (!res.ok) throw new Error('Spieler konnte nicht geladen werden.');
            const data: PlayerDB = await res.json();

            // Update only if XP and coin update requested
            const updatedPlayer: PlayerDB = {
                ...data,
                matchCount: isWinner !== undefined ? data.matchCount + 1 : data.matchCount,
                winCount: isWinner ? data.winCount + 1 : data.winCount,
                xp: xp ? data.xp + xp : data.xp,
                coins: coins ? data.coins + coins : data.coins,
            };

            const updateRes = await fetch(UPDATE_PLAYER_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(updatedPlayer),
            });

            if (!updateRes.ok) {
                throw new Error('Spieler konnte nicht aktualisiert werden.');
            }

            setPlayer(updatedPlayer);
            setPlayerInStore(updatedPlayer);
        } catch (err) {
            if (err instanceof Error) {
                setError('Fehler beim Laden oder Aktualisieren des Spielers: ' + err.message);
            } else {
                setError('Unbekannter Fehler beim Laden oder Aktualisieren des Spielers.');
            }
        } finally {
            setLoading(false);
        }
    }, [playerFromStore, setPlayerInStore, isWinner, xp, coins, router]);

    const unlockCharacter = useCallback(
        async (playerName: string, characterId: string): Promise<PlayerDB> => {
            const response = await fetch(
                `${UNLOCK_CHARACTER_URL}?playerName=${playerName}&characterId=${characterId}`,
                {
                    method: 'POST',
                }
            );

            if (!response.ok) {
                throw new Error('Failed to unlock character');
            }

            const updatedPlayerRaw = await response.json();

            const updatedPlayer: PlayerDB = {
                ...updatedPlayerRaw,
                unlockedCharacters: (updatedPlayerRaw.unlockedCharacters || []).map(
                    (char: BackendCharacter) => String(char.characterId) as CharacterId
                ),
            };

            setPlayer(updatedPlayer);
            setPlayerInStore(updatedPlayer);

            return updatedPlayer;
        },
        [setPlayer, setPlayerInStore]
    );

    useEffect(() => {
        if (isWinner !== undefined && xp !== undefined && coins !== undefined) {
            fetchAndUpdatePlayer();
        }
    }, []);

    return { player, error, loading, unlockCharacter };
}