#include <string.h>

#include <stdio.h>

int main(int argc, char *argv[]) {
	AVFormatContext *pFormatCtx = NULL;
	AVCodecContext *pCodecCtx = NULL;
	AVCodec *aaccodec = NULL;
	AVPacket aacpkt;
	int len = 0;
	int size = 0;
	unsigned char *outbuf = NULL;
// unsigned char inbuf[INBUF_SIZE*5],*inbuf_ptr;
	int outsize;
	int newpacket = 0;
	int audio_stream = -1;

	int out_ape_fp = -1;
	int in_ape_fp = -1;

	double start_time = 0;
	double duration = 0;

	FILE *out_fp;

	const unsigned char start_code[4] = { 0x00, 0x00, 0x00, 0x01 };

	out_fp = fopen("out.dat", "wb");
// memset(inbuf, 0, sizeof(inbuf));

// inbuf_ptr = inbuf;
	aacpkt.data = NULL;
	aacpkt.size = 0;

	av_init_packet(&aacpkt);

	if (argc < 2) {
		printf("Usage: test <file>\n");
		exit(1);
	}
	// Register all formats and codecs
	av_register_all();

	// Open video file
	if (av_open_input_file(&pFormatCtx, argv[1], NULL, 0, NULL) != 0)
		return -1; // Couldn't open file

	// Retrieve stream information
	if (av_find_stream_info(pFormatCtx) < 0)
		return -1; // Couldn't find stream information

	// Dump information about file onto standard error
	dump_format(pFormatCtx, 0, argv[1], 0);

	for (int i = 0; i < pFormatCtx->nb_streams; i++) {
		if (pFormatCtx->streams[i]->codec->codec_type == CODEC_TYPE_AUDIO) {
			audio_stream = i;
			break;
		}
	}

	if (audio_stream == -1) {
		printf("audio not found!\n");
		return -1;
	} else {
		pCodecCtx = pFormatCtx->streams[audio_stream]->codec;
		apecodec = avcodec_find_decoder(pCodecCtx->codec_id);
		if (apecodec == NULL)
			return -1;
		if (avcodec_open(pCodecCtx, aaccodec) < 0)
			return -1;
	}

	outbuf = (unsigned char *) malloc(AVCODEC_MAX_AUDIO_FRAME_SIZE * 2);
	outsize = AVCODEC_MAX_AUDIO_FRAME_SIZE * 2;

	while (av_read_frame(pFormatCtx, &aacpkt) >= 0) {
		if (aacpkt.stream_index == audio_stream) {
			int declen = 0;
			size = aacpkt.size;
			while (aacpkt.size > 0) {
				outsize = AVCODEC_MAX_AUDIO_FRAME_SIZE * 2;
				memset(outbuf, 0, outsize);
				len = avcodec_decode_audio3(pCodecCtx, (int16_t *) outbuf,
						&outsize, &aacpkt);
				if (len < 0) {
					printf("avcodec_decode_audio3 failed!\n");
					break;
				}
				if (outsize > 0) {
					fwrite(outbuf, 1, outsize, out_fp);
					printf("write %d bytes\n", outsize);
				}
				declen += len;
				if (declen == size) {
					av_free_packet(&aacpkt);
					printf("packet decoded succeed!\n");
					break;
				} else if (declen < size) {
					aacpkt.size -= len;
					aacpkt.data += len;
				} else {
					printf("decode error!\n");
					break;
				}

			}

		}
	}

	fclose(out_fp);
	// Close the codec
	avcodec_close(pCodecCtx);

	// Close the video file
	av_close_input_file(pFormatCtx);

	return 0;

}
