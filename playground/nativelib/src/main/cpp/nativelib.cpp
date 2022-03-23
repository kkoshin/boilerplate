#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
// 也就是路径全称
Java_com_example_nativelib_NativeLibWrapper_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}