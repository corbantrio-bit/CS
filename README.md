# CorbanSwitch — Android App

A brutal, simple screen-time kill switch for Android.

---

## HOW TO BUILD THIS APP (Free — No Computer Needed)

### Option A — Using GitHub + EAS (Recommended)

1. Go to github.com and create a free account
2. Create a new repository called "CorbanSwitch"
3. Upload all these files to the repository
4. Go to expo.dev, create a free account
5. Connect your GitHub repo
6. Trigger a build — it compiles your APK in the cloud
7. Download the APK and install on your Android phone

### Option B — Using Android Studio (If You Have a Computer)

1. Install Android Studio from developer.android.com
2. Open this folder as a project
3. Click Build > Build APK
4. Find the APK in app/build/outputs/apk/
5. Transfer to your phone and install

---

## PERMISSIONS THIS APP USES

- Device Administrator — blocks navigation during lockout
- Accessibility Service — intercepts home/back/recents buttons
- Exact Alarm — triggers blackout at precise bedtime
- Foreground Service — keeps kill switch alive in background
- Boot Receiver — restores schedule after phone restart

---

## FIRST LAUNCH

On first launch the app will ask you to:
1. Enable Device Administrator
2. Enable Accessibility Service
3. Allow exact alarms

All three are required for the full lockout to work.

---

Built with Kotlin + Jetpack Compose
Package: com.corbanswitch.app
