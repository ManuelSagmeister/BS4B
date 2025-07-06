"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useGLTF } from "@react-three/drei";
import PreloadLoadingScreen from "@/app/components/overlays/PreloadLoadingScreen";

// preload all relevant models
const preloadAssets = async () => {
    useGLTF.preload('/models/Bandit_Player.glb');
    useGLTF.preload('/models/CanyonClashFrontend.glb');
    useGLTF.preload('/models/Cowboy_One.glb');
    useGLTF.preload('/models/GoldMineFrontend.glb');
    useGLTF.preload('/models/LobbyScene.glb');

    // simulate loading time if needed
    await new Promise((res) => setTimeout(res, 1000));
};

export default function PreloadPage() {
    const router = useRouter();

    useEffect(() => {
        const load = async () => {
            await preloadAssets();
            router.push("/lobby");
        };

        load();
    }, [router]);

    return <PreloadLoadingScreen />;
}
