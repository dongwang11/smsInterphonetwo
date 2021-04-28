LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_LDLIBS :=-llog

LOCAL_MODULE    := libspeex
LOCAL_CFLAGS = -DFIXED_POINT -DUSE_KISS_FFT -DEXPORT="" -UHAVE_CONFIG_H
LOCAL_C_INCLUDES := $(LOCAL_PATH)/include

LOCAL_SRC_FILES := speex_jni.cpp \
./libspeex/bits.c \
./libspeex/buffer.c \
./libspeex/cb_search.c \
./libspeex/exc_10_16_table.c \
./libspeex/exc_10_32_table.c \
./libspeex/exc_20_32_table.c \
./libspeex/exc_5_256_table.c \
./libspeex/exc_5_64_table.c \
./libspeex/exc_8_128_table.c \
./libspeex/fftwrap.c \
./libspeex/filterbank.c \
./libspeex/filters.c \
./libspeex/gain_table.c \
./libspeex/gain_table_lbr.c \
./libspeex/hexc_10_32_table.c \
./libspeex/hexc_table.c \
./libspeex/high_lsp_tables.c \
./libspeex/jitter.c \
./libspeex/kiss_fft.c \
./libspeex/kiss_fftr.c \
./libspeex/lpc.c \
./libspeex/lsp.c \
./libspeex/lsp_tables_nb.c \
./libspeex/ltp.c \
./libspeex/mdf.c \
./libspeex/modes.c \
./libspeex/modes_wb.c \
./libspeex/nb_celp.c \
./libspeex/preprocess.c \
./libspeex/quant_lsp.c \
./libspeex/resample.c \
./libspeex/sb_celp.c \
./libspeex/scal.c \
./libspeex/smallft.c \
./libspeex/speex.c \
./libspeex/speex_callbacks.c \
./libspeex/speex_header.c \
./libspeex/stereo.c \
./libspeex/vbr.c \
./libspeex/vq.c \
./libspeex/window.c \
./wedrtc/complex_bit_reverse.c \
./wedrtc/analog_agc.c \
./wedrtc/complex_fft.c \
./wedrtc/copy_set_operations.c \
./wedrtc/cross_correlation.c \
./wedrtc/digital_agc.c \
./wedrtc/division_operations.c \
./wedrtc/dot_product_with_scale.c \
./wedrtc/downsample_fast.c \
./wedrtc/energy.c \
./wedrtc/fft4g.c \
./wedrtc/get_scaling_square.c \
./wedrtc/min_max_operations.c \
./wedrtc/noise_suppression.c \
./wedrtc/noise_suppression_x.c \
./wedrtc/ns_core.c \
./wedrtc/nsx_core.c \
./wedrtc/nsx_core_c.c \
./wedrtc/nsx_core_neon_offsets.c \
./wedrtc/real_fft.c \
./wedrtc/resample.c \
./wedrtc/resample_48khz.c \
./wedrtc/resample_by_2.c \
./wedrtc/resample_by_2_internal.c \
./wedrtc/resample_by_2_mips.c \
./wedrtc/resample_fractional.c \
./wedrtc/ring_buffer.c \
./wedrtc/spl_init.c \
./wedrtc/spl_sqrt.c \
./wedrtc/spl_sqrt_floor.c \
./wedrtc/splitting_filter.c \
./wedrtc/vector_scaling_operations.c \


include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_LDLIBS :=-llog

LOCAL_MODULE    := libaes
LOCAL_C_INCLUDES := $(LOCAL_PATH)/libaes
LOCAL_SRC_FILES :=  ./libaes/JNIEncrypt.c \
./libaes/soft_AES.c

include $(BUILD_SHARED_LIBRARY)