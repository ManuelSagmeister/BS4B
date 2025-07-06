import { Sparkles } from "@react-three/drei";

type Vec3 = [number, number, number];

interface CampfireVFXProps {
    lightPosition?: Vec3;
}

export default function CampfireVFX({ lightPosition = [-8, 1, -2] as Vec3 }: CampfireVFXProps) {
    return (
        <>
            <mesh position={[-8, 0, -2.25]}>
                <sphereGeometry args={[0.25, 16, 16]} />
                <meshStandardMaterial emissive="#ff6600" emissiveIntensity={2} color="#ff4400" />
            </mesh>

            <Sparkles
                count={50}
                speed={1}
                size={2}
                color="#ffaa33"
                scale={[1, 2, 1]}
                position={lightPosition}
            />

            <pointLight
                position={lightPosition}
                intensity={1.5}
                distance={10}
                color="#ff6600"
            />
        </>
    );
}
