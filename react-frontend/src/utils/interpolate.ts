// utils/interpolate.ts

export function lerp(a: number, b: number, t: number): number {
    return a + (b - a) * t;
}

// Angle-aware interpolation (shortest path)
export function lerpAngle(a: number, b: number, t: number): number {
    const delta = ((b - a + Math.PI) % (2 * Math.PI)) - Math.PI;
    return a + delta * t;
}
