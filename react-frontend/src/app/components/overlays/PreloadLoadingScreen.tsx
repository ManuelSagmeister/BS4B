"use client";

import "@/styles/preload.css";

export default function PreloadLoadingScreen() {
    return (
        <div className="preload-screen">
            <div className="preload-overlay">
                <h1 className="preload-title">Loading the Wild West...</h1>
                <p className="preload-subtitle">Saddling up your horses and lighting the campfire 🔥</p>
                <div className="preload-spinner"></div>
            </div>
        </div>
    );
}
