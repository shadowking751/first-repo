import base64
import os

# Create directories if they don't exist
os.makedirs('src/main/resources/assets/gunmod/textures/item', exist_ok=True)
os.makedirs('src/main/resources/assets/gunmod/textures/entity', exist_ok=True)

# Gun texture (64x64)
gun_base64 = "iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAA7ElEQVR4nO3ZwQrCMBCG4ZbWqifxKHrxWPQkHsOj6Ek8iUfxKJ7Eo3gSj+JRPG9SLIbS0jSB+QvzwQvJbHZ2d3Z2d2t3d0/jLEmSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIkSZIk/fP+BdjOHKqNH9V9AAAAAElFTkSuQmCC"

# Bullet texture (16x16)
bullet_base64 = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAK0lEQVR4nGNgGAWjYBSMglEwCkbBKBgFo2AUjIJRMApGwSgYBaNgFIwCAIsyBfKm8hPSAAAAAElFTkSuQmCC"

# Decode and save gun texture
with open('src/main/resources/assets/gunmod/textures/item/gun.png', 'wb') as f:
    f.write(base64.b64decode(gun_base64))
    print("✓ gun.png created successfully!")

# Decode and save bullet texture
with open('src/main/resources/assets/gunmod/textures/entity/bullet.png', 'wb') as f:
    f.write(base64.b64decode(bullet_base64))
    print("✓ bullet.png created successfully!")

print("\nTextures created in:")
print("- src/main/resources/assets/gunmod/textures/item/gun.png")
print("- src/main/resources/assets/gunmod/textures/entity/bullet.png")
