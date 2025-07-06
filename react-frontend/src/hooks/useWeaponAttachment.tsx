// hooks/useWeaponAttachment.ts
import { useEffect } from "react";
import * as THREE from "three";

export function useWeaponAttachment({
    skeleton,
    weaponObject,
    boneName = 'mixamorigRightHand',
    socketName = 'WeaponSocket',
    socketPosition = new THREE.Vector3(0, 0, 0),
    socketRotation = new THREE.Euler(0, 0, 0),
    weaponScale = new THREE.Vector3(100, 100, 100),
}: {
    skeleton: THREE.Skeleton;
    weaponObject: THREE.Object3D | null;
    boneName?: string;
    socketName?: string;
    socketPosition?: THREE.Vector3;
    socketRotation?: THREE.Euler;
    weaponScale?: THREE.Vector3;
}) {
    useEffect(() => {
        if (!skeleton || !weaponObject) return;

        const bone = skeleton.getBoneByName(boneName);
        if (!bone) return;

        // Remove existing socket if any
        const existingSocket = bone.getObjectByName(socketName);
        if (existingSocket) {
            bone.remove(existingSocket);
        }

        // Create new socket
        const weaponSocket = new THREE.Object3D();
        weaponSocket.name = socketName;

        bone.add(weaponSocket);

        // Set socket transform
        weaponSocket.position.copy(socketPosition);
        weaponSocket.rotation.copy(socketRotation);
        weaponSocket.scale.set(1, 1, 1); // socket scale always 1!

        // Add weapon to socket
        weaponSocket.add(weaponObject);

        weaponObject.position.set(0, 0, 0);
        weaponObject.rotation.set(0, 0, 0);
        weaponObject.scale.copy(weaponScale);

        console.log(`Weapon attached to ${boneName} via ${socketName}`);

    }, [skeleton, weaponObject, boneName, socketName, socketPosition, socketRotation, weaponScale]);
}
