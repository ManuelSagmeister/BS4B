import { useGLTF } from "@react-three/drei";

export default function GhostTown() {
    const { scene } = useGLTF("/models/FrontendGhostTown.glb");
    const clonedScene = scene.clone();
    return (
        <>
            <primitive object={clonedScene} rotation={[0, Math.PI, 0]} scale={[-1, 1, 1]} />
        </>
    );
}
