import { useGLTF, useAnimations } from "@react-three/drei"
import { useEffect, useMemo, useRef } from "react"
import { GLTF, SkeletonUtils } from "three-stdlib"
import * as THREE from "three"
import {useCharacterStore} from "@/stores/useCharacterStore";
import {CHARACTERS} from "@/types/character-types";

export default function LobbyPlayer({ 
  animationName = "Leaning",
  position = [0, 0, 0],
  rotation = [0, 0, 0],
}: {
  animationName?: string
  position?: [number, number, number]
  rotation?: [number, number, number]
}) {

  const selectedCharacterId = useCharacterStore((state) => state.selectedCharacter)
  const character = CHARACTERS.find(c => c.id === selectedCharacterId)
  const modelPath = character?.modelUrl || "/models/Cowboy_One.glb"



  const { scene, animations } = useGLTF(modelPath) as GLTF
  const clonedScene = useMemo(() => SkeletonUtils.clone(scene), [scene])
  const { actions } = useAnimations(animations, clonedScene)
  const playerRef = useRef<THREE.Object3D>(null)

  // Animation handling
  useEffect(() => {
    if (actions && actions[animationName]) {
      const action = actions[animationName]
      action?.reset().fadeIn(0.5).play()
      
      return () => {
        action?.fadeOut(0.5)
      }
    }
  }, [actions, animationName])

  // Cleanup animation on unmount
  useEffect(() => {
    return () => {
      Object.values(actions || {}).forEach(action => action?.stop())
    }
  }, [actions])

  return (
    <primitive
      ref={playerRef}
      object={clonedScene}
      position={position}
      rotation={rotation}
    />
  )
}
