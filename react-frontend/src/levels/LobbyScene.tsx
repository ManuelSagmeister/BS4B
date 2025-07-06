import { useGLTF } from "@react-three/drei";

export default function LobbyScene() {
    const { scene } = useGLTF("/models/LobbyScene.glb");
    return (
        <>
            <primitive object={scene.clone()}  />
        </>
    );
}
