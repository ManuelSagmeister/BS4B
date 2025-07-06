import { create } from 'zustand';
import { CharacterId, CHARACTERS } from '@/types/character-types';

type CharacterStore = {
    selectedCharacter: CharacterId;
    setSelectedCharacter: (character: CharacterId) => void;
};

export const useCharacterStore = create<CharacterStore>((set) => ({
    selectedCharacter: CHARACTERS[0].id,
    setSelectedCharacter: (character) => set({ selectedCharacter: character }),
}));
