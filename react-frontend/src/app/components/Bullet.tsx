import React, { useRef } from "react";
import { useFrame } from "@react-three/fiber";
import { Vector3 } from "three";
import * as THREE from "three";
import { WeaponId } from "@/types/weapon-types";
import { useGLTF } from "@react-three/drei";

const BULLET_STYLES: Record<WeaponId, { color: string; size: number; geometry?: "sphere" | "box" | "card", modelUrl?: string }> = {
    Revolver: { color: "grey", size: 0.15, geometry: "sphere",  },
    Shotgun: { color: "black", size: 0.10, geometry: "sphere" },
    DoubleActionRevolver: { color: "red", size: 0.15, geometry: "sphere" },
    CardThrower: { color: "orange", size: 2, geometry: "card", modelUrl: "/models/CardWeapon.glb" },
};

function getBulletStyle(weaponId: string) {
    if (weaponId in BULLET_STYLES) {
        return BULLET_STYLES[weaponId as WeaponId];
    } else {
        return BULLET_STYLES.Revolver; // fallback
    }
}

interface BulletProps {
    position: { x: number; y: number };
    weaponId: string;
}

export default function Bullet({ position, weaponId }: Readonly<BulletProps>) {
    const displayedPosition = useRef(new Vector3(position.x, 0.5, position.y));
    const groupRef = useRef<THREE.Group>(null);

    const style = getBulletStyle(weaponId);

    // eslint-disable-next-line react-hooks/rules-of-hooks
    const gltf = style.modelUrl ? useGLTF(style.modelUrl) : null;

    useFrame(() => {
        const target = new Vector3(position.x, 0.5, position.y);
        displayedPosition.current.lerp(target, 0.2);

        if (groupRef.current) {
            groupRef.current.position.copy(displayedPosition.current);

            if (style.geometry === "card") {
                groupRef.current.rotation.y += 0.2; // spin effect
            }
        }
    });

    let content = null;
    if (style.modelUrl && gltf) {
        content = <primitive object={gltf.scene} scale={style.size} />;
    } else if (style.geometry === "sphere") {
        content = (
            <mesh>
                <sphereGeometry args={[style.size, 16, 16]} />
                <meshStandardMaterial color={style.color} />
            </mesh>
        );
    } else if (style.geometry === "box") {
        content = (
            <mesh>
                <boxGeometry args={[style.size, style.size * 0.1, style.size]} />
                <meshStandardMaterial color={style.color} />
            </mesh>
        );
    }

    return (
        <group ref={groupRef} position={displayedPosition.current}>
            {content}
        </group>
    );
}