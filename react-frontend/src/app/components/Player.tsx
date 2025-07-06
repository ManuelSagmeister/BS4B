import { useGLTF, useAnimations } from "@react-three/drei"
import {useEffect, useMemo, useRef, useState} from "react"
import { PlayerDTO } from "@/types/dto-messages"
import { GLTF, SkeletonUtils } from "three-stdlib"
import { useFrame, useThree } from "@react-three/fiber"
import { updateCameraFollow } from "@/hooks/useCameraFollow"
import { Vector3 } from "three"
import {lerpAngle} from "@/utils/interpolate"
import * as THREE from "three"
import PlayerUI from "@/app/components/overlays/PlayerUI";
import { useWeaponAttachment } from '@/hooks/useWeaponAttachment';


import {CHARACTERS} from "@/types/character-types";
import {WEAPONS} from "@/types/weapon-types";
import { useCharacterStore } from '@/stores/useCharacterStore';

export default function Player({ isShooting, selectedWeaponId,name, position, rotationAngle, health, isLocalPlayer, selectedCharacterId, ammo,maxAmmo}: PlayerDTO
    & { isLocalPlayer: boolean ,isShooting: boolean }) {

    const globalSelectedCharacterId = useCharacterStore((state) => state.selectedCharacter);
    const characterIdToUse = isLocalPlayer ? globalSelectedCharacterId : selectedCharacterId;
    const character = CHARACTERS.find(c => c.id === characterIdToUse);
    const modelPath = character?.modelUrl || "/models/Cowboy_One.glb"
    const selectedWeapon = WEAPONS.find((w => w.id === selectedWeaponId));
    const weaponModelPath = selectedWeapon?.modelUrl || "/models/Revolver.glb"

    const { scene: weaponGLTFScene } = useGLTF(weaponModelPath) as GLTF
    const weaponObject = useMemo(() => SkeletonUtils.clone(weaponGLTFScene), [weaponGLTFScene])

    const { scene, animations } = useGLTF(modelPath) as GLTF
    const clonedScene = useMemo(() => SkeletonUtils.clone(scene), [scene])
    const { actions } = useAnimations(animations, clonedScene)

    const { camera } = useThree()
    const smoothedPositionRef = useRef(new Vector3())
    const displayedPosition = useRef(new Vector3(position.x, 0, position.y))
    const displayedRotation = useRef((90 - rotationAngle) * (Math.PI / 180))
    const playerRef = useRef<THREE.Object3D>(null)

    // Animation state management
    const [currentAnim, setCurrentAnim] = useState("Idle")
    const prevAnim = useRef<string | undefined>(undefined)

    // UI

    const [, setUiPosition] = useState<[number, number, number]>(displayedPosition.current.toArray());

    // dynamic weapons:

    let skinnedMesh: THREE.SkinnedMesh | undefined = undefined;

    clonedScene.traverse((child) => {
        if (child instanceof THREE.SkinnedMesh) {
            skinnedMesh = child;
        }
    });
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    const skeleton = skinnedMesh?.skeleton;

    useWeaponAttachment({
        skeleton,
        weaponObject,
        boneName: 'mixamorigRightHand',
        socketName: 'WeaponSocket',
        socketPosition: new THREE.Vector3(3, 15, 0), // adjust as needed!
        socketRotation: new THREE.Euler(0, 0, 0),   // adjust as needed!
        weaponScale: new THREE.Vector3(100, 100, 100), // current test value
    });

    // Play/fade animations on state change
    useEffect(() => {
        if (!actions) return
        if (prevAnim.current && actions[prevAnim.current]) {
            actions[prevAnim.current]!.fadeOut(0.2)
        }
        if (actions[currentAnim]) {
            actions[currentAnim].reset().fadeIn(0.2).play()
        }
        prevAnim.current = currentAnim
    }, [actions, currentAnim])

    // Clean up on unmount
    useEffect(() => {
        return () => {
            Object.values(actions || {}).forEach(action => action!.stop())
        }
    }, [actions])

    useFrame(() => {
        // Update smoothed position
        displayedPosition.current.lerp(new Vector3(position.x, 0, position.y), 0.1)

        // After updating displayedPosition, update the UI position state
        setUiPosition([
            displayedPosition.current.x,
            displayedPosition.current.y,
            displayedPosition.current.z
        ]);

        // Update smoothed rotation
        const targetRot = (90 - rotationAngle) * (Math.PI / 180)
        displayedRotation.current = lerpAngle(displayedRotation.current, targetRot, 0.15)

        // Apply to actual Three.js object
        if (playerRef.current) {
            playerRef.current.position.copy(displayedPosition.current)
            playerRef.current.rotation.y = displayedRotation.current
        }

        // Camera follow
        if (isLocalPlayer) {
            updateCameraFollow(
                displayedPosition.current,
                smoothedPositionRef,
                camera,
                {
                    cameraOffset: new Vector3(0, 10, 4),
                    targetLerp: 0.08,
                    cameraLerp: 0.08,
                    instant: false,
                }
            )
        }

        // Animation switching logic
        if (health <= 0 && currentAnim !== "Death") {
            setCurrentAnim("Death")
        } else if (Math.abs(position.x - displayedPosition.current.x) > 0.01 || Math.abs(position.y - displayedPosition.current.z) > 0.01) {
            // Simple movement detection: adjust as needed for your movement logic
            if (currentAnim !== "Run" && health > 0) setCurrentAnim("Run")
        } else if (currentAnim !== "Idle" && health > 0) {
            setCurrentAnim("Idle")
        }
    })

    return (
        <>
            <primitive ref={playerRef} object={clonedScene}/>
            <PlayerUI
                playerName={name}
                isLocalPlayer={isLocalPlayer}
                position={playerRef.current ? playerRef.current.position.toArray() : displayedPosition.current.toArray()}
                health={health}
                ammo={ammo}
                maxAmmo={maxAmmo}
                isZombie={false}
            />
        </>
    )
}