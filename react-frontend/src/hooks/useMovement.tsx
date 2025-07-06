import { useEffect, useState } from "react";


const useMovement = () => {
    const [movement, setMovement] = useState({ x: 0, z: 0 });
  
    useEffect(() => {
      const handleKeyDown = (e: KeyboardEvent) => {
        setMovement((prev) => {
          const newMovement = { ...prev };
          if (e.key === "w") newMovement.z = -1; 
          if (e.key === "s") newMovement.z = 1; 
          if (e.key === "a") newMovement.x = -1; 
          if (e.key === "d") newMovement.x = 1;  
          return newMovement;
        });
      };
  
      const handleKeyUp = (e: KeyboardEvent) => {
        setMovement((prev) => {
          const newMovement = { ...prev };
          if (e.key === "w" || e.key === "s") newMovement.z = 0;
          if (e.key === "a" || e.key === "d") newMovement.x = 0;
          return newMovement;
        });
      };
  
      window.addEventListener("keydown", handleKeyDown);
      window.addEventListener("keyup", handleKeyUp);
  
      return () => {
        window.removeEventListener("keydown", handleKeyDown);
        window.removeEventListener("keyup", handleKeyUp);
      };
    }, []);
  
    return movement;
  };

  export default useMovement;