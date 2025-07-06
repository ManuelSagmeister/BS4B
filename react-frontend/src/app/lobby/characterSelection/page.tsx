'use client';

import {CharacterId, CHARACTERS} from '@/types/character-types';
import {useCharacterStore} from '@/stores/useCharacterStore';
import {useRouter} from 'next/navigation';
import {usePlayerStore} from '@/stores/usePlayerStore';
import {useUpdatePlayer} from '@/hooks/database/useUpdatePlayer';
import '@/styles/characterSelection.css';

export default function ChooseCharacterPage() {
    const selectedCharacterId = useCharacterStore((state) => state.selectedCharacter);
    const setSelectedCharacter = useCharacterStore((state) => state.setSelectedCharacter);
    const router = useRouter();

    const player = usePlayerStore((state) => state.player);
    const setPlayerInStore = usePlayerStore((state) => state.setPlayer);
    const {unlockCharacter} = useUpdatePlayer();

    const ownedCharacters = player?.unlockedCharacters ?? [];

    const handleSelectCharacter = (characterId: CharacterId) => {
        setSelectedCharacter(characterId);
        router.push('/lobby');
    };

    const handleBuy = async (characterId: CharacterId, price: number) => {
        if (!player) return;

        if (ownedCharacters.includes(characterId)) {
            alert('Du besitzt diesen Charakter bereits!');
            return;
        }

        if (player.coins < price) {
            alert('Nicht genug Coins!');
            return;
        }

        try {
            const updatedPlayer = await unlockCharacter(player.playerName, characterId);
            setPlayerInStore(updatedPlayer);
        } catch (err) {
            alert('Fehler beim Freischalten: ' + (err instanceof Error ? err.message : 'Unbekannter Fehler'));
        }
    };

    return (
        <div className="shop-bg">
            <div className="shop-container">
                <h1 className="shop-title">BRAWL SHOP</h1>
                <div className="shop-grid">
                    {CHARACTERS
                        .sort((a, b) => (a.price ?? 0) - (b.price ?? 0))
                        .map((character) => {
                            const owned = ownedCharacters.includes(character.id);
                            const isSelected = selectedCharacterId === character.id;

                            return (
                                <div
                                    key={character.id}
                                    className={`shop-card${isSelected ? ' selected' : ''}${owned ? ' owned' : ''}`}

                                >
                                    <img src={character.avatar} alt={character.name} className="shop-avatar"/>
                                    <div className="shop-info">
                                        <span className="shop-name">{character.name}</span>
                                        <span
                                            className="shop-desc">{character.description ?? 'A fearless fighter in the wild west!'}</span>
                                    </div>
                                    <div className="shop-actions">
                                        {owned ? (
                                            <button
                                                className="shop-btn select"
                                                onClick={() => handleSelectCharacter(character.id)}
                                                disabled={isSelected}
                                            >
                                                {isSelected ? 'Selected' : 'Select'}
                                            </button>
                                        ) : (
                                            <button
                                                className="shop-btn buy"
                                                onClick={() => handleBuy(character.id, character.price ?? 0)}
                                            >
                                                Buy ({character.price ?? 0} 🪙)
                                            </button>
                                        )}
                                    </div>
                                    {!owned && <div className="shop-lock">🔒</div>}
                                </div>
                            );
                        })}
                </div>
                <div className="button-row">
                    <button
                        className="western-btn exit"
                        type="button"
                        onClick={() => router.push('/lobby')}
                    >
                        ⬅ Back to Lobby
                    </button>
                </div>
            </div>
        </div>
    );
}