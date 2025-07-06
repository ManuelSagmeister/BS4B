import {useGLTF} from "@react-three/drei";

export default function CanyonClash() {
    const { scene } = useGLTF("/models/CanyonClashFrontend.glb")
    const clonedScene = scene.clone()
    return (
        <primitive object={clonedScene}/>
    )
}