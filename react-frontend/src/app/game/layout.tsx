import type {Metadata} from "next";
import React from "react";

export const metadata: Metadata = {
    title: "GameField",
    description: "Game - Room -  ",
};

export default function NestedLayout({
                                         children,
                                     }: {
    children: React.ReactNode;
}) {
    return (
        <>
            {children}
        </>
    );
}

