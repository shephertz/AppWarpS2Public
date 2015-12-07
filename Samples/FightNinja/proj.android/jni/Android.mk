LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

$(call import-add-path,$(LOCAL_PATH)/../../cocos2d)
$(call import-add-path,$(LOCAL_PATH)/../../cocos2d/external)
$(call import-add-path,$(LOCAL_PATH)/../../cocos2d/cocos)

LOCAL_MODULE := cocos2dcpp_shared

LOCAL_MODULE_FILENAME := libcocos2dcpp

LOCAL_SRC_FILES := hellocpp/main.cpp \
                   ../../Classes/AppDelegate.cpp \
                   ../../Classes/HelloWorldScene.cpp \
                   ../../Classes/Player.cpp \
                   ../../Classes/StartGameLayer.cpp \
                   ../../AppWarpX/appwarp.cpp \
                   ../../AppWarpX/appwarp_extended.cpp \
                   ../../AppWarpX/requests.cpp \
                   ../../AppWarpX/socket.cpp \
                   ../../AppWarpX/utilities.cpp \
                   ../../AppWarpX/cJSON.c

LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../Classes \
                    $(LOCAL_PATH)/../../AppWarpX

LOCAL_STATIC_LIBRARIES := cocos2dx_static

include $(BUILD_SHARED_LIBRARY)

$(call import-module,.)