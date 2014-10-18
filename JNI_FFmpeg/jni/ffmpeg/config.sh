#!/bin/bash
export TMPDIR=D:/lxy/temp
PREBUILT=D:/lxy/Android/android-ndk-r4/build/prebuilt/windows/arm-eabi-4.4.0
PLATFORM=D:/lxy/Android/android-ndk-r4/build/platforms/android-8/arch-arm
#PREBUILT=d:/android-ndk-r4/build/prebuilt/windows/arm-eabi-4.4.0
#PLATFORM=d:/android-ndk-r4/build/platforms/android-8/arch-arm

#D:\temp\android
##D:\lxy\Android\android-ndk-r4
#D:\lxy\Android\android-ndk-r4\build\prebuilt\windows\arm-eabi-4.4.0
#
./configure --target-os=linux \
--arch=arm \
--enable-version3 \
 --enable-gpl \
 --enable-nonfree \
 --disable-stripping\
 --disable-ffmpeg\
 --disable-ffplay\
 --disable-ffserver\
 --disable-ffprobe\
 --disable-encoders\
 --disable-muxers\
 --disable-devices\
 --enable-protocols\
 --enable-protocol=file\
 --enable-avfilter \
 --enable-network \
 --disable-mpegaudio-hp \
 --disable-avdevice \
 --enable-cross-compile \
 --cc=$PREBUILT/bin/arm-eabi-gcc \
 --cross-prefix=$PREBUILT/bin/arm-eabi- \
 --nm=$PREBUILT/bin/arm-eabi-nm --extra-cflags="-fPIC -DANDROID" --disable-asm --enable-neon --enable-armv5te --extra-ldflags="-Wl,-T,$PREBUILT/arm-eabi/lib/ldscripts/armelf.x -W1,-rpath-link=$PLATFORM/usr/lib -L$PLATFORM/usr/lib -nostdlib $PREBUILT/lib/gcc/arm-eabi/4.4.0/crtbegin.o $PREBUILT/lib/gcc/arm-eabi/4.4.0/crtend.o -lc -lm -ldl"
