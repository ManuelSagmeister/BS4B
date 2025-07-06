"use client";

import React, { useState } from "react";
import "@/styles/login.css";
import useLoginPlayer from "@/hooks/database/useLoginPlayer";

export default function Login() {
    const [playerName, setPlayerName] = useState("");
    const { loginPlayer, loading, error } = useLoginPlayer();

    const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        await loginPlayer(playerName);
    };

    return (
        <div className="login-root">
            <div className="login-card">
                <h1 className="login-title">Showdown</h1>
                <form onSubmit={handleLogin} className="login-form">
                    <label htmlFor="playername" className="login-label">
                        Spielername
                    </label>
                    <input
                        id="playername"
                        type="text"
                        placeholder="Enter a name..."
                        className="login-input"
                        value={playerName}
                        onChange={(e) => setPlayerName(e.target.value)}
                        disabled={loading}
                    />
                    {error && <div className="login-error">{error}</div>}
                    <button
                        type="submit"
                        className="login-button"
                        disabled={loading}
                    >
                        {loading ? "Lädt..." : "Weiter"}
                    </button>
                </form>
            </div>
        </div>
    );
}
