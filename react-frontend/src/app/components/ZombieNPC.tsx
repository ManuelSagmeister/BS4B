import { useGLTF, useAnimations } from "@react-three/drei";
import { useEffect, useMemo, useRef, useState } from "react";
import { GLTF, SkeletonUtils } from "three-stdlib";
import { useFrame } from "@react-three/fiber";
import { Vector3 } from "three";
import { lerpAngle } from "@/utils/interpolate";
import * as THREE from "three";
import {ZombieDTO} from "@/types/dto-messages";
import PlayerUI from "@/app/components/overlays/PlayerUI";

export default function ZombieNPC({ position, rotationAngle, health, state }: Readonly<ZombieDTO>) {

    // Load zombie model
    const { scene, animations } = useGLTF("/models/ZombieNPC.glb") as GLTF;
    const clonedScene = useMemo(() => SkeletonUtils.clone(scene), [scene]);
    const { actions } = useAnimations(animations, clonedScene);

    // Refs for leaping
    const displayedPosition = useRef(new Vector3(position.x, 0, position.y));
    const displayedRotation = useRef((90 - rotationAngle) * (Math.PI / 180));
    const zombieRef = useRef<THREE.Object3D>(null);

    // Animation state
    const [currentAnim, setCurrentAnim] = useState("StandUp");
    const prevAnim = useRef<string | undefined>(undefined);

    // Animation switching based on state
    useEffect(() => {
        let newAnim = "StandUp";
        if (state === "Walking") newAnim = "Walking";
        else if (state === "Attack") newAnim = "Attack";
        else if (state === "Death") newAnim = "Death";

        if (newAnim !== currentAnim) {
            setCurrentAnim(newAnim);
        }
    }, [state, currentAnim]);

    // Play/fade animations
    useEffect(() => {
        if (!actions) return;
        if (prevAnim.current && actions[prevAnim.current]) {
            actions[prevAnim.current]!.fadeOut(0.2);
        }
        if (actions[currentAnim]) {
            actions[currentAnim].reset().fadeIn(0.2).play();
        }
        prevAnim.current = currentAnim;
    }, [actions, currentAnim]);

    // Clean up on unmounting
    useEffect(() => {
        return () => {
            Object.values(actions || {}).forEach((action) => action!.stop());
        };
    }, [actions]);

    // Per-frame updates (lerp position / rotation)
    useFrame(() => {
        // Lerp position
        displayedPosition.current.lerp(new Vector3(position.x, 0, position.y), 0.1);

        // Lerp rotation
        const targetRot = (90 - rotationAngle) * (Math.PI / 180);
        displayedRotation.current = lerpAngle(displayedRotation.current, targetRot, 0.15);

        // Apply to object
        if (zombieRef.current) {
            zombieRef.current.position.copy(displayedPosition.current);
            zombieRef.current.rotation.y = displayedRotation.current;
        }
    });

    return (
        <>
            <primitive ref={zombieRef} object={clonedScene} />
            <PlayerUI
                playerName={"Zombie"}
                isLocalPlayer={false}
                position={
                    zombieRef.current
                        ? zombieRef.current.position.toArray()
                        : displayedPosition.current.toArray()
                }
                health={health}
                ammo={0} // dummy
                maxAmmo={0} // dummy
                isZombie={true} // important!
            />

        </>
    );
}
