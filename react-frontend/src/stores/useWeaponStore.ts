import {create} from "zustand/index";
import {WeaponId, WEAPONS} from "@/types/weapon-types";

type WeaponStore = {
    selectedWeapon: WeaponId;
    setSelectedWeapon: (weapon: WeaponId) => void;
};

export const useWeaponStore = create<WeaponStore>((set) => ({
    selectedWeapon: WEAPONS[0]!.id,
    setSelectedWeapon: (weapon: WeaponId) => set({ selectedWeapon: weapon }),
}));