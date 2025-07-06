import { useState } from "react";
import { useRouter } from "next/navigation";
import { usePlayerStore, PlayerDB } from "@/stores/usePlayerStore";
import { CREATE_PLAYER_URL } from "@/config";

export default function useLoginPlayer() {
    const router = useRouter();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const setPlayer = usePlayerStore((state) => state.setPlayer);

    const loginPlayer = async (playerName: string) => {
        setError(null);
        if (!playerName.trim()) {
            setError("Bitte gib einen Spielernamen ein.");
            return false;
        }

        setLoading(true);
        try {
            const response = await fetch(CREATE_PLAYER_URL, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ playerName }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                setError("Fehler beim Login: " + errorData.message);
                setLoading(false);
                return false;
            }

            const player: PlayerDB = await response.json();
            setPlayer(player);
            router.push("/lobby");
            setLoading(false);
            return true;
        } catch (err) {
            setError("Verbindung zum Server fehlgeschlagen. " + err);
            setLoading(false);
            return false;
        }
    };

    return { loginPlayer, loading, error };
}
