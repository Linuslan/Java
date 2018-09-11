#include <stdlib.h>
#include <string.h>
 
#include "jvmti.h"
#include "jni.h"
#include "jni_md.h"
 
void JNICALL
MyClassFileLoadHook(
    jvmtiEnv *jvmti_env,
    JNIEnv* jni_env,
    jclass class_being_redefined,
    jobject loader,
    const char* name,
    jobject protection_domain,
    jint class_data_len,
    const unsigned char* class_data,
    jint* new_class_data_len,
    unsigned char** new_class_data
)
{
    *new_class_data_len = class_data_len;
    jvmti_env->Allocate(class_data_len, new_class_data);
 
    unsigned char* my_data = *new_class_data;
	int k = 10;
    if(name&&(strncmp(name,"com/saleoa",10)==0)){
    	printf("\n%s", name);
        for (int i = 0; i < class_data_len; ++i)
        {
            my_data[i] = class_data[i] - k;
        }
    }else{
        for (int i = 0; i < class_data_len; ++i)
        {
            my_data[i] = class_data[i];
        }
    }
}

JNIEXPORT jint JNICALL
Agent_OnLoad(
    JavaVM *vm,
    char *options,
    void *reserved
)
{
	printf("enter agent_onload");
    jvmtiEnv *jvmti;
    //Create the JVM TI environment(jvmti)
    jint ret = vm->GetEnv((void **)&jvmti, JVMTI_VERSION);
    if(JNI_OK!=ret)
    {
        printf("ERROR: Unable to access JVMTI!\n");
        return ret;
    }
 
    jvmtiCapabilities capabilities;
    (void)memset(&capabilities,0, sizeof(capabilities));
 
    capabilities.can_generate_all_class_hook_events   = 1;
    capabilities.can_tag_objects                      = 1;
    capabilities.can_generate_object_free_events      = 1;
    capabilities.can_get_source_file_name             = 1;
    capabilities.can_get_line_numbers                 = 1;
    capabilities.can_generate_vm_object_alloc_events  = 1;
 
    jvmtiError error = jvmti->AddCapabilities(&capabilities);
    if(JVMTI_ERROR_NONE!=error)
    {
        printf("ERROR: Unable to AddCapabilities JVMTI!\n");
        return error;
    }
 
    jvmtiEventCallbacks callbacks;
    (void)memset(&callbacks,0, sizeof(callbacks));
 
    callbacks.ClassFileLoadHook = &MyClassFileLoadHook;
    error = jvmti->SetEventCallbacks(&callbacks, sizeof(callbacks));
    if(JVMTI_ERROR_NONE!=error){
        printf("ERROR: Unable to SetEventCallbacks JVMTI!\n");
        return error;
    }
    
    error = jvmti->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_CLASS_FILE_LOAD_HOOK, NULL);
    if(JVMTI_ERROR_NONE!=error){
        printf("ERROR: Unable to SetEventNotificationMode JVMTI!\n");
        return error;
    }
 
    return JNI_OK;
}