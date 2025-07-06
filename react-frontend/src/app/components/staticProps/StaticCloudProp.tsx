import React, { useRef } from "react";
import { useFrame, useLoader } from "@react-three/fiber";
import * as THREE from "three";
import { TextureLoader } from "three";

type GasCloudPlaneProps = {
    textureUrl: string;
    position?: [number, number, number];
    size?: number;
    color?: THREE.Color
};

const GasCloudPlane: React.FC<GasCloudPlaneProps> = ({
                                                         textureUrl,
                                                         position = [0, 0.01, 0],
                                                         size = 2,
    color
                                                     }) => {
    const texture = useLoader(TextureLoader, textureUrl);
    const planeRef = useRef<THREE.Mesh>(null);

    useFrame((state) => {
        if (planeRef.current) {
            const time = state.clock.elapsedTime;

            // Subtle opacity flicker
            const material = planeRef.current.material as THREE.MeshBasicMaterial;
            material.opacity = 0.5 + 0.15 * Math.sin(time * 0.8);

            // Subtle scale pulsing
            const scaleFactor = 1 + 0.09 * Math.sin(time * 1.5);
            planeRef.current.scale.set(size * scaleFactor, size * scaleFactor, 1);

            // Subtle rotation of the plane itself (optional!)
            planeRef.current.rotation.z = time * 0.1;
        }
    });

    return (
        <mesh ref={planeRef} position={position} rotation={[-Math.PI / 2, 0, 0]}>
            <planeGeometry args={[size, size]} />
            <meshBasicMaterial
                map={texture}
                color={color}
                transparent
                depthWrite={false}
                depthTest={false} // optional
                opacity={0.5}
            />
        </mesh>
    );
};

export default GasCloudPlane;
