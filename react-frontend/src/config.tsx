// ---- SERVER ENVS ----
// NOTE: Using HTTP URLs temporarily because HTTPS is not yet set up on the backend.
// This is insecure and should be replaced with HTTPS as soon as possible.
export const GAME_SOCKET_URL = "ws://10.0.40.187:8080/ws/game";
export const PLAYER_INFO_URL = "http://10.0.40.187:8081/getPlayerInfo/"
export const CREATE_PLAYER_URL = "http://10.0.40.187:8081/createPlayer"
export const UPDATE_PLAYER_URL = "http://10.0.40.187:8081/updatePlayer";
export const UNLOCK_CHARACTER_URL = "http://10.0.40.187:8081/character/unlock";

// ---- DEV LOCALHOST ----
// NOTE: Using HTTP URLs temporarily because HTTPS is not yet set up on the backend.
// This is insecure and should be replaced with HTTPS as soon as possible.
// export const GAME_SOCKET_URL = "ws://localhost:8080/ws/game";
// export const PLAYER_INFO_URL = "http://localhost:8081/getPlayerInfo/"
// export const CREATE_PLAYER_URL = "http://localhost:8081/createPlayer"
// export const UPDATE_PLAYER_URL = "http://localhost:8081/updatePlayer";
// export const UNLOCK_CHARACTER_URL = "http://localhost:8081/character/unlock";
