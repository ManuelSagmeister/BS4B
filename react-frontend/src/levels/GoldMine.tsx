import { useGLTF } from "@react-three/drei";
import StaticCloudProp from "@/app/components/staticProps/StaticCloudProp";
import * as THREE from "three";
export default function GoldMine() {
    const { scene } = useGLTF("/models/GoldMineFrontend.glb");
    const clonedScene = scene.clone();
    return (
        <>
            <primitive object={clonedScene} rotation={[0, Math.PI, 0]} scale={[-1, 1, 1]} />
            <StaticCloudProp position={[8,1,-19]} textureUrl={"/images/textures/clouds.png"} color={new THREE.Color(0x66ff66)}/>
            <StaticCloudProp position={[0,1,9]} textureUrl={"/images/textures/clouds.png"} color={new THREE.Color(0xcc0000)}/>
        </>
    );
}
