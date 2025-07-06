"use client"
import { useThree } from "@react-three/fiber";
import { useEffect, useState } from "react";
import { Vector3, Raycaster, Vector2 } from "three";

export default function useMouse() {
  const [mouseWorldPosition, setMouseWorldPosition] = useState(new Vector3());
  const [clicked, setClicked] = useState(false);
  const { gl, camera } = useThree();


  useEffect(() => {
    const raycaster = new Raycaster();
    const mouse = new Vector2();

    const handleMouseMove = (event: MouseEvent) => {
      mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
      mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;

      raycaster.setFromCamera(mouse, camera);

      // Assume ground plane at y = 0
      const planeY = 0;
      const direction = raycaster.ray.direction;
      const origin = raycaster.ray.origin;

      const t = (planeY - origin.y) / direction.y;
      const point = origin.clone().add(direction.multiplyScalar(t));
      setMouseWorldPosition(point);
    };

    const handleClick = (e: MouseEvent) => {
      // DEBUG: Draw red dot where clicked
      // const marker = document.createElement("div");
      // marker.style.position = "absolute";
      // marker.style.left = `${e.clientX}px`;
      // marker.style.top = `${e.clientY}px`;
      // marker.style.width = "10px";
      // marker.style.height = "10px";
      // marker.style.backgroundColor = "red";
      // marker.style.borderRadius = "50%";
      // marker.style.zIndex = "9999";
      // marker.style.pointerEvents = "none";
      // document.body.appendChild(marker);
      // setTimeout(() => marker.remove(), 500);

      console.log("CLICKED________________", e);
      setClicked(true);
    };

    gl.domElement.addEventListener("mousemove", handleMouseMove);
    gl.domElement.addEventListener("click", handleClick);

    return () => {
      gl.domElement.removeEventListener("mousemove", handleMouseMove);
      gl.domElement.removeEventListener("click", handleClick);
    };
  }, [gl, camera]);

  // Reset clicked after one animation frame
  useEffect(() => {
    if (clicked) {
      const id = setTimeout(() => setClicked(false), 50);
      return () => clearTimeout(id);
    }
  }, [clicked]);

  return { mouseWorldPosition, clicked };
}
