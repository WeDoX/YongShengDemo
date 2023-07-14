#include <jni.h>

#include <sys/file.h>
#include <unistd.h>
#include <stdlib.h>

#include <android/log.h>

#define TAG		"YongSheng"
#define LOGI(...)	__android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGD(...)	__android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGW(...)	__android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define	LOGE(...)	__android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

#define	DAEMON_CALLBACK_NAME		"haveProcessDead" //NativeLib的Java成员方法,用于回调

int lock_file(char *lock_file_path) {
    LOGE("start try to lock file >> %s %d", lock_file_path, getpid());
    int lockFileDescriptor = open(lock_file_path, O_RDONLY);
    if (lockFileDescriptor == -1) {
        LOGE("start try to lock file >> %s, lockFileDescriptor=-1", lock_file_path);
        lockFileDescriptor = open(lock_file_path, O_CREAT, S_IRUSR);
    }
    int lockRet = flock(lockFileDescriptor, LOCK_EX);
    if (lockRet == -1) {
        LOGE("lock file failed >> %s %d", lock_file_path, getpid());
        return 0;
    } else {
        LOGE("lock file success >> %s %d", lock_file_path, getpid());
        return 1;
    }
}

void create_file_if_not_exist(char *path) {
    FILE *fp = fopen(path, "ab+");
    if (fp) {
        fclose(fp);
    }
}

void java_callback(JNIEnv *env, jobject jobj, char *method_name) {
    jclass cls = env->GetObjectClass(jobj);
    jmethodID cb_method = env->GetMethodID(cls, method_name, "()V");
    env->CallVoidMethod(jobj, cb_method);
}



extern "C" JNIEXPORT void JNICALL
Java_com_onedream_yongshengdemo_yongsheng_YongShengNativeLib_createFileIfNotExist(
        JNIEnv* env,
        jobject /* this */,
        jstring filePath){
    char *path = (char *) env->GetStringUTFChars( filePath, 0);
    create_file_if_not_exist(path);
}

extern "C" JNIEXPORT void JNICALL
Java_com_onedream_yongshengdemo_yongsheng_YongShengNativeLib_obserFile(
        JNIEnv* env,
        jobject jobj,
        jstring filePath){
    char *path = (char *) env->GetStringUTFChars( filePath, 0);
    //
    int lock_status = lock_file(path);
    if (lock_status) {
        LOGE("Have process be killed !! Observe in pid %d",getpid());
        java_callback(env, jobj, DAEMON_CALLBACK_NAME);
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_onedream_yongshengdemo_yongsheng_YongShengNativeLib_lockFile(
        JNIEnv* env,
        jobject jobj,
        jstring filePath){
    //
    char *path = (char *) env->GetStringUTFChars( filePath, 0);
    //
    int lock_status = lock_file(path);
}


