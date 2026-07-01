# DRAGONIC WORM BUILDER

> Template-Based Termux Project Generator — Android App

**Developed by Dev Leonore**

---

## 📱 About

DRAGONIC WORM BUILDER is a professional Android app that generates Termux project structures from pre-built templates.

- ❌ No AI
- ❌ No Database  
- ❌ No Login
- ❌ No Internet required (after install)
- ✅ 100% Template-based
- ✅ Generates real ZIP files
- ✅ Saves to Downloads or Share

---

## 🚀 Build APK via GitHub Actions (tanpa Android Studio)

### Step 1 — Buat repo di GitHub
1. Buka [github.com/new](https://github.com/new)
2. Beri nama repo: `dragonic-worm-builder`
3. Pilih **Private** atau Public
4. Klik **Create repository**

### Step 2 — Upload project ke repo
Di HP, buka GitHub app atau browser, upload semua folder project ini ke repo.

Atau dari PC/laptop via terminal:
```bash
git init
git add .
git commit -m "Initial commit: DRAGONIC WORM BUILDER"
git remote add origin https://github.com/USERNAME/dragonic-worm-builder.git
git push -u origin main
```

### Step 3 — GitHub Actions build otomatis
Setiap kali push ke branch `main`, Actions akan otomatis:
1. Build Debug APK
2. Build Release APK (unsigned)
3. Upload APK sebagai **Artifact**

### Step 4 — Download APK
1. Buka repo di GitHub
2. Klik tab **Actions**
3. Klik workflow run terbaru
4. Scroll ke bawah ke bagian **Artifacts**
5. Download **DragonicWormBuilder-Debug** atau **DragonicWormBuilder-Release**
6. Extract ZIP → dapat file `.apk`
7. Install di HP (aktifkan "Install from unknown sources")

---

## 📁 Project Structure

```
dragonic-worm-builder/
├── .github/
│   └── workflows/
│       └── build.yml          ← GitHub Actions workflow
├── app/
│   ├── src/main/
│   │   ├── java/com/devleonore/dragonicworm/
│   │   │   ├── model/         ← Data models
│   │   │   ├── template/      ← Template engine (no AI)
│   │   │   ├── ui/            ← Fragments (Dashboard, Create, etc.)
│   │   │   └── util/          ← ZIP builder, file exporter
│   │   ├── res/               ← Layouts, drawables, colors
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/wrapper/
│   └── gradle-wrapper.properties
├── build.gradle.kts
├── settings.gradle.kts
└── gradlew
```

---

## 🛠️ Tech Stack

- Language: **Kotlin**
- Min SDK: **API 29 (Android 10+)**
- UI: **XML Layouts + ViewBinding**
- ZIP: **java.util.zip** (no external lib)
- File Save: **MediaStore API** (scoped storage)
- Build: **Gradle 8.7 + AGP 8.5.2**

---

## 👤 Developer

**Dev Leonore**

*DRAGONIC WORM BUILDER © 2026 — All rights reserved*
