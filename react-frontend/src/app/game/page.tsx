"use client";

import {  KeyboardControls} from "@react-three/drei";
import controlMap from "@/types/keyboard-controls";
import { useWebSocket } from "@/network/useWebSocket";
import { GAME_SOCKET_URL } from "@/config";
import {GameLogic} from "@/app/components/world/GameLogic";

export default function Game() {
    const {sendMessage, lastMessage} = useWebSocket(GAME_SOCKET_URL);
    return (
            <KeyboardControls map={controlMap}>
            <GameLogic sendMessage={sendMessage} lastMessage={lastMessage}/>
        </KeyboardControls>
    );
}
