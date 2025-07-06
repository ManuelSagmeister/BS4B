import {KeyboardControlsEntry} from "@react-three/drei";

const controlMap: KeyboardControlsEntry<MyKeyboardControls>[] = [
    {name: 'forward', keys: ['ArrowUp', 'KeyW']},
    {name: 'back', keys: ['ArrowDown', 'KeyS']},
    {name: 'left', keys: ['ArrowLeft', 'KeyA']},
    {name: 'right', keys: ['ArrowRight', 'KeyD']},
];

export default controlMap;


export const MyKeyboardControls = {
    forward: 'forward',
    back: 'back',
    left: 'left',
    right: 'right',
} as const;

export type MyKeyboardControls = keyof typeof MyKeyboardControls;