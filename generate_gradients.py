import colorsys

def hex_to_hls(hex_str):
    hex_str = hex_str.lstrip('#')
    if len(hex_str) == 8:
        # AARRGGBB to RRGGBBAA? No, usually gradients are RGB
        hex_str = hex_str[2:]
    r, g, b = tuple(int(hex_str[i:i+2], 16) for i in (0, 2, 4))
    return colorsys.rgb_to_hls(r/255.0, g/255.0, b/255.0)

def hls_to_hex(hls):
    r, g, b = colorsys.hls_to_rgb(hls[0], hls[1], hls[2])
    return "#{:02x}{:02x}{:02x}".format(int(r*255), int(g*255), int(b*255)).upper()

def adjust(hex_str):
    h, l, s = hex_to_hls(hex_str)
    # Saturation reduced 18%, Lightness reduced 8% (absolute or relative? Usually absolute percentage points if it says 18%, but let's do absolute. If s=0.8, -0.18 = 0.62)
    s = max(0, s - 0.18)
    l = max(0, l - 0.08)
    return hls_to_hex((h, l, s))

gradients = [
    ("G01_Nebula", "#7C5CFF", "#FF6B9D"),
    ("G02_Aurora", "#00E5A0", "#4D9FFF"),
    ("G03_Inferno", "#FF6B35", "#FF2E63"),
    ("G04_Cyber", "#00F0A0", "#0575E6"),
    ("G05_Amethyst", "#8E2DE2", "#4A00E0"),
    ("G06_Solar", "#FFC93C", "#FF6B35"),
    ("G07_Abyss", "#0F2027", "#2C5364"),
    ("G08_Bloom", "#FF4D8D", "#C792EA"),
    ("G09_Glacier", "#4D9FFF", "#B8E6FF"),
    ("G10_Toxic", "#A5E075", "#00E5A0"),
    ("G11_Ember", "#FF5A5F", "#FFB020"),
    ("G12_Void", "#1A1A22", "#07070A")
]

out = ["package com.unsulliedcode.ui.theme\n\nimport androidx.compose.ui.graphics.Color\nimport androidx.compose.ui.graphics.Brush\nimport androidx.compose.runtime.staticCompositionLocalOf\n\ndata class AppGradients("]
for g in gradients:
    out.append(f"    val {g[0]}: Brush,")
out[-1] = out[-1][:-1]
out.append(")\n\nval ObsidianGradients = AppGradients(")

for g in gradients:
    out.append(f"    {g[0]} = Brush.linearGradient(listOf(Color(0xFF{g[1][1:]}), Color(0xFF{g[2][1:]}))),")
out[-1] = out[-1][:-1]
out.append(")\n\nval PorcelainGradients = AppGradients(")

for g in gradients:
    if g[0] == "G12_Void":
        out.append(f"    {g[0]} = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFAFAFC))),")
    else:
        out.append(f"    {g[0]} = Brush.linearGradient(listOf(Color(0xFF{adjust(g[1])[1:]}), Color(0xFF{adjust(g[2])[1:]}))),")
out[-1] = out[-1][:-1]
out.append(")\n\nval LocalAppGradients = staticCompositionLocalOf { ObsidianGradients }\n")

with open(r"app\src\main\java\com\unsulliedcode\ui\theme\Gradients.kt", "w") as f:
    f.write("\n".join(out))

