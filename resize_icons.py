import os
from PIL import Image

image_path = r"C:\Users\Dell\.gemini\antigravity\brain\96f1a0d9-f804-4a16-89b5-0276f39c422b\stark_wolf_logo_1789891600504.jpg"
res_dir = r"X:\CODE APK\CultCode\app\src\main\res"

sizes = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192
}

img = Image.open(image_path)

for folder, size in sizes.items():
    folder_path = os.path.join(res_dir, folder)
    os.makedirs(folder_path, exist_ok=True)
    
    # Save standard icon
    resized = img.resize((size, size), Image.Resampling.LANCZOS)
    resized.save(os.path.join(folder_path, "ic_launcher.png"), format="PNG")
    
    # Save round icon
    # Since it's a square black logo, round icon can just be the same for now, or scaled slightly
    # Let's just duplicate it as ic_launcher_round.png to be safe.
    resized.save(os.path.join(folder_path, "ic_launcher_round.png"), format="PNG")

print("Icons generated successfully!")
