## What is Ruta Engine?

Ruta Engine is a 3D game engine written in Java using the LWJGL (Lightweight Java Game Library). It’s designed for simple first-person games, supporting .obj model loading, collisions, entities, level switching, and game saving.

## The engine supports:

    Loading models with multiple materials (.obj + .mtl + image textures)

    Static and dynamic entities (e.g., player, enemies, objects)

    JSON-based level descriptions

    Saving and loading game state

    Simple GUI (e.g., player HUD)

## How to run the project

    Clone the repository

    Navigate to the project folder

    Run the test game com.keras1n.test.Launcher. The TestGame contains logic that utilizes all engine components.

## Loading 3D Models

Ruta Engine supports loading static 3D models exclusively in the .obj format, accompanied by an .mtl file that defines materials and texture paths (note: in the .mtl file, texture paths must be correct and without leading slashes).
Placement of models and textures

    All models must be in the folder: src/main/resources/models

    All textures (e.g., .jpg, .png) must be in the folder: ./textures

    Textures must not be located in any other folder, or loading will fail

### Exporting from Blender

When exporting a model from Blender as .obj, you must check the option:
✔️ Triangulate Faces (Blender: “Triangulate mesh”)

Otherwise, the model will not load correctly (the engine expects triangulated meshes).
In the code:

You just need to add entities in the file src/main/resources/levels/level.json
and provide the path to the .obj file.
```
{
      "type": "Object",
      "model": "/models/markX279.obj",
      "position": [-4, -1, 14],
      "rotation": [0, 200, 10],
      "scale": 1
}
```

## Creating the Game World

In the game, everything is an Entity type. Each entity has:

    A 3D model (MultiMaterialModel)

    A position in the world (Vector3f)

    Rotation (Vector3f)

    Scale (float)

Levels are described in JSON format and saved in the folder src/main/resources/levels/.

## Configuration Constants (com.keras1n.core.utils.Constants)

The engine allows adjusting game behavior via public constants in the Constants class, located in com.keras1n.core.utils.

### Window settings:
- TITLE – window title
- WINDOW_MIN_WIDTH, WINDOW_MIN_HEIGHT – minimum window size

### Player properties:
- CAMERA_MOVE_SPEED – player movement speed
- CAMERA_MOVE_SPEED_SPRINT – sprinting speed
- MOUSE_SENSITIVITY – mouse sensitivity
- GRAVITY – gravity affecting the player
- JUMP_POWER – jump strength

### Game balance:
- DEFAULT_ENEMY_HEALTH – default enemy health
- DEFAULT_ENEMY_DAMAGE – default damage dealt by enemies

**These constants can be adjusted directly in the Constants class to change game behavior without modifying other code.
Custom Entities and Extensions**

**Each entity in the game extends the base Entity class. It contains the model, position, rotation, scale, size, and collision flag.**
## Basic entity types:

    Entity – static or generic object

    PickupItem – an object the player can pick up (e.g., health pack, teleport)

    Teleport – a special pickup that activates upon meeting conditions (e.g., owning a crystal)

## How entities work

Each entity has a type (getType()), which determines its behavior in the engine.

Pickups can override the onPickup(Player p) method for custom logic.

An entity can have collisions (hasCollision) or be pass-through.

## How to create a custom entity (e.g., Teleport)

    Extend PickupItem or Entity

    Implement the onPickup(Player) method

    Register the type via getType() — it must match the type in level.json

    Optionally, add update logic using the update(deltaTime) method

Example: Teleport is a pickup that rotates and activates only if the player has a crystal. It can display a message and change the game state.
