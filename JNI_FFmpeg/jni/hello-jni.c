/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>
#include <android/log.h>
#include <ffmpeg/libavcodec/avcodec.h>
#include <ffmpeg/libavformat/avformat.h>
#include <ffmpeg/libswscale/swscale.h>
#include <string.h>

#define LOG_TAG    "***************************lixingyun*****************" // ������Զ����LOG�ı�ʶ
#undef LOG // ȡ��Ĭ�ϵ�LOG
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__) // ����LOG����
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__) // ����LOG����
#define LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__) // ����LOG����
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__) // ����LOG����
#define LOGF(...)  __android_log_print(ANDROID_LOG_FATAL,LOG_TAG,__VA_ARGS__) // ����LOG����
/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/HelloJni/HelloJni.java
 */
JNIEXPORT jstring Java_com_example_hellojni_HelloJni_stringFromJNI(JNIEnv* env,
		jobject thiz) {
	char str[25];
	sprintf(str, "%d", avcodec_version());

	return (*env)->NewStringUTF(env, str);
	// return (*env)->NewStringUTF(env, "Hello from JNI !");0
}

JNIEXPORT jint Java_com_example_hellojni_HelloJni_getVoice(JNIEnv* env,
		jobject thiz) {
	int flag = 0;
	AVPacket aacpkt;
	aacpkt.data = NULL;
	aacpkt.size = 0;

	const char *filename = "file:/sdcard/test.mp3";
	av_init_packet(&aacpkt);
	av_register_all(); //ע�����пɽ�������

	AVFormatContext *pInFmtCtx = NULL; //�ļ���ʽ
	AVCodecContext *pInCodecCtx = NULL; //�����ʽ

	pInFmtCtx = avformat_alloc_context();

	if (av_open_input_file(&pInFmtCtx, filename, NULL, 0, NULL) != 0) //��ȡ�ļ���ʽ
		LOGE("av_open_input_file error\n");
	if (av_find_stream_info(pInFmtCtx) < 0) //��ȡ�ļ�������Ƶ������Ϣ
		LOGE("av_find_stream_info error\n");
	unsigned int j;
	// Find the first audio stream

	int audioStream = -1;
	// Dump information about file onto standard error
	dump_format(pInFmtCtx, 0, filename, 0);

	//��FormatdContext�����ҵ���Ӧ����Ӧ�ı������ͣ�����CODEC_TYPE_AUDIO���ʾ����Ƶ
	for (j = 0; j < pInFmtCtx->nb_streams; j++) //�ҵ���Ƶ��Ӧ��stream
		if (pInFmtCtx->streams[j]->codec->codec_type == CODEC_TYPE_AUDIO) {
			audioStream = j;
			break;
		}
	if (audioStream == -1) {
		printf("input file has no audio stream\n");
		return 0; // Didn't find a audio stream

	}
	LOGE("audio stream num: %d\n", audioStream);

	pInCodecCtx = pInFmtCtx->streams[audioStream]->codec; //��Ƶ�ı���������
	AVCodec *pInCodec = NULL;
	/* FILE *file3 = fopen("codec_private_data_size.txt","w");
	 for(int i = 0; i <200000; i++)
	 {
	 pInCodec = avcodec_find_decoder((CodecID)i);
	 if (pInCodec!=NULL)
	 {
	 fprintf(file3,"%s %d\n",pInCodec->name,pInCodec->priv_data_size );
	 }
	 }
	 fclose(file3);
	 system("pause");
	 */
	pInCodec = avcodec_find_decoder(pInCodecCtx->codec_id); //���ݱ���ID�ҵ����ڽ���Ľṹ��
	if (pInCodec == NULL) {
		printf("error no Codec found\n");
		return -1; // Codec not found
	}

	//ʹ��test�Ĵ���pInCodecCtxҲ������ɽ��룬���Կ���ֻҪ��ȡ���¼�����Ҫ��Ϣ�Ϳ���ʵ�ֽ�����ز���
	AVCodecContext *test = avcodec_alloc_context();
	test->bit_rate = pInCodecCtx->bit_rate; //�ز�����
	test->sample_rate = pInCodecCtx->sample_rate; //�ز�����
	test->channels = pInCodecCtx->channels; //�ز�����
	test->extradata = pInCodecCtx->extradata; //���������
	test->extradata_size = pInCodecCtx->extradata_size; //�������Ҫ
	test->codec_type = CODEC_TYPE_AUDIO; //����Ҫ
	test->block_align = pInCodecCtx->block_align; //��Ҫ

	if (avcodec_open(test, pInCodec) < 0) //�����߽���Ա�������Ľ��뺯���е���pInCodec�еĶ�Ӧ���뺯��
			{
		printf("error avcodec_open failed.\n");
		return -1; // Could not open codec

	}

	if (avcodec_open(pInCodecCtx, pInCodec) < 0) {
		printf("error avcodec_open failed.\n");
		return -1; // Could not open codec

	}

	static AVPacket packet;

	LOGI(" bit_rate = %d \r\n", pInCodecCtx->bit_rate);
	LOGI(" sample_rate = %d \r\n", pInCodecCtx->sample_rate);
	LOGI(" channels = %d \r\n", pInCodecCtx->channels);
	LOGI(" code_name = %s \r\n", pInCodecCtx->codec->name);
	//LOGI("extra data size: %d :data%x %x %x %x\n",pInCodecCtx->extradata_size,pInCodecCtx->extradata[0]
	// ,pInCodecCtx->extradata[1],pInCodecCtx->extradata[2],pInCodecCtx->extradata[3]);
	LOGI(" block_align = %d\n", pInCodecCtx->block_align);
	/********************************************************/

	return flag;

}

