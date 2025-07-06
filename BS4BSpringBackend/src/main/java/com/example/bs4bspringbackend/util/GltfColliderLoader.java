package com.example.bs4bspringbackend.util;

import com.example.bs4bspringbackend.model.Physics.Collider;
import com.example.bs4bspringbackend.model.Props.Prop;
import com.example.bs4bspringbackend.model.Props.GasPropStatic;
import com.example.bs4bspringbackend.model.Props.HealPropStatic;
import com.example.bs4bspringbackend.network.server.BoxColliderDTO;
import com.example.bs4bspringbackend.model.Physics.BoxCollider;
import com.example.bs4bspringbackend.model.Physics.Vector2;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.*;

public class GltfColliderLoader {

    // Private constructor to prevent instantiation
    private GltfColliderLoader() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    private static final Logger logger = LogManager.getLogger(GltfColliderLoader.class);

    /**
     * Loads all colliders from the resource file, but only returns those that are NOT special props
     * (i.e., their id does not start with "gas_zone" or "heal_zone").
     */
    public static List<Collider> loadColliders(String resourcePath) {
        try (InputStream is = GltfColliderLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                logger.error("Resource not found: {}", resourcePath);
                return Collections.emptyList();
            }
            ObjectMapper mapper = new ObjectMapper();
            List<BoxColliderDTO> dtos = mapper.readValue(is,
                    mapper.getTypeFactory().constructCollectionType(List.class, BoxColliderDTO.class));
            // Only include colliders that are not special props
            return dtos.stream()
                    .filter(dto -> !isGasZone(dto.id) && !isHealZone(dto.id))
                    .map(dto -> new BoxCollider(dto.id, new Vector2(dto.x, dto.y), dto.width, dto.height))
                    .map(collider -> (Collider) collider)
                    .toList();
        } catch (Exception e) {
            logger.error("Failed to load colliders from resource '{}': {}", resourcePath, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Loads only special props (gas, heal, etc.) from the resource file by matching id patterns.
     */
    public static List<Prop> loadProps(String resourcePath) {
        try (InputStream is = GltfColliderLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                logger.error("Resource not found: {}", resourcePath);
                return Collections.emptyList();
            }
            ObjectMapper mapper = new ObjectMapper();
            List<BoxColliderDTO> dtos = mapper.readValue(is,
                    mapper.getTypeFactory().constructCollectionType(List.class, BoxColliderDTO.class));
            List<Prop> props = new ArrayList<>();
            for (BoxColliderDTO dto : dtos) {
                BoxCollider collider = new BoxCollider(dto.id, new Vector2(dto.x, dto.y), dto.width, dto.height );
                if (isGasZone(dto.id)) {
                    props.add(new GasPropStatic(collider));
                } else if (isHealZone(dto.id)) {
                    props.add(new HealPropStatic(collider));
                }
                // Add more prop types here as needed, e.g.:
            }
            return props;
        } catch (Exception e) {
            logger.error("Failed to load props from resource '{}': {}", resourcePath, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    // Helper methods for matching by id/name
    private static boolean isGasZone(String id) {
        return id != null && id.toLowerCase().startsWith("gas_zone");
    }
    private static boolean isHealZone(String id) {
        return id != null && id.toLowerCase().startsWith("heal_zone");
    }
    // Add more helpers for other prop types if needed
}
