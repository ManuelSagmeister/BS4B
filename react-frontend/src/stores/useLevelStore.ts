import { create } from 'zustand';
import {LevelId, LEVELS} from '@/types/level-types';

type LevelStore = {
    selectedLevel: LevelId;
    setSelectedLevel: (level: LevelId) => void;
};

export const useLevelStore = create<LevelStore>((set) => ({
    selectedLevel: (LEVELS[0]?.id ?? "CanyonClash"),
    setSelectedLevel: (level: LevelId) => set({ selectedLevel: level }),
}));