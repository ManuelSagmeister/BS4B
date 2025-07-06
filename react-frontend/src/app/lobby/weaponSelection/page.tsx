'use client';

import {Weapon, WEAPONS} from '@/types/weapon-types';
import {useWeaponStore} from '@/stores/useWeaponStore';
import {useRouter} from 'next/navigation';
import '@/styles/WeaponPage.css';

export default function ChooseWeaponPage() {
    const selectedWeapon = useWeaponStore((state) => state.selectedWeapon);
    const setSelectedWeapon = useWeaponStore((state) => state.setSelectedWeapon);
    const router = useRouter();

    function handleSelect(weapon: Weapon) {
        setSelectedWeapon(weapon.id);
        router.push('/lobby');
    }

    function handleBack() {
        router.push("/lobby");
    }

    return (
        <div className="page-center">
            <div className="shop-container">
                <div className="weapon-chooser">
                    <h1 className="western-title">Choose Your Weapon</h1>
                    <div className="weapon-list">
                        {WEAPONS.map((weapon) => {
                            const isSelected = selectedWeapon === weapon.id;

                            return (
                                <div
                                    key={weapon.id}
                                    className={`weapon-card${selectedWeapon === weapon.id ? ' selected' : ''}`}
                                    aria-pressed={selectedWeapon === weapon.id}
                                >
                                    <img
                                        src={weapon.avatar}
                                        alt={weapon.name}
                                        className="weapon-avatar"
                                    />
                                    <div className="weapon-info">
                                        <span className="weapon-name">{weapon.name}</span>
                                        <span className="weapon-desc">{weapon.description}</span>
                                        <ul className="weapon-stats">
                                            <li>Damage: {weapon.stats.damage}</li>
                                            <li>Range: {weapon.stats.range}</li>
                                        </ul>
                                    </div>
                                    <button
                                        className="shop-btn select"
                                        type="button"
                                        onClick={() => handleSelect(weapon)}
                                        disabled={isSelected}
                                    >
                                        {isSelected ? 'Selected' : 'Select'}
                                    </button>
                                </div>
                            )
                        })}
                    </div>
                    <div className="button-row">
                        <button
                            className="western-btn exit"
                            type="button"
                            onClick={handleBack}
                        >
                            ⬅ Back to Lobby
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
