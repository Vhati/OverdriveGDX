The build process for this multi-module project is automated by Maven.
  http://maven.apache.org/
  http://docs.codehaus.org/display/MAVENUSER/Getting+Started+with+Maven


The Overdrive engine doesn't come with images and such. You'll need to do
the following to transform the original FTL's vanilla resources into
something the engine can understand.

  1) Run the maven command to generate "desktop" distribution archives.
  2) Extract one of the archives to a folder (e.g., Overdrive_Test).
  3) Run the packer setup tool to create Overdrive resources there.
  4) Then run the engine.


Maven Structure

Most of the directories here are individual projects, each containing their
own pom.xml. They each have an artifactId to identify themselves and to
depend on each other as if they were in a repository.

engine-core
  The source code for the game engine.

engine-desktop
  Borrows the "engine-core" files, and adds native libraries, and classes
  to bootstrap the engine on Windows/Mac/Linux.

engine-android
  As above, but for the Android platform.
  Note: That pom.xml hasn't been configured yet.

packer
  A setup tool to locate the original FTL's resources and preprocess them.

dist-desktop
  Collects the artifacts (compiled binaries) from other modules and
  packages them together with stock dirs/files as zip and a tar.gz
  archives for distribution.

  * It copies the "jar-with-dependencies" jars and renames them.

  * "dist-desktop/skel_exe/" contains materials to create Launch4j
    executables (not part of Maven).
    http://launch4j.sourceforge.net/index.html

dist-android
  As above, but just copies the "engine-android" jar.
  Never tried, so it may not be valid.

repo
  This is not a module. It houses library jars that aren't in normal
  Maven repositories.

shared-assets
  This is not a module. It's a folder for the modules to keep non-code
  files they have in common.

  * Anything in "shared-assets/engine-resources/" will appear at the root
    of "engine-desktop.jar" and "engine-android.jar".


The top-level pom.xml here declares them all as its modules. When
you run a maven command against the top-level project, the command is
selectively passed on to its modules.

Technically the top-level project has alternate lists of modules, called
profiles.
  desktop: Windows/Mac/Linux
           engine-core, engine-desktop, packer, dist-desktop

  android: Android
           engine-core, engine-android, dist-android

  default: A token profile that's used when no other is chosen.
           engine-core


Build Commands

mvn -P desktop clean package

  This will clean and compile all the modules in the "desktop" profile,
  create jars, and generate zip and tar.gz archives.
  (See "dist-desktop/target/" for those.)


mvn -P desktop -pl packer clean integration-test

  This will clean, compile, package (a jar), and test run "packer" only.


mvn -P desktop -pl engine-core,engine-desktop clean integration-test

  This will clean, compile, package (a jar), and test run only the engine.

  Note: This requires first setting an environment variable to tell the
  engine where to find resources (they're not in "engine-desktop/target/").

    Unix: export OVERDRIVE_APP_PATH=/.../Overdrive_Test
    Win:  SET OVERDRIVE_APP_PATH=C:\...\Overdrive_Test


Dependencies

libGDX 0.9.9 Nightly Snapshot
	http://libgdx.badlogicgames.com/index.html
  http://libgdx.badlogicgames.com/nightlies/docs/api/

BeanShell2
  http://code.google.com/p/beanshell2/
  For docs, see the GoogleCode Wiki and the original BeanShell site.



Here's a batch file that builds when double-clicked (edit the vars).
- - - -
@ECHO OFF
SETLOCAL

SET JAVA_HOME=D:\Apps\j2sdk1.6.0_45
SET M2_HOME=D:\Apps\Maven

SET M2=%M2_HOME%\bin
SET PATH=%M2%;%PATH%

CD /D "%~dp0"
CALL mvn -P desktop clean package

PAUSE
ENDLOCAL & EXIT /B
- - - -
