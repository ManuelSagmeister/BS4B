import { Box } from "@react-three/drei";
import { BoxColliderDTO } from "@/types/dto-messages";
import { nanoid } from "nanoid";

// Props: colliders = array of BoxColliderDTO
export function ColliderDebug({ colliders }: Readonly<{ colliders: BoxColliderDTO[] }>) {
    return (
        <>
            {colliders.map(collider => (
                <Box
                    key={nanoid()} // Prefer id, fallback to random
                    position={[collider.x, 0, collider.y]}
                    args={[collider.width * 2, 0.1, collider.height * 2]}
                >
                    <meshStandardMaterial color="black" />
                </Box>
            ))}
        </>
    );
}
