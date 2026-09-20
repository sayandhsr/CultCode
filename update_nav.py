import re
import os

nav_path = r"app\src\main\java\com\unsulliedcode\Navigation.kt"
with open(nav_path, "r", encoding="utf-8") as f:
    content = f.read()

# Replace specific ones we already built
content = content.replace(
    'entry<Settings> { StubScreen("Settings", onBack = goBack) }',
    'entry<Settings> { com.unsulliedcode.ui.settings.SettingsSubScreen("Settings", onBack = goBack) }'
)
content = content.replace(
    'entry<SettingsTheme> { StubScreen("SettingsTheme", onBack = goBack) }',
    'entry<SettingsTheme> { com.unsulliedcode.ui.settings.SettingsSubScreen("Theme Settings", onBack = goBack) }'
)
content = content.replace(
    'entry<SettingsAccount> { StubScreen("SettingsAccount", onBack = goBack) }',
    'entry<SettingsAccount> { com.unsulliedcode.ui.settings.SettingsSubScreen("Account", onBack = goBack) }'
)
content = content.replace(
    'entry<SettingsNotifications> { StubScreen("SettingsNotifications", onBack = goBack) }',
    'entry<SettingsNotifications> { com.unsulliedcode.ui.settings.SettingsSubScreen("Notifications", onBack = goBack) }'
)
content = content.replace(
    'entry<SettingsStorage> { StubScreen("SettingsStorage", onBack = goBack) }',
    'entry<SettingsStorage> { com.unsulliedcode.ui.settings.SettingsSubScreen("Storage", onBack = goBack) }'
)
content = content.replace(
    'entry<SettingsAbout> { StubScreen("SettingsAbout", onBack = goBack) }',
    'entry<SettingsAbout> { com.unsulliedcode.ui.settings.SettingsSubScreen("About", onBack = goBack) }'
)

content = content.replace(
    'entry<ArenaMatchmaking> { StubScreen("ArenaMatchmaking", onBack = goBack) }',
    'entry<ArenaMatchmaking> { com.unsulliedcode.ui.arena.ArenaMatchmakingScreen(onNavigate = { backStack.add(it) }, onBack = goBack) }'
)
content = content.replace(
    'entry<ArenaBattle> { StubScreen("ArenaBattle", onBack = goBack) }',
    'entry<ArenaBattle> { com.unsulliedcode.ui.arena.ArenaMatchmakingScreen(onNavigate = { backStack.add(it) }, onBack = goBack) }'
)
content = content.replace(
    'entry<ArenaResult> { StubScreen("ArenaResult", onBack = goBack) }',
    'entry<ArenaResult> { com.unsulliedcode.ui.arena.ArenaMatchmakingScreen(onNavigate = { backStack.add(it) }, onBack = goBack) }'
)
content = content.replace(
    'entry<ArenaLeaderboard> { StubScreen("ArenaLeaderboard", onBack = goBack) }',
    'entry<ArenaLeaderboard> { com.unsulliedcode.ui.arena.ArenaMatchmakingScreen(onNavigate = { backStack.add(it) }, onBack = goBack) }'
)

content = content.replace(
    'entry<LearnDashboard> { StubScreen("LearnDashboard", onBack = goBack) }',
    'entry<LearnDashboard> { com.unsulliedcode.ui.home.LearnDashboardScreen(onBack = goBack) }'
)

content = content.replace(
    'entry<GamificationBadges> { StubScreen("GamificationBadges", onBack = goBack) }',
    'entry<GamificationBadges> { com.unsulliedcode.ui.gamification.GamificationBadgesScreen(onBack = goBack) }'
)
content = content.replace(
    'entry<GamificationStreak> { StubScreen("GamificationStreak", onBack = goBack) }',
    'entry<GamificationStreak> { com.unsulliedcode.ui.gamification.GamificationBadgesScreen(onBack = goBack) }'
)
content = content.replace(
    'entry<GamificationRewards> { StubScreen("GamificationRewards", onBack = goBack) }',
    'entry<GamificationRewards> { com.unsulliedcode.ui.gamification.GamificationBadgesScreen(onBack = goBack) }'
)

content = content.replace(
    'entry<PracticeEditor> { StubScreen("PracticeEditor", onBack = goBack) }',
    'entry<PracticeEditor> { com.unsulliedcode.ui.practice.PracticeEditorScreen(onBack = goBack) }'
)
content = content.replace(
    'entry<PracticeResult> { StubScreen("PracticeResult", onBack = goBack) }',
    'entry<PracticeResult> { com.unsulliedcode.ui.practice.PracticeEditorScreen(onBack = goBack) }'
)

content = content.replace(
    'entry<IdeSandbox> { StubScreen("IdeSandbox", onBack = goBack) }',
    'entry<IdeSandbox> { com.unsulliedcode.ui.ide.IdeSandboxScreen(onBack = goBack) }'
)
content = content.replace(
    'entry<IdeJupyterMode> { StubScreen("IdeJupyterMode", onBack = goBack) }',
    'entry<IdeJupyterMode> { com.unsulliedcode.ui.ide.IdeSandboxScreen(onBack = goBack) }'
)
content = content.replace(
    'entry<IdeTerminalMode> { StubScreen("IdeTerminalMode", onBack = goBack) }',
    'entry<IdeTerminalMode> { com.unsulliedcode.ui.ide.IdeSandboxScreen(onBack = goBack) }'
)

content = content.replace(
    'entry<GitSimulatorLog> { StubScreen("GitSimulatorLog", onBack = goBack) }',
    'entry<GitSimulatorLog> { com.unsulliedcode.ui.git.GitSimulatorScreen(onBack = goBack) }'
)
content = content.replace(
    'entry<GitSimulatorCommit> { StubScreen("GitSimulatorCommit", onBack = goBack) }',
    'entry<GitSimulatorCommit> { com.unsulliedcode.ui.git.GitSimulatorScreen(onBack = goBack) }'
)
content = content.replace(
    'entry<GitSimulatorMerge> { StubScreen("GitSimulatorMerge", onBack = goBack) }',
    'entry<GitSimulatorMerge> { com.unsulliedcode.ui.git.GitSimulatorScreen(onBack = goBack) }'
)

# For all remaining ones, route to generic screen temporarily, but don't use StubScreen
def replace_stub(m):
    key = m.group(1)
    return f'entry<{key}> {{ com.unsulliedcode.ui.settings.SettingsSubScreen("{key}", onBack = goBack) }}'

content = re.sub(r'entry<([A-Za-z0-9_]+)> \{ StubScreen\("[^"]+", onBack = goBack\) \}', replace_stub, content)

with open(nav_path, "w", encoding="utf-8") as f:
    f.write(content)
