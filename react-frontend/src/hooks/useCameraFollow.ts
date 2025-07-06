import { Vector3 } from "three";
import { RefObject } from "react";

/**
 * Smoothly moves the camera to follow a target position.
 * @param targetPosition - The position the camera should follow (player position).
 * @param cameraTargetRef - Ref to the smoothed "look at" target (Vector3).
 * @param camera - The Three.js camera.
 * @param options - Optional configuration.
 */
export function updateCameraFollow(
    targetPosition: Vector3,
    cameraTargetRef: RefObject<Vector3> | Vector3,
    camera: any,
    options?: {
        cameraOffset?: Vector3;
        targetLerp?: number;
        cameraLerp?: number;
        instant?: boolean;
    }
) {
    // Default parameters
    const {
        cameraOffset = new Vector3(0, 2, 2), // [x, y, z] offset from target
        targetLerp = 0.15,                   // Smoothing for "look at" target
        cameraLerp = 0.15,                   // Smoothing for camera position
        instant = false,                     // Instantly snap camera (for teleports)
    } = options || {};

    // Get the smoothed target position (the point the camera looks at)
    const smoothedTarget =
        cameraTargetRef instanceof Vector3
            ? cameraTargetRef
            : cameraTargetRef.current;

    if (!smoothedTarget) return;

    if (instant) {
        smoothedTarget.copy(targetPosition);
    } else {
        smoothedTarget.lerp(targetPosition, targetLerp);
    }

    // Calculate the desired camera position (relative to the target)
    const desiredCameraPos = smoothedTarget.clone().add(cameraOffset);

    if (instant) {
        camera.position.copy(desiredCameraPos);
    } else {
        camera.position.lerp(desiredCameraPos, cameraLerp);
    }

    camera.lookAt(smoothedTarget);
}
