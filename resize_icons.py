import os
from PIL import Image

image_path = r"C:\Users\Dell\.gemini\antigravity\brain\96f1a0d9-f804-4a16-89b5-0276f39c422b\uc_brutalist_logo_1789892852638.jpg"
res_dir = r"X:\CODE APK\CultCode\app\src\main\res"

sizes = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192
}

img = Image.open(image_path)
# Let's crop the grey background slightly to get the pure black square!
# The image is 1024x1024. Let's crop from 100,100 to 924,924 roughly.
width, height = img.size
crop_box = (int(width*0.1), int(height*0.1), int(width*0.9), int(height*0.9))
img = img.crop(crop_box)

for folder, size in sizes.items():
    folder_path = os.path.join(res_dir, folder)
    os.makedirs(folder_path, exist_ok=True)
    
    resized = img.resize((size, size), Image.Resampling.LANCZOS)
    resized.save(os.path.join(folder_path, "ic_launcher.png"), format="PNG")
    resized.save(os.path.join(folder_path, "ic_launcher_round.png"), format="PNG")

print("Icons generated and cropped successfully!")
