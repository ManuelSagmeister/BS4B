// app/not-found.tsx (or pages/404.tsx)
import Link from 'next/link';
import '@/styles/notFound.css';

export default function NotFound() {
    return (
        <div className="notfound-root">
            <div className="notfound-poster">
                <div className="notfound-poster-title">WANTED</div>
                <div className="notfound-poster-subtitle">Page Not Found</div>
                <div className="notfound-poster-avatar">
                    <span role="img" aria-label="Cowboy">🤠</span>
                </div>
                <div className="notfound-poster-name">404 - Outlaw</div>
                <div className="notfound-poster-tagline">
                    This page done rode off into the sunset.<br />
                    Try heading back to safer territory!
                </div>
                <Link href="/" className="notfound-action-btn">
                    <span>Ride Home</span>
                </Link>
            </div>
        </div>
    );
}
