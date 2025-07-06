import * as THREE from "three";
import React, { useMemo, useRef } from "react";
import { useFrame, useLoader } from "@react-three/fiber";

type DynamicGasCloudPropProps = {
    gasCloudWidth: number;
    gasCloudHeight: number;
    outerSize: number;
    textureURL?: string;
    opacity?: number;
};

const DynamicGasCloudProp: React.FC<DynamicGasCloudPropProps> = ({
                                                                     gasCloudHeight,
                                                                     gasCloudWidth,
                                                                     outerSize,
                                                                     textureURL = "images/textures/fire-ring.png",  // use the PNG you just downloaded
                                                                     opacity = 1.0,
                                                                 }) => {
    const geometry = useMemo(() => {
        const outer = new THREE.Shape();
        outer.moveTo(-outerSize / 2, -outerSize / 2);
        outer.lineTo(-outerSize / 2, outerSize / 2);
        outer.lineTo(outerSize / 2, outerSize / 2);
        outer.lineTo(outerSize / 2, -outerSize / 2);
        outer.lineTo(-outerSize / 2, -outerSize / 2);

        const hole = new THREE.Path();
        hole.moveTo(-gasCloudWidth / 2, -gasCloudHeight / 2);
        hole.lineTo(-gasCloudWidth / 2, gasCloudHeight / 2);
        hole.lineTo(gasCloudWidth / 2, gasCloudHeight / 2);
        hole.lineTo(gasCloudWidth / 2, -gasCloudHeight / 2);
        hole.lineTo(-gasCloudWidth / 2, -gasCloudHeight / 2);

        outer.holes.push(hole);

        return new THREE.ShapeGeometry(outer);
    }, [gasCloudWidth, gasCloudHeight, outerSize]);

    const texture = useLoader(THREE.TextureLoader, textureURL);
    const materialRef = useRef<THREE.MeshBasicMaterial>(null);

    // Animate UVs for "fire moving" effect
    useFrame((state, delta) => {
        if (texture) {
            texture.offset.y += delta * 0.05;  // adjust speed
        }
    });

    // Repeat texture to avoid stretching
    useMemo(() => {
        if (texture) {
            texture.wrapS = THREE.RepeatWrapping;
            texture.wrapT = THREE.RepeatWrapping;
            texture.repeat.set(0.1, 0.1);  // for ring texture 1x1 is good
        }
    }, [texture]);

    return (
        <mesh
            geometry={geometry}
            position={[0,1, 0]}
            rotation={[-Math.PI / 2, 0, 0]}
        >
            <meshBasicMaterial
                ref={materialRef}
                map={texture}
                transparent
                color={new THREE.Color(0x66ff66)}
                opacity={opacity}
                depthWrite={false}
            />
        </mesh>
    );
};

export default DynamicGasCloudProp;
