"use client";
import { useEffect, useRef, useState } from "react";

export function useWebSocket(url: string) {
    const socketRef = useRef<WebSocket | null>(null);
    const [lastMessage, setLastMessage] = useState<string | null>(null);

    useEffect(() => {
        socketRef.current = new WebSocket(url);

        socketRef.current.onmessage = (event) => {
            setLastMessage(event.data);
        };

        return () => {
            socketRef.current?.close();
        };
    }, [url]);

    const sendMessage = (msg: string) => {
        socketRef.current?.send(msg);
    };

    return { sendMessage, lastMessage, socket: socketRef.current };
}
